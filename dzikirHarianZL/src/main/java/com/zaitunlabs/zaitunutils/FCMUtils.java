package com.zaitunlabs.zaitunutils;

import android.content.Context;

import com.zaitunlabs.zlcore.utils.Prefs;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ahmad s on 2/24/2016.
 */
public class FCMUtils {
    private static AtomicInteger atomicInteger = null;
    private static final String ATOMIC_INIT_VALUE_FOR_NOTIF = "atomic_init_value_for_fcm";
    private static final int init_value = 0;
    public static int getID(Context context) {
        synchronized (FCMUtils.class){
            if(atomicInteger == null){
                int init = Prefs.with(context).getInt(ATOMIC_INIT_VALUE_FOR_NOTIF, init_value);
                atomicInteger = new AtomicInteger(init);
            }
            int nextValue = atomicInteger.incrementAndGet();
            Prefs.with(context).save(ATOMIC_INIT_VALUE_FOR_NOTIF,nextValue);
            return nextValue;
        }
    }
}