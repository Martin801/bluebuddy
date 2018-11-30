package com.bluebuddy.activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.bluebuddy.R;
import com.bluebuddy.adapters.PlaceAutocompleteAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.ServerResponseLoc;
import com.bluebuddy.interfaces.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_STEP;
import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

//MAP

public class LocationActivity extends BuddyActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int RECORD_REQUEST_CODE = 101;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(new LatLng(-34.041458, 150.790100), new LatLng(40.719268, -74.006287));
    /**
     * GoogleApiClient wraps our service connection to Google Play Services and provides access
     * to the user's sign in state as well as the Google's APIs.
     */
    protected GoogleApiClient mGoogleApiClient;
    private EditText cp6weblnk;
    private AutoCompleteTextView autoCompView;
    private Button cp6nxt, cp6skp;
    private Activity _myActivity;
    private ImageView back;
    private SharedPreferences pref;
    private String token, TAG, _lat, _long, _lat1, _long1;
    private boolean _isValid = false;
    private PlaceAutocompleteAdapter mAdapter;

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
            _lat = String.valueOf(place.getLatLng().latitude);
            _long = String.valueOf(place.getLatLng().longitude);

            Log.d("LATLONG:", place.getLatLng().toString());
            Log.i(TAG, "Place details received: " + place.getName());
            places.release();
        }
    };

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_location);
        super.onCreate(savedInstanceState);
//
        //today23
        int permission = ContextCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("HELLO", "Permission to location denied");

            if (ActivityCompat.shouldShowRequestPermissionRationale(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);
                builder.setMessage("Permission to access the location is required for this app to access your location.")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("HELLO", "Clicked");
                        // makeRequest();
                        //
                        ActivityCompat.requestPermissions(LocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RECORD_REQUEST_CODE);

                        //
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                //  makeRequest();
                ActivityCompat.requestPermissions(LocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RECORD_REQUEST_CODE);

            }
        }

        //today23

        //today23

        //
        // Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using AutoManage
        // functionality, which automatically sets up the API client to handle Activity lifecycle
        // events. If your activity does not extend FragmentActivity, make sure to call connect()
        // and disconnect() explicitly.
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, 0 /* clientId */, this).addApi(Places.GEO_DATA_API).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.initializeElements();

        TAG = "LocationActivity";
        _myActivity = this;

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
    }

    protected void setMyPref(String key, String value) {
        super.setPref(key, value);
    }

    private void initializeElements() {
        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        autoCompView = findViewById(R.id.autoCompleteTextView);

        // Register a listener that receives callbacks when a suggestion has been selected
        autoCompView.setOnItemClickListener(mAutocompleteClickListener);

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_GREATER_SYDNEY, null);
        autoCompView.setAdapter(mAdapter);

        //map
        back = findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        //comment today below line2 cp6weblink
        cp6nxt = findViewById(R.id.weblnknxtid);
        TextView tx = findViewById(R.id.idtxt1);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");
        tx.setTypeface(custom_font);

        autoCompView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //if(s.length() <= 0){
                _isValid = false;
                _lat = "";
                _long = "";
                //}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cp6nxt.setOnClickListener(v -> {
            if (_isValid)
                cp6nxt();
            else
                oDialog("Select your location!", "Close", "", false, _myActivity, "", 0);
        });
    }

    public void cp6nxt() {

        String location = autoCompView.getText().toString().trim();

        if (location.isEmpty()) {
            oDialog("Enter your Location", "Close", "", false, _myActivity, "", 0);
        } else {
            location = autoCompView.getText().toString();
            Log.d(TAG, "location:" + location + ",lat:" + _lat + ",long:" + _long);
            double _lat3 = Double.parseDouble(_lat);
            double _long3 = Double.parseDouble(_long);
            _lat1 = String.format("%.6f", _lat3);
            _long1 = String.format("%.6f", _long3);
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<ServerResponseLoc> userCall = service.Loc("Bearer " + token, "put", location, _lat1, _long1, 8);
            userCall.enqueue(new Callback<ServerResponseLoc>() {
                @Override
                public void onResponse(Call<ServerResponseLoc> call, Response<ServerResponseLoc> response) {
                    if (response.body().getStatus() == "true") {
                        setMyPref(ACCESS_STEP, "8");
                        Log.d("onResponse", "" + response.body().getStatus());

                        Intent intent = new Intent(LocationActivity.this, CreateProfileActivity7.class);
                        startActivity(intent);
//                        finish();
                    } else {
                        Log.d("onResponse", "" + response.body().getStatus());
                        logoutAndSignInPage(LocationActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ServerResponseLoc> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                }
            });

        }
    }

    /**
     * @param msg
     * @param btnText
     * @param btnText2
     * @param _redirect
     * @param activity
     * @param _redirectClass
     * @param layout
     */
    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

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
        Toast.makeText(this, "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {

//        super.onBackPressed();
//        setMyPref(ACCESS_STEP, "6"); // CreateProfile6
//        Intent i = new Intent(LocationActivity.this, CreateProfileActivity6.class);
//        startActivity(i);

        finish();
    }


}