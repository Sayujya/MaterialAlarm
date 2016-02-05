package com.sairijal.alarm.listeners;

import android.view.View;

import com.sairijal.alarm.fragments.AddAlarmFragment;

/**
 * Created by sayujya on 2016-02-03.
 */
public class AddAlarmClickListener implements View.OnClickListener {

    AddAlarmFragment mFragment;

    public AddAlarmClickListener(AddAlarmFragment mFragment) {
        this.mFragment = mFragment;
    }

    @Override
    public void onClick(View v) {
        mFragment.addAlarm();
    }
}
