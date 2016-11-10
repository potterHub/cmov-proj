package org.feup.potter.client.order.qr_nfc;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.feup.potter.client.LunchAppData;
import org.feup.potter.client.MainActivity;
import org.feup.potter.client.R;
import org.feup.potter.client.Util.Util;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class NFCtranmitor extends Activity implements NfcAdapter.OnNdefPushCompleteCallback {
    private final String tag = "application/nfc.feup.potter.message.type1";
    private LunchAppData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_nfc_send);

        // Check for available NFC Adapter
        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(getApplicationContext(), "NFC is not available on this device.", Toast.LENGTH_LONG).show();
            finish();
        }

        data = (LunchAppData) getApplication();

        byte[] order;
        try {
            Toast.makeText(NFCtranmitor.this, this.data.orderVoucherList.toString(), Toast.LENGTH_SHORT).show();
            order = Util.getBytesForOrder(this.data.user.getUsername(), this.data.orderItemList, this.data.orderVoucherList);

            NdefMessage msg = new NdefMessage(new NdefRecord[]{createMimeRecord(tag, order)});

            // Register a NDEF message to be sent in a beam operation (P2P)
            mNfcAdapter.setNdefPushMessage(msg, this);
            mNfcAdapter.setOnNdefPushCompleteCallback(this, this);

            //app.reply = "Entered NfcSend";
        } catch (JSONException | UnsupportedEncodingException e) {
            Toast.makeText(getApplicationContext(), "Error sending order.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public void onNdefPushComplete(NfcEvent arg0) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "Order sent.", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    public NdefRecord createMimeRecord(String mimeType, byte[] payload) {
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("ISO-8859-1"));
        return new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
    }
}
