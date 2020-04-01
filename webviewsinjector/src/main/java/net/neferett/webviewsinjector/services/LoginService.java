/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector.services;

import android.content.Context;
import android.os.Handler;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import net.neferett.webviewsinjector.WebInjector;
import net.neferett.webviewsinjector.WebViewCreator;
import net.neferett.webviewsinjector.login.StepEnum;
import net.neferett.webviewsinjector.login.steps.Step;
import net.neferett.webviewsinjector.response.ResponseCallback;
import net.neferett.webviewsinjector.response.ResponseEnum;
import net.neferett.webviewsinjector.response.ResponseInjector;
import net.neferett.webviewsinjector.settings.Settings;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Objects;

@Data
public abstract class LoginService {

    private final Context context;
    private final String name;
    private final int drawableLogo;
    private final boolean mobile;

    protected final int delay;
    protected final String url;
    protected Step step;
    protected StepEnum stepEnum;
    protected ResponseInjector responseInjector;
    protected WebViewCreator webViewCreator;
    protected WebInjector webInjector;

    protected int preDelay = 0;

    /**
     *
     * @param name Service name
     * @param step Step Method link of instance (One step, two step...)
     * @param context Main context of Android Application
     * @param url Url to load WebView
     * @param delay Delay between each click
     */
    public LoginService(String name, StepEnum step, final Context context, String url, int delay, int drawableLogo, boolean mobile) {
        this.step = StepEnum.createInstance(step);
        this.stepEnum = step;
        this.context = context;
        this.delay = delay;
        this.name = name;
        this.drawableLogo = drawableLogo;
        this.url = url;
        this.mobile = mobile;

        this.setupElements();
    }

    public void load() {
        this.webViewCreator = new WebViewCreator(context, mobile, new HashMap<Settings, Object>() {{
            put(Settings.JavaScript, true);
            put(Settings.DomStorage, true);
            put(Settings.ClearCache, true);
        }});

        this.webInjector = new WebInjector(this.webViewCreator, url);
        this.responseInjector = new ResponseInjector(this.webInjector, this.setupTests());
    }

    /**
     *
     * @param email Email or Username to fill inside AutoLogin
     * @param password Password to fill inside AutoLogin
     * @param responseCallback CallBack with ResponseType when connection is success or false
     */
    @SneakyThrows
    public void autoLogin(String email, String password, final ResponseCallback responseCallback) {
        if (this.webViewCreator == null)
            this.load();

        if (responseCallback != null)
            responseCallback.setResponseInjector(this.responseInjector);

        try {
            this.webInjector.inject(
                    this.build(email, password), responseCallback);
            new Handler().postDelayed(Objects.requireNonNull(responseCallback),
                    ((delay * stepEnum.getMultiplicand()) + 10) * 1000);
        } catch (Exception e) { e.printStackTrace(); }

        this.webInjector.load();
    }

    /**
     * Setting Up TypeAuth ElementValue linking
     */
    public abstract void setupElements();

    /**
     *
     * @param email Email to fill
     * @param password Password to fill
     * @return Full JavaScript to inject
     * @throws Exception Throwing when promise creation is failed
     */
    private String build(String email, String password) throws Exception {
        return this.step.construct(email, password, this.delay);
    }

    /**
     *
     * @return Setup of Response and Linked JavaScript code
     */
    protected abstract HashMap<ResponseEnum, String> setupTests();

    /**
     *
     * Change current webview page
     *
     * @param url new URL to load
     */
    public void changeUrl(String url) {
        this.webViewCreator.getWebView().loadUrl(url);
    }

    /**
     * New code injection
     *
     * @param js Js code to inject
     */
    public void injectJavaScript(String js, ValueCallback<String> callback) {
        if (this.webInjector == null)
            this.load();
        this.webInjector.injectJavascript(js, null, callback);
    }

    public WebView getWebview() {
        return this.getWebViewCreator().getWebView();
    }
}
