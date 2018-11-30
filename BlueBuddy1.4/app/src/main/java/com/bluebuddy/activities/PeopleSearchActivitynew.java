package com.bluebuddy.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bluebuddy.R;
import com.bluebuddy.adapters.PeopleSearchAdapter;
import com.bluebuddy.adapters.ViewPagerAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AfterLoginStatus;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.PeopleDetail;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.USER_ID;

public class PeopleSearchActivitynew extends BuddyActivity {

    ViewPagerAdapter viewPagerAdapter;
    ImageView back;
    Activity _activity;
    Context _context;
    EditText srch;
    //    SearchView srch;
    Button srchbtn;
    MyTextView bell_counter, txtshid1;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PeopleSearchAdapter peopleSearchAdapter;
    private String token, id, bellcounter;
    private SharedPreferences pref;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.setLayout(R.layout.activity_people_search);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.notice();
        super.setImageResource1(2);
        super.setTitle("SEARCH");
        _activity = this;
        _context = this;

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        userId = pref.getString(USER_ID, "");

        txtshid1 = (MyTextView) findViewById(R.id.txtshid1);
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_pplsrch);

        if (mRecyclerView != null) {

            mRecyclerView.setHasFixedSize(true);

        }

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        getAllPeople();

        srch = (EditText) findViewById(R.id.srch);
//        srch = (SearchView) findViewById(R.id.srch);
//        search(srch);

        srch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                if (!s.toString().isEmpty()) {
                    searchUsers(s.toString());
                } else {
                    searchUsers("");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        srch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    String personname = srch.getText().toString().trim();
                    searchUsers(personname);
                    return true;
                }
                return false;

            }
        });

        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(PeopleSearchActivitynew.this, MyProfileActivity.class);
                startActivity(i);

            }
        });

        bellcount();
    }

    private void searchUsers(String searchString) {
        String personname = searchString;
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AfterLoginStatus> userCall = service.allpeople("Bearer " + token, "", "", "", personname);
        userCall.enqueue(new Callback<AfterLoginStatus>() {
            @Override
            public void onResponse(Call<AfterLoginStatus> call, Response<AfterLoginStatus> response) {

                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("TOKEN:", "" + token);

                ArrayList<PeopleDetail> PEOPLEslist = response.body().getUser_details_arr();
                ArrayList<PeopleDetail> SearchList = new ArrayList<>();

                if (PEOPLEslist.size() <= 0) {
                    txtshid1.setVisibility(View.VISIBLE);
                    txtshid1.setText("No buddies match your search.");
                    peopleSearchAdapter = new PeopleSearchAdapter(_activity, _context, SearchList, token);
                    mRecyclerView.setAdapter(peopleSearchAdapter);
                    peopleSearchAdapter.notifyDataSetChanged();
                } else if (SearchList.size() > 0) {

                    for (PeopleDetail d : PEOPLEslist) {
                        if (d.getId() != null && !d.getId().equals(userId))
                            SearchList.add(d);
                    }

                    txtshid1.setVisibility(View.GONE);
                    peopleSearchAdapter = new PeopleSearchAdapter(_activity, _context, SearchList, token);
                    mRecyclerView.setAdapter(peopleSearchAdapter);
                    peopleSearchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<AfterLoginStatus> call, Throwable t) {
            }
        });
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Toast.makeText(PeopleSearchActivitynew.this, " " + newText, Toast.LENGTH_SHORT).show();

                if (peopleSearchAdapter != null) peopleSearchAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private void getAllPeople() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

        Call<AfterLoginStatus> userCall = service.allpeople("Bearer " + token, "", "", "", "");

        userCall.enqueue(new Callback<AfterLoginStatus>() {
            @Override
            public void onResponse(Call<AfterLoginStatus> call, Response<AfterLoginStatus> response) {

                ArrayList<PeopleDetail> PEOPLEslist = response.body().getUser_details_arr();
                ArrayList<PeopleDetail> SearchList = new ArrayList<>();
                for (PeopleDetail d : PEOPLEslist) {
                    if (!d.getId().equalsIgnoreCase(userId))
                        SearchList.add(d);
                }

                peopleSearchAdapter = new PeopleSearchAdapter(_activity, _context, SearchList, token);
                mRecyclerView.setAdapter(peopleSearchAdapter);
//                peopleSearchAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<AfterLoginStatus> call, Throwable t) {
            }
        });

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
                if (bellcounter != null) {
                    if (bellcounter.equals("0")) {
                        bell_counter.setVisibility(View.GONE);
                    } else if (!bellcounter.equals("0")) {
                        bell_counter.setVisibility(View.VISIBLE);
                    }
                    bell_counter.setText(response.body().getCounter());
                }
            }

            @Override
            public void onFailure(Call<AllNotice> call, Throwable t) {

            }
        });
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final String btnText3, final String btnText4, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog4(msg, btnText, btnText2, btnText3, btnText4, _redirect, activity, _redirectClass, layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(PeopleSearchActivitynew.this, MyProfileActivity.class);
        startActivity(i);
    }

}

