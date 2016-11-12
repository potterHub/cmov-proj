package org.feup.potter.terminal.serverConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ServerConnectionAPI {

    // http://127.0.0.1:8080/item
    protected final String address = "192.168.1.90";//"172.30.9.64";// path to server root
    protected final int port = 8080;

    protected final String GET_VOUCHER_PATH = "customer/voucher";
    protected final String DO_ORDER_PATH = "terminal/order";

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
