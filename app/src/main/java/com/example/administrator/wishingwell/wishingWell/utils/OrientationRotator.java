package com.example.administrator.wishingwell.wishingWell.utils;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import java.util.ArrayList;


public class OrientationRotator {
    private final Activity activity;

    private SensorManager mSensorManager;
    private OnRotationListener mOnRotationListener;
    private int mCurrentRotation = Surface.ROTATION_0;
    private final ArrayList<View> mControlsNeedToRotate = new ArrayList<>();
    private SensorEventListener mSensorEventListener;
    private static final float HIGH_MARK = 6.5f;                // 用于判断手机持向
    private static final float LOW_MARK = 2.5f;                 // 用于判断手机持向

    private void startRotateControls(float from, float to) {
        // 调整旋转方向，避免超过180度的旋转
        from %= 360;
        to %= 360;
        if (Math.abs(to - from) > 180) {
            while (from < 0) {
                from += 360;
            }
            if (from > 180) {
                from -= 360;
            }

            while (to < 0) {
                to += 360;
            }
            if (to > 180) {
                to -= 360;
            }
        }

        for (View v : mControlsNeedToRotate) {
            Animation rotateAnimation = new RotateAnimation(
                    from, to,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(500);
            rotateAnimation.setFillAfter(true);
            v.startAnimation(rotateAnimation);
        }
    }

    public OrientationRotator(Activity activity, int rotation) {
        this(activity);
        setRotation(rotation);
    }

    public OrientationRotator(Activity activity) {
        this.activity = activity;

        mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);

        mSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];

                // 持握朝向判断
                int newRotation = mCurrentRotation;
                if (Math.abs(y) > HIGH_MARK && Math.abs(x) < LOW_MARK) {
                    newRotation = y > 0 ? Surface.ROTATION_0 : Surface.ROTATION_180;
                } else if (Math.abs(x) > HIGH_MARK && Math.abs(y) < LOW_MARK) {
                    // Landscape
                    newRotation = x > 0 ? Surface.ROTATION_270 : Surface.ROTATION_90;
                }

                setRotation(newRotation);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        mOnRotationListener = new OnRotationListener() {
            @Override
            public void onRotate(int newRotation, int oldRotation) {
                startRotateControls(-oldRotation * 90, -newRotation * 90);
            }
        };
    }

    public void setRotation(int newRotation) {
        if (mCurrentRotation != newRotation) {
            Log.v("OrientationRotate", "rotate to " + newRotation * 90);
            int oldRotation = mCurrentRotation;
            mCurrentRotation = newRotation;
            if (mOnRotationListener != null) {
                mOnRotationListener.onRotate(newRotation, oldRotation);
            }
        }
    }

    public void addControl(int id) {
        addControl(activity.findViewById(id));
    }

    public void addControl(View view) {
        if (!mControlsNeedToRotate.contains(view)) {
            mControlsNeedToRotate.add(view);
        }
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            mSensorManager.registerListener(
                    mSensorEventListener,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            mSensorManager.unregisterListener(mSensorEventListener);
        }
    }

    public interface OnRotationListener {
        void onRotate(int newRotation, int oldRotation);
    }
}
