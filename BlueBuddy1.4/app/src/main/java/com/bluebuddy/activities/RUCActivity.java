package com.bluebuddy.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.bluebuddy.R;

import static com.bluebuddy.app.AppConfig.ACCESS_STEP;
import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class RUCActivity extends BuddyActivity {

    public ImageView chk, chk1, back;
    Button btnscuba, btnfreed, Rucbtn, rucaskp;
    boolean one, two = false;
    FirebaseAuth auth;
    FirebaseUser user;
    private String token;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.setLayout(R.layout.activity_ruc);
        super.onCreate(savedInstanceState);
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        Rucbtn = (Button) findViewById(R.id.rucbtnid);
        btnscuba = (Button) findViewById(R.id.sdid);
        btnfreed = (Button) findViewById(R.id.fdid);
        chk = (ImageView) findViewById(R.id.chk);
        chk1 = (ImageView) findViewById(R.id.chk1);
        rucaskp = (Button) findViewById(R.id.skp);

        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setMyPref(ACCESS_STEP, "1");
//                Intent i = new Intent(RUCActivity.this, CreateProfileActivity1.class);
//                startActivity(i);
                onBackPressed();
            }
        });

        Rucbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setMyPref(ACCESS_STEP, "3");
                if (one && two) {
                    Intent i31 = new Intent(RUCActivity.this, CreateProfileActivityscubadiving.class);
                    i31.putExtra("both", 1);
                    startActivity(i31);
                } else if (one && !two) {
                    Intent i32 = new Intent(RUCActivity.this, CreateProfileActivityscubadiving.class);
                    startActivity(i32);
                } else if (!one && two) {
                    Intent i33 = new Intent(RUCActivity.this, CreateProfileActivityfreediving.class);
                    startActivity(i33);
                } else if (!one && !two) {
                    oDialog("Please select your certification!", "Close", "", false, RUCActivity.this, "", 0);
                }

            }
        });

        rucaskp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rucaskp();
            }
        });

        btnfreed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (two) {
                    chk1.setImageResource(R.drawable.unchecked);
                    two = false;
                } else {
                    chk1.setImageResource(R.drawable.checked);
                    two = true;
                }
            }
        });

        btnscuba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (one) {
                    chk.setImageResource(R.drawable.unchecked);
                    one = false;
                } else {
                    chk.setImageResource(R.drawable.checked);
                    one = true;
                }
            }
        });

    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    public void rucaskp() {

        setMyPref(ACCESS_STEP, "4");
        Intent jj = new Intent(RUCActivity.this, CreateProfileActivity4.class);
        startActivity(jj);

    }

    protected void setMyPref(String key, String value) {
        super.setPref(key, value);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        setMyPref(ACCESS_STEP, "1");
//        Intent i = new Intent(RUCActivity.this, CreateProfileActivity1.class);
//
//        startActivity(i);

        finish();
    }
}
