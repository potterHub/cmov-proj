package org.feup.potter.terminal;

import android.app.Activity;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener{

    private Button button_qr;
    private Button button_nfc;

    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the phone nfc adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

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
                if (mNfcAdapter == null) {
                    // Stop here, we definitely need NFC
                    Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
                    break;
                }
                break;
            default:
                break;
        }
    }

    public void onResume() {
        super.onResume();
        replyMsg.setText(app.reply);
    }
}
