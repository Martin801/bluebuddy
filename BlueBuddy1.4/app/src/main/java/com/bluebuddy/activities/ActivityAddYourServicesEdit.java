package com.bluebuddy.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
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
import com.bluebuddy.Utilities.Utility;
import com.bluebuddy.adapters.PlaceAutocompleteAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.app.AppConfig;
import com.bluebuddy.classes.SelectImage;
import com.bluebuddy.classes.ServerResponsechrt;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.CommonResponse;
import com.bluebuddy.models.ServiceDetail;
import com.bluebuddy.models.ServiceDetailResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.IMG_URL;

public class ActivityAddYourServicesEdit extends BuddyActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final LatLngBounds BOUNDS_NEWYORK = new LatLngBounds(new LatLng(-34.041458, 150.790100), new LatLng(40.719268, -74.006287));
    protected GoogleApiClient mGoogleApiClient;
    String encode_image;
    String token, TAG, _lat, _long;
    String Service_id;
    String lat1, long1, _lat1, _long1;
    String servicedescip, servicename;
    Activity _myActivity;
    File fileImage = null;
    String img_url_From_server = "";
    int pass = 0;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String encoded_image = "";
    private Button btndlt, upload_pic, btnupdt;
    private EditText srvcedesc, service_name;
    private ImageView back, imageView;
    private SelectImage selectImage;
    private SharedPreferences pref;
    private Activity activity;
    private Context context;
    private String passval, profile_Pic = "";
    private boolean _isValid = false;
    private AutoCompleteTextView autoCompView;
    private PlaceAutocompleteAdapter mAdapter;
    private ProgressDialog progressDialog;
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                //   Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }

            final Place place = places.get(0);
            _isValid = true;

            _lat = String.valueOf(place.getLatLng().latitude);
            _long = String.valueOf(place.getLatLng().longitude);

            //  Log.d("LATLONG:", place.getLatLng().toString());
            // Log.i(TAG, "Place details received: " + place.getName());
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

            //  Log.i(TAG, "Autocomplete item selected: " + primaryText);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            //Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };
    private CheckBox cb_hideContactDetails;
    private String hideContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_add_your_services_edit);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.setTitle("ADD YOUR SERVICE");

        _myActivity = this;
        this.activity = this;
        this.context = this;
        TAG = "LocationActivity";
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, 0 /* clientId */, this).addApi(Places.GEO_DATA_API).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");

        Bundle validation = getIntent().getExtras();
        Service_id = validation.getString("AllService");
        /// pass = validation.getString("pass");
        pass = validation.getInt("pass");
        // Log.d("Service_id", Service_id.toString());
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");

        cb_hideContactDetails = findViewById(R.id.cb_hideContactDetails);
        imageView = (ImageView) findViewById(R.id.edt_profile_view);
        back = (ImageView) findViewById(R.id.imgNotification2);
        btnupdt = (Button) findViewById(R.id.btnupdt);
        btndlt = (Button) findViewById(R.id.btndlt);
        upload_pic = (Button) findViewById(R.id.upload_pic);
        srvcedesc = (EditText) findViewById(R.id.srvcedesc);
        service_name = (EditText) findViewById(R.id.service_name);
        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompView.setOnItemClickListener(mAutocompleteClickListener);
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_NEWYORK, null);
        autoCompView.setAdapter(mAdapter);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPressCall();
                /*Intent i = new Intent(ActivityAddYourServicesEdit.this, ServicesOfferedListActivity.class);
                startActivity(i);*/

            }
        });

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

        getServiceDetail(Service_id);

        btndlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(_myActivity);
                    progressDialog.setTitle(getString(R.string.loading_title));
                    progressDialog.setMessage(getString(R.string.loading_message));
                }
                progressDialog.show();
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                Call<CommonResponse> userCall = service.deleteapi("Bearer " + token, "service", Service_id);

                userCall.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }

                        if (response.body().getStatus() == true) {
                            Log.d("onResponse", "" + response.body().getStatus());
                            //  oDialog("Service Deleted Successfully.", "Submit", "", true, _myActivity, "ServicesOfferedListActivity", 0);

                            oDialog("Service Deleted Successfully.", "Submit", "", true, _myActivity, "MyListingActivity", 0);

                        } else {
                            Log.d("onResponse", "" + response.body().getStatus());
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }

                    }
                });
            }
        });


        btnupdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateservice(profile_Pic);
            }
        });


    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(ActivityAddYourServicesEdit.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Utility.RECORD_REQUEST_CODE) {

            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    selectImages();
                } else {
                    // Permission Denied
                    Toast.makeText(ActivityAddYourServicesEdit.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void checkforpermission() {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("READ_EXTERNAL_STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("WRITE_EXTERNAL_STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("CAMERA");
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(ActivityAddYourServicesEdit.this,
                                        permissionsList.toArray(new String[permissionsList.size()]),
                                        Utility.RECORD_REQUEST_CODE);
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(this, permissionsList.toArray(new String[permissionsList.size()]),
                    Utility.RECORD_REQUEST_CODE);
            return;
        }
        selectImages();

    }

    private void selectImages() {
        selectImage = new SelectImage(_myActivity, context);
        selectImage.selectImage();
    }

    public void scndpic(View v) {
        /*selectImage = new SelectImage1(_myActivity, context, imageView);
        selectImage.selectImage();*/
        checkforpermission();
    }


    /*private String decodeImageFromUrl(String img) {
        Bitmap bm = null;
        String image = "";
        if (!img.equals("")) {
            try {
                URL url = new URL(IMG_URL + img);
                bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                image = "data:image/jpeg;base64," + Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                Log.d("decodeimg", image);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NetworkOnMainThreadException e) {
                e.printStackTrace();
            }
        }
        return image;
    }*/

    private void getServiceDetail(String service_id) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(_myActivity);
            progressDialog.setTitle(getString(R.string.loading_title));
            progressDialog.setMessage(getString(R.string.loading_message));
        }
        progressDialog.show();
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<ServiceDetailResponse> userCall = service.serviceDt("Bearer " + token, service_id);
        userCall.enqueue(new Callback<ServiceDetailResponse>() {
            @Override
            public void onResponse(Call<ServiceDetailResponse> call, Response<ServiceDetailResponse> response) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                if (response.code() == 200) {
                    if (response.body().getStatus() == true) {
                        ServiceDetail details = response.body().getDetails();

                        try {
                            if (details.getHide_details().equalsIgnoreCase("1")) {
                                cb_hideContactDetails.setChecked(true);
                            } else
                                cb_hideContactDetails.setChecked(false);
                        } catch (Exception e) {
                            e.getStackTrace();
                        }

                        service_name.setText(details.getService_type());
                        autoCompView.setText(details.getLocation());
                        srvcedesc.setText(details.getDescription());
                        String imge = details.getImage();
                        Log.d("image5", imge);

                        Glide.with(ActivityAddYourServicesEdit.this).asBitmap().load(IMG_URL + details.getImage()).into(new BitmapImageViewTarget(imageView) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(ActivityAddYourServicesEdit.this.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                imageView.setImageDrawable(circularBitmapDrawable);
                            }
                        });
                        _lat = details.getLatitude();
                        // Log.d("lat55", _lat);
                        _long = details.getLongitude();
                        //Log.d("long55", _long);

                    } else {
                        String msg = (String) getApplication().getText(R.string.goeswrong);
                        oDialog(msg, "Ok", "", false, _myActivity, "", 0);
                    }
                } else {
                    String msg = (String) getApplication().getText(R.string.goeswrong);
                    oDialog(msg, "Ok", "", false, _myActivity, "", 0);
                    //  Log.d("onResponse", "" + response.code());
                }
            }

            @Override
            public void onFailure(Call<ServiceDetailResponse> call, Throwable t) {
                t.printStackTrace();
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void updateservice(final String profile_Pic) {

        String servicename = service_name.getText().toString();
        String servicedescip = srvcedesc.getText().toString();
        String location = autoCompView.getText().toString().trim();
        if(cb_hideContactDetails.isChecked()){
            hideContact = "1";
        } else {
            hideContact = "0";
        }

        if (servicename.isEmpty()) {
            oDialog("Enter Service Type", "Close", "", false, _myActivity, "", 0);
        } else if (location.isEmpty()) {
            oDialog("Enter location ", "Close", "", false, _myActivity, "", 0);
        } else if (servicedescip.isEmpty()) {
            oDialog("Enter Service Description ", "Close", "", false, _myActivity, "", 0);
        } else {

            location = autoCompView.getText().toString();
            //   Log.d(TAG, "location:" + location + ",lat:" + _lat + ",long:" + _long);
            double _lat3 = Double.parseDouble(_lat);
            double _long3 = Double.parseDouble(_long);
            _lat1 = String.format("%.6f", _lat3);
            _long1 = String.format("%.7f", _long3);
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(_myActivity);
                progressDialog.setTitle(getString(R.string.loading_title));
                progressDialog.setMessage(getString(R.string.loading_message));
            }
            progressDialog.show();
            MultipartBody.Part body = null;
            RequestBody requestBodyId = RequestBody.create(
                    MediaType.parse("multipart/form-data"), Service_id);
            RequestBody requestBodyservicename = RequestBody.create(
                    MediaType.parse("multipart/form-data"), servicename);
            RequestBody requestBodylocation = RequestBody.create(
                    MediaType.parse("multipart/form-data"), location);
            RequestBody requestBodyservicedescip = RequestBody.create(
                    MediaType.parse("multipart/form-data"), servicedescip);
            RequestBody requestBody_lat1 = RequestBody.create(
                    MediaType.parse("multipart/form-data"), _lat1);
            RequestBody requestBodyHideContact = RequestBody.create(
                    MediaType.parse("multipart/form-data"), hideContact);
            RequestBody requestBody_long1 = RequestBody.create(
                    MediaType.parse("multipart/form-data"), _long1);


            if (fileImage != null && fileImage.length() > 1) {
                RequestBody requestBodyOrderDataFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileImage);
                body = MultipartBody.Part.createFormData("sv_img", fileImage.getName().replace(" ", ""), requestBodyOrderDataFile);
            }
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            //Call<ServerResponsechrt> userCall = service.serviceupdate("Bearer " + token, Service_id, servicename, location, servicedescip, profile_Pic, _lat1, _long1);
            Call<ServerResponsechrt> userCall = service.serviceupdateMultipart("Bearer " + token,
                    requestBodyId,
                    requestBodyservicename,
                    requestBodylocation,
                    requestBodyservicedescip,
                    requestBody_lat1,
                    requestBody_long1,
                    requestBodyHideContact,
                    body);
            userCall.enqueue(new Callback<ServerResponsechrt>() {
                @Override
                public void onResponse(Call<ServerResponsechrt> call, Response<ServerResponsechrt> response) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (response.body() != null && response.body().getStatus() == "true") {
                        final String rc2 = "MyListingActivity";
                        //  final String rc2 = "ServicesOfferedListActivity";
                        oDialog(response.body().getMessage(), "Ok", "", true, _myActivity, rc2, 0);
                    } else {
                        //Log.d("onResponse", "" + response.body().getStatus());
                        String msg = (String) getApplication().getText(R.string.goeswrong);
                        oDialog(msg, "Ok", "", false, _myActivity, "", 0);

                    }
                }

                @Override
                public void onFailure(Call<ServerResponsechrt> call, Throwable t) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Log.d("onFailure", t.toString());
                }
            });

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                String imagepath = selectImage.getRealPathFromURI(data.getData());
                fileImage = null;
                if (imagepath != null && imagepath.trim().length() > AppConfig.IMG_LENTH_CHECK) {

                    fileImage = new File(imagepath);
                    Bitmap bm = null;
                    try {
                        bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        imageView.setImageBitmap(bm);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getText(R.string.imgnotvalid), Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (requestCode == REQUEST_CAMERA) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                fileImage = selectImage.SaveImage(photo);
                imageView.setImageBitmap(photo);
            }

        }
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //  Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this, "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
       /* Intent i = new Intent(ActivityAddYourServicesEdit.this, ServicesOfferedListActivity.class);
        startActivity(i);*/
        backPressCall();
    }

    void backPressCall() {

        if (pass == 2) {
            Intent i = new Intent(ActivityAddYourServicesEdit.this, MyListingActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(ActivityAddYourServicesEdit.this, MyListingActivity.class);
            startActivity(i);
        }
        finish();
    }

}

