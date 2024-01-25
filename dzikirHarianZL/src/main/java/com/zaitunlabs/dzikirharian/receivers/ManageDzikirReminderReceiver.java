package com.zaitunlabs.dzikirharian.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.JobIntentService;

import com.zaitunlabs.dzikirharian.constants.Constanta;
import com.zaitunlabs.dzikirharian.services.DzikirReminderService;


/**
 * Created by ahmad s on 3/14/2016.
 */

public class ManageDzikirReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() != null && (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) ||
                intent.getAction().startsWith(Constanta.ACTION_MANAGE_DZIKIR_REMINDER))) {
            JobIntentService.enqueueWork(context, DzikirReminderService.class, 1000, new Intent());
        }
    }

    public static void start(Context context){
        Intent setReminderIntent = new Intent(context, ManageDzikirReminderReceiver.class);
        setReminderIntent.setAction(Constanta.ACTION_MANAGE_DZIKIR_REMINDER);
        context.sendBroadcast(setReminderIntent);
    }
}
