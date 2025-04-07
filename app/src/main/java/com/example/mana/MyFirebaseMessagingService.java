package com.example.mana;

import android.annotation.SuppressLint;
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
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.mana.chatPage.ChatPage;
import com.example.mana.mainPage.MainPage;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onCreate() {
        super.onCreate();

    }

    private static final String TAG = "MyFirebaseMsgService";
    String name, roomnum;
    String fromid, nowroom;
    Context context = this;
    PowerManager powerManager;

    PowerManager.WakeLock wakeLock;


    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @SuppressLint("InvalidWakeLockTag")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("loginId", MODE_PRIVATE);
        name = sharedPreferences.getString("loginName", "");
        nowroom = sharedPreferences.getString("NowRoom", "");
        roomnum = remoteMessage.getData().get("room");
        fromid = remoteMessage.getData().get("fromid");
        Log.e("온메세지 리시브", "이름:" + name + "방:" + roomnum);
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.e("온리시브", remoteMessage.getData().get("room") + "아이디" + remoteMessage.getData().get("fromid") + "타입 : " + remoteMessage.getData().get("type"));
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = manager.getRunningTasks(1);
        ComponentName componentName = info.get(0).topActivity;
        String ActivityName = componentName.getShortClassName().substring(1);
        Log.e("현재 액티비티", "" + ActivityName);

        // Check if message contains a data payload.
//            if (remoteMessage.getData().size() > 0) {
//                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//
//                if (/* Check if data needs to be processed by long running job */ true) {
//                    // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                } else {
//                    // Handle message within 10 seconds
//                    handleNow();
//                }
//
//            }

        // Check if message contains a notification payload.
//            if (remoteMessage.getNotification() != null) {
//                Log.e("노티데이터", "데이터 : " + remoteMessage.getData().get("message"));
//                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//                sendNotification(remoteMessage.getNotification().getBody());
//            } else
        /**통화창  호출**/
        if (remoteMessage.getData().size() > 0 && remoteMessage.getData().get("type").equals("face")) {
            String index = remoteMessage.getData().get("index");
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn = pm.isScreenOn();
            if (isScreenOn) {
                context.startActivity(new Intent(context, ReciveFaceTalk.class).putExtra("room", remoteMessage.getData().get("room"))
                        .putExtra("fromid", remoteMessage.getData().get("fromid"))
                        .putExtra("name", remoteMessage.getData().get("name"))
                        .putExtra("index", index)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


//            PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

            } else {



                Intent intent = new Intent(this, MainActivity.class);
                intent.setClassName(getApplicationContext().getPackageName(), ReciveFaceTalk.class.getName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                intent.putExtra("room", remoteMessage.getData().get("room"));
                intent.putExtra("fromid", remoteMessage.getData().get("fromid"));
                intent.putExtra("name", remoteMessage.getData().get("name"));
                intent.putExtra("index", index);

//            PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
                startActivity(intent);

            }


            /**
             * 채팅 노티피수신부
             */
        } else if (remoteMessage.getData().size() > 0 && !remoteMessage.getData().get("room").equals(nowroom) && remoteMessage.getData().get("type").equals("msg")) {
            sendNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("title"));
            if(ActivityName.equals("ChatPage.chatPage")){
            ((ChatPage) ChatPage.chatcontext).LoadChatList();}
            /**
             * 대화신청이나  추천목록 갱신 수신부
             */
        } else if (remoteMessage.getData().size() > 0 && remoteMessage.getData().get("type").equals("server")) {
            sendNotificationServer(remoteMessage.getData().get("message"), remoteMessage.getData().get("title"));
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */

    private void sendNotification(String messageBody, String title) {


        Intent intent = new Intent(this, ChatPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("notifi", "gomsg");
        intent.putExtra("youid", fromid);
        intent.putExtra("room", roomnum);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

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

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelName = "ssssdsadas";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendNotificationServer(String messageBody, String title) {


        Intent intent = new Intent(this, MainPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

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

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelName = "ssssdsadas";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}


