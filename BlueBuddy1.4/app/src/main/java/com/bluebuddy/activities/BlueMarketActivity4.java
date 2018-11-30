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
import com.bluebuddy.adapters.BlueMarketProductAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.AllProduct;
import com.bluebuddy.models.AllProductDetails;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.USER_ID;

public class BlueMarketActivity4 extends BuddyActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<AllProductDetails> BMCList;
    private BlueMarketProductAdapter blueMarketProductAdapter;
    private Activity _activity;
    private Context _context;
    private ImageView back;
    private MyTextView bell_counter;
    private SharedPreferences pref;
    private String token, category, curru, bellcounter;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_blue_market4);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.notice();
        super.setTitle("PRODUCTS");

        _activity = this;
        _context = this;
        Bundle b = getIntent().getExtras();

        if (b != null) {
            category = b.getString("interestList");
        }

        super.loader();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        curru = pref.getString(USER_ID, "");
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BlueMarketActivity4.this, CreateProfileActivity2.class);
                i.putExtra("pass", 1);
                startActivity(i);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(BlueMarketActivity4.this, BlueMarketPicActivity.class);
                startActivity(intent);

            }
        });


        mRecyclerView = (RecyclerView) findViewById(R.id.blue_market_recycler);
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
        }
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        blueMarketProductAdapter = new BlueMarketProductAdapter(_activity, _context, null, token, category, curru);
        mRecyclerView.setAdapter(blueMarketProductAdapter);
        blueMarketProductAdapter.notifyDataSetChanged();
        getAllProduct();
        bellcount();
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final String btnText3, final String btnText4, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog4(msg, btnText, btnText2, btnText3, btnText4, _redirect, activity, _redirectClass, layout);
    }

    public void getAllProduct() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllProduct> userCall = service.allProduct("Bearer " + token, category);

        userCall.enqueue(new Callback<AllProduct>() {
            @Override
            public void onResponse(Call<AllProduct> call, Response<AllProduct> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("onResponse:", "" + response.body().getProduct_list());
                Log.d("TOKEN:", "" + token);

                blueMarketProductAdapter.updateAllProduct(response.body().getProduct_list());
            }

            @Override
            public void onFailure(Call<AllProduct> call, Throwable t) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(BlueMarketActivity4.this, CreateProfileActivity2.class);
        i.putExtra("pass", 1);
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
                bell_counter.setText(response.body().getCounter());
                if (bellcounter.equals("0")) {
                    bell_counter.setVisibility(View.GONE);
                } else if (!bellcounter.equals("0")) {
                    bell_counter.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<AllNotice> call, Throwable t) {

            }
        });
    }
}
