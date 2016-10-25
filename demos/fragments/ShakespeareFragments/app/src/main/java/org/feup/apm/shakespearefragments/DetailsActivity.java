package org.feup.apm.shakespearefragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

public class DetailsActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      finish();
      return;
    }
    if(getIntent() != null) {
      DetailsFragment details = DetailsFragment.newInstance(getIntent().getExtras());

      getFragmentManager().beginTransaction()
        .add(android.R.id.content, details)
        .commit();
    }
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
        case android.R.id.home:
          finish();
          return true;
      }
      return super.onOptionsItemSelected(item);
  }
}