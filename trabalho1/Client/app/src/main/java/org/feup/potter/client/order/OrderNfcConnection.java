package org.feup.potter.client.order;


import android.app.Activity;
import android.os.Bundle;

import org.feup.potter.client.LunchAppData;
import org.feup.potter.client.R;
import org.feup.potter.client.db.ItemInList;

import java.util.ArrayList;

public class OrderNfcConnection extends Activity {
    private LunchAppData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus);

        this.data = (LunchAppData) getApplicationContext();
        this.data.orderItemIdList = new ArrayList<ItemInList>();

    }

}
