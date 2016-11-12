package org.feup.potter.terminal;

import android.app.Application;

import org.feup.potter.terminal.db.Order;

public class LunchAppData extends Application {

    public boolean nfcRead = false;
    public Order currentOrder;

}
