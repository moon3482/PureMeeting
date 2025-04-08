package com.example.mana;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.mana.chatPage.ChatPage;
import com.example.mana.feature.login.MainActivity;
import com.example.mana.mainPage.MainPage;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String name, roomnum;
    String fromid, nowroom;
    Context context = this;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("loginId", MODE_PRIVATE);
        name = sharedPreferences.getString("loginName", "");
        nowroom = sharedPreferences.getString("NowRoom", "");
        roomnum = remoteMessage.getData().get("room");
        fromid = remoteMessage.getData().get("fromid");

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = manager.getRunningTasks(1);
        ComponentName componentName = info.get(0).topActivity;
        assert componentName != null;
        String ActivityName = componentName.getShortClassName().substring(1);

        if (!remoteMessage.getData().isEmpty() && remoteMessage.getData().get("type").equals("face")) {
            String index = remoteMessage.getData().get("index");
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn = pm.isScreenOn();
            if (isScreenOn) {
                context.startActivity(new Intent(context, ReciveFaceTalk.class).putExtra("room", remoteMessage.getData().get("room"))
                        .putExtra("fromid", remoteMessage.getData().get("fromid"))
                        .putExtra("name", remoteMessage.getData().get("name"))
                        .putExtra("index", index)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            } else {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setClassName(getApplicationContext().getPackageName(), ReciveFaceTalk.class.getName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("room", remoteMessage.getData().get("room"));
                intent.putExtra("fromid", remoteMessage.getData().get("fromid"));
                intent.putExtra("name", remoteMessage.getData().get("name"));
                intent.putExtra("index", index);
                startActivity(intent);
            }
        } else if (!remoteMessage.getData().isEmpty() && !remoteMessage.getData().get("room").equals(nowroom) && remoteMessage.getData().get("type").equals("msg")) {
            sendNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("title"));
            if (ActivityName.equals("ChatPage.chatPage")) {
                ((ChatPage) ChatPage.chatcontext).LoadChatList();
            }
        } else if (!remoteMessage.getData().isEmpty() && remoteMessage.getData().get("type").equals("server")) {
            sendNotificationServer(remoteMessage.getData().get("message"), remoteMessage.getData().get("title"));
        }
    }

    private void sendNotification(String messageBody, String title) {
        Intent intent = new Intent(this, ChatPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("notifi", "gomsg");
        intent.putExtra("youid", fromid);
        intent.putExtra("room", roomnum);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        String channelId = "id";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_noti_mana)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setColor(Color.parseColor("#E91E63"))
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "ssssdsadas";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendNotificationServer(String messageBody, String title) {
        Intent intent = new Intent(this, MainPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        String channelId = "id";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_noti_mana)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setColor(Color.parseColor("#E91E63"))
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "ssssdsadas";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        //TODO("토큰갱신 처리")
    }
}


