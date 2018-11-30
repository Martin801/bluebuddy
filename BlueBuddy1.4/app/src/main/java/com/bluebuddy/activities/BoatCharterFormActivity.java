package com.bluebuddy.activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bluebuddy.R;
import com.bluebuddy.adapters.PlaceAutocompleteAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class BoatCharterFormActivity extends BuddyActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int RECORD_REQUEST_CODE = 101;
    private static final LatLngBounds BOUNDS_NEWYORK = new LatLngBounds(new LatLng(-34.041458, 150.790100), new LatLng(40.719268, -74.006287));
    protected GoogleApiClient mGoogleApiClient;
    Button bcf1;
    EditText chrtname, chrtdesc, chrtloc, chrtfd, chrthd, chrtbtype, chrtbcap;
    Activity _myActivity;
    MyTextView bell_counter;
    private String token, TAG, _lat, _long, bellcounter, _lat1, _long1;
    private SharedPreferences pref;
    private String encode_image;
    private ImageView back;
    private boolean _isValid = false;
    private AutoCompleteTextView autoCompView;
    private PlaceAutocompleteAdapter mAdapter;
    private String hideContact;
    private CheckBox cb_hideContactDetails;
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
        super.setLayout(R.layout.activity_boat_charter_form);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.notice();
        super.setTitle("BLUE MARKET RENTALS/CHARTERS");
        _myActivity = this;

        int permission = ContextCompat.checkSelfPermission(BoatCharterFormActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("HELLO", "Permission to location denied");

            if (ActivityCompat.shouldShowRequestPermissionRationale(BoatCharterFormActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BoatCharterFormActivity.this);
                builder.setMessage("Permission to access the location is required for this app to access your location.")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("HELLO", "Clicked");

                        ActivityCompat.requestPermissions(BoatCharterFormActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RECORD_REQUEST_CODE);

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {

                ActivityCompat.requestPermissions(BoatCharterFormActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RECORD_REQUEST_CODE);
            }
        }

        TAG = "LocationActivity";
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, 0 /* clientId */, this).addApi(Places.GEO_DATA_API).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle validation = getIntent().getExtras();

      /*  if (validation.containsKey("encoded_image"))
            encode_image = validation.getString("encoded_image");*/


        if (validation.containsKey("encoded_image")) {
            encode_image = validation.getString("encoded_image");

            //   String tmp = validation.getString("encoded_image");
          /*  if (!encode_image.equals("")) {
                if (tmp.contains(",")) {
                    encoded_imagelist = encode_image.split(",");
                } else {
                    encoded_imagelist = new String[1];
                    encoded_imagelist[0] = encode_image;
                }
            }*/
        }


        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");

        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompView.setOnItemClickListener(mAutocompleteClickListener);
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_NEWYORK, null);
        autoCompView.setAdapter(mAdapter);

        cb_hideContactDetails = findViewById(R.id.cb_hideContactDetails);
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        bcf1 = (Button) findViewById(R.id.chrtbtn);
        chrtname = (EditText) findViewById(R.id.chrtname);
        chrtdesc = (EditText) findViewById(R.id.chrtdesc);
        chrtfd = (EditText) findViewById(R.id.chrtfd);
        chrthd = (EditText) findViewById(R.id.chrthd);
        chrtbtype = (EditText) findViewById(R.id.chrtbtype);
        chrtbcap = (EditText) findViewById(R.id.chrtbcap);

        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(BoatCharterFormActivity.this, BlueMarketCharterPicActivity.class);
//                i.putExtra("bmcharter", "0");
//                startActivity(i);

                onBackPressed();

            }
        });

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

        bcf1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chrtname1 = chrtname.getText().toString().trim();
                String chrtdesc1 = chrtdesc.getText().toString().trim();
                String chrtfd1 = chrtfd.getText().toString().trim();
                String chrthd1 = chrthd.getText().toString().trim();
                String chrtbtype1 = chrtbtype.getText().toString().trim();
                String chrtbcap1 = chrtbcap.getText().toString().trim();

                String location = autoCompView.getText().toString().trim();

                if (cb_hideContactDetails.isChecked())
                    hideContact = "1";
                else
                    hideContact = "0";

                if (chrtname1.isEmpty()) {
                    oDialog("Enter Charter name", "Close", "", false, _myActivity, "", 0);
                } else if (chrtdesc1.isEmpty()) {
                    oDialog("Enter Charter description ", "Close", "", false, _myActivity, "", 0);
                } else if (location.isEmpty()) {
                    oDialog("Enter location ", "Close", "", false, _myActivity, "", 0);
                } else if (chrtfd1.isEmpty()) {
                    oDialog("Enter amount for full day ", "Close", "", false, _myActivity, "", 0);
                } else if (chrthd1.isEmpty()) {
                    oDialog("Enter amount for half day", "Close", "", false, _myActivity, "", 0);
                } else if (chrtbtype1.isEmpty()) {
                    oDialog("Enter boat type", "Close", "", false, _myActivity, "", 0);
                } else if (chrtbcap1.isEmpty()) {
                    oDialog("Enter boat capacity", "Close", "", false, _myActivity, "", 0);
                } else {

                    location = autoCompView.getText().toString();
                    Log.d(TAG, "location:" + location + ",lat:" + _lat + ",long:" + _long);
                    double _lat3 = Double.parseDouble(_lat);
                    double _long3 = Double.parseDouble(_long);
                    _lat1 = String.format("%.6f", _lat3);
                    _long1 = String.format("%.6f", _long3);

                    Intent i = new Intent(BoatCharterFormActivity.this, ListingQueryActivity.class);
                    i.putExtra("cName", chrtname1);
                    i.putExtra("cDesc", chrtdesc1);
                    i.putExtra("cLoc", location);
                    i.putExtra("cFPrice", chrtfd1);
                    i.putExtra("cHPrice", chrthd1);
                    i.putExtra("cType", chrtbtype1);
                    i.putExtra("cCap", chrtbcap1);
                    i.putExtra("cImage", encode_image);
                    i.putExtra("cLat", _lat1);
                    i.putExtra("cLong", _long1);
                    i.putExtra("cHideContact", hideContact);
                    startActivity(i);
                    finish();


                }
            }
        });
        bellcount();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
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

}
