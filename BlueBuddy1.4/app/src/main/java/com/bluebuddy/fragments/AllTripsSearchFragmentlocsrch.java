package com.bluebuddy.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.bluebuddy.R;
import com.bluebuddy.activities.CreateEventActivity3;
import com.bluebuddy.adapters.AllEventAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllTrip;
import com.bluebuddy.models.EventDetails;
import com.bluebuddy.models.PeopleDetail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllTripsSearchFragmentlocsrch extends Fragment {
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private AllEventAdapter allEventAdapter;
    private Activity _activity;
    private List<PeopleDetail> PEOPLEslist;
    private ArrayList<EventDetails> loc_srch;

    private Context _context;
    private SharedPreferences pref;
    private String token, category, fd, td,curru;
    private String textcat = "";
    private String texttoken = "";
    Calendar myCalendar = Calendar.getInstance();
    Button frm_date, to_date, dateaction;
    EditText edtfrmdate, edttodate;
    private MyTextView txtshid1;



    /*public AllTripsSearchFragment(String tkn,String category) {
        this.token = tkn;
        this.category=category;

    }*/
//
public static AllTripsSearchFragmentlocsrch createInstance(int i) {
    AllTripsSearchFragmentlocsrch allTripsSearchFragment = new AllTripsSearchFragmentlocsrch();
    Bundle bundle = new Bundle();
    allTripsSearchFragment.setArguments(bundle);
    return allTripsSearchFragment;
}

    public static AllTripsSearchFragmentlocsrch createInstance(String category) {
        AllTripsSearchFragmentlocsrch allTripsSearchFragment = new AllTripsSearchFragmentlocsrch();
        Bundle bundle = new Bundle();
        bundle.putString("ABOUT", category);
        allTripsSearchFragment.setArguments(bundle);
        return allTripsSearchFragment;
    }


    public static AllTripsSearchFragmentlocsrch createInstance(String cat, ArrayList<EventDetails> loc_srch) {
        AllTripsSearchFragmentlocsrch allTripsSearchFragment = new AllTripsSearchFragmentlocsrch();
        Bundle bundle = new Bundle();
        bundle.putString("CAT", cat);
     //   bundle.putString("TOKEN", token);
        bundle.putSerializable("LOC_SEARCH", loc_srch);
        allTripsSearchFragment.setArguments(bundle);
        return allTripsSearchFragment;
    }

//

    /*public AllTripsSearchFragment(String tkn) {
        this.token = tkn;

    }*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        Log.d("ABOUT", "" + bundle.getString("CAT"));
        Log.d("ABOUT2", "" + bundle.getString("TOKEN"));
        textcat = bundle.getString("CAT");
        texttoken = bundle.getString("TOKEN");




    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment, container, false);
        getAllTrip();

        frm_date = (Button) rootView.findViewById(R.id.frm_date);
        to_date = (Button) rootView.findViewById(R.id.to_date);
        edtfrmdate = (EditText) rootView.findViewById(R.id.edtfrmdate);
        edttodate = (EditText) rootView.findViewById(R.id.edttodate);
        dateaction = (Button) rootView.findViewById(R.id.dateaction);
        txtshid1=(MyTextView)rootView.findViewById(R.id.txtshid1);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }
        mLayoutManager = new LinearLayoutManager(_context);
        recyclerView.setLayoutManager(mLayoutManager);
        final Bundle bundle = getArguments();
        token=bundle.getString("TOKEN");
        curru=bundle.getString("CURRU");
        if(bundle.containsKey("LOC_SEARCH")){
          //  Toast.makeText(getActivity(),"hiiiiii",Toast.LENGTH_LONG).show();
            loc_srch=(ArrayList<EventDetails>) bundle.getSerializable("LOC_SEARCH");

            allEventAdapter = new AllEventAdapter(_activity, _context, loc_srch, token,curru);
            recyclerView.setAdapter(allEventAdapter);
            allEventAdapter.updateAllTrip(loc_srch);
           // allEventAdapter.notifyDataSetChanged();
        }
        else {
            allEventAdapter = new AllEventAdapter(_activity, _context, null, token, curru);
            recyclerView.setAdapter(allEventAdapter);
            allEventAdapter.notifyDataSetChanged();
        }
        //  R.layout.fragment, container, false);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action

                Intent intent = new Intent(getActivity(), CreateEventActivity3.class);
                intent.putExtra("pass",1);
                intent.putExtra("interestList",textcat);
                startActivity(intent);
            }
        });

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
                /*String myFormat = "dd.MM.yyyy";*/
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
                // DatePickerDialog datePickerDialog = new DatePickerDialog(AllEventsActivity.this, date, myCalendar
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        EditText edtfrmdate, edttodate;



        //

        dateaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                Call<AllTrip> userCall = service.srchdt("Bearer " + texttoken, category, fd, td);
                userCall.enqueue(new Callback<AllTrip>() {
                    @Override
                    public void onResponse(Call<AllTrip> call, Response<AllTrip> response) {

                        //   allEventAdapter.updateAllTrip(response.body().getDetails());
                        //   allEventAdapter = new AllEventAdapter(_activity, _context, null, token);
                        //    mRecyclerView.setAdapter(allEventAdapter);
                        //  allEventAdapter.notifyDataSetChanged();
                        //
                           allEventAdapter.updateAllTrip(response.body().getDetails());
                      /*  allEventAdapter = new AllEventAdapter(_activity, _context, response.body().getDetails(), token);
                        recyclerView.setAdapter(allEventAdapter);
                        allEventAdapter.notifyDataSetChanged();*/


                        //
                        /*//peopleSearchAdapter.updateAllPeople(response.body().getUser_details_arr());

                        peopleSearchAdapter = new PeopleSearchAdapter(_activity, _context, response.body().getUser_details_arr(), token);
                        mRecyclerView.setAdapter(peopleSearchAdapter);
                        peopleSearchAdapter.notifyDataSetChanged();*/
                    }

                    @Override
                    public void onFailure(Call<AllTrip> call, Throwable t) {
                        Log.d("onFailure", t.toString());
                    }
                });
            }
        });
        return rootView;



    }


    public void getAllTrip(){

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllTrip> userCall = service.alltrip("Bearer "+texttoken,textcat);

        userCall.enqueue(new Callback<AllTrip>() {
            @Override
            public void onResponse(Call<AllTrip> call, Response<AllTrip> response) {
                if(response.body().getMessage()==null){
                // if (mProgressDialog.isShowing())
                //    mProgressDialog.dismiss();
                    txtshid1.setVisibility(View.GONE);
             /*   Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("onResponse:", "" + response.body().getDetails());
                Log.d("TOKEN:", "" + token);*/
                allEventAdapter.updateAllTrip(response.body().getDetails());
            }
                 else if(response.body().getMessage().equals("No data found.")){
                    txtshid1.setVisibility(View.VISIBLE);
                    txtshid1.setText("No trips found!");
                }
            }
            @Override
            public void onFailure(Call<AllTrip> call, Throwable t) {
               // if (mProgressDialog.isShowing())
                   // mProgressDialog.dismiss();

            }
        });
    }

    /*@Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }*/

}

