package com.bluebuddy.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class Categories_bluemarketActivity2 extends BuddyActivity {
    private Button Charter, Courses, Product, Service;
    private SharedPreferences pref;
    private String token, bellcounter, category;
    MyTextView bell_counter;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_categories_bluemarket);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.notice();
        super.setImageResource1(5);
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        category = "";
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        TextView tx = (TextView) findViewById(R.id.idtxt1);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");
        tx.setTypeface(custom_font);
        Charter = (Button) findViewById(R.id.bmcharterid);
        back = (ImageView) findViewById(R.id.imgNotification2);
        Courses = (Button) findViewById(R.id.bmcourseid);
        Product = (Button) findViewById(R.id.bmproductid);
        Service = (Button) findViewById(R.id.bmservicesid);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Charter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent i = new Intent(Categories_bluemarketActivity2.this, BlueMarketActivity22.class);
                startActivity(i);*/
                Intent intent = new Intent(Categories_bluemarketActivity2.this, BlueMarketCharterPicActivity.class);
                intent.putExtra("bmcharter", "2");
                startActivity(intent);
            }
        });
        Courses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent iI = new Intent(Categories_bluemarketActivity2.this, BlueMarketActivity32.class);
                startActivity(iI);*/
                Intent iI = new Intent(Categories_bluemarketActivity2.this, BlueMarketCoursePicActivity.class);
                iI.putExtra("category", category);
                iI.putExtra("bmcourse", "2");
                startActivity(iI);
            }
        });
        Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent iT = new Intent(Categories_bluemarketActivity2.this, BlueMarketActivity42.class);
                startActivity(iT);*/
                Intent intent = new Intent(Categories_bluemarketActivity2.this, BlueMarketPicActivity.class);
                intent.putExtra("bmproduct", "2");
                startActivity(intent);
            }
        });
        Service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent iT = new Intent(Categories_bluemarketActivity2.this, ServicesOfferedListActivity.class);
                startActivity(iT);*/
                Intent intent = new Intent(Categories_bluemarketActivity2.this, ActivityAddYourServices.class);
                intent.putExtra("bmserv", "2");
                startActivity(intent);
            }
        });
        bellcount();
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Categories_bluemarketActivity2.this, MyListingActivity.class);
        startActivity(i);
        finish();
    }
}
