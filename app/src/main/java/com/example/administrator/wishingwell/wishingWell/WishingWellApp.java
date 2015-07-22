package com.example.administrator.wishingwell.wishingWell;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.util.Log;

import com.example.administrator.wishingwell.wishingWell.callbacks.NotificationCallback;
import com.example.administrator.wishingwell.wishingWell.models.DeviceInfo;
import com.example.administrator.wishingwell.wishingWell.models.NotificationCenter;
import com.example.administrator.wishingwell.wishingWell.models.TimerWatcher;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;




/**
 * Created by kennex on 2015/7/17.
 */

public class WishingWellApp extends Application {
    private static final String TAG = WishingWellApp.class.getSimpleName();


    public final static String SHARED_PREF_NAME = "SHARE_PREF_FOR_WISHING_WELL";
    public final static String USER_NAME_PREF_KEY = "user_name_pref_key";
    public final static String PASSWORD_HASH_PREF_KEY = "password_hash_pref_key";

    private final static AtomicReference<WishingWellApp> instance = new AtomicReference<>();

    public final ScheduledThreadPoolExecutor scheduleTaskExecutor = new ScheduledThreadPoolExecutor(1);
    private final AtomicInteger liveActivitiesCount = new AtomicInteger(0);
    private SharedPreferences sharedPref;

    private Activity mFromActivity = null;

    public static WishingWellApp getInstance() {
        return instance.get();
    }


    private DeviceInfo deviceInfo;
    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }
    private TimerWatcher mTimerWatcher;

    /**
     * 获取安装包版本名
     *
     * @return
     */
    public String getVersionName() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;
        } catch (Throwable e) {
            Log.w(TAG, "get version name failed.");
        }
        return "Unknown Version";
    }

    /**
     * 获得安装包版本号
     *
     * @return
     */
    public int getVersionCode() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionCode;
        } catch (Throwable e) {
            Log.w(TAG, "get version name failed.");
        }
        return 0;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "on HomeworkApp create");

        final Thread.UncaughtExceptionHandler prevHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                //noinspection EmptyCatchBlock
                try {
                    Log.wtf(TAG,
                            "Uncaught exception thrown from thread id: "
                                    + (thread == null ? "unknown" : String.valueOf(thread.getId())),
                            ex);
                    if (prevHandler != null) {
                        prevHandler.uncaughtException(thread, ex);
                    }
                    close();
                } catch (Throwable t) {
                }
                System.exit(2);
            }
        });

        instance.set(this);

        sharedPref = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        initNotificationCenter();

        // start api client thread


        createTimerWatcher();
    }


    private final AtomicReference<Activity> currentActivity = new AtomicReference<>();

    public Activity getCurrentActivity() {
        return currentActivity.get();
    }


    public void close() {
        if (mTimerWatcher != null) {
            mTimerWatcher.stopAllWatch();
        }

        //ApiClientThread.Instance.stop();
    }

    @Override
    public void onTerminate() {
        Log.d(TAG, "onTerminate");
        close();
        super.onTerminate();
    }


    private void initNotificationCenter() {
        NotificationCenter.INSTANCE.addCallbacks(NotificationCallback.TimerChangedListener.class);
    }

    private void createTimerWatcher() {
        mTimerWatcher = new TimerWatcher(this);
    }

    public void startTimerWatcher(int type) {
        mTimerWatcher.startWatch(type);
    }

    public void stopTimerWatcher(int type) {
        mTimerWatcher.stopWatch(type);
    }

    public void pauseTimerWatcher(int type) {
        mTimerWatcher.pauseTimerWatch(type);
    }
}