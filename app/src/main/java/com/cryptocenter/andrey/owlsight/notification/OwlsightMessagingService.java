package com.cryptocenter.andrey.owlsight.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.data.preferences.Preferences;
import com.cryptocenter.andrey.owlsight.di.Scopes;
import com.cryptocenter.andrey.owlsight.ui.screens.groups.GroupsActivity;
import com.cryptocenter.andrey.owlsight.ui.screens.player.SinglePlayerActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Random;

import toothpick.Toothpick;

public class OwlsightMessagingService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "Owlsight channel for remote messages";

    NotificationManager notificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("REMOTE MESSAGE", "MESSAGE RECEIVED!");
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        setupChannels();

        NotificationBody body = fromRemoteMessage(remoteMessage);

        String title;
        Intent notificationIntent;
        String camera = getString(R.string.camera);
        String available = getString(R.string.available);
        String unavailable = getString(R.string.unavailable);
        String motionDetected = getString(R.string.motion_detected);

        switch (body.getType()) {
            default:
            case MOTION_DETECTED:
                title = String.format("%s: %s", body.getCameraName(), motionDetected);
                notificationIntent = SinglePlayerActivity.intent(this, body.getCameraId());
                break;
            case CAMERA_UNAVAILABLE:
                title = String.format("%s %s %s", camera, body.getCameraName(), unavailable);
                notificationIntent = GroupsActivity.intent(this, body.getCameraId());
                break;
            case CAMERA_AVAILABLE:
                title = String.format("%s %s %s", camera, body.getCameraName(), available);
                notificationIntent = SinglePlayerActivity.intent(this, body.getCameraId());
                break;
        }

        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_ONE_SHOT);

        int notificationId = new Random().nextInt(Integer.MAX_VALUE);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setLargeIcon(getNotificationBitmap())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    private Bitmap getNotificationBitmap() {
        return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    }

    private NotificationBody fromRemoteMessage(RemoteMessage remoteMessage) {
        NotificationBody notificationBody = new NotificationBody();
        notificationBody.setAps(new Gson().fromJson(remoteMessage.getData().get("aps"), NotificationBody.Aps.class));
        notificationBody.setCameraId(remoteMessage.getData().get("cameraId"));
        notificationBody.setType(remoteMessage.getData().get("type"));
        return notificationBody;
    }

    private void setupChannels() {
        CharSequence adminChannelName = getString(R.string.notifications_admin_channel_name);
        String adminChannelDescription = getString(R.string.notifications_admin_channel_description);

        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationChannel adminChannel = new NotificationChannel(CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_DEFAULT);
            adminChannel.setDescription(adminChannelDescription);
            adminChannel.enableLights(true);
            adminChannel.setLightColor(Color.RED);
            adminChannel.enableVibration(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(adminChannel);
            }
        }
    }

    @Override
    public void onNewToken(String s) {
        Preferences preferences = Toothpick.openScope(Scopes.APP).getInstance(Preferences.class);
        preferences.setFirebaseNotificationToken(s);
        Log.d("FirebaseToken", String.format("New token =%s", s));
    }

}
