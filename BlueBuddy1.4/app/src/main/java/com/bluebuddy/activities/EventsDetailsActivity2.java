package com.bluebuddy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bluebuddy.R;
import com.bluebuddy.adapters.EventDetailsAdapter;
import com.bluebuddy.adapters.EventDetailsPagerAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.fragments.DetailsFragment;
import com.bluebuddy.fragments.ParticipantsFragment;
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
import static com.bluebuddy.app.AppConfig.USER_ID;

public class EventsDetailsActivity2 extends BuddyActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    private TabLayout tabLayout;
    private MyTextView actname, txtunameid, txtevtlocid, txtevtdatefromid, txtnumpart;
    private ImageView txtupicid;
    private ViewPager viewPager;
    private EventDetailsAdapter eventDetailsAdapter;
    private ImageView back;
    private SharedPreferences pref;
    private String token, bellcounter;
    private Activity _activity;
    private Context _context;
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsing;
    private LinearLayout linearlayoutTitle;
    private Toolbar toolbar;
    private MyTextView textviewTitle, textviewTitle2, bell_counter;
    private EventDetails allEventsDetails;
    private Button btnupicid;
    private ProgressDialog mProgressDialog;
    private String user_id;
    private String eventUserId;

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_events_details2);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.notice();
        super.setImageResource1(1);
        super.setTitle("TRIP DETAILS");
        super.loader();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;

        _activity = this;
        _context = this;

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        user_id = pref.getString(USER_ID, "");
        Bundle bundle = new Bundle();
        /*if(bundle.containsKey("token")){
            token1=bundle.getString("token");
        }*/

        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtupicid = (ImageView) findViewById(R.id.txtupicid);
        btnupicid = (Button) findViewById(R.id.btnupicid);

        findViews();

        toolbar.setTitle("");
        appbar.addOnOffsetChangedListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        allEventsDetails = (EventDetails) getIntent().getSerializableExtra("AllEventsDetails");

        eventUserId = allEventsDetails.getUser_id();

        getAllTrip(String.valueOf(allEventsDetails.getEvent_id()));

        btnupicid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Log.d("USERID:", String.valueOf(allEventsDetails.getUser_id()));
                Intent i = new Intent(EventsDetailsActivity2.this, PeopleProfileActivity.class);
                //  i.putExtra("user_id", String.valueOf(allEventsDetails.getUser_id()));
                i.putExtra("uid", String.valueOf(allEventsDetails.getUser_id()));
                startActivity(i);
            }
        });

        bellcount();

    }

    private void setDetailsInfo(EventDetails allEventsDetails) {
        txtevtlocid.setText(allEventsDetails.getLocation());
        txtnumpart.setText(allEventsDetails.getParticipants());
        txtunameid.setText(allEventsDetails.getCreator_fname() + " " + allEventsDetails.getCreator_lname());
        txtevtdatefromid.setText(allEventsDetails.getEvent_date());
        //  actname.setText(allEventsDetails.getActivity());
        //vvp
        if (allEventsDetails.getActivity().contains(":")) {
            String[] separated = allEventsDetails.getActivity().split(":");
            // separated[0];
            // separated[1];
            separated[1] = separated[1].trim();
            actname.setText(separated[1]);
        } else if (!allEventsDetails.getActivity().contains(":")) {
            actname.setText(allEventsDetails.getActivity());
        }

        //vvp

        collapsing.setTitle(actname.getText().toString());
        collapsing.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        collapsing.setCollapsedTitleTextColor((Color.WHITE));

        Glide.with(EventsDetailsActivity2.this).asBitmap().load(IMG_URL + allEventsDetails.getCreator_dp()).into(new BitmapImageViewTarget(txtupicid) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(EventsDetailsActivity2.this.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                txtupicid.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    private void initViewPagerAndTabs(String eventId, Boolean isRemoveVisible, EventDetails allEventsDetails, ArrayList<Participant> participation_list) {
        ViewPager viewPager2 = (ViewPager) findViewById(R.id.pager2);
        EventDetailsPagerAdapter pagerAdapter2 = new EventDetailsPagerAdapter(getSupportFragmentManager());

        pagerAdapter2.addFragment(DetailsFragment.createInstance(token, allEventsDetails), "DETAILS");
        pagerAdapter2.addFragment(ParticipantsFragment.createInstance(eventId, token, isRemoveVisible, participation_list), "PARTICIPANTS");

        viewPager2.setAdapter(pagerAdapter2);
        TabLayout tabLayout2 = (TabLayout) findViewById(R.id.tabLayout2);
        tabLayout2.setupWithViewPager(viewPager2);
    }

    private void findViews() {

        appbar = (AppBarLayout) findViewById(R.id.appbar);
        collapsing = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        linearlayoutTitle = (LinearLayout) findViewById(R.id.evcard1);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);

        actname = (MyTextView) findViewById(R.id.trip_name);
        txtunameid = (MyTextView) findViewById(R.id.txtunameid);
        txtevtlocid = (MyTextView) findViewById(R.id.txtevtlocid);
        txtnumpart = (MyTextView) findViewById(R.id.textView1);
        txtevtdatefromid = (MyTextView) findViewById(R.id.txtevtdatefromid);
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

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    public void getAllTrip(final String event_id) {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<EventDetailsResponse> userCall = service.tripDetails("Bearer " + token, event_id);

        userCall.enqueue(new Callback<EventDetailsResponse>() {
            @Override
            public void onResponse(Call<EventDetailsResponse> call, Response<EventDetailsResponse> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                if (response.body() != null && response.body().getDetails() != null) {
                    setDetailsInfo(response.body().getDetails());

                    if (eventUserId.equalsIgnoreCase(user_id))
                        initViewPagerAndTabs(event_id, true, response.body().getDetails(), response.body().getParticipation_list());
                    else
                        initViewPagerAndTabs(event_id, false, response.body().getDetails(), response.body().getParticipation_list());

                } else {

                    String msg = (String) getApplication().getText(R.string.goeswrong);
                    oDialog(msg, "Ok", "", false, _activity, "", 0);

                }
            }

            @Override
            public void onFailure(Call<EventDetailsResponse> call, Throwable t) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
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
        Call<AllNotice> userCall = service.allnotice1("Bearer " + token);

        userCall.enqueue(new Callback<AllNotice>() {
            @Override
            public void onResponse(Call<AllNotice> call, Response<AllNotice> response) {
                //    if (mProgressDialog.isShowing())
                //      mProgressDialog.dismiss();
                //Log.d("STATUS", String.valueOf(response.body().getStatus()));
                //  Log.d("onResponse:", "" + response.body().getNotification_details());
                bellcounter = response.body().getCounter();
                bell_counter = (MyTextView) findViewById(R.id.bell_counter);
                if (bellcounter.equals("0")) {
                    bell_counter.setVisibility(View.GONE);
                } else if (!bellcounter.equals("0")) {
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