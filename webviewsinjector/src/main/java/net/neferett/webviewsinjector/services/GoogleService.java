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

public class GoogleService extends LoginService {

    /**
     *
     * @param context Main Activity Context
     */
    public GoogleService(final Context context) {
        super("Google", StepEnum.TWO_STEP, context, "https://accounts.google.com/signin/v2/identifier", 4,
                R.drawable.googlelogo, false);
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
                "getElementById", "identifierId", false
        ));

        this.step.addElement(TypesAuthElement.PASSWORD, new ElementValue(
                "getElementsByName", "password", true
        ));

        this.defineButton(TypesAuthElement.BUTTON_LOGIN, "identifierNext");
        this.defineButton(TypesAuthElement.BUTTON_PASSWORD, "passwordNext");
    }

    /**
     *
     * @return ResponseEnum and JavaScript Linked Code
     */
    @Override
    protected HashMap<ResponseEnum, String> setupTests() {
        return new HashMap<ResponseEnum, String>(){{
            put(ResponseEnum.FINISH_URL, "https://myaccount.google.com/?utm_source=sign_in_no_continue&pli=1");
            put(ResponseEnum.BAD_EMAIL, "document.getElementsByClassName('jibhHc')[0]");
            put(ResponseEnum.BAD_PASSWORD, "document.querySelector('div[jsname=\"B34EJ\"] span')");
            put(ResponseEnum.CONFIRMATION_IDENTITY, "document.getElementsByClassName('vxx8jf')[0]");
            put(ResponseEnum.CAPTCHA, "document.getElementsByClassName('o6cuMc')[0]");
            put(ResponseEnum.SUCCESS, "document.getElementsByClassName('x7WrMb')[0].innerHTML");
        }};
    }
}
