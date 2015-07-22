package com.example.administrator.wishingwell.wishingWell.models;

import android.os.Handler;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kennex on 2015/7/17.
 */

public class Notification<T> implements InvocationHandler {
    private Handler mainHandler;
    private Map<Object, Boolean> observers = new HashMap<Object, Boolean>();
    private T observerProxy = null;
    private Class<T> callback;

    public Notification(Class<T> callback, Handler handler) {
        this.callback = callback;
        this.mainHandler = handler;
    }

    public void add(Object observer) {
        observers.put(observer, true);
    }

    public void remove(Object observer) {
        observers.remove(observer);
    }

    @Override
    public Object invoke(Object proxy, final Method method, final Object[] args) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                doInvoke(method, args);
            }
        });
        return null;
    }

    private void doInvoke(Method method, Object[] args) {
        for (Object observer : observers.keySet()) {
            try {
                method.invoke(observer, args);
            } catch (Exception e) {
                Log.e("notification", String.format("invoke error, method: %s, error:", method.getName(), e));
            }
        }
    }

    public T getObserver() {
        if (observerProxy == null) {
            observerProxy = (T) Proxy.newProxyInstance(callback.getClassLoader(), new Class<?>[]{callback}, this);
        }
        return observerProxy;
    }

    public void removeAll() {
        observers.clear();
    }
}

