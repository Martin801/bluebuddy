package com.bluebuddy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bluebuddy.R;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;

import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.CommonResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class ChangePassword extends BuddyActivity {

    private ImageView back, notice;
    private EditText oldpass, newpass, confpass;
    private Button cpassbtn;
    Activity _activity;
    Context _context;
    private SharedPreferences pref;
    private String token;
    String bellcounter;
    private ProgressDialog mProgressDialog;
    MyTextView bell_counter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.changepassword);
        super.onCreate(savedInstanceState);
        super.notice();
        super.initialize();
        super.setImageResource1(5);
        _activity = this;
        _context = this;

        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        oldpass = (EditText) findViewById(R.id.oldpass);
        newpass = (EditText) findViewById(R.id.rgidps);
        confpass = (EditText) findViewById(R.id.con_pass);
        cpassbtn = (Button) findViewById(R.id.cpassbtn);
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChangePassword.this, MyAccountMenu.class);
                startActivity(i);
            }
        });


        cpassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Changepass();
            }
        });
        bellcount();
    }

    public void Changepass() {

        final String oldpassword = oldpass.getText().toString().trim();
        final String password = newpass.getText().toString().trim();
        final String conpassword = confpass.getText().toString().trim();

        if (oldpassword.isEmpty()) {
            oDialog("Enter old password", "Close", "", false, _activity, "", 0);
            oldpass.requestFocus();
        } else if (password.isEmpty()) {
            oDialog("Enter password", "Close", "", false, _activity, "", 0);
            newpass.requestFocus();
        } else if (conpassword.isEmpty()) {
            oDialog("Enter Confirm password", "Close", "", false, _activity, "", 0);
            newpass.requestFocus();
        } else if (!confpass.getText().toString().equals(newpass.getText().toString())) {
            oDialog("Enter the same password", "Close", "", false, _activity, "", 0);
            confpass.requestFocus();
        } else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            user.updatePassword(conpassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                                Call<CommonResponse> userCall = service.changepass("Bearer " + token, oldpassword, conpassword);

                                userCall.enqueue(new Callback<CommonResponse>() {
                                    @Override
                                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {


                                        Log.d("onResponse", "" + response.body().getMessage());
                                        if (response.body().getStatus() == true) {
                                            Log.d("onResponse", "" + response.body().getStatus());
                                            oDialog("Your password changed Succesfully.", "Submit", "", true, _activity, "MyProfileActivity", 0);
                                        } else {
                                            Log.d("onResponse", "" + response.body().getStatus());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                                        Log.d("onFailure", t.toString());
                                    }
                                });
                            }
                        }
                    });


        }
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    private void bellcount() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllNotice> userCall = service.allnotice1("Bearer " + token);

        userCall.enqueue(new Callback<AllNotice>() {
            @Override
            public void onResponse(Call<AllNotice> call, Response<AllNotice> response) {
                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("onResponse:", "" + response.body().getNotification_details());
                bellcounter = response.body().getCounter();
                    if (bellcounter.equals("0")) {
                        bell_counter.setVisibility(View.GONE);
                    } else if (!bellcounter.equals("0")) {
                        bell_counter.setVisibility(View.VISIBLE);
                    }

                bell_counter.setText(response.body().getCounter());
            }

            @Override
            public void onFailure(Call<AllNotice> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ChangePassword.this, MyAccountMenu.class);
        startActivity(i);
    }
}

