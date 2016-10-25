package org.feup.apm.testrestservice;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
  TextView tv = null;
  EditText edtIp = null, edtId = null, edtName;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    tv = (TextView) findViewById(R.id.tv_response);
    edtIp = (EditText) findViewById(R.id.edt_IP);
    edtId = (EditText) findViewById(R.id.edt_id);
    edtName = (EditText) findViewById(R.id.edt_name);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.getusers) {
      GetUsers getUsers = new GetUsers(edtIp.getText().toString());
      Thread thr = new Thread(getUsers);
      thr.start();
      return true;
    }
    else if (id == R.id.getuser) {
      GetUser getUser = new GetUser(edtIp.getText().toString(), edtId.getText().toString());
      Thread thr = new Thread(getUser);
      thr.start();
      return true;
    }
    else if (id == R.id.adduser) {
      AddUser addUser = new AddUser(edtIp.getText().toString(), edtName.getText().toString());
      Thread thr = new Thread(addUser);
      thr.start();
      return true;
    }
    else if (id == R.id.deluser) {
      DelUser delUser = new DelUser(edtIp.getText().toString(), edtId.getText().toString());
      Thread thr = new Thread(delUser);
      thr.start();
      return true;
    }
    else if (id == R.id.changeuser) {
      ChUser chUser = new ChUser(edtIp.getText().toString(), edtId.getText().toString(), edtName.getText().toString());
      Thread thr = new Thread(chUser);
      thr.start();
      return true;
    }
    else if (id == R.id.clear) {
      tv.setText(R.string.tv_start_value);
      return true;
    }
      
    return super.onOptionsItemSelected(item);
  }
  
  private void appendText(final String text) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        tv.setText(tv.getText() + "\n" + text);
      }
    });
  }
  
  private void writeText(final String text) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        tv.setText(text);
      }
    });
  }

  
  private String readStream(InputStream in) {
    BufferedReader reader = null;
    StringBuffer response = new StringBuffer();
    try {
      reader = new BufferedReader(new InputStreamReader(in));
      String line = "";
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
    }
    catch (IOException e) {
      return e.getMessage();
    }
    finally {
      if (reader != null) {
        try {
          reader.close();
        } 
        catch (IOException e) {
          return e.getMessage();
        }
      }
    }
    return response.toString();
  } 
  
//**************************************************************************
// Internal class to call REST operation GetUsers
  
  private class GetUsers implements Runnable {
    String address = null;
    
    GetUsers(String baseAddress) {
      address = baseAddress;
    }
    
    @Override
    public void run() {
      URL url;
      HttpURLConnection urlConnection = null;
      
      try {
        url = new URL("http://" + address + ":8701/Rest/users");
        writeText("GET " + url.toExternalForm());
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setUseCaches (false);
        
        int responseCode = urlConnection.getResponseCode();
        if(responseCode == 200) {
          String response = readStream(urlConnection.getInputStream());
          appendText(response);
        }
        else
          appendText("Code: " + responseCode);
      }
      catch (Exception e) {
        appendText(e.toString());
      } 
      finally {
        if(urlConnection != null)
          urlConnection.disconnect();
      }
    }
  }
  
//**************************************************************************
//Internal class to call REST operation GetUser
 
  private class GetUser implements Runnable {
    String address = null;
    String uid = null;
    
    GetUser(String baseAddress, String userId) {
      address = baseAddress;
      uid = userId;
    }
    
    @Override
    public void run() {
      URL url;
      HttpURLConnection urlConnection = null;
      
      try {
        url = new URL("http://" + address + ":8701/Rest/users/" + uid);
        writeText("GET " + url.toExternalForm());
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setUseCaches (false);

        int responseCode = urlConnection.getResponseCode();
        if(responseCode == 200) {
          String response = readStream(urlConnection.getInputStream());
          appendText(response);
        }
        else
          appendText("Code: " + responseCode);
      }
      catch (Exception e) {
        appendText(e.toString());
      } 
      finally {
        if(urlConnection != null)
          urlConnection.disconnect();
      }
    }
  }
  
//**************************************************************************
//Internal class to call REST operation AddUser
 
  private class AddUser implements Runnable {
    String address = null;
    String uname = null;
    
    AddUser(String baseAddress, String userName) {
      address = baseAddress;
      uname = userName;
    }
    
    @Override
    public void run() {
      URL url;
      HttpURLConnection urlConnection = null;
      
      try {
        url = new URL("http://" + address + ":8701/Rest/users");
        writeText("POST " + url.toExternalForm());
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setUseCaches (false);

        DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
        String payload = "\"" + uname + "\"";
        appendText("payload: " + payload);
        outputStream.writeBytes(payload);
        outputStream.flush();
        outputStream.close();

        // get response
        int responseCode = urlConnection.getResponseCode();
        if(responseCode == 200) {
          String response = readStream(urlConnection.getInputStream());
          appendText(response);
        }
        else
          appendText("Code: " + responseCode);
      }
      catch (Exception e) {
        appendText(e.toString());
      } 
      finally {
        if(urlConnection != null)
          urlConnection.disconnect();
      }
    }
  }
  
//**************************************************************************
//Internal class to call REST operation DeleteUser
 
  private class DelUser implements Runnable {
    String address = null;
    String uid = null;
    
    DelUser(String baseAddress, String userId) {
      address = baseAddress;
      uid = userId;
    }
    
    @Override
    public void run() {
      URL url;
      HttpURLConnection urlConnection = null;
      
      try {
        url = new URL("http://" + address + ":8701/Rest/users/" + uid);
        writeText("DELETE " + url.toExternalForm());
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("DELETE");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setUseCaches (false);

        // get response
        int responseCode = urlConnection.getResponseCode();
        appendText("Code: " + responseCode);
      }
      catch (Exception e) {
        appendText(e.toString());
      } 
      finally {
        if(urlConnection != null)
          urlConnection.disconnect();
      }
    }
  }
  
//**************************************************************************
//Internal class to call REST operation ChangeUser
 
  private class ChUser implements Runnable {
    String address = null;
    String uid = null;
    String uname = null;
    
    ChUser(String baseAddress, String userId, String userName) {
      address = baseAddress;
      uid = userId;
      uname = userName;
    }
    
    @Override
    public void run() {
      URL url;
      HttpURLConnection urlConnection = null;
      
      try {
        url = new URL("http://" + address + ":8701/Rest/users/" + uid);
        writeText("PUT " + url.toExternalForm());
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("PUT");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setUseCaches (false);

        DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
        String payload = "\"" + uname + "\"";
        appendText("payload: " + payload);
        outputStream.writeBytes(payload);
        outputStream.flush();
        outputStream.close();

        // get response
        int responseCode = urlConnection.getResponseCode();
        appendText("Code: " + responseCode);
      }
      catch (Exception e) {
        appendText(e.toString());
      } 
      finally {
        if(urlConnection != null)
          urlConnection.disconnect();
      }
    }
  }
  
}
