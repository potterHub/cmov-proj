package org.feup.apm.shakespearefragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TitlesFragment extends ListFragment {
  private MainActivity myActivity = null;
  private int mCurCheckPosition = -1;

  @Override
  public void onAttach(Activity myActivity) {
    super.onAttach(myActivity);
    this.myActivity = (MainActivity) myActivity;
  }

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    if (icicle != null) {
      // Restore last state for checked position.
      mCurCheckPosition = icicle.getInt("curChoice", 0);
    }
  }
  
  @Override
  public void onActivityCreated(Bundle icicle) {
    super.onActivityCreated(icicle);

    // Populate list with our static array of titles.
    setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Shakespeare.TITLES));
    ListView lv = getListView();
    lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    if (mCurCheckPosition != -1) {
      lv.setSelection(mCurCheckPosition);
      myActivity.showDetails(mCurCheckPosition);
    }
  }
  
  @Override
  public void onSaveInstanceState(Bundle icicle) {
    super.onSaveInstanceState(icicle);
    icicle.putInt("curChoice", mCurCheckPosition);
  }

  @Override
  public void onListItemClick(ListView l, View v, int pos, long id) {
    l.setSelection(pos);  
    mCurCheckPosition = pos;
    myActivity.showDetails(pos);
  }
  
  @Override
  public void onDetach() {
    super.onDetach();
    myActivity = null;
  }
}
