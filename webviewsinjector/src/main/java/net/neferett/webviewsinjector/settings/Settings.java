/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector.settings;

import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.WebView;

import net.neferett.webviewsinjector.summoner.Summoner;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Settings {

    /**
     * Set JavaScript to <boolean>
     */
    JavaScript(instance -> {
        return new Summoner(instance.getSettings().getClass().getMethod("setJavaScriptEnabled", boolean.class), instance.getSettings());
    }),
    /**
     * Set DomStorage to <boolean>
     */
    DomStorage(instance -> {
        return new Summoner(instance.getSettings().getClass().getMethod("setDomStorageEnabled", boolean.class), instance.getSettings());
    }),
    /**
     * Clear cache and Cookie, clear cookie if step inside HashMap, but clear cache only on boolean
     */
    ClearCache(instance -> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            CookieManager.getInstance().removeAllCookies(null);

        instance.clearHistory();

        return new Summoner(instance.getClass().getMethod("clearCache", boolean.class), instance);
    });

    /* Enum attribute */
        private final SettingsAction settingsAction;
    /* end */
}
