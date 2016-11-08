package org.feup.potter.client.serverConnection;

import android.util.Log;

import org.feup.potter.client.DataStructures.CreditCard;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class Register extends ServerConnectionAPI implements Runnable {
    private String name;
    private String username;
    private String password;
    private CreditCard card;

    protected HttpResponsePassword androidActivity;

    public Register(HttpResponsePassword myclass, String name, String username, String password, CreditCard card) {
        super();
        this.androidActivity = myclass;
        this.name = name;
        this.username = username;
        this.password = password;
        this.card = card;
    }

    @Override
    public void run() {
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL("http://" + address + ":" + port + "/" + REGISTER_PATH);

            // writeText("POST " + url.toExternalForm());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setUseCaches(false);

            JSONObject jsonObj = new JSONObject();
            JSONObject jsonCreditCard = new JSONObject();
            jsonCreditCard.put("month", Integer.valueOf(card.getMonthExpiration()));
            jsonCreditCard.put("year", Integer.valueOf(card.getYearExpiration()));
            jsonCreditCard.put("code", card.getCardNumber());
            jsonCreditCard.put("idCreditCardType", Integer.valueOf(card.getTypeDbId()));

            jsonObj.put("creditCard", jsonCreditCard);
            jsonObj.put("password", password);
            jsonObj.put("username", username);
            jsonObj.put("name", name);

            Log.d("Json",jsonObj.toString());


            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.writeBytes(jsonObj.toString());
            outputStream.flush();
            outputStream.close();

            int responseCode = urlConnection.getResponseCode();

            String response = "";
            if (responseCode == 200) {
                response = readStream(urlConnection.getInputStream());
                Log.d("GetItem","Server Response OK");
            } else {
                response = readStream(urlConnection.getErrorStream());
                Log.d("GetItem","Server Response ERROR");
            }
            androidActivity.handleResponse(responseCode, response, password);
        } catch (Exception e) {
            androidActivity.handleResponse(0, e.toString(),"");
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
    }
}
