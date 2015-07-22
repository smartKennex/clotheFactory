package com.example.administrator.wishingwell.wishingWell.models;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by kennex on 15/3/20.
 */

public class TimerWatcher {
    private Context mContext;
    private  OnTimerChangedListener mListener;
    private Timer mTimer;
    private HashMap<Integer, TimerTask> mTaskMap;
    private List<Integer> mPauseTask = new ArrayList<Integer>();
    private  int mCallTimeCount = CALLING_STUDENT_SECONDS;
    private  int mWBTimeCount = WHITEBOARD_BEGIN_SECONDS;
    private  int mWaitingCreate = WAITING_CREATE_SECONDS;
    private int mTeacherQuickResponse = TEACHER_QUICK_RESPONSE_SECONDS;

    public static final String TAG = TimerWatcher.class.getSimpleName();

    public static final int CALLING_TIMER_TYPE = 0;
    public static final int WHITEBOARD_TIMER_TYPE = 1;
    public static final int WAITING_TO_CREATE_TYPE = 2;
    public static final int TEACHER_RESPONDING_TYPE = 3;
    public static final int STUDENT_REALTIME_PUSH = 4;
    public static final int TEACHER_REPSONDED_FAIL = 5;

    private static final int CALLING_STUDENT_SECONDS = 29;
    private static final int CALLING_TEACHER_SECONDS = 39;
    private static final int WHITEBOARD_BEGIN_SECONDS = 1;
    private static final int WAITING_CREATE_SECONDS = 20;
    private static final int TEACHER_QUICK_RESPONSE_SECONDS = 60;
    private static final int TEACHER_RESPONED_FALI = 30;//30秒还没有抢答到结果 认为抢答失败

    public TimerWatcher(Context context) {
        mContext = context;
        createHandler();
        mTimer = new Timer();
        mTaskMap = new HashMap<>();
    }

    private static class SmartHandler extends Handler {
        private WeakReference<TimerWatcher> mOuter;
        public SmartHandler(TimerWatcher watcher) {
            mOuter =  new WeakReference<TimerWatcher>(watcher);
        }

        public boolean isOuterExist() {
            return mOuter.get() != null;
        }
    }
    private  SmartHandler  mHandler;

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setOnTimerChangedListener(OnTimerChangedListener listener) {
        mListener = listener;
    }

    // 回调接口
    public interface OnTimerChangedListener {
        void onTimerChanged(int type, int timer);
        void onTimerStopped(int type);
    }

    private void createHandler() {
        mHandler = new SmartHandler(this) {
            @Override
            public void handleMessage(Message msg) {

                if (!mHandler.isOuterExist()) {
                    return;
                }
                if (!mTaskMap.containsKey(msg.what)){
                    //任务已经被外部结束掉了
                    return;
                }
                TimerTask task = mTaskMap.get(msg.what);
                switch (msg.what) {
                    case WHITEBOARD_TIMER_TYPE:
                        if (!mPauseTask.contains(msg.what)) {
                            mListener.onTimerChanged(WHITEBOARD_TIMER_TYPE, mWBTimeCount);
                            ++mWBTimeCount;
                        }
                        break;

                    case CALLING_TIMER_TYPE:
                        mListener.onTimerChanged(CALLING_TIMER_TYPE, mCallTimeCount);
                        --mCallTimeCount;
                        if (mCallTimeCount < 0) {
                            task.cancel();
                            mTaskMap.remove(msg.what);
                            mListener.onTimerStopped(msg.what);
                        }
                        break;

                    case WAITING_TO_CREATE_TYPE:
                        --mWaitingCreate;
                        if (mWaitingCreate < 0){
                            task.cancel();
                            mTaskMap.remove(msg.what);
                            mListener.onTimerStopped(msg.what);
                        }
                        break;

                    case TEACHER_RESPONDING_TYPE:
                    case TEACHER_RESPONED_FALI:
                        --mTeacherQuickResponse;
                        if (mTeacherQuickResponse < 0) {
                            task.cancel();
                            mTaskMap.remove(msg.what);
                            mListener.onTimerStopped(msg.what);
                        } else {
                            mListener.onTimerChanged(msg.what,mTeacherQuickResponse);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private  void createTaskByType(final int type) {
        if (mPauseTask.contains(type)) {
            mPauseTask.remove((Integer)type);
        }
        if (mTaskMap.containsKey(type)) {
            return;
        }
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(type);
            }
        };

        switch (type) {
            case CALLING_TIMER_TYPE:
                break;
            case WHITEBOARD_TIMER_TYPE:
                mWBTimeCount = WHITEBOARD_BEGIN_SECONDS;//白板做正计时
                break;
            case WAITING_TO_CREATE_TYPE:
                mWaitingCreate = WAITING_CREATE_SECONDS;//进白板前的 逻辑处理时间
                break;

            case TEACHER_RESPONED_FALI:
                mTeacherQuickResponse = TEACHER_RESPONED_FALI;
                break;

            case TEACHER_RESPONDING_TYPE:
                mTeacherQuickResponse = TEACHER_QUICK_RESPONSE_SECONDS;
                break;
            default:
                break;
        }
        mTaskMap.put(type, task);

        mTimer.schedule(task, 1000,1000);
    }

    /**
     * 开始
     */
    public void startWatch(int type) {
        //timer开始执行
        if (mTimer == null) {
            mTimer = new Timer();
        }

        createTaskByType(type);
    }

    public void stopWatch(int type) {
        if (mTaskMap.isEmpty()) {
            return;
        }
        if (mTaskMap.containsKey(type)) {
            TimerTask task = mTaskMap.get(type);
            task.cancel();
            task = null;
            mTaskMap.remove(type);
        }
        if (type == CALLING_TIMER_TYPE) {
        }
        mWBTimeCount = 0;
        mWaitingCreate = WAITING_CREATE_SECONDS;
        mCallTimeCount = CALLING_STUDENT_SECONDS;
        mTeacherQuickResponse = TEACHER_QUICK_RESPONSE_SECONDS;
    }

    public void pauseTimerWatch(int type) {
        if (mTaskMap.isEmpty()) {
            return;
        }
        if (!mPauseTask.contains(type)) {
            mPauseTask.add(type);
        }
    }

    /**
     * 结束
     */
    public void stopAllWatch() {
        Iterator it = mTaskMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            int key = (int)entry.getKey();
            TimerTask val = (TimerTask)entry.getValue();
            val.cancel();
            val = null;
        }
        mPauseTask.clear();
        mTaskMap.clear();

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    public int getLengthOfWhiteboard(){
        return mWBTimeCount;
    }

}
