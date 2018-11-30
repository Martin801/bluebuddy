package com.bluebuddy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bluebuddy.R;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.ServerResponsechrt;
import com.bluebuddy.interfaces.ApiInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class ListingQueryCourse extends BuddyActivity implements AdapterView.OnItemSelectedListener {
    public ImageView chk, chk2, chk3, back;
    String token, Catc, Urlc, Agnc, Durc, Descc, Prc, Locc, Imgc, Latc, Longc, spinneritem;
    boolean one, two = false;
    Button sdid, sdid2;
    EditText boatday, boatfee;
    Button goforpay;
    String[] payday = {"1 Week", "2 Week", "3 Week", "4 Week", "Select Dur"};
    File fileImage = null;
    private Activity _activity;
    private Context _context;
    private SharedPreferences pref;
    private String payPrice, payDuration, category;
    private String is_featured = "";
    private Spinner spin;
    private ProgressDialog progressDialog;

    /*
     i.putExtra("Ccat",co_cat);
                        i.putExtra("Cagn",agency_name1);
                        i.putExtra("Curl",agency_url1);
                        i.putExtra("Cdur",co_duration1);
                        i.putExtra("Cdesc",co_desc1);
                        i.putExtra("Cpr",co_price1);
                        i.putExtra("Cloc",location);
                        i.putExtra("Cimg","data:image/jpeg;base64,"+ encode_image1);
                        i.putExtra("Clat",_lat);
                        i.putExtra("Clong",_long);*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_listing_query);
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_listing_query);
        super.initialize();
        super.setTitle("LISTING");
        Bundle bundle = getIntent().getExtras();
        category = bundle.getString("category");
        Agnc = bundle.getString("Cagn");
        //Log.d("agency",Agnc);
        Urlc = bundle.getString("Curl");
        // Log.d("url",Urlc);
        Durc = bundle.getString("Cdur");
        // Log.d("duration",Durc);
        Catc = bundle.getString("Ccat");
        // Log.d("catc",Catc);
        Descc = bundle.getString("Cdesc");
        // Log.d("desc",Descc);
        Prc = bundle.getString("Cpr");
        //Log.d("price",Prc);
        Locc = bundle.getString("Cloc");
        // Log.d("loc",Locc);
        Imgc = bundle.getString("Cimg");
        // Log.d("img",Imgc);
        Latc = bundle.getString("Clat");
        // Log.d("lat",Latc);
        Longc = bundle.getString("Clong");
        //Log.d("long",Longc);

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");

        _activity = this;
        _context = this;
        spin = (Spinner) findViewById(R.id.boatday);
        spin.setOnItemSelectedListener(this);

        setSpinner();

//        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), payday);
//        spin.setAdapter(customAdapter);
//        spin.setSelection(payday.length - 1);

        spinneritem = spin.getSelectedItem().toString();

        // pref = super.getPref();
        // token = pref.getString(ACCESS_TOKEN, "");
        boatfee = (EditText) findViewById(R.id.boatfee);
        //   boatday = (EditText) findViewById(R.id.boatday);

        goforpay = (Button) findViewById(R.id.goforpay);
        goforpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkforcheckbox();
            }
        });

        sdid2 = (Button) findViewById(R.id.sdid2);
        chk2 = (ImageView) findViewById(R.id.chk2);

        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListingQueryCourse.this, BlueMarketCoursesActivity.class);
                i.putExtra("encoded_image", "");
                i.putExtra("category", category);
                i.putExtra("DATA_VALUE", "");
                startActivity(i);
            }
        });

        sdid2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (two) {
                    chk2.setImageResource(R.drawable.unchecked);
                    chk.setImageResource(R.drawable.checked);
                    two = false;
                    //  chk.setImageResource(R.drawable.unchecked);
                } else {
                    chk2.setImageResource(R.drawable.checked);
                    chk.setImageResource(R.drawable.unchecked);
                    two = true;
                    if (two = true) {
                        //api call for feature
                        is_featured = "1";
                        //   Toast.makeText(ListingQueryCourse.this, "feature api call", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });


        chk = (ImageView) findViewById(R.id.chk);
        sdid = (Button) findViewById(R.id.sdid);
        sdid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (one) {
                    chk.setImageResource(R.drawable.unchecked);
                    chk2.setImageResource(R.drawable.checked);
                    one = false;

                } else {
                    chk.setImageResource(R.drawable.checked);
                    chk2.setImageResource(R.drawable.unchecked);
                    one = true;
                    if (one = true) {
                        //api call for free
                        is_featured = "0";
                        //    Toast.makeText(ListingQueryCourse.this, "free api call", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });
    }

    private void setSpinner() {
        List<String> list = new ArrayList<String>();
        list.add("1 Week");
        list.add("2 Weeks");
        list.add("3 Weeks");
        list.add("4 Weeks");
        list.add("Select Duration");
        final int listsize = list.size() - 1;

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list) {
            @Override
            public int getCount() {
                return (listsize); // Truncate the list
            }
        };

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);
        spin.setSelection(listsize);
    }

    private void serviceCallForMarket(final boolean is_featured) {

        if (BlueMarketCoursePicActivity.courseImagePath != null && !BlueMarketCoursePicActivity.courseImagePath.matches("")) {
            // fileImage=new File(Imgc);
            fileImage = new File(BlueMarketCoursePicActivity.courseImagePath);

        } else {
            oDialog("Image is missing", "Close", "", false, _activity, "", 0);
            return;
        }
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(_activity);
            progressDialog.setTitle(getString(R.string.loading_title));
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.loading_message));
        }
        progressDialog.show();
        MultipartBody.Part body = null;
        RequestBody requestBodyCname = RequestBody.create(
                MediaType.parse("multipart/form-data"), Catc);
        RequestBody requestBodyCdesc = RequestBody.create(
                MediaType.parse("multipart/form-data"), Agnc);
        RequestBody requestBodyCloc = RequestBody.create(
                MediaType.parse("multipart/form-data"), Urlc);
        RequestBody requestBodyCfp = RequestBody.create(
                MediaType.parse("multipart/form-data"), Durc);
        RequestBody requestBodyChp = RequestBody.create(
                MediaType.parse("multipart/form-data"), Descc);
        RequestBody requestBodyCtype = RequestBody.create(
                MediaType.parse("multipart/form-data"), Prc);
        RequestBody requestBodyCcap = RequestBody.create(
                MediaType.parse("multipart/form-data"), Locc);
        RequestBody requestBodyClat = RequestBody.create(
                MediaType.parse("multipart/form-data"), Latc);
        RequestBody requestBodyClong = RequestBody.create(
                MediaType.parse("multipart/form-data"), Longc);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

        if (fileImage != null && fileImage.length() > 0) {
            RequestBody requestBodyOrderDataFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileImage);
            body = MultipartBody.Part.createFormData("co_img", fileImage.getName().replace(" ", ""), requestBodyOrderDataFile);
        }
        Call<ServerResponsechrt> userCall = service.coursesinstMultipart("Bearer " + token, requestBodyCname, requestBodyCdesc, requestBodyCloc, requestBodyCfp, requestBodyChp, requestBodyCtype, requestBodyCcap, requestBodyClat, requestBodyClong, body);
        userCall.enqueue(new Callback<ServerResponsechrt>() {
            @Override
            public void onResponse(Call<ServerResponsechrt> call, Response<ServerResponsechrt> response) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                if (response.body().getStatus() != null) {
                    if (response.body().getStatus() == "true") {
                        BlueMarketCoursePicActivity.courseImagePath = "";
                        if (is_featured) {
                            // go for paypel
                            Intent i = new Intent(ListingQueryCourse.this, PaymentActivity2.class);
                            i.putExtra("ID", response.body().getId());
                            i.putExtra("category2", category);
                            i.putExtra("price", payPrice);
                            i.putExtra("name", "Bluebuddy Course");
                            startActivity(i);
                        } else {
                            final String rc2 = "MyListingActivity";
                            oDialog(response.body().getMessage(), "Close", "", true, _activity, rc2, 0);
                        }

                    } else {

                        String msg = (String) getApplication().getText(R.string.goeswrong);
                        oDialog(response.body().getMessage(), "Ok", "", false, _activity, "", 0);
                    }
                } else {
                    String msg = (String) getApplication().getText(R.string.goeswrong);
                    oDialog(msg, "Ok", "", false, _activity, "", 0);
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

    private void checkforcheckbox() {
        //  if(!is_featured.equals("0") && !is_featured.equals("1")){
        if (is_featured.equals("")) {
            oDialog("Please Select one Free/Feature", "Close", "", false, _activity, "", 0);
        } else if (is_featured.equals("0")) {
            serviceCallForMarket(false);
            /*ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<ServerResponsechrt> userCall = service.coursesinst("Bearer " + token,Catc,Agnc,Urlc,Durc,Descc,Prc,Locc,Imgc,Latc,Longc);

            userCall.enqueue(new Callback<ServerResponsechrt>() {
                @Override
                public void onResponse(Call<ServerResponsechrt> call, Response<ServerResponsechrt> response) {
                  *//*  if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();*//*
                    Log.d("onResponse", "" + response.body().getMessage());

                    if (response.body().getStatus() == "true") {
                        Log.d("onResponse", "" + response.body().getStatus());
                       // oDialogReturn("Thanks for Listing with us.", "Submit", "", true,_activity, "BlueMarketCourseActivityNew", 0, category);
                      //  BlueMarketActivity32
                        oDialogReturn("Thanks for Listing with us.", "Close", "", true,_activity, "MyListingActivity", 0, category);
                    } else {
                        Log.d("onResponse", "" + response.body().getStatus());
                    }
                }

                @Override
                public void onFailure(Call<ServerResponsechrt> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                }
            });*/

            //
        } else if (is_featured.equals("1")) {
            payPrice = boatfee.getText().toString().trim();
            // payDuration = boatday.getText().toString().trim();
            payDuration = spin.getSelectedItem().toString().trim();
            if (String.valueOf(spin.getSelectedItem()) == "Select Dur") {

                oDialog("Please select Days", "Close", "", false, _activity, "", 0);

            } else if (payPrice.isEmpty()) {

                oDialog("Enter Price ", "Close", "", false, _activity, "", 0);

            } /*else if (payDuration.isEmpty()) {
                oDialog("Enter Duration ", "Close", "", false, _activity, "", 0);
            }*/ else {
                serviceCallForMarket(true);
               /* Log.d("price:",payPrice);
                Log.d("duration:",payDuration);
//

                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                Call<ServerResponsechrt> userCall = service.coursesinst("Bearer " + token,Catc,Agnc,Urlc,Durc,Descc,Prc,Locc,Imgc,Latc,Longc);

                userCall.enqueue(new Callback<ServerResponsechrt>() {
                    @Override
                    public void onResponse(Call<ServerResponsechrt> call, Response<ServerResponsechrt> response) {
                      *//*  if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();*//*
                        Log.d("onResponse", "" + response.body().getMessage());

                        if (response.body().getStatus() == "true") {
                            Log.d("onResponse", "" + response.body().getStatus());
                            Log.d("Return ID:",response.body().getId());
                            Intent i = new Intent(ListingQueryCourse.this,PaymentActivity2.class);
                            i.putExtra("ID",response.body().getId());
                            i.putExtra("category2",category);
                            i.putExtra("price",payPrice);
                            i.putExtra("name","Bluebuddy Course");
                            startActivity(i);
                          //  oDialogReturn("Thanks for Listing with us.", "Submit", "", true,_activity, "BlueMarketCourseActivityNew", 0, category);
                        } else {
                            Log.d("onResponse", "" + response.body().getStatus());
                        }
                    }

                    @Override
                    public void onFailure(Call<ServerResponsechrt> call, Throwable t) {
                        Log.d("onFailure", t.toString());
                    }
                });*/
                //
            }


        }

    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    public void oDialogReturn(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, String returnParam) {
        super.openDialogReturn(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, returnParam);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinneritem = payday[position];
        if (String.valueOf(spin.getSelectedItem()).equalsIgnoreCase("1 Week")) {
            //TODAY
            boatfee.setText("$ 10");
        } else if (String.valueOf(spin.getSelectedItem()).equalsIgnoreCase("2 Weeks")) {
            //TODAY
            boatfee.setText("$ 15");
        } else if (String.valueOf(spin.getSelectedItem()).equalsIgnoreCase("3 Weeks")) {
            //TODAY
            boatfee.setText("$ 20");
        } else if (String.valueOf(spin.getSelectedItem()).equalsIgnoreCase("4 Weeks")) {
            //TODAY
            boatfee.setText("$ 25");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
