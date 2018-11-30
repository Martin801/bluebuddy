package com.bluebuddy.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluebuddy.R;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.classes.ServerResponseCp6;
import com.bluebuddy.interfaces.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_STEP;
import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class CreateProfileActivity5 extends BuddyActivity {

    Button slfb, sltwt, slinst, slgl, slnxt;
    ImageView back;
    private SharedPreferences pref;
    private String token;
    private Activity _activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_create_profile5);
        super.onCreate(savedInstanceState);
        pref = super.getPref();

        _activity = this;

        token = pref.getString(ACCESS_TOKEN, "");

        back = findViewById(R.id.imgNotification2);

        final int white = Color.parseColor("#ffffffff");

        slfb = findViewById(R.id.slfbid);
        sltwt = findViewById(R.id.sltwtid);
        slinst = findViewById(R.id.slinstid);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");
        MyTextView IDtxt1 = findViewById(R.id.idtxt1);
        MyTextView IDtxt2 = findViewById(R.id.idtxt2);

        IDtxt1.setTypeface(custom_font);
        IDtxt2.setTypeface(custom_font);

        slgl = findViewById(R.id.slglid);
        slnxt = findViewById(R.id.btn_sociallinkid);

        back.setOnClickListener(v -> onBackPressed());

        slnxt.setOnClickListener(v -> btncp5nxt());

        slgl.setOnClickListener(v -> {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(_activity);
            LayoutInflater inflater = _activity.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.custom_popup_inputdialog3, null);
            dialogBuilder.setView(dialogView);

            final EditText edt = dialogView.findViewById(R.id.edit1);
            final TextView concat = dialogView.findViewById(R.id.concat);
            // concat.setText("https://plus.google.com/");
            concat.setText("https://www.youtube.com/channel/");
            edt.setHint("your id");

            dialogBuilder.setMessage("Enter Your YouTube Url");

            dialogBuilder.setPositiveButton("Done", (dialog, whichButton) -> {
                //final String googlepluslink = "https://plus.google.com/"+edt.getText().toString().trim();
                final String googlepluslink = "https://www.youtube.com/channel/" + edt.getText().toString().trim();
                slgl.setBackgroundColor(0xff0000ff);
                slgl.setTextColor(white);
                Log.d("googlepluslink input", googlepluslink);

                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                Call<ServerResponseCp6> userCall = service.socialurl4("Bearer " + token, "put", googlepluslink, 6);
                userCall.enqueue(new Callback<ServerResponseCp6>() {
                    @Override
                    public void onResponse(Call<ServerResponseCp6> call, Response<ServerResponseCp6> response) {
                        if (response.body().getStatus() == "true") {
                            Log.d("ggl msg", "" + response.body().getStatus());
                        } else {
                            logoutAndSignInPage(CreateProfileActivity5.this);
                        }
                    }

                    @Override
                    public void onFailure(Call<ServerResponseCp6> call, Throwable t) {
                        Log.d("onFailure", t.toString());
                        t.printStackTrace();
                    }
                });
            });
            dialogBuilder.setNegativeButton("Cancel", (dialog, whichButton) -> {
            });
            AlertDialog b = dialogBuilder.create();
            b.show();

        });

        slinst.setOnClickListener(v -> {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(_activity);
            LayoutInflater inflater = _activity.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.custom_popup_inputdialog3, null);
            dialogBuilder.setView(dialogView);

            final EditText edt = dialogView.findViewById(R.id.edit1);

            final TextView concat = dialogView.findViewById(R.id.concat);
            concat.setText("https://www.instagram.com/");
            edt.setHint("your id");

            dialogBuilder.setMessage("Enter Your Instagram Url");
            dialogBuilder.setPositiveButton("Done", (dialog, whichButton) -> {
                final String instlink = "https://www.instagram.com/" + edt.getText().toString().trim();
                slinst.setBackgroundColor(0xff0000ff);
                slinst.setTextColor(white);
                Log.d("instlink input", instlink);

                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                Call<ServerResponseCp6> userCall = service.socialurl3("Bearer " + token, "put", instlink, 6);
                userCall.enqueue(new Callback<ServerResponseCp6>() {
                    @Override
                    public void onResponse(Call<ServerResponseCp6> call, Response<ServerResponseCp6> response) {
                        if (response.body().getStatus() == "true") {
                            Log.d("twt msg", "" + response.body().getStatus());
                        } else {
                            logoutAndSignInPage(CreateProfileActivity5.this);
                        }
                    }

                    @Override
                    public void onFailure(Call<ServerResponseCp6> call, Throwable t) {
                        Log.d("onFailure", t.toString());
                        t.printStackTrace();
                    }
                });
            });
            dialogBuilder.setNegativeButton("Cancel", (dialog, whichButton) -> {
            });
            AlertDialog b = dialogBuilder.create();
            b.show();
        });

        sltwt.setOnClickListener(v -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(_activity);
            LayoutInflater inflater = _activity.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.custom_popup_inputdialog3, null);
            dialogBuilder.setView(dialogView);

            final EditText edt = dialogView.findViewById(R.id.edit1);

            final TextView concat = dialogView.findViewById(R.id.concat);
            concat.setText("https://www.twitter.com/");
            edt.setHint("your id");

            dialogBuilder.setMessage("Enter Your Twitter Url");
            dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    final String twtlink = "https://www.twitter.com/" + edt.getText().toString().trim();
                    sltwt.setBackgroundColor(0xff0000ff);
                    sltwt.setTextColor(white);
                    Log.d("twt input", twtlink);

                    ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                    Call<ServerResponseCp6> userCall = service.socialurl2("Bearer " + token, "put", twtlink, 6);

                    userCall.enqueue(new Callback<ServerResponseCp6>() {
                        @Override
                        public void onResponse(Call<ServerResponseCp6> call, Response<ServerResponseCp6> response) {
                            if (response.body().getStatus() == "true") {
                                Log.d("twt msg", "" + response.body().getStatus());
                            } else {
                                logoutAndSignInPage(CreateProfileActivity5.this);
                            }
                        }

                        @Override
                        public void onFailure(Call<ServerResponseCp6> call, Throwable t) {
                            Log.d("onFailure", t.toString());
                            t.printStackTrace();
                        }
                    });

                }
            });
            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            AlertDialog b = dialogBuilder.create();
            b.show();
        });

        slfb.setOnClickListener(v -> {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(_activity);
            LayoutInflater inflater = _activity.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.custom_popup_inputdialog3, null);
            dialogBuilder.setView(dialogView);

            final EditText edt = dialogView.findViewById(R.id.edit1);

            final TextView concat = dialogView.findViewById(R.id.concat);
            concat.setText("https://www.facebook.com/");
            edt.setHint("your id");

            dialogBuilder.setMessage("Enter Your Facebook Url");

            dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String fblink = "";
                    if (!edt.getText().toString().trim().contains("https://www.facebook.com/"))
                        fblink = "https://www.facebook.com/" + edt.getText().toString().trim();
                    else
                        fblink = edt.getText().toString().trim();

                    slfb.setBackgroundColor(0xff0000ff);
                    slfb.setTextColor(white);
                    Log.d("fb input", fblink);

                    ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                    Call<ServerResponseCp6> userCall = service.socialurl("Bearer " + token, "put", fblink, 6);

                    userCall.enqueue(new Callback<ServerResponseCp6>() {
                        @Override
                        public void onResponse(Call<ServerResponseCp6> call, Response<ServerResponseCp6> response) {
                            if (response.body().getStatus() == "true") {
                                Log.d("fb msg", "" + response.body().getStatus());
                            } else {
                                logoutAndSignInPage(CreateProfileActivity5.this);
                            }
                        }

                        @Override
                        public void onFailure(Call<ServerResponseCp6> call, Throwable t) {
                            Log.d("onFailure", t.toString());
                            t.printStackTrace();
                        }
                    });

                }
            });
            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });
            AlertDialog b = dialogBuilder.create();
            b.show();

        });

    }

    public void btncp5nxt() {

        setMyPref(ACCESS_STEP, "6"); // CreateProfile6
        Intent ii = new Intent(CreateProfileActivity5.this, CreateProfileActivity6.class);
        startActivity(ii);

    }

    protected void setMyPref(String key, String value) {
        super.setPref(key, value);
    }

    @Override
    public void onBackPressed() {

//        super.onBackPressed();
//        setMyPref(ACCESS_STEP, "4"); // CreateProfile4
//        Intent i = new Intent(CreateProfileActivity5.this, CreateProfileActivity4.class);
//        startActivity(i);

        finish();
    }

}
