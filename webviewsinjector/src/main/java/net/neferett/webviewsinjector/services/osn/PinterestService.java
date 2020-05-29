/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector.services.osn;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import net.neferett.webviewsinjector.R;
import net.neferett.webviewsinjector.javascript.ElementValue;
import net.neferett.webviewsinjector.login.StepEnum;
import net.neferett.webviewsinjector.login.TypesAuthElement;
import net.neferett.webviewsinjector.response.ResponseEnum;
import net.neferett.webviewsinjector.services.custom.CustomSelfInput;
import net.neferett.webviewsinjector.services.travelling.AgodaService;

import java.util.HashMap;
import java.util.Objects;

public class PinterestService extends CustomSelfInput {

    /**
     *
     * @param context Main Activity Context
     */
    public PinterestService(Context context) {
        super("Pinterest", StepEnum.ONE_STEP, context, "https://www.pinterest.co.uk/login", 3, R.drawable.pinterest, false);
    }

    @Override
    public void setupElements() {
        if (this.typesAuthElementObjectHashMap == null)
            this.typesAuthElementObjectHashMap = new HashMap<>();

        this.typesAuthElementObjectHashMap.put(
                TypesAuthElement.BUTTON_LOGIN, new ElementValue(
                        "querySelector", "button[type=submit]", false
                ));
    }

    /**
     *
     * @return ResponseEnum and Linked JavaScript code
     */
    @Override
    protected HashMap<ResponseEnum, String> setupTests() {
        return new HashMap<ResponseEnum, String>(){{
            put(ResponseEnum.FINAL_EQUALS_URL, "https://www.pinterest.co.uk/");
            put(ResponseEnum.CAPTCHA, "document.getElementById('FunCaptcha')");
        }};
    }

    public void waitAndAction(int delay, Runnable runnable) {
        CustomSelfInput instance = PinterestService.this;
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
        this.waitAndAction(2, () -> {
            ((Activity) this.getContext()).runOnUiThread(() -> this.getWebview().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_TAB)));
            ((Activity) this.getContext()).runOnUiThread(() -> this.getWebview().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_TAB)));
            this.getWebview().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
        });
    }

    @Override
    public void beforeSignIn() {
        this.waitAndAction(5, () -> {
            for (int i = 0; i < 6; i++)
                ((Activity) this.getContext()).runOnUiThread(() -> this.getWebview().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_TAB)));
            this.waitAndAction(5, () -> {
                injectingDataFromAuth(TypesAuthElement.USERNAME, (v) -> ((Activity) this.getContext()).runOnUiThread(() -> this.getWebview().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_TAB))));
                injectingDataFromAuth(TypesAuthElement.PASSWORD, (v) -> ((Activity) this.getContext()).runOnUiThread(() -> this.getWebview().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_TAB))));

                this.signIn();
            });
        });
    }
}
