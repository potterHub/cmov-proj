package org.feup.potter.client.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import org.feup.potter.client.db.ItemInList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Util {

    public static String getStringFromBitmap(Bitmap bitmapPicture) {
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    public static Bitmap getBitmapFromString(String jsonString) {
        byte[] decodedString = Base64.decode(jsonString, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public static synchronized void saveData(Object obj, String path, Context context) {
        try {
            // creates a private file
            FileOutputStream fileOut = context.openFileOutput(path, Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(obj);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            Log.d("saving " + path, e.getMessage());
        }
    }

    public static synchronized Object loadData(String path, Context context) {
        Object obj = null;
        try {
            FileInputStream fis = context.openFileInput(path);
            ObjectInputStream is = new ObjectInputStream(fis);
            obj = is.readObject();
            is.close();
            fis.close();
        } catch (ClassNotFoundException | IOException e) {
            Log.d("loading user", e.getMessage());
        }
        return obj;
    }

    public static byte[] getBytesForOrder(String username,ArrayList<ItemInList> orderList) throws JSONException, UnsupportedEncodingException {
        JSONObject jsonOrder = new JSONObject();
        jsonOrder.put("idUser", username);
        JSONArray itemJsonArray = new JSONArray();
        for (ItemInList item : orderList) {
            JSONObject itemJson = new JSONObject();
            itemJson.put("idItem", item.getIdItem());
            itemJson.put("quantity", item.getQuantity());
            itemJsonArray.put(itemJson);
        }
        jsonOrder.put("items", itemJsonArray);

        Log.d("QR code","Json: " + jsonOrder.toString());
        return jsonOrder.toString().getBytes("ISO-8859-1");
    }
}
