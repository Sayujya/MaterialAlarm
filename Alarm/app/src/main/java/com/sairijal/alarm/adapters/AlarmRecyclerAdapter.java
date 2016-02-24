package com.sairijal.alarm.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sairijal.alarm.activities.AlarmDetailsActivity;
import com.sairijal.alarm.R;
import com.sairijal.alarm.activities.AlarmActivity;
import com.sairijal.alarm.alarm.Alarm;
import com.sairijal.alarm.alarm.AlarmWrapper;
import com.sairijal.alarm.alarm.AlarmWrapperHolder;
import com.sairijal.alarm.listeners.UndoDeleteListener;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;

/**
 * Created by ltorres on 2016-01-13.
 */
public class AlarmRecyclerAdapter extends RecyclerView.Adapter<AlarmRecyclerAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    // tag for sending alarm position to details activity
    public static final String ALARM_POSITION_TAG = "alarm";

    // tags for adapter
    public static final String ADAPTER_TAG = "/time";

    // last removed alarm
    private List<AlarmWrapper> mRemovedAlarm = new ArrayList<AlarmWrapper>();

    // list of alarms
    private List<AlarmWrapper> mDataset = new ArrayList<AlarmWrapper>();

    // layout manager of the view this adapter is attached to
    private LinearLayoutManager mLayoutManager;

    // last view animated
    private int lastPosition = -1;

    // realm instance, not updated until required
    private Realm mRealm;

    // context of the view
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView mContainer;

        public RelativeLayout mAlarmCardLayout;

        public TextView mAlarmTime;
        public TextView mAlarmAmPm;
        public TextView mAlarmDays;
        public SwitchCompat mAlarmSwitch;
        public ImageView mAlarmIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            mContainer = (CardView) itemView.findViewById(R.id.alarm_card);
            mAlarmCardLayout = (RelativeLayout) itemView.findViewById(R.id.alarm_card_layout);
            mAlarmTime = (TextView) itemView.findViewById(R.id.alarm_time);
            mAlarmAmPm = (TextView) itemView.findViewById(R.id.alarm_ampm);
            mAlarmDays = (TextView) itemView.findViewById(R.id.alarm_days);
            mAlarmSwitch = (SwitchCompat) itemView.findViewById(R.id.alarm_switch);
            mAlarmIcon = (ImageView) itemView.findViewById(R.id.alarm_card_icon);
        }
    }

    public AlarmRecyclerAdapter(Context context, List<AlarmWrapper> mDataset, LinearLayoutManager layoutManager) {
        mContext = context;
        mLayoutManager = layoutManager;
        DateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

        boolean is24Hour = android.text.format.DateFormat.is24HourFormat(mContext);

        this.mDataset = mDataset;

        Collections.sort(this.mDataset);


        // todo update when creating alarms is supported
        //this.mDataset = mDataset;
        //Collections.sort(mDataset);
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AlarmRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        final View stockCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_card, parent, false);
        // link views to class
        final ViewHolder alarmViewHolder = new ViewHolder(stockCard);

        alarmViewHolder.mAlarmTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //ToDo Show alarm details when clicked
                    Toast.makeText(mContext, "Alarm Clicked", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        return alarmViewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final AlarmWrapper mAlarm = mDataset.get(position);

        holder.mAlarmCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmWrapperHolder.setInstance(mAlarm);
                Intent alarmDetailsIntent = new Intent(mContext, AlarmDetailsActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext,
                            Pair.create((View) holder.mContainer, mContext.getString(R.string.card_transition_name)),
                            Pair.create((View) holder.mAlarmAmPm, mContext.getString(R.string.card_ampm_transition_name)),
                            Pair.create((View) holder.mAlarmTime, mContext.getString(R.string.card_time_transition_name)),
                            Pair.create((View) holder.mAlarmDays, mContext.getString(R.string.card_days_transition_name)));
                    mContext.startActivity(alarmDetailsIntent, options.toBundle());
                } else {
                    mContext.startActivity(alarmDetailsIntent);
                }
            }
        });

            String[] alarmTime = mAlarm.getTime();

            holder.mAlarmTime.setText(alarmTime[0]);
            holder.mAlarmAmPm.setText(alarmTime[1]);

            holder.mAlarmSwitch.setChecked(mAlarm.getState().equals(Alarm.ON));

                    setAlarmCardIcon(mAlarm.getAuthenticationType(), holder.mAlarmSwitch.isChecked(), holder.mAlarmIcon);

            holder.mAlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setAlarmCardIcon(mAlarm.getAuthenticationType(), isChecked, holder.mAlarmIcon);
                    mRealm.beginTransaction();
                    mAlarm.setState((isChecked) ? Alarm.ON : Alarm.OFF);
                    mRealm.commitTransaction();
                }
            });

            boolean repeating[] = mAlarm.getRepeating();
            holder.mAlarmDays.setText(mContext.getResources().getText(R.string.alarm_days_text), TextView.BufferType.SPANNABLE);
            for (int i = 0; i < repeating.length; i++) {
                if (repeating[i]) {
                    Spannable span = (Spannable) holder.mAlarmDays.getText();
                    span.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)), i * 3, (i * 3) + 2,
                            Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
            }

            // set animation
            setAnimation(holder.mContainer, position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        // return size of items in recyclerview
        return mDataset.size();
    }

    private void setAnimation(final View viewToAnimate, int position) {
        // if the view hasn't been animated yet
        if (position > lastPosition) {
            // add an animation
            final Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            // attach animation
            viewToAnimate.startAnimation(animation);
            // update count of views animated
            lastPosition = position;
        }
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onItemDismiss(final int position) {

        // remove and cache the alarm
        mRemovedAlarm.add(mDataset.remove(position));

        // notify adapter that alarm has been removed
        notifyItemRemoved(position);

        // create arguments for snackbar
        View snackBarView = ((AlarmActivity) mContext).findViewById(R.id.alarm_activity_layout);
        String[] snackBarTextTime = mRemovedAlarm.get(mRemovedAlarm.size()-1).getTime();

        UndoDeleteListener undoDeleteListener = new UndoDeleteListener(this, position);

        // create snackbar, add action and show snackbar
        Snackbar deletedSnackBar = Snackbar.make(snackBarView, snackBarTextTime[0] + " " + snackBarTextTime[1] + " alarm deleted", Snackbar.LENGTH_LONG);
        deletedSnackBar.setAction("Undo", undoDeleteListener);
        deletedSnackBar.show();

    }

    public void undoDelete(int position) {

        scrollToPositionIfNeeded(position);

        AlarmWrapper restoredAlarm = mRemovedAlarm.remove(mRemovedAlarm.size() - 1);

        // add alarm back to dataset
        mDataset.add(position, restoredAlarm);

        // notify the adapter item has been inserted
        notifyItemInserted(position);

    }

    public void clearAlarms() {
        // cleared cached alarm
        mRealm.beginTransaction();
        for (AlarmWrapper alarm: mRemovedAlarm){
            alarm.removeFromRealm();
        }
        mRemovedAlarm.clear();
        mRealm.commitTransaction();
    }

    public void addAlarm(AlarmWrapper alarm){
        mDataset.add(alarm);
        Collections.sort(mDataset);
        int insertedPosition = mDataset.indexOf(alarm);
        scrollToPositionIfNeeded(insertedPosition);
        notifyItemInserted(insertedPosition);
    }

    private void scrollToPositionIfNeeded(int position) {
        mLayoutManager.scrollToPosition(position);
    }

    public void addRealmObject(Realm realm){
        this.mRealm = realm;
    }

    public void setAlarmCardIcon(int alarmType, boolean isOn, ImageView alarmCardIcon){
        if (isOn) {
            alarmCardIcon.setImageResource(R.drawable.ic_alarm_on_24dp);
        } else {
            alarmCardIcon.setImageResource(R.drawable.ic_alarm_off_24dp);
        }
        // // TODO: 2016-01-28 set alarm icon depending on alarm type
        /*int image;
        switch (mAlarm.getAuthenticationType()){
            case Alarm.DISTANCE:
                image = R.drawable.ic_location_on_black_24dp;
                break;
            case Alarm.MATH:
                image = R.drawable.ic_exposure_black_24dp;
                break;
            default:
                image = R.drawable.ic_cancel_black_24dp;
                break;
        }
        holder.mAlarmDays.setCompoundDrawablesWithIntrinsicBounds( image, 0, 0, 0);*/
    }

}
