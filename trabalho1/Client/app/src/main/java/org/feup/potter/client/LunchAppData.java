package org.feup.potter.client;

import android.app.Application;
import android.content.res.Configuration;

import org.feup.potter.client.db.DataBaseHelper;
import org.feup.potter.client.db.User;

import java.util.ArrayList;

public class LunchAppData extends Application{
    public User user;

    // list with item id
    public ArrayList<String> orderItemIdList;
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        orderItemIdList = new ArrayList<String>();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
