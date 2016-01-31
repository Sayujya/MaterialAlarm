package com.sairijal.alarm;

import android.content.Context;
import android.view.View;

/**
 * Created by sayujya on 2016-01-30.
 */
public class SetAlarmTimeListener implements View.OnClickListener {

    Context mContext;
    AlarmTimeSetListener mAlarmTimeSetListener;

    public SetAlarmTimeListener(Context c, AlarmTimeSetListener alarmTimeSetListener){
        this.mContext = c;
        this.mAlarmTimeSetListener = alarmTimeSetListener;
    }
    @Override
    public void onClick(View v) {
        if (mContext instanceof AlarmActivity){
            ((AlarmActivity) mContext).showTimePickerDialog(mAlarmTimeSetListener);
        }
    }
}
