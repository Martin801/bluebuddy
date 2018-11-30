package com.bluebuddy.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.bluebuddy.R;
import com.bluebuddy.adapters.AllEventAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllTrip;
import com.bluebuddy.models.EventDetails;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.USER_ID;

public class AllEventsActivity extends BuddyActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageView back;
    private ArrayList<EventDetails> EVENTList;
    private AllEventAdapter allEventAdapter;
    private Activity _activity;
    private Context _context;
    private SharedPreferences pref;
    private String token, category, fd, td, curru;
    Button frm_date, to_date, dateaction;
    EditText edtfrmdate, edttodate;
    ProgressDialog mProgressDialog;
    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_all_events);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.loader();
        super.notice();
        super.setImageResource1(1);
        super.setTitle("TRIPS");
        _activity = this;
        _context = this;

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        curru = pref.getString(USER_ID, "");
        frm_date = (Button) findViewById(R.id.frm_date);
        to_date = (Button) findViewById(R.id.to_date);
        edtfrmdate = (EditText) findViewById(R.id.edtfrmdate);
        edttodate = (EditText) findViewById(R.id.edttodate);
        dateaction = (Button) findViewById(R.id.dateaction);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;
        Bundle b = getIntent().getExtras();

        if (b != null) {
            category = b.getString("interestList");
        }


        //from
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar);
            }

            private void updateLabel(Calendar myCalendar) {

                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                edtfrmdate.setText(sdf.format(myCalendar.getTime()));
                fd = sdf.format(myCalendar.getTime());
            }

        };
        //to
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1(myCalendar);
            }

            private void updateLabel1(Calendar myCalendar) {
                /*String myFormat = "dd.MM.yyyy";*/
                String myFormat = "yyyy-MM-dd";

                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                edttodate.setText(sdf.format(myCalendar.getTime()));
                td = sdf.format(myCalendar.getTime());
            }

        };
        frm_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(AllEventsActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AllEventsActivity.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        EditText edtfrmdate, edttodate;

        dateaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                Call<AllTrip> userCall = service.srchdt("Bearer " + token, category, fd, td);
                userCall.enqueue(new Callback<AllTrip>() {
                    @Override
                    public void onResponse(Call<AllTrip> call, Response<AllTrip> response) {

                        allEventAdapter.updateAllTrip(response.body().getDetails());

                    }

                    @Override
                    public void onFailure(Call<AllTrip> call, Throwable t) {
                        Log.d("onFailure", t.toString());
                    }
                });
            }
        });
        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AllEventsActivity.this, CreateProfileActivity2.class);
                startActivity(i);
            }
        });

        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllEventsActivity.this, CreateEventActivity3.class);
                startActivity(intent);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_event);
        mRecyclerView.setNestedScrollingEnabled(false);
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
        }
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        allEventAdapter = new AllEventAdapter(_activity, _context, null, token, curru);
        mRecyclerView.setAdapter(allEventAdapter);
        allEventAdapter.notifyDataSetChanged();
        getAllTrip();
    }

    public void getAllTrip() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllTrip> userCall = service.alltrip("Bearer " + token, category);

        userCall.enqueue(new Callback<AllTrip>() {
            @Override
            public void onResponse(Call<AllTrip> call, Response<AllTrip> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("onResponse:", "" + response.body().getDetails());
                Log.d("TOKEN:", "" + token);

                allEventAdapter.updateAllTrip(response.body().getDetails());
            }

            @Override
            public void onFailure(Call<AllTrip> call, Throwable t) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(AllEventsActivity.this, CreateProfileActivity2.class);
        startActivity(i);
    }

}
