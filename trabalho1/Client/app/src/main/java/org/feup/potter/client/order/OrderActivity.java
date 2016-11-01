package org.feup.potter.client.order;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.feup.potter.client.R;
import org.feup.potter.client.menus.MenusActivity;

public class OrderActivity extends MenusActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus);

        // initiate list adapter
        this.initiateListAndListAdapter(false);

        // connection api
        this.connectToServer();
    }


}
