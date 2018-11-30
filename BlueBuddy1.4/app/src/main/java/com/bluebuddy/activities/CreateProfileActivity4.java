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

import com.bluebuddy.R;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.classes.ServerResponseCp4;
import com.bluebuddy.interfaces.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_STEP;
import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class CreateProfileActivity4 extends BuddyActivity {

    EditText cp4tellst;
    Button cp4nxt;
    Activity _myActivity;
    ImageView back;
    private SharedPreferences pref;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.setLayout(R.layout.activity_create_profile4);
        super.onCreate(savedInstanceState);
        this.initializeElements();
        _myActivity = this;
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");

    }

    private void initializeElements() {

        cp4tellst = findViewById(R.id.tellstid);
        MyTextView tx = findViewById(R.id.txtv1);
        back = findViewById(R.id.imgNotification2);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");
        tx.setTypeface(custom_font);
        cp4tellst.setTypeface(custom_font);

        cp4nxt = findViewById(R.id.btn_tellstid);

        back.setOnClickListener(v -> onBackPressed());

        cp4nxt.setOnClickListener(v -> btncp4nxt());

    }

    public void btncp4nxt() {
        String abuser = cp4tellst.getText().toString().trim();

        if (abuser.isEmpty()) {
            oDialog("Enter something about yourself", "Close", "", false, _myActivity, "", 0);
        } else if (abuser.length() > 350) {
            oDialog("Character limit 350 ", "Close", "", false, _myActivity, "", 0);
        } else {
            abuser = cp4tellst.getText().toString();

            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

            Call<ServerResponseCp4> userCall = service.Cp4("Bearer " + token, abuser, 5);
            userCall.enqueue(new Callback<ServerResponseCp4>() {
                @Override
                public void onResponse(Call<ServerResponseCp4> call, Response<ServerResponseCp4> response) {

                    // Log.d("onResponse", "" + response.body().getMessage());

                    if (response.body().getStatus() == "true") {
                        //  Log.d("onResponse", "" + response.body().getStatus());

                        setMyPref(ACCESS_STEP, "5"); // CreateProfile5
                        Intent intent = new Intent(CreateProfileActivity4.this, CreateProfileActivity5.class);
                        startActivity(intent);
//                        finish();
                    } else {
                        Log.d("onResponse", "" + response.body().getStatus());
                        logoutAndSignInPage(CreateProfileActivity4.this);
                    }
                }

                @Override
                public void onFailure(Call<ServerResponseCp4> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                }
            });
        }
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    protected void setMyPref(String key, String value) {
        super.setPref(key, value);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        setMyPref(ACCESS_STEP, "2");
//        Intent i=new Intent(CreateProfileActivity4.this,RUCActivity.class);
//        startActivity(i);

        finish();
    }
}
