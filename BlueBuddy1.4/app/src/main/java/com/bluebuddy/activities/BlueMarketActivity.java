package com.bluebuddy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bluebuddy.R;
import com.bluebuddy.adapters.BlueMarketAdapter;
import com.bluebuddy.models.BlueMarketItems;

import java.util.ArrayList;

public class BlueMarketActivity extends BuddyActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<BlueMarketItems> BMCList;
    private BlueMarketAdapter blueMarketAdapter;
    private Activity _activity;
    private Context _context;
    private ImageView back;
    private ProgressDialog mProgressDialog;
    //


    String[] BMCname = {"Scuba Diving Course", "Fishing Course", "Free Diving Course"};
    String[] BMCprice = {"$30", "$25", "$40"};
    String[] BMCloc = {"London Docklands", "London Docklands", "London Docklands"};
    String[] BMCposted = {"Martin jones", "Steve Jobes", "Steve Jobes"};

   /* int[] BMCpic = {
            R.drawable.bluemarketpic,
            R.drawable.bluemarketpic2,
            R.drawable.bluemarketpic};
*/
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_blue_market);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.setTitle("BLUE MARKET");
        _activity = this;
        _context = this;


        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BlueMarketActivity.this, CreateProfileActivity2.class);
                startActivity(i);
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.blue_market_recycler);
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
        }
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        BMCList = new ArrayList<>();
     /*   for (int i = 0; i < BMCname.length; i++) {
          //  BlueMarketItems blueMarketItems = new BlueMarketItems(BMCname[i], BMCprice[i], BMCloc[i], BMCposted[i], i + 1, BMCpic[i]);
           // BMCList.add(blueMarketItems);
        }*/
        blueMarketAdapter = new BlueMarketAdapter(_activity, _context, BMCList);
        mRecyclerView.setAdapter(blueMarketAdapter);
        blueMarketAdapter.notifyDataSetChanged();
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final String btnText3, final String btnText4, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog4(msg, btnText, btnText2, btnText3, btnText4, _redirect, activity, _redirectClass, layout);
    }

    public void abc() {

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(BlueMarketActivity.this, CreateProfileActivity2.class);
        startActivity(i);
    }
}
