package com.example.administrator.wishingwell.wishingWell.allviews.baseviews;

/**
 * Created by kennex on 2015/7/17.
 */

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.administrator.wishingwell.wishingWell.models.NotificationCenter;

public abstract class BaseFragmentActivity extends FragmentActivity {
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

    @Override
    public void finish() {
        NotificationCenter.INSTANCE.removeObserver(this);
        super.finish();
    }
}
