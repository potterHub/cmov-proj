package org.feup.potter.client.order;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import org.feup.potter.client.R;

public class OrderActivity extends FragmentActivity {
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("tab_menus").setIndicator(getResources().getString(R.string.menu_menus)),
                MenuOrderListFrag.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab_orders").setIndicator(getResources().getString(R.string.menu_order)),
                OrderListFrag.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab_vauchers").setIndicator(getResources().getString(R.string.menu_vouchers)),
                VaucherOrderFrag.class, null);
    }
}
