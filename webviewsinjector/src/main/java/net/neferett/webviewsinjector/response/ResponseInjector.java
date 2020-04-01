/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector.response;

import android.webkit.ValueCallback;
import net.neferett.webviewsinjector.WebInjector;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResponseInjector {

    /* Args constructor */
        private final WebInjector injector;
        private final HashMap<ResponseEnum, String> responses;
    /* end */

    /**
     *
     * @param responseInterface Response Interface to invoke callback when linked javascript code is different than null
     */
    void getResponse(final ResponseInterface responseInterface) {
        for (final Map.Entry<ResponseEnum, String> entry : this.responses.entrySet()) {
            if (!entry.getKey().isResponseInjectorEnable())
                continue;

            this.injector.injectJavascript(
                    entry.getValue(), null, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                            if (s.equalsIgnoreCase("null") ||
                                    s.equalsIgnoreCase("undefined"))
                                return;

                            responseInterface.preLaunchOption(entry.getKey(), s);
                        }
                    }
            );
        }
    }
}
