package org.feup.potter.terminal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity implements View.OnClickListener {
    private static final String QR_CODE_FORMAT = "QR_CODE";
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

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
        switch (v.getId()) {
            case R.id.button_qr:
                try {
                    Intent intent = new Intent(ACTION_SCAN);
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(intent, 0);
                } catch (ActivityNotFoundException anfe) {
                    showDialog(this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
                }
                break;
            case R.id.button_nfc:
                if (mNfcAdapter == null) {
                    // Stop here, we definitely need NFC
                    Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
                    break;
                }else{

                }
                break;
            default:
                break;
        }
    }

    // after qr code scan
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                Log.d("Qr Code", "Format: " + format + "\nMessage: " + contents);
                // decode de qr code message
                if (format.equals(QR_CODE_FORMAT)) {
                    try {
                        JSONObject obj = new JSONObject(contents);
                        String idUser = obj.getString("idUser");

                        JSONArray jsonArray = obj.getJSONArray("items");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject itemObj = jsonArray.getJSONObject(i);
                            String idItem = itemObj.getString("idItem");
                            String quantity = itemObj.getString("quantity");
                        }
                        // need vouchers
                        Toast.makeText(this, "Scan was successfully Complete.", Toast.LENGTH_LONG).show();

                        // make data base request
                        // ----------------------
                        // **********************

                    } catch (JSONException e) {
                        Toast.makeText(this, "Error retriving information from QR code.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Valid order was not detected.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    // dialog to download and show
    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {
                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    public void onResume() {
        super.onResume();
        //replyMsg.setText(app.reply);
    }
}
