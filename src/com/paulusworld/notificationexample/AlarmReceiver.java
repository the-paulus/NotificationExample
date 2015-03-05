package com.paulusworld.notificationexample;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = AlarmReceiver.class.getSimpleName();
    
    public AlarmReceiver() {
	// TODO Auto-generated constructor stub
    }

    @Override
    public void onReceive(Context context, Intent intent) {
	Intent resultIntent;
	PendingIntent pendingIntent;
	TaskStackBuilder stackBuilder;
	NotificationManager notificationManager;
	Bundle bundle = intent.getExtras();
	String action = intent.getAction();
	String message = bundle.getString("TIME");
	
	// Create a PowerManager object.
	PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
	/* Crate a WakeLock. The first parameter indicates what kind of WakeLock we want:
	 * PARTIAL_WAKE_LOCK - CPU is on, screen and keyboard are off.
	 * SCREEN_DIM_WAKE_LOCK - CPU is, screen is dim, keyboard is off.
	 * SCREEN_BRIGHT_WAKE_LOCK - CPU is on, the screen is bright, but the keyboard is off.
	 * FULL_WAKE_LOCK - CPU is on, screen is bright, and the keyboard is bright.
	 * 
	 * According to the Android Documentation: 
	 * If you hold a partial wake lock, the CPU will continue to run, regardless of any 
	 * display timeouts or the state of the screen and even after the user presses the power 
	 * button. In all other wake locks, the CPU will run, but the user can still put the device 
	 * to sleep using the power button.
	 * 
	 * There are two additional flags that can be used in combination with all wake logs except 
	 * for partials.
	 * 
	 * ACQUIRE_CAUSES_WAKEUP - Normal wake locks don't actually turn on the illumination. Instead, they cause the illumination to remain on once it turns on (e.g. from user activity). This flag will force the screen and/or keyboard to turn on immediately, when the WakeLock is acquired. A typical use would be for notifications which are important for the user to see immediately.
	 * ON_AFTER_RELEASE - f this flag is set, the user activity timer will be reset when the WakeLock is released, causing the illumination to remain on a bit longer. This can be used to reduce flicker if you are cycling between wake lock conditions.
	 * 
	 * The second parameter is the tag, which is used for debugging.
	 */
	PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
	wakeLock.acquire();
	
	/*
	 * Notifications require the following:
	 * - Small icon
	 * - Title
	 * - Detail text
	 * 
	 * All other settings are optional: http://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html
	 */
	NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle("Alarm")
		.setContentText(message);
	
	// Create an intent for an activity.
	resultIntent = new Intent(context, MainActivity.class);
	
	/**
	 * We need to create a new back stack for when the activity is started. In this case
	 * when the user hits the back button, it will return them to the home screen. 
	 */
	stackBuilder = TaskStackBuilder.create(context);
	
	/*
	 * Adds the back stack for the intent.
	 */
	stackBuilder.addParentStack(MainActivity.class);
	
	// Add the intent that starts the activity to the top of the stack.
	stackBuilder.addNextIntent(resultIntent);
	pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
	
	notificationBuilder.setContentIntent(pendingIntent);
	
	notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	
	/**
	 * If you needed to update an existing notification, call the Notification Manager's
	 * notify function again passing the same id (first parameter). 
	 */
	notificationManager.notify(0, notificationBuilder.build());
	
	/**
	 * To remove the notifications:
	 * 
	 * The user dismisses the notification either individually or by using "Clear All" (if the notification can be cleared).
	 * The user clicks the notification, and you called setAutoCancel() when you created the notification.
	 * You call cancel() for a specific notification ID. This method also deletes ongoing notifications.
	 * You call cancelAll(), which removes all of the notifications you previously issued.
	 */
	wakeLock.release();
    }

}
