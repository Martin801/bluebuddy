package com.bluebuddy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.classes.ServerResponsechrt;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.CharterDetailResponse;
import com.bluebuddy.models.CommonResponse;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class BoatCharterFormEditActivity extends BuddyActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final LatLngBounds BOUNDS_NEWYORK = new LatLngBounds(new LatLng(-34.041458, 150.790100), new LatLng(40.719268, -74.006287));
    private static final String TAG = "BChartFormEditActivity";
    protected GoogleApiClient mGoogleApiClient;
    Button bcf1, chrtbtndlt;
    EditText chrtname, chrtdesc, chrtloc, chrtfd, chrthd, chrtbtype, chrtbcap;
    Activity _myActivity;
    ImageView back;
    private String token, charter_id, _lat, _long;
    private SharedPreferences pref;
    private MyTextView bell_counter;
    private String encode_image;
    private boolean _isValid = false;
    private AutoCompleteTextView autoCompView;
    private PlaceAutocompleteAdapter mAdapter;
    private String image, bellcounter, name, location, full_price, half_price, type_of_boat, capacity, description;
    private ProgressDialog mProgressDialog;
    private ProgressDialog progressDialog;

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
    private CheckBox cb_hideContactDetails;
    private String hideContact;
    private String imageData = "";
    private File fileImage1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.setLayout(R.layout.activity_boat_charter_form2);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.notice();
        super.setTitle("BLUE MARKET CHARTER");
        super.loader();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;
        _myActivity = this;

        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, 0, this).addApi(Places.GEO_DATA_API).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle validation = getIntent().getExtras();
        charter_id = validation.getString("DATA_VALUE");

        if (validation.containsKey("encoded_image")) {
            imageData = validation.getString("encoded_image");
        }

        // Log.d("AllCharter charter_id", charter_id.toString());
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        getCharterDetail(charter_id);

        cb_hideContactDetails = findViewById(R.id.cb_hideContactDetails);
        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompView.setOnItemClickListener(mAutocompleteClickListener);
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_NEWYORK, null);
        autoCompView.setAdapter(mAdapter);

        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        bcf1 = (Button) findViewById(R.id.chrtbtn);
        chrtbtndlt = (Button) findViewById(R.id.chrtbtndlt);
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
                backPressCall();
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

        chrtbtndlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(_myActivity);
                    progressDialog.setTitle(getString(R.string.loading_title));
                    progressDialog.setMessage(getString(R.string.loading_message));
                }
                progressDialog.show();
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                Call<CommonResponse> userCall = service.deleteapi("Bearer " + token, "charter", charter_id);

                userCall.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        if (response.body() != null) {
                            if (response.body().getStatus() == true) {

                                // oDialog("Product Image updated Successfully.", "Submit", "", true, _myActivity, "BlueMarketProductEditActivity", 0, id);
                                // oDialog("Charter Deleted Successfully.", "Submit", "", true, _myActivity, "BlueMarketCharterActivityNew", 0);
                                String goIntent = "BlueMarketCharterActivityNew";
                                if (BlueMarketCharterPicEditActivity.pass_from.matches("2")) {
                                    goIntent = "MyListingActivity";

                                }
                                oDialog(response.body().getMessage(), "Ok", "", true, _myActivity, goIntent, 0);

                            } else {
                                //Log.d("onResponse", "" + response.body().getStatus());
                                oDialog(response.body().getMessage(), "Ok", "", false, _myActivity, "", 0);
                            }
                        } else {
                            String msg = (String) getApplication().getText(R.string.goeswrong);
                            oDialog(msg, "Ok", "", false, _myActivity, "", 0);
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        bcf1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditCharter();
            }
        });
        bellcount();
    }

    private void setInitializedElements(String chrtname1, String chrtdesc1, String chrtloc1, String chrtfd1, String chrthd1, String chrtbtype1, String chrtbcap1, String hideContact) {

        if (hideContact.equalsIgnoreCase("1"))
            cb_hideContactDetails.setChecked(true);
        else
            cb_hideContactDetails.setChecked(false);
        chrtname.setText(chrtname1);
        chrtdesc.setText(chrtdesc1);
        autoCompView.setText(chrtloc1);
        chrtfd.setText(chrtfd1);
        chrthd.setText(chrthd1);
        chrtbtype.setText(chrtbtype1);
        chrtbcap.setText(chrtbcap1);

    }

    private void getCharterDetail(String charter_id) {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<CharterDetailResponse> userCall = service.chrtDt("Bearer " + token, charter_id);
        userCall.enqueue(new Callback<CharterDetailResponse>() {
            @Override
            public void onResponse(Call<CharterDetailResponse> call, Response<CharterDetailResponse> response) {

                if (response.code() == 200) {
                    if (response.body().getStatus() && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                        setInitializedElements(
                                response.body().getDetails().getName(),
                                response.body().getDetails().getDescription(),
                                response.body().getDetails().getLocation(),
                                response.body().getDetails().getFull_price(),
                                response.body().getDetails().getHalf_price(),
                                response.body().getDetails().getType_of_boat(),
                                response.body().getDetails().getCapacity(),
                                response.body().getDetails().getHide_details()
                        );
                    } else {
                        //Log.d("onResponse", "" + response.body().getStatus());
                    }
                } else {
                    // Log.d("onResponse", "" + response.code());
                }
            }

            @Override
            public void onFailure(Call<CharterDetailResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void EditCharter() {
        name = chrtname.getText().toString().trim();

        String location = autoCompView.getText().toString().trim();

        full_price = chrtfd.getText().toString().trim();

        half_price = chrthd.getText().toString().trim();

        type_of_boat = chrtbtype.getText().toString().trim();

        capacity = chrtbcap.getText().toString().trim();

        description = chrtdesc.getText().toString().trim();

        if (cb_hideContactDetails.isChecked())
            hideContact = "1";
        else
            hideContact = "0";

        if (name.isEmpty())

        {
            oDialog("Enter Charter name ", "Close", "", false, _myActivity, "", 0);
        } else if (location.isEmpty())

        {
            oDialog("Enter location  ", "Close", "", false, _myActivity, "", 0);
        } else if (full_price.isEmpty())

        {
            oDialog("Enter full price ", "Close", "", false, _myActivity, "", 0);
        } else if (half_price.isEmpty())

        {
            oDialog("Enter half price", "Close", "", false, _myActivity, "", 0);
        } else if (type_of_boat.isEmpty())

        {
            oDialog("Enter Type of boat", "Close", "", false, _myActivity, "", 0);
        } else if (capacity.isEmpty())

        {
            oDialog("Enter capacity", "Close", "", false, _myActivity, "", 0);
        } else if (description.isEmpty())

        {
            oDialog("Enter description", "Close", "", false, _myActivity, "", 0);
        } else

        {

            location = autoCompView.getText().toString();
            // Log.d(TAG, "location:" + location + ",lat:" + _lat + ",long:" + _long);
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(_myActivity);
                progressDialog.setTitle(getString(R.string.loading_title));
                progressDialog.setMessage(getString(R.string.loading_message));
            }
            progressDialog.show();

            MultipartBody.Part body = null;

            RequestBody requestBodycharter_id = RequestBody.create(
                    MediaType.parse("multipart/form-data"), charter_id);
            RequestBody requestBodyname = RequestBody.create(
                    MediaType.parse("multipart/form-data"), name);
            RequestBody requestBodylocation = RequestBody.create(
                    MediaType.parse("multipart/form-data"), location);
            RequestBody requestBodyfull_price = RequestBody.create(
                    MediaType.parse("multipart/form-data"), full_price);
            RequestBody requestBodyhalf_price = RequestBody.create(
                    MediaType.parse("multipart/form-data"), half_price);
            RequestBody requestBodytype_of_boat = RequestBody.create(
                    MediaType.parse("multipart/form-data"), type_of_boat);
            RequestBody requestBodycapacity = RequestBody.create(
                    MediaType.parse("multipart/form-data"), capacity);
            RequestBody requestBodydescription = RequestBody.create(
                    MediaType.parse("multipart/form-data"), description);
            RequestBody requestBody_lat = RequestBody.create(
                    MediaType.parse("multipart/form-data"), _lat);
            RequestBody requestBody_long = RequestBody.create(
                    MediaType.parse("multipart/form-data"), _long);
            RequestBody requesthideContact = RequestBody.create(
                    MediaType.parse("multipart/form-data"), hideContact);

            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            if (!imageData.matches("")) {
                fileImage1 = new File(imageData);
            }
            if (fileImage1 != null && fileImage1.length() > 0) {
                RequestBody requestBodyOrderDataFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileImage1);
                body = MultipartBody.Part.createFormData("ch_img", fileImage1.getName().replace(" ", ""), requestBodyOrderDataFile);
            }

            Call<ServerResponsechrt> userCall = service.CharterupdateMultiPart("Bearer " + token,
                    requestBodycharter_id,
                    requestBodyname,
                    requestBodylocation,
                    requestBodyfull_price,
                    requestBodyhalf_price,
                    requestBodytype_of_boat,
                    requestBodycapacity,
                    requestBodydescription,
                    requestBody_lat,
                    requestBody_long,
                    requesthideContact,
                    body);

//            Call<ServerResponsechrt> userCall = service.Charterupdate("Bearer " + token,
//                    charter_id,
//                    name,
//                    location,
//                    full_price,
//                    half_price,
//                    type_of_boat,
//                    capacity,
//                    description,
//                    _lat,
//                    _long,
//                    hideContact);

            userCall.enqueue(new Callback<ServerResponsechrt>() {
                @Override
                public void onResponse(Call<ServerResponsechrt> call, Response<ServerResponsechrt> response) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }

                    if (response.body() != null) {
                        if (response.body().getStatus() == "true") {
                            //    Log.d("onResponse", "" + response.body().getStatus());
                            oDialog(response.body().getMessage(), "Ok", "", true, _myActivity, "MyListingActivity", 0);
                        } else {
                            oDialog(response.body().getMessage(), "Ok", "", false, _myActivity, "", 0);
                        }
                    } else {
                        String msg = (String) getApplication().getText(R.string.goeswrong);
                        oDialog(msg, "Ok", "", false, _myActivity, "", 0);
                    }
                }

                @Override
                public void onFailure(Call<ServerResponsechrt> call, Throwable t) {
                    t.printStackTrace();
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
                }
            });
        }

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
        super.onBackPressed();
        backPressCall();

    }

    void backPressCall() {
        Intent i = new Intent(BoatCharterFormEditActivity.this, BlueMarketCharterPicEditActivity.class);
        i.putExtra("AllCharter", charter_id);
        startActivity(i);
    }
}
