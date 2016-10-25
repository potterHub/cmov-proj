package org.feup.apm.testbroadcastreceiver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class TestBroadcastReceiverActivity extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) { 
    super.onCreateOptionsMenu(menu);
    MenuInflater inflater = getMenuInflater();      //from activity
    inflater.inflate(R.menu.main_menu, menu);
    return true;
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) { 
    appendMenuItemText(item);
    if (item.getItemId() == R.id.menu_clear) {
      emptyText();
      return true;
    }
    if (item.getItemId() == R.id.menu_send_broadcast) {
      testSendBroadcast();
      return true;
    }
    return true;
  }
  
  private TextView getTextView(){
    return (TextView)this.findViewById(R.id.text1);
  }
  
  private void appendMenuItemText(MenuItem menuItem){
    String title = menuItem.getTitle().toString();
    TextView tv = getTextView(); 
    tv.setText(tv.getText() + title + "\n");
  }
  
  private void emptyText(){
    TextView tv = getTextView();
    tv.setText("");
  }
  
  private void testSendBroadcast() {
  	//Create an intent with an action
  	Intent brIntent = new Intent("org.feup.apm.intents.ACTION_TESTBC_NOTIFICATION");
    brIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);      // for custom standalone BRs
  	brIntent.putExtra("message", "Hello world");
  		
  	//send out the broadcast
  	//there may be multiple receivers receiving it
  	sendBroadcast(brIntent);
  }
}