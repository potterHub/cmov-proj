package org.feup.potter.client.log_in;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.feup.potter.client.LunchAppData;
import org.feup.potter.client.MainActivity;
import org.feup.potter.client.R;
import org.feup.potter.client.Util.Util;
import org.feup.potter.client.db.User;
import org.feup.potter.client.serverConnection.HttpResponsePassword;
import org.feup.potter.client.serverConnection.LogIn;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class SignIn extends Activity implements View.OnClickListener, HttpResponsePassword {
    private EditText text_user_name;
    private EditText text_password;

    private LunchAppData data;

    private boolean connecting;
    private LogIn connApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        this.data = (LunchAppData) getApplicationContext();

        this.text_user_name = (EditText) findViewById(R.id.edit_username);
        this.text_password = (EditText) findViewById(R.id.edit_password);

        TextView registerLink = (TextView) findViewById(R.id.sign_up_link);
        registerLink.setOnClickListener(this);

        this.connApi = null;
        this.connecting = false;
    }

    // go back to sign up
    public void onClick(View v) {
        finish();
    }

    public void sign_in(View view) {
        if (!this.connecting) {
            String isValid = "";

            String username = this.text_user_name.getText().toString();
            if (username.isEmpty()) {
                isValid += "Put a username...\n";
            } else if (username.length() < 3) {
                isValid += "Your user name must have 3 at least...\n";
            }

            String password = this.text_password.getText().toString();
            if (password.isEmpty()) {
                isValid += "Put a password...\n";
            } else if (password.length() < 6) {
                isValid += "Password must have a length bigger than 6...\n";
            }

            if (isValid.isEmpty())
                connectToServer(username, password);
            else
                Toast.makeText(getApplicationContext(), isValid, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Connecting To server...", Toast.LENGTH_SHORT).show();
        }
    }

    private void connectToServer(String username, String password) {
        this.connecting = true;

        this.connApi = new LogIn(this, username, password); // this must implement HttpResponse interface
        Thread thr = new Thread(this.connApi);
        thr.start();
    }


    @Override
    public void handleResponse(int code, final String response, String password) {
        if (code == 200) {
            try {
                Log.d("response", response);
                JSONObject obj = new JSONObject(response);
                logUser(obj, password);
            } catch (JSONException e) {
                final String resp = response;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        logInFail(resp);
                    }
                });
            }
        } else {
            final String resp = response;
            runOnUiThread(
                    new Runnable() {
                @Override
                public void run() {
                    logInFail(resp);
                }
            });
        }
        this.connecting = false;
    }

    private void logUser(final JSONObject obj, final String password) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    String token = obj.getString("token");
                    String pin = obj.getString("PIN");

                    Log.d("token", token);
                    Log.d("pin", pin);

                    String[] tokenArr = token.split("\\.");

                    // encode
                    // byte[] data = text.getBytes("UTF-8");
                    // String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                    if (tokenArr.length >= 2) {
                        byte[] token2Decode = Base64.decode(tokenArr[1], Base64.DEFAULT);
                        String tokenData = new String(token2Decode, "utf-8");

                        System.out.println("Original String: " + tokenData);
                        JSONObject tokeObj = new JSONObject(tokenData);

                        SignIn.this.data.user = new User(tokeObj.getString("IdCustomer"), tokeObj.getString("Name"), tokeObj.getString("Username"), password, pin, token);

                        // String id, String name, String username, String password,String pin, String tokan
                        Util.saveData(SignIn.this.data.user, SignIn.this.data.userPath, getApplicationContext());

                        Toast.makeText(getApplicationContext(), "Log In was successful.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        logInFail("");
                    }
                } catch (JSONException | UnsupportedEncodingException e) {
                    logInFail("");
                }
            }
        });
    }

    private void logInFail(String message) {
        this.connecting = false;
        Toast.makeText(getApplicationContext(), message.isEmpty() ? "Log in Fail..." : message, Toast.LENGTH_SHORT).show();
    }
}
