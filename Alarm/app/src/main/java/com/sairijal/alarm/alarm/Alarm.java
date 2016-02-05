package com.sairijal.alarm.alarm;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by sayujya on 2016-01-21.
 */
public class Alarm extends RealmObject{
    private long time;
    private int authenticationType;
    private int state;
    private boolean repeatingMonday;
    private boolean repeatingTuesday;
    private boolean repeatingWednesday;
    private boolean repeatingThursday;
    private boolean repeatingFriday;
    private boolean repeatingSaturday;
    private boolean repeatingSunday;
    private String label;

    @Ignore
    public static final int DISTANCE = 0;
    @Ignore
    public static final int MATH = 1;
    @Ignore
    public static final int NONE = 2;
    @Ignore
    public static final int ON = 3;
    @Ignore
    public static final int OFF = 4;
    @Ignore
    public static final int SNOOZE = 5;
    @Ignore
    private static final String[] days = new String[]{"Su, M, T, W, Th, F, S"};
    @Ignore
    private static int snoozeTime = 10;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(int authenticationType) {
        this.authenticationType = authenticationType;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isRepeatingMonday() {
        return repeatingMonday;
    }

    public void setRepeatingMonday(boolean repeatingMonday) {
        this.repeatingMonday = repeatingMonday;
    }

    public boolean isRepeatingTuesday() {
        return repeatingTuesday;
    }

    public void setRepeatingTuesday(boolean repeatingTuesday) {
        this.repeatingTuesday = repeatingTuesday;
    }

    public boolean isRepeatingWednesday() {
        return repeatingWednesday;
    }

    public void setRepeatingWednesday(boolean repeatingWednesday) {
        this.repeatingWednesday = repeatingWednesday;
    }

    public boolean isRepeatingThursday() {
        return repeatingThursday;
    }

    public void setRepeatingThursday(boolean repeatingThursday) {
        this.repeatingThursday = repeatingThursday;
    }

    public boolean isRepeatingFriday() {
        return repeatingFriday;
    }

    public void setRepeatingFriday(boolean repeatingFriday) {
        this.repeatingFriday = repeatingFriday;
    }

    public boolean isRepeatingSaturday() {
        return repeatingSaturday;
    }

    public void setRepeatingSaturday(boolean repeatingSaturday) {
        this.repeatingSaturday = repeatingSaturday;
    }

    public boolean isRepeatingSunday() {
        return repeatingSunday;
    }

    public void setRepeatingSunday(boolean repeatingSunday) {
        this.repeatingSunday = repeatingSunday;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static int getDISTANCE() {
        return DISTANCE;
    }

    public static int getMATH() {
        return MATH;
    }

    public static int getNONE() {
        return NONE;
    }

    public static int getON() {
        return ON;
    }

    public static int getOFF() {
        return OFF;
    }

    public static int getSNOOZE() {
        return SNOOZE;
    }

    public static String[] getDays() {
        return days;
    }

    public static int getSnoozeTime() {
        return snoozeTime;
    }

    public static void setSnoozeTime(int snoozeTime) {
        Alarm.snoozeTime = snoozeTime;
    }
}
