/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector.services.costum;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import net.neferett.webviewsinjector.javascript.ElementValue;
import net.neferett.webviewsinjector.login.StepEnum;
import net.neferett.webviewsinjector.login.TypesAuthElement;
import net.neferett.webviewsinjector.response.ResponseCallback;
import net.neferett.webviewsinjector.response.ResponseEnum;
import net.neferett.webviewsinjector.services.LoginService;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Objects;

public abstract class CustomSelfInput extends LoginService {

    protected HashMap<TypesAuthElement, Object> typesAuthElementObjectHashMap;
    protected ResponseCallback responseCallback;

    /**
     *
     * Generic Automated connection without javascript
     *
     * @param name Service Name
     * @param stepEnum Login type
     * @param context Activity Context
     * @param url Url of the login page
     * @param delay Delay between entering input and login button click
     * @param drawableLogo Service Logo
     * @param mobile Loading WebPage in mobile or desktop version
     */
    public CustomSelfInput(String name, StepEnum stepEnum, Context context, String url, int delay, int drawableLogo, boolean mobile) {
        super(name, stepEnum, context, url, delay, drawableLogo, mobile);
    }

    public abstract void signIn();

    public abstract void beforeSignIn();

    /**
     *
     * @param element Element from TypeAuth to get
     * @param <T> Generic parameter to cast
     * @return T
     */
    private <T> T getObjectFromHash(TypesAuthElement element) {
        return (T) this.typesAuthElementObjectHashMap.get(element);
    }

    /**
     *
     * @param element Type of KeyEvent to inject into inputs
     * @param callback Callback return when input is injected
     */
    protected void injectingDataFromAuth(TypesAuthElement element, ValueCallback<Void> callback) {
        for (KeyEvent event : this.<KeyEvent []>getObjectFromHash(element))
            this.getWebViewCreator().getWebView().dispatchKeyEvent(event);
        if (callback != null) callback.onReceiveValue(null);
    }

    /**
     *
     * @param element JavaScript Wrapper ElementValue of the login button
     * @return String
     */
    public String constructJavaScriptMethod(TypesAuthElement element) {
        return this.<ElementValue>getObjectFromHash(element).clickValue();
    }

    /**
     *
     * @param responseEnum Response Enum to get the string of
     * @return String
     */
    private String getStringFromInjector(ResponseEnum responseEnum) {
        return CustomSelfInput.this.responseInjector.getResponses().get(responseEnum);
    }

    private WebViewClient createCustomClient() {
        CustomSelfInput instance = CustomSelfInput.this;

        return new WebViewClient() {

            @SneakyThrows
            private void launchSignIn() {
                instance.signIn();
            }

            @SneakyThrows
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (!url.equalsIgnoreCase(CustomSelfInput.this.url))
                    return;

                instance.beforeSignIn();
            }

            @Override
            public void doUpdateVisitedHistory(WebView webView1, String url, boolean isReload) {
                // We want here to build our response
                if (instance.responseInjector.getResponses().containsKey(ResponseEnum.PASSWORD_URL) &&
                        url.equalsIgnoreCase(instance.getStringFromInjector(ResponseEnum.PASSWORD_URL)))
                    this.launchSignIn();

                if (url.contains(instance.getStringFromInjector(ResponseEnum.FINISH_URL)))
                    instance.responseCallback.getResponse(ResponseEnum.SUCCESS, "Success");

                else if (instance.responseCallback != null)
                    ((Activity)instance.getContext()).runOnUiThread(responseCallback);
            }
        };
    }

    /**
     *
     * @param data Mapped Data
     * @return KeyEvent[]
     */
    private KeyEvent[] getMappedEvent(String data) {
        return KeyCharacterMap.load(KeyCharacterMap.VIRTUAL_KEYBOARD).getEvents(data.toCharArray());
    }

    /**
     *
     * @param email Email to map into keyboard events
     * @param password Password to map into keyboard events
     */
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

    /**
     *
     * @param email Email or Username to fill inside AutoLogin
     * @param password Password to fill inside AutoLogin
     * @param responseCallback CallBack with ResponseType when connection is success or false
     */
    @Override
    public void autoLogin(String email, String password, final ResponseCallback responseCallback) {
        this.load();
        this.setupKeyEvents(email, password);

        if (responseCallback != null)
            responseCallback.setResponseInjector(this.responseInjector);

        this.responseCallback = responseCallback;

        this.webViewCreator.applyClient(this.createCustomClient());
        new Handler().postDelayed(Objects.requireNonNull(responseCallback),
                ((delay * stepEnum.getMultiplicand())) * 1000);

        this.getWebInjector().load();
    }
}
