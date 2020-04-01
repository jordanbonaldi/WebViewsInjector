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

public class StravaService extends LoginService {

    /**
     *
     * @param context Main Activity Context
     */
    public StravaService(final Context context) {
        super("Strava", StepEnum.ONE_STEP, context, "https://www.strava.com/login", 4, R.drawable.strava, false);
    }

    /**
     *
     * @param element TypeAuth element
     * @param name Name of Element©©
     */
    private void defineButton(TypesAuthElement element, String name) {
        this.step.addElement(element, new ElementValue(
                "getElementById", name, false
        ));
    }

    @Override
    public void setupElements() {
        this.step.addElement(TypesAuthElement.USERNAME, new ElementValue(
                "getElementById", "email", false
        ));

        this.step.addElement(TypesAuthElement.PASSWORD, new ElementValue(
                "getElementById", "password", false
        ));

        this.defineButton(TypesAuthElement.BUTTON_LOGIN, "login-button");
        this.defineButton(TypesAuthElement.BUTTON_PASSWORD, "login-button");
    }

    /**
     *
     * @return ResponseEnum and JavaScript Linked Code
     */
    @Override
    protected HashMap<ResponseEnum, String> setupTests() {
        return new HashMap<ResponseEnum, String>(){{
            put(ResponseEnum.FINISH_URL, "https://www.strava.com/onboarding");
            put(ResponseEnum.BAD_EMAIL, "document.querySelector('.alert.error > .alert-message').innerHTML");
            put(ResponseEnum.BAD_PASSWORD, "document.querySelector('.alert.error > .alert-message').innerHTML");
            put(ResponseEnum.SUCCESS, "document.querySelector('div.athlete-name').innerHTML");
        }};
    }
}
