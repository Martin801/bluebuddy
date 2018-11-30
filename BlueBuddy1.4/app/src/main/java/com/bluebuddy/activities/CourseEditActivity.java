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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.bluebuddy.classes.ServerResponsechrt;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.CommonResponse;
import com.bluebuddy.models.CourseDetailResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class CourseEditActivity extends BuddyActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private static final LatLngBounds BOUNDS_NEWYORK = new LatLngBounds(new LatLng(-34.041458, 150.790100), new LatLng(40.719268, -74.006287));
    protected GoogleApiClient mGoogleApiClient;
    ImageView back;
    private Spinner spacttype3;
    private EditText certlvlid1, agnname, agnurl, codur, codesc, coprice, coloc;
    private Button bmcoursecontid, bmcoursedlt;
    private LinearLayout newlvl1;
    private View certlvlid1_view;
    private MyTextView bell_counter;
    private String token, TAG, _lat, _long, bellcounter, co_cat, Othertxt;
    private SharedPreferences pref;
    private ProgressDialog mProgressDialog;
    private String[] courses_menu = {"Scuba Diving", "Freediving", "Others"};
    private String spinneritem, otherCourseType, name, url, duration, description, price, location, course_id, category = "";
    private CustomAdapter customAdapter;
    private Activity _myActivity;
    private boolean _isValid = false;
    private AutoCompleteTextView autoCompView;
    private PlaceAutocompleteAdapter mAdapter;
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

            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_course_edit);
        super.onCreate(savedInstanceState);
        super.initialize();
        // super.loader();
        super.notice();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;

        _myActivity = this;

        TAG = "LocationActivity";
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, 0 /* clientId */, this).addApi(Places.GEO_DATA_API).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle b = getIntent().getExtras();
        course_id = b.getString("DATA_VALUE");
        category = b.getString("encoded_image");
        /*Log.d("COURSE_ID", course_id.toString());
        Log.d("category", category.toString());*/

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");

        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompView.setOnItemClickListener(mAutocompleteClickListener);
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_NEWYORK, null);
        autoCompView.setAdapter(mAdapter);

        initializeElements();
        getCourseDetail(course_id);
        bellcount();
    }

    private void initializeElements() {
        customAdapter = new CustomAdapter(getApplicationContext(), courses_menu);

        spacttype3 = (Spinner) findViewById(R.id.spacttype3);
        spacttype3.setOnItemSelectedListener(this);
        spacttype3.setAdapter(customAdapter);
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        certlvlid1 = (EditText) findViewById(R.id.certlvlid1);
        agnname = (EditText) findViewById(R.id.agnname);
        agnurl = (EditText) findViewById(R.id.agnurl);
        codur = (EditText) findViewById(R.id.codur);
        codesc = (EditText) findViewById(R.id.codesc);
        coprice = (EditText) findViewById(R.id.coprice);
        bmcoursecontid = (Button) findViewById(R.id.bmcoursecontid);
        bmcoursedlt = (Button) findViewById(R.id.bmcoursedlt);
        bmcoursecontid.setOnClickListener(this);

        newlvl1 = (LinearLayout) findViewById(R.id.newlvl1);
        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(BlueMarketCoursePicEditActivity.this, BlueMarketActivity2.class);
                startActivity(i);*/
                backPressCall();
            }
        });
        certlvlid1_view = (View) findViewById(R.id.certlvlid1_view);
        bmcoursedlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                Call<CommonResponse> userCall = service.deleteapi("Bearer " + token, "course", course_id);

                userCall.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        if (response.body().getStatus() == true) {
                            //Log.d("onResponse", "" + response.body().getStatus());
                            // oDialog("Product Image updated Successfully.", "Submit", "", true, _myActivity, "BlueMarketProductEditActivity", 0, id);
                            oDialog("Course Deleted Successfully.", "Ok", "", true, _myActivity, "MyListingActivity", 0);

                        } else {
                            Log.d("onResponse", "" + response.body().getStatus());
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {

                    }
                });
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
    }

    private void setInitializedElements(String courseType, String name, String url, String duration, String description, String price, String location) {
        spacttype3.setSelection(customAdapter.getIndex(courseType));

        agnname.setText(name);
        agnurl.setText(url);
        codur.setText(duration);
        codesc.setText(description);
        coprice.setText(price);
        autoCompView.setText(location);
    }

    private void getCourseDetail(String course_id) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(_myActivity);
            progressDialog.setTitle(getString(R.string.loading_title));
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.loading_message));
        }
        progressDialog.show();
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<CourseDetailResponse> userCall = service.courseDt("Bearer " + token, course_id);

        userCall.enqueue(new Callback<CourseDetailResponse>() {
            @Override
            public void onResponse(Call<CourseDetailResponse> call, Response<CourseDetailResponse> response) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                if (response.code() == 200) {

                    if (response.body().getStatus()) {
                        setInitializedElements(
                                response.body().getDetails().getCategory(),
                                response.body().getDetails().getAgency_name(),
                                response.body().getDetails().getAgency_url(),
                                response.body().getDetails().getDuration(),
                                response.body().getDetails().getDescription(),
                                response.body().getDetails().getPrice(),
                                response.body().getDetails().getLocation()
                        );
                    } else {
                        // Log.d("onResponse", "" + response.body().getStatus());
                        String msg = (String) getApplication().getText(R.string.goeswrong);
                        oDialog(msg, "Ok", "", false, _myActivity, "", 0);
                    }
                } else {
                    String msg = (String) getApplication().getText(R.string.goeswrong);
                    oDialog(msg, "Ok", "", false, _myActivity, "", 0);
                }
            }

            @Override
            public void onFailure(Call<CourseDetailResponse> call, Throwable t) {
                t.printStackTrace();
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void courseUpdate() {
        spinneritem = spacttype3.getSelectedItem().toString();
        otherCourseType = certlvlid1.getText().toString().trim();
        name = agnname.getText().toString().trim();
        url = agnurl.getText().toString().trim();
        duration = codur.getText().toString().trim();
        description = codesc.getText().toString().trim();
        price = coprice.getText().toString().trim();
        String location = autoCompView.getText().toString().trim();

        /*if (String.valueOf(spacttype3.getSelectedItem()) == "Select Course Type") {
            oDialog("Enter the Course", "Close", "", false, _myActivity, "", 0);
        } else*/
        if (String.valueOf(spacttype3.getSelectedItem()) == "Others" && certlvlid1.getText().toString().isEmpty()) {
            oDialog("Please enter your course name", "Close", "", false, _myActivity, "", 0);
        } else if (name.isEmpty()) {
            oDialog("Enter Agency name ", "Close", "", false, _myActivity, "", 0);
        } else if (duration.isEmpty()) {
            oDialog("Enter Course duration ", "Close", "", false, _myActivity, "", 0);
        } else if (description.isEmpty()) {
            oDialog("Enter course description ", "Close", "", false, _myActivity, "", 0);
        } else if (price.isEmpty()) {
            oDialog("Enter course price", "Close", "", false, _myActivity, "", 0);
        } else if (location.isEmpty()) {
            oDialog("Enter Location", "Close", "", false, _myActivity, "", 0);
        } else {
            if (String.valueOf(spacttype3.getSelectedItem()) == "Others") {
                //TODAY
                co_cat = certlvlid1.getText().toString().trim();
                // Log.d("Othertxt1", co_cat);
                Othertxt = "Others:" + co_cat;
                // Log.d("Othertxt2", Othertxt);
                co_cat = Othertxt;
                //  Log.d("cat co", co_cat);
            } else {
                co_cat = spacttype3.getSelectedItem().toString().trim();
                // Log.d("co cat", co_cat);
            }
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(_myActivity);
                progressDialog.setTitle(getString(R.string.loading_title));
                progressDialog.setCancelable(false);
                progressDialog.setMessage(getString(R.string.loading_message));
            }
            progressDialog.show();
            String courseType = spinneritem.equals("") ? otherCourseType : spinneritem;

            location = autoCompView.getText().toString();
            Log.d(TAG, "location:" + location + ",lat:" + _lat + ",long:" + _long);

            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<ServerResponsechrt> userCall = service.coursesupdate(
                    "Bearer " + token,
                    course_id,
                    courseType,
                    name,
                    url,
                    duration,
                    description,
                    price,
                    location,
                    _lat,
                    _long,
                    ""
            );

            userCall.enqueue(new Callback<ServerResponsechrt>() {
                @Override
                public void onResponse(Call<ServerResponsechrt> call, Response<ServerResponsechrt> response) {
                    // Log.d("onResponse", "" + response.body().getMessage());
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (response.body() != null && response.body().getStatus() == "true") {
                        // Log.d("onResponse", "" + response.body().getStatus());
                        String inTent = "MyListingActivity";
                        if (category != null && !category.matches("")) {
                            inTent = "BlueMarketCourseActivityNew";
                        }
                        oDialogReturn(response.body().getMessage(), "Ok", "", true, _myActivity, inTent, 0, category);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.bmcoursecontid:
                courseUpdate();
                break;
        }
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
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void oDialogReturn(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, final String returnParam) {
        super.openDialogReturn(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, returnParam);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backPressCall();
    }

    void backPressCall() {
        if (category == null || category.matches("")) {
            Intent i = new Intent(CourseEditActivity.this, BlueMarketCoursePicEditActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(CourseEditActivity.this, BlueMarketCoursePicEditActivity.class);
            i.putExtra("category", category);
            startActivity(i);
        }
    }
}
