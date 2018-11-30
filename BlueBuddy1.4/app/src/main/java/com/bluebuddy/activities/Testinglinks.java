package com.bluebuddy.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluebuddy.R;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class Testinglinks extends BuddyActivity {
    Button slfb, sltwt, slinst, slgl, slnxt;
    private SharedPreferences pref;
    private String token;
    ImageView back;
    private Activity _activity;
    private Context _context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_create_profile5test);
        super.onCreate(savedInstanceState);
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        back = (ImageView) findViewById(R.id.imgNotification2);
        _activity = this;
        _context = this;
        slfb = (Button) findViewById(R.id.slfbid);
        sltwt = (Button) findViewById(R.id.sltwtid);
        slinst = (Button) findViewById(R.id.slinstid);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");
        TextView IDtxt1 = (TextView) findViewById(R.id.idtxt1);
        TextView IDtxt2 = (TextView) findViewById(R.id.idtxt2);
        IDtxt1.setTypeface(custom_font);
        IDtxt2.setTypeface(custom_font);
        slgl = (Button) findViewById(R.id.slglid);
        slnxt = (Button) findViewById(R.id.btn_sociallinkid);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Testinglinks.this, MyAccountMenu.class);
                startActivity(i);
            }
        });
        slnxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    public void btncp5nxt() {
        Intent ii = new Intent(Testinglinks.this, CreateProfileActivity6.class);


        startActivity(ii);
    }


}
