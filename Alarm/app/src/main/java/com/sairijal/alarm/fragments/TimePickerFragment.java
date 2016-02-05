package com.sairijal.alarm.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.sairijal.alarm.listeners.AlarmTimeSetListener;

import java.util.Calendar;

/**
 * Created by sayujya on 2016-01-30.
 */
public class TimePickerFragment extends DialogFragment {



        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            AlarmTimeSetListener alarmTimeSetListener = getArguments().getParcelable("Alarm Set Listener");

            return new TimePickerDialog(getActivity(), alarmTimeSetListener, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
        }
    }
