package com.sairijal.alarm.listeners;

import android.app.TimePickerDialog;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;

import com.sairijal.alarm.alarm.AlarmWrapper;
import com.sairijal.alarm.application.AlarmApplication;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by sayujya on 2016-01-30.
 */
public class AlarmTimeSetListener implements TimePickerDialog.OnTimeSetListener, Parcelable {

    TextView mAlarmTime;

    public AlarmTimeSetListener(TextView mAlarmTime) {
        this.mAlarmTime = mAlarmTime;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        SimpleDateFormat formatter;
        Log.i(AlarmApplication.APP_TAG, hourOfDay + ":" + minute);
        if (AlarmWrapper.is24Hours()){
            formatter = new SimpleDateFormat("HH:mm ", Locale.getDefault());
        } else {
            formatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        }
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedTime = formatter.format((hourOfDay * 3600 + minute * 60) * 1000);
        mAlarmTime.setText(formattedTime);
    }

    protected AlarmTimeSetListener(Parcel in) {
        mAlarmTime = (TextView) in.readValue(TextView.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mAlarmTime);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AlarmTimeSetListener> CREATOR = new Parcelable.Creator<AlarmTimeSetListener>() {
        @Override
        public AlarmTimeSetListener createFromParcel(Parcel in) {
            return new AlarmTimeSetListener(in);
        }

        @Override
        public AlarmTimeSetListener[] newArray(int size) {
            return new AlarmTimeSetListener[size];
        }
    };
}
