package com.bluebuddy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bluebuddy.R;
import com.bluebuddy.Utilities.MyUtility;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.data.SharedPreferenceHelper;
import com.bluebuddy.data.StaticConfig;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.LoginStatus;
import com.bluebuddy.models.User;
import com.bluebuddy.models.UserDetails;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_STEP;
import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.FIRE_ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.FIRE_REG_ID;
import static com.bluebuddy.app.AppConfig.PREF_NAME;
import static com.bluebuddy.app.AppConfig.USER_ABOUT;
import static com.bluebuddy.app.AppConfig.USER_DP;
import static com.bluebuddy.app.AppConfig.USER_EMAIL;
import static com.bluebuddy.app.AppConfig.USER_FULL_NAME;
import static com.bluebuddy.app.AppConfig.USER_ID;
import static com.bluebuddy.app.AppConfig.USER_INTERESTED_IN;
import static com.bluebuddy.app.AppConfig.USER_LOCATION;
import static com.bluebuddy.app.AppConfig.USER_OTP;
import static com.bluebuddy.app.AppConfig.USER_PHONE;
import static com.bluebuddy.app.AppConfig.USER_SOCIAL_LINK;
import static com.bluebuddy.helpers.SharedPrefManager.SHARED_PREF_NAME;
import static com.bluebuddy.helpers.SharedPrefManager.TAG_TOKEN;

public class LoginActivity extends BuddyActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
//    private static final String TAG_TOKEN = "firebase_token";

    int backButtonCount;
    ProgressDialog mProgressDialog;
    String fn, ln, fbid, a, femail;
    private EditText edt_mail, edt_pass;
    private Button btn_lg, btnTermsPage;
    private Activity _myActivity;
    private ImageButton rgcbox;
    private boolean terms = false;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private SharedPreferences pref;
    private LoginButton loginButton;
    private AuthUtils authUtils;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseUser user;
    private boolean firstTimeAccess;
    private ProgressDialog progressDialog;
    private String RefreshtokenLogin;
    private String socialId = "";
    private String social = "";
    private String isSocial = "0";

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuthListener != null)
            auth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                RefreshtokenLogin = FirebaseInstanceId.getInstance().getToken();
                while (RefreshtokenLogin == null)//this is used to get firebase token until its null so it will save you from null pointer exeption
                {
                    RefreshtokenLogin = FirebaseInstanceId.getInstance().getToken();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {

            }
        }.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());

        super.setLayout(R.layout.activity_login);
        super.onCreate(savedInstanceState);

        this.initializeElements();
        _myActivity = this;

        auth = FirebaseAuth.getInstance();

        pref = super.getPref();
        edt_pass.setTypeface(Typeface.DEFAULT);
        edt_pass.setTransformationMethod(new PasswordTransformationMethod());

        // Log.d(TAG, pref.getString(ACCESS_TOKEN, ""));
        //   Log.d(TAG, pref.getString(ACCESS_STEP, "ACCESS_STEP"));
        //  Log.d(TAG, pref.getString(USER_ID, "USER_ID"));

        if (!(pref.getString(ACCESS_STEP, "")).equals("")) {
            gotoActivity(Integer.valueOf(pref.getString(ACCESS_STEP, "0")));
        }

        pref = super.getPref();

        _myActivity = this;

        mProgressDialog = new ProgressDialog(this);

        loginButton = (LoginButton) findViewById(R.id.loginButton);

        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions("email", "public_profile");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

//                handleFacebookAccessToken(loginResult.getAccessToken(), loginResult);

                getUserDetails(loginResult);


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

       /* accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                nextActivity(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();*/
        // loginButton.setReadPermissions("email");
        /*FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
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
        };*/

       /* loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, callback);*/

        /*LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.d("wwe:","helllo123");
                        Toast.makeText(_myActivity, "Facebook onSuccess here ", Toast.LENGTH_SHORT).show();
                        handleFacebookAccessToken(loginResult.getAccessToken());
                        getUserDetails(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });*/
       /* loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                getUserDetails(loginResult);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });*/
        initFirebase();
    }

    public String getDeviceToken() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String firebaseToken = sharedPreferences.getString(TAG_TOKEN, null);
        Log.d(TAG, "getDeviceToken: " + firebaseToken);
        return firebaseToken;

    }

/*
    private void nextActivity(Profile profile) {
        if (profile != null) {
            final String fbid = profile.getId();
            final String fbfname = profile.getFirstName();
            final String fblname = profile.getLastName();

            Log.d(TAG, "FB ID: " + profile.getId() + ", FirstName: " + profile.getFirstName() + ", LastName: " + profile.getLastName());

            String socialfbid = profile.getId().toString().trim();
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<CommonResponse> userCall = service.fblog(socialfbid, "facebook", fbfname, fblname);

            userCall.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    String at = response.body().getToken();
                    if (response.body().getStatus() == true && response.body().getNew_socialuser() == "true") {
                        Intent intent = new Intent(LoginActivity.this, CreateProfileActivity1fb.class);
                        intent.putExtra("fbid1", fbid);
                        intent.putExtra("fbfname1", fbfname);
                        intent.putExtra("fblname1", fblname);
                        setPref(ACCESS_TOKEN, response.body().getToken());
                        startActivity(intent);
                        finish();
                    } else if (response.body().getStatus() == true && response.body().getNew_socialuser() == "false") {
                        gotoActivityFB(Integer.valueOf(response.body().getUser_details().getNext_step()),fbid,fbfname,fblname,at);

                      */
/* Intent intent = new Intent(RegisterActivity.this, MyProfileActivity.class);
                       setPref(ACCESS_TOKEN, response.body().getToken());
                       startActivity(intent);
                       finish();*//*

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
*/

    private void initializeElements() {
        edt_mail = (EditText) findViewById(R.id.id_mail);
        edt_pass = (EditText) findViewById(R.id.id_pass);
        btn_lg = (Button) findViewById(R.id.id_lgbtn);
        TextView tx = (MyTextView) findViewById(R.id.txtv1);
        TextView IDtxt1 = (MyTextView) findViewById(R.id.idtxt1);
        TextView IDtxt2 = (MyTextView) findViewById(R.id.idtxt2);
        TextView IDtxt3 = (MyTextView) findViewById(R.id.textView8);

        rgcbox = (ImageButton) findViewById(R.id.rgcbx);
        loginButton = (LoginButton) findViewById(R.id.loginButton);
        btn_lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lgbtn(btn_lg);
            }
        });
        btnTermsPage = (Button) findViewById(R.id.btnTermsPage);
        btnTermsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, TermsActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Logging Out User from Facebook.
        logoutFacebook();
    }

    private void logoutFacebook() {
        // Logging Out User from Facebook.
        try {
            LoginManager.getInstance().logOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    public void lgbtn(View view) {

        // Log.d("login btn clicked", "Clicked");
        final String RefreshtokenLogin = FirebaseInstanceId.getInstance().getToken();
//        Log.d("today REFRESH TKN1:", RefreshtokenLogin);
        final String email = edt_mail.getText().toString().trim();
        final String password = edt_pass.getText().toString().trim();
        if (email.isEmpty()) {
            oDialog("Enter email address", "Close", "", false, _myActivity, "", 0);
        } else if (!email.matches(MyUtility.emailPattern)) {
            oDialog("Enter valid email address", "Close", "", false, _myActivity, "", 0);
        } else if (password.isEmpty()) {
            oDialog("Enter password", "Close", "", false, _myActivity, "", 0);
        } else if (password.length() < 6) {
            oDialog("The password must be at least 6 characters", "Close", "", false, _myActivity, "", 0);
        } else if (!terms) {
            oDialog("Please accept our terms & conditions.", "Close", "", false, _myActivity, "", 0);
        } else {
            authUtils.signIn(email, password);
        }

    }

    public void fblgbtn(View view) {
        loginButton.performClick();
    }

    public void regtxt(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void fPasstxt(View view) {
        Intent intent = new Intent(LoginActivity.this, ForgotpasswordActivity.class);
        startActivity(intent);
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
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    protected void setPref(String key, String value) {
        super.setPref(key, value);
    }

    /*  // added nipa for all permission
      private boolean addPermission(List<String> permissionsList, String permission) {
          if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
              permissionsList.add(permission);
              // Check for Rationale Option
              if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                  return false;
          }
          return true;
      }
      private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
          new AlertDialog.Builder(LoginActivity.this)
                  .setMessage(message)
                  .setPositiveButton("OK", okListener)
                  .setNegativeButton("Cancel", null)
                  .create()
                  .show();
      }
      private  void  checkforpermission(int step){
          List<String> permissionsNeeded = new ArrayList<String>();

          final List<String> permissionsList = new ArrayList<String>();
          if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
              permissionsNeeded.add("ACCESS_FINE_LOCATION");
          if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
              permissionsNeeded.add("READ_EXTERNAL_STORAGE");
          if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
              permissionsNeeded.add("WRITE_EXTERNAL_STORAGE");
          if (!addPermission(permissionsList, Manifest.permission.CAMERA))
              permissionsNeeded.add("CAMERA");

          if (permissionsList.size() > 0) {
              if (permissionsNeeded.size() > 0) {
                  // Need Rationale
                  String message = "You need to grant access to " + permissionsNeeded.get(0);
                  for (int i = 1; i < permissionsNeeded.size(); i++)
                      message = message + ", " + permissionsNeeded.get(i);
                  showMessageOKCancel(message,
                          new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  ActivityCompat.requestPermissions(LoginActivity.this,
                                          permissionsList.toArray(new String[permissionsList.size()]),
                                          Utility.RECORD_REQUEST_CODE);
                              }
                          });
                  return;
              }
              ActivityCompat.requestPermissions(this, permissionsList.toArray(new String[permissionsList.size()]),
                      Utility.RECORD_REQUEST_CODE);
              return;
          }
          gotoActivity(step);
      }
      @Override
      public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
          if (requestCode == Utility.RECORD_REQUEST_CODE) {
              *//*if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this,"Can not",Toast.LENGTH_SHORT).show();
            }*//*
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    gotoActivity(Integer.valueOf(pref.getString(ACCESS_STEP, "0")));
                } else {
                    // Permission Denied
                    Toast.makeText(LoginActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }*/

    private void gotoActivity(int step) {
        Intent intent;

        if (step == 0)
            return;

        switch (step) {

            case 1:
                intent = new Intent(LoginActivity.this, CreateProfileActivity1.class);
                break;

            case 2:
                intent = new Intent(LoginActivity.this, RUCActivity.class);
                break;

            case 3:
                intent = new Intent(LoginActivity.this, CreateProfileActivityscubadiving.class);
                break;

            case 4:
                intent = new Intent(LoginActivity.this, CreateProfileActivity4.class);
                break;

            case 5:
                intent = new Intent(LoginActivity.this, CreateProfileActivity5.class);
                break;

            case 6:
                intent = new Intent(LoginActivity.this, CreateProfileActivity6.class);
                break;

            case 7:
                intent = new Intent(LoginActivity.this, LocationActivity.class);
                break;

            case 8:
                intent = new Intent(LoginActivity.this, CreateProfileActivity7.class);
                break;

            case 101:
                intent = new Intent(LoginActivity.this, MobileVerificationActivity.class);
                break;

            default:
                intent = new Intent(LoginActivity.this, MyProfileActivity.class);

        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    private void gotoActivityFB(int step, String fbid, String fbfname, String fblname, String femail, String user_id, String at) {
        Intent intent = null;

        if (step == 0)
            return;

        switch (step) {
            case 1:

                intent = new Intent(LoginActivity.this, CreateProfileActivity1fb.class);
                intent.putExtra("fbid1", fbid);
                intent.putExtra("fbfname1", fbfname);
                intent.putExtra("fblname1", fblname);
                intent.putExtra("femail", femail);
                intent.putExtra("user_id", user_id);
                setPref(ACCESS_TOKEN, at);
                break;

            case 2:
                intent = new Intent(LoginActivity.this, RUCActivity.class);
                break;

            case 3:
                intent = new Intent(LoginActivity.this, CreateProfileActivityscubadiving.class);
                break;

            case 4:
                intent = new Intent(LoginActivity.this, CreateProfileActivity4.class);
                break;

            case 5:
                intent = new Intent(LoginActivity.this, CreateProfileActivity5.class);
                break;

            case 6:
                intent = new Intent(LoginActivity.this, CreateProfileActivity6.class);
                break;

            case 7:
                intent = new Intent(LoginActivity.this, LocationActivity.class);
                break;

            case 8:
                intent = new Intent(LoginActivity.this, CreateProfileActivity7.class);
                break;

            case 9:
                intent = new Intent(LoginActivity.this, MyProfileActivity.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void initFirebase() {

        auth = FirebaseAuth.getInstance();

        authUtils = new AuthUtils();

//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//
//                user = firebaseAuth.getCurrentUser();
//
//                if (user != null) {
//                    StaticConfig.UID = user.getUid();
//                    if (firstTimeAccess) {
//                        startActivity(new Intent(LoginActivity.this, MyProfileActivity.class));
//                        LoginActivity.this.finish();
//                    }
//                } else {
//
//                }
//
//                firstTimeAccess = false;
//            }
//        };

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);
        }
    }

    void initNewUserInfo(UserDetails userDetails) {

//        User newUser = new User();
//        user = auth.getCurrentUser();
//        newUser.email = user.getEmail();
//        newUser.name = userDetails.getFirst_name() + " " + userDetails.getLast_name();
//        if (userDetails.getProfile_pic().isEmpty())
//            newUser.avata = userDetails.getProfile_pic();
        FirebaseUser user = auth.getCurrentUser();
//        User newUser = new User();

        HashMap<String, String> updateUser = new HashMap<>();
        updateUser.put("email", user.getEmail());
        updateUser.put("name", userDetails.getFirst_name() + " " + userDetails.getLast_name());

        if (userDetails.getProfile_pic().isEmpty())
            updateUser.put("image", userDetails.getProfile_pic());
        else
            updateUser.put("image", "default");

        FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("credentials").setValue(updateUser);
    }

    void saveUserInfo() {

        user = auth.getCurrentUser();
        StaticConfig.UID = user.getUid();

        FirebaseDatabase.getInstance().getReference().child("users").child(StaticConfig.UID).child("credentials").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap hashUser = (HashMap) dataSnapshot.getValue();
                User userInfo = new User();
                userInfo.name = (String) hashUser.get("name");
                userInfo.email = (String) hashUser.get("email");
                userInfo.avata = (String) hashUser.get("avata");
                SharedPreferenceHelper.getInstance(LoginActivity.this).saveUserInfo(userInfo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void handleFacebookAccessToken(AccessToken token, final LoginResult loginResult) {
        Log.d(TAG, "handleFacebookAccessToken: " + token + " " + token.getToken());
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();

                            updateUI(user, loginResult);

                        } else {

                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Log.d(TAG, "Authentication failed.");
                            oDialog("An account already exists with the same email address but different sign-in credential.", "Close", "", false, _myActivity, "", 0, "");

                            // Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    private void updateUI(FirebaseUser user, LoginResult loginResult) {
        String user_id = user.getUid().toString();
        if (user != null) {
//            getUserDetails(loginResult, user_id);
        } else {

        }
    }

    //    protected void getUserDetails(LoginResult loginResult, final String user_id) {
    protected void getUserDetails(LoginResult loginResult) {

        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    private JSONObject jsonObject;

                    @Override
                    public void onCompleted(JSONObject json_object, GraphResponse response) {

                        String userDetail = response.getRawResponse();
                        //  System.out.println("JSONObject1 : " + userDetail);
                        try {
                            jsonObject = new JSONObject(userDetail);
                            // System.out.println("JSONObject : " + jsonObject);
                            String gotval = jsonObject.toString().trim();
                            //  Log.d("HELLO", gotval);
                            try {
                                fbid = jsonObject.getString("id");
                                socialId = fbid;
                                social = "facebook";
                                isSocial = "1";
                            } catch (JSONException e) {
                                socialId = "";
                                social = "";
                                isSocial = "1";
                                e.printStackTrace();
                            }

                            try {
                                String fname = jsonObject.getString("name");
                                String[] splited = fname.split("\\s+");
                                for (int x = 0; x < fname.length(); x++) {
                                    fn = splited[0];
                                    ln = splited[1];
                                }
                            } catch (JSONException e) {
                                fn = "";
                                ln = "";
                                e.printStackTrace();
                            }

                            try {
                                femail = jsonObject.getString("email");
                            } catch (JSONException e) {
                                femail = "";
                                e.printStackTrace();
                            }
                            // Log.d("print msg1:", fbid);
                            //  Log.d("print msg2:", fname);
                            Log.d("print msg3:", femail);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (femail.trim().isEmpty())
                            oDialog("Sorry you cant create an account without email", "Close", "", false, _myActivity, "", 0);
                        else
                            authUtils.signIn(femail, "");

                        // Logging Out User from Facebook.
                        logoutFacebook();

                        /*ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

                        Call<CommonResponse> userCall = service.fblog(fbid, "facebook", fn, ln, "android");

                        userCall.enqueue(new Callback<CommonResponse>() {
                            @Override
                            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                                String accessToken = response.body().getToken();

                                // Logging Out User from Facebook.
                                logoutFacebook();

                                if (response.body().getStatus() == true && response.body().getNew_socialuser() == "true") {

                                    mProgressDialog.dismiss();

                                    Intent intent = new Intent(LoginActivity.this, CreateProfileActivity1fb.class);
                                    intent.putExtra("fbid1", fbid);
                                    intent.putExtra("fbfname1", fn);
                                    intent.putExtra("fblname1", ln);
                                    intent.putExtra("femail", femail);
                                    intent.putExtra("user_id", user_id);
                                    setPref(ACCESS_TOKEN, accessToken);
                                    startActivity(intent);
                                    finish();

                                } else if (response.body().getStatus() && response.body().getNew_socialuser() == "false") {

                                    mProgressDialog.dismiss();

                                    if (Integer.valueOf(response.body().getUser_details().getNext_step()) > 1) {

                                        setPref(ACCESS_TOKEN, accessToken);
                                        setPref(PREF_NAME, "1");
                                        setPref(ACCESS_STEP, String.valueOf(response.body().getUser_details().getNext_step()));
                                        setPref(USER_ID, String.valueOf(response.body().getUser_details().getId()));
                                        setPref(USER_FULL_NAME, String.valueOf(response.body().getUser_details().getFirst_name() + " " + response.body().getUser_details().getLast_name()));
                                        setPref(USER_EMAIL, String.valueOf(response.body().getUser_details().getEmail()));
                                        setPref(USER_PHONE, String.valueOf(response.body().getUser_details().getPhone()));
                                        setPref(USER_ABOUT, String.valueOf(response.body().getUser_details().getAbout()));
                                        setPref(USER_SOCIAL_LINK, String.valueOf(response.body().getUser_details().getSocial_link()));
                                        setPref(USER_DP, String.valueOf(response.body().getUser_details().getDp()));
                                        setPref(USER_INTERESTED_IN, String.valueOf(response.body().getUser_details().getInterested_in()));
                                        setPref(USER_LOCATION, String.valueOf(response.body().getUser_details().getLocation()));
                                        setPref(FIRE_ACCESS_TOKEN, String.valueOf(response.body().getUser_details().getFirebase_API_key()));
                                        setPref(FIRE_REG_ID, String.valueOf(response.body().getUser_details().getFirebase_reg_id()));

                                    }

                                    //  gotoActivity(Integer.valueOf(response.body().getUser_details().getNext_step()));
                                    gotoActivityFB(Integer.valueOf(response.body().getUser_details().getNext_step()), fbid, fn, ln, femail, user_id, accessToken);

                                    *//* Intent intent = new Intent(LoginActivity.this, MyProfileActivity.class);
                                    setPref(ACCESS_TOKEN, response.body().getToken());
                                    startActivity(intent);
                                    finish();*//*

                                    Log.d("onResponse", "" + response.body().getStatus());

                                }
                            }

                            @Override
                            public void onFailure(Call<CommonResponse> call, Throwable t) {
                                Log.d("onFailure", t.toString());
                            }
                        });*/

                    }

                });

        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, String data) {
        super.openDialog6(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, data);
    }

    class AuthUtils {

        void signIn(final String email, final String password) {

            if (progressDialog == null) {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle(getString(R.string.loading_title));
                progressDialog.setMessage(getString(R.string.loading_message));
            }

            progressDialog.show();

//            final String RefreshtokenLogin = getDeviceToken();
            Log.d(" today REFRESH TKN2: ", RefreshtokenLogin);
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

            Call<LoginStatus> userCall = service.login(email,
                    password,
                    RefreshtokenLogin,
                    socialId,
                    social,
                    isSocial,
                    "android");

            userCall.enqueue(new Callback<LoginStatus>() {
                @Override
                public void onResponse(Call<LoginStatus> call, final Response<LoginStatus> response) {

                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }

                    if (response.body() != null) {

                        if (response.body().getStatus() == true) {
                            // Log.d("User_details", "" + response.body().getUser_details().toString());

                            if (response.body().getUser_details().getNext_step().equalsIgnoreCase("9")) {
                                String pass;
                                if (password.trim().isEmpty())
                                    pass = response.body().getUser_details().getPassword();
                                else
                                    pass = password;
                                auth = FirebaseAuth.getInstance();
                                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
//                                                auth.createUserWithEmailAndPassword(email, password);
//                                                initNewUserInfo();
                                        } else if (task.isSuccessful()) {
                                            initNewUserInfo(response.body().getUser_details());
//                                                saveUserInfo();

                                            setPref(ACCESS_TOKEN, response.body().getToken().toString());
                                            setPref(PREF_NAME, "1");
                                            setPref(ACCESS_STEP, String.valueOf(response.body().getUser_details().getNext_step()));
                                            setPref(USER_ID, String.valueOf(response.body().getUser_details().getId()));
                                            setPref(USER_FULL_NAME, String.valueOf(response.body().getUser_details().getFirst_name() + " " + response.body().getUser_details().getLast_name()));
                                            setPref(USER_EMAIL, String.valueOf(response.body().getUser_details().getEmail()));
                                            setPref(USER_PHONE, String.valueOf(response.body().getUser_details().getPhone()));
                                            setPref(USER_ABOUT, String.valueOf(response.body().getUser_details().getAbout()));
                                            setPref(USER_SOCIAL_LINK, String.valueOf(response.body().getUser_details().getSocial()));
                                            setPref(USER_DP, String.valueOf(response.body().getUser_details().getProfile_pic()));
                                            setPref(USER_INTERESTED_IN, String.valueOf(response.body().getUser_details().getInterested_in()));
                                            setPref(USER_LOCATION, String.valueOf(response.body().getUser_details().getLocation()));
                                            setPref(FIRE_ACCESS_TOKEN, String.valueOf(response.body().getUser_details().getFirebase_API_key()));
                                            setPref(FIRE_REG_ID, String.valueOf(response.body().getUser_details().getFirebase_reg_id()));
                                            gotoActivity(Integer.valueOf(response.body().getUser_details().getNext_step()));
                                        }
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                            } else {
                                setPref(ACCESS_TOKEN, response.body().getToken().toString());
                                setPref(PREF_NAME, "1");
                                setPref(ACCESS_STEP, String.valueOf(response.body().getUser_details().getNext_step()));
                                setPref(USER_ID, String.valueOf(response.body().getUser_details().getId()));
                                setPref(USER_FULL_NAME, String.valueOf(response.body().getUser_details().getFirst_name() + " " + response.body().getUser_details().getLast_name()));
                                setPref(USER_EMAIL, String.valueOf(response.body().getUser_details().getEmail()));
                                setPref(USER_PHONE, String.valueOf(response.body().getUser_details().getPhone()));
                                setPref(USER_ABOUT, String.valueOf(response.body().getUser_details().getAbout()));
                                setPref(USER_SOCIAL_LINK, String.valueOf(response.body().getUser_details().getSocial()));
                                setPref(USER_DP, String.valueOf(response.body().getUser_details().getProfile_pic()));
                                setPref(USER_INTERESTED_IN, String.valueOf(response.body().getUser_details().getInterested_in()));
                                setPref(USER_LOCATION, String.valueOf(response.body().getUser_details().getLocation()));
                                setPref(FIRE_ACCESS_TOKEN, String.valueOf(response.body().getUser_details().getFirebase_API_key()));
                                setPref(FIRE_REG_ID, String.valueOf(response.body().getUser_details().getFirebase_reg_id()));
                                gotoActivity(Integer.valueOf(response.body().getUser_details().getNext_step()));
                            }

                        } else {
                            if (response.body().getUserStatus().matches("0")) {

                                // user not activated
                                setPref(USER_ID, String.valueOf(response.body().getUser_details().getId()));
                                setPref(USER_OTP, "");
                                gotoActivity(Integer.valueOf(101));

                            } else {

                                if (socialId.isEmpty()) {
                                    String genmsg = response.body().getMessage();
                                    oDialog(genmsg, "Close", "", false, _myActivity, "", 0);
                                } else {
                                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                                    intent.putExtra("email", email);
                                    intent.putExtra("social_id", socialId);
                                    intent.putExtra("social", social);
                                    intent.putExtra("is_social", isSocial);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        }
                    } else {

                        String msg = (String) getApplication().getText(R.string.goeswrong);
                        oDialog(msg, "Close", "", false, _myActivity, "", 0);

                    }
                }

                @Override
                public void onFailure(Call<LoginStatus> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
                    //  t.printStackTrace();
                }
            });

        }

    }

}