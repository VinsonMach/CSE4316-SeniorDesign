package vib.track.cerberus.notifications;

import vib.track.cerberus.R;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vib.track.cerberus.data.NotifTokenData;
import vib.track.cerberus.data.NotifTokenResponse;
import vib.track.cerberus.data.RingAuthResponse;
import vib.track.cerberus.home.HomepageActivity;
import vib.track.cerberus.network.RetrofitClient;
import vib.track.cerberus.network.ServiceApi;
import vib.track.cerberus.ring_connect.auth_ring;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private ServiceApi service;
    SharedPreferences sharedPreferences;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Bundle bundle = new Bundle();
        bundle.putString("icon", "");

        RemoteMessage.Notification notif = remoteMessage.getNotification();
        String title = notif.getTitle();
        String body = notif.getBody();

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
