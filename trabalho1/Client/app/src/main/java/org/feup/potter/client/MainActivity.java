package org.feup.potter.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.feup.potter.client.log_in.SingUp;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // starting login tab
        // do something when clicking the button

        // just get user from db (if exists log in)

        // if not register fragment
        Intent intent = new Intent(this,SingUp.class);

        // starts the second activity
        startActivity(intent);
    }
}
