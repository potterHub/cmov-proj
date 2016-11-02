package org.feup.potter.client.order;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import org.feup.potter.client.R;
import org.feup.potter.client.menus.DialogMenuDetailsFrag;
import org.feup.potter.client.menus.MenusActivity;

import java.util.ArrayList;

public class OrderActivity extends MenusActivity {
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
        this.menus = new ArrayList<String[]>();
        // instantiate the adapter
        this.listAdapter = new ListRowAdapter();
        //attach the adapter to the list
        this.setListAdapter(this.listAdapter);
    }

    @Override
    //  method from the interface on listActivity that handles the user clicks in the items from the ListView the list with the restaurants
    public void onListItemClick(ListView list, View view, int position, long id) {
        //DialogMenuDetailsFrag dialog = DialogMenuDetailsFrag.newInstance(menus.get(position));// enviamos o id do objecto na lista
        //dialog.show(getFragmentManager(), "item_details");
    }

    // Cursor adapter (to implement the list row view)
    public class ListRowAdapter extends ArrayAdapter<String[]> {
        // receives the cursor model
        public ListRowAdapter() {
            super(OrderActivity.this, R.layout.row_list_menu_to_order, menus);
        }

        @Override // to make our custom adapter build each row according with our list layout
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = OrderActivity.this.getLayoutInflater();
                row = inflater.inflate(R.layout.row_list_menu_to_order, parent, false); // get out the custom layout
            }

            final String[] data = menus.get(position);

            // set the custom row view values
            ((TextView) row.findViewById(R.id.title)).setText(data[1]);        // sets the restaurant name by the cursor from the selected line
            ((TextView) row.findViewById(R.id.price)).setText(data[2] + " " + getResources().getString(R.string.money));   // sets the restaurant address by the cursor from the selected line

            // set the symbol image by the restaurant type
            // ImageView img = (ImageView) row.findViewById(R.id.img);
            // img.setImageBitmap(dataHelper.getImg(cursor));

            Button button = (Button) row.findViewById(R.id.button_add_to_order);
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // change to string.xml and stuff
                    Toast.makeText(OrderActivity.this, data[1] + " added", Toast.LENGTH_LONG).show();
                    // need to add to the application data or sql
                }
            });

            // return the changed row
            return (row);
        }
    }
}
