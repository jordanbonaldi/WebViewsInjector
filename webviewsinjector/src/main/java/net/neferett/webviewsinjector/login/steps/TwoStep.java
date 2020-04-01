/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector.login.steps;

import net.neferett.webviewsinjector.login.TypesAuthElement;

public class TwoStep extends Step{

    /**
     *
     * Promise Creator
     *
     * @param username Username or Email to fill in Login form
     * @param password Password to fill in Login form
     * @param delay Delay between fill and connection click button
     * @return String - Promise to inject with delay
     * @throws Exception Throwing when element is not defined
     */
    @Override
    public String construct(String username, String password, int delay) throws Exception {
        return  "Promise.all([" +
                this.stepPromise(TypesAuthElement.USERNAME,
                            TypesAuthElement.BUTTON_LOGIN, username, delay
                        ) +
                "]).then(() => Promise.all([\n" +
                    this.stepPromise(TypesAuthElement.PASSWORD, null, password, delay)
                +"])).then(() => "+ this.getElementByTypes(TypesAuthElement.BUTTON_PASSWORD).clickValue() +");";
    }

}
