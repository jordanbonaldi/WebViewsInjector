/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector.services.travelling;

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

import java.util.HashMap;

public class AgodaService extends CustomSelfInput {

    /**
     *
     * @param context Main Activity Context
     */
    public AgodaService(Context context) {
        super("Agoda", StepEnum.ONE_STEP, context, "https://www.agoda.com/en-gb/signin/email", 2, R.drawable.agoda, false);
    }

    @Override
    public void setupElements() {
        if (this.typesAuthElementObjectHashMap == null)
            this.typesAuthElementObjectHashMap = new HashMap<>();

    }

    /**
     *
     * @return ResponseEnum and Linked JavaScript code
     */
    @Override
    protected HashMap<ResponseEnum, String> setupTests() {
        return new HashMap<ResponseEnum, String>(){{
            put(ResponseEnum.FINAL_EQUALS_URL, "https://www.agoda.com/en-gb/");
            put(ResponseEnum.BAD_EMAIL, "document.querySelector('.SimpleMessage__show--J0VfQ')");
            put(ResponseEnum.CAPTCHA, "document.getElementById('FunCaptcha')");
        }};
    }

    @Override
    public void signIn() {
        this.waitAndAction(5, () -> ((Activity) AgodaService.this.getContext()).runOnUiThread(() ->
            injectJavaScript(
                    new ElementValue(
                            "querySelector", "button[type=submit]", false
                    ).clickValue(), null
            )));
    }

    @Override
    public void beforeSignIn() {
        this.waitAndAction(5, () -> {
            this.getWebview().performLongClick();
            for (int i = 0; i < 16; i++)
                ((Activity) AgodaService.this.getContext()).runOnUiThread(() -> this.getWebview().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_TAB)));

            this.waitAndAction(5, () -> {
                injectingDataFromAuth(TypesAuthElement.USERNAME, (v) -> ((Activity) AgodaService.this.getContext()).runOnUiThread(() -> this.getWebview().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_TAB))));
                injectingDataFromAuth(TypesAuthElement.PASSWORD, (v) -> ((Activity) AgodaService.this.getContext()).runOnUiThread(() -> this.getWebview().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_TAB))));

                this.signIn();
            });
        });
    }
}