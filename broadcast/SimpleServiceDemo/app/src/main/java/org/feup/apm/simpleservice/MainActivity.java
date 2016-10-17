package org.feup.apm.simpleservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

public class MainActivity extends Activity implements RadioGroup.OnCheckedChangeListener {
  private static final String TAG = "MainActivity";
  private int counter = 1;
  private RadioGroup rg;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    rg = (RadioGroup) findViewById(R.id.radioGroup1);
    rg.setOnCheckedChangeListener(this);
  }

  public void doClick(View view) {
    Intent intent;
    
    switch(view.getId()) {
      case R.id.startBtn:
        Log.v(TAG, "Starting service... counter = " + counter);
        
        if (rg.getCheckedRadioButtonId() == R.id.radio0)
          intent = new Intent(this, BackgroundService.class);
        else
          intent = new Intent("org.feup.apm.intents.ACTION_TESTSERVICE");
        intent.putExtra("counter", counter++);
        startService(intent);
        break;
      case R.id.stopBtn:
        stopService(rg.getCheckedRadioButtonId());
        break;
    }
  }
  
  public void onCheckedChanged(RadioGroup rgr, int id) {
    if (id == R.id.radio0)
      stopService(R.id.radio1);
    else
      stopService(R.id.radio0);
  }

  private void stopService(int id) {
    Intent intent;
    
  	Log.v(TAG, "Stopping service...");
  	if (id == R.id.radio0)
      intent = new Intent(this, BackgroundService.class);
  	else
  	  intent = new Intent("org.feup.apm.intents.ACTION_TESTSERVICE");
  	if (stopService(intent))
  	  Log.v(TAG, "stopService was successful");
    else
     	Log.v(TAG, "stopService was unsuccessful");
  }

  @Override
  public void onDestroy() {
  	stopService(rg.getCheckedRadioButtonId());
    super.onDestroy();
  }
}
