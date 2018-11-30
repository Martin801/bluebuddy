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
import android.widget.ImageView;

import com.bluebuddy.R;
import com.bluebuddy.adapters.MyReviewAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.AllReview;
import com.bluebuddy.models.MyReviews;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class ActivityMyReviews extends BuddyActivity {

    private RecyclerView mRecyclerView2;
    ImageView back;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MyReviews> REVIEWList;
    private MyReviewAdapter myReviewAdapter;
    Activity _activity;
    Context _context;
    private SharedPreferences pref;
    private String token, bellcounter;
    MyTextView bell_counter;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_my_reviews);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.setImageResource1(5);
        super.notice();
        super.loader();

        super.setTitle("MY REVIEWS");
        _activity = this;
        _context = this;

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        back = (ImageView) findViewById(R.id.imgNotification2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityMyReviews.this, MyAccountMenu.class);
                startActivity(i);
            }
        });

        mRecyclerView2 = (RecyclerView) findViewById(R.id.recycler_view6);

        if (mRecyclerView2 != null) {
            mRecyclerView2.setHasFixedSize(true);
        }
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView2.setLayoutManager(mLayoutManager);

        myReviewAdapter = new MyReviewAdapter(_activity, _context, null, token);
        mRecyclerView2.setAdapter(myReviewAdapter);
        myReviewAdapter.notifyDataSetChanged();
        getAllReview();
        bellcount();
    }

    public void getAllReview() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllReview> userCall = service.allReview("Bearer " + token);

        userCall.enqueue(new Callback<AllReview>() {
            @Override
            public void onResponse(Call<AllReview> call, Response<AllReview> response) {

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("onResponse:", "" + response.body().getDetails());
                Log.d("TOKEN:", "" + token);

                myReviewAdapter.updateAllReview(response.body().getDetails());
            }

            @Override
            public void onFailure(Call<AllReview> call, Throwable t) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
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
                if(bellcounter!=null) {
                if (bellcounter.equals("0")) {
                    bell_counter.setVisibility(View.GONE);
                } else if (!bellcounter.equals("0")) {
                    bell_counter.setVisibility(View.VISIBLE);
                }
                bell_counter.setText(response.body().getCounter());
            }

            }

            @Override
            public void onFailure(Call<AllNotice> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ActivityMyReviews.this, MyAccountMenu.class);
        startActivity(i);
    }

}

