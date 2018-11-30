package com.bluebuddy.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bluebuddy.R;

import java.util.ArrayList;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class ActivityFilterCourses extends BuddyActivity {
    private static final int RECORD_REQUEST_CODE = 101;
    public ImageView chk, chk1, chk3, back;
    Button btnscuba, btnfreed, Rucbtn, rucaskp, other, minus_level1;
    boolean one, two, three = false;
    EditText certlvlid1;
    String othstr;
    boolean isCurrentLocationGranted = false;
    private SharedPreferences pref;
    private String token, category;
    private ArrayList<String> categorieList;
    private LinearLayout newlvl1;
    private View certlvlid1_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_filter_course);
        super.onCreate(savedInstanceState);
        super.notice();
        super.setTitle("COURSES");

        permissionCheckforGps();

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
        newlvl1 = (LinearLayout) findViewById(R.id.newlvl1);
        certlvlid1_view = (View) findViewById(R.id.certlvlid1_view);
        certlvlid1 = (EditText) findViewById(R.id.certlvlid123);

        minus_level1 = (Button) findViewById(R.id.minus_level1);
        minus_level1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (three == true) {
                    chk3.setImageResource(R.drawable.unchecked);
                    three = false;
                }
                newlvl1.setVisibility(View.GONE);
                certlvlid1_view.setVisibility(View.GONE);
                certlvlid1.setText("");

            }
        });

        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        Rucbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCurrentLocationGranted) {
                    String joined = TextUtils.join(",", categorieList);

                    Intent i31 = new Intent(ActivityFilterCourses.this, BlueMarketCourseActivityNew.class);
                    i31.putExtra("category", joined);
                    startActivity(i31);
                } else {
                    permissionCheckforGps();
                }
            }
        });

        btnfreed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (two) {
                    chk1.setImageResource(R.drawable.unchecked);
                    two = false;
                    addData("Free Diving", "sub");
                } else {
                    chk1.setImageResource(R.drawable.checked);
                    two = true;
                    addData("Free Diving", "add");
                }
            }
        });

        btnscuba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (one) {
                    chk.setImageResource(R.drawable.unchecked);
                    one = false;

                    addData("Scuba Diving", "sub");
                } else {
                    chk.setImageResource(R.drawable.checked);
                    one = true;
                    addData("Scuba Diving", "add");

                }
            }
        });

        certlvlid1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addOtherData(s.toString(), "add");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (three) {
                    chk3.setImageResource(R.drawable.unchecked);
                    three = false;
                    addData("Others", "sub");

                } else {
                    chk3.setImageResource(R.drawable.checked);
                    three = true;
                    addData("Others", "add");


                }
            }
        });
    }

    void permissionCheckforGps() {
        int permission = ContextCompat.checkSelfPermission(ActivityFilterCourses.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //  Log.i("HELLO", "Permission to location denied");
            isCurrentLocationGranted = false;
            if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityFilterCourses.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityFilterCourses.this);
                builder.setMessage("Permission to access the location is required for this app to access your location.")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("HELLO", "Clicked");

                        ActivityCompat.requestPermissions(ActivityFilterCourses.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RECORD_REQUEST_CODE);


                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                ActivityCompat.requestPermissions(ActivityFilterCourses.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RECORD_REQUEST_CODE);

            }
        } else {
            isCurrentLocationGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RECORD_REQUEST_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isCurrentLocationGranted = true;

                } else {
                    isCurrentLocationGranted = false;
                }
            }
        }
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

    private void addOtherData(String c, String type) {
        if (categorieList.size() > 0) {
            for (int i = 0; i < categorieList.size(); i++) {
                if (!categorieList.get(i).equals("scuba") && !categorieList.get(i).equals("free")) {
                    categorieList.remove(i);
                    i--;
                }
            }
        }

        if (type == "add" && !c.equals(""))
            categorieList.add(c);
    }

    @Override
    public void onBackPressed() {
//        String joined = TextUtils.join(",", categorieList);
//        Intent i = new Intent(ActivityFilterCourses.this, Categories_bluemarketActivity.class);
////        i.putExtra("category", joined);
//        startActivity(i);
        finish();
    }
}

