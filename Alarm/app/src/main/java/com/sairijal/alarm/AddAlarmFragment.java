package com.sairijal.alarm;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by sayujya on 2016-01-29.
 */
public class AddAlarmFragment extends Fragment {

    TextView mALarmTime;
    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_add_alarm, container, false);
        initalizeViews();
        addListeners();
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() instanceof AlarmActivity){
            ((AlarmActivity) getActivity()).enableFab();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initalizeViews() {
        mALarmTime = (TextView) mView.findViewById(R.id.time_picker);

    }

    private void addListeners() {
        AlarmTimeSetListener alarmTimeSetListener = new AlarmTimeSetListener(mALarmTime);
        SetAlarmTimeListener setAlarmTimeListener = new SetAlarmTimeListener(this.getActivity(), alarmTimeSetListener);
        mALarmTime.setOnClickListener(setAlarmTimeListener);
    }
}
