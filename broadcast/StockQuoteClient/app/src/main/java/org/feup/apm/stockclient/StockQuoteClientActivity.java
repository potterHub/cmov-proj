package org.feup.apm.stockclient;

import org.feup.apm.stockservice.IStockQuoteService;
import org.feup.apm.stockservice.Person;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class StockQuoteClientActivity extends Activity {

  protected static final String TAG = "StockQuoteClient";
	private IStockQuoteService stockService = null;
	
	private Button bindBtn;
	private Button callBtn;
	private Button unbindBtn;

	/** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    bindBtn = (Button)findViewById(R.id.bindBtn);
    bindBtn.setOnClickListener(new OnClickListener() {
      public void onClick(View view) {
        if (bindService(new Intent(IStockQuoteService.class.getName()), serConn, Context.BIND_AUTO_CREATE)) {
          bindBtn.setEnabled(false);
          unbindBtn.setEnabled(true);
        }
      }
    });

    callBtn = (Button)findViewById(R.id.callBtn);
    callBtn.setOnClickListener(new OnClickListener() {
      public void onClick(View view) {
          callService();
      }
    });
    callBtn.setEnabled(false);
    
    unbindBtn = (Button)findViewById(R.id.unbindBtn);
    unbindBtn.setOnClickListener(new OnClickListener() {
      public void onClick(View view) {
        unbindService(serConn);
        bindBtn.setEnabled(true);
        callBtn.setEnabled(false);
        unbindBtn.setEnabled(false);
      }
    });
    unbindBtn.setEnabled(false);
  }

  private void callService() {
    try {
    	Person person = new Person();
    	person.setAge(47);
    	person.setName("Dave");
      String response = stockService.getQuote("IBM", person);
      Toast.makeText(this, "Value from service is \"" + response + "\"", Toast.LENGTH_LONG).show();
    } catch (RemoteException ee) {
      Log.e(TAG, ee.getMessage(), ee);
    }
  }

  private ServiceConnection serConn = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      Log.v(TAG, "onServiceConnected() called");
      stockService = IStockQuoteService.Stub.asInterface(service);
      callBtn.setEnabled(true);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      Log.v(TAG, "onServiceDisconnected() called");
    	stockService = null;
      bindBtn.setEnabled(true);
      callBtn.setEnabled(false);
      unbindBtn.setEnabled(false);
    }
  };
}
