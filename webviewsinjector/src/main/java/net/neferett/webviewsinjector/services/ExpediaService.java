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

public class ExpediaService extends LoginService {

    /**
     *
     * @param context Main Activity Context
     */
    public ExpediaService(Context context) {
        super("Expedia", StepEnum.ONE_STEP, context, "https://www.expedia.co.uk/user/signin", 3, R.drawable.expedia, false);
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
        this.defineValues(TypesAuthElement.USERNAME, "signin-loginid");
        this.defineValues(TypesAuthElement.PASSWORD, "signin-password");

        this.defineValues(TypesAuthElement.BUTTON_LOGIN, "submitButton");
        this.defineValues(TypesAuthElement.BUTTON_PASSWORD, "submitButton");
    }

    /**
     *
     * @return ResponseEnum and Linked JavaScript code
     */
    @Override
    protected HashMap<ResponseEnum, String> setupTests() {
        return new HashMap<ResponseEnum, String>(){{
            put(ResponseEnum.FINISH_URL, "https://www.expedia.co.uk/?rfrr=Account.Login.FullAccount.Success");
            put(ResponseEnum.BAD_EMAIL, "document.getElementById('userEmailidError')");
            put(ResponseEnum.BAD_PASSWORD, "document.getElementById('wrong-credentials-error-div')");
            put(ResponseEnum.CAPTCHA, "document.getElementById('recaptcha-token')");
            put(ResponseEnum.SUCCESS, "document.querySelector('#header-container').innerHTML");
        }};
    }
}
