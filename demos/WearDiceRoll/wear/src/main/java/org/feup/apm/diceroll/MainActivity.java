package org.feup.apm.diceroll;

import android.app.Activity;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.squareup.seismic.ShakeDetector;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity implements ShakeDetector.Listener {

  private static final int MAX_SHAKE_TIME = 1000;
  private static final int COUNTDOWN_INTERVAL = 100;
  private CircledImageView mCircledImageView;
  private CountDownTimer mCountDownTimer;
  private SensorManager mSensorManager;
  private ShakeDetector mShakeDetector;
  private Random mRandom = new Random();
  private GoogleApiClient mGoogleApiClient;
  private Node mNode;
  private TextView mTv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    mShakeDetector = new ShakeDetector(this);

    final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
    stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
      @Override
      public void onLayoutInflated(WatchViewStub stub) {
        mCircledImageView = (CircledImageView) stub.findViewById(R.id.progress);
        mTv = (TextView) stub.findViewById(R.id.die_value);
      }
    });

    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addApi(Wearable.API)
        .addConnectionCallbacks(mConnectionCallbacks)
        .addOnConnectionFailedListener(mFailed)
        .build();
    mGoogleApiClient.connect();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mShakeDetector.stop();
  }

  @Override
  protected void onResume() {
    super.onResume();
    mShakeDetector.start(mSensorManager);
  }

  @Override
  public void hearShake() {
    startTimer();
  }

  GoogleApiClient.ConnectionCallbacks mConnectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
    @Override
    public void onConnected(Bundle bundle) {
      new Thread(new Runnable() {
        @Override
        public void run() {
          NodeApi.GetConnectedNodesResult result = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
          List<Node> nodes = result.getNodes();
          if (nodes.size() > 0)
            mNode = nodes.get(0);
        }
      }).start();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }
  };

  GoogleApiClient.OnConnectionFailedListener mFailed = new GoogleApiClient.OnConnectionFailedListener() {
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
      Log.d("org.feup.apm.diceroll", "Result: " + connectionResult);
    }
  };

  private void startTimer() {
    if (mCountDownTimer != null)
      mCountDownTimer.cancel();

    mCountDownTimer = new CountDownTimer(MAX_SHAKE_TIME, COUNTDOWN_INTERVAL) {
      @Override
      public void onTick(long millisLeft) {
        float progress = (float) millisLeft/MAX_SHAKE_TIME;
        mCircledImageView.setProgress(progress);
      }

      @Override
      public void onFinish() {
        mCircledImageView.setProgress(1.0f);
        int value = rollDie(6);
        mTv.setText(String.valueOf(value));
        sendToPhone(value);
      }
    };
    mCountDownTimer.start();
  }

  private int rollDie(int sides) {
    return mRandom.nextInt(sides) + 1;
  }

  private void sendToPhone(final int value) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        if (mNode != null) {
          byte[] bytes = ByteBuffer.allocate(4).putInt(value).array();
          Wearable.MessageApi.sendMessage(mGoogleApiClient, mNode.getId(), "/diceroll", bytes).await();
        }
      }
    }).start();
  }
}
