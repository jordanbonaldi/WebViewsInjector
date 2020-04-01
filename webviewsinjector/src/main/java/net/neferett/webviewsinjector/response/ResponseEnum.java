/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseEnum {

    BAD_PASSWORD("Bad password", true),
    BAD_EMAIL("Bad email", true),
    CAPTCHA("Need to fill captcha", true),
    SECOND_STEP_AUTH("Second step auth needed", true),
    CONFIRMATION_IDENTITY("Need to confirm your identity", true),
    SUCCESS("Successfully connected", true),
    PASSWORD_URL("Password URL", false),

    /* System will check if final URL is contained to current URL */
    FINISH_URL("Final URL", false),
    /* System will check if final url is strictly equals to current URL */
    FINAL_EQUALS_URL("Final Equals URL", false);

    /* Enum attribute */
        private final String name;
        private final boolean responseInjectorEnable;
    /* end */
}
