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

public class ListingQueryActivity extends BuddyActivity implements AdapterView.OnItemSelectedListener {

    public ImageView chk, chk2, chk3, back;
    String token, Cname, Cdesc, Cloc, Ctype, Clat, Clong, Cimg, Ccap, Cfp, Chp, spinneritem;
    boolean one, two = false;
    Button sdid, sdid2;
    EditText boatday, boatfee;
    Button goforpay;
    String[] payday = {"1 Week", "2 Week", "3 Week", "4 Week", "Select Duration"};
    File fileImage = null;
    private Activity _activity;
    private Context _context;
    private SharedPreferences pref;
    private String payPrice, payDuration;
    private String is_featured = "";
    private Spinner spin;
    private Activity _myActivity;
    private ProgressDialog progressDialog;
    private String hideContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_listing_query);
        super.onCreate(savedInstanceState);
        _myActivity = this;
        //  setContentView(R.layout.activity_listing_query);
        super.initialize();
        super.setTitle("LISTING");
        Bundle bundle = getIntent().getExtras();
        Cname = bundle.getString("cName");
        //  Log.d("cname",Cname);
        Cdesc = bundle.getString("cDesc");
        //  Log.d("cdesc",Cdesc);
        Cloc = bundle.getString("cLoc");
        // Log.d("cloc",Cloc);
        Cfp = bundle.getString("cFPrice");
        //  Log.d("cfullprice",Cfp);
        Chp = bundle.getString("cHPrice");
        //  Log.d("chalfprice",Chp);
        Ctype = bundle.getString("cType");
        //  Log.d("ctype",Ctype);
        Ccap = bundle.getString("cCap");

        Cimg = bundle.getString("cImage");
        //   Log.d("cimage",Cimg);
        Clat = bundle.getString("cLat");
        // Log.d("clat",Clat);
        Clong = bundle.getString("cLong");
        hideContact = bundle.getString("cHideContact");
        // Log.d("clong",Clong);
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");

        _activity = this;
        _context = this;

        spin = (Spinner) findViewById(R.id.boatday);
        spin.setOnItemSelectedListener(this);

//        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), payday);
//        spin.setAdapter(customAdapter);
//        spin.setSelection(payday.length - 1);

        setSpinner();

        spinneritem = spin.getSelectedItem().toString();
        // pref = super.getPref();
        // token = pref.getString(ACCESS_TOKEN, "");
        boatfee = (EditText) findViewById(R.id.boatfee);
        // boatday = (EditText) findViewById(R.id.boatday);

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
//                Intent i = new Intent(ListingQueryActivity.this, BoatCharterFormActivity.class);
//                 i.putExtra("encoded_image","");
//                startActivity(i);
                onBackPressed();

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
                        //  Toast.makeText(ListingQueryActivity.this, "feature api call", Toast.LENGTH_LONG).show();
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
                        //   Toast.makeText(ListingQueryActivity.this, "free api call", Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void serviceCallForMarket(final boolean is_featured) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(_myActivity);
            progressDialog.setTitle(getString(R.string.loading_title));
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.loading_message));
        }
        progressDialog.show();
        if (!Cimg.matches("")) {
            fileImage = new File(Cimg);
        }
        MultipartBody.Part body = null;
        RequestBody requestBodyCname = RequestBody.create(
                MediaType.parse("multipart/form-data"), Cname);
        RequestBody requestBodyCdesc = RequestBody.create(
                MediaType.parse("multipart/form-data"), Cdesc);
        RequestBody requestBodyCloc = RequestBody.create(
                MediaType.parse("multipart/form-data"), Cloc);
        RequestBody requestBodyCfp = RequestBody.create(
                MediaType.parse("multipart/form-data"), Cfp);
        RequestBody requestBodyChp = RequestBody.create(
                MediaType.parse("multipart/form-data"), Chp);
        RequestBody requestBodyCtype = RequestBody.create(
                MediaType.parse("multipart/form-data"), Ctype);
        RequestBody requestBodyCcap = RequestBody.create(
                MediaType.parse("multipart/form-data"), Ccap);
        RequestBody requestBodyClat = RequestBody.create(
                MediaType.parse("multipart/form-data"), Clat);
        RequestBody requestBodyClong = RequestBody.create(
                MediaType.parse("multipart/form-data"), Clong);
        RequestBody requestBodyHideContact = RequestBody.create(
                MediaType.parse("multipart/form-data"), hideContact);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

        if (fileImage != null && fileImage.length() > 0) {
            RequestBody requestBodyOrderDataFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileImage);
            body = MultipartBody.Part.createFormData("ch_img", fileImage.getName().replace(" ", ""), requestBodyOrderDataFile);
        }
        Call<ServerResponsechrt> userCall = service.chrtinstMultipart("Bearer " + token,
                requestBodyCname,
                requestBodyCdesc,
                requestBodyCloc,
                requestBodyCfp,
                requestBodyChp,
                requestBodyCtype,
                requestBodyCcap,
                requestBodyClat,
                requestBodyClong,
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
                        if (is_featured) {
                            // go for paypel
                            Intent i = new Intent(ListingQueryActivity.this, PaymentActivity2.class);
                            i.putExtra("ID", response.body().getId());
                            i.putExtra("price", payPrice);
                            i.putExtra("name", "Bluebuddy Charter");
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
            // boatfee.setText("");
            //boatday.setText("");
            //
            serviceCallForMarket(false);
            /*ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

            Call<ServerResponsechrt> userCall = service.chrtinst("Bearer "+token,"post",Cname, Cdesc, Cloc, Cfp, Chp, Ctype, Ccap,Cimg,Clat,Clong);
            userCall.enqueue(new Callback<ServerResponsechrt>() {
                @Override
                public void onResponse(Call<ServerResponsechrt> call, Response<ServerResponsechrt> response) {
                //    Log.d("onResponse", "" + response.body().getMessage());
                    if (response.body().getStatus() == "true") {
                        //   Log.d("onResponse", "" + response.body().getEmail());
                       // Log.d("onResponse", "" + response.body().getStatus());
                       // Log.d("Return ID:",response.body().getId());
                      //  final String rc2 = "BlueMarketCharterActivityNew";
                        final String rc2 = "MyListingActivity";
                        oDialog("Thanks for Listing with us.", "Close", "", true, _activity, rc2, 0);
                    } else{
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
            //    payDuration = boatday.getText().toString().trim();
            payDuration = spin.getSelectedItem().toString().trim();
            if (String.valueOf(spin.getSelectedItem()) == "Select Duration") {
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

                Call<ServerResponsechrt> userCall = service.chrtinst("Bearer "+token,"post",Cname, Cdesc, Cloc, Cfp, Chp, Ctype, Ccap,Cimg,Clat,Clong);
                userCall.enqueue(new Callback<ServerResponsechrt>() {
                    @Override
                    public void onResponse(Call<ServerResponsechrt> call, Response<ServerResponsechrt> response) {
                        Log.d("onResponse", "" + response.body().getMessage());
                        if (response.body().getStatus() == "true") {
                            //   Log.d("onResponse", "" + response.body().getEmail());
                            Log.d("onResponse", "" + response.body().getStatus());
                            Log.d("Return ID:",response.body().getId());
                            Intent i = new Intent(ListingQueryActivity.this,PaymentActivity2.class);
                            i.putExtra("ID",response.body().getId());
                            i.putExtra("price",payPrice);
                            i.putExtra("name","Bluebuddy Charter");
                            startActivity(i);

                          *//*  final String rc2 = "BlueMarketCharterActivityNew";
                            oDialog("Thanks for Listing with us.", "Submit", "", true, _activity, rc2, 0);*//*
                        } else{
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
