package com.sairijal.alarm;

import android.media.Ringtone;

import com.orm.SugarRecord;

import java.sql.Time;
import java.text.SimpleDateFormat;

/**
 * Created by sayujya on 2016-01-21.
 */
public class Alarm extends SugarRecord implements Comparable{
    public static final int DISTANCE = 0;
    public static final int MATH = 1;
    public static final int NONE = 2;
    public static final int ON = 3;
    public static final int OFF = 4;
    public static final int SNOOZE = 5;
    private static final String[] days = new String[]{"Su, M, T, W, Th, F, S"};


    private static int snoozeTime = 10;

    private Time time;
    private int authenticationType;
    private int state;
    private String label;
    private Ringtone ringtone;

    // boolean to see if the alarm is repeating
    boolean[] repeating = new boolean[]{false, false, false, false, false, false, false};

    public Alarm(Time time, boolean[] repeating, int authenticationType, String label){
        this.time = time;
        this.authenticationType = authenticationType;
        this.repeating = repeating;
        this.label = label;
        //this.ringtone = ringtone;
        this.state = ON;
    }

    public Alarm(Time time, int authenticationType, String label){
        this(time, new boolean[7], authenticationType, label);
    }


    public String getTime() {
        return new SimpleDateFormat("HH:mm").format(time);
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public int getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(int authenticationType) {
        this.authenticationType = authenticationType;
    }

    public String getState() {
        switch (this.state){
            case 3:
                return "On";
            case 4:
                return "Off";
            case 5:
                return "Snooze";
        }
        return "Error";
    }


    public void setState(String state) {
        switch (state.toLowerCase()){
            case "on":
                this.state=ON;
                break;
            case "off":
                this.state=OFF;
                break;
            case "snooze":
                this.state=SNOOZE;
                break;
        }
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean[] getRepeating() {
        return repeating;
    }

    public void setRepeating(boolean... booleans) {
        for (int i = 0; i<7; i++){
            repeating[i] = booleans[i];
        }
    }

    public Ringtone getRingtone() {
        return ringtone;
    }

    public void setRingtone(Ringtone ringtone) {
        this.ringtone = ringtone;
    }

    public void snooze(){
        //snooze
        // todo snooze code
    }

    public static void changeSnoozeTime(int minutes){
        snoozeTime = minutes;
    }


    @Override
    public int compareTo(Object another) {
        Time givenTime = ((Alarm)another).time;

        //ToDo check this logic
        if (givenTime.getTime()<this.time.getTime()){
            return -1;
        } else if (givenTime.getTime()>this.time.getTime()) {
            return 1;
        } else {
            return 0;
        }
    }
}
