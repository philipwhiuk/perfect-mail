package com.whiuk.philip.perfect.activity;


import android.app.Activity;
import android.content.res.TypedArray;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.whiuk.philip.perfect.PerfectMail;
import com.whiuk.philip.perfect.activity.misc.SwipeGestureDetector;
import com.whiuk.philip.perfect.activity.misc.SwipeGestureDetector.OnSwipeGestureListener;


/**
 * This class implements functionality common to most activities used in K-9 Mail.
 *
 * @see MailActivity
 * @see MailListActivity
 */
public class MailActivityCommon {
    /**
     * Creates a new instance of {@link MailActivityCommon} bound to the specified activity.
     *
     * @param activity
     *         The {@link Activity} the returned {@code MailActivityCommon} instance will be bound to.
     *
     * @return The {@link MailActivityCommon} instance that will provide the base functionality of the
     *         "PerfectMail" activities.
     */
    public static MailActivityCommon newInstance(Activity activity) {
        return new MailActivityCommon(activity);
    }


    /**
     * Base activities need to implement this interface.
     *
     * <p>The implementing class simply has to call through to the implementation of these methods
     * in {@link MailActivityCommon}.</p>
     */
    public interface MailActivityMagic {
        void setupGestureDetector(OnSwipeGestureListener listener);
    }


    private Activity mActivity;
    private GestureDetector mGestureDetector;


    private MailActivityCommon(Activity activity) {
        mActivity = activity;
        mActivity.setTheme(PerfectMail.getMailThemeResourceId());
    }

    /**
     * Call this before calling {@code super.dispatchTouchEvent(MotionEvent)}.
     */
    public void preDispatchTouchEvent(MotionEvent event) {
        if (mGestureDetector != null) {
            mGestureDetector.onTouchEvent(event);
        }
    }

    /**
     * Get the background color of the theme used for this activity.
     *
     * @return The background color of the current theme.
     */
    public int getThemeBackgroundColor() {
        TypedArray array = mActivity.getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.colorBackground });

        int backgroundColor = array.getColor(0, 0xFF00FF);

        array.recycle();

        return backgroundColor;
    }

    /**
     * Call this if you wish to use the swipe gesture detector.
     *
     * @param listener
     *         A listener that will be notified if a left to right or right to left swipe has been
     *         detected.
     */
    public void setupGestureDetector(OnSwipeGestureListener listener) {
        mGestureDetector = new GestureDetector(mActivity,
                new SwipeGestureDetector(mActivity, listener));
    }
}
