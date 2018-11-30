package com.bluebuddy.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.bluebuddy.R;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class MyAccountMenu extends BuddyActivity {

    LinearLayout logout, tripip, tv_reviews, tvproid, tvlistid, tvcont, tvpass, edtpro, cert, tvmyloc, aboutus, allmylist;
    Activity _myActivity;
    MyTextView bell_counter;
    String bellcounter, token;
    ImageView back;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_my_account_menu);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.notice();
        super.setImageResource1(5);
        super.setTitle("MY ACCOUNT");
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        bellcount();
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        logout = (LinearLayout) findViewById(R.id.logout);
        tv_reviews = (LinearLayout) findViewById(R.id.tv_reviews);
        back = (ImageView) findViewById(R.id.imgNotification2);
        tvproid = (LinearLayout) findViewById(R.id.myproid);
        //  aboutus = (LinearLayout) findViewById(R.id.aboutus);
        tvlistid = (LinearLayout) findViewById(R.id.mylisting);
        tvcont = (LinearLayout) findViewById(R.id.mycont);
        tvpass = (LinearLayout) findViewById(R.id.changepass);
        allmylist = (LinearLayout) findViewById(R.id.allmylist);
        cert = (LinearLayout) findViewById(R.id.mycert);
        edtpro = (LinearLayout) findViewById(R.id.edtpro);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hi = new Intent(MyAccountMenu.this, MyProfileActivity.class);
                startActivity(hi);
            }
        });
        allmylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hi = new Intent(MyAccountMenu.this, MyListingActivity.class);
                startActivity(hi);

            }
        });
        /*
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hi = new Intent(MyAccountMenu.this, AboutUsActivity.class);
                startActivity(hi);
            }
        });
        */
        cert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hi = new Intent(MyAccountMenu.this, MyCertificationActivity.class);
                startActivity(hi);
            }
        });
        edtpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jji = new Intent(MyAccountMenu.this, EditProfileActivity.class);
                startActivity(jji);
            }
        });
        tripip = (LinearLayout) findViewById(R.id.idtrip);
        tv_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent jj = new Intent(MyAccountMenu.this, ActivityMyReviews.class);
                startActivity(jj);

            }
        });
        tvproid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jji = new Intent(MyAccountMenu.this, MyProfileActivity.class);
                startActivity(jji);
            }
        });
        tvlistid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iji = new Intent(MyAccountMenu.this, Categories_bluemarketActivity2.class);
                startActivity(iji);
            }
        });
        tvcont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyAccountMenu.this, BuddyContactList.class);
                startActivity(i);
            }
        });
        tripip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ij = new Intent(MyAccountMenu.this, MyEventsActivityNewLay.class);
                startActivity(ij);
            }
        });
        tvpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent io = new Intent(MyAccountMenu.this, ChangePassword.class);
                startActivity(io);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rc1 = "LoginActivity";
                oDialog("Are you sure? You want to logout!", "Yes", "No", true, MyAccountMenu.this, rc1, 0);
            }
        });

    }

    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return;
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }


    private void bellcount() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllNotice> userCall = service.allnotice1("Bearer " + token);

        userCall.enqueue(new Callback<AllNotice>() {
            @Override
            public void onResponse(Call<AllNotice> call, Response<AllNotice> response) {
//                Log.d("STATUS", response.body().getStatus());
//                Log.d("onResponse:", "" + response.body().getNotification_details());
                if (response.body() != null) {
                    if (response.body().getCounter() != null) {
                        bellcounter = response.body().getCounter();
                        if (bellcounter.equals("0")) {
                            bell_counter.setVisibility(View.GONE);
                        }
                        if (!bellcounter.equals("0")) {
                            bell_counter.setVisibility(View.VISIBLE);
                        }
                        bell_counter.setText(response.body().getCounter());
                    }
                }
            }

            @Override
            public void onFailure(Call<AllNotice> call, Throwable t) {

            }
        });

    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog8(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    @Override
    public void onBackPressed() {
        Intent hi = new Intent(MyAccountMenu.this, MyProfileActivity.class);
        startActivity(hi);
    }
}
