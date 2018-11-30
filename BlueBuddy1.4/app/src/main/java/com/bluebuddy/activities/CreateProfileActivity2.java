package com.bluebuddy.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bluebuddy.R;
import com.bluebuddy.Utilities.Utility;
import com.bluebuddy.adapters.InterestAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.Interest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class CreateProfileActivity2 extends BuddyActivity {

    private static final int PERIOD = 2000;
    int pass = 0;
    MyTextView bell_counter;
    ImageView back;
    int backFlag = 0;
    private Context context;
    private Activity _myActivity;
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recylerViewLayoutManager;
    private Button btnchsyint;
    private boolean doubleBackToExitPressedOnce;
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };
    private Handler mHandler = new Handler();
    private long lastPressedTime;
    private String token, bellcounter;
    private SharedPreferences pref;

    /*private String[] interests = {
            "Free Diving",
            "Scuba Diving",
            "Spear Fishing",
            "Photography",
            "Fishing",
            "Snorkeling",
            "Kayaking",
            "Others"
    };*/

    private ArrayList<Interest> interestList = new ArrayList<Interest>();

    private String[] interests_products = {
            "Scuba Diving",
            "Freediving",
            "Fishing",
            "Spearfishing",
            "Boats",
            "Others"
    };

    private boolean isChecked = false;

    private boolean checkInterst() {
        isChecked = false;

        for (int i = 0; i < interestList.size(); i++) {
            if (interestList.get(i).getImgInterest()) {
                isChecked = true;
            }
        }

        return isChecked;
    }

    private String getInterst() {
        String interest = "";

        for (int i = 0; i < interestList.size(); i++) {
            if (interestList.get(i).getImgInterest()) {
                interest += interestList.get(i).getInterestName() + ",";
            }
        }

        return interest;
    }

    private void generateDummyData() {
        for (int i = 0; i < Utility.interests.length; i++) {
            Interest in = new Interest(false, Utility.interests[i]);
            interestList.add(in);
        }
    }

    private void generateDummyData_products() {
        for (int i = 0; i < interests_products.length; i++) {
            Interest in = new Interest(false, interests_products[i]);
            interestList.add(in);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);

        super.setLayout(R.layout.activity_create_profile2);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.notice();
        _myActivity = this;
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        bellcount();
        context = getApplicationContext();
        final Bundle bundle = getIntent().getExtras();
        pass = bundle.getInt("pass");

        if (pass == 2) {
            super.setImageResource1(1);
            super.setTitle("TRIPS");
        } else if (pass == 1) {
            super.setImageResource1(3);
            super.setTitle("BLUE MARKET PRODUCTS");
        }


        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        linearLayout = (LinearLayout) findViewById(R.id.lview1);
        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        btnchsyint = (Button) findViewById(R.id.chsid);
        if (pass == 2) {
            generateDummyData();
        } else if (pass == 1) {
            generateDummyData_products();
        }

        recylerViewLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(recylerViewLayoutManager);
        recyclerViewAdapter = new InterestAdapter(context, _myActivity, interestList, false);
        recyclerView.setAdapter(recyclerViewAdapter);
        btnchsyint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkInterst()) {

                    oDialog("Please select atleast one interest", "Close", "", false, _myActivity, "", 0);

                } else {

                    String interestList = "";
                    String interestList2 = "";
                    //  Log.d("eventTypeList", getInterst());

                    if (getInterst().contains(",")) {
                        interestList = getInterst();
                        interestList2 = interestList.substring(0, interestList.length() - 1);
                    }

                    if (pass == 2) {
                        Intent ii = new Intent(CreateProfileActivity2.this, AllEventsActivityNew.class);
                        ii.putExtra("interestList", interestList);
                        ii.putExtra("pass", pass);
                        startActivity(ii);
                    } else if (pass == 1) {
                        Intent ii = new Intent(CreateProfileActivity2.this, BlueMarketProductActivityNew.class);
                        ii.putExtra("category", interestList2);
                        ii.putExtra("pass", pass);
                        startActivity(ii);
                    }

                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    public void onBackPressed() {
        if (pass == 2) {
            Intent i = new Intent(CreateProfileActivity2.this, MyProfileActivity.class);
            startActivity(i);
        } else if (pass == 1) {
            Intent i = new Intent(CreateProfileActivity2.this, Categories_bluemarketActivity.class);
            startActivity(i);
        }
        finish();
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    private void bellcount() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllNotice> userCall = service.allnotice1("Bearer " + token);

        userCall.enqueue(new Callback<AllNotice>() {
            @Override
            public void onResponse(Call<AllNotice> call, Response<AllNotice> response) {
                bellcounter = response.body().getCounter();
                if (bellcounter != null) {
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
}
