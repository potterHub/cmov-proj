package org.feup.potter.client.pastTranfer;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.feup.potter.client.LunchAppData;
import org.feup.potter.client.R;

public class ConfirmUserAcitivity extends Activity {
    private EditText text_user_name;
    private EditText text_password;

    private LunchAppData data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        this.data = (LunchAppData) getApplicationContext();

        this.text_user_name = (EditText) findViewById(R.id.edit_username);
        this.text_password = (EditText) findViewById(R.id.edit_password);

        TextView registerLink = (TextView) findViewById(R.id.sign_up_link);
        registerLink.setVisibility(View.GONE);
    }

    public void sign_in(View v) {
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

        if (isValid.isEmpty()) {
            if (!username.equals(this.data.user.getUsername())) {
                isValid += "Wrong user name.\n";
            }
            if (!password.equals(this.data.user.getPassword())) {
                isValid += "Wrong password.\n";
            }
            if (isValid.isEmpty()) {
                this.data.logInOnce = true;
                Intent intent = new Intent(ConfirmUserAcitivity.this, PastTransferActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), isValid, Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(getApplicationContext(), isValid, Toast.LENGTH_SHORT).show();
    }
}
