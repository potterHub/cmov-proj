package org.feup.potter.client.log_in;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.feup.potter.client.DataStructures.CreditCard;
import org.feup.potter.client.LunchAppData;
import org.feup.potter.client.R;
import org.feup.potter.client.Util.Util;
import org.feup.potter.client.db.User;
import org.feup.potter.client.serverConnection.HttpResponsePassword;
import org.feup.potter.client.serverConnection.Register;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class SignUp extends Activity implements GetCreditCardDialog.CredidCardInterface, View.OnClickListener, HttpResponsePassword {

    // layout edit texts
    private EditText text_name;
    private EditText text_user_name;
    private EditText text_password;
    private EditText text_confirm_password;

    private CreditCard card;
    private CheckBox checkCreditCard;

    private LunchAppData data;

    private boolean connecting;
    private Register connApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        this.data = (LunchAppData) getApplicationContext();

        this.text_name = (EditText) findViewById(R.id.edit_name);
        this.text_user_name = (EditText) findViewById(R.id.edit_username);
        this.text_password = (EditText) findViewById(R.id.edit_password);
        this.text_confirm_password = (EditText) findViewById(R.id.edit_confirm_password);
        this.card = null;

        this.checkCreditCard = (CheckBox) findViewById(R.id.check_credit_card);

        Button but = (Button) findViewById(R.id.button_field_card);
        but.setOnClickListener(this);

        TextView registerLink = (TextView) findViewById(R.id.sign_in_link);
        registerLink.setOnClickListener(this);

        this.connApi = null;
        this.connecting = false;
    }

    /* for the credit card button button */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_field_card: // opens the dialog to enter de card data
                GetCreditCardDialogFragment dialog = GetCreditCardDialogFragment.newInstance(this.card);
                dialog.show(getFragmentManager(), "get_card");
                break;
            case R.id.sign_in_link:
                Intent registerIntent = new Intent(SignUp.this, SignIn.class);
                startActivity(registerIntent);
                break;
        }
    }

    public void sign_up(View view) {
        if (!this.connecting) {
            String isValid = "";

            String name = this.text_name.getText().toString();
            if (name.isEmpty()) {
                isValid += "Choose a name...\n";
            } else if (name.length() < 3) {
                isValid += "Your name must have 3 at least...\n";
            }
            String username = this.text_user_name.getText().toString();
            if (username.isEmpty()) {
                isValid += "Choose a username...\n";
            } else if (username.length() < 3) {
                isValid += "Your user name must have 3 at least...\n";
            }

            String password = this.text_password.getText().toString();
            if (password.isEmpty()) {
                isValid += "Choose a password...\n";
            } else if (password.length() < 6) {
                isValid += "Password must have a length bigger than 6...\n";
            }

            String password_conf = this.text_confirm_password.getText().toString();
            if (!password_conf.equals(password)) {
                isValid += "The passwords don't match...\n";
            }

            if (!this.checkCreditCard.isChecked() || card == null) {
                isValid += "Invalid Credit Card!\n";
            }

            if (isValid.isEmpty()) {
                connectToServer(name, username, password, card); // this must implement HttpResponse interface
            } else {
                Toast.makeText(getApplicationContext(), isValid, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Connecting To server.", Toast.LENGTH_SHORT).show();
        }
    }

    private void connectToServer(String name, String username, String password, CreditCard card) {
        this.connecting = true;

        this.connApi = new Register(this, name, username, password, card); // this must implement HttpResponse interface
        Thread thr = new Thread(this.connApi);
        thr.start();
    }

    @Override
    public void handleResponse(int code, String response, String password) {
        if (code == 200) {
            try {
                JSONObject obj = new JSONObject(response);
                RegisterUser(obj, password);
            } catch (JSONException e) {
                RegisterFail();
            }
        } else {
            RegisterFail();
        }
        this.connecting = false;
    }

    private void RegisterUser(final JSONObject obj, final String password) {
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

                        SignUp.this.data.user = new User(tokeObj.getString("IdCustomer"), tokeObj.getString("Name"), tokeObj.getString("Username"), password, pin, token);

                        // String id, String name, String username, String password,String pin, String tokan
                        Util.saveUser(SignUp.this.data.user, SignUp.this.data.userPath, getApplicationContext());

                        Toast.makeText(getApplicationContext(), "Register was successful.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ShowPin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        RegisterFail();
                    }
                } catch (JSONException | UnsupportedEncodingException e) {
                    RegisterFail();
                }
            }
        });
    }

    /* for the dialog button */
    @Override
    public void whenCreditCardAdded(CreditCard card) {
        if (card == null) {
            this.card = null;
        } else {
            this.card = card;
            this.checkCreditCard.setChecked(card.isValid());
        }
    }

    private void RegisterFail() {
        this.connecting = false;
        Toast.makeText(getApplicationContext(), "Register in Fail...", Toast.LENGTH_SHORT).show();
    }
}
