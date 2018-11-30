package com.bluebuddy.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bluebuddy.R;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class BoatCharterAdvFormActivity extends BuddyActivity {
    Button Boatcadv;
    Activity _myActivity;
    EditText Bcadvname;
    MyTextView bell_counter;
    String bellcounter, token;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_boat_charter_adv_form);
        super.onCreate(savedInstanceState);
        super.notice();
        super.initialize();
        super.setTitle("BLUE MARKET");
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");


        _myActivity = this;

        Bcadvname = (EditText) findViewById(R.id.bcadname);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");

        TextView IDtxt1 = (TextView) findViewById(R.id.idtxt1);
        TextView IDtxt2 = (TextView) findViewById(R.id.idtxt2);
        TextView IDtxt3 = (TextView) findViewById(R.id.idtxt3);
        TextView IDtxt4 = (TextView) findViewById(R.id.idtxt4);
        TextView IDtxt5 = (TextView) findViewById(R.id.idtxt5);
        IDtxt1.setTypeface(custom_font);
        IDtxt2.setTypeface(custom_font);
        IDtxt3.setTypeface(custom_font);
        IDtxt4.setTypeface(custom_font);
        IDtxt5.setTypeface(custom_font);
        Boatcadv = (Button) findViewById(R.id.btbcadvf);
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        Boatcadv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String BCFname = Bcadvname.getText().toString().trim();
                this.BoatCpop(BCFname);
            }

            private void BoatCpop(final String BCFname) {
                if (BCFname.isEmpty()) {
                    final String rc2 = "Categories_bluemarketActivity";
                    oDialog("Thanks for Listing with us.", "Submit", "", true, _myActivity, rc2, 0);
                } else {

                }
            }

        });
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

}
