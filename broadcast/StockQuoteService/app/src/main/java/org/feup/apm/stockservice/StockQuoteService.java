package org.feup.apm.stockservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class StockQuoteService extends Service {
  private NotificationManager notificationMgr;

  public class StockQuoteServiceImpl extends IStockQuoteService.Stub {
    public String getQuote(String ticker, Person requester) throws RemoteException {
        return "Hello " + requester.getName() + "! Quote for " + ticker + " is $20.00";
    }
  }

  @Override
  public void onCreate() {
      super.onCreate();
      notificationMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
      displayNotificationMessage("onCreate() called");
  }

  @Override
  public void onDestroy()
  {
    // Clear all notifications from this service
    notificationMgr.cancelAll();
    super.onDestroy();
  }

  @Override
  public IBinder onBind(Intent intent)
  {
      displayNotificationMessage("onBind() called");
      return new StockQuoteServiceImpl();
  }

  private void displayNotificationMessage(String message) {
    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent("org.feup.apm.stockclientactivity"), 0);

    Notification notification = new Notification.Builder(this)
        .setSmallIcon(R.drawable.emo_im_happy)
        .setContentTitle("StockQuoteService")
        .setContentText(message)
        .setContentIntent(contentIntent)
        .build();
    notification.flags = Notification.FLAG_NO_CLEAR;
    notificationMgr.notify(R.id.app_notification_id, notification);
  }
}
