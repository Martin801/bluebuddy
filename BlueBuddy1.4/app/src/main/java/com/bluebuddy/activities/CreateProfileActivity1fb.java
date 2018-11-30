package com.bluebuddy.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bluebuddy.R;
import com.bluebuddy.Utilities.MyUtility;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.ServerResponseCp1;
import com.bluebuddy.data.StaticConfig;
import com.bluebuddy.hbb20.CountryCodePicker;
import com.bluebuddy.interfaces.ApiInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_STEP;
import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class CreateProfileActivity1fb extends BuddyActivity {

    EditText txtFirstName, txtLastName;
    Activity _myActivity;
    Button btflname;
    JSONObject response;
    //
    CountryCodePicker ccpLoadNumber, ccpGetNumber;
    Button buttonGetNumberWithPlus;
    EditText editTextLoadCarrierNumber, editTextGetCarrierNumber;
    private EditText rgmail, rgphone, rgpass, rgphcc;
    private SharedPreferences pref;
    private String token, fn, ln, name, femail, user_id;
    private FirebaseAuth auth;
    private AuthUtils authUtils;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_create_profile1fb);
        super.onCreate(savedInstanceState);
        this.initializeElements();
        _myActivity = this;
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        registerCarrierEditText();
        initFirebase();
    }

    private void registerCarrierEditText() {
        //  ccpLoadNumber.registerCarrierNumberEditText(editTextLoadCarrierNumber);
        ccpGetNumber.registerCarrierNumberEditText(editTextGetCarrierNumber);
        /*
        ccpGetNumber.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                if (isValidNumber) {
                    imgValidity.setImageDrawable(getResources().getDrawable(R.drawable.ic_assignment_turned_in_black_24dp));
                    tvValidity.setText("Valid Number");
                } else {
                    imgValidity.setImageDrawable(getResources().getDrawable(R.drawable.ic_assignment_late_black_24dp));
                    tvValidity.setText("Invalid Number");
                }
            }
        });
        */
    }

    private void initializeElements() {

        ccpGetNumber = (CountryCodePicker) findViewById(R.id.ccp_getFullNumber);
        editTextGetCarrierNumber = (EditText) findViewById(R.id.editText_getCarrierNumber);
        rgmail = (EditText) findViewById(R.id.rgidmail);
        //    rgphone = (EditText) findViewById(R.id.rgidph);
        // rgpass = (EditText) findViewById(R.id.rgidps);
        //   rgphcc = (EditText) findViewById(R.id.rgidph1);

        txtFirstName = (EditText) findViewById(R.id.fnedt);
        txtLastName = (EditText) findViewById(R.id.lnedt);
        TextView tx = (TextView) findViewById(R.id.txtv1);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");
        tx.setTypeface(custom_font);
        TextView IDtxt1 = (TextView) findViewById(R.id.idtxt1);
        TextView IDtxt2 = (TextView) findViewById(R.id.idtxt2);
        Bundle bundle = getIntent().getExtras();
        fn = bundle.getString("fbfname1");
        ln = bundle.getString("fblname1");
        femail = bundle.getString("femail");

        Log.d("femail ", "initializeElements: " + femail);
        user_id = bundle.getString("user_id");
        txtFirstName.setText(fn);
        txtLastName.setText(ln);

        try {
            rgmail.setText(femail);
            rgmail.setTextColor(Color.BLACK);
            if (!femail.trim().equals("")) {
                rgmail.setEnabled(false);
            }
        } catch (Exception e) {
            rgmail.setText("");
            rgmail.setTextColor(Color.BLACK);
        }

        btflname = (Button) findViewById(R.id.cpflname);

        btflname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String firstname = txtFirstName.getText().toString().trim();
                final String lastname = txtLastName.getText().toString().trim();
                String email = rgmail.getText().toString().trim();
                String password = "";
                final String mobile_no = ccpGetNumber.getFullNumberWithPlus();
                Log.d("mobile num:", ccpGetNumber.getFullNumberWithPlus());
                //    Toast.makeText(CreateProfileActivity1fb.this,ccpGetNumber.getFullNumberWithPlus(),Toast.LENGTH_LONG).show();
                //   String mobile_no = "+" + rgphcc.getText().toString().trim() + rgphone.getText().toString().trim();

                if (firstname.isEmpty()) {
                    oDialog("Enter first name", "Close", "", false, _myActivity, "", 0);
                } else if (!firstname.matches(MyUtility.namePattern)) {
                    oDialog("Enter a valid First name", "Close", "", false, _myActivity, "", 0);
                } else if (lastname.isEmpty()) {
                    oDialog("Enter last name", "Close", "", false, _myActivity, "", 0);
                } else if (!lastname.matches(MyUtility.namePattern)) {
                    oDialog("Enter a valid last name", "Close", "", false, _myActivity, "", 0);
                }
                if (email.isEmpty()) {
                    oDialog("Enter email address", "Close", "", false, _myActivity, "", 0);
                    rgmail.requestFocus();
                }
                /* else if (rgphcc.length() == 0) {
                    oDialog("Enter the country code", "Close", "", false, _myActivity, "", 0);
                }*/
                else if (!email.matches(MyUtility.emailPattern)) {
                    oDialog("Enter valid email address", "Close", "", false, _myActivity, "", 0);
                    rgmail.requestFocus();
                } else if (mobile_no.isEmpty()) {
                    oDialog("Enter mobile number", "Close", "", false, _myActivity, "", 0);
                    //  rgphone.requestFocus();
                    editTextGetCarrierNumber.requestFocus();
                } else if (mobile_no.length() < 11 && !mobile_no.contains("+")) {
                    oDialog("Enter valid mobile number", "Close", "", false, _myActivity, "", 0);
                    //   rgphone.requestFocus();
                    editTextGetCarrierNumber.requestFocus();
                } else if (mobile_no.length() < 11) {
                    oDialog("Enter valid mobile number", "Close", "", false, _myActivity, "", 0);
                    //   rgphone.requestFocus();
                    editTextGetCarrierNumber.requestFocus();
                }
                /*else if (mobile_no.isEmpty()) {
                    oDialog("Enter mobile number", "Close", "", false, _myActivity, "", 0);
                    rgphone.requestFocus();
                } else if (mobile_no.length() < 10) {
                    oDialog("Enter valid mobile number", "Close", "", false, _myActivity, "", 0);
                    rgphone.requestFocus();
                } *//*else if (password.isEmpty()) {
                    oDialog("Enter password", "Close", "", false, _myActivity, "", 0);
                    rgpass.requestFocus();
                } else if (password.length() < 6) {
                    oDialog("The password must be at least 6 characters", "Close", "", false, _myActivity, "", 0);
                    rgpass.requestFocus();
                } */
                else {
                    authUtils.createUser(email, mobile_no, password, firstname, lastname);
                }
            }
        });

    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    protected void setMyPref(String key, String value) {
        super.setPref(key, value);
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, String data) {
        super.openDialog6(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, data);
    }

    private void initFirebase() {

        auth = FirebaseAuth.getInstance();
        authUtils = new AuthUtils();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    StaticConfig.UID = user.getUid();

                    startActivity(new Intent(CreateProfileActivity1fb.this, MyProfileActivity.class));
                    finish();

                } else {

                }
            }
        };
    }

    void initNewUserInfo(String email, String fname, String lname) {

//        User newUser = new User();
//        user = auth.getCurrentUser();
//        newUser.email = user.getEmail();
//        newUser.name = userDetails.getFirst_name() + " " + userDetails.getLast_name();
//        if (userDetails.getProfile_pic().isEmpty())
//            newUser.avata = userDetails.getProfile_pic();
        FirebaseUser user = auth.getCurrentUser();
//        User newUser = new User();

        HashMap<String, String> updateUser = new HashMap<>();
        updateUser.put("email", email);
        updateUser.put("name", fname + " " + lname);
        updateUser.put("image", "default");

        FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("credentials").setValue(updateUser);
    }

    class AuthUtils {

//        void initNewUserInfo() {
//            User newUser = new User();
//            user = auth.getCurrentUser();
//            newUser.email = user.getEmail();
//            newUser.name = user.getEmail().substring(0, user.getEmail().indexOf("@"));
//            newUser.avata = StaticConfig.STR_DEFAULT_BASE64;
//            FirebaseDatabase.getInstance().getReference().child("user/" + user.getUid()).setValue(newUser);
//        }

        void createUser(final String email, final String mobile_no, final String password, final String firstname, final String lastname) {

            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            final String accessToken = FirebaseInstanceId.getInstance().getToken();

            Call<ServerResponseCp1> userCall = service.Cp1fb("Bearer " + token, "put", firstname, lastname, email, password, mobile_no, 2, accessToken, user_id);

            userCall.enqueue(new Callback<ServerResponseCp1>() {

                @Override
                public void onResponse(Call<ServerResponseCp1> call, Response<ServerResponseCp1> response) {

                    Log.d("onResponse", "" + response.body().getMessage());

                    if (response.body().getStatus() == "true") {

                        initNewUserInfo(email, firstname, lastname);

                        setMyPref(ACCESS_STEP, "2");
                        Log.d("onResponse", "" + response.body().getStatus());
                        Log.d("onResposeFirstName", "" + response.body().getFirst_name());
                        Log.d("onResponseLastName", "" + response.body().getFirst_name());

                        Intent intent = new Intent(CreateProfileActivity1fb.this, RUCActivity.class);
                        startActivity(intent);
//                        finish();

                    } else {

                        Log.d("onResponse", "" + response.body().getStatus());
                        logoutAndSignInPage(CreateProfileActivity1fb.this);

                    }
                }

                @Override
                public void onFailure(Call<ServerResponseCp1> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                }
            });
        /*    auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(CreateProfileActivity1fb.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                oDialog("The email address is already in use by another account." + task.getException(), "Ok", "", false, _myActivity, "", 0, "");

                            } else if (task.isSuccessful()) {
                                initNewUserInfo();
                                FirebaseUser user = task.getResult().getUser();
                                String user_id = user.getUid().toString();
                                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                                final String accessToken = FirebaseInstanceId.getInstance().getToken();
                                Call<ServerResponseCp1> userCall = service.Cp1fb("Bearer " + token, "put", firstname, lastname, email, password, mobile_no, 2, accessToken, user_id);

                                userCall.enqueue(new Callback<ServerResponseCp1>() {
                                    @Override
                                    public void onResponse(Call<ServerResponseCp1> call, Response<ServerResponseCp1> response) {
                                        Log.d("onResponse", "" + response.body().getMessage());
                                        if (response.body().getStatus() == "true") {
                                            setMyPref(ACCESS_STEP, "2");
                                            Log.d("onResponse", "" + response.body().getStatus());
                                            Log.d("onResposeFirstName", "" + response.body().getFirst_name());
                                            Log.d("onResponseLastName", "" + response.body().getFirst_name());

                                            Intent intent = new Intent(CreateProfileActivity1fb.this, RUCActivity.class);

                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Log.d("onResponse", "" + response.body().getStatus());

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ServerResponseCp1> call, Throwable t) {
                                        Log.d("onFailure", t.toString());
                                    }
                                });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });*/
        }
    }

//     private void handleFacebookAccessToken(AccessToken token) {
//        Log.d(TAG, "handleFacebookAccessToken:" + token);
//
//        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
//        auth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = auth.getCurrentUser();
//
//                        } else {
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Log.d(TAG, "Authentication failed.");
//                           // Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//    }
}
