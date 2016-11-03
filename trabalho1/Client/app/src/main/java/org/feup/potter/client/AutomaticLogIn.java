package org.feup.potter.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.feup.potter.client.Util.Util;
import org.feup.potter.client.db.ItemInList;
import org.feup.potter.client.db.User;
import org.feup.potter.client.log_in.SignUp;
import org.feup.potter.client.serverConnection.HttpResponsePassword;
import org.feup.potter.client.serverConnection.LogIn;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class AutomaticLogIn extends Activity implements HttpResponsePassword {
    private LogIn connApi;
    private LunchAppData data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automatic_log_in);

        this.data = (LunchAppData) getApplicationContext();

        this.data.user = (User) Util.loadData(this.data.userPath, this);

        this.data.orderItemIdList = new ArrayList<ItemInList>();
        this.data.hash = (String) Util.loadData(this.data.itemHashPath, this);
        if (this.data.hash == null)
            Log.d("hash", "null");


        // to skip automatic login
        //this.data.user = null;

        // test no data in table
        // this.data.hash = null;

        // drop all tables
        // DataBaseHelper DB = new DataBaseHelper(this);
        // DB.dropAllTables();

        // this.deleteDatabase(DB.DATABASE_NAME);

        // connection api
        if (this.data.user == null) {
            logInFail("First you need to create an account!");
        } else {
            connectToServer(this.data.user.getUsername(), this.data.user.getPassword());
        }
    }

    private void connectToServer(String username, String password) {
        this.connApi = new LogIn(this, username, password); // this must implement HttpResponse interface
        Thread thr = new Thread(this.connApi);
        thr.start();
    }

    @Override
    public void handleResponse(int code, String response, String password) {
        if (code == 200) {
            try {
                JSONObject obj = new JSONObject(response);
                logUser(obj, password);
            } catch (JSONException e) {
                final String resp = response;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        logInFail("");
                    }
                });
            }
        } else {
            final String resp = response;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    logInFail(resp);
                }
            });
        }
    }

    private void logUser(final JSONObject obj, final String password) {
        runOnUiThread(new Runnable() {
                          public void run() {
                              try {
                                  String token = obj.getString("token");
                                  String pin = obj.getString("PIN");

                                  Log.d("token", token);
                                  Log.d("pin", pin);

                                  String[] tokenArr = token.split("\\.");

                                  // encode
                                  // byte[] data = text.getBytes("UTF-8");
                                  // String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                                  if (tokenArr.length >= 2) {
                                      byte[] token2Decode = Base64.decode(tokenArr[1], Base64.DEFAULT);
                                      String tokenData = new String(token2Decode, "utf-8");

                                      System.out.println("Original String: " + tokenData);
                                      JSONObject tokeObj = new JSONObject(tokenData);

                                      if (AutomaticLogIn.this.data.user == null) {
                                          logInFail("");
                                      } else {
                                          AutomaticLogIn.this.data.user = new User(tokeObj.getString("IdCustomer"), tokeObj.getString("Name"), tokeObj.getString("Username"), password, pin, token);

                                          // String id, String name, String username, String password,String pin, String tokan
                                          Util.saveData(AutomaticLogIn.this.data.user, AutomaticLogIn.this.data.userPath, getApplicationContext());

                                          Toast.makeText(getApplicationContext(), "Log In was successful.", Toast.LENGTH_SHORT).show();
                                          Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                          startActivity(intent);
                                      }
                                  } else {
                                      logInFail("");
                                  }
                              } catch (JSONException |
                                      UnsupportedEncodingException e
                                      ) {
                                  logInFail("");
                              }
                          }
                      }
        );
    }

    private void logInFail(String message) {
        Toast.makeText(getApplicationContext(), message.isEmpty() ? "Log in Fail..." : message, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
