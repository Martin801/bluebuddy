package com.bluebuddy.activities;

import android.graphics.Typeface;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluebuddy.R;
import com.bluebuddy.adapters.TabsPagerAdapter;
import com.bluebuddy.fragments.AboutFragment;
import com.bluebuddy.fragments.EventFragment;

public class RoughActivity extends BuddyActivity {


    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;


    private boolean mIsTheTitleVisible = false;
    private AppBarLayout appbar;
    private ImageView coverImage;
    private LinearLayout linearlayoutTitle;
    private Toolbar toolbar;
    private TextView textviewTitle, textviewTitle2;
    private CollapsingToolbarLayout collapsing;


    private void findViews() {
        collapsing = (CollapsingToolbarLayout) findViewById(R.id.collapsing);

        appbar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tx = (TextView) findViewById(R.id.txtUserName);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");
        tx.setTypeface(custom_font);
        TextView IDtxt2 = (TextView) findViewById(R.id.txtLocation);
        TextView IDtxt4 = (TextView) findViewById(R.id.txtBuddies);
        IDtxt2.setTypeface(custom_font);
        IDtxt4.setTypeface(custom_font);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_rough);
        super.onCreate(savedInstanceState);
        super.initialize();
        findViews();

        initViewPagerAndTabs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    private void initViewPagerAndTabs() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager1);
        TabsPagerAdapter pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(AboutFragment.createInstance(20), "ABOUT MYSELF");
        pagerAdapter.addFragment(EventFragment.createInstance(4), "TRIPS");
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout1);
        tabLayout.setupWithViewPager(viewPager);
    }
}

