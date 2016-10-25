package org.feup.apm.diceroll;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.nio.ByteBuffer;

public class MainActivity extends Activity {

  TextView mResult;
  GoogleApiClient mGoogleApiClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mResult = (TextView) findViewById(R.id.result);
    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addApi(Wearable.API)
        .addConnectionCallbacks(mConnectionCallbacks)
        .build();
    mGoogleApiClient.connect();
  }

  private void setDiceValue(int value) {
    mResult.setText(Integer.toString(value));
  }

  GoogleApiClient.ConnectionCallbacks mConnectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
    @Override
    public void onConnected(Bundle bundle) {
      Wearable.MessageApi.addListener(mGoogleApiClient, mMessageListener);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }
  };

  MessageApi.MessageListener mMessageListener = new MessageApi.MessageListener() {
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
      Log.d("org.feup.apm.diceroll", "Message arrived!");
      if (messageEvent.getPath().equals("/diceroll")) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(messageEvent.getData());
        final int value = byteBuffer.getInt();

        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            setDiceValue(value);
          }
        });
      }
    }
  };
}
