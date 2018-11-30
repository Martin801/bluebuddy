package com.bluebuddy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bluebuddy.R;
import com.bluebuddy.adapters.TabsPagerAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.fragments.EventFragment;
import com.bluebuddy.fragments.EventFragmentimingone;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AfterLoginStatus;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.TripDetail;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class MyEventsActivityNewLay extends BuddyActivity {

    private Button bt;
    private ImageView back;
    private Activity _activity;
    private Context _context;
    private SharedPreferences pref;
    private String token, bellcounter;
    private ProgressDialog mProgressDialog;
    MyTextView bell_counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.my_events_new_lay);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.setImageResource1(5);
        super.notice();
        super.loader();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;

        super.setTitle("MY TRIPS");

        _activity = this;
        _context = this;

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");

        apiMEA();
        bellcount();
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        bt = (Button) findViewById(R.id.Buddyupid);
        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyEventsActivityNewLay.this, MyAccountMenu.class);
                startActivity(i);
            }
        });

    }

    public void btnBup(View view) {
        Intent intent = new Intent(MyEventsActivityNewLay.this, CreateEventActivity3.class);
        startActivity(intent);
    }

    private void initViewPagerAndTabs(ArrayList<TripDetail> my_trips, ArrayList<TripDetail> joined_trips) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        TabsPagerAdapter pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(EventFragment.createInstance(my_trips), "MY TRIPS");
        pagerAdapter.addFragment(EventFragmentimingone.createInstance(joined_trips, false), "JOINED TRIPS");
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void apiMEA() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AfterLoginStatus> userCall = service.mpa("Bearer " + token);

        userCall.enqueue(new Callback<AfterLoginStatus>() {
            @Override
            public void onResponse(Call<AfterLoginStatus> call, Response<AfterLoginStatus> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                if (response.body().getStatus() == true) {
                    Log.d("Got status", "" + response.body().getStatus());
                    Log.d("Profile_details3 sdata", "" + response.body().getProfile_details());
                    initViewPagerAndTabs(response.body().getTrip_details(), response.body().getMy_participation());
                } else {
                    Toast.makeText(MyEventsActivityNewLay.this, "" + response.body().getStatus(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<AfterLoginStatus> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(MyEventsActivityNewLay.this, MyAccountMenu.class);
        startActivity(i);
    }

}

