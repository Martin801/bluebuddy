package com.bluebuddy.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bluebuddy.R;
import com.bluebuddy.adapters.BuddyContactListAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.BuddyResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class BuddyContactList extends BuddyActivity {
    private RecyclerView recycler_view_event;
    private BuddyContactListAdapter buddyContactListAdapter;
    private Activity _activity;
    private Context _context;
    private SharedPreferences pref;
    private String token, bellcounter;
    private LinearLayoutManager mLayoutManager;
    private ImageView back;
    private MyTextView navtxt;
    private EditText srch;
    MyTextView bell_counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide();
            slide.setSlideEdge(Gravity.RIGHT);
            getWindow().setEnterTransition(slide);
        }*/
        super.setLayout(R.layout.activity_buddy_contact_list);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.notice();
        super.setImageResource1(5);
        super.setTitle("BUDDIES");
        _activity = this;
        _context = this;
        intializeElements();
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");

        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BuddyContactList.this, MyAccountMenu.class);
                startActivity(i);
            }
        });
        if (recycler_view_event != null) {
            recycler_view_event.setHasFixedSize(true);
        }
        mLayoutManager = new LinearLayoutManager(this);
        recycler_view_event.setLayoutManager(mLayoutManager);
        getAllPeople();
        bellcount();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(BuddyContactList.this, MyAccountMenu.class);
        startActivity(i);
    }

    private void intializeElements() {
        recycler_view_event = (RecyclerView) findViewById(R.id.recycler_view_event);
        navtxt = (MyTextView) findViewById(R.id.navtxt);
        back = (ImageView) findViewById(R.id.imgNotification2);
        srch = (EditText) findViewById(R.id.srch);
    }

    private void getAllPeople() {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<BuddyResponse> userCall = service.buddyList("Bearer " + token);
        userCall.enqueue(new Callback<BuddyResponse>() {
            @Override
            public void onResponse(Call<BuddyResponse> call, Response<BuddyResponse> response) {

                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("TOKEN:", "" + token);


                buddyContactListAdapter = new BuddyContactListAdapter(_activity, _context, response.body().getDetails(), token);
                recycler_view_event.setAdapter(buddyContactListAdapter);
                buddyContactListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<BuddyResponse> call, Throwable t) {
            }
        });
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
