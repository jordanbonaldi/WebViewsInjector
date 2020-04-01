/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector.summoner;

import lombok.Data;

import java.lang.reflect.Method;

@Data
public class Summoner {

    private final Method method;
    private final Object instance;

    /**
     *
     * @param params Parameters of summoned method
     * @throws Exception Throwing when summoning fails
     */
    public void invoke(Object ... params) throws Exception {
        this.method.invoke(this.instance, params);
    }
}
