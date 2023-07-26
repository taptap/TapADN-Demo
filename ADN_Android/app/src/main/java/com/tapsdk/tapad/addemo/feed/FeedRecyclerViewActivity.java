package com.tapsdk.tapad.addemo.feed;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.WindowManager;

import com.tapsdk.tapad.addemo.R;


public class FeedRecyclerViewActivity extends AppCompatActivity {

    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_feed);

        try {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        } catch (Throwable ignore) {
        }

        initViews();
    }

    private void initViews() {
        TabLayout categoryTabLayout = findViewById(R.id.categoryTabLayout);
        categoryTabLayout.addTab(categoryTabLayout.newTab().setText("视频"));
        categoryTabLayout.addTab(categoryTabLayout.newTab().setText("图片"));

        final ViewPager feedViewPager = findViewById(R.id.feedViewPager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), categoryTabLayout.getTabCount());
        feedViewPager.setAdapter(pagerAdapter);
        feedViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(categoryTabLayout));

        categoryTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                feedViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void finish() {
        super.finish();
    }

    static class PagerAdapter extends FragmentPagerAdapter {

        private int num;

        public PagerAdapter(FragmentManager fm, int num) {
            super(fm);
            this.num = num;
        }


        @Override
        public Fragment getItem(int i) {
            return createItem(i);
        }

        private Fragment createItem(int i) {
            if (1 == i) {
                return new NormalFeedAdFragment();
            } else {
                return new VideoFeedAdFragment();
            }
        }

        @Override
        public int getCount() {
            return num;
        }
    }
}
