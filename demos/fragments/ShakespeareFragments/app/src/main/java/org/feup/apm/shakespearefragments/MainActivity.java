package org.feup.apm.shakespearefragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
	public static final String TAG = "Shakespeare";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
	}

  public boolean isMultiPane() {
    return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
  }

  /**
   * Helper function to show the details of a selected item, either by
   * displaying a fragment in-place in the current UI, or starting a
   * whole new activity in which is displayed.
   */
  public void showDetails(int index) {
    Log.v(TAG, "in MainActivity showDetails(" + index + ")");

    if (isMultiPane()) {
      // Check what fragment is shown, replace if needed.
      DetailsFragment details = (DetailsFragment) getFragmentManager().findFragmentById(R.id.details);
      if (details == null || details.getShownIndex() != index) {
        details = DetailsFragment.newInstance(index);           // Make new fragment to show this selection.
        // Execute a transaction, replacing any existing fragment inside the frame with the new one.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
        //ft.setCustomAnimations(R.animator.bounce_in_down, R.animator.slide_out_right);
        //ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.details, details);
        ft.addToBackStack(TAG);
        ft.commit();
      }
    } 
    else {
      // Otherwise we need to launch a new activity
      Intent intent = new Intent();
      intent.setClass(this, DetailsActivity.class);
      intent.putExtra("index", index);
      startActivity(intent);
    }
  }
}
