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
import com.bluebuddy.adapters.BlueMarketProductAdapter;
import com.bluebuddy.adapters.PlaceAutocompleteAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.AllProduct;
import com.bluebuddy.models.AllProductDetails;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.USER_ID;

public class LocationBasedSearchActivity2Product extends BuddyActivity implements GoogleApiClient.OnConnectionFailedListener {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    private ArrayList<AllProductDetails> BMCList;
    private BlueMarketProductAdapter blueMarketProductAdapter;

    LocationManager locationManager;
    String provider;
    SeekBar seek;
    private AutoCompleteTextView autoCompView;
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private static final LatLngBounds BOUNDS_NEWYORK = new LatLngBounds(new LatLng(-34.041458, 150.790100), new LatLng(40.719268, -74.006287));

    ImageView back;
    Button rdsbtn;
    private Activity _activity;
    private Context _context;
    private String token, TAG, id, rds, s, _lat, _long, category, bellcounter;
    private SharedPreferences pref;
    Double clat, clong;
    private boolean _isValid = false;
    private String _lat1, _long1, curru;
    MyTextView bell_counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_people_search_olocation2_product);

        super.onCreate(savedInstanceState);
        super.initialize();
        super.setTitle("SEARCH");
        super.notice();
        _activity = this;
        _context = this;
        TAG = "LocationActivity";
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, 0 /* clientId */, this).addApi(Places.GEO_DATA_API).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        curru = pref.getString(USER_ID, "");

        bellcount();
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        Bundle b = getIntent().getExtras();
        category = b.getString("cat1");
        Log.d("hello1", category);
        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        autoCompView.setOnItemClickListener(mAutocompleteClickListener);

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
                _isValid = false;
                _lat = "";
                _long = "";

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_event);
        recyclerView.setNestedScrollingEnabled(false);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }
        mLayoutManager = new LinearLayoutManager(_context);
        recyclerView.setLayoutManager(mLayoutManager);

        blueMarketProductAdapter = new BlueMarketProductAdapter(_activity, _context, null, token, category, curru);
        recyclerView.setAdapter(blueMarketProductAdapter);
        blueMarketProductAdapter.notifyDataSetChanged();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LocationBasedSearchActivity2Product.this, BlueMarketProductActivityNew.class);
                i.putExtra("category", category);
                startActivity(i);
            }
        });


        rdsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_isValid)
                    currentlocationpeople();
                else
                    oDialog("Select your location!", "Ok", "", false, _activity, "", 0);

            }
        });

    }


    private void currentlocationpeople() {
        String location = autoCompView.getText().toString().trim();

        if (location.isEmpty()) {
            oDialog("Enter your Location", "Close", "", false, _activity, "", 0);
        } else {
            location = autoCompView.getText().toString();
            Log.d(TAG, "location:" + location + ",lat:" + _lat + ",long:" + _long);

            double _lat3 = Double.parseDouble(_lat);
            double _long3 = Double.parseDouble(_long);
            _lat1 = String.format("%.6f", _lat3);
            _long1 = String.format("%.6f", _long3);


            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

            Call<AllProduct> userCall = service.allproduct11("Bearer " + token, _lat1, _long1, 50, category);
            userCall.enqueue(new Callback<AllProduct>() {
                @Override
                public void onResponse(Call<AllProduct> call, Response<AllProduct> response) {

                    ArrayList<AllProductDetails> details = response.body().getProduct_list();

                    if (details != null) {
                        blueMarketProductAdapter.updateAllProduct(response.body().getProduct_list());
                    }
                    if (details == null) {
                        oDialog("No Products Found for this Location", "Close", "", false, _activity, "", 0);
                    }

                    Log.d("STATUS", String.valueOf(response.body().getStatus()));
                    Log.d("TOKEN:", "" + token);


                }

                @Override
                public void onFailure(Call<AllProduct> call, Throwable t) {
                }
            });


        }

    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final String btnText3, final String btnText4, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog4(msg, btnText, btnText2, btnText3, btnText4, _redirect, activity, _redirectClass, layout);
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);


            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }

            final Place place = places.get(0);
            _isValid = true;

            _lat = String.valueOf(place.getLatLng().latitude);
            _long = String.valueOf(place.getLatLng().longitude);

            Log.d("LATLONG:", place.getLatLng().toString());
            Log.i(TAG, "Place details received: " + place.getName());
            places.release();
        }
    };


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

        Toast.makeText(this, "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
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
        Intent i = new Intent(LocationBasedSearchActivity2Product.this, BlueMarketProductActivityNew.class);
        i.putExtra("category", category);
        startActivity(i);
    }
}

