/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector.javascript;

public class JSInjector {

    /**
     *
     * @param delay Delay to postponed promise
     * @param methods Different lines to execute
     * @return String - Promise
     */
    public String promiseCreator(int delay, String ... methods) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String m : methods)
            stringBuilder.append(m).append(";");

        return "new Promise((res) => {\n" +
                stringBuilder +
                "      setTimeout(res, " + delay * 1000 + ")\n" +
                "})";
    }

}
