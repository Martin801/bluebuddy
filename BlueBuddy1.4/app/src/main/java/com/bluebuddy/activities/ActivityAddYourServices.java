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
import android.support.v7.widget.CardView;
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
import com.bluebuddy.Utilities.Utility;
import com.bluebuddy.adapters.PlaceAutocompleteAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.app.AppConfig;
import com.bluebuddy.classes.SelectImage;
import com.bluebuddy.classes.ServerResponsechrt;
import com.bluebuddy.interfaces.ApiInterface;

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

public class ActivityAddYourServices extends BuddyActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final LatLngBounds BOUNDS_NEWYORK = new LatLngBounds(new LatLng(-34.041458, 150.790100), new LatLng(40.719268, -74.006287));
    private static final String TAG = "AddYourServices";
    protected GoogleApiClient mGoogleApiClient;
    String encode_image;
    ImageView back;
    String servicedescip, servicename;
    Activity _myActivity;
    File fileImage = null;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String encoded_image = "";
    private Button nxtbt, upload_pic;
    private EditText srvcedesc, service_name;
    private SelectImage selectImage;
    private String token, _lat, _long, backpass, ract;
    private SharedPreferences pref;
    private Activity activity;
    private Context context;
    private ImageView imageView;
    private CardView view2;
    private boolean _isValid = false;
    private AutoCompleteTextView autoCompView;
    private PlaceAutocompleteAdapter mAdapter;
    private ProgressDialog progressDialog;
    private CheckBox cb_hideContactDetails;

    private String hideContact = "0";

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                //  Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                _isValid = false;
                return;
            }

            // Get the Place object from the buffer.
            final Place place = places.get(0);
            _isValid = true;
            _lat = String.valueOf(place.getLatLng().latitude);
            _long = String.valueOf(place.getLatLng().longitude);

            //  Log.d("LATLONG:", place.getLatLng().toString());
            //  Log.i(TAG, "Place details received: " + place.getName());
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

            //Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.setLayout(R.layout.activity_add_your_services);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.setTitle("ADD YOUR SERVICE");

        _myActivity = this;
        this.activity = this;
        this.context = this;

        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, 0 /* clientId */, this).addApi(Places.GEO_DATA_API).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pref = super.getPref();
        Bundle bundle = getIntent().getExtras();
        backpass = bundle.getString("bmserv");
        String Bs = backpass.toString().trim();
        if (Bs.equals("1")) {
            ract = "BlueMarketServiceActivityNew";
        }
        //  category3 = bundle.getString("category2");
        else if (Bs.equals("2")) {
            ract = "MyListingActivity";
        } else if (Bs.equals("0")) {
            ract = "MyListingActivity";
        }
        token = pref.getString(ACCESS_TOKEN, "");

        cb_hideContactDetails = (CheckBox) findViewById(R.id.cb_hideContactDetails);

        view2 = (CardView) findViewById(R.id.view2);
        back = (ImageView) findViewById(R.id.imgNotification2);
        imageView = (ImageView) findViewById(R.id.imgcharpic);
        nxtbt = (Button) findViewById(R.id.nxtbt);
        upload_pic = (Button) findViewById(R.id.upload_pic);
        srvcedesc = (EditText) findViewById(R.id.srvcedesc);
        service_name = (EditText) findViewById(R.id.service_name);
        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompView.setOnItemClickListener(mAutocompleteClickListener);
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_NEWYORK, null);
        autoCompView.setAdapter(mAdapter);

        upload_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*selectImage = new SelectImage1(activity, context, imageView);
                selectImage.selectImage();*/
                checkforpermission();

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent i = new Intent(ActivityAddYourServices.this, BlueMarketServiceActivityNew.class);
                startActivity(i);*/
//                Intent i = null;
//                try {
//                    i = new Intent(ActivityAddYourServices.this, Class.forName("com.bluebuddy.activities." + ract));
//                    startActivity(i);
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
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
        /* cp6nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_isValid)
                    cp6nxt();
                else
                    oDialog("Select your location!", "Close", "", false, _myActivity, "", 0);
            }
        });*/

        nxtbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servicename = service_name.getText().toString();
                servicedescip = srvcedesc.getText().toString();
                String location = autoCompView.getText().toString().trim();

                // encode_image = encoded_image.toString().trim();
                //  Log.d("test", encode_image);

                if (cb_hideContactDetails.isChecked()) {
                    hideContact = "1";
                } else {
                    hideContact = "0";
                }

                if (servicename.isEmpty()) {
                    oDialog("Enter Service Type", "Close", "", false, _myActivity, "", 0);
                } else if (location.isEmpty() || !_isValid) {
                    oDialog("Select your location ", "Close", "", false, _myActivity, "", 0);
                } else if (servicedescip.isEmpty()) {
                    oDialog("Enter Service Description ", "Close", "", false, _myActivity, "", 0);
                } else if (fileImage == null || (fileImage.length() < AppConfig.IMG_LENTH_CHECK)) {
                    oDialog("Please Upload Image ", "Close", "", false, _myActivity, "", 0);
                } else {
                    serviceCallForMarket();
                    /*location = autoCompView.getText().toString();
                    double _lat3 = Double.parseDouble(_lat);
                    double _long3 = Double.parseDouble(_long);
                    _lat1 = String.format("%.6f", _lat3);
                    _long1 = String.format("%.6f", _long3);

                    ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                    Call<ServerResponsechrt> userCall = service.serviceinst("Bearer " + token, "post", servicename, location, servicedescip, "data:image/jpeg;base64," + encode_image, _lat1, _long1);
                    userCall.enqueue(new Callback<ServerResponsechrt>() {
                        @Override
                        public void onResponse(Call<ServerResponsechrt> call, Response<ServerResponsechrt> response) {

                            Log.d("onResponse", "" + response.body().getMessage());

                            if (response.body().getStatus() == "true") {

                                final String rc2 = "ServicesOfferedListActivity";
                                oDialog("Thanks for Listing with us.", "Close", "", true, _myActivity, rc2, 0);
                            } else {
                                Log.d("onResponse", "" + response.body().getStatus());

                            }
                        }

                        @Override
                        public void onFailure(Call<ServerResponsechrt> call, Throwable t) {

                            Log.d("onFailure", t.toString());
                        }
                    });*/

                }

            }
        });
    }

    private void serviceCallForMarket() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(_myActivity);
            progressDialog.setTitle(getString(R.string.loading_title));
            progressDialog.setMessage(getString(R.string.loading_message));
        }
        progressDialog.show();
        String location = autoCompView.getText().toString().trim();
        //service.serviceinst("Bearer " + token, "post", servicename, location, servicedescip, _lat1, _long1);
        MultipartBody.Part body = null;
        RequestBody requestBodyCname = RequestBody.create(
                MediaType.parse("multipart/form-data"), servicename);
        RequestBody requestBodyCdesc = RequestBody.create(
                MediaType.parse("multipart/form-data"), location);
        RequestBody requestBodyCloc = RequestBody.create(
                MediaType.parse("multipart/form-data"), servicedescip);
        RequestBody requestBodyCfp = RequestBody.create(
                MediaType.parse("multipart/form-data"), _lat);
        RequestBody requestBodyHideContact = RequestBody.create(
                MediaType.parse("multipart/form-data"), hideContact);
        RequestBody requestBodyChp = RequestBody.create(
                MediaType.parse("multipart/form-data"), _long);

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        RequestBody requestBodyOrderDataFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileImage);
        if (fileImage != null && fileImage.length() > 0) {

            body = MultipartBody.Part.createFormData("sv_img", fileImage.getName().replace(" ", ""), requestBodyOrderDataFile);
        }
        Call<ServerResponsechrt> userCall = service.serviceinstMultipart("Bearer " + token,
                requestBodyCname,
                requestBodyCdesc,
                requestBodyCloc,
                requestBodyCfp,
                requestBodyChp,
                requestBodyHideContact,
                body);
        userCall.enqueue(new Callback<ServerResponsechrt>() {
            @Override
            public void onResponse(Call<ServerResponsechrt> call, Response<ServerResponsechrt> response) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                if (response.body().getStatus() != null) {
                    if (response.body().getStatus() == "true") {

                           /* final String rc2 = "MyListingActivity";
                            oDialog(response.body().getMessage(), "Close", "", true, activity, rc2, 0);*/
                        final String rc2 = "MyListingActivity";
                        oDialog(response.body().getMessage(), "Close", "", true, _myActivity, rc2, 0);

                    } else {

                        String msg = (String) getApplication().getText(R.string.goeswrong);
                        oDialog(response.body().getMessage(), "Ok", "", false, activity, "", 0);
                    }
                } else {
                    String msg = (String) getApplication().getText(R.string.goeswrong);
                    oDialog(msg, "Ok", "", false, activity, "", 0);
                }
            }

            @Override
            public void onFailure(Call<ServerResponsechrt> call, Throwable t) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

       /* Intent i = new Intent(ActivityAddYourServices.this, BlueMarketServiceActivityNew.class);
        startActivity(i);*/
//        Intent i = null;
//        try {
//            i = new Intent(ActivityAddYourServices.this, Class.forName("com.bluebuddy.activities." + ract));
//            startActivity(i);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        finish();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                String imagepath = selectImage.getRealPathFromURI(data.getData());
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
                if (fileImage != null || fileImage.length() > AppConfig.IMG_LENTH_CHECK) {
                    view2.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    view2.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                }
                //
            } else if (requestCode == REQUEST_CAMERA) {
                // encoded_image = selectImage.onCaptureImageResultSqaure11(data, "");
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                fileImage = selectImage.SaveImage(photo);
                // fileImage=selectImage.SaveImage(photo);
                imageView.setImageBitmap(photo);

                if (fileImage != null || fileImage.length() > AppConfig.IMG_LENTH_CHECK) {
                    view2.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    view2.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                }

            }

        }
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
        new android.app.AlertDialog.Builder(ActivityAddYourServices.this)
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
                    Toast.makeText(ActivityAddYourServices.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
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
                                ActivityCompat.requestPermissions(ActivityAddYourServices.this,
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

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, String data) {
        super.openDialog6(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, data);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this, "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
    }


}

