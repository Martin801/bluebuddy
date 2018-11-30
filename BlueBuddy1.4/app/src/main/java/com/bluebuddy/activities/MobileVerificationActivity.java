package com.bluebuddy.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bluebuddy.R;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.api.ApiClient2;
import com.bluebuddy.classes.ServerResponseOtp;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.PushNotification;
import com.bluebuddy.models.PushRequest;
import com.bluebuddy.models.ResendOtpModel;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_STEP;
import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.PREF_NAME;
import static com.bluebuddy.app.AppConfig.USER_ID;
import static com.bluebuddy.app.AppConfig.USER_OTP;

public class MobileVerificationActivity extends BuddyActivity {

    private static final String TAG = MobileVerificationActivity.class.getSimpleName();
    Dialog dialog;
    private EditText txtCode;
    private Button btnAuthenticate;
    private Activity _myActivity;
    private SharedPreferences pref;
    private String data = "";
    private String otp = "";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_mobile_verification);
        super.onCreate(savedInstanceState);

        _myActivity = this;
        pref = super.getPref();
        initializeElements();

    }

    private void initializeElements() {

        txtCode = (EditText) findViewById(R.id.txtCode);
        txtCode.setOnClickListener(v -> txtCode.setCursorVisible(true));

        TextView tx = (TextView) findViewById(R.id.txtv1);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");

        tx.setTypeface(custom_font);

        TextView IDtxt1 = (TextView) findViewById(R.id.idtxt1);
        TextView IDtxt2 = (TextView) findViewById(R.id.idtxt2);

        IDtxt1.setTypeface(custom_font);
        IDtxt2.setTypeface(custom_font);

        btnAuthenticate = (Button) findViewById(R.id.btnotp);

        data = pref.getString(USER_ID, "");
        final String otp = pref.getString(USER_OTP, "");

        /*if (otp.matches("") && !data.matches("")) {
            // popup is open for mobile number
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.custom_popup);
            final EditText txtRecoverCode = (EditText) dialog.findViewById(R.id.txtRecoverCode);
            final Button btn_recover_otp = (Button) dialog.findViewById(R.id.btn_recover_otp);

            btn_recover_otp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_recover_otp.setClickable(false);
                    String mobNo = txtRecoverCode.getText().toString().trim();
                    if (mobNo.matches("")) {
                        btn_recover_otp.setClickable(true);
                        oDialog("Enter Mobile No", "Close", "", false, _myActivity, "", 0, "");
                    } else {

                        calltoService(mobNo, data);
                    }
                }
            });

            dialog.setCancelable(false);

            dialog.show();

        } else {

            txtCode.setText(otp);

        }*/

        btnAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String otp = txtCode.getText().toString().trim();

                if (otp.isEmpty()) {

                    oDialog("Enter OTP", "Close", "", false, _myActivity, "", 0, "");

                } else if (otp.length() < 4) {
                    oDialog("Enter valid OTP", "Close", "", false, _myActivity, "", 0, "");
                } else {
                    otp = txtCode.getText().toString();

                    if (progressDialog == null) {

                        progressDialog = new ProgressDialog(MobileVerificationActivity.this);
                        progressDialog.setTitle(getString(R.string.loading_title));
                        progressDialog.setMessage(getString(R.string.loading_message));

                    }

                    progressDialog.show();

                    ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

                    Call<ServerResponseOtp> userCall = service.Otp(data, otp);

                    userCall.enqueue(new Callback<ServerResponseOtp>() {
                        @Override
                        public void onResponse(Call<ServerResponseOtp> call, Response<ServerResponseOtp> response) {

                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }

                            //Log.d("onResponse5", "" + response.body().getStatus());
                            // Log.d("Token", "" + response.body().getToken());

                            if (response.body().getStatus() == "true") {

                                String Token = response.body().getToken();
                                Log.d("Retrive Token", Token.toString());
                                setMyPref(ACCESS_TOKEN, Token);
                                setMyPref(ACCESS_STEP, "1");

                                Intent intent = new Intent(MobileVerificationActivity.this, CreateProfileActivity1.class);
                                startActivity(intent);
                                finish();

                            } else {

                                String msg = (String) getApplication().getText(R.string.goeswrong);

                                if (response.body() != null) {
                                    msg = response.body().getMessage();
                                }

                                oDialog(msg, "Ok", "", false, _myActivity, "", 0, "");

                            }
                        }

                        @Override
                        public void onFailure(Call<ServerResponseOtp> call, Throwable t) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
                            // Log.d("onFailure", t.toString());
                        }
                    });
                }
            }
        });
    }

    public void resendCode(View view) {

        txtCode.setCursorVisible(false);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_popup);
        final EditText txtRecoverCode = (EditText) dialog.findViewById(R.id.txtRecoverCode);
        final Button btn_recover_otp = (Button) dialog.findViewById(R.id.btn_recover_otp);

        btn_recover_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                btn_recover_otp.setClickable(false);
                String mobNo = txtRecoverCode.getText().toString().trim();
                if (mobNo.matches("")) {
//                    btn_recover_otp.setClickable(true);
                    oDialog("Enter Mobile No", "Close", "", false, _myActivity, "", 0, "");
                } else {
                    calltoService(mobNo, data);
                }
            }
        });
//        dialog.setCancelable(false);
        dialog.show();
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, String data) {
        super.openDialog6(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, data);
    }

    protected void setMyPref(String key, String value) {
        super.setPref(key, value);
    }

    private void sendNotification(String serverKey, PushRequest notification) {
        ApiInterface service = ApiClient2.getClient2().create(ApiInterface.class);
        Call<PushNotification> pushCall = service.push("key=" + serverKey, notification);

        pushCall.enqueue(new Callback<PushNotification>() {
            @Override
            public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                txtCode.setText(otp);
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t) {
                /* Log.d("FAILURE_REGISTER", "HERE I M");*/
                t.printStackTrace();
            }
        });
    }

    public void calltoService(String mobNo, final String UserId) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(getString(R.string.loading_title));
            progressDialog.setMessage(getString(R.string.loading_message));
        }
        progressDialog.show();
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<ResendOtpModel> userCall = service.resendOtp(UserId, mobNo);
        userCall.enqueue(new Callback<ResendOtpModel>() {
            @Override
            public void onResponse(Call<ResendOtpModel> call, Response<ResendOtpModel> response) {

                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                if (response.body() != null && response.body().getOtp() != null && response.body().getOtp().length() > 0) {
                    setPref(PREF_NAME, "1");
                    setPref(USER_ID, UserId);
                    setPref(USER_OTP, response.body().getOtp());
                    otp = response.body().getOtp();
                    // txtCode.setText(otp);
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    HashMap<String, String> notification = new HashMap<String, String>();
                    notification.put("body", "Your One Time Password: " + response.body().getOtp());
                    notification.put("title", "OTP");
                    notification.put("icon", "appicon");
                    final String accessToken = FirebaseInstanceId.getInstance().getToken();

                    final String serverKey = getResources().getString(R.string.firebase_server_key);
                    sendNotification(serverKey, new PushRequest(accessToken, notification));
                } else {
                    String msg = (String) getApplication().getText(R.string.goeswrong);
                    oDialog(msg, "Ok", "", false, _myActivity, "", 0, "");
                }
            }

            @Override
            public void onFailure(Call<ResendOtpModel> call, Throwable t) {
                Log.d("onFailure", t.toString());
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}