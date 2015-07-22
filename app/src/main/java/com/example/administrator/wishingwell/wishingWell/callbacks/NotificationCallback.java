package com.example.administrator.wishingwell.wishingWell.callbacks;

/**
 * Created by kennex on 2015/7/20.
 */

public interface NotificationCallback {
    interface TimerChangedListener {
        void onTimerChanged(int type, int count);
        void onTimerStopped(int type);
    }
}
