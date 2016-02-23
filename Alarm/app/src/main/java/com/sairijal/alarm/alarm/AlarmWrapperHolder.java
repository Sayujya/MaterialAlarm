package com.sairijal.alarm.alarm;

/**
 * Created by sayujya on 2016-02-21.
 */
public class AlarmWrapperHolder {
    private static AlarmWrapperHolder ourInstance = new AlarmWrapperHolder();

    public static AlarmWrapperHolder getInstance() {
        return ourInstance;
    }

    private AlarmWrapperHolder() {
    }
}
