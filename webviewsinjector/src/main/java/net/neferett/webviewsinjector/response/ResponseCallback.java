/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector.response;

import lombok.Data;

@Data
public abstract class ResponseCallback implements Runnable, ResponseInterface{

    private ResponseInjector responseInjector;
    private boolean sent = false;

    /**
     *
     * @param responseEnum Response to check
     * @param data Response data
     */
    @Override
    public void preLaunchOption(ResponseEnum responseEnum, String data) {
        this.sent = true;
        this.getResponse(responseEnum, data);
    }

    @Override
    public void run() {
        if (this.responseInjector == null)
            return;

        if (this.sent)
            return;

        this.responseInjector.getResponse(this);
    }
}
