package com.sairijal.alarm.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sairijal.alarm.alarm.Alarm;
import com.sairijal.alarm.activities.AlarmActivity;
import com.sairijal.alarm.alarm.AlarmWrapper;
import com.sairijal.alarm.R;
import com.sairijal.alarm.listeners.AddAlarmClickListener;
import com.sairijal.alarm.listeners.AlarmTimeSetListener;
import com.sairijal.alarm.listeners.SetAlarmTimeListener;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

import io.realm.Realm;

/**
 * Created by sayujya on 2016-01-29.
 */
public class AddAlarmFragment extends Fragment {

    private View mView;
    private TextView mALarmTime;
    private EditText mLabel;
    private ToggleButton[] mRepeating;
    private Button mCancel;
    private Button mConfirm;
    private Realm mRealm;

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
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
        mRealm.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initalizeViews() {
        mALarmTime = (TextView) mView.findViewById(R.id.time_picker);
        mLabel = (EditText) mView.findViewById(R.id.name);
        mRepeating = new ToggleButton[7];
        mRepeating[0] = (ToggleButton) mView.findViewById(R.id.monday_repeating);
        mRepeating[1] = (ToggleButton) mView.findViewById(R.id.tuesday_repeating);
        mRepeating[2] = (ToggleButton) mView.findViewById(R.id.wednesday_repeating);
        mRepeating[3] = (ToggleButton) mView.findViewById(R.id.thursday_repeating);
        mRepeating[4] = (ToggleButton) mView.findViewById(R.id.friday_repeating);
        mRepeating[5] = (ToggleButton) mView.findViewById(R.id.saturday_repeating);
        mRepeating[6] = (ToggleButton) mView.findViewById(R.id.sunday_repeating);

        mCancel = (Button) mView.findViewById(R.id.add_cancel);
        mConfirm = (Button) mView.findViewById(R.id.add_confirm);
    }

    private void addListeners() {
        AlarmTimeSetListener alarmTimeSetListener = new AlarmTimeSetListener(mALarmTime);
        SetAlarmTimeListener setAlarmTimeListener = new SetAlarmTimeListener(this.getActivity(), alarmTimeSetListener);
        mALarmTime.setOnClickListener(setAlarmTimeListener);

        AddAlarmClickListener confirmClickListener = new AddAlarmClickListener(this);
        mConfirm.setOnClickListener(confirmClickListener);

    }

    public void addAlarm(){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Time time = new Time(formatter.parse(this.mALarmTime.getText().toString()).getTime());
            boolean[] repeating = new boolean[7];
            for (int i=0; i<7; i++){
                repeating[i] = this.mRepeating[i].isChecked();
            }
            int alarmType = Alarm.NONE;
            String label = mLabel.getText().toString();
            AlarmWrapper newAlarm = createAlarm(time, repeating, alarmType);
            if (getActivity() instanceof  AlarmActivity){
                ((AlarmActivity) getActivity()).addAlarm(newAlarm);
            }
            getFragmentManager().popBackStack();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private AlarmWrapper createAlarm(Time time, boolean[] repeating, int alarmType) {
        Alarm newAlarm = new Alarm();
        newAlarm.setTime(time.getTime());
        newAlarm.setRepeatingMonday(repeating[0]);
        newAlarm.setRepeatingTuesday(repeating[1]);
        newAlarm.setRepeatingWednesday(repeating[2]);
        newAlarm.setRepeatingThursday(repeating[3]);
        newAlarm.setRepeatingFriday(repeating[4]);
        newAlarm.setRepeatingSaturday(repeating[5]);
        newAlarm.setRepeatingSunday(repeating[6]);
        newAlarm.setAuthenticationType(alarmType);
        newAlarm.setState(Alarm.ON);
        newAlarm.setUniqueID(UUID.randomUUID().toString());

        mRealm.beginTransaction();
        newAlarm = mRealm.copyToRealm(newAlarm);
        mRealm.commitTransaction();
        return new AlarmWrapper(newAlarm);
    }

}
