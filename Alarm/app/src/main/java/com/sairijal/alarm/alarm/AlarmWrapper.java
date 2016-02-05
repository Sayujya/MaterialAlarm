package com.sairijal.alarm.alarm;

import junit.framework.Assert;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by sayujya on 2016-02-04.
 */
public class AlarmWrapper implements Comparable<AlarmWrapper> {
    private Alarm alarm;
    private static boolean is24Hours;

    public AlarmWrapper(Alarm alarm) {
        this.alarm = alarm;
    }

    @Override
    public int compareTo(AlarmWrapper another) {
        if (this.alarm.getTime()>another.alarm.getTime()){
            return 1;
        } else if (this.alarm.getTime()<another.alarm.getTime()) {
            return -1;
        } else {
            return 0;
        }
    }

    public String[] getTime(){
        // set formatter depending on the system date
        SimpleDateFormat formatter;
        if (AlarmWrapper.is24Hours){
            formatter = new SimpleDateFormat("HH:mm ", Locale.getDefault());
        } else {
            formatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        }
        return formatter.format(alarm.getTime()).split(" ", -1);
    }

    public void setTime(long time) {
        this.alarm.setTime(time);
    }

    public int getAuthenticationType() {
        return this.alarm.getAuthenticationType();
    }

    public void setAuthenticationType(int authenticationType) {
        this.alarm.setAuthenticationType(authenticationType);
    }

    public String getState() {
        switch (this.alarm.getState()){
            case 3:
                return "On";
            case 4:
                return "Off";
            case 5:
                return "Snooze";
        }
        return "Error";
    }

    public void setState(int state) {
        this.alarm.setState(state);
    }

    public boolean[] getRepeating() {
        boolean[] repeating = new boolean[7];
        repeating[0] = this.alarm.isRepeatingMonday();
        repeating[1] = this.alarm.isRepeatingTuesday();
        repeating[2] = this.alarm.isRepeatingWednesday();
        repeating[3] = this.alarm.isRepeatingThursday();
        repeating[4] = this.alarm.isRepeatingFriday();
        repeating[5] = this.alarm.isRepeatingSaturday();
        repeating[6] = this.alarm.isRepeatingSunday();
        return repeating;
    }

    public void setRepeating(boolean[] repeating) {
        Assert.assertEquals(7, repeating.length);
        this.alarm.setRepeatingMonday(repeating[0]);
        this.alarm.setRepeatingTuesday(repeating[1]);
        this.alarm.setRepeatingWednesday(repeating[2]);
        this.alarm.setRepeatingThursday(repeating[3]);
        this.alarm.setRepeatingFriday(repeating[4]);
        this.alarm.setRepeatingSaturday(repeating[5]);
        this.alarm.setRepeatingSunday(repeating[6]);
    }

    public String getLabel() {
        return this.alarm.getLabel();
    }

    public void setLabel(String label) {
        this.alarm.setLabel(label);
    }

    public static int getDISTANCE() {
        return Alarm.getDISTANCE();
    }

    public static int getMATH() {
        return Alarm.getMATH();
    }

    public static int getNONE() {
        return Alarm.getNONE();
    }

    public static int getON() {
        return Alarm.getON();
    }

    public static int getOFF() {
        return Alarm.getOFF();
    }

    public static int getSNOOZE() {
        return Alarm.getSNOOZE();
    }

    public static String[] getDays() {
        return Alarm.getDays();
    }

    public static int getSnoozeTime() {
        return Alarm.getSnoozeTime();
    }

    public static void setSnoozeTime(int snoozeTime) {
        Alarm.setSnoozeTime(snoozeTime);
    }

    public static boolean is24Hours() {
        return is24Hours;
    }

    public static void setIs24Hours(boolean is24Hours) {
        AlarmWrapper.is24Hours = is24Hours;
    }
}
