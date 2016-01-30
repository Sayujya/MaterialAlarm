package com.sairijal.alarm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * Created by sayujya on 2016-01-29.
 */
public class AlarmViewFragment extends Fragment {

    private View mMainView;
    private LinearLayoutManager mLinearLayoutManager;
    private AlarmRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;

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

        // enable fab on alarm activity
        if (getActivity() instanceof AlarmActivity) {
            ((AlarmActivity) getActivity()).enableFab();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        // clear cached alarm
        mAdapter.clearAlarms();
    }

    private void initializeRecyclerView() {
        // initialize Linear Layout Manager
        mLinearLayoutManager= new LinearLayoutManager(this.getActivity());

        // set Layout Manager
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        // create and set adapter
        mAdapter = new AlarmRecyclerAdapter(this.getActivity(), null, mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        // callbacks for swipe to remove
        ItemTouchHelper.Callback swipeCallback = new AlarmSwipeTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeCallback);
        touchHelper.attachToRecyclerView(mRecyclerView);

    }
}
