package com.zaitunlabs.dzikirharian.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.zaitunlabs.dzikirharian.R;
import com.zaitunlabs.dzikirharian.activity.DzikirBoard;
import com.zaitunlabs.dzikirharian.activity.InitApp;
import com.zaitunlabs.dzikirharian.services.DzikirReminderService;
import com.zaitunlabs.zlcore.utils.CommonUtils;
import com.zaitunlabs.zlcore.utils.IntegerIDUtils;
import com.zaitunlabs.zlcore.utils.Prefs;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ahmad s on 3/14/2016.
 */
public class DzikirReminderReceiver extends BroadcastReceiver {
    private final int NOTIFICATION_ID_FOR_REMINDER = 100;

    final public static String reminderReceiverTAG = "DzikirReminderReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, reminderReceiverTAG);

        wl.acquire();

        boolean isShow = CommonUtils.getBooleanIntent(intent, DzikirReminderService.PARAM_SHOW_FLAG, false);
        String message = CommonUtils.getStringIntent(intent, DzikirReminderService.PARAM_MESSAGE, "");
        showNotification(context,isShow,message);

        wl.release();
    }

    private void showNotification(Context context, boolean isShow,  String message){
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(isShow) {
            Intent intent = new Intent(context, InitApp.class);

            Notification notification = null;

            long currentTime = Calendar.getInstance().getTimeInMillis();

            String timeMessage = message;
            if(message.contains("petang")){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(Prefs.with(context).getLong(DzikirReminderService.START_PETANG_TIME,0)));
                int minutes = calendar.get(Calendar.MINUTE);
                timeMessage = calendar.get(Calendar.HOUR_OF_DAY)+":"+(minutes>9?minutes:"0"+minutes);

                calendar.setTime(new Date(Prefs.with(context).getLong(DzikirReminderService.END_PETANG_TIME,0)));
                minutes = calendar.get(Calendar.MINUTE);
                timeMessage += " - "+calendar.get(Calendar.HOUR_OF_DAY)+":"+(minutes>9?minutes:"0"+minutes);


                intent.putExtra(InitApp.ARG_NEXT_ACTIVITY_CLASS, DzikirBoard.class);
                intent.putExtra(InitApp.ARG_DATA,"petang");
            }else if(message.contains("pagi")){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(Prefs.with(context).getLong(DzikirReminderService.START_PAGI_TIME,0)));
                int minutes = calendar.get(Calendar.MINUTE);
                timeMessage = calendar.get(Calendar.HOUR_OF_DAY)+":"+(minutes>9?minutes:"0"+minutes);

                calendar.setTime(new Date(Prefs.with(context).getLong(DzikirReminderService.END_PAGI_TIME,0)));
                minutes = calendar.get(Calendar.MINUTE);
                timeMessage += " - "+calendar.get(Calendar.HOUR_OF_DAY)+":"+(minutes>9?minutes:"0"+minutes);

                intent.putExtra(InitApp.ARG_NEXT_ACTIVITY_CLASS, DzikirBoard.class);
                intent.putExtra(InitApp.ARG_DATA,"pagi");
            }


            intent.setAction("com.zaitunlabs.dzikirharian.reminder_notification"+ IntegerIDUtils.getID(context));
            PendingIntent pi = PendingIntent.getActivity(context, 232, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setAutoCancel(false)
                        .setOngoing(true)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(message)
                        .setSubText(timeMessage)
                        .setContentIntent(pi)
                        .setSmallIcon(R.drawable.icon_apps)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

                if((currentTime > Prefs.with(context).getLong(DzikirReminderService.START_PAGI_TIME,currentTime)+10000 && currentTime < Prefs.with(context).getLong(DzikirReminderService.END_PAGI_TIME,0))
                        || (currentTime > Prefs.with(context).getLong(DzikirReminderService.START_PETANG_TIME,currentTime)+10000 && currentTime < Prefs.with(context).getLong(DzikirReminderService.END_PETANG_TIME,currentTime))){
                    //silent
                }else{
                    builder.setDefaults(Notification.DEFAULT_ALL)
                            .setTicker(message);
                }

                notification = builder.build();
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setAutoCancel(false)
                        .setOngoing(true)
                        .setContentTitle(message)
                        .setContentText(timeMessage)
                        .setContentIntent(pi)
                        .setSmallIcon(R.drawable.icon_apps)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

                if((currentTime > Prefs.with(context).getLong(DzikirReminderService.START_PAGI_TIME,currentTime)+10000 && currentTime < Prefs.with(context).getLong(DzikirReminderService.END_PAGI_TIME,0))
                        || (currentTime > Prefs.with(context).getLong(DzikirReminderService.START_PETANG_TIME,currentTime)+10000 && currentTime < Prefs.with(context).getLong(DzikirReminderService.END_PETANG_TIME,currentTime))){
                    //silent
                }else{
                    builder.setDefaults(Notification.DEFAULT_ALL)
                            .setTicker(message);
                }

                notification = builder.getNotification();
            }

            nm.notify(NOTIFICATION_ID_FOR_REMINDER, notification);
        }else{
            nm.cancel(NOTIFICATION_ID_FOR_REMINDER);
        }
    }
}
