package com.bluebuddy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bluebuddy.R;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.classes.ServerResponseFp;
import com.bluebuddy.data.StaticConfig;
import com.bluebuddy.interfaces.ApiInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class ForgotpasswordActivity extends BuddyActivity {
    private static final String TAG = ForgotpasswordActivity.class.getSimpleName();
    EditText txtEmail;
    Button btnSubmit;
    private SharedPreferences pref;
    private String token;
    ProgressDialog progressDialog = null;
    Activity _myActivity;

    private AuthUtils authUtils;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_forgotpassword);
        super.onCreate(savedInstanceState);
        this.initializeElements();
        _myActivity = this;
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        auth = FirebaseAuth.getInstance();
        initFirebase();
    }

    private void initializeElements() {
        txtEmail = (EditText) findViewById(R.id.fpid);
        MyTextView tx = (MyTextView) findViewById(R.id.txtv1);
        MyTextView IDtxt1 = (MyTextView) findViewById(R.id.idtxt1);
        MyTextView IDtxt2 = (MyTextView) findViewById(R.id.idtxt2);
        btnSubmit = (Button) findViewById(R.id.btnfpsnd);
    }

    public void TXTreg(View view) {
        Intent intent = new Intent(ForgotpasswordActivity.this, RegisterActivity.class);
        startActivity(intent);
    }


    private void forgotPassword(String email) {
        email = txtEmail.getText().toString().trim();

        if (email.isEmpty()) {
            oDialog("Enter email address", "Close", "", false, _myActivity, "", 0);
        } else if (!email.matches(emailPattern)) {
            oDialog("Enter valid email address", "Close", "", false, _myActivity, "", 0);
        } else {
            authUtils.resetPassword(email);
        }
    }

    public void btnSND(View view) {
        String email = txtEmail.getText().toString().trim();
        this.forgotPassword(email);
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }


    class AuthUtils {
        void resetPassword(final String email) {
         /*   auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            oDialog("Please check your email", "Close", "", false, _myActivity, "", 0);*/
                            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                            Call<ServerResponseFp> userCall = service.Fp("post", email);
                            userCall.enqueue(new Callback<ServerResponseFp>() {
                                @Override
                                public void onResponse(Call<ServerResponseFp> call, retrofit2.Response<ServerResponseFp> response) {

                                    if (response.body().getStatus() == "true") {

                                        Toast.makeText(ForgotpasswordActivity.this, "Please check your email", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(ForgotpasswordActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        oDialog("The email id does not exist!Put the right email id!", "Close", "", false, _myActivity, "", 0);
                                        Log.d("onResponse", "" + response.body().getStatus());
                                    }
                                }

                                @Override
                                public void onFailure(Call<ServerResponseFp> call, Throwable t) {
                                    //   hidepDialog();
                                    Log.d("onFailure", t.toString());
                                }
                            });
/*
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });*/
        }
    }

    private void initFirebase() {

        auth = FirebaseAuth.getInstance();
        authUtils = new AuthUtils();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    StaticConfig.UID = user.getUid();
                }
            }
        };

    }
}
