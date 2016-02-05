package com.sairijal.alarm.listeners;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sairijal.alarm.AlarmRecyclerAdapter;

/**
 * Created by sayujya on 2016-01-29.
 */
public class UndoDeleteListener implements View.OnClickListener {

    AlarmRecyclerAdapter mRecyclerAdapter;
    int mPosition;

    // constructor
    public UndoDeleteListener(AlarmRecyclerAdapter recyclerAdapter, int positon){
        // set member fields
        this.mRecyclerAdapter = recyclerAdapter;
        this.mPosition = positon;
    }

    @Override
    public void onClick(View v) {

        // undo delete
        mRecyclerAdapter.undoDelete(mPosition);
    }
}
