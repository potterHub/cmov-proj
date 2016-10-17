package org.feup.apm.nfcsendreceive;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

public class NfcReceive extends Activity {
  NfcApp app;
  TextView tv;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_nfc_receive);
    tv = (TextView) findViewById(R.id.textView1);
    app = (NfcApp) getApplication();
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
    app.reply = new String(msg.getRecords()[0].getPayload());
    tv.setText(app.reply);
  }
}
