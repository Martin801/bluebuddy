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
import com.bluebuddy.adapters.AllEventAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.AllTrip;
import com.bluebuddy.models.EventDetails;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.USER_ID;

public class LocationBasedSearchActivity extends BuddyActivity implements LocationListener {
    private RecyclerView recyclerView;
    // private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    double latitude,longitude;
    private List<EventDetails> EVENTList;
   // private List<PeopleDetail> PEOPLEslist;
    //private CurrentLocationAdapter currentlocadpt;
    //   LocationSrchViewPagerAdapter locationSrchViewPagerAdapter ;
    //  LocationSrchViewPagerAdapter locationSrchViewPagerAdapter2;
    //private PeopleSearchAdapter peopleSearchAdapter;
    private AllEventAdapter allEventAdapter;
    LocationManager locationManager;
    String provider;
    SeekBar seek;
    ImageView back;
    Button rdsbtn;
    //private ArrayList<PeopleSearch> PEOPLEsList;
    //private PeopleSearchAdapter peopleSearchAdapter;
    private Activity _activity;
    private Context _context;
    private String token, id, rds, s,curru,bellcounter;
    private SharedPreferences pref;
    // public Integer rds;
    Double clat, clong;
    private String _lat1, _long1;
    MyTextView bell_counter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_people_search_clocation_trip);
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_people_search2);
        super.initialize();
        super.setTitle("SEARCH");
        super.setImageResource1(1);
        super.notice();
        _activity = this;
        _context = this;
     /*   super.loader();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;*/
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        curru = pref.getString(USER_ID, "");
        rds = "50";
        bellcount();
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        rdsbtn = (Button) findViewById(R.id.rdsbtn);
        back = (ImageView) findViewById(R.id.imgNotification2);
        seek = (SeekBar) findViewById(R.id.idseek);

        /*mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_event);
        mRecyclerView.setNestedScrollingEnabled(false);
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
        }
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        currentlocadpt = new CurrentLocationAdapter(_activity, _context, token);
        mRecyclerView.setAdapter(currentlocadpt);
        currentlocadpt.notifyDataSetChanged();*/

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_event);
        recyclerView.setNestedScrollingEnabled(false);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }
        mLayoutManager = new LinearLayoutManager(_context);
        recyclerView.setLayoutManager(mLayoutManager);
        /*peopleSearchAdapter = new PeopleSearchAdapter(_activity, _context, null, token);*/
        allEventAdapter = new AllEventAdapter(_activity, _context, null, token,curru);
        recyclerView.setAdapter(allEventAdapter);
        allEventAdapter.notifyDataSetChanged();

        currentlocationpeople();


      //  tabLayout = (TabLayout) findViewById(R.id.tabLayout);
      //  viewPager = (ViewPager) findViewById(R.id.viewPager);
        // Getting LocationManager object

       /* locationManager = (LocationManager) getSystemService(_context.LOCATION_SERVICE);
        // Creating an empty criteria object
        Criteria criteria = new Criteria();
        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, false);
        if (provider != null && !provider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            // Get the location from the given provider
            Location location = locationManager.getLastKnownLocation(provider);

            locationManager.requestLocationUpdates(provider, 20000, 1, this);

            if (location != null)
                onLocationChanged(location);
            else
                Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getBaseContext(), "No Provider Found , Please turn on your GPS", Toast.LENGTH_SHORT).show();
        }*/


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

     //   tabLayout.setupWithViewPager(viewPager);
        //  locationSrchViewPagerAdapter = new LocationSrchViewPagerAdapter(getSupportFragmentManager(),token,clat,clong,rds);
        //   viewPager.setAdapter(locationSrchViewPagerAdapter);
        //  viewPager.getCurrentItem();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LocationBasedSearchActivity.this, AllEventsActivityNew.class);
                startActivity(i);
            }
        });


        rdsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // s rds;
                currentlocationpeople();
            }
        });

    }


    private void currentlocationpeople() {

        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
         locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,this);
        if (locationManager!=null){

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location!=null){
             //   Log.d("latitude",location.getLatitude()+"");
              // Log.d("longitude",location.getLongitude()+"");
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
           // else
               // Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getBaseContext(), "No Provider Found , Please turn on your GPS", Toast.LENGTH_SHORT).show();
        }

        // tabLayout.setupWithViewPager(viewPager);
        //Log.d("rds", rds);
        //  sendData(rds);
        //locationSrchViewPagerAdapter = new LocationSrchViewPagerAdapter(getSupportFragmentManager(),token,clat,clong,rds);
        //viewPager.setAdapter(locationSrchViewPagerAdapter);
    }

    private void fn_update(Location location) {
       clat= location.getLatitude();
        clong= location.getLongitude();
    //    Toast.makeText(getBaseContext(), clat.toString(), Toast.LENGTH_SHORT).show();
    //    Toast.makeText(getBaseContext(), clong.toString(), Toast.LENGTH_SHORT).show();
      //  Log.d("lat1", String.valueOf(clat));
       // Log.d("long1", String.valueOf(clong));
        _lat1 = String.format("%.6f", clat);
        _long1 = String.format("%.6f", clong);
    }

   /* PeopleSearchFragment2 frag2 = (PeopleSearchFragment2)viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
    frag2.;*/
  /*  public void sendData(String rds){
        PeopleSearchFragment2 FF = new PeopleSearchFragment2();
        FF.getData(rds);
    }*/

    public void oDialog(final String msg, final String btnText, final String btnText2, final String btnText3, final String btnText4, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog4(msg, btnText, btnText2, btnText3, btnText4, _redirect, activity, _redirectClass, layout);
    }

    private void bellcount() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllNotice> userCall = service.allnotice1("Bearer "+token);

        userCall.enqueue(new Callback<AllNotice>() {
            @Override
            public void onResponse(Call<AllNotice> call, Response<AllNotice> response) {
                //    if (mProgressDialog.isShowing())
                //      mProgressDialog.dismiss();
               // Log.d("STATUS", String.valueOf(response.body().getStatus()));
               // Log.d("onResponse:", "" + response.body().getNotification_details());
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

    @Override
    public void onLocationChanged(Location location) {

        clat = location.getLatitude();
        clong = location.getLongitude();
     //   Log.d("lat", String.valueOf(clat));
        //Log.d("long", String.valueOf(clong));


        _lat1 = String.format("%.6f", clat);
        _long1 = String.format("%.6f", clong);
       /* _lat1 = String.format("%.6f", clat);
        Log.d("convert",_lat1);
        _long1 = String.format("%.6f", clong);
        Log.d("convert",_long1);*/


        fetchcurrentpeople();

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

    private void fetchcurrentpeople() {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
       // Call<AllTrip> userCall = service.alltrip1("Bearer " + token, clat, clong, rds);
        Call<AllTrip> userCall = service.alltrip18("Bearer " + token, _lat1, _long1, rds);

        userCall.enqueue(new Callback<AllTrip>() {
            @Override
            public void onResponse(Call<AllTrip> call, Response<AllTrip> response) {
                // if (mProgressDialog.isShowing())
                //    mProgressDialog.dismiss();
                ArrayList<EventDetails> trip_details = response.body().getDetails();
//                Log.d("rrr",trip_details.toString().trim());
                if(trip_details.isEmpty()){
                    oDialog("No Trips Found", "Close", "", false, _activity, "", 0);

                    /*int len = trip_details.size();
                    if(len == 0){
                        oDialog("No Trip Found", "Ok", "", false, _activity, "", 0);
                        //  Log.d("size", String.valueOf(len));
                    }*/
                }

             /*   Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("onResponse:", "" + response.body().getDetails());
                Log.d("TOKEN:", "" + token);*/
              else if (trip_details != null) {

                    allEventAdapter.updateAllTrip(response.body().getDetails());
                }

            }
            @Override
            public void onFailure(Call<AllTrip> call, Throwable t) {
                // if (mProgressDialog.isShowing())
                // mProgressDialog.dismiss();

            }
        });
       /* // Log.d("tokeninloc", token);
        //  Log.d("latinloc", String.valueOf(clat));
        //  Log.d("latinlong", String.valueOf(clong));
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        // if(!rad.equals("")){
        Call<AfterLoginStatus> userCall = service.alltrip1("Bearer " + token, clat, clong, rds);
        userCall.enqueue(new Callback<AfterLoginStatus>() {
            @Override
            public void onResponse(Call<AfterLoginStatus> call, Response<AfterLoginStatus> response) {
                //  if (mProgressDialog.isShowing())
                //    mProgressDialog.dismiss();
               // ArrayList<PeopleDetail> peopleDetail = response.body().getUser_details_arr();
                List<TripDetail> trip_details =response.body().getTrip_details();
                String certList = "";
                int len = trip_details.size();
                if (len == 0) {
                    oDialog("No Trip Found", "Ok", "", false, _activity, "", 0);
                }
              //  if (peopleDetail != null) {

          //      if (mProgressDialog.isShowing())
                //    mProgressDialog.dismiss();
                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("TOKEN:", "" + token);
                allEventAdapter.updateAllTrip(response.body().getTrip_details());
             //   peopleSearchAdapter.updateAllPeople(response.body().getUser_details_arr());
           // }
            }

            @Override
            public void onFailure(Call<AfterLoginStatus> call, Throwable t) {
            }
        });*/
    }
    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(LocationBasedSearchActivity.this, AllEventsActivityNew.class);
        startActivity(i);
    }

}

