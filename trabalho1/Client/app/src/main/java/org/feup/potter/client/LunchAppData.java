package org.feup.potter.client;

import android.app.Application;

import org.feup.potter.client.db.User;

import java.util.ArrayList;

public class LunchAppData extends Application {

    public final String userPath = "user.dat";
    public User user;

    // list with item id
    public ArrayList<String> orderItemIdList;
}
