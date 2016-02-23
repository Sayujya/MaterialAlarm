package com.sairijal.alarm.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sairijal.alarm.R;
import com.sairijal.alarm.alarm.Alarm;
import com.sairijal.alarm.alarm.AlarmWrapper;
import com.sairijal.alarm.alarm.AlarmWrapperHolder;

import io.realm.Realm;

public class AlarmDetailsActivity extends AppCompatActivity {

    private AlarmWrapper mAlarm;

    private CardView mContainer;
    private RelativeLayout mAlarmCardLayout;
    private TextView mAlarmTime;
    private TextView mAlarmAmPm;
    private TextView mAlarmDays;
    private ToggleButton[] mRepeating = new ToggleButton[7];
    private SwitchCompat mAlarmSwitch;
    private ImageView mAlarmIcon;

    private Realm mRealm;

    @Override
    protected void onStart() {
        super.onStart();
        mRealm= Realm.getDefaultInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_details);

        fetchIntentData();
        findGeneralViews(findViewById(R.id.alarm_general_activity_card));
        findDetailedViews(findViewById(R.id.alarm_details_activity_card));
        fillData();
    }

    @Override
    protected void onStop() {
        mRealm.close();
        super.onStop();
    }

    private void fetchIntentData() {
        mAlarm = AlarmWrapperHolder.getInstance();
    }

    private void findGeneralViews(View itemView) {
        mContainer = (CardView) itemView.findViewById(R.id.alarm_card);
        mAlarmCardLayout = (RelativeLayout) itemView.findViewById(R.id.alarm_card_layout);
        mAlarmTime = (TextView) itemView.findViewById(R.id.alarm_time);
        mAlarmAmPm = (TextView) itemView.findViewById(R.id.alarm_ampm);
        mAlarmDays = (TextView) itemView.findViewById(R.id.alarm_days);
        mAlarmDays.setHeight(0);
        mAlarmSwitch = (SwitchCompat) itemView.findViewById(R.id.alarm_switch);
        mAlarmIcon = (ImageView) itemView.findViewById(R.id.alarm_card_icon);
    }

    private void findDetailedViews(View itemView) {
        mRepeating[0] = (ToggleButton) itemView.findViewById(R.id.monday_repeating);
        mRepeating[1] = (ToggleButton) itemView.findViewById(R.id.tuesday_repeating);
        mRepeating[2] = (ToggleButton) itemView.findViewById(R.id.wednesday_repeating);
        mRepeating[3] = (ToggleButton) itemView.findViewById(R.id.thursday_repeating);
        mRepeating[4] = (ToggleButton) itemView.findViewById(R.id.friday_repeating);
        mRepeating[5] = (ToggleButton) itemView.findViewById(R.id.saturday_repeating);
        mRepeating[6] = (ToggleButton) itemView.findViewById(R.id.sunday_repeating);
    }

    public void setAlarmCardIcon(int alarmType, boolean isOn, ImageView alarmCardIcon){
        if (isOn) {
            alarmCardIcon.setImageResource(R.drawable.ic_alarm_on_24dp);
        } else {
            alarmCardIcon.setImageResource(R.drawable.ic_alarm_off_24dp);
        }
    }

    private void fillData() {

        String[] time = mAlarm.getTime();

        mAlarmTime.setText(time[0]);
        mAlarmAmPm.setText(time[1]);

        mAlarmSwitch.setChecked(mAlarm.getState().equals(Alarm.ON));

        setAlarmCardIcon(mAlarm.getAuthenticationType(), mAlarmSwitch.isChecked(), mAlarmIcon);

        mAlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setAlarmCardIcon(mAlarm.getAuthenticationType(), isChecked, mAlarmIcon);
                mRealm.beginTransaction();
                mAlarm.setState((isChecked)? Alarm.ON:Alarm.OFF);
                mRealm.commitTransaction();
            }
        });
    }
}
