package com.bluebuddy.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bluebuddy.R;
import com.bluebuddy.adapters.BlueMarketProductAdapter2;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.AllProduct;
import com.bluebuddy.models.AllProductDetails;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.USER_ID;

public class BlueMarketActivity42 extends BuddyActivity {
    private RecyclerView mRecyclerView;
    private static final int RECORD_REQUEST_CODE = 101;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<AllProductDetails> BMCList;
    private BlueMarketProductAdapter2 blueMarketProductAdapter;
    private Activity _activity;
    private Context _context;
    private ImageView back;
    private MyTextView validationmsg;
    private MyTextView bell_counter;
    private Button fab1;
    private SharedPreferences pref;
    private String token,category,curru,bellcounter;
    private ProgressDialog mProgressDialog;
    private  FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_blue_market4);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.notice();
        super.setTitle("BLUE MARKET PRODUCTS");
        //today23
        int permission = ContextCompat.checkSelfPermission(BlueMarketActivity42.this, Manifest.permission.ACCESS_FINE_LOCATION );
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("HELLO", "Permission to location denied");

            if (ActivityCompat.shouldShowRequestPermissionRationale(BlueMarketActivity42.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BlueMarketActivity42.this);
                builder.setMessage("Permission to access the location is required for this app to access your location.")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("HELLO", "Clicked");
                        // makeRequest();
                        //
                        ActivityCompat.requestPermissions(BlueMarketActivity42.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},RECORD_REQUEST_CODE);

                        //
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                ActivityCompat.requestPermissions(BlueMarketActivity42.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RECORD_REQUEST_CODE);

            }
        }

        //today23
        super.loader();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;

        _activity = this;
        _context = this;
        Bundle b = getIntent().getExtras();

        if(b != null) {
            category = b.getString("interestList");
        }
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        curru = pref.getString(USER_ID, "");
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        validationmsg=(MyTextView)findViewById(R.id.validationmsg);
        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BlueMarketActivity42.this, Categories_bluemarketActivity2.class);
                startActivity(i);
            }
        });
        fab1=(Button)findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                try {
                    String rc = "BlueMarketPicActivity";
                    intent = new Intent(BlueMarketActivity42.this, Class.forName("com.example.admin.bluebuddy.activities."+rc));

                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
         fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(BlueMarketActivity42.this, BlueMarketPicActivity.class);
                intent.putExtra("bmproduct","2");
                startActivity(intent);

            }
        });


        mRecyclerView = (RecyclerView) findViewById(R.id.blue_market_recycler);
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
        }
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        blueMarketProductAdapter = new BlueMarketProductAdapter2(_activity, _context, null, token, curru);
        mRecyclerView.setAdapter(blueMarketProductAdapter);
        blueMarketProductAdapter.notifyDataSetChanged();
        getAllProduct();
        bellcount();
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final String btnText3, final String btnText4, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog4(msg, btnText, btnText2, btnText3, btnText4, _redirect, activity, _redirectClass, layout);
    }

    public void getAllProduct(){

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllProduct> userCall = service.myProduct("Bearer "+token);

        userCall.enqueue(new Callback<AllProduct>() {
            @Override
            public void onResponse(Call<AllProduct> call, Response<AllProduct> response) {
                if(mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                if(!response.body().equals(null)) {

                    validationmsg.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);
                    fab1.setVisibility(View.GONE);
                    Log.d("STATUS", String.valueOf(response.body().getStatus()));
                    Log.d("onResponse:", "" + response.body().getProduct_list());
                    Log.d("TOKEN:", "" + token);

                    blueMarketProductAdapter.updateAllProduct(response.body().getProduct_list());
                }
                else if(response.body().equals(null)) {

                    validationmsg.setVisibility(View.VISIBLE);

                    validationmsg.setText("No product found!");
                    fab1.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.GONE);
                    Log.d("STATUS", String.valueOf(response.body().getStatus()));
                    Log.d("onResponse:", "" + response.body().getProduct_list());
                    Log.d("TOKEN:", "" + token);

                }
            }

            @Override
            public void onFailure(Call<AllProduct> call, Throwable t) {

            }
        });
    }
    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(BlueMarketActivity42.this, Categories_bluemarketActivity2.class);
        startActivity(i);
    }
    private void bellcount() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllNotice> userCall = service.allnotice1("Bearer "+token);

        userCall.enqueue(new Callback<AllNotice>() {
            @Override
            public void onResponse(Call<AllNotice> call, Response<AllNotice> response) {
                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("onResponse:", "" + response.body().getNotification_details());
                bellcounter = response.body().getCounter();
                if(bellcounter.equals("0")){
                    bell_counter.setVisibility(View.GONE);
                }
                else if(!bellcounter.equals("0")){
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
