package com.bluebuddy.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bluebuddy.R;
import com.bluebuddy.adapters.EventDetailsPagerAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.fragments.DetailsFragment2;
import com.bluebuddy.fragments.ParticipantsFragment;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class MyListDetailsActivity extends BuddyActivity implements AppBarLayout.OnOffsetChangedListener {


    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    private ImageView back;
    String token, bellcounter;
    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsing;
    private SharedPreferences pref;
    private LinearLayout linearlayoutTitle;
    private Toolbar toolbar;
    private MyTextView textviewTitle, textviewTitle2, trip_name, bell_counter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_events_details);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.notice();
        super.setTitle("LIST DETAILS");
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        findViews();
        toolbar.setTitle("");
        appbar.addOnOffsetChangedListener(this);
        setSupportActionBar(toolbar);
        initViewPagerAndTabs();
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        collapsing.setCollapsedTitleTypeface(custom_font);
        collapsing.setTitle(trip_name.getText().toString().toUpperCase());
        collapsing.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        collapsing.setCollapsedTitleTextColor((Color.WHITE));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyListDetailsActivity.this, MyListingActivity.class);
                startActivity(i);
            }
        });
        bellcount();
    }

    private void initViewPagerAndTabs() {
        ViewPager viewPager2 = (ViewPager) findViewById(R.id.pager2);
        EventDetailsPagerAdapter pagerAdapter2 = new EventDetailsPagerAdapter(getSupportFragmentManager());
        pagerAdapter2.addFragment(DetailsFragment2.createInstance(20), "DETAILS");
        pagerAdapter2.addFragment(ParticipantsFragment.createInstance(4), "PARTICIPANTS");
        viewPager2.setAdapter(pagerAdapter2);
        TabLayout tabLayout2 = (TabLayout) findViewById(R.id.tabLayout2);
        tabLayout2.setupWithViewPager(viewPager2);
    }

    private void findViews() {
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        trip_name = (MyTextView) findViewById(R.id.trip_name);
        back = (ImageView) findViewById(R.id.imgNotification2);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        collapsing = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        linearlayoutTitle = (LinearLayout) findViewById(R.id.evcard1);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;
        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
                mIsTheTitleVisible = true;
            }
        } else {
            if (mIsTheTitleVisible) {

                mIsTheTitleVisible = false;
            }
        }
    }


    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                mIsTheTitleContainerVisible = false;
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(MyListDetailsActivity.this, MyListingActivity.class);
        startActivity(i);
    }

    private void bellcount() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllNotice> userCall = service.allnotice1("Bearer " + token);

        userCall.enqueue(new Callback<AllNotice>() {
            @Override
            public void onResponse(Call<AllNotice> call, Response<AllNotice> response) {
                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("onResponse:", "" + response.body().getNotification_details());
                bellcounter = response.body().getCounter();
                if (bellcounter.equals("0")) {
                    bell_counter.setVisibility(View.GONE);
                } else if (!bellcounter.equals("0")) {
                    bell_counter.setVisibility(View.VISIBLE);
                }
                bell_counter.setText(response.body().getCounter());

            }

            @Override
            public void onFailure(Call<AllNotice> call, Throwable t) {

            }
        });
    }
}
