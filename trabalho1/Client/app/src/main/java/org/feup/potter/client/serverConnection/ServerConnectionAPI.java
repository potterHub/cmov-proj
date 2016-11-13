package org.feup.potter.client.serverConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ServerConnectionAPI {

    // http://127.0.0.1:8080/item
    protected final String address = "192.168.1.90";// "172.30.9.64"; //path to server root
    protected final int port = 8080;

    protected final String HASH_GET_FIELD = "?hash=";

    protected final String GET_ITEMS_PATH = "item";
    protected final String LOG_IN_PATH = "customer/login";
    protected final String REGISTER_PATH = "customer/register";
    protected final String GET_VOUCHER_PATH = "customer/voucher";
    protected final String GET_PAST_TRANSACTIONS_PATH = "customer/order";

    protected String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            return e.getMessage();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    return e.getMessage();
                }
            }
        }
        return response.toString();
    }
}
