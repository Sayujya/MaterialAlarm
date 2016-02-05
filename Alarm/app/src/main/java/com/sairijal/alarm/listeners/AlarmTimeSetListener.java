package com.sairijal.alarm.listeners;

import android.app.TimePickerDialog;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.TextView;
import android.widget.TimePicker;

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
        String formattedTime = hourOfDay+":"+minute;
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
