/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector;

import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import net.neferett.webviewsinjector.response.ResponseCallback;
import net.neferett.webviewsinjector.response.ResponseEnum;
import lombok.Data;

@Data
public class WebInjector {

    private final WebViewCreator webViewCreator;
    private final String url;

    private ValueCallback<String> callback;
    private boolean redirected = false;

    /**
     *
     * WebClient Creator
     *
     * @param inject JavaScript to inject
     * @param callback CallBack used when injection is done
     *
     * @return WebViewClient
     */
    private WebViewClient clientCreator(final String inject, final ResponseCallback callback) {
        return new WebViewClient() {
            @Override
            public void onPageFinished(final WebView view, String url) {
                if (redirected) return;

                redirected = true;

                super.onPageFinished(view, url);

                injectJavascript(inject, view, null);
            }

            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) { // WARNING CHANGE METHOD TO NON DEPRECATED
                String final_url = callback.getResponseInjector().getResponses().get(ResponseEnum.FINISH_URL);
                String final_equaling_url = callback.getResponseInjector().getResponses().get(ResponseEnum.FINAL_EQUALS_URL);

                if (final_url != null && final_url.contains(url) || final_equaling_url != null && final_equaling_url.equalsIgnoreCase(url)) { //WARNING IF BUG REPLACE BY EQUALS
                    callback.setSent(true);
                    callback.getResponse(ResponseEnum.SUCCESS, "Success");
                }
            }
        };
    }

    /**
     *
     * @param data JavaScript Response JSON
     */
    @JavascriptInterface
    public void promiseReceive(String data) {
        if (callback != null)
            this.callback.onReceiveValue(data);
    }

    /**
     * Enabling JSInterface
     */
    private void enableInterface() {
        this.webViewCreator.getWebView().addJavascriptInterface(this, "Injector");
    }

    private String smoothUrl(String url) {
        return url.replaceAll("[^\\x00-\\x7F]", "")
                .replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "")
                .replaceAll("\\p{C}", "").trim();
    }

    /**
     *
     * @param newUrl Dynamic URL to load or reload
     * @param js JavaScript code to inject
     * @param callback CallBack after synchronous injection Response
     */
    public void urlInjector(final String newUrl, final String js, final ValueCallback<String> callback) {
        this.urlInjector(newUrl, js, callback, 0);
    }
    /**
     *
     * @param newUrl New url to inject
     * @param js Code injection
     * @param callback callback of code injection result
     */
    public void urlInjector(final String newUrl, final String js, final ValueCallback<String> callback, final int latency) {
        this.callback = callback;

        this.webViewCreator.getWebView().setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(final WebView view, String url) {
                if (url.equalsIgnoreCase(smoothUrl(newUrl))) {
                    try {
                        Thread.sleep(latency * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    injectJavascript(js, view, null);
                }
            }
        });

        this.enableInterface();
        this.webViewCreator.load(newUrl);
    }

    /**
     *
     * @param javascript JavaScript to inject
     * @param webview WebView where code should be injected, if null set by WebViewCreator Instance
     * @param callback CallBack used when injection is done
     */
    public void injectJavascript(String javascript, WebView webview, ValueCallback<String> callback) {
        if (webview == null)
            webview = this.webViewCreator.getWebView();

        webview.evaluateJavascript("javascript:" + javascript, callback);
    }

    /**
     * Url loader
     */
    public void load() {
        this.webViewCreator.load(this.url);
    }

    /**
     *
     * @param javascript JavaScript to inject
     * @param responseCallback CallBack used when injection is done
     */
    public void inject(String javascript, ResponseCallback responseCallback) {
        this.webViewCreator.applyClient(this.clientCreator(javascript, responseCallback));
    }
}
