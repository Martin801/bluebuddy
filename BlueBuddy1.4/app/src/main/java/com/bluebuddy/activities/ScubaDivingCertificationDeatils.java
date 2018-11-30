package com.bluebuddy.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluebuddy.R;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class ScubaDivingCertificationDeatils extends BuddyActivity {
    EditText namecrt, crtnum, certlvlid1, certlvlid2, certlvlid, certlvlid33, certlvlid22;
    private TextView txtv2;
    Button nxt;
    LinearLayout newlvl1, newlvl2, crtdt22, crtdt33;
    Activity _myActivity;
    ImageView back;
    View certlvlid1_view, certlvlid2_view;
    private SharedPreferences pref;
    private String token, certtype, certname, certnum, bckey, certlvl, certid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.scubacerdetails);
        super.onCreate(savedInstanceState);
        this.initializeElements();
        _myActivity = this;
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");

        Bundle b = getIntent().getExtras();

        certtype = b.getString("cert_type");
        certid = b.getString("cert_id");
        certname = b.getString("cert_name");
        certnum = b.getString("cert_num");
        certlvl = b.getString("cert_lvl");
        bckey = b.getString("backkey");
        getAllCertificatefree();
    }

    private void initializeElements() {
        certlvlid33 = (EditText) findViewById(R.id.certlvlid33);
        certlvlid22 = (EditText) findViewById(R.id.certlvlid22);
        crtdt22 = (LinearLayout) findViewById(R.id.crtdt22);
        crtdt33 = (LinearLayout) findViewById(R.id.crtdt33);
        txtv2 = (TextView) findViewById(R.id.txtv2);
        namecrt = (EditText) findViewById(R.id.certnameid);
        certlvlid1 = (EditText) findViewById(R.id.certlvlid1);
        newlvl1 = (LinearLayout) findViewById(R.id.newlvl1);
        certlvlid1_view = (View) findViewById(R.id.certlvlid1_view);
        newlvl2 = (LinearLayout) findViewById(R.id.newlvl2);
        certlvlid2 = (EditText) findViewById(R.id.certlvlid2);
        certlvlid2_view = (View) findViewById(R.id.certlvlid2_view);
        certlvlid = (EditText) findViewById(R.id.certlvlid);
        crtnum = (EditText) findViewById(R.id.certnumid);
        nxt = (Button) findViewById(R.id.certdnxtid);
        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bckey.equals("peopleprofile")) {
                    Bundle b = getIntent().getExtras();
                    String uid = b.getString("uid");
                    Intent i = new Intent(ScubaDivingCertificationDeatils.this, PeopleProfileActivity.class);
                    i.putExtra("uid", uid);
                    startActivity(i);
                } else if (bckey.equals("myprofile")) {

                    Intent i = new Intent(ScubaDivingCertificationDeatils.this, MyProfileActivity.class);
                    startActivity(i);
                }
            }
        });

        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bckey.equals("peopleprofile")) {
                    Bundle b = getIntent().getExtras();
                    String uid = b.getString("uid");
                    Intent i = new Intent(ScubaDivingCertificationDeatils.this, PeopleProfileActivity.class);
                    i.putExtra("uid", uid);
                    startActivity(i);
                } else if (bckey.equals("myprofile")) {
                    Intent i = new Intent(ScubaDivingCertificationDeatils.this, MyProfileActivity.class);
                    startActivity(i);
                }
            }
        });

    }


    private void getAllCertificatefree() {
        if (certtype.equals("Scuba_Diving")) {
            txtv2.setText("Scuba diving");
        } else if (certtype.equals("Free_Diving")) {
            txtv2.setText("Freediving");
        }
        namecrt.setText(certname);
        namecrt.setKeyListener(null);
        namecrt.setCursorVisible(false);
        namecrt.setPressed(false);
        namecrt.setFocusable(false);

        crtnum.setText(certnum);
        crtnum.setKeyListener(null);
        crtnum.setCursorVisible(false);
        crtnum.setPressed(false);
        crtnum.setFocusable(false);

        certlvlid.setKeyListener(null);
        certlvlid.setCursorVisible(false);
        certlvlid.setPressed(false);
        certlvlid.setFocusable(false);

        certlvlid22.setKeyListener(null);
        certlvlid22.setCursorVisible(false);
        certlvlid22.setPressed(false);
        certlvlid22.setFocusable(false);

        certlvlid33.setKeyListener(null);
        certlvlid33.setCursorVisible(false);
        certlvlid33.setPressed(false);
        certlvlid33.setFocusable(false);


        if (certlvl.contains(":")) {
            String[] separated = certlvl.split(":");
            if (separated.length == 0) {
                separated[0] = separated[0].trim();
                certlvlid.setText(separated[0]);
                Log.d("lvl 1", separated[0]);
            } else if (separated.length == 1) {
                separated[0] = separated[0].trim();
                separated[1] = separated[1].trim();
                certlvlid.setText(separated[0]);
                certlvlid22.setText(separated[1]);
                Log.d("lvl 1", separated[0]);
                Log.d("lvl 2", separated[1]);
            } else if (separated.length == 2) {
                crtdt22.setVisibility(View.VISIBLE);
                separated[0] = separated[0].trim();
                separated[1] = separated[1].trim();
                certlvlid.setText(separated[0]);
                certlvlid22.setText(separated[1]);
                Log.d("lvl 1", separated[0]);
                Log.d("lvl 2", separated[1]);

            } else if (separated.length == 3) {
                crtdt22.setVisibility(View.VISIBLE);
                crtdt33.setVisibility(View.VISIBLE);
                separated[0] = separated[0].trim();
                separated[1] = separated[1].trim();
                separated[2] = separated[2].trim();
                certlvlid.setText(separated[0]);
                certlvlid22.setText(separated[1]);
                certlvlid33.setText(separated[2]);
                Log.d("lvl 1", separated[0]);
                Log.d("lvl 2", separated[1]);
                Log.d("lvl 3", separated[2]);
            }
        } else if (!certlvl.contains(":")) {
            certlvlid.setText(certlvl);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (bckey.equals("peopleprofile")) {
            Bundle b = getIntent().getExtras();
            String uid = b.getString("uid");
            Intent i = new Intent(ScubaDivingCertificationDeatils.this, PeopleProfileActivity.class);
            i.putExtra("uid", uid);
            startActivity(i);
        } else if (bckey.equals("myprofile")) {
            Intent i = new Intent(ScubaDivingCertificationDeatils.this, MyProfileActivity.class);
            startActivity(i);
        }
    }
}
