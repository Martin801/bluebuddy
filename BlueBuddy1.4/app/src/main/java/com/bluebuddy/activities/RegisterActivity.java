package com.bluebuddy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bluebuddy.R;
import com.bluebuddy.Utilities.MyUtility;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.api.ApiClient2;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.classes.ServerResponse;
import com.bluebuddy.data.RegPrefHelper;
import com.bluebuddy.data.StaticConfig;
import com.bluebuddy.hbb20.CountryCodePicker;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.CommonResponse;
import com.bluebuddy.models.PushNotification;
import com.bluebuddy.models.PushRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_STEP;
import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.PREF_NAME;
import static com.bluebuddy.app.AppConfig.USER_ID;

public class RegisterActivity extends BuddyActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    //
    CountryCodePicker ccpLoadNumber, ccpGetNumber;
    Button buttonGetNumberWithPlus;
    EditText editTextLoadCarrierNumber, editTextGetCarrierNumber;
    ProgressDialog progressDialog = null;
    private EditText rgmail, rgphone, rgpass, rgconpass, rgphcc;
    private MyTextView rgtxtlogin, IDtxt1, IDtxt2, IDtxt3, IDtxt4;
    private Button regbtn, regwfb, btnTermsPage;
    private ImageButton rgcbox;
    private Activity _myActivity;
    private Context _context;
    private boolean terms = false;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private LoginButton loginButton;
    private SharedPreferences pref;
    private FirebaseAuth auth;
    private AuthUtils authUtils;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    //
    private RegPrefHelper regPrefHelper;
    private String firebaseToken;

    private String email;
    private String socialId = "";
    private String social = "";
    private String isSocial = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getApplicationContext());

        super.setLayout(R.layout.activity_register);
        super.onCreate(savedInstanceState);

        this.initializeElements();

        regPrefHelper = RegPrefHelper.instance();

        _myActivity = this;
        _context = this;
        auth = FirebaseAuth.getInstance(/*FirebaseApp.initializeApp(_context)*/);
        firebaseToken = FirebaseInstanceId.getInstance().getToken();
//
        registerCarrierEditText();

        pref = super.getPref();
        rgconpass.setTypeface(Typeface.DEFAULT);
        rgconpass.setTransformationMethod(new PasswordTransformationMethod());
        rgpass.setTypeface(Typeface.DEFAULT);
        rgpass.setTransformationMethod(new PasswordTransformationMethod());

        if (!(pref.getString(ACCESS_STEP, "")).equals("")) {
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
//                nextActivity(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        rgconpass.setTypeface(Typeface.DEFAULT);
        rgconpass.setTransformationMethod(new PasswordTransformationMethod());
        rgpass.setTypeface(Typeface.DEFAULT);
        rgpass.setTransformationMethod(new PasswordTransformationMethod());

        //  loginButton = (LoginButton) findViewById(R.id.loginButton);
        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Profile profile = Profile.getCurrentProfile();
                nextActivity(profile);

            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        };

        // loginButton.setReadPermissions("user_friends");
        // loginButton.registerCallback(callbackManager, callback);

        /*auth = FirebaseAuth.getInstance();
        initFirebase();*/
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }

    private void registerCarrierEditText() {
        //  ccpLoadNumber.registerCarrierNumberEditText(editTextLoadCarrierNumber);
        ccpGetNumber.registerCarrierNumberEditText(editTextGetCarrierNumber);
        /*ccpGetNumber.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
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
        });*/
    }

    private void nextActivity(Profile profile) {
        if (profile != null) {
            final String fbid = profile.getId();
            final String fbfname = profile.getFirstName();
            final String fblname = profile.getLastName();

            Log.d(TAG, "FB ID: " + profile.getId() + ", FirstName: " + profile.getFirstName() + ", LastName: " + profile.getLastName());

            String socialfbid = profile.getId().trim();
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonResponse> userCall = service.fblog(socialfbid, "facebook", fbfname, fblname, "android");

            userCall.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    String at = response.body().getToken();
                    if (response.body().getStatus() == true && response.body().getNew_socialuser() == "true") {
                        Intent intent = new Intent(RegisterActivity.this, CreateProfileActivity1fb.class);
                        intent.putExtra("fbid1", fbid);
                        intent.putExtra("fbfname1", fbfname);
                        intent.putExtra("fblname1", fblname);
                        setPref(ACCESS_TOKEN, response.body().getToken());
                        startActivity(intent);
                        finish();
                    } else if (response.body().getStatus() == true && response.body().getNew_socialuser() == "false") {
                        gotoActivity(Integer.valueOf(response.body().getUser_details().getNext_step()), fbid, fbfname, fblname, at);

                       /* Intent intent = new Intent(RegisterActivity.this, MyProfileActivity.class);
                        setPref(ACCESS_TOKEN, response.body().getToken());
                        startActivity(intent);
                        finish();*/
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

    private void gotoActivity(int step, String fbid, String fbfname, String fblname, String at) {
        Intent intent;

        if (step == 0)
            return;

        switch (step) {
            case 1:
                intent = new Intent(RegisterActivity.this, CreateProfileActivity1fb.class);
                intent.putExtra("fbid1", fbid);
                intent.putExtra("fbfname1", fbfname);
                intent.putExtra("fblname1", fblname);
                setPref(ACCESS_TOKEN, at);
                break;

            case 2:
                intent = new Intent(RegisterActivity.this, RUCActivity.class);
                break;

            case 3:
                intent = new Intent(RegisterActivity.this, CreateProfileActivityscubadiving.class);
                break;

            case 4:
                intent = new Intent(RegisterActivity.this, CreateProfileActivity4.class);
                break;

            case 5:
                intent = new Intent(RegisterActivity.this, CreateProfileActivity5.class);
                break;

            case 6:
                intent = new Intent(RegisterActivity.this, CreateProfileActivity6.class);
                break;

            case 7:
                intent = new Intent(RegisterActivity.this, LocationActivity.class);
                break;

            case 8:
                intent = new Intent(RegisterActivity.this, CreateProfileActivity7.class);
                break;

            case 9:
                intent = new Intent(RegisterActivity.this, MyProfileActivity.class);
                setPref(ACCESS_TOKEN, at);
                break;

            default:
                intent = new Intent(RegisterActivity.this, MyProfileActivity.class);
        }

        startActivity(intent);
        finish();
    }

    private void initializeElements() {

        //    buttonGetNumberWithPlus = (Button) findViewById(R.id.button_getFullNumberWithPlus);
        //    ccpLoadNumber = (CountryCodePicker)findViewById(R.id.ccp_loadFullNumber);
        //  editTextLoadCarrierNumber = (EditText) findViewById(R.id.editText_loadCarrierNumber);

        ccpGetNumber = findViewById(R.id.ccp_getFullNumber);
        editTextGetCarrierNumber = findViewById(R.id.editText_getCarrierNumber);

        rgmail = findViewById(R.id.rgidmail);

        // rgphone = (EditText) findViewById(R.id.rgidph);
        rgpass = findViewById(R.id.rgidps);
        rgconpass = findViewById(R.id.con_pass);
        regbtn = findViewById(R.id.rgidreg);
        //regwfb = (Button) findViewById(R.id.rgidregfb);
        btnTermsPage = findViewById(R.id.btnTermsPage);
        rgtxtlogin = findViewById(R.id.rgtxtlg);
        rgcbox = findViewById(R.id.rgcbx);
        //    rgphcc = (EditText) findViewById(R.id.rgidph1);
        //  loginButton = (LoginButton) findViewById(R.id.loginButton);
        TextView tx = (MyTextView) findViewById(R.id.txtv1);

        TextView IDtxt1 = (MyTextView) findViewById(R.id.idtxt1);
        TextView IDtxt2 = (MyTextView) findViewById(R.id.idtxt2);
        TextView IDtxt3 = (MyTextView) findViewById(R.id.idtxt3);
        TextView IDtxt4 = (MyTextView) findViewById(R.id.idtxt4);

        Bundle bundle = getIntent().getExtras();

        email = bundle.getString("email");
        socialId = bundle.getString("social_id");
        social = bundle.getString("social");
        isSocial = bundle.getString("is_social");

        try {
            if (email == null) {
                email = "";
                socialId = "";
                social = "";
                isSocial = "0";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("femail ", "initializeElements: " + email + " " + socialId);

        try {
            rgmail.setText(email);
            rgmail.setTextColor(Color.BLACK);

            if (!email.trim().equals("") && !socialId.trim().equals("")) {
                rgmail.setEnabled(false);
            }

        } catch (Exception e) {
            rgmail.setText("");
            rgmail.setTextColor(Color.BLACK);
        }

       /* rgphcc.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (rgphcc.getText().length() == 2)
                    rgphone.requestFocus();
                return false;
            }
        });*/
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                regbtn(regbtn);
            }
        });

        btnTermsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, TermsActivity.class);
                startActivity(i);
            }
        });
      /*  buttonGetNumberWithPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("wake up:",ccpGetNumber.getFullNumberWithPlus());
                Toast.makeText(RegisterActivity.this,ccpGetNumber.getFullNumberWithPlus(),Toast.LENGTH_LONG).show();
               // editTextGetFullNumber.setText(ccpGetNumber.getFullNumberWithPlus());
            }
        });*/

    }

//editText_getCarrierNumber

    //today
    public void regbtn(View view) {

        final String email = rgmail.getText().toString().trim();
        final String password = rgpass.getText().toString().trim();
        //  final String mobile_no = "+" + rgphcc.getText().toString().trim() + rgphone.getText().toString().trim();
        final String mobile_no = ccpGetNumber.getFullNumberWithPlus();
        Log.d("mobile num:", ccpGetNumber.getFullNumberWithPlus());
        //   Toast.makeText(RegisterActivity.this,ccpGetNumber.getFullNumberWithPlus(),Toast.LENGTH_LONG).show();

        if (email.isEmpty()) {
            oDialog("Enter email address", "Close", "", false, _myActivity, "", 0);
            rgmail.requestFocus();
        } /*else if (rgphcc.length() == 0) {
            oDialog("Enter the country code", "Close", "", false, _myActivity, "", 0);
        }*/ else if (!rgconpass.getText().toString().equals(rgpass.getText().toString())) {
            oDialog("Enter the same password", "Close", "", false, _myActivity, "", 0);
            rgconpass.requestFocus();
        } else if (!email.matches(MyUtility.emailPattern)) {
            oDialog("Enter valid email address", "Close", "", false, _myActivity, "", 0);
            rgmail.requestFocus();
        } else if (mobile_no.isEmpty()) {
            oDialog("Enter mobile number", "Close", "", false, _myActivity, "", 0);
            //  rgphone.requestFocus();
            editTextGetCarrierNumber.requestFocus();
        } else if (mobile_no.length() < 12 && !mobile_no.contains("+")) {
            oDialog("Enter valid mobile number", "Close", "", false, _myActivity, "", 0);
            //   rgphone.requestFocus();
            editTextGetCarrierNumber.requestFocus();
        } else if (mobile_no.length() < 12) {
            oDialog("Enter valid mobile number", "Close", "", false, _myActivity, "", 0);
            //   rgphone.requestFocus();
            editTextGetCarrierNumber.requestFocus();
        } else if (password.isEmpty()) {
            oDialog("Enter password", "Close", "", false, _myActivity, "", 0);
            rgpass.requestFocus();
        } else if (password.length() < 6) {
            oDialog("The password must be at least 6 characters", "Close", "", false, _myActivity, "", 0);
            rgpass.requestFocus();
        } else if (!terms) {
            oDialog("Please Agree terms and conditions", "Close", "", false, _myActivity, "", 0);
        } else {
            regbtn.setClickable(false);
            regbtn.setFocusable(false);

            signUpUser(email, mobile_no, password);

           /* final FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();

            if (fbUser != null) {

                AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                fbUser.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                fbUser.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "User account deleted.");
                                                }
                                            }
                                        });
                            }
                        });
            }

            auth = FirebaseAuth.getInstance();

            authUtils.createUser(email, mobile_no, password);*/

        }
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, String data) {
        super.openDialog6(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, data);
    }

    public void regfb(View view) {
        //  loginButton.performClick();
    }

    public void txtlg(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void rgcbxbtn(View view) {
        if (!terms) {
            terms = true;
            rgcbox.setImageResource(R.drawable.checked3);
        } else {
            terms = false;
            rgcbox.setImageResource(R.drawable.unchecked2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void sendNotification(String serverKey, PushRequest notification) {
        ApiInterface service = ApiClient2.getClient2().create(ApiInterface.class);
        Call<PushNotification> pushCall = service.push("key=" + serverKey, notification);

        pushCall.enqueue(new Callback<PushNotification>() {
            @Override
            public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {
                Log.d("SUCCESS_REGISTER", "HERE I M");
                oDialog("Sit back & Relax! while we verify your mobile number", "Ok", "", true, _myActivity, "MobileVerificationActivity", 0, "");
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t) {
                Log.d("FAILURE_REGISTER", "HERE I M");
                t.printStackTrace();
            }
        });
    }

    protected void setPref(String key, String value) {
        super.setPref(key, value);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);
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
                    StaticConfig.UID = user.getUid();

                    startActivity(new Intent(RegisterActivity.this, MyProfileActivity.class));
                    RegisterActivity.this.finish();

                } else {

                }
            }
        };
    }

    private void signUpUser(/*@NonNull Task<AuthResult> task,*/ final String email, final String mobile_no, final String password) {

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setTitle(getString(R.string.loading_title));
            progressDialog.setMessage(getString(R.string.loading_message));
        }

        progressDialog.show();

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

        Call<ServerResponse> userCall = service.register("post",
                email,
                mobile_no,
                password,
                firebaseToken,
                socialId,
                social,
                isSocial,
                "android");
        userCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                if (Boolean.valueOf(response.body().getStatus())) {

                    String uid = response.body().getUser_id();
                    setPref(PREF_NAME, "1");
                    setPref(USER_ID, uid);

//                        setPref(USER_OTP, response.body().getOtp());

//                        HashMap<String, String> notification = new HashMap<String, String>();
//                        notification.put("body", "Your One Time Password: " + response.body().getOtp());
//                        notification.put("title", "OTP");
//                        notification.put("icon", "appicon");
//
//                        sendNotification(serverKey, new PushRequest(accessToken, notification));
                    oDialog("Sit back & Relax! while we verify your mobile number", "Ok", "", true, _myActivity, "MobileVerificationActivity", 0, "");
                } else {
                    oDialog(response.body().getMessage(), "Ok", "", false, _myActivity, "", 0, "");
                }

                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                regbtn.setClickable(true);
                regbtn.setFocusable(true);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                oDialog(t.toString(), "Ok", "", false, _myActivity, "", 0, "");

                regbtn.setClickable(true);
                regbtn.setFocusable(true);

                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    class AuthUtils {

        private ProgressDialog progressDialog;

        void createUser(final String email, final String mobile_no, final String password) {

            if (progressDialog == null) {
                progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setTitle(getString(R.string.loading_title));
                progressDialog.setMessage(getString(R.string.loading_message));
            }

            progressDialog.show();

            auth = FirebaseAuth.getInstance();

            auth.createUserWithEmailAndPassword(email, password)

                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {

                                oDialog("The email address is already in use by another account!", "Ok", "", false, _myActivity, "", 0, "");
                                regbtn.setClickable(true);
                                regbtn.setFocusable(true);

                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }

                            } else if (task.isSuccessful()) {

//                                initNewUserInfo();

//                                signUpUser(task, email, mobile_no, password);

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });

        }

        private void signUpUser(@NonNull Task<AuthResult> task, final String email, final String mobile_no, final String password) {

            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

            FirebaseUser user = task.getResult().getUser();
            final String accessToken = FirebaseInstanceId.getInstance().getToken();

            final String serverKey = getResources().getString(R.string.firebase_server_key);
            String api_key = getResources().getString(R.string.firebase_api_key);
            String user_id = user.getUid();

            /*Call<ServerResponse> userCall = service.register("post",
                    email,
                    mobile_no,
                    password,
                    accessToken,
                    user_id,
                    "android");*/

            Call<ServerResponse> userCall = service.register("post",
                    email,
                    mobile_no,
                    password,
                    accessToken,
                    socialId,
                    social,
                    isSocial,
                    "android");

            userCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                    if (Boolean.valueOf(response.body().getStatus())) {

                        initNewUserInfo();

                        String uid = response.body().getUser_id();
                        setPref(PREF_NAME, "1");
                        setPref(USER_ID, uid);

//                        setPref(USER_OTP, response.body().getOtp());
//                        HashMap<String, String> notification = new HashMap<String, String>();
//                        notification.put("body", "Your One Time Password: " + response.body().getOtp());
//                        notification.put("title", "OTP");
//                        notification.put("icon", "appicon");
//
//                        sendNotification(serverKey, new PushRequest(accessToken, notification));

                        oDialog("Sit back & Relax! while we verify your mobile number", "Ok", "", true, _myActivity, "MobileVerificationActivity", 0, "");
                    } else {
                        oDialog(response.body().getMessage(), "Ok", "", false, _myActivity, "", 0, "");
                    }

                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }

                    regbtn.setClickable(true);
                    regbtn.setFocusable(true);
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    oDialog(t.toString(), "Ok", "", false, _myActivity, "", 0, "");

                    regbtn.setClickable(true);
                    regbtn.setFocusable(true);

                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }
            });
        }

        void initNewUserInfo() {
//            User newUser = new User();
            user = auth.getCurrentUser();

//            newUser.email = user.getEmail();
//            newUser.name = user.getEmail().substring(0, user.getEmail().indexOf("@"));
//            newUser.avata = StaticConfig.STR_DEFAULT_BASE64;
            HashMap<String, String> updateUser = new HashMap<>();
            updateUser.put("email", user.getEmail());
            updateUser.put("name", user.getEmail().substring(0, user.getEmail().indexOf("@")));
            updateUser.put("image", StaticConfig.STR_DEFAULT_BASE64);

            FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(user.getUid())
                    .child("credentials")
                    .setValue(updateUser);
        }
    }

}
