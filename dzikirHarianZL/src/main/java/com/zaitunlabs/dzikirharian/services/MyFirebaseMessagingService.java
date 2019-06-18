package com.zaitunlabs.dzikirharian.services;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.zaitunlabs.dzikirharian.R;
import com.zaitunlabs.dzikirharian.activity.HomePage;
import com.zaitunlabs.zlcore.utils.NotificationUtils;
import com.zaitunlabs.zlcore.utils.PrefsData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by ahsai on 8/22/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    final public static String smartFirebaseMessagingServiceTAG = "dh:SmartFirebaseMessagingService";

    @Override
    public void onNewToken(String refreshedToken) {
        PrefsData.setPushyToken(refreshedToken);
        PrefsData.setPushyTokenSent(false);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String notifTitle = null;
        String notifBody=null;
        String clickAction=null;
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if(notification != null) {
            notifTitle = remoteMessage.getNotification().getTitle();
            notifBody = remoteMessage.getNotification().getBody();
        }

        Map<String, String> remoteData = remoteMessage.getData();
        Map<String, Object> data = new HashMap<>();
        Set<Map.Entry<String, String>> entrySet = remoteData.entrySet();

        for (Map.Entry<String, String> item : entrySet) {
            data.put(item.getKey(), item.getValue());
        }


        NotificationUtils.onMessageReceived(getBaseContext(),data, notifTitle, notifBody
                ,HomePage.class, null, null, R.string.app_name,R.drawable.icon_apps,
                null);
    }
}
