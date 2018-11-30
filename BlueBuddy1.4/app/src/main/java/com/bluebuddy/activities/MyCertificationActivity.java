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
import android.widget.Button;
import android.widget.ImageView;

import com.bluebuddy.R;
import com.bluebuddy.adapters.CertAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AfterLoginStatus;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.CertificateDetail;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class MyCertificationActivity extends BuddyActivity {
    FloatingActionButton fab;
    String rc, edtrc;
    MyTextView bell_counter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageView back;
    private Button fab1;
    private MyTextView validationmsg;
    private CertAdapter certAdapter;
    private Activity _activity;
    private Context _context;
    private SharedPreferences pref;
    private String token, category, cat, bellcounter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_mycertification);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.notice();
        super.setImageResource1(5);
        super.setTitle("CERTIFICATION DETAILS");
        super.loader();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;
        _activity = this;
        _context = this;

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        getAllCertificate();
        bellcount();
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);

        fab1 = (Button) findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                try {
                    intent = new Intent(MyCertificationActivity.this, Class.forName("com.bluebuddy.activities." + rc));
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        back = (ImageView) findViewById(R.id.imgNotification2);
        validationmsg = (MyTextView) findViewById(R.id.validationmsg);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                try {
                    intent = new Intent(MyCertificationActivity.this, Class.forName("com.bluebuddy.activities." + rc));
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });


        mRecyclerView = (RecyclerView) findViewById(R.id.blue_market_recycler);

        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
        }

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        certAdapter = new CertAdapter(_activity, _context, null, token);
        mRecyclerView.setAdapter(certAdapter);
        certAdapter.notifyDataSetChanged();
    }


    public void oDialog(final String msg, final String btnText, final String btnText2, final String btnText3, final String btnText4, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog4(msg, btnText, btnText2, btnText3, btnText4, _redirect, activity, _redirectClass, layout);
    }

    public void getAllCertificate() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AfterLoginStatus> userCall = service.myCertificate("Bearer " + token);

        userCall.enqueue(new Callback<AfterLoginStatus>() {
            @Override
            public void onResponse(Call<AfterLoginStatus> call, Response<AfterLoginStatus> response) {

                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("TOKEN:", "" + token);

                if (response.body().getStatus() == true && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    certAdapter.updateAllCert(response.body().getCertification_details());
                    ArrayList<CertificateDetail> certificateDetails = response.body().getCertification_details();
                    int len = certificateDetails.size();
                    if (len == 2) {
                        fab1.setVisibility(View.GONE);
                        validationmsg.setVisibility(View.GONE);
                        fab.setVisibility(View.GONE);
                    } else if (len == 1) {
                        fab1.setVisibility(View.GONE);
                        validationmsg.setVisibility(View.GONE);
                        if (certificateDetails.get(0).getCert_type().equals("Scuba_Diving")) {
                            fab.setVisibility(View.VISIBLE);
                            rc = "CreateProfileActivityfreediving2";

                        } else if (certificateDetails.get(0).getCert_type().equals("Free_Diving")) {
                            fab.setVisibility(View.VISIBLE);
                            rc = "CreateProfileActivityscubadiving2";

                        }
                    } else if (len == 0) {
                        fab1.setVisibility(View.VISIBLE);
                        fab.setVisibility(View.GONE);
                        rc = "RUCActivity2";
                        validationmsg.setVisibility(View.VISIBLE);
                        validationmsg.setText("You have not entered any certification yet!");
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<AfterLoginStatus> call, Throwable t) {
                t.printStackTrace();
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

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }
}
