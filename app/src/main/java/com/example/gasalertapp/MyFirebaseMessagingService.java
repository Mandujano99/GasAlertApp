package com.example.gasalertapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle FCM messages here.
        if (remoteMessage.getNotification() != null) {
            // Display the notification
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                showNotification(title, body);
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "your_channel_id";
        NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
