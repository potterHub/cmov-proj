package org.feup.potter.client.order;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.feup.potter.client.R;
import org.feup.potter.client.db.DataBaseHelper;
import org.feup.potter.client.db.ItemTable;
import org.feup.potter.client.menus.MenusActivity;

public class VaucherOrderFrag extends ListFragment implements LoaderManager.LoaderCallbacks<D>{
    private DataBaseHelper dataBase;
    private ItemTable dataHelper;

    // list
    private Cursor model;
    private MenusActivity.DataBaseCursorRowAdapter dataBaseAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment_order_vauchers, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // creates the data base
        this.dataBase = new DataBaseHelper(this.getActivity());

        this.dataHelper = dataBase.getItemsTable();
        // gets the items table cursor model
        this.model = dataHelper.getAll();
        // defines the cursor as a managing cursor
        // startManagingCursor(model);
        // sets initiates the table data base adapter
        dataBaseAdapter = new MenusActivity.DataBaseCursorRowAdapter(this.model);

        //attach the adapter to the list
        this.setListAdapter(this.dataBaseAdapter);

    }
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Uri MY_URI = "your data URI goes here";
        return new CursorLoader(getActivity(), MY_URI, MY_PROJECTION, selection, selectionArgs, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mAdapter.swapCursor(null);
    }
}

}