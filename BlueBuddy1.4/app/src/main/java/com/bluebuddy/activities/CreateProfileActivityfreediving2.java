package com.bluebuddy.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluebuddy.R;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.ServerResponseCp3;
import com.bluebuddy.interfaces.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class CreateProfileActivityfreediving2 extends BuddyActivity {
    EditText namecrt, crtnum, crtlvl, certlvlid1, certlvlid2;
    Button nxt, skp, add_level, minus_level1, minus_level2;
    LinearLayout newlvl1, newlvl2;
    Activity _myActivity;
    ImageView back;
    View certlvlid1_view, certlvlid2_view;
    private SharedPreferences pref;
    private String token, crtlevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_create_profile3_freediving2);
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
                onBackPressed();
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
                Intent i = new Intent(CreateProfileActivityfreediving2.this, MyCertificationActivity.class);
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
        String lvlcrt2 = certlvlid1.getText().toString().trim();
        String lvlcrt3 = certlvlid2.getText().toString().trim();

        if (crtname.isEmpty()) {
            oDialog("Enter certificate name", "Close", "", false, _myActivity, "", 0);
        } else if (lvlcrt.isEmpty()) {
            oDialog("Enter certificate level", "Close", "", false, _myActivity, "", 0);
        } else {
            crtname = namecrt.getText().toString();
            if (!lvlcrt.isEmpty() && !lvlcrt2.isEmpty() && !lvlcrt3.isEmpty()) {
                crtlevel = crtlvl.getText().toString() + ":" + certlvlid1.getText().toString().trim() + ":" + certlvlid2.getText().toString().trim();
            } else if (!lvlcrt.isEmpty() && lvlcrt2.isEmpty() && lvlcrt3.isEmpty()) {
                crtlevel = crtlvl.getText().toString().trim();
            } else if (lvlcrt.isEmpty() && !lvlcrt2.isEmpty() && lvlcrt3.isEmpty()) {
                crtlevel = certlvlid1.getText().toString().trim();
            } else if (lvlcrt.isEmpty() && lvlcrt2.isEmpty() && !lvlcrt3.isEmpty()) {
                crtlevel = certlvlid2.getText().toString().trim();
            } else if (!lvlcrt.isEmpty() && !lvlcrt2.isEmpty() && lvlcrt3.isEmpty()) {
                crtlevel = crtlvl.getText().toString() + ":" + certlvlid1.getText().toString().trim();
            }

            numcrt = crtnum.getText().toString();

            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

            Call<ServerResponseCp3> userCall = service.Cp3("Bearer " + token, "", crtname, crtlevel, numcrt, "Free_Diving");
            userCall.enqueue(new Callback<ServerResponseCp3>() {
                @Override
                public void onResponse(Call<ServerResponseCp3> call, Response<ServerResponseCp3> response) {
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (response.body().getStatus() == "true") {
                        oDialog("Your Certificate Details inserted Successfully.", "Close", "", true, _myActivity, "MyCertificationActivity", 0, "");
                    } else {
                        Log.d("onResponse", "" + response.body().getStatus());
                    }
                }

                @Override
                public void onFailure(Call<ServerResponseCp3> call, Throwable t) {
                    //   hidepDialog();
                    Log.d("onFailure", t.toString());
                }
            });
            //-------------------------------------------------
        }
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, String data) {
        super.openDialog6(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, data);
    }

    public void cp3skp(View view) {

        Intent jj = new Intent(CreateProfileActivityfreediving2.this, MyCertificationActivity.class);
        startActivity(jj);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
