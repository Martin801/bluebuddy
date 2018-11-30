package com.bluebuddy.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bluebuddy.R;
import com.bluebuddy.adapters.AllEventAdapter;
import com.bluebuddy.adapters.PlaceAutocompleteAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.AllTrip;
import com.bluebuddy.models.EventDetails;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.USER_ID;

public class LocationBasedSearchActivity2 extends BuddyActivity implements GoogleApiClient.OnConnectionFailedListener {
    private RecyclerView recyclerView;
    // private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<EventDetails> EVENTList;
   // private List<PeopleDetail> PEOPLEslist;
    //private CurrentLocationAdapter currentlocadpt;
    //   LocationSrchViewPagerAdapter locationSrchViewPagerAdapter ;
    //  LocationSrchViewPagerAdapter locationSrchViewPagerAdapter2;
   private AllEventAdapter allEventAdapter;
  //  private PeopleSearchAdapter peopleSearchAdapter;
    LocationManager locationManager;
    String provider;
    SeekBar seek;
    private AutoCompleteTextView autoCompView;
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
   // private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
    private static final LatLngBounds BOUNDS_NEWYORK= new LatLngBounds(new LatLng(-34.041458, 150.790100), new LatLng(40.719268, -74.006287));

    ImageView back;
    Button rdsbtn;
    //private ArrayList<PeopleSearch> PEOPLEsList;
    //private PeopleSearchAdapter peopleSearchAdapter;
    private Activity _activity;
    private Context _context;
    private String token, TAG,id, rds, s,_lat,_long,curru,bellcounter;
    private SharedPreferences pref;
    private String _lat1, _long1;
    MyTextView bell_counter;

    // public Integer rds;
    Double clat, clong;
    private boolean _isValid = false;
   // Double _lat,_long;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_people_search_olocation2_trip);

        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_people_search2);
        super.initialize();
        super.setTitle("SEARCH");
        super.notice();
        _activity = this;
        _context = this;
        TAG = "LocationActivity";
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, 0 /* clientId */, this).addApi(Places.GEO_DATA_API).build();
        }catch (Exception e){
            e.printStackTrace();
        }

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        curru = pref.getString(USER_ID, "");
        bellcount();
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
      //  rds = "50000";
        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        // Register a listener that receives callbacks when a suggestion has been selected
        autoCompView.setOnItemClickListener(mAutocompleteClickListener);

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
      //  mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_GREATER_SYDNEY, null);
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_NEWYORK, null);

        autoCompView.setAdapter(mAdapter);
        rdsbtn = (Button) findViewById(R.id.rdsbtn);
        back = (ImageView) findViewById(R.id.imgNotification2);

        autoCompView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //if(s.length() <= 0){
                _isValid = false;
                _lat = "";
                _long ="";
                //}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
       // seek = (SeekBar) findViewById(R.id.idseek);

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

        /*peopleSearchAdapter = new PeopleSearchAdapter(_activity, _context, null, token);
        recyclerView.setAdapter(peopleSearchAdapter);
        peopleSearchAdapter.notifyDataSetChanged();*/
        allEventAdapter = new AllEventAdapter(_activity, _context, null, token,curru);
        recyclerView.setAdapter(allEventAdapter);
        allEventAdapter.notifyDataSetChanged();
        //currentlocationpeople();


      //  tabLayout = (TabLayout) findViewById(R.id.tabLayout);
       // viewPager = (ViewPager) findViewById(R.id.viewPager);
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


/*
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
*/

     //   tabLayout.setupWithViewPager(viewPager);
        //  locationSrchViewPagerAdapter = new LocationSrchViewPagerAdapter(getSupportFragmentManager(),token,clat,clong,rds);
        //   viewPager.setAdapter(locationSrchViewPagerAdapter);
        //  viewPager.getCurrentItem();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LocationBasedSearchActivity2.this, AllEventsActivityNew.class);
                startActivity(i);
            }
        });


        rdsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_isValid)
                    currentlocationpeople();
                else
                    oDialog("Select your location!", "Ok", "", false, _activity, "", 0);
                // s rds;
              //  currentlocationpeople();
            }
        });

    }


    private void currentlocationpeople() {
        String location = autoCompView.getText().toString().trim();

        if (location.isEmpty()) {
            oDialog("Enter your Location", "Close", "", false, _activity, "", 0);
        } else {

            location = autoCompView.getText().toString();
            Log.d(TAG, "location:"+location+",lat:"+_lat+",long:"+_long);

            double _lat3 = Double.parseDouble(_lat);
            double _long3 = Double.parseDouble(_long);
            _lat1 = String.format("%.6f", _lat3);
            _long1 = String.format("%.6f", _long3);

//
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            // if(!rad.equals("")){
       //     Call<AfterLoginStatus> userCall = service.allpeople11("Bearer " + token, _lat, _long, "", "");
            Call<AllTrip> userCall = service.alltrip11("Bearer " + token, _lat1, _long1, 50);
            userCall.enqueue(new Callback<AllTrip>() {
                @Override
                public void onResponse(Call<AllTrip> call, Response<AllTrip> response) {
                    //  if (mProgressDialog.isShowing())
                    //    mProgressDialog.dismiss();
                   // ArrayList<EventDetails> trip_details = response.body().getTrip_details();
                    ArrayList<EventDetails> trip_details = response.body().getDetails();
                 //   ArrayList<PeopleDetail> peopleDetail = response.body().getUser_details_arr();
                 //   String certList = "";

                    if(trip_details.isEmpty()) {
                        oDialog("No Trips Found for this Location", "Close", "", false, _activity, "", 0);
                    }
                    else if (trip_details != null && !trip_details.isEmpty() ) {
                        Intent i=new Intent(LocationBasedSearchActivity2.this,AllEventsActivityNewLocSrch.class);
                        i.putExtra("loc_srch",response.body().getDetails());
                        startActivity(i);
                       /* Intent i=new Intent(LocationBasedSearchActivity2.this,AllEventsActivityNew.class);
                        i.putExtra("loc_srch",response.body().getTrip_details());
                        i.putExtra("interestList","");
                        startActivity(i);*/
                       //allEventAdapter.updateAllTrip(response.body().getTrip_details());
                    }


                   // int len = trip_details.size();
                  //  if (len == 0) {
                   //     oDialog("No people found for this Location", "Ok", "", false, _activity, "", 0);
                 //   }
                //    Log.d("STATUS", String.valueOf(response.body().getStatus()));
                  //  Log.d("TOKEN:", "" + token);
                 //   peopleSearchAdapter.updateAllPeople(response.body().getUser_details_arr());



                }

                @Override
                public void onFailure(Call<AllTrip> call, Throwable t) {
                }
            });
            //



            /*ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<ServerResponseLoc> userCall = service.Loc("Bearer " + token, "put", location, _lat, _long, 8);
            userCall.enqueue(new Callback<ServerResponseLoc>() {
                @Override
                public void onResponse(Call<ServerResponseLoc> call, Response<ServerResponseLoc> response) {
                    if (response.body().getStatus() == "true") {
                       // setMyPref(ACCESS_STEP, "8");
                       // Log.d("onResponse", "" + response.body().getStatus());

                        Intent intent = new Intent(LocationActivity.this, CreateProfileActivity7.class);
                        startActivity(intent);
                        finish();
                    } else {
                      //  Log.d("onResponse", "" + response.body().getStatus());
                    }
                }

                @Override
                public void onFailure(Call<ServerResponseLoc> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                }
            });*/

        }
        /*locationManager = (LocationManager) getSystemService(_context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (provider != null && !provider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 20000, 1, this);
            if (location != null)
                onLocationChanged(location);
            else
                Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getBaseContext(), "No Provider Found , Please turn on your GPS", Toast.LENGTH_SHORT).show();
        }

        // tabLayout.setupWithViewPager(viewPager);
        Log.d("rds", rds);*/
    }
    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
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

    //  sendData(rds);
        //locationSrchViewPagerAdapter = new LocationSrchViewPagerAdapter(getSupportFragmentManager(),token,clat,clong,rds);
        //viewPager.setAdapter(locationSrchViewPagerAdapter);


   /* PeopleSearchFragment2 frag2 = (PeopleSearchFragment2)viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
    frag2.;*/
  /*  public void sendData(String rds){
        PeopleSearchFragment2 FF = new PeopleSearchFragment2();
        FF.getData(rds);
    }*/

    public void oDialog(final String msg, final String btnText, final String btnText2, final String btnText3, final String btnText4, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog4(msg, btnText, btnText2, btnText3, btnText4, _redirect, activity, _redirectClass, layout);
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
            */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            //Toast.makeText(getApplicationContext(), "Clicked: " + primaryText, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }

            // Get the Place object from the buffer.
            final Place place = places.get(0);
            _isValid = true;
            //_lat = place.getLatLng().latitude;
            //_long=place.getLatLng().longitude;
            _lat = String.valueOf(place.getLatLng().latitude);
            _long = String.valueOf(place.getLatLng().longitude);

            Log.d("LATLONG:", place.getLatLng().toString());
            Log.i(TAG, "Place details received: " + place.getName());
            places.release();
        }
    };

    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,  "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
    }

  /*  @Override
    public void onLocationChanged(Location location) {
        clat = location.getLongitude();
        clong = location.getLatitude();
        Log.d("lat", String.valueOf(clat));
        Log.d("long", String.valueOf(clong));
        fetchcurrentpeople();

    }*/


   /* @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }*/

  /*  private void fetchcurrentpeople() {
        // Log.d("tokeninloc", token);
        //  Log.d("latinloc", String.valueOf(clat));
        //  Log.d("latinlong", String.valueOf(clong));
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        // if(!rad.equals("")){
        Call<AfterLoginStatus> userCall = service.allpeople1("Bearer " + token, clat, clong, rds, "");
        userCall.enqueue(new Callback<AfterLoginStatus>() {
            @Override
            public void onResponse(Call<AfterLoginStatus> call, Response<AfterLoginStatus> response) {
                //  if (mProgressDialog.isShowing())
                //    mProgressDialog.dismiss();
                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("TOKEN:", "" + token);
                peopleSearchAdapter.updateAllPeople(response.body().getUser_details_arr());
            }

            @Override
            public void onFailure(Call<AfterLoginStatus> call, Throwable t) {
            }
        });
    }*/
  @Override
  public void onBackPressed() {
      super.onBackPressed();
      Intent i = new Intent(LocationBasedSearchActivity2.this, AllEventsActivityNew.class);
      startActivity(i);
  }
}

