package org.feup.potter.terminal;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener{

    private Button button_qr;
    private Button button_nfc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.button_qr = (Button) findViewById(R.id.button_nfc);
        this.button_qr.setOnClickListener(this);

        this.button_nfc = (Button) findViewById(R.id.button_qr);
        this.button_nfc.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_qr:

                break;
            case R.id.button_nfc:

                break;
            default:
                break;
        }
    }
}
