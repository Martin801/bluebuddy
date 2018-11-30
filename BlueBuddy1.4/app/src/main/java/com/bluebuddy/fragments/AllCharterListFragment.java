package com.bluebuddy.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
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

import com.bluebuddy.R;
import com.bluebuddy.activities.BlueMarketCharterPicActivity;
import com.bluebuddy.adapters.AllEventAdapter;
import com.bluebuddy.adapters.BlueCharterAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllCharter;
import com.bluebuddy.models.AllCharterDetails;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllCharterListFragment extends Fragment {
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private AllEventAdapter allEventAdapter;
    private Activity _activity;

    private ArrayList<AllCharterDetails> BCCList;
    private BlueCharterAdapter blueCharterAdapter;
   // private List<PeopleDetail> PEOPLEslist;
   private ProgressDialog mProgressDialog;
    private Context _context;
    private SharedPreferences pref;
    private String token, category, fd, td,curru;
    private String textcat = "";
    private String texttoken = "";
    private MyTextView txtshid1;
    private static final int RECORD_REQUEST_CODE = 101;
 //   Calendar myCalendar = Calendar.getInstance();
    Button frm_date, to_date, dateaction;
 //   EditText edtfrmdate, edttodate;

    /*public AllTripsSearchFragment(String tkn,String category) {
        this.token = tkn;
        this.category=category;

    }*/
//
public static AllCharterListFragment createInstance(int i) {
    AllCharterListFragment allTripsSearchFragment = new AllCharterListFragment();
    Bundle bundle = new Bundle();
    allTripsSearchFragment.setArguments(bundle);
    return allTripsSearchFragment;
}

    public static AllCharterListFragment createInstance(String category) {
        AllCharterListFragment allTripsSearchFragment = new AllCharterListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ABOUT", category);
        allTripsSearchFragment.setArguments(bundle);
        return allTripsSearchFragment;
    }

    public static AllCharterListFragment createInstance(String category, String token,String curru) {
        AllCharterListFragment allTripsSearchFragment = new AllCharterListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("CAT", category);
        bundle.putString("TOKEN", token);
        bundle.putString("CURRU",curru);
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
        View rootView = inflater.inflate(R.layout.fragmentcharter, container, false);
        getAllBoat();

     /*   frm_date = (Button) rootView.findViewById(R.id.frm_date);
        to_date = (Button) rootView.findViewById(R.id.to_date);
        edtfrmdate = (EditText) rootView.findViewById(R.id.edtfrmdate);
        edttodate = (EditText) rootView.findViewById(R.id.edttodate);
        dateaction = (Button) rootView.findViewById(R.id.dateaction);*/

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }
        mLayoutManager = new LinearLayoutManager(_context);
        recyclerView.setLayoutManager(mLayoutManager);
        Bundle bundle = getArguments();



            if (bundle.containsKey("CURRU"))
                curru = bundle.getString("CURRU");
             //   blueCharterAdapter = new BlueCharterAdapter(_activity, _context, null, token, curru);
        blueCharterAdapter = new BlueCharterAdapter(_activity, _context, null, texttoken, curru);

        recyclerView.setAdapter(blueCharterAdapter);
        blueCharterAdapter.notifyDataSetChanged();
        txtshid1=(MyTextView)rootView.findViewById(R.id.txtshid1);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action


                Intent intent = new Intent(getActivity(), BlueMarketCharterPicActivity.class);
                intent.putExtra("bmcharter","1");
                startActivity(intent);
            }
        });
        //  R.layout.fragment, container, false);

       /* dateaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                Call<AllTrip> userCall = service.srchdt("Bearer " + texttoken, category, fd, td);
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
        });*/

        return rootView;
    }


    public void getAllBoat(){

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
//        Call<AllTrip> userCall = service.alltrip("Bearer "+texttoken,textcat);
        Call<AllCharter> userCall = service.allCharter("Bearer " + texttoken);

        userCall.enqueue(new Callback<AllCharter>() {
            @Override
            public void onResponse(Call<AllCharter> call, Response<AllCharter> response) {
if(response.body().getStatus()!=false){
    txtshid1.setVisibility(View.GONE);
                // if (mProgressDialog.isShowing())
                //    mProgressDialog.dismiss();

             /*   Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("onResponse:", "" + response.body().getDetails());
                Log.d("TOKEN:", "" + token);*/
                blueCharterAdapter.updateAllCharter(response.body().getCharter_list());
                // allEventAdapter.updateAllTrip(response.body().getDetails());
            }
                else if(response.body().getStatus()!=true){
                    txtshid1.setVisibility(View.VISIBLE);
    txtshid1.setText("No boat rentals/charters found!");
                }
            }
            @Override
            public void onFailure(Call<AllCharter> call, Throwable t) {
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

