package com.paulusworld.notificationexample;
 
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
 
public class MainActivity extends Activity {
 
    private final String NOTIFICATION_TAG = "NotificationExample";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
     
    public void sendNotification(View view) {
        Intent resultIntent = new Intent(this, NotifiedActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
         
        // Adds the back stack -- defined in the manifest file.
        stackBuilder.addParentStack(NotifiedActivity.class);
        // Adds the intent to the top of the stack.
        stackBuilder.addNextIntent(resultIntent);
        // Create a PendingIntent that contains the back stack.
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
         
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
            // Set appropriate defaults for the notification light, sound,
            // and vibration.
            .setDefaults(Notification.DEFAULT_ALL)
            // Set the required fields.
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle("Notification Title")
            .setContentText("Notification Message")
            // The following are all optional.
            // Use a default priority; recognized by Android 4.1+
            .setPriority(Notification.PRIORITY_DEFAULT)
            // Provide a large icon, shown with the notification
            // in the notification drawer on devices running Android
            // 3.0+
            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
            // Show some preview text.
            .setTicker("Ticker!")
            // Show a number -- this is typically used to show the
            // number of notifications.
            .setNumber(1)
            // If the notification relates to a past or upcoming event
            // You can specify the time in which the notification
            // will appear to have been fired. The following is an
            // example of how to set the notification time. If you
            // don't want to specify a time, then the current time
            // will be used.
            .setWhen(System.currentTimeMillis()) 
            // Set the Intent for when a user touches the notification.
            .setContentIntent(resultPendingIntent)
            // With Android 4.1+ you can add more actions to a notifications.
            // For example, the gmail app. When an email is received, you have
            // the options of replying or deleting the email right from the
            // Notification drawer.
            .addAction(
                    R.drawable.ic_launcher, 
                    "Action", 
                    null)
            // Automatically dismiss the notification when the user taps on it.
            .setAutoCancel(true);
         
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
         
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            notificationManager.notify(NOTIFICATION_TAG, 0, builder.build());
        } else {
            notificationManager.notify(NOTIFICATION_TAG.hashCode(), builder.build());
        }
    }
}