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
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.bluebuddy.adapters.CustomAdapter;
import com.bluebuddy.adapters.PlaceAutocompleteAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class BlueMarketCoursesActivity extends BuddyActivity implements AdapterView.OnItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {
    private static final int RECORD_REQUEST_CODE = 101;
    private static final LatLngBounds BOUNDS_NEWYORK = new LatLngBounds(new LatLng(-34.041458, 150.790100), new LatLng(40.719268, -74.006287));
    protected GoogleApiClient mGoogleApiClient;
    String co_cat, Othertxt;
    private Button BMCOUSRE, BMCOUSRESKP;
    private Activity _myActivity;
    private String[] courses_menu = {"Scuba Diving", "Freediving", "Others"};
    private String spinneritem = "";
    private ImageView back;
    private Spinner spinner3;
    private LinearLayout newlvl1;
    private EditText certlvlid1, co_name, agency_name, agency_url, co_duration, co_desc, co_price, co_location;
    private Button minus_level1;
    private View certlvlid1_view;
    private String token, TAG, _lat, _long, _lat1, _long1;
    private SharedPreferences pref;
    private String encode_image, category, bellcounter;
    private MyTextView bell_counter;
    private boolean _isValid = false;
    private AutoCompleteTextView autoCompView;
    private PlaceAutocompleteAdapter mAdapter;
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }

            final Place place = places.get(0);
            _isValid = true;
            _lat = String.valueOf(place.getLatLng().latitude);
            _long = String.valueOf(place.getLatLng().longitude);

            //  Log.d("LATLONG:", place.getLatLng().toString());
            //    Log.i(TAG, "Place details received: " + place.getName());
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

            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_blue_market_courses);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.notice();
        super.setTitle("BLUE MARKET COURSE");

        int permission = ContextCompat.checkSelfPermission(BlueMarketCoursesActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("HELLO", "Permission to location denied");

            if (ActivityCompat.shouldShowRequestPermissionRationale(BlueMarketCoursesActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BlueMarketCoursesActivity.this);
                builder.setMessage("Permission to access the location is required for this app to access your location.")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("HELLO", "Clicked");

                        ActivityCompat.requestPermissions(BlueMarketCoursesActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RECORD_REQUEST_CODE);

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                ActivityCompat.requestPermissions(BlueMarketCoursesActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RECORD_REQUEST_CODE);

            }
        }

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");

        TAG = "LocationActivity";
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, 0 /* clientId */, this).addApi(Places.GEO_DATA_API).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle b = getIntent().getExtras();
        category = b.getString("category");


        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompView.setOnItemClickListener(mAutocompleteClickListener);
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_NEWYORK, null);
        autoCompView.setAdapter(mAdapter);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");
        certlvlid1 = (EditText) findViewById(R.id.certlvlid1);
        minus_level1 = (Button) findViewById(R.id.minus_level1);
        newlvl1 = (LinearLayout) findViewById(R.id.newlvl1);
        certlvlid1_view = (View) findViewById(R.id.certlvlid1_view);
        agency_name = (EditText) findViewById(R.id.agnname);
        agency_url = (EditText) findViewById(R.id.agnurl);
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        co_duration = (EditText) findViewById(R.id.codur);
        co_desc = (EditText) findViewById(R.id.codesc);
        co_price = (EditText) findViewById(R.id.coprice);
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

        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(BlueMarketCoursesActivity.this, BlueMarketCoursePicActivity.class);
//                i.putExtra("category", category);
//                i.putExtra("bmcourse", "0");
//                startActivity(i);
                onBackPressed();
            }
        });

        TextView IDtxt1 = (TextView) findViewById(R.id.idtxt1);
        TextView IDtxt2 = (TextView) findViewById(R.id.idtxt2);
        TextView IDtxt3 = (TextView) findViewById(R.id.idtxt3);
        TextView IDtxt4 = (TextView) findViewById(R.id.idtxt4);
        TextView IDtxt5 = (TextView) findViewById(R.id.idtxt5);
        TextView IDtxt6 = (TextView) findViewById(R.id.idtxt6);
        TextView IDtxt7 = (TextView) findViewById(R.id.idtxt7);
        TextView IDtxt8 = (TextView) findViewById(R.id.idtxt8);

        _myActivity = this;
        Bundle validation = getIntent().getExtras();

        encode_image = validation.getString("encoded_image");
        category = validation.getString("DATA_VALUE");

        Log.d("Img receive courseAct", encode_image.toString());

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");

        spinner3 = (Spinner) findViewById(R.id.spacttype3);
        spinner3.setOnItemSelectedListener(this);

        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), courses_menu);
        spinner3.setAdapter(customAdapter);
//        spinner3.setSelection(courses_menu.length - 1);

        spinneritem = spinner3.getSelectedItem().toString();
        minus_level1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner3.setSelection(courses_menu.length - 1);
                newlvl1.setVisibility(View.GONE);
                certlvlid1_view.setVisibility(View.GONE); /*Gone is used for ommit the layout space*/
                certlvlid1.setText("");

            }
        });

        BMCOUSRE = (Button) findViewById(R.id.bmcoursecontid);
        BMCOUSRE.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String location = autoCompView.getText().toString().trim();

                String agency_name1 = agency_name.getText().toString().trim();
                String agency_url1 = agency_url.getText().toString().trim();
                String co_duration1 = co_duration.getText().toString().trim();
                String co_desc1 = co_desc.getText().toString().trim();
                String co_price1 = co_price.getText().toString().trim();


                //String encode_image1 = "data:image/jpeg;base64," + encode_image;
                String encode_image1 = encode_image;
                //Log.d("course image:", encode_image1);

                /*if (String.valueOf(spinner3.getSelectedItem()) == "Select Course Type") {
                    oDialog("Enter the Course", "Close", "", false, _myActivity, "", 0);
                } else*/
                if (String.valueOf(spinner3.getSelectedItem()) == "Others" && certlvlid1.getText().toString().isEmpty()) {
                    oDialog("Please enter your course name", "Close", "", false, _myActivity, "", 0);
                } else if (agency_name1.isEmpty()) {
                    oDialog("Enter Agency name ", "Close", "", false, _myActivity, "", 0);
                } else if (co_duration1.isEmpty()) {
                    oDialog("Enter Course duration ", "Close", "", false, _myActivity, "", 0);
                } else if (co_desc1.isEmpty()) {
                    oDialog("Enter course description ", "Close", "", false, _myActivity, "", 0);
                } else if (co_price1.isEmpty()) {
                    oDialog("Enter course price", "Close", "", false, _myActivity, "", 0);
                } else if (location.isEmpty()) {
                    oDialog("Enter Location", "Close", "", false, _myActivity, "", 0);
                } else {

                    if (String.valueOf(spinner3.getSelectedItem()) == "Others") {
                        co_cat = certlvlid1.getText().toString().trim();
                        // Log.d("Othertxt1", co_cat);
                        Othertxt = "Others:" + co_cat;
                        //  Log.d("Othertxt2", Othertxt);
                        co_cat = Othertxt;
                        //  Log.d("cat co", co_cat);
                    } else {
                        co_cat = spinner3.getSelectedItem().toString().trim();
                        // Log.d("co cat", co_cat);
                    }


                    location = autoCompView.getText().toString();
                    Log.d(TAG, "location:" + location + ",lat:" + _lat + ",long:" + _long);
                    double _lat3 = Double.parseDouble(_lat);
                    double _long3 = Double.parseDouble(_long);
                    _lat1 = String.format("%.6f", _lat3);
                    _long1 = String.format("%.6f", _long3);

                    Intent i = new Intent(BlueMarketCoursesActivity.this, ListingQueryCourse.class);
                    i.putExtra("Ccat", co_cat);
                    i.putExtra("Cagn", agency_name1);
                    i.putExtra("Curl", agency_url1);
                    i.putExtra("Cdur", co_duration1);
                    i.putExtra("Cdesc", co_desc1);
                    i.putExtra("Cpr", co_price1);
                    i.putExtra("Cloc", location);
                    i.putExtra("Cimg", encode_image1);
                    i.putExtra("Clat", _lat1);
                    i.putExtra("Clong", _long1);
                    startActivity(i);
                    finish();
                }

            }
        });
        bellcount();
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    public void oDialogReturn(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, String returnParam) {
        super.openDialogReturn(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, returnParam);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinneritem = courses_menu[position];

        if (spinneritem.equals("Others")) {
            if (newlvl1.getVisibility() == View.GONE) {
                newlvl1.setVisibility(View.VISIBLE);
                certlvlid1_view.setVisibility(View.VISIBLE);
            } else {
                newlvl1.setVisibility(View.GONE);
                certlvlid1_view.setVisibility(View.GONE);
            }
        } else if (!spinneritem.equals("Others")) {
            newlvl1.setVisibility(View.GONE);
            certlvlid1_view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this, "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        finish();
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
