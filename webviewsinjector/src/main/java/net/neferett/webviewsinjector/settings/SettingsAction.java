/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector.settings;

import android.webkit.WebView;

import net.neferett.webviewsinjector.summoner.Summoner;

public interface SettingsAction {
    /**
     *
     * @param instance WebView Instance to apply settings on.
     * @return Summoner
     */
    Summoner action(WebView instance) throws NoSuchMethodException;
}