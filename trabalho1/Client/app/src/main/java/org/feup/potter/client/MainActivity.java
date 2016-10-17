package org.feup.potter.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // starting login tab
        // do something when clicking the button
       // Intent intent = new Intent(this,SignIn.class);

        // starts the second activity
        //  startActivity(intent);
    }
}
