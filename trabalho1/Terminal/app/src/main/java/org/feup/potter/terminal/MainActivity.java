package org.feup.potter.terminal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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

import org.feup.potter.terminal.db.Order;
import org.feup.potter.terminal.serverConnection.DoOrder;
import org.feup.potter.terminal.serverConnection.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity implements View.OnClickListener, HttpResponse {
    private static final String QR_CODE_FORMAT = "QR_CODE";
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    private Button button_qr;
    private Button button_nfc;

    private NfcAdapter mNfcAdapter;

    private LunchAppData data;
    private ProgressDialog progDiag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.data = (LunchAppData) getApplicationContext();

        // get the phone nfc adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        this.button_nfc = (Button) findViewById(R.id.button_qr);
        this.button_nfc.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // clears previous order
        this.data.currentOrder = null;

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
                        this.data.currentOrder = new Order(obj);

                        this.hadleOrder();
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error retriving information from QR code.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Valid order was not detected.", Toast.LENGTH_SHORT).show();
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

    // nfc onResume
    public void onResume() {
        super.onResume();
        if (this.data.nfcRead) {
            this.data.nfcRead = false;
            this.hadleOrder();
        }
    }

    private void hadleOrder() {
        if (this.data.currentOrder != null) {
            // starts server connection
            startProgressBar();

            DoOrder connApi = new DoOrder(this, this.data.currentOrder); // this must implement HttpResponse interface
            Thread thr = new Thread(connApi);
            thr.start();

            this.data.currentOrder = null;
        }
    }

    public void startProgressBar() {
        progDiag = new ProgressDialog(MainActivity.this);
        progDiag.setTitle("Ordering");
        progDiag.setMessage("Please wait...");
        progDiag.show();
    }

    private void stopProgressBar() {
        if (progDiag != null)
            progDiag.dismiss();
    }

    @Override
    public void handleResponse(int code, String response) {
        if (code == 200) {
            try {
                Log.d("HandlerOrder", "response OK : " + response);
                JSONObject obj = new JSONObject(response);

                System.out.println(obj.toString());

                final String totalPrice = obj.getString("total");
                final String idSale = obj.getString("idSale");
                final String discont = obj.getString("discount");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), ShowOrder.class);
                        intent.putExtra("id", idSale);
                        intent.putExtra("totalP", totalPrice);
                        intent.putExtra("discount", discont);
                        startActivity(intent);
                        stopProgressBar();
                    }
                });
            } catch (JSONException e) {
                Log.d("HandlerOrder", "Json format error");
                inCaseOfError(code, response);
            }
        } else {
            if (code == 403) {
                inCaseOfError(code, "Your are black listed.");//"Your are black listed.");
            } else {
                Log.d("HandlerOrder", code + ": " + response);
                inCaseOfError(code, response);
            }
        }
    }

    private void inCaseOfError(final int code, final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stopProgressBar();
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(response);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }
}
