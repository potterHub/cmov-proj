package org.feup.potter.client.serverConnection;

import android.util.Log;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LogIn extends ServerConnectionAPI implements Runnable {

    private String username;
    private String password;

    protected HttpResponsePassword androidActivity;

    // serverURL + path
    public LogIn(HttpResponsePassword myclass, String username, String password) {
        super();
        this.androidActivity = myclass;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run() {
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL("http://" + address + ":" + port + "/" + logInPath);

            //writeText("GET " + url.toExternalForm());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setUseCaches(false);

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("username", username);
            jsonObj.put("password", password);

            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.writeBytes(jsonObj.toString());
            outputStream.flush();
            outputStream.close();


            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                String response = readStream(urlConnection.getInputStream());

                Log.d("LogIn", "Server Response OK");
                androidActivity.handleResponse(responseCode, response, password);
            } else {
                Log.d("LogIn", "Server Response ERROR");
                androidActivity.handleResponse(responseCode, "", "");
            }
        } catch (Exception e) {
            Log.d("LogIn", "Server ERROR");
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
    }
}
