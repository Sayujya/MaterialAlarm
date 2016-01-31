package com.sairijal.alarm;

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
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AlarmActivity extends AppCompatActivity {

    private final static String APP_TAG = "Material Alarm";

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
    private final SimpleDateFormat mWatchTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private final SimpleDateFormat mWatchDate = new SimpleDateFormat("MMMM dd", Locale.getDefault());
    private final SimpleDateFormat mWatchDay = new SimpleDateFormat("EEEE", Locale.getDefault());


    // Activity lifecycle start
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        initializeViews();
        initializeFragment();
        setListeners();
    }


    @Override
    protected void onStart() {
        super.onStart();
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
        mFragmentTransaction.add(R.id.fragment_holder, alarmViewFragment, ALARM_VIEW_FRAGMENT);
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

    protected void replaceWithAddFragment() {
        mFragmentTransaction = mFragmentManager.beginTransaction();
        AddAlarmFragment addAlarmFragment= new AddAlarmFragment();
        mFragmentTransaction.replace(R.id.fragment_holder, addAlarmFragment, ALARM_ADD_FRAGMENT).addToBackStack(ALARM_VIEW_FRAGMENT);
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
}
