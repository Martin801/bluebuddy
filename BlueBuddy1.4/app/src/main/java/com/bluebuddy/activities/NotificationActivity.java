package com.bluebuddy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bluebuddy.R;
import com.bluebuddy.adapters.AllNoticeAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.NoticeDetails;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class NotificationActivity extends BuddyActivity {

    MyTextView bell_counter, txtshid1;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<NoticeDetails> EVENTList;
    private AllNoticeAdapter allNoticeAdapter;
    private Activity _activity;
    private Context _context;
    private SharedPreferences pref;
    private String token, bellcounter;
    private ProgressDialog mProgressDialog;
    private Parcelable recyclerViewState;
    private ImageView imgNotification2;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_notification);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.loader();
        super.notice();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;

        _activity = this;
        _context = this;

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");

        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        txtshid1 = (MyTextView) findViewById(R.id.txtshid1);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_event);

        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent i = new Intent(AllEventsActivityNew.this, CreateProfileActivity2.class);
//                i.putExtra("pass", 2);
//                startActivity(i);

                onBackPressed();
            }
        });

        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
        }

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        allNoticeAdapter = new AllNoticeAdapter(_activity, _context, null, token, pref);
        mRecyclerView.setAdapter(allNoticeAdapter);
        allNoticeAdapter.notifyDataSetChanged();
        getAllNotice();
        bellcount();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        pref = super.getPref();
//        token = pref.getString(ACCESS_TOKEN, "");
//
//        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_event);
//
//        if (mRecyclerView != null) {
//            mRecyclerView.setHasFixedSize(true);
//        }
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        allNoticeAdapter = new AllNoticeAdapter(_activity, _context, null, token, pref);
//        mRecyclerView.setAdapter(allNoticeAdapter);
//        allNoticeAdapter.notifyDataSetChanged();
        getAllNotice();
        bellcount();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        pref = super.getPref();
//        token = pref.getString(ACCESS_TOKEN, "");
//
//        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_event);
//
//        if (mRecyclerView != null) {
//            mRecyclerView.setHasFixedSize(true);
//        }
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        allNoticeAdapter = new AllNoticeAdapter(_activity, _context, null, token, pref);
//        mRecyclerView.setAdapter(allNoticeAdapter);
//        allNoticeAdapter.notifyDataSetChanged();
//        getAllNotice();
    }

    public void getAllNotice() {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllNotice> userCall = service.allnotice("Bearer " + token);

        userCall.enqueue(new Callback<AllNotice>() {
            @Override
            public void onResponse(Call<AllNotice> call, Response<AllNotice> response) {

                //   Log.d("STATUS", String.valueOf(response.body().getStatus()) );
                //  Log.d("onResponse:", "" + response.body().getNotification_details());

                if (response.body().getNotification_details().size() > 0 && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    txtshid1.setVisibility(View.GONE);
                    allNoticeAdapter.updateAllNotice(response.body().getNotification_details());
                } else if (response.body().getNotification_details().size() <= 0 && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    txtshid1.setVisibility(View.VISIBLE);
                    txtshid1.setText("No notifications found.");
                }

            }

            @Override
            public void onFailure(Call<AllNotice> call, Throwable t) {

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
                }
                if (!bellcounter.equals("0")) {
                    bell_counter.setVisibility(View.VISIBLE);
                }

                bell_counter.setText(response.body().getCounter());

            }

            @Override
            public void onFailure(Call<AllNotice> call, Throwable t) {

            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
