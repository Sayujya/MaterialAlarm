package com.sairijal.alarm.listeners;

import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;

/**
 * Created by sayujya on 2016-01-30.
 */
public class FabDisableAnimationListener implements Animation.AnimationListener {

    FloatingActionButton mFab;

    public FabDisableAnimationListener(FloatingActionButton mFab) {
        this.mFab = mFab;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        mFab.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
