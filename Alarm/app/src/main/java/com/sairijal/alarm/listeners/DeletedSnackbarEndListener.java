package com.sairijal.alarm.listeners;

import android.content.Context;
import android.support.design.widget.Snackbar;

import com.sairijal.alarm.fragments.AlarmViewFragment;

/**
 * Created by sayujya on 2016-02-05.
 */
public class DeletedSnackbarEndListener extends Snackbar.Callback {

    AlarmViewFragment mFragment;

    @Override
    public void onDismissed(Snackbar snackbar, int event) {
        super.onDismissed(snackbar, event);
        if (event != Snackbar.Callback.DISMISS_EVENT_ACTION){

        }
    }
}
