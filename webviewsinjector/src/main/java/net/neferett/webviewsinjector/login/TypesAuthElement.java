/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TypesAuthElement {

    PASSWORD("password"),
    USERNAME("username"),
    BUTTON_LOGIN("button_login"),
    BUTTON_PASSWORD("button_password");

    /* Enum attribute */
        private final String name;
    /* end */
}
