package org.feup.potter.client.log_in;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.feup.potter.client.R;

public class SignIn extends Activity implements View.OnClickListener {

    private EditText text_user_name;
    private EditText text_password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        this.text_user_name= (EditText) findViewById(R.id.edit_username);
        this.text_password = (EditText) findViewById(R.id.edit_password);

        TextView registerLink = (TextView) findViewById(R.id.sign_up_link);
        registerLink.setOnClickListener(this);
    }

    // go back to sign up
    public void onClick(View v) {
        finish();
    }

    public void sign_in(View view){
        boolean isValid = false;

        String username = this.text_user_name.getText().toString();
        String password = this.text_password.getText().toString();

        if(isValid){
            // valid credit card validate data and continue
        }else{
            // error message to user
        }
    }

}
