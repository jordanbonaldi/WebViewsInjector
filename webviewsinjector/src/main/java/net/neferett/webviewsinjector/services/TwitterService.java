/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector.services;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import net.neferett.webviewsinjector.R;
import net.neferett.webviewsinjector.javascript.ElementValue;
import net.neferett.webviewsinjector.login.StepEnum;
import net.neferett.webviewsinjector.login.TypesAuthElement;
import net.neferett.webviewsinjector.response.ResponseEnum;
import net.neferett.webviewsinjector.services.costum.CustomSelfInput;

import java.util.HashMap;

public class TwitterService extends CustomSelfInput {

    /**
     *
     * @param context Main Activity Context
     */
    public TwitterService(Context context) {
        super("Twitter", StepEnum.ONE_STEP, context, "https://mobile.twitter.com/login", 3, R.drawable.ic_twitter_logo_blue, false);
    }

    @Override
    public void setupElements() {
        if (this.typesAuthElementObjectHashMap == null)
            this.typesAuthElementObjectHashMap = new HashMap<>();

        this.typesAuthElementObjectHashMap.put(
                TypesAuthElement.BUTTON_LOGIN, new ElementValue(
                        "querySelector", "div[data-testid=\"LoginForm_Login_Button\"]]", false
                ));
    }

    /**
     *
     * @return ResponseEnum and Linked JavaScript code
     */
    @Override
    protected HashMap<ResponseEnum, String> setupTests() {
        return new HashMap<ResponseEnum, String>(){{
            put(ResponseEnum.FINISH_URL, "https://mobile.twitter.com/home");
            put(ResponseEnum.CAPTCHA, "document.getElementById('FunCaptcha')");
        }};
    }

    public void waitAndAction(int delay, Runnable runnable) {
        CustomSelfInput instance = TwitterService.this;
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

    @Override
    public void signIn() {
        this.waitAndAction(2, () -> this.getWebview().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER)));
    }

    @Override
    public void beforeSignIn() {
        this.waitAndAction(5, () -> {
            injectingDataFromAuth(TypesAuthElement.USERNAME, (v) -> ((Activity)TwitterService.this.getContext()).runOnUiThread(() -> this.getWebview().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_TAB))));
            injectingDataFromAuth(TypesAuthElement.PASSWORD, (v) -> ((Activity)TwitterService.this.getContext()).runOnUiThread(() -> this.getWebview().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_TAB))));

            this.signIn();
        });
    }
}
