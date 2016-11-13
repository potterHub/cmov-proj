package org.feup.potter.client;

import android.app.Application;

import org.feup.potter.client.db.ItemInList;
import org.feup.potter.client.db.User;
import org.feup.potter.client.db.VouchersInList;

import java.util.ArrayList;

public class LunchAppData extends Application {

    public final String userPath = "user.dat";
    public User user;

    public final String itemHashPath = "item.hash";
    public volatile String hash;

    // by default always false after log in once is true until the end of the aplication life
    public boolean logInOnce = false;

    // list with item id
    public ArrayList<ItemInList> orderItemList;

    // list with selected vouchers code
    public ArrayList<VouchersInList> orderVoucherList;
    public boolean alreadyHasGlobalDiscont;
}
