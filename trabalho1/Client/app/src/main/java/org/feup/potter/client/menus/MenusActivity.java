package org.feup.potter.client.menus;


import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.feup.potter.client.R;
import org.feup.potter.client.db.DataBaseHelper;
import org.feup.potter.client.db.ItemTable;

public class MenusActivity extends ListActivity {
    //
    private ItemTable dataHelper;

    // list
    private Cursor model;
    private DataBaseCursorRowAdapter dataBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus);

        // creates the data base
        DataBaseHelper DataBase = new DataBaseHelper(this);

        this.dataHelper = DataBase.getItemsTable();
        // gets the items table cursor model
        this.model = dataHelper.getAll();
        // defines the cursor as a managing cursor
        startManagingCursor(model);
        // sets initiates the table data base adapter
        dataBaseAdapter = new DataBaseCursorRowAdapter(this.model);

        //attach the adapter to the list
        this.setListAdapter(this.dataBaseAdapter);
    }


    @Override
    //  method from the interface on listActivity that handles the user clicks in the items from the ListView the list with the restaurants
    public void onListItemClick(ListView list, View view, int position, long id) {

    }

    // Cursor adapter (to implement the list row view)
    public class DataBaseCursorRowAdapter extends CursorAdapter {

        // receives the cursor model
        public DataBaseCursorRowAdapter(Cursor c) {
            super(MenusActivity.this, c);
        }

        @Override // it receives the cursor for the selected line
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = MenusActivity.this.getLayoutInflater();
            // inflate -> Inflate a new view hierarchy from the specified XML node. (create the custom row view)
            View row = inflater.inflate(R.layout.menu_list_row, parent, false); // get out the custom layout

            // set the custom row view values
            ((TextView) row.findViewById(R.id.title)).setText(dataHelper.getName(cursor));        // sets the restaurant name by the cursor from the selected line
            ((TextView) row.findViewById(R.id.price)).setText(dataHelper.getPrice(cursor) + " " + getResources().getString(R.string.money));   // sets the restaurant address by the cursor from the selected line

            // set the symbol image by the restaurant type
            ImageView img = (ImageView) row.findViewById(R.id.img);
            img.setImageBitmap(dataHelper.getImg(cursor));

            // return the changed row
            return (row);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {}
    }
}
