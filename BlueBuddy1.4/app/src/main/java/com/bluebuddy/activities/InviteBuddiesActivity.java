package com.bluebuddy.activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bluebuddy.R;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.EventDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class InviteBuddiesActivity extends BuddyActivity {
    Button inviteBud,btn_invbuddylist;
    Activity _myActivity;
    MyTextView bell_counter;
    String bellcounter, token;
    SharedPreferences pref;
    private static final int RECORD_REQUEST_CODE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_invite_buddies);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.setTitle("INVITE BUDDIES");
        super.notice();
        pref = super.getPref();
        token=pref.getString(ACCESS_TOKEN,"");
        _myActivity = this;

        Bundle b = getIntent().getExtras();
        final EventDetails eventDetails = (EventDetails) b.getSerializable("AllEventsDetails");
        final String backPress = b.containsKey("normal_backpressed") ? "1" : "0";

        Log.d("HEREIM", backPress);

        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        inviteBud = (Button) findViewById(R.id.btn_invbuddy);
        btn_invbuddylist = (Button) findViewById(R.id.btn_invbuddylist);
        btn_invbuddylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InviteBuddiesActivity.this, InviteBuddyContactList.class);
                i.putExtra("AllEventsDetails", eventDetails);
                i.putExtra("normal_backpressed", backPress);
                startActivity(i);
            }
        });

        inviteBud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //today23
                int permission = ContextCompat.checkSelfPermission(InviteBuddiesActivity.this, Manifest.permission.READ_CONTACTS);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    Log.i("HELLO", "Permission to contacts denied");

                    if (ActivityCompat.shouldShowRequestPermissionRationale(InviteBuddiesActivity.this, Manifest.permission.READ_CONTACTS)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(InviteBuddiesActivity.this);
                        builder.setMessage("Permission to access the contacts is required for this app to invite buddy.")
                                .setTitle("Permission required");

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("HELLO", "Clicked");
                               // makeRequest();
                                //
                                ActivityCompat.requestPermissions(InviteBuddiesActivity.this,new String[]{Manifest.permission.READ_CONTACTS},RECORD_REQUEST_CODE);
                                Intent i = new Intent(InviteBuddiesActivity.this, InviteBuddyContactListActivity.class);
                                i.putExtra("AllEventsDetails", eventDetails);
                                i.putExtra("normal_backpressed", backPress);
                                startActivity(i);

                                //
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        makeRequest();
                    }
                }
                else if(permission == PackageManager.PERMISSION_GRANTED){
                    Intent i = new Intent(InviteBuddiesActivity.this, InviteBuddyContactListActivity.class);
                    i.putExtra("AllEventsDetails", eventDetails);
                    i.putExtra("normal_backpressed", backPress);
                    startActivity(i);
                }

                //today23
              /*  Intent i = new Intent(InviteBuddiesActivity.this, InviteBuddyContactListActivity.class);
                i.putExtra("AllEventsDetails", eventDetails);
                i.putExtra("normal_backpressed", backPress);
                startActivity(i);*/
            }
        });
        bellcount();
    }

    private void makeRequest() {
        ActivityCompat.requestPermissions(InviteBuddiesActivity.this,new String[]{Manifest.permission.READ_CONTACTS},RECORD_REQUEST_CODE);
    }

    private void bellcount() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllNotice> userCall = service.allnotice1("Bearer " + token);

        userCall.enqueue(new Callback<AllNotice>() {
            @Override
            public void onResponse(Call<AllNotice> call, Response<AllNotice> response) {
                //    if (mProgressDialog.isShowing())
                //      mProgressDialog.dismiss();
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

                //  Log.d("TOKEN:", "" + token);
                //     ArrayList<AllNotice> allNotices = response.body().getNotification_details();
                //    allNoticeAdapter.updateAllNotice(response.body().getNotification_details());
            }

            @Override
            public void onFailure(Call<AllNotice> call, Throwable t) {

            }
        });
    }
}
