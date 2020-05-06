/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector.services.travelling;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import lombok.SneakyThrows;
import net.neferett.webviewsinjector.R;
import net.neferett.webviewsinjector.javascript.ElementValue;
import net.neferett.webviewsinjector.login.StepEnum;
import net.neferett.webviewsinjector.login.TypesAuthElement;
import net.neferett.webviewsinjector.response.ResponseEnum;
import net.neferett.webviewsinjector.services.custom.CustomSelfInput;

import java.util.HashMap;

public class TrainlineService extends CustomSelfInput {

    TrainlineService(final Context context) {
        super("Trainline", StepEnum.ONE_STEP, context,
                "https://www.thetrainline.com/my-account/login?redirectTo=%2Fmy-account%2Fbookings", 2, R.drawable.trainline, false);
    }

    public void waitAndAction(int delay, Runnable runnable) {
        CustomSelfInput instance = TrainlineService.this;
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        ((Activity)instance.getContext()).runOnUiThread(runnable);
                    }
                },
                delay * 1000
        );
    }

    @SneakyThrows
    public void signIn() {
        this.waitAndAction(5, () -> TrainlineService.this.getWebInjector().injectJavascript(constructJavaScriptMethod(TypesAuthElement.BUTTON_LOGIN), null, null));
        this.waitAndAction(10, () -> TrainlineService.this.getWebInjector().injectJavascript(constructJavaScriptMethod(TypesAuthElement.BUTTON_LOGIN), null, null));
    }

    @SneakyThrows
    public void beforeSignIn() {
        injectingDataFromAuth(TypesAuthElement.USERNAME, (v) -> ((Activity)TrainlineService.this.getContext()).runOnUiThread(() -> {
            this.getWebview().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_TAB));
            this.webInjector.injectJavascript("document.querySelector('main').style.position = 'absolute'", null, null);
        }));

        injectingDataFromAuth(TypesAuthElement.PASSWORD, (v) ->
                ((Activity)TrainlineService.this.getContext()).runOnUiThread(() ->
                        this.getWebview().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_TAB))
                )
        );

        this.signIn();
    }

    @Override
    public void setupElements() {
        if (this.typesAuthElementObjectHashMap == null)
            this.typesAuthElementObjectHashMap = new HashMap<>();

        this.typesAuthElementObjectHashMap.put(
                TypesAuthElement.BUTTON_LOGIN, new ElementValue(
                        "querySelector", "button[data-test=login-form-submit]", false
                ));
    }

    @Override
    protected HashMap<ResponseEnum, String> setupTests() {
        return new HashMap<ResponseEnum, String>() {{
            put(ResponseEnum.FINISH_URL, "https://www.thetrainline.com/my-account/bookings");
            put(ResponseEnum.BAD_EMAIL, "document.getElementById('username-error')");
            put(ResponseEnum.BAD_PASSWORD, "document.getElementById('password-error')");
            put(ResponseEnum.SUCCESS, "document.getElementById('profile-menu-trigger--content')");
        }};
    }
}
