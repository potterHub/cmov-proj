package org.feup.potter.terminal.nfc;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

import org.feup.potter.terminal.LunchAppData;
import org.feup.potter.terminal.R;
import org.feup.potter.terminal.db.Order;
import org.json.JSONException;
import org.json.JSONObject;

public class NfcReceive extends Activity {
    private TextView waitting_view_message;

    private LunchAppData data;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_receive);

        this.data = (LunchAppData) getApplicationContext();

        this.waitting_view_message = (TextView) findViewById(R.id.text_view_message);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        String message = new String(msg.getRecords()[0].getPayload());

        try {
            JSONObject obj = new JSONObject(message);
            this.data.currentOrder = new Order(obj);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
