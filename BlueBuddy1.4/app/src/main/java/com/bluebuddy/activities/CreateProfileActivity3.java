package com.bluebuddy.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluebuddy.R;

import static com.bluebuddy.app.AppConfig.ACCESS_STEP;
import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class CreateProfileActivity3 extends BuddyActivity {
    EditText namecrt, crtnum, crtlvl, certlvlid1, certlvlid2;
    Button nxt, skp, add_level, minus_level1, minus_level2;
    LinearLayout newlvl1, newlvl2;
    Activity _myActivity;
    ImageView back;
    View certlvlid1_view, certlvlid2_view;
    private SharedPreferences pref;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_create_profile3);
        super.onCreate(savedInstanceState);
        this.initializeElements();
        _myActivity = this;
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
    }

    private void initializeElements() {
        back = (ImageView) findViewById(R.id.imgNotification2);
        certlvlid1_view = (View) findViewById(R.id.certlvlid1_view);
        certlvlid2_view = (View) findViewById(R.id.certlvlid2_view);
        namecrt = (EditText) findViewById(R.id.certnameid);
        crtnum = (EditText) findViewById(R.id.certnumid);
        add_level = (Button) findViewById(R.id.add_level);
        minus_level1 = (Button) findViewById(R.id.minus_level1);
        minus_level2 = (Button) findViewById(R.id.minus_level2);
        certlvlid1 = (EditText) findViewById(R.id.certlvlid1);
        certlvlid2 = (EditText) findViewById(R.id.certlvlid2);
        newlvl1 = (LinearLayout) findViewById(R.id.newlvl1);
        newlvl2 = (LinearLayout) findViewById(R.id.newlvl2);
        TextView tx = (TextView) findViewById(R.id.txtv1);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");
        tx.setTypeface(custom_font);
        TextView IDtxt1 = (TextView) findViewById(R.id.idtxt1);
        TextView IDtxt2 = (TextView) findViewById(R.id.idtxt2);
        TextView IDtxt3 = (TextView) findViewById(R.id.idtxt3);
        TextView IDtxt4 = (TextView) findViewById(R.id.idtxt4);
        TextView IDtxt5 = (TextView) findViewById(R.id.idtxt5);
        //  Typeface fn1 = Typeface.createFromAsset(getAssets(),"fonts/PTC55F.ttf");
        IDtxt1.setTypeface(custom_font);
        IDtxt2.setTypeface(custom_font);
        IDtxt3.setTypeface(custom_font);
        IDtxt4.setTypeface(custom_font);
        IDtxt5.setTypeface(custom_font);
        crtlvl = (EditText) findViewById(R.id.certlvlid);
        nxt = (Button) findViewById(R.id.certdnxtid);
        skp = (Button) findViewById(R.id.certdskpid);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMyPref(ACCESS_STEP, "2");
                Intent i = new Intent(CreateProfileActivity3.this, RUCActivity.class);


                startActivity(i);
            }
        });
        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cp3nxt();
            }
        });
        skp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMyPref(ACCESS_STEP, "4"); // CreateProfile4
                Intent i = new Intent(CreateProfileActivity3.this, CreateProfileActivity4.class);
                startActivity(i);
            }
        });
        add_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newlvl1.getVisibility() == View.VISIBLE && certlvlid1_view.getVisibility() == View.VISIBLE) {
                    newlvl2.setVisibility(View.VISIBLE);
                    certlvlid2_view.setVisibility(View.VISIBLE);
                } else {

                    newlvl1.setVisibility(View.VISIBLE);
                    certlvlid1_view.setVisibility(View.VISIBLE);

                }
            }
        });
        minus_level1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newlvl1.setVisibility(View.GONE);
                certlvlid1_view.setVisibility(View.GONE); /*Gone is used for ommit the layout space*/
                certlvlid1.setText("");
            }
        });
        minus_level2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newlvl2.setVisibility(View.GONE);
                certlvlid2_view.setVisibility(View.GONE);
                certlvlid2.setText("");
            }
        });
    }

    public void cp3nxt() {
        String crtname = namecrt.getText().toString().trim();
        String numcrt = crtnum.getText().toString().trim();
        String lvlcrt = crtlvl.getText().toString().trim();

        if (crtname.isEmpty()) {
            oDialog("Enter certificate name", "Close", "", false, _myActivity, "", 0);
        } else if (lvlcrt.isEmpty()) {
            oDialog("Enter certificate level", "Close", "", false, _myActivity, "", 0);
        } else {
            setMyPref(ACCESS_STEP, "4");
            Intent ii = new Intent(CreateProfileActivity3.this, CreateProfileActivity4.class);

            startActivity(ii);
            finish();
        }
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    public void cp3skp(View view) {
        setMyPref(ACCESS_STEP, "4");
        Intent jj = new Intent(CreateProfileActivity3.this, CreateProfileActivity4.class);
        startActivity(jj);
    }

    protected void setMyPref(String key, String value) {
        super.setPref(key, value);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setMyPref(ACCESS_STEP, "2");
        Intent i = new Intent(CreateProfileActivity3.this, RUCActivity.class);
        startActivity(i);
    }
}
