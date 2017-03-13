package com.aftarobot.traffic.department.admin.services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.aftarobot.traffic.department.admin.DeptMainActivity;
import com.aftarobot.traffic.department.admin.R;
import com.aftarobot.traffic.library.data.FCMData;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

/**
 * Created by aubreymalabie on 3/9/17.
 */

public class DeptAdminMessagingService extends FirebaseMessagingService {

    public DeptAdminMessagingService() {
    }

    public static final String TAG = DeptAdminMessagingService.class.getSimpleName(),
            BROADCAST_EMERGENCY = "com.aftarobot.BROADCAST_EMERGENCY",
            BROADCAST_INSTRUCTION = "com.aftarobot.BROADCAST_INSTRUCTION";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        boolean isRunning = isMainActivityRunning("com.aftarobot.traffic.officer");
        Log.w(TAG, "onMessageReceived: com.aftarobot.traffic.officer isRunning: " + isRunning);
        //check for notifcations without app data structures
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "onMessageReceived: this is a NOTIFICATION from the console" );
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            sendNotification(title,body);
            return;
        }

        try {
            Map<String, String> map = remoteMessage.getData();

            FCMData data = new FCMData();
            data.setMessage(map.get("message"));
            data.setTitle(map.get("title"));
            data.setFromUser(map.get("fromUser"));
            data.setUserID(map.get("userID"));
            data.setJson(map.get("json"));
            data.setMessageType(Integer.parseInt(map.get("messageType")));
            data.setAnnouncementID(map.get("announcementID"));
            if (map.get("expiryDate") != null)
                data.setExpiryDate(Long.parseLong(map.get("expiryDate")));
            if (map.get("date") != null)
                data.setDate(Long.parseLong(map.get("date")));
            Log.d(TAG, "onMessageReceived: " + gson.toJson(data));
            LocalBroadcastManager bm = LocalBroadcastManager.getInstance(getApplicationContext());
            Intent m = null;
            switch (data.getMessageType()) {
                case FCMData.EMERGENCY:
                    m = new Intent(BROADCAST_EMERGENCY);
                    bm.sendBroadcast(m);
                    Log.d(TAG, "onMessageReceived: broadcast sent to notify of emergency condition");
                    break;
                case FCMData.INSTRUCTION:
                    m = new Intent(BROADCAST_INSTRUCTION);
                    bm.sendBroadcast(m);
                    Log.d(TAG, "onMessageReceived: broadcast sent to notify of incoming instruction");
                    break;
                case FCMData.ANNOUNCEMENT:
                    break;
                case FCMData.WELCOME:
                    break;
            }

            sendNotification(data);


        } catch (Exception e) {
            Log.e(TAG, "onMessageReceived: ", e);
        }
    }

    private void sendNotification(FCMData data) {
        if (data.getTitle() == null) {
            data.setTitle("Department Administrator");
        }
        StringBuilder sb = new StringBuilder();
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_hand)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentTitle(data.getTitle())
                        .setContentText(data.getMessage());

        Intent resultIntent = new Intent(this, DeptMainActivity.class);
        resultIntent.putExtra("data", data);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setTicker(data.getMessage());
        int mNotificationId = 0011;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        Log.e(TAG, "sendNotification: notification has been sent");


    }
    private void sendNotification(String title, String body) {

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_hand)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentTitle(title)
                        .setContentText(body);

        Intent resultIntent = new Intent(this, DeptMainActivity.class);
        resultIntent.putExtra("title", title);
        resultIntent.putExtra("body", body);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setTicker(body);
        int mNotificationId = 0011;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        Log.e(TAG, "sendNotification: notification has been sent");


    }

    public boolean isMainActivityRunning(String packageName) {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (int i = 0; i < tasksInfo.size(); i++) {
            Log.d(TAG, "isMainActivityRunning: " +
                    tasksInfo.get(i).baseActivity.getPackageName().toString());
            if (tasksInfo.get(i).baseActivity.getPackageName().toString().equals(packageName))
                return true;
        }

        return false;
    }

    static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
}
