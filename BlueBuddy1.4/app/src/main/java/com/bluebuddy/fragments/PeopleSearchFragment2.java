package com.bluebuddy.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluebuddy.R;
import com.bluebuddy.adapters.PeopleSearchAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AfterLoginStatus;
import com.bluebuddy.models.PeopleDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by USER on 5/7/2017.
 */

public class PeopleSearchFragment2 extends Fragment {
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PeopleSearchAdapter peopleSearchAdapter;
    //ListView list;
    private Activity _activity;
    private List<PeopleDetail> PEOPLEslist;
   // private ArrayList<PeopleSearch> PEOPLEsList;

    private Context _context;
    private SharedPreferences pref;
    private String token, category,fd,td;
    Double clat ,clong;
    String rad;


  public PeopleSearchFragment2(){

  }


  public PeopleSearchFragment2(String tkn,Double clt,Double clg) {
      this.token=tkn;
      this.clat=clt;
      this.clong=clg;
  //    this.rad=rd;

  }
   public PeopleSearchFragment2(String tkn,Double clt,Double clg,String rd) {
        this.token=tkn;
        this.clat=clt;
        this.clong=clg;
        this.rad=rd;

    }

    public static PeopleSearchFragment2 createInstance(String token,Double clat,Double clong,String rad) {
        PeopleSearchFragment2 peopleSearchFragment2 = new PeopleSearchFragment2();
       // Bundle bundle = new Bundle();
       // bundle.putString("radius", "abcd");
       // peopleSearchFragment2.setArguments(bundle);
        return peopleSearchFragment2;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      //  pref = super.getPref();
    //    token = pref.getString(ACCESS_TOKEN, "");
        getAllPeople();
        View rootView = inflater.inflate(
                //  R.layout.fragment, container, false);
                R.layout.fragment, container, false);

        return rootView;

    }

    public void getData(String rad){
      //  txt.setText(message);

        Log.d("hello txt",rad);
    }

    private void getAllPeople() {
        Log.d("tokeninloc", token);
        Log.d("latinloc", String.valueOf(clat));
        Log.d("latinlong", String.valueOf(clong));
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
       // if(!rad.equals("")){
            Call<AfterLoginStatus> userCall = service.allpeople1("Bearer " + token,clat,clong,rad,"");
            userCall.enqueue(new Callback<AfterLoginStatus>() {
                @Override
                public void onResponse(Call<AfterLoginStatus> call, Response<AfterLoginStatus> response) {
                    //  if (mProgressDialog.isShowing())
                    //    mProgressDialog.dismiss();
                    Log.d("STATUS", String.valueOf(response.body().getStatus()));
                    Log.d("TOKEN:", "" + token);
                    peopleSearchAdapter.updateAllPeople(response.body().getUser_details_arr());
                }
                @Override
                public void onFailure(Call<AfterLoginStatus> call, Throwable t) {
                }
            });
     //   }

     /*   else{
            Call<AfterLoginStatus> userCall = service.allpeople1("Bearer " + token,clat,clong,"","");
            userCall.enqueue(new Callback<AfterLoginStatus>() {
                @Override
                public void onResponse(Call<AfterLoginStatus> call, Response<AfterLoginStatus> response) {
                    //  if (mProgressDialog.isShowing())
                    //    mProgressDialog.dismiss();

                    Log.d("STATUS", String.valueOf(response.body().getStatus()));
                    Log.d("TOKEN:", "" + token);
                    peopleSearchAdapter.updateAllPeople(response.body().getUser_details_arr());
                }
                @Override
                public void onFailure(Call<AfterLoginStatus> call, Throwable t) {
                }
            });
        }*/

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }
        mLayoutManager = new LinearLayoutManager(_context);
        recyclerView.setLayoutManager(mLayoutManager);
        peopleSearchAdapter = new PeopleSearchAdapter(_activity, _context, null,token);
        recyclerView.setAdapter(peopleSearchAdapter);
        peopleSearchAdapter.notifyDataSetChanged();
       // PEOPLEsList = new ArrayList<>();
      /*  for (int i = 0; i < Pplname.length; i++) {
            PeopleSearch peopleSearch = new PeopleSearch(Pplname[i], Pplloc[i], Pplint[i], Pplpic[i], Pplcrt1[i], Pplcrt2[i]);
            PEOPLEsList.add(peopleSearch);
        }*/


      ///  peopleSearchAdapter = new PeopleSearchAdapter(_activity, _context, PEOPLEsList,);

       /* recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), PeopleProfileActivity.class);
                startActivity(intent);
            }
        });*/


        //  String[] items = getResources().getStringArray(R.array.tab_A);
//       /* RecyclerViewAdapter1 adapter = new RecyclerViewAdapter1(items);
//         recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);*/

    }


    public void abc(){
        System.out.print("find text ");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if(bundle != null){
            if(bundle.containsKey("radius")){
                Log.d("got string", "" + bundle.getString("radius"));
            }
        }



      //  strtext = bundle.getString("ABOUT");
    }
    /*public void oDialog(final String msg, final String btnText, final String btnText2, final String btnText3, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog6(msg, btnText, btnText2, btnText3, _redirect, activity, _redirectClass, layout);
    }*/
}

