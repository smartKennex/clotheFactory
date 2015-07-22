package com.example.administrator.wishingwell.wishingWell.service;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by ruoshili on 12/1/14.
 */
public final class ApiClientThread {
    private static final String TAG = ApiClientThread.class.getSimpleName();
    private static final int MaxNoLiveActivitySeconds = 5;
    private static final long PollNotificationInterval = MaxNoLiveActivitySeconds * 1500;

   // private final LinkedBlockingDeque<ServiceCommand> commandQueue = new LinkedBlockingDeque<ServiceCommand>();

   // private final OkHttpClient httpClient = IoHelper.createSslEnabledHttpClient();

    public static final ApiClientThread Instance = new ApiClientThread();


    private ApiClientThread() {
        idleCount = 0;
        //httpClient.setReadTimeout(15, TimeUnit.SECONDS);
       // httpClient.setWriteTimeout(15, TimeUnit.SECONDS);
        //httpClient.setConnectTimeout(15, TimeUnit.SECONDS);
    }

//    public void postPriorityCommand(ServiceCommand command) {
//        Log.d(TAG, "post priority command into queue: " + command.getClass().getSimpleName() + " queue.size =" +
//                commandQueue.size());
//        commandQueue.addFirst(command);
//    }
//
//    public void postCommand(ServiceCommand command) {
//        Log.d(TAG, "post command into queue: " + command.getClass().getSimpleName() + " queue.size =" +
//                commandQueue.size());
//        commandQueue.addLast(command);
//    }

    private boolean isStarted;
    private int idleCount;

    private long lastPollNotificationTimeStamp;


    synchronized public void start() {
        if (isStarted) {
            Log.d(TAG, "API Client thread already started.");
            return;
        }

        Log.d(TAG, "Start API Client thread");

        isStarted = true;
        idleCount = 0;
    }

//        final Thread runnerThread = new Thread(new Runnable() {
//            int noLiveActivityAcc = 0;
//
//            @Override
//            public void run() {
//                while (isStarted) {
//                    try {
//                        idleCount++;
//                        Session session = Session.getCurrent();
//                        if (session != null) {
//                            ServerConfig config = TutorApp.getInstance().getCurrentConfig();
//                            if (idleCount >= session.getKeepAliveInterval()) {
//                                KeepAliveCommand keepAliveCommand =
//                                        new KeepAliveCommand(config.getServersBaseUrl(ServerCategory.Web)[0], session.getId(),
//                                                TutorApp.getInstance().getCurrentUserState() == User.UserState.InWhiteboard ? 1 : 0);
//                                postCommand(keepAliveCommand);
//                                idleCount = 0;
//                            }
//                        }
//
//                        ServiceCommand command = commandQueue.poll(1, TimeUnit.SECONDS);
//                        if (null == command) {
//                            pollNotification();
//
//                            if (TutorApp.getInstance().getLiveActivitiesCount() > 0) {
//                                noLiveActivityAcc = 0;
//                            } else {
//                                noLiveActivityAcc++;
//                            }
//
//                            if (noLiveActivityAcc > MaxNoLiveActivitySeconds) {
//                                isStarted = false;
//                                Log.d(TAG, "Shutdown ApiClientThread and this app");
////                                HomeworkApp.getInstance().onTerminate();
//                                break;
//                            } else {
//                                continue;
//                            }
//                        }
//
//                        command.execute(httpClient);
//                    } catch (InterruptedException e) {
//                        Log.e(TAG, "API Client thread crash", e);
//                        break;
//                    } catch (Exception ex) {
//                        Log.e(TAG, "Execute command failed.", ex);
//                    }
//                }
//            }
//        }});
//        runnerThread.start();
//    }


    private void pollNotification() {
//        long now = new Date().getTime();
//
//        if ((now - lastPollNotificationTimeStamp) < PollNotificationInterval) {
//            return;
//        }
//
//        lastPollNotificationTimeStamp = now;
//
//        PollNotificationCommand pnc = new PollNotificationCommand(TutorApp.getInstance()
//                .getCurrentConfig().getDefaultServersBaseUrl());
//
//        pnc.setServiceCommandListener(new IAsyncOpListener() {
//            @Override
//            public void onComplete(Object result) {
//                @SuppressWarnings("unchecked")
//                final List<NotificationMessage> messages
//                        = (List<NotificationMessage>) result;
//
//                if (messages == null || messages.size() == 0) {
//                    return;
//                }
//
//                final HashMap<Long, List<NotificationMessage>> mMessageMap = new HashMap<>();
//                for (NotificationMessage message : messages) {
//                    if (message == null) {
//                        continue;
//                    }
//
//                    if (TextUtils.isEmpty(message.event)) {
//                        Log.d(TAG, String.format("event is empty, notification: id: %d", message.id));
//                        continue;
//                    }
//
//                    if (message.payload == null) {
//                        Log.d(TAG, String.format("payload is empty, notification: id: %d, event: %s", message.id, message.event));
//                        continue;
//                    }
//
//                    if (TextUtils.equals(message.event, NotificationMessage.ChangeQuestionStatusEvent)) {
//                        //简单处理 只判断messageId最大的一个Id
//                        if (ChangeQuestionStatusAction.LOCK.name().compareToIgnoreCase(message.payload.action) == 0 ||
//                                ChangeQuestionStatusAction.ABANDON.name().compareToIgnoreCase(message.payload.action) == 0) {
//                            if (!mMessageMap.containsKey(message.fromUserId)) {
//                                List<NotificationMessage> list = new ArrayList<>();
//                                list.add(message);
//                                mMessageMap.put(message.fromUserId, list);
//                            } else {
//                                mMessageMap.get(message.fromUserId).add(message);
//                            }
//                            continue;
//                        }
//                    }
//
//                    message.dispatch();
//                }
//                for (Map.Entry entry : mMessageMap.entrySet()) {
//                    @SuppressWarnings("unchecked")
//                    final List<NotificationMessage> val = (List<NotificationMessage>) entry.getValue();
//
//                    int i = 0;
//                    NotificationMessage theLastLockMessage = null;
//                    for (NotificationMessage message : val) {
//                        if (ChangeQuestionStatusAction.LOCK.name()
//                                .compareToIgnoreCase(message.payload.action) == 0) {
//                            theLastLockMessage = message;
//                            ++i;
//                        } else {
//                            --i;
//                        }
//                    }
//                    if (i > 0 && theLastLockMessage != null) {
//                        theLastLockMessage.dispatch();
//                        Log.d(TAG, "dispatch the last lock message");
//                        mMessageMap.clear();
//                        return;
//                    }
//                }
//                mMessageMap.clear();
//            }
//
//            @Override
//            public void onError(Throwable ex) {
//                Log.e(TAG, "PollNotificationCommand failed.", ex);
//            }
//        });
//        ApiClientThread.this.postCommand(pnc);
    }


    synchronized public void stop() {
        if (!isStarted) {
            return;
        }
        isStarted = false;
    }
}
