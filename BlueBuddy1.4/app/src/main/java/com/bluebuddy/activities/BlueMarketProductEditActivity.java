package com.bluebuddy.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import com.bluebuddy.R;
import com.bluebuddy.adapters.CustomAdapter;
import com.bluebuddy.adapters.PlaceAutocompleteAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.classes.ServerResponsechrt;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.CommonResponse;
import com.bluebuddy.models.ProductDetail;
import com.bluebuddy.models.ProductDetailResponse;
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

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class BlueMarketProductEditActivity extends BuddyActivity implements AdapterView.OnItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "BMProductEditActivity";
    private static final LatLngBounds BOUNDS_NEWYORK = new LatLngBounds(new LatLng(-34.041458, 150.790100), new LatLng(40.719268, -74.006287));
    protected GoogleApiClient mGoogleApiClient;
    private Button btn1;
    private Activity _myActivity;
    // private String[] product_menu = {"Scuba Diving", "Freediving", "Fishing", "SpearFishing", "Boats", "Others", "Select Product Type"};
    private String[] product_menu = {"Scuba Diving", "Freediving", "Fishing", "Spearfishing", "Boats", "Others"};
    private ImageView back;
    private Spinner spinner4;
    private MyTextView bell_counter;
    private LinearLayout newlvl1;
    private EditText certlvlid1, bmpname, bmpdesc, bmpprice;
    private Button minus_level1, bmprodlt;
    private View certlvlid1_view;
    private String token, productId, _lat, _long, imageData;
    private SharedPreferences pref;
    private String encode_image1, encode_image2, encode_image3, bellcounter, encode_image4 = "";
    private String encoded_imagelist[];
    /*  private ProgressDialog mProgressDialog;*/
    private ProgressDialog progressDialog;
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

    private File fileImage1, fileImage2, fileImage3, fileImage4;
    private String bmpname1;
    private String bmpdesc1;
    private String bmpprice1;
    private String location;
    private String co_cat;
    private String Othertxt;
    private String spinneritem = "";
    private CheckBox cb_hideContactDetails;
    private String hideContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.setLayout(R.layout.activity_blue_market_product2);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.notice();
        super.setTitle("BLUE MARKET PRODUCT");
        // super.loader();

        Bundle validation = getIntent().getExtras();

        productId = validation.getString("encoded_image");

        if (validation.containsKey("DATA_VALUE")) {
            imageData = validation.getString("DATA_VALUE");
            if (!imageData.matches("")) {
                if (imageData.trim().contains(",")) {
                    encoded_imagelist = imageData.trim().split(",");
                } else {
                    encoded_imagelist = new String[1];
                    encoded_imagelist[0] = imageData;
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

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");

        cb_hideContactDetails = findViewById(R.id.cb_hideContactDetails);
        certlvlid1 = (EditText) findViewById(R.id.certlvlid1);
        minus_level1 = (Button) findViewById(R.id.minus_level1);
        bmprodlt = (Button) findViewById(R.id.bmprodlt);
        newlvl1 = (LinearLayout) findViewById(R.id.newlvl1);
        certlvlid1_view = (View) findViewById(R.id.certlvlid1_view);
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);

        // Delete Product.
        bmprodlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                Call<CommonResponse> userCall = service.deleteapi("Bearer " + token, "product", productId);

                userCall.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        if (response.body().getStatus() == true) {
                            Log.d("onResponse", "" + response.body().getStatus());
//                            oDialog("Product Deleted Successfully.", "Submit", "", true, _myActivity, "BlueMarketActivity4", 0);
                            oDialog("Product Deleted Successfully.", "Submit", "", false, _myActivity, "BlueMarketActivity4", 0);
                            Intent intent = new Intent(BlueMarketProductEditActivity.this, MyListingActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
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

        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent i = new Intent(BlueMarketProductEditActivity.this, Categories_bluemarketActivity.class);
                startActivity(i);*/
                onBackPressed();
            }
        });

        TextView IDtxt1 = (TextView) findViewById(R.id.idtxt1);
        TextView IDtxt2 = (TextView) findViewById(R.id.idtxt2);
        TextView IDtxt3 = (TextView) findViewById(R.id.idtxt3);
        TextView IDtxt4 = (TextView) findViewById(R.id.idtxt4);
        TextView IDtxt5 = (TextView) findViewById(R.id.idtxt5);
        TextView IDtxt6 = (TextView) findViewById(R.id.idtxt6);

        this._myActivity = this;

        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, 0 /* clientId */, this).addApi(Places.GEO_DATA_API).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");

        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompView.setOnItemClickListener(mAutocompleteClickListener);
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_NEWYORK, null);
        autoCompView.setAdapter(mAdapter);

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

        getProductDetail(productId);

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

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bmpname1 = bmpname.getText().toString().trim();
//                String spinneritem = spinner4.getSelectedItem().toString();
                bmpdesc1 = bmpdesc.getText().toString().trim();
                bmpprice1 = bmpprice.getText().toString().trim();
                location = autoCompView.getText().toString().trim();

                if (cb_hideContactDetails.isChecked())
                    hideContact = "1";
                else
                    hideContact = "0";

//                location = autoCompView.getText().toString();
                //  Log.d(TAG, "location:" + location + ",lat:" + _lat + ",long:" + _long);

                if (bmpname1.isEmpty()) {
                    oDialog("Enter Product name", "Close", "", false, _myActivity, "", 0);
                }
                /*if (String.valueOf(spinner4.getSelectedItem()) == "Select Product Type") {
                    oDialog("Select Product Category", "Close", "", false, _myActivity, "", 0);
                } else*/
                if (String.valueOf(spinner4.getSelectedItem()) == "Others" && certlvlid1.getText().toString().isEmpty()) {
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
                    serviceCallForMarket();
                }
//                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
//
//                Call<ServerResponsechrt> userCall = service.Productdetailupdate("Bearer " + token, productId, bmpname1, spinneritem, bmpdesc1, bmpprice1, location, _lat, _long);
//                userCall.enqueue(new Callback<ServerResponsechrt>() {
//                    @Override
//                    public void onResponse(Call<ServerResponsechrt> call, Response<ServerResponsechrt> response) {
//                        if (response.body().getStatus() == "true" /*&& mProgressDialog.isShowing()*/) {
//
//                            final String rc2 = "MyListingActivity";
//
//                            Log.d(TAG, "onResponse: " + response.body().getMessage());
//                            oDialog(response.body().getMessage(), "Ok", "", true, _myActivity, rc2, 0, imageData);
//
//                        } else {
//
//                            Log.d("onResponse", "" + response.body().getStatus());
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ServerResponsechrt> call, Throwable t) {
//                        Log.d("onFailure", t.toString());
//                    }
//                });
            }
            //  }
        });

        bellcount();
    }

    private void serviceCallForMarket() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(_myActivity);
            progressDialog.setTitle(getString(R.string.loading_title));
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.loading_message));
        }
        progressDialog.show();

        MultipartBody.Part body = null;
        MultipartBody.Part body1 = null;
        MultipartBody.Part body2 = null;
        MultipartBody.Part body3 = null;

        //imgP1, imgP2, imgP3, imgP4, nameP, typeP, descP, prp,locP,latP,longP
        RequestBody requestBodyPId = RequestBody.create(
                MediaType.parse("multipart/form-data"), productId);
        RequestBody requestBodyPname = RequestBody.create(
                MediaType.parse("multipart/form-data"), bmpname1);
        RequestBody requestBodyPCat = RequestBody.create(
                MediaType.parse("multipart/form-data"), spinneritem);
        RequestBody requestBodyPDesc = RequestBody.create(
                MediaType.parse("multipart/form-data"), bmpdesc1);
        RequestBody requestBodyPPrice = RequestBody.create(
                MediaType.parse("multipart/form-data"), bmpprice1);
        RequestBody requestBodyPLocation = RequestBody.create(
                MediaType.parse("multipart/form-data"), location);
        RequestBody requestBodyPLat = RequestBody.create(
                MediaType.parse("multipart/form-data"), _lat);
        RequestBody requestBodyPLong = RequestBody.create(
                MediaType.parse("multipart/form-data"), _long);
        RequestBody requestBodyHideContact = RequestBody.create(
                MediaType.parse("multipart/form-data"), hideContact);

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        if (!encode_image1.matches("")) {
            fileImage1 = new File(encode_image1);
        }
        if (!encode_image2.matches("")) {
            fileImage2 = new File(encode_image2);
        }
        if (!encode_image3.matches("")) {
            fileImage3 = new File(encode_image3);
        }
        if (!encode_image4.matches("")) {
            fileImage4 = new File(encode_image4);
        }
        if (fileImage1 != null && fileImage1.length() > 0) {
            RequestBody requestBodyOrderDataFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileImage1);
            body = MultipartBody.Part.createFormData("pr_img1", fileImage1.getName().replace(" ", ""), requestBodyOrderDataFile);
        }
        if (fileImage2 != null && fileImage2.length() > 0) {
            RequestBody requestBodyOrderDataFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileImage2);
            body1 = MultipartBody.Part.createFormData("pr_img2", fileImage2.getName().replace(" ", ""), requestBodyOrderDataFile);
        }
        if (fileImage3 != null && fileImage3.length() > 0) {
            RequestBody requestBodyOrderDataFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileImage3);
            body2 = MultipartBody.Part.createFormData("pr_img3", fileImage3.getName().replace(" ", ""), requestBodyOrderDataFile);
        }
        if (fileImage4 != null && fileImage4.length() > 0) {
            RequestBody requestBodyOrderDataFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileImage4);
            body3 = MultipartBody.Part.createFormData("pr_img4", fileImage4.getName().replace(" ", ""), requestBodyOrderDataFile);
        }
        Call<ServerResponsechrt> userCall = service.ProductimgupdateMultipart("Bearer " + token,
                requestBodyPId,
                requestBodyPname,
                requestBodyPCat,
                requestBodyPDesc,
                requestBodyPPrice,
                requestBodyPLocation,
                requestBodyPLat,
                requestBodyPLong,
                requestBodyHideContact,
                body, body1, body2, body3);
        userCall.enqueue(new Callback<ServerResponsechrt>() {
            @Override
            public void onResponse(Call<ServerResponsechrt> call, Response<ServerResponsechrt> response) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                if (response.body().getStatus() != null) {
                    if (response.body().getStatus() == "true") {

//                        oDialog(response.body().getMessage(), "Ok", "", false, _myActivity, "", 0);

                        oDialog(response.body().getMessage());

                    } else {
                        String msg = (String) getApplication().getText(R.string.goeswrong);
                        oDialog(response.body().getMessage(), "Ok", "", false, _myActivity, "", 0);
                    }
                } else {
                    String msg = (String) getApplication().getText(R.string.goeswrong);
                    oDialog(msg, "Ok", "", false, _myActivity, "", 0);
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

    public void oDialog(String msg) {
        final Dialog _dialog = new Dialog(this);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setCancelable(false);
        _dialog.setCanceledOnTouchOutside(true);

        _dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        _dialog.setContentView(R.layout.custom_popup_layout);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        int pxHeight = (int) (outMetrics.heightPixels / getResources().getDisplayMetrics().density) / 3;

        WindowManager.LayoutParams wmlp = _dialog.getWindow().getAttributes();

        wmlp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;

        wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmlp.y = pxHeight;

        TextView tvSampleText = (TextView) _dialog.findViewById(R.id.txtMessage);
        tvSampleText.setText(msg);

        Button btnContinue = (Button) _dialog.findViewById(R.id.btnContinue);
        Button btnContinue2 = (Button) _dialog.findViewById(R.id.btnContinue2);

        btnContinue.setText("Ok");

        btnContinue.setVisibility(View.VISIBLE);
        btnContinue2.setVisibility(View.GONE);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(_myActivity, MyListingActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
//                ((BlueMarketPicEditActivity)getApplicationContext()).finish();

//                if (_redirect) {
//                    try {
//
//                        Intent intent = new Intent(activity, Class.forName(activityPath + "." + _redirectClass));
//                        activity.startActivity(intent);
//                        activity.finish();
//
//                    } catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    _dialog.dismiss();
//                }
            }
        });

        _dialog.show();
        _dialog.getWindow().setAttributes(wmlp);
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, String data) {
        super.openDialog13(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, data);
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

    private void getProductDetail(String productId) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(_myActivity);
            progressDialog.setTitle(getString(R.string.loading_title));
            progressDialog.setMessage(getString(R.string.loading_message));
        }
        progressDialog.show();
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<ProductDetailResponse> userCall = service.productDt("Bearer " + token, productId);
        userCall.enqueue(new Callback<ProductDetailResponse>() {
            @Override
            public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                if (response.body() != null && response.body().getStatus() == true) {
                    ProductDetail details = response.body().getDetails();
                    String spin = details.getCategory();
                    //Log.d("cat", spin);
                    CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), product_menu);

                    spinner4.setAdapter(customAdapter);

                    int spinnerPosition = customAdapter.getIndex(spin);

                    spinner4.setSelection(spinnerPosition);

                    if (details.getHideDetails().trim().equalsIgnoreCase("1"))
                        cb_hideContactDetails.setChecked(true);
                    else
                        cb_hideContactDetails.setChecked(false);

                    bmpname.setText(details.getName());

                    bmpdesc.setText(details.getDescription());

                    autoCompView.setText(details.getLocation());
                    bmpprice.setText(details.getPrice());

                } else {
                    String msg = (String) getApplication().getText(R.string.goeswrong);
                    Toast.makeText(BlueMarketProductEditActivity.this, msg, Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<ProductDetailResponse> call, Throwable t) {
                t.printStackTrace();
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Toast.makeText(BlueMarketProductEditActivity.this, getApplication().getText(R.string.goeswrong), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this, "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
       /* Intent i = new Intent(BlueMarketProductEditActivity.this, Categories_bluemarketActivity.class);
        startActivity(i);*/
//        Intent i = new Intent(BlueMarketProductEditActivity.this, BlueMarketPicEditActivity.class);
//        i.putExtra("AllProduct", id);
//        i.putExtra("imageData", imageData);
//        startActivity(i);

        finish();
    }

    private void bellcount() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllNotice> userCall = service.allnotice1("Bearer " + token);

        userCall.enqueue(new Callback<AllNotice>() {
            @Override
            public void onResponse(Call<AllNotice> call, Response<AllNotice> response) {

                //  Log.d("STATUS", String.valueOf(response.body().getStatus()));
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
}
