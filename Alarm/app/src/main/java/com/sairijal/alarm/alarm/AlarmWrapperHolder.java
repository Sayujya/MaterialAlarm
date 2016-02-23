package com.sairijal.alarm.alarm;

import android.util.Log;

import com.sairijal.alarm.application.AlarmApplication;

/**
 * Created by sayujya on 2016-02-21.
 */
public class AlarmWrapperHolder {
    private static AlarmWrapper ourInstance;

    public static AlarmWrapper getInstance() {
        if (ourInstance == null){
            Log.e(AlarmApplication.APP_TAG, "Alarm not found. Are you sure you set the alarm?");
            return null;
        } else {
            return ourInstance;
        }
    }

    public static void setInstance(AlarmWrapper instance) {
        ourInstance = instance;
    }
}
