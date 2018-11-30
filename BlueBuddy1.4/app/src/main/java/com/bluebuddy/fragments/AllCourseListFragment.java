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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bluebuddy.R;
import com.bluebuddy.activities.BlueMarketCoursePicActivity;
import com.bluebuddy.adapters.BlueMarketCoursesAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllCourse;
import com.bluebuddy.models.AllCourseDetails;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllCourseListFragment extends Fragment {
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
   /* private AllEventAdapter allEventAdapter;*/
    private Activity _activity;
    private ArrayList<AllCourseDetails> BMCList;
    private BlueMarketCoursesAdapter blueMarketCoursesAdapter;
/*    private ArrayList<AllCharterDetails> BCCList;
    private BlueCharterAdapter blueCharterAdapter;*/
   // private List<PeopleDetail> PEOPLEslist;
   private ProgressDialog mProgressDialog;
    private Context _context;
    private SharedPreferences pref;
    private String token, category, fd, td,curru;
    private String textcat = "";
    private String texttoken = "";
    private MyTextView txtshid1;

 //   Calendar myCalendar = Calendar.getInstance();
    Button frm_date, to_date, dateaction;
 //   EditText edtfrmdate, edttodate;

    /*public AllTripsSearchFragment(String tkn,String category) {
        this.token = tkn;
        this.category=category;

    }*/
//
public static AllCourseListFragment createInstance(int i) {
    AllCourseListFragment allTripsSearchFragment = new AllCourseListFragment();
    Bundle bundle = new Bundle();
    allTripsSearchFragment.setArguments(bundle);
    return allTripsSearchFragment;
}

    public static AllCourseListFragment createInstance(String category) {
        AllCourseListFragment allTripsSearchFragment = new AllCourseListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ABOUT", category);
        allTripsSearchFragment.setArguments(bundle);
        return allTripsSearchFragment;
    }

    public static AllCourseListFragment createInstance(String category, String token,String curru) {
        AllCourseListFragment allTripsSearchFragment = new AllCourseListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("CAT", category);
        bundle.putString("TOKEN", token);
        bundle.putString("CURRU", curru);
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
        //Log.d("ABOUT", "" + bundle.getString("CAT"));
       // Log.d("ABOUT2", "" + bundle.getString("TOKEN"));
        textcat = bundle.getString("CAT");
        texttoken = bundle.getString("TOKEN");
        curru = bundle.getString("CURRU");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragmentcharter, container, false);


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

        blueMarketCoursesAdapter = new BlueMarketCoursesAdapter(_activity, _context, null, texttoken, textcat,curru);
       // blueCharterAdapter = new BlueCharterAdapter(_activity, _context, null, token);
        recyclerView.setAdapter(blueMarketCoursesAdapter);
        blueMarketCoursesAdapter.notifyDataSetChanged();
        getAllCourse();
        txtshid1=(MyTextView)rootView.findViewById(R.id.txtshid1);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BlueMarketCoursePicActivity.class);
                //intent.putExtra("category", category);
                intent.putExtra("category", textcat);
                intent.putExtra("bmcourse","1");
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


    public void getAllCourse(){

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
//        Call<AllTrip> userCall = service.alltrip("Bearer "+texttoken,textcat);
        Call<AllCourse> userCall = service.allCourse("Bearer " + texttoken,textcat);

        userCall.enqueue(new Callback<AllCourse>() {
            @Override
            public void onResponse(Call<AllCourse> call, Response<AllCourse> response) {
if(response.body().getStatus()!=false){
                // if (mProgressDialog.isShowing())
                //    mProgressDialog.dismiss();
    txtshid1.setVisibility(View.GONE);
             /*   Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("onResponse:", "" + response.body().getDetails());
                Log.d("TOKEN:", "" + token);*/
                blueMarketCoursesAdapter.updateAllCourses(response.body().getCourse_list());
                // allEventAdapter.updateAllTrip(response.body().getDetails());
            }
else if(response.body().getStatus()!=true){
    txtshid1.setVisibility(View.VISIBLE);
    txtshid1.setText("No courses found!");
}
            }
            @Override
            public void onFailure(Call<AllCourse> call, Throwable t) {
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

