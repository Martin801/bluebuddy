package com.bluebuddy.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bluebuddy.R;
import com.bluebuddy.adapters.BlueCharterAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllCharter;
import com.bluebuddy.models.AllCharterDetails;
import com.bluebuddy.models.AllNotice;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.USER_ID;

public class LocationBasedSearchActivityCharetr extends BuddyActivity implements LocationListener {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    double latitude, longitude;
    private String _lat1, _long1;
    private ArrayList<AllCharterDetails> BCCList;
    private BlueCharterAdapter blueCharterAdapter;

    LocationManager locationManager;
    String provider;
    SeekBar seek;
    ImageView back;
    Button rdsbtn;

    private Activity _activity;
    private Context _context;
    private String token, id, rds, s, curru, bellcounter;
    private SharedPreferences pref;
    Double clat, clong;
    MyTextView bell_counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_people_search_clocation_charter);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.setTitle("SEARCH");
        super.notice();
        _activity = this;
        _context = this;

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        curru = pref.getString(USER_ID, "");
        rds = "50";

        rdsbtn = (Button) findViewById(R.id.rdsbtn);
        back = (ImageView) findViewById(R.id.imgNotification2);
        seek = (SeekBar) findViewById(R.id.idseek);
        bellcount();
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_event);
        recyclerView.setNestedScrollingEnabled(false);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }
        mLayoutManager = new LinearLayoutManager(_context);
        recyclerView.setLayoutManager(mLayoutManager);

        blueCharterAdapter = new BlueCharterAdapter(_activity, _context, null, token, curru);
        recyclerView.setAdapter(blueCharterAdapter);
        blueCharterAdapter.notifyDataSetChanged();

        currentlocationpeople();


        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rds = String.valueOf(progress);
                Log.d("radius", rds);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LocationBasedSearchActivityCharetr.this, BlueMarketCharterActivityNew.class);
                startActivity(i);
            }
        });


        rdsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentlocationpeople();
            }
        });

    }


    private void currentlocationpeople() {

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        if (locationManager != null) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                fn_update(location);
            }
        }


        locationManager = (LocationManager) getSystemService(_context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (provider != null && !provider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 1000, 1, this);
            if (location != null)
                onLocationChanged(location);
            //else
            //    Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getBaseContext(), "No Provider Found , Please turn on your GPS", Toast.LENGTH_SHORT).show();
        }

        Log.d("rds", rds);

    }

    private void fn_update(Location location) {
        clat = location.getLatitude();
        clong = location.getLongitude();
        Log.d("lat1", String.valueOf(clat));
        Log.d("long1", String.valueOf(clong));
        _lat1 = String.format("%.6f", clat);
        _long1 = String.format("%.6f", clong);
    }


    public void oDialog(final String msg, final String btnText, final String btnText2, final String btnText3, final String btnText4, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog4(msg, btnText, btnText2, btnText3, btnText4, _redirect, activity, _redirectClass, layout);
    }


    @Override
    public void onLocationChanged(Location location) {

        clat = location.getLatitude();
        clong = location.getLongitude();
        Log.d("lat", String.valueOf(clat));
        Log.d("long", String.valueOf(clong));
        _lat1 = String.format("%.6f", clat);
        _long1 = String.format("%.6f", clong);
        fetchcurrentcharter();

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void fetchcurrentcharter() {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllCharter> userCall = service.allcharter1("Bearer " + token, _lat1, _long1, rds);

        userCall.enqueue(new Callback<AllCharter>() {
            @Override
            public void onResponse(Call<AllCharter> call, Response<AllCharter> response) {
                ArrayList<AllCharterDetails> details = response.body().getCharter_list();
                if (details == null) {
                    oDialog("No Charter Found", "Close", "", false, _activity, "", 0);

                } else if (details != null) {
                    blueCharterAdapter.updateAllCharter(response.body().getCharter_list());
                }

            }

            @Override
            public void onFailure(Call<AllCharter> call, Throwable t) {


            }
        });

    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
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
        Intent i = new Intent(LocationBasedSearchActivityCharetr.this, BlueMarketCharterActivityNew.class);
        startActivity(i);
    }
}

