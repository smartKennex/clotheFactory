package com.example.administrator.wishingwell.wishingWell.allviews.baseviews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.wishingwell.wishingWell.models.NotificationCenter;


/**
 * Created by kennex on 2015/2/5.
 */
public class BaseFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NotificationCenter.INSTANCE.addObserver(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NotificationCenter.INSTANCE.removeObserver(this);
    }
}

