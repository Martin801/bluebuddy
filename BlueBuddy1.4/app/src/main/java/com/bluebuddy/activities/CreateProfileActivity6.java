package com.bluebuddy.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluebuddy.R;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.ServerResponseCp6;
import com.bluebuddy.interfaces.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_STEP;
import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class CreateProfileActivity6 extends BuddyActivity {
    EditText cp6weblnk;
    Button cp6nxt, cp6skp;
    Activity _myActivity;
    ImageView back;
    private SharedPreferences pref;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_create_profile6);
        super.onCreate(savedInstanceState);
        this.initializeElements();
        _myActivity = this;
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
    }

    private void initializeElements() {
        back = findViewById(R.id.imgNotification2);
        cp6weblnk = findViewById(R.id.weblnkid);
        cp6nxt = findViewById(R.id.weblnknxtid);
        cp6skp = findViewById(R.id.weblnkskpid);
        TextView tx = findViewById(R.id.idtxt1);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");
        tx.setTypeface(custom_font);
        cp6weblnk.setTypeface(custom_font);

        back.setOnClickListener(v -> onBackPressed());
        cp6nxt.setOnClickListener(v -> cp6nxt());
        cp6skp.setOnClickListener(v -> cp6skp());
    }

    public void cp6nxt() {

        String wblnk = cp6weblnk.getText().toString().trim();
        if (!URLUtil.isValidUrl(wblnk)) {
            oDialog("Enter your valid website link", "Close", "", false, _myActivity, "", 0);
        } else if (wblnk.isEmpty()) {
            oDialog("Enter your website link", "Close", "", false, _myActivity, "", 0);
        } else {
            wblnk = cp6weblnk.getText().toString();
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<ServerResponseCp6> userCall = service.Cp6("Bearer " + token, "put", wblnk, 7);
            userCall.enqueue(new Callback<ServerResponseCp6>() {
                @Override
                public void onResponse(Call<ServerResponseCp6> call, Response<ServerResponseCp6> response) {
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (response.body().getStatus() == "true") {
                        //   Log.d("onResponse", "" + response.body().getEmail());
                        Log.d("onResponse", "" + response.body().getStatus());
                        setMyPref(ACCESS_STEP, "7"); // CreateProfile7
                        Intent intent = new Intent(CreateProfileActivity6.this, LocationActivity.class);
                        startActivity(intent);
//                        finish();
                    } else {
                        Log.d("onResponse", "" + response.body().getStatus());
                        logoutAndSignInPage(CreateProfileActivity6.this);
                    }
                }

                @Override
                public void onFailure(Call<ServerResponseCp6> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                }
            });
        }

    }

    public void cp6skp() {
        setMyPref(ACCESS_STEP, "7");
        Intent jj = new Intent(CreateProfileActivity6.this, LocationActivity.class);

        startActivity(jj);
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
//        setMyPref(ACCESS_STEP, "5"); // CreateProfile5
//        Intent i = new Intent(CreateProfileActivity6.this, CreateProfileActivity5.class);
//
//        startActivity(i);

        finish();
    }
}
