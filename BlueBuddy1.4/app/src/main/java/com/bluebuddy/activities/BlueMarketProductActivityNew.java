package com.bluebuddy.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.bluebuddy.R;
import com.bluebuddy.Utilities.Utility;
import com.bluebuddy.adapters.BlueMarketProductAdapter;
import com.bluebuddy.adapters.PlaceAutocompleteAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.app.AppConfig;
import com.bluebuddy.app.AppController;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.AllProduct;
import com.bluebuddy.models.AllProductDetails;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.FIRE_REG_ID;
import static com.bluebuddy.app.AppConfig.USER_ID;
import static com.bluebuddy.app.AppController.isActivityVisible;

public class BlueMarketProductActivityNew extends BuddyActivity implements LocationListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RECORD_REQUEST_CODE = 101;
    protected GoogleApiClient mGoogleApiClient;
    ImageView back;
    Activity _activity;
    ProgressDialog mProgressDialog;
    String cat, curru;
    MyTextView bell_counter;
    LinearLayout ll_datePicker_main;
    String fromDate, toDate;
    ProgressDialog progressDialog;
    LinearLayout ll_currentLocation, ll_otherLocation, ll_seekbar, ll_locationText, ll_search_icon;
    TextView tv_searchResultText, tv_otherLocationText, tv_currentLocationText;
    SeekBar seekBar;
    int showStatus = 0;
    String radious = Utility.DEFAULT_RADIOUS;
    Double clat, clong;
    LocationManager locationManager;
    String provider;
    TextView tv_noDataFound, tv_rediousChange;
    List<AllProductDetails> allProductDetailsList;
    boolean isCurrentLocationGranted = false;
    private Context _context;
    private String token, id, category, fd, td, regid, bellcounter;
    private int pass;
    private SharedPreferences pref;
    private BlueMarketProductAdapter blueMarketProductAdapter;
    private String _lat, _long;
    private String current_location_lat, current_location_long;
    private PlaceAutocompleteAdapter mAdapter;
    private AutoCompleteTextView autoCompView;
    private boolean _isValid = false;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                //  Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }

            final Place place = places.get(0);
            _isValid = true;
            _lat = String.valueOf(place.getLatLng().latitude);
            _long = String.valueOf(place.getLatLng().longitude);
            places.release();
            // call for web service
            checkBeforeServiceCall();


        }
    };
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

          /*   Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.*/

            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            //   Log.i(TAG, "Autocomplete item selected: " + primaryText);


        /*     Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.*/

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            //Toast.makeText(getApplicationContext(), "Clicked: " + primaryText, Toast.LENGTH_SHORT).show();
            //  Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.setLayout(R.layout.activity_all_bluemarketcharter);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.notice();
        super.setImageResource1(3);

        super.setTitle("BLUE MARKET PRODUCTS");
        _activity = this;
        _context = this;

        Bundle b = getIntent().getExtras();

        if (b != null) {
            category = b.getString("category");
            pass = b.getInt("pass");
        }

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        curru = pref.getString(USER_ID, "");
        regid = pref.getString(FIRE_REG_ID, "");
        //  Log.d("regid", regid);

        bell_counter = (MyTextView) findViewById(R.id.bell_counter);

        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        googleSearchOn();
        currentlocationsearch();
        initialize_all();

        bellcount();

    }

    @Override
    public void onStart() {
        super.onStart();
        AppController.activityResumed();
    }

    @Override
    public void onStop() {
        super.onStop();
        AppController.activityPaused();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideShowLayOut();
        bellcount();
    }

    void initialize_all() {

        ll_currentLocation = (LinearLayout) findViewById(R.id.ll_currentLocation);
        ll_otherLocation = (LinearLayout) findViewById(R.id.ll_otherLocation);
        ll_seekbar = (LinearLayout) findViewById(R.id.ll_seekbar);

        ll_locationText = (LinearLayout) findViewById(R.id.ll_locationText);
        ll_search_icon = (LinearLayout) findViewById(R.id.ll_search_icon);
        // tv_searchResultText = (TextView) findViewById(R.id.tv_searchResultText);

        tv_otherLocationText = (TextView) findViewById(R.id.tv_otherLocationText);
        tv_currentLocationText = (TextView) findViewById(R.id.tv_currentLocationText);
        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        tv_noDataFound = (TextView) findViewById(R.id.tv_noDataFound);
        seekBar = (SeekBar) findViewById(R.id.idseek);
        tv_rediousChange = (TextView) findViewById(R.id.tv_rediousChange);

        // for calender

        ll_datePicker_main = (LinearLayout) findViewById(R.id.ll_datePicker_main);
        ll_datePicker_main.setVisibility(View.GONE);
       /* frm_date = (Button) findViewById(R.id.frm_date);
        to_date = (Button) findViewById(R.id.to_date);
        edtfrmdate = (EditText) findViewById(R.id.edtfrmdate);
        edttodate = (EditText) findViewById(R.id.edttodate);*/
        /// recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);

        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }

        mLayoutManager = new LinearLayoutManager(_context);
        recyclerView.setLayoutManager(mLayoutManager);
        allProductDetailsList = new ArrayList<AllProductDetails>();
        blueMarketProductAdapter = new BlueMarketProductAdapter(_activity, _context, allProductDetailsList, token, category, curru);
        recyclerView.setAdapter(blueMarketProductAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(_activity, BlueMarketPicActivity.class);
                intent.putExtra("category", category);
                intent.putExtra("bmproduct", "1");
                startActivity(intent);
            }
        });

        clickListnerInitiliseAll();
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, Utility.BOUNDS_NEWYORK, null);
        autoCompView.setAdapter(mAdapter);
        hideShowLayOut();
    }

    void googleSearchOn() {
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, 0 /* clientId */, this).addApi(Places.GEO_DATA_API).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void clickListnerInitiliseAll() {
        ll_currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showStatus == 1) {
                    showStatus = 0;
                } else {
                    showStatus = 1;
                }
                hideShowLayOut();
            }
        });
        ll_otherLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (showStatus == 2) {
                    showStatus = 0;
                } else {
                    showStatus = 2;
                    nodatafound();

                }
                hideShowLayOut();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radious = String.valueOf(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        autoCompView.setOnItemClickListener(mAutocompleteClickListener);
        autoCompView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                _isValid = false;
                _lat = "";
                _long = "";
                //}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ll_search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showStatus == 2) {
                    if (!checkValidationForOtherLocation()) {
                        return;
                    }
                }
                checkBeforeServiceCall();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                tv_rediousChange.setText(progress + " km");
                radious = String.valueOf(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //   Toast.makeText(getApplicationContext(),"touch completed",Toast.LENGTH_SHORT).show();
                checkBeforeServiceCall();
            }
        });
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    void hideShowLayOut() {
        if (showStatus == 1) {
            // for current location
            /* ll_currentLocation.setBackgroundResource(R.color.amber_50);*/
            ll_seekbar.setVisibility(View.VISIBLE);
            ll_locationText.setVisibility(View.GONE);
            // tv_searchResultText.setText(getApplication().getString(R.string.search_result_for_current_location));
            ll_currentLocation.setBackgroundResource(R.drawable.blue_full_border);
            ll_otherLocation.setBackgroundResource(R.drawable.border1);
            tv_currentLocationText.setTextColor(ContextCompat.getColor(_activity, R.color.white));
            tv_otherLocationText.setTextColor(ContextCompat.getColor(_activity, R.color.black));

            checkBeforeServiceCall();
        } else if (showStatus == 2) {
            // other location search result
            ll_seekbar.setVisibility(View.GONE);
            ll_locationText.setVisibility(View.VISIBLE);
            ll_locationText.setBackgroundResource(R.drawable.border1);
            ll_currentLocation.setBackgroundResource(R.drawable.border1);
            ll_otherLocation.setBackgroundResource(R.drawable.blue_full_border);
            tv_currentLocationText.setTextColor(ContextCompat.getColor(_activity, R.color.black));
            tv_otherLocationText.setTextColor(ContextCompat.getColor(_activity, R.color.white));
        } else {
            ll_seekbar.setVisibility(View.GONE);
            ll_locationText.setVisibility(View.GONE);
            // tv_searchResultText.setText(getApplication().getString(R.string.search_result_for_choose_category));

            ll_currentLocation.setBackgroundResource(R.drawable.border1);
            ll_otherLocation.setBackgroundResource(R.drawable.border1);
            tv_currentLocationText.setTextColor(ContextCompat.getColor(_activity, R.color.black));
            tv_otherLocationText.setTextColor(ContextCompat.getColor(_activity, R.color.black));

            checkBeforeServiceCall();
        }
//        checkBeforeServiceCall();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RECORD_REQUEST_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isCurrentLocationGranted = true;
                    currentlocationsearch();

                } else {

                    isCurrentLocationGranted = false;
                }
            }
        }
    }

    private void currentlocationsearch() {
        int permission = ContextCompat.checkSelfPermission(BlueMarketProductActivityNew.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(BlueMarketProductActivityNew.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BlueMarketProductActivityNew.this);
                builder.setMessage("Permission to access the location is required for this app to access your location.")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        ActivityCompat.requestPermissions(BlueMarketProductActivityNew.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RECORD_REQUEST_CODE);

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                ActivityCompat.requestPermissions(BlueMarketProductActivityNew.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RECORD_REQUEST_CODE);

            }
        } else {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
            if (locationManager != null) {

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    fn_update(location);
                }

            } else {
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
                } else {
                    Toast.makeText(getBaseContext(), "No Provider Found , Please turn on your GPS", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private void fn_update(Location location) {
        // clat= location.getLatitude();
        // clong= location.getLongitude();
        current_location_lat = String.format("%.6f", location.getLatitude());
        current_location_long = String.format("%.6f", location.getLongitude());
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
                // Log.d("onResponse:", "" + response.body().getNotification_details());
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
//        Intent i = new Intent(BlueMarketProductActivityNew.this, CreateProfileActivity2.class);
//        i.putExtra("pass", 1);
//        startActivity(i);
        finish();
    }

    @Override
    public void onLocationChanged(Location location) {
        fn_update(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
    }

    void checkBeforeServiceCall() {

        if (showStatus == 0) {
            serverCall(category, "", "", "");
        } else if (showStatus == 1) {
            if (current_location_lat != null && current_location_long != null && current_location_lat.length() > 1 && current_location_long.length() > 1) {
                serverCall("", current_location_lat, current_location_long, radious);
            } else {
                // oDialog("Current Location Not Found.", "Close", "", "", "", false, _activity,"",0);
                oDialog(getString(R.string.gps_off), "Close", "", false, _activity, "", 0);
                return;
            }

        } else if (showStatus == 2) {
            if (checkValidationForOtherLocation()) {
                serverCall("", _lat, _long, Utility.DEFAULT_RADIOUS);
            }
        }
    }

    boolean checkValidationForOtherLocation() {

        String location = autoCompView.getText().toString().trim();
        if (location.isEmpty()) {
            oDialog(getString(R.string.enter_location), "Close", "", false, _activity, "", 0);
            autoCompView.setFocusable(true);
            autoCompView.requestFocus();
            return false;
        }
        return true;
    }

    public void serverCall(String sendCategory, String user_lat, String user_long, String radius_send) {
        if (isActivityVisible()) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(_activity);
                progressDialog.setTitle(getString(R.string.loading_title));
                progressDialog.setCancelable(false);
                progressDialog.setMessage(getString(R.string.loading_message));
            }

            progressDialog.show();
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            // Call<AllCourse> userCall = service.allCourse("Bearer " + token,category);
            // Call<AllProduct> userCall = service.allProduct("Bearer " + token,category);
            Call<AllProduct> userCall = service.allProductSearch("Bearer " + token, sendCategory, user_lat, user_long, radius_send);
            userCall.enqueue(new Callback<AllProduct>() {
                @Override
                public void onResponse(Call<AllProduct> call, Response<AllProduct> response) {

                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (response.code() == AppConfig.SUCCESS_STATUS) {
                        if (response.body().getStatus() == true && response.body().getProduct_list().size() > 0) {
                            tv_noDataFound.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            allProductDetailsList.clear();
                            allProductDetailsList.addAll(response.body().getProduct_list());
                            blueMarketProductAdapter.notifyDataSetChanged();
                        } else {
                            nodatafound();
                        }
                    } else if (response.code() == AppConfig.ERROR_STATUS_LOG_AGAIN) {
                        // login again
                        logoutAndSignInPage(_activity);

                    } else {
                        Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<AllProduct> call, Throwable t) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(getApplicationContext(), getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    void nodatafound() {
        recyclerView.setVisibility(View.GONE);
        tv_noDataFound.setText(getText(R.string.nodatafound));
        tv_noDataFound.setVisibility(View.VISIBLE);
    }
}
