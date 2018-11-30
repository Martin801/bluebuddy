package com.bluebuddy.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bluebuddy.R;
import com.bluebuddy.adapters.EventDetailsPagerAdapter;
import com.bluebuddy.fragments.DetailsFragment2;
import com.bluebuddy.fragments.ParticipantsFragmentMyEvents;
import com.bluebuddy.models.TripDetail;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.IMG_URL;

public class EventsDetailsActivity extends BuddyActivity implements AppBarLayout.OnOffsetChangedListener {

    TextView actname,txtunameid,txtevtlocid,txtevtdatefromid,textView1;
    private  ImageView txtupicid;
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    private SharedPreferences pref;
    private String token;
    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsing;
    //  private ImageView coverImage;
    // private FrameLayout framelayoutTitle;
    private LinearLayout linearlayoutTitle;
    private Toolbar toolbar;
    ImageView back;
    private TextView textviewTitle, textviewTitle2,trip_name;
    // private SimpleDraweeView avatar;
    private TripDetail tripDetail;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_events_details);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.setTitle("TRIP DETAILS");
        //super.loader();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        findViews();
        toolbar.setTitle("");
        appbar.addOnOffsetChangedListener(this);
        setSupportActionBar(toolbar);
        //  startAlphaAnimation(textviewTitle2, 0, View.INVISIBLE);
        /*initViewPagerAndTabs();*/
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");

        // getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false); // remove the left caret
        //   getSupportActionBar().setDisplayShowHomeEnabled(false);
        collapsing.setCollapsedTitleTypeface(custom_font);
        //  toUpperCase

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(EventsDetailsActivity.this,MyEventsActivity.class);
                startActivity(i);
            }
        });

        tripDetail = (TripDetail) getIntent().getSerializableExtra("TripDetail");
        Log.d("tripDetail", tripDetail.toString());
        initViewPagerAndTabs(tripDetail);
        String evtcname =  tripDetail.getCreator_fname()+tripDetail.getCreator_lname();
        String  cdp = tripDetail.getCreator_dp();
        String  evtlocation = tripDetail.getLocation();
        String  evtdate = tripDetail.getEvent_date();
        String  acttype = tripDetail.getActivity();

        String  prtcpnt = tripDetail.getParticipants();

        txtevtlocid.setText(evtlocation);
        txtunameid.setText(evtcname);
        txtevtdatefromid.setText(evtdate);
        actname.setText(acttype);
        collapsing.setTitle(actname.getText().toString());

        collapsing.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        collapsing.setCollapsedTitleTextColor((Color.WHITE));
        textView1.setText(prtcpnt);


        Glide.with(EventsDetailsActivity.this).asBitmap().load(IMG_URL+cdp).into(new BitmapImageViewTarget(txtupicid) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(EventsDetailsActivity.this.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                txtupicid.setImageDrawable(circularBitmapDrawable);
            }
        });
        //---------------------
    }

    private void initViewPagerAndTabs(TripDetail tripDetail) {
        ViewPager viewPager2 = (ViewPager) findViewById(R.id.pager2);
        EventDetailsPagerAdapter pagerAdapter2 = new EventDetailsPagerAdapter(getSupportFragmentManager());
        pagerAdapter2.addFragment(DetailsFragment2.createInstance(tripDetail), "DETAILS");
        pagerAdapter2.addFragment(ParticipantsFragmentMyEvents.createInstance(4), "PARTICIPANTS");
        viewPager2.setAdapter(pagerAdapter2);
        TabLayout tabLayout2 = (TabLayout) findViewById(R.id.tabLayout2);
        tabLayout2.setupWithViewPager(viewPager2);
    }

    private void findViews() {
        trip_name=(TextView)findViewById(R.id.trip_name);
        back=(ImageView)findViewById(R.id.imgNotification2);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        collapsing = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        linearlayoutTitle = (LinearLayout) findViewById(R.id.evcard1);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
       // textviewTitle2 = (TextView) findViewById(R.id.textview_title21);
        txtupicid = (ImageView) findViewById(R.id.txtupicid);
        actname=(TextView)findViewById(R.id.trip_name);
        txtunameid =(TextView)findViewById(R.id.txtunameid);
        txtevtlocid =(TextView)findViewById(R.id.txtevtlocid);
        txtevtdatefromid =(TextView)findViewById(R.id.txtevtdatefromid);
        textView1 =(TextView)findViewById(R.id.textView1);
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
                //  startAlphaAnimation(textviewTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
             //  startAlphaAnimation(textviewTitle2, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }
        } else {
            if (mIsTheTitleVisible) {
                //  startAlphaAnimation(textviewTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
               // startAlphaAnimation(textviewTitle2, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                //startAlphaAnimation(linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
               // startAlphaAnimation(linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(EventsDetailsActivity.this,MyEventsActivity.class);
        startActivity(i);
    }
}
