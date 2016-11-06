package org.feup.potter.client.menus;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.feup.potter.client.R;
import org.feup.potter.client.db.ItemInList;

import java.util.ArrayList;

public class MenusActivity extends BaseItemMenuList {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus);

        // initiate list adapter
        initiateListAndListAdapter();

        // connection api
        connectToServer();
    }

    protected void initiateListAndListAdapter() {
        this.menus = new ArrayList<ItemInList>();
        // instantiate the adapter
        this.listAdapter = new ListRowAdapter();
        //attach the adapter to the list
        this.setListAdapter(this.listAdapter);
    }


    // Cursor adapter (to implement the list row view)
    public class ListRowAdapter extends ArrayAdapter<ItemInList> {
        // receives the cursor model
        public ListRowAdapter() {
            super(MenusActivity.this, R.layout.row_list_menu, menus);
        }

        @Override // to make our custom adapter build each row according with our list layout
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = MenusActivity.this.getLayoutInflater();
                row = inflater.inflate(R.layout.row_list_menu, parent, false); // get out the custom layout
            }

            final ItemInList item = menus.get(position);

            // set the custom row view values
            ((TextView) row.findViewById(R.id.title)).setText(item.getName());        // sets the restaurant name by the cursor from the selected line
            ((TextView) row.findViewById(R.id.price)).setText(item.getPrice() + " " + getResources().getString(R.string.money));   // sets the restaurant address by the cursor from the selected line

            // set the symbol image by the restaurant type
            ImageView img = (ImageView) row.findViewById(R.id.img);
            img.setImageBitmap(item.getImage());

            // return the changed row
            return (row);
        }
    }
}