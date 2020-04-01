/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector.javascript;

import lombok.Data;

@Data
public class ElementValue {

    /* Args Constructor */
        private final String methodName;
        private final String identifier;
        private final boolean takeFirst;
    /* end */

    /**
     *
     * Constructing JavaScript method with identifier
     *
     * @return String
     */
    private String getConstructedMethod() {
        return "document." + methodName + "('" + this.identifier + "')" + (this.takeFirst ? "[0]" : "");
    }

    /**
     *
     * @param value Value to set
     * @return String
     */
    public String changeValue(String value) {
        return this.getConstructedMethod() + ".value = '" + value + "'";
    }

    /**
     *
     * Click on element identified
     *
     * @return String
     */
    public String clickValue() {
        return this.getConstructedMethod() + ".click()";
    }
}
