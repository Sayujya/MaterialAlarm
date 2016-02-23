package com.sairijal.alarm.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sairijal.alarm.alarm.Alarm;
import com.sairijal.alarm.adapters.AlarmRecyclerAdapter;
import com.sairijal.alarm.callbacks.AlarmSwipeTouchHelperCallback;
import com.sairijal.alarm.alarm.AlarmWrapper;
import com.sairijal.alarm.R;
import com.sairijal.alarm.application.AlarmApplication;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by sayujya on 2016-01-29.
 */
public class AlarmViewFragment extends Fragment {

    private View mMainView;
    private LinearLayoutManager mLinearLayoutManager;
    private AlarmRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Realm mRealm;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter!=null){
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        // clear cached alarm
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAdapter.clearAlarms(mRealm);
        mRealm.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initializeViews(inflater, container);
        initializeRecyclerView();
        return mMainView;

    }

    private void initializeViews(LayoutInflater inflater, ViewGroup container) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_alarm_list, container, false);

        // link recylerview to layout for alarms
        mRecyclerView = (RecyclerView) mMainView.findViewById(R.id.alarm_recycler_view);
    }

    private void initializeRecyclerView() {
        // initialize Linear Layout Manager
        mLinearLayoutManager= new LinearLayoutManager(this.getActivity());

        // set Layout Manager
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        List<AlarmWrapper> savedAlarms = loadAlarms();
        // create and set adapter
        mAdapter = new AlarmRecyclerAdapter(this.getActivity(), savedAlarms, mLinearLayoutManager);
        mAdapter.addRealmObject(mRealm);
        mRecyclerView.setAdapter(mAdapter);

        // callbacks for swipe to remove
        ItemTouchHelper.Callback swipeCallback = new AlarmSwipeTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeCallback);
        touchHelper.attachToRecyclerView(mRecyclerView);

    }

    private List<AlarmWrapper> loadAlarms() {
        RealmQuery<Alarm> query = mRealm.where(Alarm.class);
        RealmResults<Alarm> savedAlarms = query.findAll();
        Log.i(AlarmApplication.APP_TAG, String.valueOf(savedAlarms.size()));
        List<AlarmWrapper> savedAlarmsList = new ArrayList<>();
        for (Alarm alarm: savedAlarms){
            savedAlarmsList.add(new AlarmWrapper(alarm));
        }
        return  savedAlarmsList;
    }

    public void addAlarm(AlarmWrapper alarm){
        mAdapter.addAlarm(alarm);
    }
}
