package com.bluebuddy.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.bluebuddy.R;

import java.util.ArrayList;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class ActivityFilterCourses2 extends BuddyActivity {
    Button btnscuba, btnfreed, Rucbtn, rucaskp, other;
    public ImageView chk, chk1, chk3, back;
    private SharedPreferences pref;
    private String token, category;
    private ArrayList<String> categorieList;
    boolean one, two, three = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_filter_course);
        super.onCreate(savedInstanceState);


        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");

        categorieList = new ArrayList<String>();


        Rucbtn = (Button) findViewById(R.id.rucbtnid);
        btnscuba = (Button) findViewById(R.id.sdid);
        other = (Button) findViewById(R.id.other);
        btnfreed = (Button) findViewById(R.id.fdid);
        chk = (ImageView) findViewById(R.id.chk);
        chk1 = (ImageView) findViewById(R.id.chk1);
        chk3 = (ImageView) findViewById(R.id.chk3);
        rucaskp = (Button) findViewById(R.id.skp);

        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String joined = TextUtils.join(",", categorieList);
                Intent i = new Intent(ActivityFilterCourses2.this, Categories_bluemarketActivity2.class);
                i.putExtra("category", joined);
                startActivity(i);
            }
        });

        Rucbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String joined = TextUtils.join(",", categorieList);
                Intent i31 = new Intent(ActivityFilterCourses2.this, BlueMarketActivity32.class);
                i31.putExtra("category", joined);
                startActivity(i31);
            }
        });

        btnfreed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (two) {
                    chk1.setImageResource(R.drawable.unchecked);
                    two = false;
                    addData("free", "sub");
                } else {
                    chk1.setImageResource(R.drawable.checked);
                    two = true;
                    addData("free", "add");
                }
            }
        });

        btnscuba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (one) {
                    chk.setImageResource(R.drawable.unchecked);
                    one = false;
                    addData("scuba", "sub");
                } else {
                    chk.setImageResource(R.drawable.checked);
                    one = true;
                    addData("scuba", "add");
                }
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (three) {
                    chk3.setImageResource(R.drawable.unchecked);
                    three = false;
                    addData("others", "sub");
                } else {
                    chk3.setImageResource(R.drawable.checked);
                    three = true;
                    addData("others", "add");
                }
            }
        });
    }

    private void addData(String c, String type) {
        if (categorieList.size() > 0) {
            for (int i = 0; i < categorieList.size(); i++) {
                if (categorieList.get(i).equals(c)) {
                    categorieList.remove(i);
                    i--;
                }
            }
        }

        if (type == "add")
            categorieList.add(c);
    }

    @Override
    public void onBackPressed() {
        String joined = TextUtils.join(",", categorieList);
        Intent i = new Intent(ActivityFilterCourses2.this, Categories_bluemarketActivity2.class);
        i.putExtra("category", joined);
        startActivity(i);
    }

}
