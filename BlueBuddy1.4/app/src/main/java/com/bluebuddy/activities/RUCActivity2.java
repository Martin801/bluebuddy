package com.bluebuddy.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bluebuddy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class RUCActivity2 extends BuddyActivity {
    Button btnscuba, btnfreed, Rucbtn, rucaskp;
    public ImageView chk, chk1, back;
    private String token;
    boolean one, two = false;
    private SharedPreferences pref;
    FirebaseAuth auth;
    FirebaseUser user;

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
                Intent i = new Intent(RUCActivity2.this, MyCertificationActivity.class);
                startActivity(i);
            }
        });

        Rucbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (one && two) {
                    Intent i31 = new Intent(RUCActivity2.this, CreateProfileActivityscubadiving2.class);
                    i31.putExtra("both", 1);
                    startActivity(i31);
                } else if (one && !two) {
                    Intent i32 = new Intent(RUCActivity2.this, CreateProfileActivityscubadiving2.class);

                    startActivity(i32);
                } else if (!one && two) {
                    Intent i33 = new Intent(RUCActivity2.this, CreateProfileActivityfreediving2.class);
                    startActivity(i33);
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

    public void rucaskp() {
        Intent jj = new Intent(RUCActivity2.this, MyCertificationActivity.class);

        startActivity(jj);
    }

    protected void setMyPref(String key, String value) {
        super.setPref(key, value);
    }

    @Override
    public void onBackPressed() {

    }
}
