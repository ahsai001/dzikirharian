package com.zaitunlabs.dzikirharian.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.zaitunlabs.dzikirharian.R;
import com.zaitunlabs.dzikirharian.activity.InitApp;
import com.zaitunlabs.zaitunutils.FCMUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ahsai on 8/22/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    final public static String smartFirebaseMessagingServiceTAG = "SmartFirebaseMessagingService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        PowerManager pm = (PowerManager) getBaseContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, smartFirebaseMessagingServiceTAG);

        wl.acquire();

        Map<String, String> data = remoteMessage.getData();

        Notification notif = null;

        if(data != null){
            // data
            String title = data.get("title");
            String body = data.get("body");
            if(data.containsKey("action") && data.get("action").equals("WAKEUP")) {
            } else {
                HashMap<String, String> intentData = new HashMap<>();
                notif = getFCMNotification(title, body,InitApp.class, intentData);
            }
        }else{
            // notification
            HashMap<String, String> intentData = new HashMap<>();
            notif = getFCMNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),InitApp.class, intentData);
        }

        if(notif != null){
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(FCMUtils.getID(getBaseContext()), notif);
        }


        wl.release();
    }

    public Notification getFCMNotification(String title, String content, Class nextActivity,
                                           HashMap<String, String> data){
        Intent intent = new Intent(this, nextActivity);

        if(data != null) {
            Object[] keys = data.keySet().toArray();
            for (Object key : keys) {
                intent.putExtra((String)key, data.get(key));
            }
        }

        PendingIntent pi = PendingIntent.getActivity(getBaseContext(), 131, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = null;

        String notifTitle = TextUtils.isEmpty(title) ? getString(R.string.app_name) : title;
        String notifText = TextUtils.isEmpty(content) ? getString(R.string.app_name) : content;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setAutoCancel(false)
                .setContentTitle(notifTitle)
                .setContentText(notifText)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notifText))
                //.setSubText("subtext")
                .setContentIntent(pi);

        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher));
        builder.setSmallIcon(R.drawable.ic_launcher);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            notification = builder.getNotification();
        }
        return notification;
    }
}
