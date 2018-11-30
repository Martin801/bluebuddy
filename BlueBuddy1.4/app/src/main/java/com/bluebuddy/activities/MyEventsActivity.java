package com.bluebuddy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.bluebuddy.R;
import com.bluebuddy.adapters.MyEventAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.MyEventItems;
import com.bluebuddy.models.Trip;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class MyEventsActivity extends BuddyActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout create_event;
    private ScrollView card_vieww;
    private Button bt;
    private ImageView back;
    private ArrayList<MyEventItems> EVENTList22;
    private MyEventAdapter myEventAdapter;
    private Activity _activity;
    private Context _context;
    private SharedPreferences pref;
    private String token;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.my_events);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.loader();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;

        super.setTitle("MY TRIPS");
        _activity = this;
        _context = this;
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        bt = (Button) findViewById(R.id.Buddyupid);

        create_event = (LinearLayout) findViewById(R.id.create_event);
        card_vieww = (ScrollView) findViewById(R.id.card_vieww);
        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyEventsActivity.this, MyAccountMenu.class);
                startActivity(i);
            }
        });


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_event22);
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
        }
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        myEventAdapter = new MyEventAdapter(_activity, _context, null);
        mRecyclerView.setAdapter(myEventAdapter);
        myEventAdapter.notifyDataSetChanged();
        getMyTrip();

    }

    public void btnBup(View view) {
        Intent intent = new Intent(MyEventsActivity.this, CreateEventActivity3.class);
        startActivity(intent);
    }

    public void getMyTrip() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Trip> userCall = service.trip("Bearer " + token);

        userCall.enqueue(new Callback<Trip>() {
            @Override
            public void onResponse(Call<Trip> call, Response<Trip> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("onResponse:", "" + response.body().getDetails());
                if (response.body().getDetails() == null) {
                    mRecyclerView.setVisibility(View.GONE);
                    create_event.setVisibility(View.GONE);
                    card_vieww.setVisibility(View.VISIBLE);


                } else {
                    create_event.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    card_vieww.setVisibility(View.GONE);
                }

                myEventAdapter.updateMyTrip(response.body().getDetails());
            }

            @Override
            public void onFailure(Call<Trip> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(MyEventsActivity.this, MyAccountMenu.class);
        startActivity(i);
    }
}
