/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector;

import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import lombok.Data;
import net.neferett.webviewsinjector.summoner.Summoner;
import net.neferett.webviewsinjector.settings.Settings;

import java.util.HashMap;
import java.util.Map;

@Data
public class WebViewCreator {

    private final WebView webView;
    private final HashMap<Settings, Object> settings;

    /**
     *
     * WebView creator wrapping Android's WebView instance and allowing settings management easier
     *
     * @param context Activity context to create WebView
     * @param settings HashMap of settings and boolean to define active or not
     */
    public WebViewCreator(final Context context, final boolean mobile, HashMap<Settings, Object> settings) {
        this.webView = new WebView(context);
        this.settings = settings;

        if (mobile)
            this.webView.getSettings().setUserAgentString("Android");

        this.applySettings();
    }

    /**
     * Applying instance settings thanks to enum callback returning methods to invoke
     */
    private void applySettings() {
        for (Map.Entry<Settings, Object> entries : settings.entrySet()) {
            Summoner summoner;
            try {
                summoner = entries.getKey().getSettingsAction().action(this.webView);

                summoner.invoke(entries.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param url Url to load inside WebView
     */
    void load(String url) {
        this.webView.loadUrl(url);
    }

    /**
     *
     * @param client Client to link with WebView
     */
    public void applyClient(WebViewClient client) {
        this.webView.setWebViewClient(client);
    }
}
