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

import com.bluebuddy.R;
import com.bluebuddy.adapters.BlueCharterAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllCharter;
import com.bluebuddy.models.AllCharterDetails;
import com.bluebuddy.models.AllNotice;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.FIRE_REG_ID;
import static com.bluebuddy.app.AppConfig.USER_ID;

public class BlueMarketActivity2 extends BuddyActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageView back;
    private MyTextView bell_counter;
    private ArrayList<AllCharterDetails> BCCList;
    private BlueCharterAdapter blueCharterAdapter;
    private Activity _activity;
    private Context _context;
    private SharedPreferences pref;
    private String token, contains_key, regid, curru, bellcounter;
    private ProgressDialog mProgressDialog;
    private static final int RECORD_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_blue_market2);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.setImageResource1(3);
        super.notice();
        super.setTitle("BOAT RENTALS/CHARTERS");
        _activity = this;
        _context = this;

        super.loader();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        regid = pref.getString(FIRE_REG_ID, "");
        curru = pref.getString(USER_ID, "");

        Log.d("regid", regid);
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BlueMarketActivity2.this, Categories_bluemarketActivity.class);
                startActivity(i);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permission = ContextCompat.checkSelfPermission(BlueMarketActivity2.this, Manifest.permission.CAMERA);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    Log.i("HELLO", "Permission to storage denied");

                    if (ActivityCompat.shouldShowRequestPermissionRationale(BlueMarketActivity2.this, Manifest.permission.CAMERA)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BlueMarketActivity2.this);
                        builder.setMessage("Permission to access the storage is required for this app to import images.")
                                .setTitle("Permission required");

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("HELLO", "Clicked");

                                ActivityCompat.requestPermissions(BlueMarketActivity2.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RECORD_REQUEST_CODE);


                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {

                        ActivityCompat.requestPermissions(BlueMarketActivity2.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RECORD_REQUEST_CODE);

                    }
                }

                Intent intent = new Intent(BlueMarketActivity2.this, BlueMarketCharterPicActivity.class);
                startActivity(intent);
            }
        });


        mRecyclerView = (RecyclerView) findViewById(R.id.blue_market_recycler);
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
        }
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        blueCharterAdapter = new BlueCharterAdapter(_activity, _context, null, token, curru);
        mRecyclerView.setAdapter(blueCharterAdapter);
        blueCharterAdapter.notifyDataSetChanged();
        getAllCharter();
        bellcount();
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final String btnText3, final String btnText4, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog4(msg, btnText, btnText2, btnText3, btnText4, _redirect, activity, _redirectClass, layout);
    }

    public void getAllCharter() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllCharter> userCall = service.allCharter("Bearer " + token);

        userCall.enqueue(new Callback<AllCharter>() {
            @Override
            public void onResponse(Call<AllCharter> call, Response<AllCharter> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("onResponse:", "" + response.body().getCharter_list());
                Log.d("TOKEN:", "" + token);

                blueCharterAdapter.updateAllCharter(response.body().getCharter_list());
            }

            @Override
            public void onFailure(Call<AllCharter> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(BlueMarketActivity2.this, Categories_bluemarketActivity.class);
        startActivity(i);
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
