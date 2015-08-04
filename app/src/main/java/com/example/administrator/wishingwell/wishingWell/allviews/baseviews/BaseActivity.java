package com.example.administrator.wishingwell.wishingWell.allviews.baseviews;

/**
 * Created by kennex on 2015/8/4.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.administrator.wishingwell.wishingWell.models.NotificationCenter;


/**
 * Created by caijw on 2015/2/5.
 */
@SuppressLint("Registered")
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationCenter.INSTANCE.addObserver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationCenter.INSTANCE.removeObserver(this);
    }
}

