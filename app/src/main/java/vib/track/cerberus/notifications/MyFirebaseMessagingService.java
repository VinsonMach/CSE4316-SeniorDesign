package vib.track.cerberus.notifications;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import vib.track.cerberus.R;
import vib.track.cerberus.bluetooth.BluetoothHandler;
import vib.track.cerberus.home.HomepageActivity;
import vib.track.cerberus.network.ServiceApi;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private ServiceApi service;
    SharedPreferences sharedPreferences;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        boolean muteNotifications = sharedPreferences.getBoolean("switch1", false);
        if (muteNotifications)
            return;

        Bundle bundle = new Bundle();
        bundle.putString("icon", "");

        RemoteMessage.Notification notif = remoteMessage.getNotification();
        String title = notif.getTitle();
        String body = notif.getBody();

        String interval = sharedPreferences.getString("textInterval", "50");
        String cycle = sharedPreferences.getString("textCycle", "5");
        String connector = " ";
        String intervalConnect = interval + connector;
        String bleWrite = intervalConnect + cycle;
        //BluetoothHandler.write("50 5");
        BluetoothHandler.write(bleWrite);

        Intent intent = new Intent(this, HomepageActivity.class); // Should this be HistoryList? want recent history
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "RING")
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify((int)(Math.random() * 1024), builder.build());
    }

    @Override
    public void onNewToken(String token) {
        Log.d("RefreshToken", "Refreshed token: " + token);
    }
}
