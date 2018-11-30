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
import com.bluebuddy.adapters.CertAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.classes.ServerResponseCp3;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AfterLoginStatus;
import com.bluebuddy.models.CertificateDetail;
import com.bluebuddy.models.CommonResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class CreateProfileActivityfreedivingedit extends BuddyActivity {
    EditText namecrt, crtnum, crtlvl, certlvlid1, certlvlid2;
    MyTextView certtypeheader;
    Button nxt, skp, add_level, minus_level1, minus_level2;
    LinearLayout newlvl1, newlvl2;
    Activity _myActivity;
    ImageView back;
    private CertAdapter certAdapter;
    View certlvlid1_view, certlvlid2_view;
    private SharedPreferences pref;
    private String token, certtype, certname, certnum, certlvl, certid, crtlevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_create_profile3_freedivingedt);
        super.onCreate(savedInstanceState);
        this.initializeElements();
        _myActivity = this;

        Bundle b = getIntent().getExtras();
        certtype = b.getString("cert_type");
        certname = b.getString("cert_name");
        certnum = b.getString("cert_num");
        certlvl = b.getString("cert_lvl");
        certid = b.getString("cert_id");

        Log.d("cert_type", certtype.toString());
        Log.d("cert_name", certname.toString());
        Log.d("cert_num", certnum.toString());
        Log.d("cert_lvl", certlvl.toString());
        Log.d("cert_id", certid.toString());


        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        getAllCertificatefree();
    }

    private void getAllCertificatefree() {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AfterLoginStatus> userCall = service.myCertificate("Bearer " + token);

        userCall.enqueue(new Callback<AfterLoginStatus>() {
            @Override
            public void onResponse(Call<AfterLoginStatus> call, Response<AfterLoginStatus> response) {
                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("TOKEN:", "" + token);
                if (response.body().getStatus() == true) {
                    ArrayList<CertificateDetail> certificateDetails = response.body().getCertification_details();

                    if (certtype.equals("Scuba_Diving")) {
                        certtypeheader.setText("Scuba Diving Certification");
                    } else if (certtype.equals("Free_Diving")) {
                        certtypeheader.setText("Free Diving Certification");
                    }

                    namecrt.setText(certname);
                    crtnum.setText(certnum);

                    if (certlvl.contains(":")) {
                        String[] separated = certlvl.split(":");
                        if (separated.length == 0) {
                            separated[0] = separated[0].trim();
                            crtlvl.setText(separated[0]);
                          //  Log.d("lvl 1", separated[0]);
                        } else if (separated.length == 1) {
                            separated[0] = separated[0].trim();
                            separated[1] = separated[1].trim();
                            crtlvl.setText(separated[0]);
                            certlvlid1.setText(separated[1]);
                           // Log.d("lvl 1", separated[0]);
                          //  Log.d("lvl 2", separated[1]);
                        } else if (separated.length == 2) {
                            newlvl1.setVisibility(View.VISIBLE);
                            separated[0] = separated[0].trim();
                            separated[1] = separated[1].trim();
                            crtlvl.setText(separated[0]);
                            certlvlid1.setText(separated[1]);
                          //  Log.d("lvl 1", separated[0]);
                          //  Log.d("lvl 2", separated[1]);


                        } else if (separated.length == 3) {
                            newlvl1.setVisibility(View.VISIBLE);
                            newlvl2.setVisibility(View.VISIBLE);
                            separated[0] = separated[0].trim();
                            separated[1] = separated[1].trim();
                            separated[2] = separated[2].trim();
                            crtlvl.setText(separated[0]);
                            certlvlid1.setText(separated[1]);
                            certlvlid2.setText(separated[2]);
                           // Log.d("lvl 1", separated[0]);
                           // Log.d("lvl 2", separated[1]);
                           // Log.d("lvl 3", separated[2]);
                        }
                    } else if (!certlvl.contains(":")) {
                        crtlvl.setText(certlvl);
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

    private void initializeElements() {
        back = (ImageView) findViewById(R.id.imgNotification2);
        certtypeheader = (MyTextView) findViewById(R.id.idcerttype);
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
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");
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
                Intent i = new Intent(CreateProfileActivityfreedivingedit.this, MyCertificationActivity.class);
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
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                Call<CommonResponse> userCall = service.deleteapi("Bearer " + token, "certification", certid);

                userCall.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        if (response.body().getStatus() == true) {
                            Log.d("onResponse", "" + response.body().getStatus());
                            oDialog("Certificate Deleted Successfully.", "Submit", "", true, _myActivity, "MyCertificationActivity", 0);

                        } else {
                            Log.d("onResponse", "" + response.body().getStatus());
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {

                    }
                });
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

                certlvlid1_view.setVisibility(View.GONE);

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
        String crtttpe = certtype.toString().trim();

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

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

        Call<ServerResponseCp3> userCall = service.Cp3("Bearer " + token, "", crtname, crtlevel, numcrt, crtttpe);
        userCall.enqueue(new Callback<ServerResponseCp3>() {
            @Override
            public void onResponse(Call<ServerResponseCp3> call, Response<ServerResponseCp3> response) {
                Log.d("onResponse", "" + response.body().getMessage());
                if (response.body().getStatus() == "true") {
                    Log.d("onResponse", "" + response.body().getStatus());
                    oDialog("Certificate updated successfuly", "Close", "", false, _myActivity, "", 0);
                    Intent intent = new Intent(CreateProfileActivityfreedivingedit.this, MyCertificationActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d("onResponse", "" + response.body().getStatus());
                }
            }

            @Override
            public void onFailure(Call<ServerResponseCp3> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });

    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    public void cp3skp(View view) {
        Intent jj = new Intent(CreateProfileActivityfreedivingedit.this, CreateProfileActivity4.class);
        startActivity(jj);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(CreateProfileActivityfreedivingedit.this, MyCertificationActivity.class);
        startActivity(i);
    }
}
