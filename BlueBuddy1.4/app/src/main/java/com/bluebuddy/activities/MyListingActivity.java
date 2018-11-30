package com.bluebuddy.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bluebuddy.R;
import com.bluebuddy.adapters.BlueCharterAdapter2;
import com.bluebuddy.adapters.BlueMarketCoursesAdapter2;
import com.bluebuddy.adapters.BlueMarketProductAdapter2;
import com.bluebuddy.adapters.ServiceOfferedAdapter2;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.app.AppConfig;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllCharterDetails;
import com.bluebuddy.models.AllMyListing;
import com.bluebuddy.models.AllNotice;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.USER_ID;

public class MyListingActivity extends BuddyActivity {

    private static final int RECORD_REQUEST_CODE = 101;
    ImageView back;
    TextView txtidc, txtidpr, txtidco, txtidser;
    MyTextView bell_counter, txtshid;
    // private MyListingAdapter myListingAdapter;
    String bellcounter, token, curru;
    SharedPreferences pref;
    private RecyclerView mRecyclerView, mRecyclerView1, mRecyclerView2, mRecyclerView3;
    private RecyclerView.LayoutManager mLayoutManager, mLayoutManager1, mLayoutManager2, mLayoutManager3;
    //   private ArrayList<BlueMarketItems> BMCList;
    private ArrayList<AllCharterDetails> BCCList;
    private BlueCharterAdapter2 blueCharterAdapter;
    private BlueMarketCoursesAdapter2 blueMarketCoursesAdapter;
    private BlueMarketProductAdapter2 blueMarketProductAdapter;
    private ServiceOfferedAdapter2 serviceOfferedAdapter;

    // private MyListingsAdapter2 myListingsAdapter2,myListingsAdapter3,myListingsAdapter4,myListingsAdapter5;
    private Activity _activity;
    private Context _context;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //  super.setLayout(R.layout.activity_blue_market);
        super.setLayout(R.layout.activity_blue_market2showall);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.loader();
        super.setImageResource1(5);
        super.notice();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;
        super.setTitle("MY LISTINGS");

        _activity = this;
        _context = this;
        //
        int permission = ContextCompat.checkSelfPermission(MyListingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //   Log.i("HELLO", "Permission to location denied");

            if (ActivityCompat.shouldShowRequestPermissionRationale(MyListingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyListingActivity.this);
                builder.setMessage("Permission to access the location is required for this app to access your location.")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        //  Log.i("HELLO", "Clicked");
                        ActivityCompat.requestPermissions(MyListingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RECORD_REQUEST_CODE);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                ActivityCompat.requestPermissions(MyListingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RECORD_REQUEST_CODE);

            }
        }

        //
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        curru = pref.getString(USER_ID, "");

       /* FloatingActionButton create_new_itemforlist = (FloatingActionButton) findViewById(R.id.create_new_itemforlist);
        create_new_itemforlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyListingActivity.this, Categories_bluemarketActivity2.class);
                startActivity(intent);
            }
        });*/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyListingActivity.this, Categories_bluemarketActivity2.class);
                //  intent.putExtra("bmcharter","2");
                startActivity(intent);
            }
        });

        txtidc = (TextView) findViewById(R.id.txtidc);
        txtidpr = (TextView) findViewById(R.id.txtidpr);
        txtidco = (TextView) findViewById(R.id.txtidco);
        txtidser = (TextView) findViewById(R.id.txtidser);
        txtshid = (MyTextView) findViewById(R.id.txtshid);
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        back = (ImageView) findViewById(R.id.imgNotification2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(MyListingActivity.this, MyAccountMenu.class);
//                startActivity(i);
                onBackPressed();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.blue_market_recycler);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView1 = (RecyclerView) findViewById(R.id.blue_market_recycler1);
        mRecyclerView1.setNestedScrollingEnabled(false);
        mRecyclerView2 = (RecyclerView) findViewById(R.id.blue_market_recycler2);
        mRecyclerView2.setNestedScrollingEnabled(false);
        mRecyclerView3 = (RecyclerView) findViewById(R.id.blue_market_recycler3);
        mRecyclerView3.setNestedScrollingEnabled(false);

        //   mRecyclerView = (RecyclerView) findViewById(R.id.blue_market_recycler);
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
        }
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mLayoutManager1 = new LinearLayoutManager(this);
        mRecyclerView1.setLayoutManager(mLayoutManager1);

        mLayoutManager2 = new LinearLayoutManager(this);
        mRecyclerView2.setLayoutManager(mLayoutManager2);

        mLayoutManager3 = new LinearLayoutManager(this);
        mRecyclerView3.setLayoutManager(mLayoutManager3);


        //  BMCList = new ArrayList<>();

        blueCharterAdapter = new BlueCharterAdapter2(_activity, _context, null, token, curru);
        mRecyclerView.setAdapter(blueCharterAdapter);

        blueMarketCoursesAdapter = new BlueMarketCoursesAdapter2(_activity, _context, null, token, "", curru);
        mRecyclerView1.setAdapter(blueMarketCoursesAdapter);

        blueMarketProductAdapter = new BlueMarketProductAdapter2(_activity, _context, null, token, curru);
        mRecyclerView2.setAdapter(blueMarketProductAdapter);

        serviceOfferedAdapter = new ServiceOfferedAdapter2(_activity, _context, null, token, curru);
        mRecyclerView3.setAdapter(serviceOfferedAdapter);

//        blueCharterAdapter.notifyDataSetChanged();
//        blueMarketCoursesAdapter.notifyDataSetChanged();
//        blueMarketProductAdapter.notifyDataSetChanged();

//        serviceOfferedAdapter.notifyDataSetChanged();


        //  myListingAdapter = new MyListingAdapter(_activity, _context, BMCList);
        //  mRecyclerView.setAdapter(myListingAdapter);
       /* if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();*/
        //  myListingAdapter.notifyDataSetChanged();
        getAllListings();
        // getAllCourse();
        //  getAllProduct();
        // getAllService();
        bellcount();
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final String btnText3, final String btnText4, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog4(msg, btnText, btnText2, btnText3, btnText4, _redirect, activity, _redirectClass, layout);
    }

    public void getAllListings() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllMyListing> userCall = service.allmylist("Bearer " + token);

        userCall.enqueue(new Callback<AllMyListing>() {
            @Override
            public void onResponse(Call<AllMyListing> call, Response<AllMyListing> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                if (response.code() == AppConfig.SUCCESS_STATUS) {
                    if (response.body() != null && response.body().getStatus() != false) {

                        if (!response.body().getCharter_list().isEmpty()) {
                            txtidc.setVisibility(View.VISIBLE);
                        }
                        blueCharterAdapter.updateAllCharter(response.body().getCharter_list());

                        // for Courses
                        if (!response.body().getCourse_list().isEmpty()) {
                            txtidco.setVisibility(View.VISIBLE);

                        }
                        blueMarketCoursesAdapter.updateAllCourses(response.body().getCourse_list());

                        // for product
                        if (!response.body().getProduct_list().isEmpty()) {
                            txtidpr.setVisibility(View.VISIBLE);
                        }
                        blueMarketProductAdapter.updateAllProduct(response.body().getProduct_list());

                        // for service
                        if (!response.body().getService_list().isEmpty()) {
                            txtidser.setVisibility(View.VISIBLE);
                        }
                        serviceOfferedAdapter.updateAllService(response.body().getService_list());

                    } else if (response.body().getStatus() != true) {
                        txtshid.setVisibility(View.VISIBLE);
                    }
                } else if (response.code() == AppConfig.ERROR_STATUS_LOG_AGAIN) {
                    // login again
                    logoutAndSignInPage(_activity);

                } else {
                    Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AllMyListing> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAllCourse() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllMyListing> userCall = service.allmylist("Bearer " + token);

        userCall.enqueue(new Callback<AllMyListing>() {
            @Override
            public void onResponse(Call<AllMyListing> call, Response<AllMyListing> response) {
                //  if (mProgressDialog.isShowing())
                //    mProgressDialog.dismiss();
                if (response.body().getStatus() != false) {
                    if (!response.body().getCourse_list().isEmpty()) {
                        txtidco.setVisibility(View.VISIBLE);

                    }
                    blueMarketCoursesAdapter.updateAllCourses(response.body().getCourse_list());

                    //,response.body().getProduct_list()
                    //  myListingsAdapter2.updateAllCharter(response.body().getCharter_list());
                } else if (response.body().getStatus() != true) {
                    txtshid.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<AllMyListing> call, Throwable t) {

            }
        });
    }

    public void getAllProduct() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllMyListing> userCall = service.allmylist("Bearer " + token);

        userCall.enqueue(new Callback<AllMyListing>() {
            @Override
            public void onResponse(Call<AllMyListing> call, Response<AllMyListing> response) {
                //   if (mProgressDialog.isShowing())
                //     mProgressDialog.dismiss();
                if (response.body().getStatus() != false) {
                    if (!response.body().getProduct_list().isEmpty()) {
                        txtidpr.setVisibility(View.VISIBLE);
                    }
                    Log.d("STATUS", String.valueOf(response.body().getStatus()));
                    Log.d("onResponse:", "" + response.body().getCharter_list());
                    Log.d("TOKEN:", "" + token);
                    blueMarketProductAdapter.updateAllProduct(response.body().getProduct_list());
                    //,response.body().getProduct_list()
                    //  myListingsAdapter2.updateAllCharter(response.body().getCharter_list());
                } else if (response.body().getStatus() != true) {
                    txtshid.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<AllMyListing> call, Throwable t) {

            }
        });
    }

    public void getAllService() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllMyListing> userCall = service.allmylist("Bearer " + token);

        userCall.enqueue(new Callback<AllMyListing>() {
            @Override
            public void onResponse(Call<AllMyListing> call, Response<AllMyListing> response) {
                //  if (mProgressDialog.isShowing())
                //      mProgressDialog.dismiss();
                if (response.body().getStatus() != false) {
                    if (!response.body().getService_list().isEmpty()) {
                        txtidser.setVisibility(View.VISIBLE);
                    }
                    Log.d("STATUS", String.valueOf(response.body().getStatus()));
                    Log.d("onResponse:", "" + response.body().getCharter_list());
                    Log.d("TOKEN:", "" + token);
                    serviceOfferedAdapter.updateAllService(response.body().getService_list());


                    //,response.body().getProduct_list()
                    //  myListingsAdapter2.updateAllCharter(response.body().getCharter_list());
                } else if (response.body().getStatus() != true) {
                    txtshid.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<AllMyListing> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        Intent i = new Intent(MyListingActivity.this, MyAccountMenu.class);
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
