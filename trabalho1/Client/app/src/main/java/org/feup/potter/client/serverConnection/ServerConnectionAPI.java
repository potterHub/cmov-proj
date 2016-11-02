package org.feup.potter.client.serverConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ServerConnectionAPI {

    // http://127.0.0.1:8080/item
    protected final String address = "192.168.1.90";// path to server root
    protected final int port = 8080;

    protected final String getItemsPath = "item";
    protected final String logInPath = "customer/login";
    protected final String registerPath = "customer/register";

    // how to call it
    /*
        GetItems getItems = new GetItems(this); // this must implement HttpResponse interface
        Thread thr = new Thread(GetItems);
        thr.start();
     */

    protected String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                // nao se para converter para json preciso do \n (eu acho que precisa line + "\n")
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
