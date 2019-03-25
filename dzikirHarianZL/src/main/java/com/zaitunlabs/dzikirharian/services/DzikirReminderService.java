package com.zaitunlabs.dzikirharian.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.legacy.content.WakefulBroadcastReceiver;

import com.zaitunlabs.dzikirharian.constants.Constanta;
import com.zaitunlabs.dzikirharian.receivers.DzikirReminderReceiver;
import com.zaitunlabs.dzikirharian.receivers.ManageDzikirReminderReceiver;
import com.zaitunlabs.zlcore.utils.IntegerIDUtils;
import com.zaitunlabs.zlcore.utils.LocationUtils;
import com.zaitunlabs.zlcore.utils.Prefs;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import id.web.michsan.praytimes.Configuration;
import id.web.michsan.praytimes.Method;
import id.web.michsan.praytimes.PrayTimes;
import id.web.michsan.praytimes.Util;

/**
 * Created by ahmad s on 3/14/2016.
 */
public class DzikirReminderService extends JobIntentService {


    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        onHandleIntent(intent);
    }

    protected void onHandleIntent(final Intent intent) {
        //set ashr/dzikir sore reminder and subuh/dzikir pagi reminder
        //getcurrent location

        long dateInMillis = Prefs.with(DzikirReminderService.this).getLong(DZIKIR_PREFS_LAST_UPDATE,0);

        boolean isNeedRunning = true;

        /*
        isNeedRunning = false;
        if(dateInMillis > 0){
            Date lastupdate = new Date(dateInMillis);
            if(CommonUtils.compareToDay(Calendar.getInstance().getTime(),lastupdate)==0){
                isNeedRunning = false;
            }else{
                isNeedRunning = true;
            }
        }else{
            isNeedRunning = true;
        }
        */

        if(isNeedRunning) {
            final LocationUtils helper = new LocationUtils(this);
            helper.setUpdateLocationCallback(new LocationUtils.LocationHelperCallback() {
                @Override
                public void currentLocationUpdate(Location newLocation) {
                    //set new schedule
                    PrayTimes pt = new PrayTimes(Method.MAKKAH);

                    // Adjustments
                    pt.adjust(PrayTimes.Time.FAJR, Configuration.angle(20));
                    pt.adjust(PrayTimes.Time.ISHA, Configuration.angle(18));

                    pt.adjust(PrayTimes.Time.IMSAK, Configuration.minutes(10));

                    // Offset tunings
                    pt.tuneOffset(PrayTimes.Time.FAJR, 2);
                    pt.tuneOffset(PrayTimes.Time.DHUHR, 2);
                    pt.tuneOffset(PrayTimes.Time.ASR, 2);
                    pt.tuneOffset(PrayTimes.Time.MAGHRIB, 2);
                    pt.tuneOffset(PrayTimes.Time.ISHA, 2);

                    pt.setAsrFactor(Method.ASR_FACTOR_STANDARD);

                    // Calculate praytimes
                    double elevation = newLocation.getAltitude();
                    double lat = newLocation.getLatitude();
                    double lng = newLocation.getLongitude();
                    id.web.michsan.praytimes.Location location = new id.web.michsan.praytimes.Location(lat, lng, elevation);
                    // Timezone is defined in the calendar
                    Map<PrayTimes.Time, Double> times = pt.getTimes(new GregorianCalendar(), location);

                    Util.DayTime fajrTime = Util.toDayTime(times.get(PrayTimes.Time.FAJR), false);
                    Util.DayTime syurukTime = Util.toDayTime(times.get(PrayTimes.Time.SUNRISE), false);
                    Util.DayTime dzuhurTime = Util.toDayTime(times.get(PrayTimes.Time.DHUHR), false);
                    Util.DayTime ashrTime = Util.toDayTime(times.get(PrayTimes.Time.ASR), false);
                    Util.DayTime maghribTime = Util.toDayTime(times.get(PrayTimes.Time.MAGHRIB), false);
                    Util.DayTime isyaTime = Util.toDayTime(times.get(PrayTimes.Time.ISHA), false);


                    //=============================================================================================
                    //Dzikir pagi
                    Calendar fajarCalendar = Calendar.getInstance();
                    fajarCalendar.setTime(fajarCalendar.getTime());
                    fajarCalendar.set(Calendar.HOUR_OF_DAY, fajrTime.getHours());
                    fajarCalendar.set(Calendar.MINUTE, fajrTime.getMinutes());

                    setReminderAlarm(fajarCalendar.getTimeInMillis() + (30*60*1000), DzikirReminderReceiver.class, true, "waktunya dzikir pagi", REQUEST_CODE_FOR_START_PAGI);

                    Calendar syurukCalendar = Calendar.getInstance();
                    syurukCalendar.setTime(syurukCalendar.getTime());
                    syurukCalendar.set(Calendar.HOUR_OF_DAY, syurukTime.getHours());
                    syurukCalendar.set(Calendar.MINUTE, syurukTime.getMinutes());

                    setReminderAlarm(syurukCalendar.getTimeInMillis() - (5*60*1000), DzikirReminderReceiver.class, false, "dzikir pagi", REQUEST_CODE_FOR_END_PAGI);


                    //Dzikir petang
                    Calendar asrCalendar = Calendar.getInstance();
                    asrCalendar.setTime(asrCalendar.getTime());
                    asrCalendar.set(Calendar.HOUR_OF_DAY, ashrTime.getHours());
                    asrCalendar.set(Calendar.MINUTE, ashrTime.getMinutes());

                    setReminderAlarm(asrCalendar.getTimeInMillis() + (5*60*1000), DzikirReminderReceiver.class, true, "waktunya dzikir petang", REQUEST_CODE_FOR_START_PETANG);

                    Calendar magribCalendar = Calendar.getInstance();
                    magribCalendar.setTime(magribCalendar.getTime());
                    magribCalendar.set(Calendar.HOUR_OF_DAY, maghribTime.getHours());
                    magribCalendar.set(Calendar.MINUTE, maghribTime.getMinutes());

                    setReminderAlarm(magribCalendar.getTimeInMillis() - (5*60*1000), DzikirReminderReceiver.class, false, "dzikir petang", REQUEST_CODE_FOR_END_PETANG);



                    //=================================================================================================





                    // Set the alarm to start next day 02:00 AM
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(calendar.getTime());
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    calendar.set(Calendar.HOUR_OF_DAY, 2);

                    setManageReminderAlarm(calendar.getTimeInMillis(), ManageDzikirReminderReceiver.class);

                    //set last_update setting that alarm already set
                    Calendar updateCal = Calendar.getInstance();
                    updateCal.setTime(updateCal.getTime());
                    Prefs.with(DzikirReminderService.this).save(DZIKIR_PREFS_LAST_UPDATE, updateCal.getTimeInMillis());


                    //WakefulBroadcastReceiver.completeWakefulIntent(intent);
                }

                
                @Override
                public void failed(String reason) {

                    //Set the alarm to start again in a minutes
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(calendar.getTime());
                    calendar.add(Calendar.MINUTE, 5);

                    setManageReminderAlarm(calendar.getTimeInMillis(), ManageDzikirReminderReceiver.class);

                    //WakefulBroadcastReceiver.completeWakefulIntent(intent);
                }
            });
            helper.init();
            helper.start();
        }else{
            //WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void setManageReminderAlarm(long time, Class receiver){
        AlarmManager alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);

        Intent reminderIntent = new Intent(this, receiver);
        reminderIntent.setAction(Constanta.ACTION_MANAGE_DZIKIR_REMINDER+IntegerIDUtils.getID(this));
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 212, reminderIntent, 0);

        alarmMgr.set(AlarmManager.RTC_WAKEUP, time, alarmIntent);
    }


    private void setReminderAlarm(long time, Class receiver, boolean show, String message, int requestCode){
        AlarmManager alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);

        Intent reminderIntent = new Intent(this, receiver);
        reminderIntent.putExtra(PARAM_SHOW_FLAG, show);
        reminderIntent.putExtra(PARAM_MESSAGE, message);
        reminderIntent.setAction("com.zaitunlabs.dzikirharian.reminder_alarm"+IntegerIDUtils.getID(this));
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 222, reminderIntent, 0);

        alarmMgr.set(AlarmManager.RTC_WAKEUP, time, alarmIntent);


        switch (requestCode){
            case REQUEST_CODE_FOR_START_PAGI:
                Prefs.with(this).save(START_PAGI_TIME,time);
                break;
            case REQUEST_CODE_FOR_END_PAGI:
                Prefs.with(this).save(END_PAGI_TIME,time);
                break;
            case REQUEST_CODE_FOR_START_PETANG:
                Prefs.with(this).save(START_PETANG_TIME,time);
                break;
            case REQUEST_CODE_FOR_END_PETANG:
                Prefs.with(this).save(END_PETANG_TIME,time);
                break;
        }
    }


    public static final String PARAM_SHOW_FLAG = "param_show_flag";
    public static final String PARAM_MESSAGE = "param_message";
    public static final String DZIKIR_PREFS_LAST_UPDATE = "dzikir_prefs_last_update";

    public static final int REQUEST_CODE_FOR_START_PAGI = 0;
    public static final int REQUEST_CODE_FOR_END_PAGI = 1;
    public static final int REQUEST_CODE_FOR_START_PETANG = 2;
    public static final int REQUEST_CODE_FOR_END_PETANG = 3;


    public static final String START_PAGI_TIME = "prefs_start_pagi_timems";
    public static final String END_PAGI_TIME = "prefs_end_pagi_timems";

    public static final String START_PETANG_TIME = "prefs_start_petang_timems";
    public static final String END_PETANG_TIME = "prefs_end_petang_timems";
}
