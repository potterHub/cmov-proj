package org.feup.apm.standalonereceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class StandAloneReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context ctx, Intent intent) {
	  String message = intent.getStringExtra("message");
	  sendNotification(ctx, message);
	}
	
	private void sendNotification(Context ctx, String message) {
		//Get the notification manager
		NotificationManager nm = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent = new Intent(Intent.ACTION_DIAL);
		PendingIntent pi = PendingIntent.getActivity(ctx, 0, intent, 0);
    Bitmap icon = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.robot);

		//Create Notification Object
		Notification notification = new Notification.Builder(ctx)
                                      .setSmallIcon(R.drawable.robot)
																			.setContentIntent(pi)
																			.setContentTitle("From Me")
																			.setContentText(message)
																			.build();

	  //Send notification
		nm.notify(1, notification);
	}
}

