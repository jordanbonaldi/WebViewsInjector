/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector.services.travelling;

import android.content.Context;
import net.neferett.webviewsinjector.R;
import net.neferett.webviewsinjector.javascript.ElementValue;
import net.neferett.webviewsinjector.login.StepEnum;
import net.neferett.webviewsinjector.login.TypesAuthElement;
import net.neferett.webviewsinjector.response.ResponseEnum;
import net.neferett.webviewsinjector.services.LoginService;

import java.util.HashMap;

public class HotelsComService extends LoginService {

    /**
     *
     * @param context Main Activity Context
     */
    public HotelsComService(Context context) {
        super("Hotels.com", StepEnum.ONE_STEP, context, "https://uk.hotels.com/profile/signin.html", 3, R.drawable.hotelscom, false);
    }

    /**
     *
     * @param element TypesAuth Button
     */
    private void defineButton(TypesAuthElement element) {
        this.step.addElement(element, new ElementValue(
                "querySelector", "button[type=\"submit\"]", false
        ));
    }

    /**
     *
     * @param element TypeAuth element
     * @param value Name of Element
     */
    private void defineValues(TypesAuthElement element, String value) {
        this.step.addElement(element, new ElementValue(
                "getElementById", value, false
        ));
    }

    @Override
    public void setupElements() {
        this.defineValues(TypesAuthElement.USERNAME, "sign-in-email");
        this.defineValues(TypesAuthElement.PASSWORD, "sign-in-password");

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
            put(ResponseEnum.FINISH_URL, "https://uk.hotels.com/profile/landing.html");
            put(ResponseEnum.BAD_EMAIL, "document.getElementsByClassName('msg-error-icon msg-big mb-spider')[0]");
            put(ResponseEnum.BAD_PASSWORD, "document.getElementsByClassName('msg-error-icon msg-big mb-spider')[0]");
            put(ResponseEnum.CAPTCHA, "document.getElementById('FunCaptcha')");
            put(ResponseEnum.SUCCESS, "document.querySelector('#item-member').innerHTML");
        }};
    }
}
