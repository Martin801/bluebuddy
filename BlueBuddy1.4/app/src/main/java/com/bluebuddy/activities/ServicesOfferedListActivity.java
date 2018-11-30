package com.bluebuddy.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bluebuddy.R;
import com.bluebuddy.adapters.ServiceOfferedAdapter2;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.AllService;
import com.bluebuddy.models.AllServiceDetails;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.USER_ID;

public class ServicesOfferedListActivity extends BuddyActivity {
    private MyTextView navtxt, bell_counter;
    private EditText srch;
    private static final int RECORD_REQUEST_CODE = 101;
    private ImageView back;
    private RecyclerView recycler_view_event;
    private ArrayList<AllServiceDetails> BCCList;
    private ServiceOfferedAdapter2 serviceOfferedAdapter;
    private Activity _activity;
    private Context _context;
    private SharedPreferences pref;
    private String token, curru, bellcounter;
    private LinearLayoutManager mLayoutManager;
    private FloatingActionButton fab1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_services_offered_list);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.notice();
        super.setTitle("SERVICES");
        int permission = ContextCompat.checkSelfPermission(ServicesOfferedListActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("HELLO", "Permission to location denied");

            if (ActivityCompat.shouldShowRequestPermissionRationale(ServicesOfferedListActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ServicesOfferedListActivity.this);
                builder.setMessage("Permission to access the location is required for this app to access your location.")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("HELLO", "Clicked");

                        ActivityCompat.requestPermissions(ServicesOfferedListActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RECORD_REQUEST_CODE);


                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                ActivityCompat.requestPermissions(ServicesOfferedListActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RECORD_REQUEST_CODE);

            }
        }

        _activity = this;
        _context = this;
        intializeElements();
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        curru = pref.getString(USER_ID, "");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ServicesOfferedListActivity.this, Categories_bluemarketActivity2.class);
                startActivity(i);
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ServicesOfferedListActivity.this, ActivityAddYourServices.class);
                i.putExtra("bmserv","2");
                startActivity(i);
            }
        });

        if (recycler_view_event != null) {
            recycler_view_event.setHasFixedSize(true);
        }
        mLayoutManager = new LinearLayoutManager(this);
        recycler_view_event.setLayoutManager(mLayoutManager);

        serviceOfferedAdapter = new ServiceOfferedAdapter2(_activity, _context, null, token, curru);
        recycler_view_event.setAdapter(serviceOfferedAdapter);
        serviceOfferedAdapter.notifyDataSetChanged();
        getAllService();
        bellcount();
    }

    private void getAllService() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllService> userCall = service.myService("Bearer " + token);

        userCall.enqueue(new Callback<AllService>() {
            @Override
            public void onResponse(Call<AllService> call, Response<AllService> response) {

                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("onResponse:", "" + response.body().getService_list());
                Log.d("TOKEN:", "" + token);

                serviceOfferedAdapter.updateAllService(response.body().getService_list());
            }

            @Override
            public void onFailure(Call<AllService> call, Throwable t) {

            }
        });
    }

    private void intializeElements() {
        recycler_view_event = (RecyclerView) findViewById(R.id.recycler_view_event);
        navtxt = (MyTextView) findViewById(R.id.navtxt);
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        back = (ImageView) findViewById(R.id.imgNotification2);
        srch = (EditText) findViewById(R.id.srch);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ServicesOfferedListActivity.this, Categories_bluemarketActivity2.class);
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
