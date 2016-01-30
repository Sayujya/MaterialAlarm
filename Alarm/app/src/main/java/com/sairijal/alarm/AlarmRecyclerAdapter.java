package com.sairijal.alarm;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ltorres on 2016-01-13.
 */
public class AlarmRecyclerAdapter extends RecyclerView.Adapter<AlarmRecyclerAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    // app tag for log
    private final String APP_TAG = "com.com.sairijal.alarm";

    // last removed alarm
    private Alarm mRemovedAlarm;

    // list of alarms
    private List<Alarm> mDataset = new ArrayList<Alarm>() {{
        DateFormat formatter = new SimpleDateFormat("hh:mm");
        try {
            add(new Alarm((new Time((formatter.parse("08:30")).getTime())), new boolean[]{true, true, true, true, true, false, false}, Alarm.DISTANCE, "snack!"));
            add(new Alarm((new Time((formatter.parse("12:30")).getTime())), new boolean[]{true, false, true, true, true, false, false}, Alarm.DISTANCE, "snack!"));
            add(new Alarm((new Time((formatter.parse("11:30")).getTime())), new boolean[]{true, false, true, false, true, false, false}, Alarm.DISTANCE, "snack!"));
            add(new Alarm((new Time((formatter.parse("16:30")).getTime())), new boolean[]{true, true, true, true, true, false, false}, Alarm.DISTANCE, "snack!"));
            add(new Alarm((new Time((formatter.parse("19:05")).getTime())), new boolean[]{true, true, true, true, true, true, false}, Alarm.DISTANCE, "snack!"));
            add(new Alarm((new Time((formatter.parse("20:30")).getTime())), new boolean[]{true, true, true, false, true, false, false}, Alarm.DISTANCE, "snack!"));
            add(new Alarm((new Time((formatter.parse("21:10")).getTime())), new boolean[]{true, true, false, true, true, false, false}, Alarm.DISTANCE, "snack!"));
            add(new Alarm((new Time((formatter.parse("12:30")).getTime())), new boolean[]{true, true, true, false, true, false, false}, Alarm.DISTANCE, "snack!"));
            add(new Alarm((new Time((formatter.parse("12:20")).getTime())), new boolean[]{true, true, true, true, true, false, false}, Alarm.DISTANCE, "snack!"));
            add(new Alarm((new Time((formatter.parse("15:30")).getTime())), new boolean[]{true, true, false, true, true, false, false}, Alarm.DISTANCE, "snack!"));
            add(new Alarm((new Time((formatter.parse("12:30")).getTime())), new boolean[]{true, false, true, true, true, false, true}, Alarm.DISTANCE, "snack!"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }};

    // layout manager of the view this adapter is attached to
    private LinearLayoutManager mLayoutManager;

    // last view animated
    private int lastPosition = -1;

    // context of the view
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView mContainer;

        public TextView mAlarmTime;
        public TextView mAlarmDays;
        public SwitchCompat mAlarmSwitch;

        public ViewHolder(View itemView) {
            super(itemView);

            mContainer = (CardView) itemView.findViewById(R.id.alarm_card);
            mAlarmTime = (TextView) itemView.findViewById(R.id.alarm_time);
            mAlarmDays = (TextView) itemView.findViewById(R.id.alarm_days);
            mAlarmSwitch = (SwitchCompat) itemView.findViewById(R.id.alarm_switch);
        }
    }

    public AlarmRecyclerAdapter(Context context, List<Alarm> mDataset, LinearLayoutManager layoutManager) {
        mContext = context;
        mLayoutManager = layoutManager;

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

        // mAla

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

        final Alarm mAlarm = mDataset.get(position);

        holder.mAlarmTime.setText(mAlarm.getTime());
        boolean repeating[] = mAlarm.getRepeating();

        holder.mAlarmDays.setText(mContext.getResources().getText(R.string.alarm_days_text), TextView.BufferType.SPANNABLE);
        for (int i = 0; i < repeating.length; i++) {
            if (repeating[i]) {
                Spannable span = (Spannable) holder.mAlarmDays.getText();
                span.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)), i * 3, (i * 3) + 2,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
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
        mRemovedAlarm = mDataset.remove(position);

        // notify adapter that alarm has been removed
        notifyItemRemoved(position);

        // create arguments for snackbar
        View snackBarView = ((AlarmActivity) mContext).findViewById(R.id.alarm_activity_layout);
        String snackBarText = mRemovedAlarm.getTime().toString() + " alarm deleted";
        UndoDeleteListener undoDeleteListener = new UndoDeleteListener(this, position);

        // create snackbar, add action and show snackbar
        Snackbar.make(snackBarView, snackBarText, Snackbar.LENGTH_LONG)
                .setAction("Undo", undoDeleteListener).
                show();

    }

    public void undoDelete(int position) {

        // if position is zero, scroll to top
        if (position == 0) {
            mLayoutManager.scrollToPosition(0);
        }

        // add alarm back to dataset
        mDataset.add(position, mRemovedAlarm);

        // notify the adapter item has been inserted
        notifyItemInserted(position);

    }

    public void clearAlarms() {

        // cleared cached alarm
        mRemovedAlarm = null;

    }

}
