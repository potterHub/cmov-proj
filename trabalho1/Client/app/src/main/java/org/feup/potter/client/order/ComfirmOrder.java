package org.feup.potter.client.order;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.feup.potter.client.LunchAppData;
import org.feup.potter.client.R;
import org.feup.potter.client.db.ItemInList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ComfirmOrder extends Activity {
    private LunchAppData data;

    private ImageView qrCodeImageview;
    private TextView errorTv;
    private TextView titleTv;

    private volatile String invalidQrCode;

    private String contentStr = null;

    public final static int QR_WIDTH_HEIGHT = 500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order_qr_code);

        this.data = (LunchAppData) getApplicationContext();

        qrCodeImageview = (ImageView) findViewById(R.id.img_qr_code_image);
        titleTv = (TextView) findViewById(R.id.title);
        errorTv = (TextView) findViewById(R.id.error);

        this.invalidQrCode = "";
        String content = "";
        try {
            JSONObject jsonOrder = new JSONObject();
            jsonOrder.put("idUser", data.user.getUsername());
            JSONArray itemJsonArray = new JSONArray();
            for (ItemInList item : data.orderItemList) {
                JSONObject itemJson = new JSONObject();
                itemJson.put("idItem", item.getIdItem());
                itemJson.put("quantity", item.getQuantity());
                itemJsonArray.put(itemJson);
            }
            jsonOrder.put("items", itemJsonArray);

            Log.d("QR code","Json: " + jsonOrder.toString());
            byte[] contentBytes = jsonOrder.toString().getBytes("ISO-8859-1");

            contentStr = new String(contentBytes, "ISO-8859-1");
        } catch (UnsupportedEncodingException | JSONException e) {
            errorTv.setText(e.getMessage());
        }

        titleTv.setText("Your Order QR is...");

        Thread t = new Thread(new Runnable() {  // do the creation in a new thread to avoid ANR Exception
            public void run() {
                try {
                    BitMatrix result = new MultiFormatWriter().encode(contentStr, BarcodeFormat.QR_CODE, QR_WIDTH_HEIGHT, QR_WIDTH_HEIGHT, null);
                    int w = result.getWidth();
                    int h = result.getHeight();
                    int[] pixels = new int[w * h];
                    for (int y = 0; y < h; y++) {
                        int offset = y * w;
                        for (int x = 0; x < w; x++) {
                            pixels[offset + x] = result.get(x, y) ? getResources().getColor(R.color.colorPrimary) : getResources().getColor(R.color.white);
                        }
                    }
                    final Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                    bitmap.setPixels(pixels, 0, 500, 0, 0, w, h);
                    runOnUiThread(new Runnable() {  // runOnUiThread method used to do UI task in main thread.
                        @Override
                        public void run() {
                            qrCodeImageview.setImageBitmap(bitmap);
                        }
                    });
                } catch (IllegalArgumentException | WriterException e) {
                    invalidQrCode += e.getMessage() + "\n";
                }
                if (!invalidQrCode.isEmpty())
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            errorTv.setText(invalidQrCode);
                        }
                    });
            }
        });
        t.start();
    }
}
