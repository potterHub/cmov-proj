package org.feup.potter.client;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import org.feup.potter.client.main_menu.GridViewAdapter;
import org.feup.potter.client.main_menu.Item;

public class MainActivity extends Activity implements OnItemClickListener {
    private GridView gridview;

    private GridViewAdapter gridviewAdapter;

    private ArrayList<Item> menuItems = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the GUI Components
        this.gridview = (GridView) findViewById(R.id.grid_menu_view);
        this.gridview.setOnItemClickListener(this);

        // Insert Data in to the gridView (because of the adapater inserting menuItems into the menuItems list)
        this.menuItems.add(new Item(getResources().getString(R.string.menu_menus)
                , getResources().getDrawable(R.drawable.menus_icon)));

        this.menuItems.add(new Item(getResources().getString(R.string.menu_order)
                , getResources().getDrawable(R.drawable.order_icon)));

        this.menuItems.add(new Item(getResources().getString(R.string.menu_transactions)
                , getResources().getDrawable(R.drawable.transactions_icon)));

        this.menuItems.add(new Item(getResources().getString(R.string.menu_vouchers)
                , getResources().getDrawable(R.drawable.vouchers_icon)));

        this.menuItems.add(new Item(getResources().getString(R.string.menu_setting)
                , getResources().getDrawable(R.drawable.settings_icon)));

        this.menuItems.add(new Item(getResources().getString(R.string.menu_login)
                , getResources().getDrawable(R.drawable.login_icon)));

        // Set the Data Adapter for the menu
        this.gridviewAdapter = new GridViewAdapter(getApplicationContext(), R.layout.grid_view_menu_icon, this.menuItems);
        this.gridview.setAdapter(this.gridviewAdapter);
    }

    @Override
    public void onItemClick(final AdapterView<?> arg0, final View view, final int position, final long id) {
        String message = "Clicked : " + menuItems.get(position).getTitle();
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}

