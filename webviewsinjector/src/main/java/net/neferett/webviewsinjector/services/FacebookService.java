/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector.services;

import android.content.Context;
import net.neferett.webviewsinjector.R;
import net.neferett.webviewsinjector.javascript.ElementValue;
import net.neferett.webviewsinjector.login.StepEnum;
import net.neferett.webviewsinjector.login.TypesAuthElement;
import net.neferett.webviewsinjector.response.ResponseEnum;

import java.util.HashMap;

public class FacebookService extends LoginService {

    /**
     *
     * @param context Main Activity Context
     */
    public FacebookService(Context context) {
        super("Facebook", StepEnum.ONE_STEP, context, "https://m.facebook.com/", 3, R.drawable.ic_facebook, false);
    }

    /**
     *
     * @param element TypesAuth Button
     */
    private void defineButton(TypesAuthElement element) {
        this.step.addElement(element, new ElementValue(
                "querySelector", "button[name=\"login\"]", false
        ));
    }

    @Override
    public void setupElements() {
        this.step.addElement(TypesAuthElement.USERNAME, new ElementValue(
                "getElementById", "m_login_email", false
        ));
        this.step.addElement(TypesAuthElement.PASSWORD, new ElementValue(
                "querySelector", "input[name=\"pass\"]", false
        ));

        this.defineButton(TypesAuthElement.BUTTON_LOGIN);
        this.defineButton(TypesAuthElement.BUTTON_PASSWORD);
    }

    /**
     *
     * @return ResponseEnum and Linked JavaScript code
     */
    @Override
    protected HashMap<ResponseEnum, String> setupTests() {
        return new HashMap<ResponseEnum, String>(){{
            put(ResponseEnum.BAD_EMAIL, "document.getElementsByClassName('_652e')[0]");
            put(ResponseEnum.BAD_PASSWORD, "document.getElementsByClassName('_652e')[0]");
            put(ResponseEnum.CAPTCHA, "document.getElementById('FunCaptcha')");
            put(ResponseEnum.FINAL_EQUALS_URL, "https://m.facebook.com/login/save-device/?login_source=login#_=_");
        }};
    }
}
