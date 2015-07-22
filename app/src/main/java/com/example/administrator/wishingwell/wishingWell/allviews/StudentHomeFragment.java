package com.example.administrator.wishingwell.wishingWell.allviews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.wishingwell.R;
import com.example.administrator.wishingwell.wishingWell.allviews.baseviews.BaseFragment;

/**
 * Created by kennex on 2015/7/17.
 */
public class StudentHomeFragment extends BaseFragment {
    private static final String TAG = StudentHomeFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final View ret = inflater.inflate(R.layout.home_fragment, container, false);

        return ret;
    }
}