package com.bluebuddy.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import com.bluebuddy.R;
import com.bluebuddy.Utilities.EmptyRecyclerView;
import com.bluebuddy.activities.CreateEventActivity3;
import com.bluebuddy.adapters.MyEventAdapter;
import com.bluebuddy.models.MyTripDetails;
import com.bluebuddy.models.TripDetail;

import java.util.ArrayList;

/**
 * Created by admin on 5/3/2017.
 */

public class MyProEventFragment extends Fragment {

    private EmptyRecyclerView menu_recycler_view;
    private MyEventAdapter myEventAdapter;
    ArrayList<TripDetail> tripDetail = new ArrayList<TripDetail>();
    ArrayList<MyTripDetails> myTripDetails;

    // private LinearLayout create_event;
    private Activity activity;
    private Context context;
    private boolean _editAble = true;

    public static MyProEventFragment createInstance(int itemsCount) {
        MyProEventFragment myProEventFragment = new MyProEventFragment();
        Bundle bundle = new Bundle();
        myProEventFragment.setArguments(bundle);
        return myProEventFragment;
    }

    public static MyProEventFragment createInstance(ArrayList<TripDetail> tripDetail) {
        MyProEventFragment myProEventFragment = new MyProEventFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("tripDetail", tripDetail);
        myProEventFragment.setArguments(bundle);
        return myProEventFragment;
    }

    public static MyProEventFragment createInstance(ArrayList<MyTripDetails> myTripDetail, int i) {
        MyProEventFragment myProEventFragment = new MyProEventFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("myTripDetail", myTripDetail);
        myProEventFragment.setArguments(bundle);
        return myProEventFragment;
    }

    public static MyProEventFragment createInstance(ArrayList<TripDetail> tripDetail, boolean _editAble) {
        MyProEventFragment myProEventFragment = new MyProEventFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("tripDetail", tripDetail);
        bundle.putBoolean("_editAble", _editAble);
        myProEventFragment.setArguments(bundle);
        return myProEventFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.my_pro_fragment_events, container, false);
        Button bt = (Button) rootView.findViewById(R.id.Buddyupid);
        FloatingActionButton fab1 = (FloatingActionButton) rootView.findViewById(R.id.fab1);
        ScrollView trip_recycler_empty_view = (ScrollView) rootView.findViewById(R.id.trip_recycler_empty_view);
        CardView card_view = (CardView) rootView.findViewById(R.id.card_view);
        // LinearLayout create_event=(LinearLayout)rootView.findViewById(R.id.create_event);

        Bundle bundle = getArguments();

        if (bundle != null) {

            if (bundle.containsKey("tripDetail")) {
                tripDetail = (ArrayList<TripDetail>) bundle.getSerializable("tripDetail");

                if (bundle.containsKey("_editAble")) {
                    myEventAdapter = new MyEventAdapter(activity, context, tripDetail, false);
                } else {
                    // myEventAdapter = new MyEventAdapter(activity, context, tripDetail,"");
                    myEventAdapter = new MyEventAdapter(activity, context, tripDetail);
                }
            } else {
                // myEventAdapter = new MyEventAdapter(activity, context, null,"");
                myEventAdapter = new MyEventAdapter(activity, context, null);
            }

            myEventAdapter.notifyDataSetChanged();

        } else {
            myEventAdapter = new MyEventAdapter(activity, context, null);
            Log.d("tripDetail", "Trip detail list is empty");
        }

        myEventAdapter.notifyDataSetChanged();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProEventFragment.super.getContext(), CreateEventActivity3.class);
                intent.putExtra("pass", 2);
                startActivity(intent);
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(MyProEventFragment.super.getContext(), CreateEventActivity3.class);
                intent.putExtra("pass", 2);
                //  Intent intent = new Intent(EventFragment.super.getContext(), CreateEventActivity3.class);
                startActivity(intent);
            }
        });
        // 1. get a reference to recyclerView
        menu_recycler_view = (EmptyRecyclerView) rootView.findViewById(R.id.trip_recycler_view);

        // get a reference to Empty view Fetch the empty view from the layout and set it on the new recycler view
        View emptyView = rootView.findViewById(R.id.trip_recycler_empty_view);
        menu_recycler_view.setEmptyView(emptyView);

        // 2. set layoutManger
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        menu_recycler_view.setLayoutManager(mLayoutManager);

        // 3. set item animator to DefaultAnimator
        menu_recycler_view.setItemAnimator(new DefaultItemAnimator());

        // 4. set adapter
        menu_recycler_view.setAdapter(myEventAdapter);

        return rootView;


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
        this.activity = getActivity();
    }

}
