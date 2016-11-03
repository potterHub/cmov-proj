package org.feup.potter.client.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.feup.potter.client.LunchAppData;
import org.feup.potter.client.R;
import org.feup.potter.client.db.ItemInList;
import org.feup.potter.client.menus.MenusActivity;

import java.util.ArrayList;

public class OrderSelectActivity extends MenusActivity {
    private LunchAppData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus);

        this.data = (LunchAppData) getApplicationContext();
        this.data.orderItemIdList = new ArrayList<ItemInList>();

        // initiate list adapter
        initiateListAndListAdapter();

        // connection api
        connectToServer();
    }

    // create the option toast menu (main.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.order_option, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    // when the menu is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.order_icon) {
            // if add button pressed a new activity (details activity) to add the restaurant is going to start
            startActivity(new Intent(OrderSelectActivity.this, OrderNfcConnection.class));
            return (true);
        }

        return (super.onOptionsItemSelected(item));
    }

    protected void initiateListAndListAdapter() {
        this.menus = new ArrayList<ItemInList>();
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
    public class ListRowAdapter extends ArrayAdapter<ItemInList> {
        // receives the cursor model
        public ListRowAdapter() {
            super(OrderSelectActivity.this, R.layout.row_list_menu_to_order, menus);
        }

        @Override // to make our custom adapter build each row according with our list layout
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = OrderSelectActivity.this.getLayoutInflater();
                row = inflater.inflate(R.layout.row_list_menu_to_order, parent, false); // get out the custom layout
            }

            final ItemInList data = menus.get(position);

            // set the custom row view values
            ((TextView) row.findViewById(R.id.title)).setText(data.getName());        // sets the restaurant name by the cursor from the selected line
            ((TextView) row.findViewById(R.id.price)).setText(data.getPrice() + " " + getResources().getString(R.string.money));   // sets the restaurant address by the cursor from the selected line

            // set the symbol image by the restaurant type
            ImageView img = (ImageView) row.findViewById(R.id.img);
            img.setImageBitmap(data.getImage());

            Button button = (Button) row.findViewById(R.id.button_add_to_order);
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // change to string.xml and stuff
                    Toast.makeText(OrderSelectActivity.this, data.getName() + " added", Toast.LENGTH_LONG).show();
                    OrderSelectActivity.this.data.orderItemIdList.add(data);
                }
            });

            // return the changed row
            return (row);
        }
    }
}
