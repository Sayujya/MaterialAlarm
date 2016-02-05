package com.sairijal.alarm.listeners;

import android.content.Context;
import android.view.View;

import com.sairijal.alarm.activities.AlarmActivity;

/**
 * Created by sayujya on 2016-01-28.
 */
public class AddAlarmFabListener implements View.OnClickListener {

    Context mContext;

    public AddAlarmFabListener(Context c) {
        super();
        this.mContext = c;
    }

    @Override
    public void onClick(View view) {
        if (mContext instanceof AlarmActivity) {
            ((AlarmActivity) mContext).replaceWithAddFragment();
        }
    }

}
