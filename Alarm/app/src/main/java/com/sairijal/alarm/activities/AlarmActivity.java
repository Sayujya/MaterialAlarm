package com.sairijal.alarm.activities;

import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.sairijal.alarm.alarm.AlarmWrapper;
import com.sairijal.alarm.R;
import com.sairijal.alarm.fragments.AddAlarmFragment;
import com.sairijal.alarm.fragments.AlarmViewFragment;
import com.sairijal.alarm.fragments.TimePickerFragment;
import com.sairijal.alarm.listeners.AddAlarmFabListener;
import com.sairijal.alarm.listeners.AlarmTimeSetListener;
import com.sairijal.alarm.listeners.FabDisableAnimationListener;
import com.sairijal.alarm.listeners.FabEnableAnimationListener;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

public class AlarmActivity extends AppCompatActivity {

    private final static String APP_TAG = "Material Alarm";

    // realm orm object
    Realm mRealm;

    // Views
    private TextView mDate;
    private TextView mTime;
    private TextView mDay;
    private FloatingActionButton mAddFab;

    // fragment manager
    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private FragmentTransaction mFragmentTransaction;

    // fragment tags
    private final static String ALARM_VIEW_FRAGMENT = "ALARM_VIEW_FRAGMENT";
    private final static String ALARM_ADD_FRAGMENT = "ALARM_ADD_FRAGMENT";

    // parcelable tags
    private final static String TIME_CHANGED_LISTENER = "Alarm Set Listener";

    // receivers
    private BroadcastReceiver mTimeBroadcastReceiver;

    // formatters
    private SimpleDateFormat mWatchTime;
    private SimpleDateFormat mWatchDate;
    private SimpleDateFormat mWatchDay ;

    // Activity lifecycle start
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        if (DateFormat.is24HourFormat(this)) {
            mWatchTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        } else {
            mWatchTime = new SimpleDateFormat("hh:mm", Locale.getDefault());
        }
        mWatchDate = new SimpleDateFormat("MMMM dd", Locale.getDefault());
        mWatchDay = new SimpleDateFormat("EEEE", Locale.getDefault());
        initializeViews();
        initializeFragment();
        setListeners();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mRealm = Realm.getDefaultInstance();
        mTimeBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent) {
                // get the current date;
                Date currentDate = new Date();
                // if it's a time tick update time
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    mTime.setText(mWatchTime.format(currentDate));
                }
                // if date is changed, update other textviews as well
                if (intent.getAction().compareTo(Intent.ACTION_DATE_CHANGED) == 0){
                    mDate.setText(mWatchDate.format(currentDate));
                    mDay.setText(mWatchDay.format(currentDate));
                }

            }
        };

        registerReceiver(mTimeBroadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    protected void onResume(){
        super.onResume();

        // set date
        setCurrentDate();

        // set time type
        AlarmWrapper.setIs24Hours(DateFormat.is24HourFormat(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // if receiver exists, unregister receiver
        if (mTimeBroadcastReceiver != null)
            unregisterReceiver(mTimeBroadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
    // Activity lifecycle end

    private void initializeViews() {

        // floating action button initialization
        mAddFab = (FloatingActionButton) findViewById(R.id.fab_add_alarm);

        // date and time initialization
        // mTime textview initialization, add initial time
        mTime = (TextView) findViewById(R.id.current_time);

        // mDate textview initialization, add initial date
        mDate = (TextView) findViewById(R.id.current_date);

        // mDay textview initialization, add initial day
        mDay = (TextView) findViewById(R.id.current_day);

    }

    private void initializeFragment() {
        mFragmentTransaction = mFragmentManager.beginTransaction();
        AlarmViewFragment alarmViewFragment = new AlarmViewFragment();
        AddAlarmFragment addAlarmFragment = new AddAlarmFragment();
        mFragmentTransaction.add(R.id.fragment_holder, alarmViewFragment, ALARM_VIEW_FRAGMENT);
        mFragmentTransaction.add(R.id.fragment_holder, addAlarmFragment, ALARM_ADD_FRAGMENT);
        mFragmentTransaction.commit();
    }

    private void setListeners() {
        // initialize listener for add fab
        AddAlarmFabListener fabListener = new AddAlarmFabListener(this);
        mAddFab.setOnClickListener(fabListener);
    }

    private void setCurrentDate() {
        // get current date
        Date currentDate = new Date();

        // set
        mTime.setText(mWatchTime.format(currentDate));
        mDate.setText(mWatchDate.format(currentDate));
        mDay.setText(mWatchDay.format(currentDate));
    }

    public void replaceWithAddFragment() {
        disableFab(true);
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        mFragmentTransaction.show(mFragmentManager.findFragmentByTag(ALARM_ADD_FRAGMENT)).addToBackStack(ALARM_VIEW_FRAGMENT);
        mFragmentTransaction.hide(mFragmentManager.findFragmentByTag(ALARM_VIEW_FRAGMENT));
        mFragmentTransaction.commit();
    }

    public void disableFab(boolean animate){
        mAddFab.setClickable(false);
        if (animate) {
            Animation disableAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_out);
            disableAnimation.setAnimationListener(new FabDisableAnimationListener(mAddFab));
            mAddFab.setAnimation(disableAnimation);
        } else {
            mAddFab.setVisibility(View.INVISIBLE);
        }
    }

    public void enableFab(boolean animate){
        if (animate) {
            Animation enableAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_in);
            enableAnimation.setAnimationListener(new FabEnableAnimationListener(mAddFab));
            mAddFab.setAnimation(enableAnimation);
        } else {
            mAddFab.setVisibility(View.VISIBLE);
        }
        mAddFab.setClickable(true);
    }

    public void showTimePickerDialog(AlarmTimeSetListener alarmTimeSetListener) {
        DialogFragment newFragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putParcelable("Alarm Set Listener", alarmTimeSetListener);
        newFragment.setArguments(args);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void addAlarm(AlarmWrapper alarm) {
        mFragmentTransaction = mFragmentManager.beginTransaction();
        AlarmViewFragment alarmViewFragment = (AlarmViewFragment) mFragmentManager.findFragmentByTag(ALARM_VIEW_FRAGMENT);
        mFragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        mFragmentTransaction.hide(mFragmentManager.findFragmentByTag(ALARM_ADD_FRAGMENT));
        enableFab(true);
        mFragmentTransaction.show(mFragmentManager.findFragmentByTag(ALARM_VIEW_FRAGMENT));
        mFragmentTransaction.commit();
        alarmViewFragment.addAlarm(alarm);
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.findFragmentByTag(ALARM_ADD_FRAGMENT).isVisible()) {
            enableFab(true);
        }
        super.onBackPressed();
    }

}
