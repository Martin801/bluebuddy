package com.bluebuddy.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.CheckBox;
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

public class BlueMarketProductActivity extends BuddyActivity implements AdapterView.OnItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RECORD_REQUEST_CODE = 101;
    private static final LatLngBounds BOUNDS_NEWYORK = new LatLngBounds(new LatLng(-34.041458, 150.790100), new LatLng(40.719268, -74.006287));
    protected GoogleApiClient mGoogleApiClient;
    private Button btn1;
    private Activity _myActivity;
    private String[] product_menu = {"Scuba Diving", "Freediving", "Fishing", "Spearfishing", "Boats", "Others"};
    private String spinneritem = "";
    private ImageView back;
    private MyTextView bell_counter;
    private Spinner spinner4;
    private LinearLayout newlvl1;
    private EditText certlvlid1, bmpname, bmpdesc, bmpprice;
    private Button minus_level1;
    private View certlvlid1_view;
    private String token, TAG, _lat, _long, _lat1, _long1, co_cat, Othertxt;
    private SharedPreferences pref;
    private String encode_image1, bellcounter, encode_image2, encode_image3, encode_image4 = "";
    private String encoded_imagelist[];
    private ProgressDialog mProgressDialog;
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

            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };
    private CheckBox cb_hideContactDetails;
    private String hideContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_blue_market_product);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.notice();
        super.setTitle("BLUE MARKET PRODUCT");

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");

        certlvlid1 = (EditText) findViewById(R.id.certlvlid1);
        minus_level1 = (Button) findViewById(R.id.minus_level1);
        newlvl1 = (LinearLayout) findViewById(R.id.newlvl1);
        certlvlid1_view = (View) findViewById(R.id.certlvlid1_view);
        back = (ImageView) findViewById(R.id.imgNotification2);
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        cb_hideContactDetails = findViewById(R.id.cb_hideContactDetails);
//        TextView IDtxt1 = (TextView) findViewById(R.id.idtxt1);
//        TextView IDtxt2 = (TextView) findViewById(R.id.idtxt2);
//        TextView IDtxt3 = (TextView) findViewById(R.id.idtxt3);
//        TextView IDtxt4 = (TextView) findViewById(R.id.idtxt4);
//        TextView IDtxt5 = (TextView) findViewById(R.id.idtxt5);
//        TextView IDtxt6 = (TextView) findViewById(R.id.idtxt6);

        _myActivity = this;

        int permission = ContextCompat.checkSelfPermission(BlueMarketProductActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // Log.i("HELLO", "Permission to location denied");

            if (ActivityCompat.shouldShowRequestPermissionRationale(BlueMarketProductActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BlueMarketProductActivity.this);
                builder.setMessage("Permission to access the location is required for this app to access your location.")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        // Log.i("HELLO", "Clicked");

                        ActivityCompat.requestPermissions(BlueMarketProductActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RECORD_REQUEST_CODE);

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                ActivityCompat.requestPermissions(BlueMarketProductActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RECORD_REQUEST_CODE);

            }
        }
//
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(BlueMarketProductActivity.this, BlueMarketPicActivity.class);
//                //  i.putExtra("category", category);
//                i.putExtra("bmproduct", "0");
//                startActivity(i);
                onBackPressed();
            }
        });
        //

        TAG = "LocationActivity";
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, 0 /* clientId */, this).addApi(Places.GEO_DATA_API).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle validation = getIntent().getExtras();

        if (validation.containsKey("encoded_image")) {
            String tmp = validation.getString("encoded_image");
            if (!tmp.matches("")) {
                if (tmp.contains(",")) {
                    encoded_imagelist = tmp.split(",");
                } else {
                    encoded_imagelist = new String[1];
                    encoded_imagelist[0] = tmp;
                }
            }
        }

        int tmpLen = -1;

        if (encoded_imagelist != null)
            tmpLen = encoded_imagelist.length - 1;


        encode_image1 = tmpLen >= 0 ? (encoded_imagelist[0].equals("") ? "" : encoded_imagelist[0]) : "";

        encode_image2 = tmpLen >= 1 ? (encoded_imagelist[1].equals("") ? "" : encoded_imagelist[1]) : "";

        encode_image3 = tmpLen >= 2 ? (encoded_imagelist[2].equals("") ? "" : encoded_imagelist[2]) : "";

        encode_image4 = tmpLen >= 3 ? (encoded_imagelist[3].equals("") ? "" : encoded_imagelist[3]) : "";

       /* encode_image1 = tmpLen >= 0 ? (encoded_imagelist[0].equals("") ? "" : "data:image/jpeg;base64," + encoded_imagelist[0]) : "";
       // Log.d("1st image :", encode_image1);
        encode_image2 = tmpLen >= 1 ? (encoded_imagelist[1].equals("") ? "" : "data:image/jpeg;base64," + encoded_imagelist[1]) : "";
      //  Log.d("2nd image :", encode_image2);
        encode_image3 = tmpLen >= 2 ? (encoded_imagelist[2].equals("") ? "" : "data:image/jpeg;base64," + encoded_imagelist[2]) : "";
        Log.d("3rd image :", encode_image3);
        encode_image4 = tmpLen >= 3 ? (encoded_imagelist[3].equals("") ? "" : "data:image/jpeg;base64," + encoded_imagelist[3]) : "";*/
        //  Log.d("4th image :", encode_image4);


        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");


        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompView.setOnItemClickListener(mAutocompleteClickListener);
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_NEWYORK, null);
        autoCompView.setAdapter(mAdapter);

        spinner4 = (Spinner) findViewById(R.id.spacttype4);
        spinner4.setOnItemSelectedListener(this);

        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), product_menu);
        spinner4.setAdapter(customAdapter);
//        spinner4.setSelection(product_menu.length - 1);

        spinneritem = spinner4.getSelectedItem().toString();
        minus_level1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                spinner4.setSelection(product_menu.length - 1);
                newlvl1.setVisibility(View.GONE);
                certlvlid1_view.setVisibility(View.GONE);
                certlvlid1.setText("");

            }
        });


        btn1 = (Button) findViewById(R.id.btbcadvf);
        bmpname = (EditText) findViewById(R.id.bmpname);
        spinner4 = (Spinner) findViewById(R.id.spacttype4);
        bmpdesc = (EditText) findViewById(R.id.bmpdesc);
        bmpprice = (EditText) findViewById(R.id.bmpprice);

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


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bmpname1 = bmpname.getText().toString().trim();

                String bmpdesc1 = bmpdesc.getText().toString().trim();
                String bmpprice1 = bmpprice.getText().toString().trim();
                String location = autoCompView.getText().toString().trim();

                if(cb_hideContactDetails.isChecked())
                    hideContact = "1";
                else
                    hideContact = "0";

                if (bmpname1.isEmpty()) {
                    oDialog("Enter Product name", "Close", "", false, _myActivity, "", 0);
                }
                if (String.valueOf(spinner4.getSelectedItem()) == "Select Product Type") {
                    oDialog("Select Product Category", "Close", "", false, _myActivity, "", 0);
                } else if (String.valueOf(spinner4.getSelectedItem()) == "Others" && certlvlid1.getText().toString().isEmpty()) {
                    oDialog("Please enter category name", "Close", "", false, _myActivity, "", 0);
                } else if (bmpdesc1.isEmpty()) {
                    oDialog("Enter product description ", "Close", "", false, _myActivity, "", 0);
                } else if (location.isEmpty()) {
                    oDialog("Enter Location", "Close", "", false, _myActivity, "", 0);
                } else if (bmpprice1.isEmpty()) {
                    oDialog("Enter product price ", "Close", "", false, _myActivity, "", 0);
                } else {

                    if (String.valueOf(spinner4.getSelectedItem()) == "Others") {

                        co_cat = certlvlid1.getText().toString().trim();
                        // Log.d("Othertxt1", co_cat);
                        Othertxt = "Others:" + co_cat;
                        // Log.d("Othertxt2", Othertxt);
                        spinneritem = Othertxt;
                        // Log.d("cat co", spinneritem);
                    } else {
                        spinneritem = spinner4.getSelectedItem().toString().trim();
                        //Log.d("co cat", spinneritem);
                    }

                    location = autoCompView.getText().toString();
                    //Log.d(TAG, "location:" + location + ",lat:" + _lat + ",long:" + _long);
                    /*double _lat3 = Double.parseDouble(_lat);
                    double _long3 = Double.parseDouble(_long);
                    _lat1 = String.format("%.6f", _lat3);
                    _long1 = String.format("%.6f", _long3);*/

                    Intent i = new Intent(BlueMarketProductActivity.this, ListingQueryActivityProduct.class);
                    i.putExtra("Pimg1", encode_image1);
                    i.putExtra("Pimg2", encode_image2);
                    i.putExtra("Pimg3", encode_image3);
                    i.putExtra("Pimg4", encode_image4);
                    i.putExtra("Pname", bmpname1);
                    i.putExtra("Ptype", spinneritem);
                    i.putExtra("Pdesc", bmpdesc1);
                    i.putExtra("Pprice", bmpprice1);
                    i.putExtra("Ploc", location);
                    i.putExtra("Plat", _lat);
                    i.putExtra("Plong", _long);
                    i.putExtra("PhideContact",hideContact);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinneritem = product_menu[position];
        if (spinneritem.equals("Others")) {
            if (newlvl1.getVisibility() == View.GONE) {
                newlvl1.setVisibility(View.VISIBLE);
                certlvlid1_view.setVisibility(View.VISIBLE);

            } else {

                newlvl1.setVisibility(View.GONE);
                certlvlid1_view.setVisibility(View.GONE);

            }
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
//        Intent i = new Intent(BlueMarketProductActivity.this, Categories_bluemarketActivity.class);
//        startActivity(i);
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
