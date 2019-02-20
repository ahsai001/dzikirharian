package com.zaitunlabs.dzikirharian.receivers;

import android.content.Context;
import android.content.Intent;
import androidx.legacy.content.WakefulBroadcastReceiver;

import com.zaitunlabs.dzikirharian.services.DzikirReminderService;


/**
 * Created by ahmad s on 3/14/2016.
 */

public class ManageDzikirReminderReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        startWakefulService(context, new Intent(context, DzikirReminderService.class));
    }
}
