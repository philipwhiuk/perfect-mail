package com.whiuk.philip.perfect.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.whiuk.philip.perfect.activity.MailActivityCommon.MailActivityMagic;
import com.whiuk.philip.perfect.activity.misc.SwipeGestureDetector.OnSwipeGestureListener;


public abstract class MailActivity extends Activity implements MailActivityMagic {

    private MailActivityCommon mBase;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mBase = MailActivityCommon.newInstance(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        mBase.preDispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void setupGestureDetector(OnSwipeGestureListener listener) {
        mBase.setupGestureDetector(listener);
    }
}
