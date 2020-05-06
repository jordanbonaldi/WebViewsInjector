/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import net.neferett.webviewsinjector.response.ResponseCallback;
import net.neferett.webviewsinjector.response.ResponseEnum;
import net.neferett.webviewsinjector.services.LoginService;
import net.neferett.webviewsinjector.services.ServiceManager;

import java.util.ArrayList;

public class TestConnectionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private LoginService loginService;
    private FrameLayout webview;
    private Button button;
    private EditText login;
    private EditText password;
    private ServiceManager serviceManager = new ServiceManager(this);

    private boolean connected = false;

    private ArrayAdapter<String> loginServiceToAdapter() {
        ArrayList<String> stringArrayList = new ArrayList<>();

        this.serviceManager.loadServices();
        this.serviceManager.getServiceList().forEach(e -> stringArrayList.add(e.getName()));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stringArrayList);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        Log.d("jordan", stringArrayList.toString());
        return adapter;
    }

    private void loadViewComponents() {
        this.button = findViewById(R.id.button);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        webview = findViewById(R.id.webview);

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(this.loginServiceToAdapter());
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_login);

        this.loadViewComponents();

        button.setOnClickListener(view -> {
            String login = this.login.getEditableText().toString();
            String password = this.password.getEditableText().toString();

            loginService.autoLogin(
                    login,
                    password,
                    new ResponseCallback() {
                        @Override
                        public void getResponse(ResponseEnum responseEnum, String data) {
                            if (responseEnum == ResponseEnum.SUCCESS) {
                                Toast.makeText(TestConnectionActivity.this,
                                        "Successfully logged on " + loginService.getName(), Toast.LENGTH_LONG).show();
                                Log.d("DEBUG", "Successfully logged on " + loginService.getName());

                                connected = true;
                            }
                        }
                    }
            );
            webview.removeAllViews();
            webview.addView(loginService.getWebview());
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.loginService = this.serviceManager.get(i);
        Toast.makeText(getApplicationContext(), "Selected User: " + this.serviceManager.get(i).getName(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        this.loginService = null;
    }

    public boolean isConnected() {
        return connected;
    }
}
