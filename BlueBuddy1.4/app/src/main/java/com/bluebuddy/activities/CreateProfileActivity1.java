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
import android.widget.TextView;

import com.bluebuddy.R;
import com.bluebuddy.Utilities.MyUtility;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.ServerResponseCp1;
import com.bluebuddy.interfaces.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_STEP;
import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class CreateProfileActivity1 extends BuddyActivity {
    EditText txtFirstName, txtLastName;
    Activity _myActivity;
    Button btflname;
    private SharedPreferences pref;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_create_profile1);
        super.onCreate(savedInstanceState);
        this.initializeElements();
        _myActivity = this;
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
    }

    private void initializeElements() {
        txtFirstName = (EditText) findViewById(R.id.fnedt);
        txtLastName = (EditText) findViewById(R.id.lnedt);
        TextView tx = (TextView) findViewById(R.id.txtv1);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");
        tx.setTypeface(custom_font);
        TextView IDtxt1 = (TextView) findViewById(R.id.idtxt1);
        TextView IDtxt2 = (TextView) findViewById(R.id.idtxt2);
        IDtxt1.setTypeface(custom_font);
        IDtxt2.setTypeface(custom_font);
        btflname = (Button) findViewById(R.id.cpflname);

        btflname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = txtFirstName.getText().toString().trim();
                String lastname = txtLastName.getText().toString().trim();

                if (firstname.isEmpty()) {
                    oDialog("Enter first name", "Close", "", false, _myActivity, "", 0);
                } else if (!firstname.matches(MyUtility.namePattern)) {
                    oDialog("Enter a valid First name", "Close", "", false, _myActivity, "", 0);
                } else if (lastname.isEmpty()) {
                    oDialog("Enter last name", "Close", "", false, _myActivity, "", 0);
                } else if (!lastname.matches(MyUtility.namePattern)) {
                    oDialog("Enter a valid last name", "Close", "", false, _myActivity, "", 0);
                } else {
                    firstname = txtFirstName.getText().toString();
                    lastname = txtLastName.getText().toString();

                    ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                    // Call<ServerResponseCp1> userCall = service.Cp1("Bearer " + token, "put", firstname, lastname, 2);
                    Call<ServerResponseCp1> userCall = service.Cp1("Bearer " + token, firstname, lastname, 2);

                    userCall.enqueue(new Callback<ServerResponseCp1>() {
                        @Override
                        public void onResponse(Call<ServerResponseCp1> call, Response<ServerResponseCp1> response) {
                            Log.d("onResponse", "" + response.body().getMessage());
                            if (response.body().getStatus() == "true") {
                                setMyPref(ACCESS_STEP, "2");
                               /* Log.d("onResponse", "" + response.body().getStatus());
                                Log.d("onResposeFirstName", "" + response.body().getFirst_name());
                                Log.d("onResponseLastName", "" + response.body().getFirst_name());*/
                                Intent intent = new Intent(CreateProfileActivity1.this, RUCActivity.class);

                                startActivity(intent);
//                                finish();
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
        });

    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    protected void setMyPref(String key, String value) {
        super.setPref(key, value);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
