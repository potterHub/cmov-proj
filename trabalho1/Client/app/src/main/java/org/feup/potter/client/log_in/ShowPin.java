package org.feup.potter.client.log_in;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.feup.potter.client.LunchAppData;
import org.feup.potter.client.MainActivity;
import org.feup.potter.client.R;


public class ShowPin extends Activity implements View.OnClickListener {

    private TextView text_pin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pin);

        LunchAppData data = (LunchAppData) getApplicationContext();

        text_pin = (TextView) findViewById(R.id.text_pin_code);
        text_pin.setText(data.user.getPin());

        Button but = (Button) findViewById(R.id.button_continue);
        but.setOnClickListener(this);
    }

    /* for the credit card button button */
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
