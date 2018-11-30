package com.bluebuddy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bluebuddy.R;
import com.bluebuddy.adapters.BlueMarketCoursesAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllCourse;
import com.bluebuddy.models.AllCourseDetails;
import com.bluebuddy.models.AllNotice;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.USER_ID;

public class BlueMarketActivity3 extends BuddyActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageView back;
    private ArrayList<AllCourseDetails> BMCList;
    private BlueMarketCoursesAdapter blueMarketCoursesAdapter;
    private Activity _activity;
    private Context _context;
    private SharedPreferences pref;
    private MyTextView bell_counter;
    private String token, category, cat, curru, bellcounter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_blue_market3);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.notice();
        super.setTitle("COURSES");

        _activity = this;
        _context = this;

        super.loader();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        curru = pref.getString(USER_ID, "");

        Bundle b = getIntent().getExtras();
        category = b.getString("category");
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BlueMarketActivity3.this, Categories_bluemarketActivity.class);
                startActivity(i);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BlueMarketActivity3.this, BlueMarketCoursePicActivity.class);
                intent.putExtra("category", category);
                startActivity(intent);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.blue_market_recycler);

        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
        }

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        blueMarketCoursesAdapter = new BlueMarketCoursesAdapter(_activity, _context, null, token, category, curru);
        mRecyclerView.setAdapter(blueMarketCoursesAdapter);
        blueMarketCoursesAdapter.notifyDataSetChanged();

        getAllCourses(category);
        bellcount();
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final String btnText3, final String btnText4, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog4(msg, btnText, btnText2, btnText3, btnText4, _redirect, activity, _redirectClass, layout);
    }

    public void getAllCourses(String category) {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllCourse> userCall = service.allCourse("Bearer " + token, category);

        userCall.enqueue(new Callback<AllCourse>() {
            @Override
            public void onResponse(Call<AllCourse> call, Response<AllCourse> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("onResponse:", "" + response.body().getCourse_list());
                Log.d("TOKEN:", "" + token);

                blueMarketCoursesAdapter.updateAllCourses(response.body().getCourse_list());
            }

            @Override
            public void onFailure(Call<AllCourse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(BlueMarketActivity3.this, Categories_bluemarketActivity.class);
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
