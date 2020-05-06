/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector.services.travelling;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import net.neferett.webviewsinjector.R;
import net.neferett.webviewsinjector.javascript.ElementValue;
import net.neferett.webviewsinjector.login.StepEnum;
import net.neferett.webviewsinjector.login.TypesAuthElement;
import net.neferett.webviewsinjector.response.ResponseCallback;
import net.neferett.webviewsinjector.response.ResponseEnum;
import lombok.SneakyThrows;
import net.neferett.webviewsinjector.services.LoginService;

import java.util.HashMap;
import java.util.Objects;

public class BookingService extends LoginService {

    private HashMap<TypesAuthElement, Object> typesAuthElementObjectHashMap;

    private ResponseCallback responseCallback;

    BookingService(final Context context) {
        super("Booking", StepEnum.TWO_STEP, context,
                "https://account.booking.com/", 2, R.drawable.booking, false);
    }

    @Override
    public void setupElements() {
        if (this.typesAuthElementObjectHashMap == null)
            this.typesAuthElementObjectHashMap = new HashMap<>();

        this.typesAuthElementObjectHashMap.put(
                TypesAuthElement.BUTTON_LOGIN, new ElementValue(
                        "querySelector", "button[type=submit]", false
                ));
        this.typesAuthElementObjectHashMap.put(
                TypesAuthElement.BUTTON_PASSWORD, new ElementValue(
                        "querySelector", ".bui-button__text", false
                ));
    }

    private KeyEvent[] getMappedEvent(String data) {
        return KeyCharacterMap.load(KeyCharacterMap.VIRTUAL_KEYBOARD).getEvents(data.toCharArray());
    }

    private void setupKeyEvents(String email, String password) {
        this.typesAuthElementObjectHashMap.put(
                TypesAuthElement.USERNAME,
                this.getMappedEvent(email)
        );
        this.typesAuthElementObjectHashMap.put(
                TypesAuthElement.PASSWORD,
                this.getMappedEvent(password)
        );
    }

    private WebViewClient createBookingClient() {
        BookingService instance = BookingService.this;

        return new WebViewClient() {
            private <T> T getObjectFromHash(TypesAuthElement element) {
                return (T) instance.typesAuthElementObjectHashMap.get(element);
            }

            private void injectingDataFromAuth(TypesAuthElement element) {
                for (KeyEvent event : this.<KeyEvent []>getObjectFromHash(element))
                    instance.getWebViewCreator().getWebView().dispatchKeyEvent(event);
            }

            private String constructJavaScriptMethod(TypesAuthElement element) {
                return this.<ElementValue>getObjectFromHash(element).clickValue();
            }

            private String getStringFromInjector(ResponseEnum responseEnum) {
                return BookingService.this.responseInjector.getResponses().get(responseEnum);
            }

            @SneakyThrows
            private void launchSignIn() {
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                ((Activity)instance.getContext()).runOnUiThread(() ->
                                        injectingDataFromAuth(TypesAuthElement.PASSWORD));
                            }
                        },
                        1000
                );
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                ((Activity)instance.getContext()).runOnUiThread(() ->
                                        instance.webInjector.injectJavascript(constructJavaScriptMethod(TypesAuthElement.BUTTON_PASSWORD), null, null));
                            }
                        },
                        5000
                );
            }

            @SneakyThrows
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (!url.equalsIgnoreCase(BookingService.this.url))
                    return;

                // Injecting username at load
                injectingDataFromAuth(TypesAuthElement.USERNAME);
                // Clicking on next
                Thread.sleep(5000);
                instance.webInjector.injectJavascript(constructJavaScriptMethod(TypesAuthElement.BUTTON_LOGIN), null, null);
            }

            @Override
            public void doUpdateVisitedHistory(WebView webView1, String url, boolean isReload) {
                // We want here to build our response
                if (url.equalsIgnoreCase(this.getStringFromInjector(ResponseEnum.PASSWORD_URL)))
                    this.launchSignIn();
                else if (url.contains(this.getStringFromInjector(ResponseEnum.FINISH_URL)))
                    instance.responseCallback.getResponse(ResponseEnum.SUCCESS, "Success");
                else if (instance.responseCallback != null)
                    ((Activity)instance.getContext()).runOnUiThread(responseCallback);
            }
        };
    }

    @Override
    public void autoLogin(String email, String password, final ResponseCallback responseCallback) {
        this.load();
        this.setupKeyEvents(email, password);

        if (responseCallback != null)
            responseCallback.setResponseInjector(this.responseInjector);

        this.responseCallback = responseCallback;

        this.webViewCreator.applyClient(this.createBookingClient());
        new Handler().postDelayed(Objects.requireNonNull(responseCallback),
                ((delay * stepEnum.getMultiplicand())) * 1000);

        this.getWebInjector().load();
    }

    @Override
    protected HashMap<ResponseEnum, String> setupTests() {
        return new HashMap<ResponseEnum, String>() {{
            put(ResponseEnum.PASSWORD_URL, "https://account.booking.com/sign-in/password");
            put(ResponseEnum.FINISH_URL, "https://www.booking.com/index.html");
            put(ResponseEnum.BAD_EMAIL, "document.getElementById('username-error')");
            put(ResponseEnum.BAD_PASSWORD, "document.getElementById('password-error')");
            put(ResponseEnum.SUCCESS, "document.getElementById('profile-menu-trigger--content')");
        }};
    }
}
