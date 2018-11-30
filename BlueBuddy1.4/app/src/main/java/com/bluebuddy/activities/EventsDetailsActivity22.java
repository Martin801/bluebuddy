package com.bluebuddy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bluebuddy.R;
import com.bluebuddy.adapters.EventDetailsAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.EventDetails;
import com.bluebuddy.models.EventDetailsResponse;
import com.bluebuddy.models.Participant;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.IMG_URL;

public class EventsDetailsActivity22 extends BuddyActivity implements AppBarLayout.OnOffsetChangedListener {

    private TabLayout tabLayout;
    private MyTextView actname,txtunameid,txtevtlocid,txtevtdatefromid,txtnumpart,bell_counter;
    private ImageView txtupicid;
    private ViewPager viewPager;
    private EventDetailsAdapter eventDetailsAdapter;
    private ImageView back;
    private SharedPreferences pref;
    private String token,token1,tid,bellcounter;
    private Activity _activity;
    private Context _context;
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsing;
    private LinearLayout linearlayoutTitle;
    private Toolbar toolbar;
    private TextView textviewTitle, textviewTitle2;
    private EventDetails allEventsDetails;
    private Button btnupicid;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_events_details2);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.notice();
        super.setTitle("TRIP DETAILS");
        super.loader();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;

        _activity = this;
        _context = this;
        Bundle validation = getIntent().getExtras();

        if(validation.containsKey("DATA_VALUE"))
            tid = validation.getString("DATA_VALUE");
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        Bundle bundle=new Bundle();
        /*if(bundle.containsKey("token")){
            token1=bundle.getString("token");
        }*/

        back=(ImageView)findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtupicid=(ImageView)findViewById(R.id.txtupicid);
        btnupicid = (Button) findViewById(R.id.btnupicid);

        findViews();

        toolbar.setTitle("");
        appbar.addOnOffsetChangedListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        allEventsDetails = (EventDetails) getIntent().getSerializableExtra("AllEventsDetails");
        getAllTrip(tid);

       /* getAllTrip(String.valueOf(allEventsDetails.getEvent_id()));

        btnupicid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("USERID:", String.valueOf(allEventsDetails.getUser_id()));
                Intent i = new Intent(EventsDetailsActivity22.this, PeopleProfileActivity.class);
                i.putExtra("user_id", String.valueOf(allEventsDetails.getUser_id()));
                startActivity(i);
            }
        });*/
    }

    private void setDetailsInfo(EventDetails allEventsDetails){
        txtevtlocid.setText(allEventsDetails.getLocation());
        txtnumpart.setText(allEventsDetails.getParticipants());
        txtunameid.setText(allEventsDetails.getCreator_fname()+" "+allEventsDetails.getCreator_lname());
        txtevtdatefromid.setText(allEventsDetails.getEvent_date());
        actname.setText(allEventsDetails.getActivity());
        collapsing.setTitle(actname.getText().toString());
        collapsing.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        collapsing.setCollapsedTitleTextColor((Color.WHITE));

        Glide.with(EventsDetailsActivity22.this).asBitmap().load(IMG_URL+allEventsDetails.getCreator_dp()).into(new BitmapImageViewTarget(txtupicid) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(EventsDetailsActivity22.this.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                txtupicid.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    private void initViewPagerAndTabs(EventDetails allEventsDetails, ArrayList<Participant> participation_list) {
        ViewPager viewPager2 = (ViewPager) findViewById(R.id.pager2);
//        EventDetailsPagerAdapter pagerAdapter2 = new EventDetailsPagerAdapter(getSupportFragmentManager());
//        pagerAdapter2.addFragment(DetailsFragment.createInstance(token, allEventsDetails), "DETAILS");
//        pagerAdapter2.addFragment(ParticipantsFragment.createInstance(token,participation_list), "PARTICIPANTS");
//        viewPager2.setAdapter(pagerAdapter2);
        TabLayout tabLayout2 = (TabLayout) findViewById(R.id.tabLayout2);
        tabLayout2.setupWithViewPager(viewPager2);
    }

    private void findViews() {
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        collapsing = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        linearlayoutTitle = (LinearLayout) findViewById(R.id.evcard1);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);

        actname = (MyTextView) findViewById(R.id.trip_name);
        txtunameid =(MyTextView) findViewById(R.id.txtunameid);
        txtevtlocid =(MyTextView) findViewById(R.id.txtevtlocid);
        txtnumpart = (MyTextView) findViewById(R.id.textView1);
        txtevtdatefromid =(MyTextView) findViewById(R.id.txtevtdatefromid);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;
        handleAlphaOnTitle(percentage);
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
                startAlphaAnimation(linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
    }

    public void getAllTrip(String tid){

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<EventDetailsResponse> userCall = service.tripDetails("Bearer "+token, tid);

        userCall.enqueue(new Callback<EventDetailsResponse>() {
            @Override
            public void onResponse(Call<EventDetailsResponse> call, Response<EventDetailsResponse> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                Log.d("EventDetails", response.body().getDetails().toString());
                setDetailsInfo(response.body().getDetails());
                initViewPagerAndTabs(response.body().getDetails(), response.body().getParticipation_list());
            }

            @Override
            public void onFailure(Call<EventDetailsResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }
    private void bellcount() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllNotice> userCall = service.allnotice1("Bearer "+token);

        userCall.enqueue(new Callback<AllNotice>() {
            @Override
            public void onResponse(Call<AllNotice> call, Response<AllNotice> response) {
                //    if (mProgressDialog.isShowing())
                //      mProgressDialog.dismiss();
                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("onResponse:", "" + response.body().getNotification_details());
                bellcounter = response.body().getCounter();
                if(bellcounter.equals("0")){
                    bell_counter.setVisibility(View.GONE);
                }
                else if(!bellcounter.equals("0")){
                    bell_counter.setVisibility(View.VISIBLE);
                }
                bell_counter.setText(response.body().getCounter());

                //  Log.d("TOKEN:", "" + token);
                //     ArrayList<AllNotice> allNotices = response.body().getNotification_details();
                //    allNoticeAdapter.updateAllNotice(response.body().getNotification_details());
            }

            @Override
            public void onFailure(Call<AllNotice> call, Throwable t) {

            }
        });
    }

}