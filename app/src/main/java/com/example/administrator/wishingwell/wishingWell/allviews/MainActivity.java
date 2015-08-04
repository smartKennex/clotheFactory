package com.example.administrator.wishingwell.wishingWell.allviews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.administrator.wishingwell.R;
import com.example.administrator.wishingwell.wishingWell.WishingWellApp;
import com.example.administrator.wishingwell.wishingWell.allviews.baseviews.BaseFragmentActivity;
import com.example.administrator.wishingwell.wishingWell.allviews.controls.SwipeControllableViewPager;
import com.example.administrator.wishingwell.wishingWell.utils.PhotoHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kennex on 2015/7/17.
 */

public class MainActivity extends BaseFragmentActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String SHOW_CONTENT_KEY = "SHOW_CONTENT_KEY";
    public static final int SHOW_LEFT_QUESTIONS = 1;
    public static final int SHOW_RIGHT_QUESTIONS = 2;

    public static final int FRAG_HOME = 0;
    public static final int FRAG_QUESTIONS = 1;
    public static final int FRAG_DISCOVER = 2;
    public static final int FRAG_MYSELF = 3;
    public static final int TABS_COUNT = 4;

    private List<Button> mTabButtons = new ArrayList<>();
    private SwipeControllableViewPager mFragViewPager;
    private File mCroppedPictureFile;
    private Fragment[] mFragmentList = new Fragment[TABS_COUNT];

    private long mBackPressTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Log.d(TAG, "mainActivity oncreated");
        initViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void initViews() {
        mFragViewPager = (SwipeControllableViewPager) findViewById(R.id.student_main_pager);
        mFragViewPager.setOffscreenPageLimit(3);
        mFragViewPager.setSwipeEnabled(false);
        mFragViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int idx) {
                if (idx < 0 || idx >= TABS_COUNT) {
                    return null;
                }

                Log.d(TAG, "mFragViewPager.getItem, idx: " + idx);

                if (mFragmentList[idx] != null) {
                    return mFragmentList[idx];
                }

                Fragment fragment = null;
                switch (idx) {
                    case FRAG_HOME:
                        fragment = new StudentHomeFragment();
                        break;
                    case FRAG_QUESTIONS:
                        fragment = new StudentHomeFragment();
                        break;
                    case FRAG_DISCOVER:
                        fragment = new StudentHomeFragment();
                        break;
                    case FRAG_MYSELF:
                        fragment = new StudentHomeFragment();
                        break;
                    default:
                        break;
                }
                mFragmentList[idx] = (fragment);
                return fragment;
            }

            @Override
            public int getCount() {
                return TABS_COUNT;
            }
        });

        // 为老师和学生显示不同的界面做些适配
        RadioButton questionsTabBtn = (RadioButton) findViewById(R.id.questions_tab_button);

        mTabButtons.add((RadioButton) findViewById(R.id.home_tab_button));
        mTabButtons.add((RadioButton) findViewById(R.id.questions_tab_button));
        mTabButtons.add((RadioButton) findViewById(R.id.discover_tab_button));
        mTabButtons.add((RadioButton) findViewById(R.id.myself_tab_button));
        for (Button button : mTabButtons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.home_tab_button:
                            mFragViewPager.setCurrentItem(FRAG_HOME);
                            break;
                        case R.id.questions_tab_button:
                            mFragViewPager.setCurrentItem(FRAG_QUESTIONS);
                            break;
                        case R.id.discover_tab_button:
                            mFragViewPager.setCurrentItem(FRAG_DISCOVER);
                            break;
                        case R.id.myself_tab_button:
                            mFragViewPager.setCurrentItem(FRAG_MYSELF);
                            //gotoSettingsActivity();
                            break;
                        default:
                            break;
                    }
                }
            });
        }
        mFragViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < mTabButtons.size(); ++i) {
                    if (mTabButtons.get(i) instanceof RadioButton) {
                        ((RadioButton) mTabButtons.get(i)).setChecked(i == position);
                    }
                }
            }
        });

        Button askButton = (Button) findViewById(R.id.new_cloth_button);
        askButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture2CreateQuestion();
            }
        });

    }

    @Override
    public void finish() {
        //Log.d(TAG, "Finish main activity.", new Exception("Just for debugging..."));
        Log.d(TAG, "finished main activity");
        super.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        long current = System.currentTimeMillis();
        if (current - mBackPressTime < 2000) {
            //退出程序
            WishingWellApp.getInstance().onTerminate();
            System.exit(0);
        } else {
            mBackPressTime = current;
            Toast.makeText(this, R.string.double_click_exit, Toast.LENGTH_LONG).show();
        }
    }

    private void takePicture2CreateQuestion() {
        mCroppedPictureFile = PhotoHelper.takeSquaredPhoto(this, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PhotoHelper.REQUEST_IMAGE_CUSTOM_CAMERA && resultCode == RESULT_OK && mCroppedPictureFile != null) {
            //Intent intent = new Intent(MainActivity.this, CreateClothActivity.class);
            //intent.setData(Uri.fromFile(mCroppedPictureFile));
            //startActivity(intent);
        }
    }
}