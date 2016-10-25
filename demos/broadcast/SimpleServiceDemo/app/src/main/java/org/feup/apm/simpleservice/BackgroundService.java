package org.feup.apm.simpleservice;

// BackgroundService.java
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BackgroundService extends Service {
  private static final String TAG = "BackgroundService";
  private NotificationManager notificationMgr;
  private ThreadGroup myThreads = new ThreadGroup("ServiceWorker");
  
  @Override
  public void onCreate() {
    super.onCreate();

    Log.v(TAG, "in onCreate()");
    notificationMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    displayNotificationMessage("Background Service is running");
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    super.onStartCommand(intent, flags, startId);
    int counter = intent.getExtras().getInt("counter");
    Log.v(TAG, "in onStartCommand(), counter = " + counter + ", startId = " + startId);
    new Thread(myThreads, new ServiceWorker(counter), "BackgroundService").start();
    return START_NOT_STICKY;
  }

  class ServiceWorker implements Runnable {
  	private int counter = -1;
	
  	public ServiceWorker(int counter) {
		  this.counter = counter;
	  }

  	public void run() {
      final String TAG2 = "ServiceWorker:" + Thread.currentThread().getId();
    
      // do background processing here...
      try {
			  Log.v(TAG2, "sleeping for 10 seconds. counter = " + counter);
			  Thread.sleep(10000);
			  Log.v(TAG2, "... waking up");
		  } catch (InterruptedException e) {
			  Log.v(TAG2, "... sleep interrupted");
		  }
    }
  }

  @Override
  public void onDestroy() {
    Log.v(TAG, "in onDestroy(). Interrupting threads and cancelling notifications");
    myThreads.interrupt();
    notificationMgr.cancelAll();
    super.onDestroy();
  }

  @Override
  public IBinder onBind(Intent intent) {
    Log.v(TAG, "in onBind()");
    return null;
  }

  private void displayNotificationMessage(String message) {
    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

    Notification notification = new Notification.Builder(this)
        .setSmallIcon(R.drawable.emo_im_winking)
        .setContentTitle(TAG)
        .setContentText(message)
        .setContentIntent(contentIntent)
        .build();
    notification.flags = Notification.FLAG_NO_CLEAR;
    notificationMgr.notify(0, notification);
  }
}
