package com.bluebuddy.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bluebuddy.R;
import com.bluebuddy.adapters.AllEventAdapter;
import com.bluebuddy.adapters.ViewPagerAdapterforTrips;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.EventDetails;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.USER_ID;

public class AllEventsActivityNewLocSrch extends BuddyActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AllEventAdapter allEventAdapter;
    ViewPagerAdapterforTrips viewPagerAdapterforTrips;
    ImageView back;
    Activity _activity;
    private Context _context;
    ArrayList<EventDetails> loc_srch;
    private String token, id, category, fd, td, curru, bellcounter;
    private SharedPreferences pref;
    Button frm_date, to_date, dateaction;
    EditText edtfrmdate, edttodate;
    ProgressDialog mProgressDialog;
    Calendar myCalendar = Calendar.getInstance();
    String cat;
    MyTextView bell_counter;
    private static final int RECORD_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_all_events);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.notice();
        super.setImageResource1(1);
        super.setTitle("TRIPS");
        _activity = this;
        _context = this;

        //today23
        int permission = ContextCompat.checkSelfPermission(AllEventsActivityNewLocSrch.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("HELLO", "Permission to location denied");

            if (ActivityCompat.shouldShowRequestPermissionRationale(AllEventsActivityNewLocSrch.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AllEventsActivityNewLocSrch.this);
                builder.setMessage("Permission to access the location is required for this app to access your location.")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("HELLO", "Clicked");

                        ActivityCompat.requestPermissions(AllEventsActivityNewLocSrch.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RECORD_REQUEST_CODE);

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                ActivityCompat.requestPermissions(AllEventsActivityNewLocSrch.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RECORD_REQUEST_CODE);

            }
        }

        //today23

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        curru = pref.getString(USER_ID, "");

        Bundle b = getIntent().getExtras();

        if (b != null) {
            loc_srch= (ArrayList<EventDetails>) b.getSerializable("loc_srch");
           /* if(b.containsKey("loc_srch") && b.containsKey("interestList")){
                ArrayList<EventDetails> loc_srch = (ArrayList<EventDetails>) getIntent().getExtras().getSerializable("loc_srch");
                ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
                TabsPagerAdapter pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
                pagerAdapter.addFragment(AllTripsSearchFragment.createInstance(loc_srch), "Trips");
                pagerAdapter.addFragment(LocationSearchFragment.createInstance(category, token), "Location");
                viewPager.setAdapter(pagerAdapter);
                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
                tabLayout.setupWithViewPager(viewPager);
                //allEventAdapter.updateAllTrip(response.body().getTrip_details());
                //  allEventAdapter = new AllEventAdapter(_activity, _context, null, token, curru);
            }*/
            /*category = b.getString("interestList");
            cat = category.toString().trim();
            Log.d("qqq", cat);*/
        }
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
     /*   ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabsPagerAdapter pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(AllTripsSearchFragmentlocsrch.createInstance("",loc_srch), "Trips");
        pagerAdapter.addFragment(LocationSearchFragment.createInstance(category, token), "Location");
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        allEventAdapter = new AllEventAdapter(_activity, _context, null, token, curru);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;*/
        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AllEventsActivityNewLocSrch.this, CreateProfileActivity2.class);
                i.putExtra("pass", 2);
                startActivity(i);
            }
        });
        bellcount();
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final String btnText3, final String btnText4, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog4(msg, btnText, btnText2, btnText3, btnText4, _redirect, activity, _redirectClass, layout);
    }

    private void bellcount() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllNotice> userCall = service.allnotice1("Bearer " + token);

        userCall.enqueue(new Callback<AllNotice>() {
            @Override
            public void onResponse(Call<AllNotice> call, Response<AllNotice> response) {

               // Log.d("STATUS", String.valueOf(response.body().getStatus()));
             //   Log.d("onResponse:", "" + response.body().getNotification_details());
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
        Intent i = new Intent(AllEventsActivityNewLocSrch.this, CreateProfileActivity2.class);
        i.putExtra("pass", 2);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
