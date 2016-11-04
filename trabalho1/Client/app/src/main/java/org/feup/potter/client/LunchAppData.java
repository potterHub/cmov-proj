package org.feup.potter.client;

import android.app.Application;

import org.feup.potter.client.db.ItemInList;
import org.feup.potter.client.db.User;

import java.util.ArrayList;

public class LunchAppData extends Application {

    public final String userPath = "user.dat";
    public User user;

    public final String itemHashPath = "item.hash";
    public volatile String hash;

    // list with item id
    public ArrayList<ItemInList> orderItemList;
}
