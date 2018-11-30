package com.bluebuddy.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bluebuddy.R;

import java.util.ArrayList;
import java.util.List;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class ListingQueryActivityProduct2 extends BuddyActivity implements AdapterView.OnItemSelectedListener {
    public ImageView chk, chk2, chk3, back;
    String token, nameP, descP, locP, typeP, latP, longP, imgP1, imgP2, imgP3, imgP4, prp, Cfp, Chp, spinneritem;
    boolean one, two = false;
    Button sdid, sdid2;
    EditText boatday, boatfee;
    Button goforpay;
    String[] payday = {"1 Week", "2 Week", "3 Week", "4 Week", "Select Duration"};
    private Activity _activity;
    private Context _context;
    private SharedPreferences pref;
    private String payPrice, payDuration, productid;
    private String is_featured = "";
    private Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_listing_query);
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_listing_query);
        super.initialize();
        super.setTitle("LISTING");
        Bundle bundle = getIntent().getExtras();

        /*
         i.putExtra("Pimg1",encode_image1);
                    i.putExtra("Pimg2",encode_image2);
                    i.putExtra("Pimg3",encode_image3);
                    i.putExtra("Pimg4",encode_image4);
                    i.putExtra("Pname",bmpname1);
                    i.putExtra("Ptype",spinneritem);
                    i.putExtra("Pdesc",bmpdesc1);
                 //   i.putExtra("cImage","data:image/jpeg;base64,"+ encode_image);
                    i.putExtra("Pprice",bmpprice1);
                    i.putExtra("Ploc",location);
                    i.putExtra("Plat",_lat);
                    i.putExtra("Plong",_long);
        */
        productid = bundle.getString("ID");
        Log.d("proid", productid);
       /* imgP2 = bundle.getString("Pimg2");
        Log.d("IMG2",imgP2);
        imgP3 = bundle.getString("Pimg3");
        Log.d("IMG3",imgP3);
        imgP4 = bundle.getString("Pimg4");
        Log.d("IMG4",imgP4);
         nameP= bundle.getString("Pname");
        Log.d("name",nameP);
        typeP = bundle.getString("Ptype");
        Log.d("Ptype",typeP);
        descP = bundle.getString("Pdesc");
        Log.d("desc",descP);
        prp = bundle.getString("Pprice");
        Log.d("price",prp);
        locP = bundle.getString("Ploc");
        Log.d("ploc",locP);
        latP = bundle.getString("Plat");
        Log.d("plat",latP);
        longP = bundle.getString("Plong");
        Log.d("plong",longP);*/

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
                        //   Toast.makeText(ListingQueryActivityProduct2.this, "feature api call", Toast.LENGTH_LONG).show();
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
                        //    Toast.makeText(ListingQueryActivityProduct2.this, "free api call", Toast.LENGTH_LONG).show();
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

    private void checkforcheckbox() {
        //  if(!is_featured.equals("0") && !is_featured.equals("1")){
        if (is_featured.equals("")) {
            oDialog("Please Select one Free/Feature", "Close", "", false, _activity, "", 0);
        } else if (is_featured.equals("0")) {
            //   boatfee.setText("");
            //   boatday.setText("");
            //
            Intent i = new Intent(ListingQueryActivityProduct2.this, MyListingActivity.class);
            startActivity(i);
           /* ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

            Call<ServerResponsechrt> userCall = service.productinst("Bearer " + token, "post", imgP1, imgP2, imgP3, imgP4, nameP, typeP, descP, prp,locP,latP,longP);
            userCall.enqueue(new Callback<ServerResponsechrt>() {
                @Override
                public void onResponse(Call<ServerResponsechrt> call, Response<ServerResponsechrt> response) {

                    Log.d("onResponse", "" + response.body().getMessage());

                    if (response.body().getStatus() == "true") {

                        Log.d("onResponse", "" + response.body().getStatus());
                        Log.d("Return ID:",response.body().getId());
                        final String rc2 = "BlueMarketActivity42";
                        oDialog("Thanks for Listing with us.", "Submit", "", true, _activity, rc2, 0);
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
            //  payDuration = boatday.getText().toString().trim();
            payDuration = spin.getSelectedItem().toString().trim();
            if (String.valueOf(spin.getSelectedItem()) == "Select Duration") {
                oDialog("Please select Days", "Close", "", false, _activity, "", 0);
            } else if (payPrice.isEmpty()) {
                oDialog("Enter Price ", "Close", "", false, _activity, "", 0);
            } /*else if (payDuration.isEmpty()) {
                oDialog("Enter Duration ", "Close", "", false, _activity, "", 0);
            }*/ else {
                Log.d("price:", payPrice);
                Log.d("duration:", payDuration);
//
                Intent i = new Intent(ListingQueryActivityProduct2.this, PaymentActivity2.class);
                i.putExtra("ID", productid);
                i.putExtra("price", payPrice);
                i.putExtra("name", "Bluebuddy Product");
                startActivity(i);
              /*  ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

                Call<ServerResponsechrt> userCall = service.productinst("Bearer " + token, "post", imgP1, imgP2, imgP3, imgP4, nameP, typeP, descP, prp,locP,latP,longP);
                userCall.enqueue(new Callback<ServerResponsechrt>() {
                    @Override
                    public void onResponse(Call<ServerResponsechrt> call, Response<ServerResponsechrt> response) {

                        Log.d("onResponse", "" + response.body().getMessage());

                        if (response.body().getStatus() == "true") {

                            Log.d("onResponse", "" + response.body().getStatus());
                            Log.d("onResponse", "" + response.body().getStatus());
                            Log.d("Return ID:",response.body().getId());
                            Intent i = new Intent(ListingQueryActivityProduct2.this,PaymentActivity2.class);
                            i.putExtra("ID",response.body().getId());
                            i.putExtra("price",payPrice);
                            i.putExtra("name","Bluebuddy Product");
                            startActivity(i);
                            *//*final String rc2 = "BlueMarketActivity42";
                            oDialog("Thanks for Listing with us.", "Submit", "", true, _activity, rc2, 0);*//*
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinneritem = payday[position];
        if (String.valueOf(spin.getSelectedItem()).equalsIgnoreCase("1 week")) {
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
