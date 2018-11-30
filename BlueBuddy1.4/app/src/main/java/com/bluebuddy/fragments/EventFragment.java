package com.bluebuddy.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.models.MyTripDetails;
import com.bluebuddy.models.TripDetail;

import java.util.ArrayList;


public class EventFragment extends Fragment {

    ArrayList<TripDetail> tripDetail = new ArrayList<TripDetail>();
    ArrayList<MyTripDetails> myTripDetails;
    boolean a = false;
    SharedPreferences pref;
    String token;
    private EmptyRecyclerView menu_recycler_view;
    private MyEventAdapter myEventAdapter;
    // private LinearLayout create_event;
    private Activity activity;
    private Context context;
    private boolean _editAble = true;
    private MyTextView txtshid1;

    public static EventFragment createInstance(int itemsCount) {
        EventFragment eventFragment = new EventFragment();
        Bundle bundle = new Bundle();
        eventFragment.setArguments(bundle);
        return eventFragment;
    }

    public static EventFragment createInstance(ArrayList<TripDetail> tripDetail) {
        EventFragment eventFragment = new EventFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("tripDetail", tripDetail);
        eventFragment.setArguments(bundle);
        return eventFragment;
    }

    public static EventFragment createInstance(ArrayList<MyTripDetails> myTripDetail, int i) {
        EventFragment eventFragment = new EventFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("myTripDetail", myTripDetail);
        eventFragment.setArguments(bundle);
        return eventFragment;
    }

    public static EventFragment createInstance(ArrayList<TripDetail> tripDetail, boolean _editAble) {
        EventFragment eventFragment = new EventFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("tripDetail", tripDetail);
        bundle.putBoolean("_editAble", _editAble);
        eventFragment.setArguments(bundle);
        return eventFragment;
    }

    public static EventFragment createInstance(ArrayList<TripDetail> tripDetail, boolean _editAble, boolean a) {
        EventFragment eventFragment = new EventFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("tripDetail", tripDetail);
        /*bundle.putBoolean("_editAble", _editAble);*/
        bundle.putBoolean("_editAble1", _editAble);
        bundle.putBoolean("user", a);

        eventFragment.setArguments(bundle);
        return eventFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        Button bt = (Button) rootView.findViewById(R.id.Buddyupid);
        FloatingActionButton fab1 = (FloatingActionButton) rootView.findViewById(R.id.fab1);
        ScrollView trip_recycler_empty_view = (ScrollView) rootView.findViewById(R.id.trip_recycler_empty_view);
        CardView card_view = (CardView) rootView.findViewById(R.id.card_view);
        txtshid1 = (MyTextView) rootView.findViewById(R.id.txtshid1);
        // LinearLayout create_event=(LinearLayout)rootView.findViewById(R.id.create_event);

        Bundle bundle = getArguments();

        if (bundle != null) {

            if (bundle.containsKey("tripDetail")) {
                tripDetail = (ArrayList<TripDetail>) bundle.getSerializable("tripDetail");
                if (tripDetail.isEmpty()) {
                    txtshid1.setVisibility(View.VISIBLE);
                    txtshid1.setText("No trips found!");
                }
                if (bundle.containsKey("_editAble")) {
                    myEventAdapter = new MyEventAdapter(activity, context, tripDetail, false);
                } else {
                    myEventAdapter = new MyEventAdapter(activity, context, tripDetail);
                }
            } else if (bundle.containsKey("_editAble1")) {
                myEventAdapter = new MyEventAdapter(activity, context, tripDetail, true);
                //  myEventAdapter = new MyEventAdapter(activity, context, tripDetail,token);
            }/*else{
                myEventAdapter = new MyEventAdapter(activity, context, null);
            }*/

            myEventAdapter.notifyDataSetChanged();

        } else {
            myEventAdapter = new MyEventAdapter(activity, context, null);
            Log.d("tripDetail", "Trip detail list is empty");
        }

        myEventAdapter.notifyDataSetChanged();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventFragment.super.getContext(), CreateEventActivity3.class);
                startActivity(intent);
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(EventFragment.super.getContext(), CreateEventActivity3.class);
                //  Intent intent = new Intent(EventFragment.super.getContext(), CreateEventActivity3.class);
                startActivity(intent);
            }
        });
        // 1. get a reference to recyclerView
        menu_recycler_view = (EmptyRecyclerView) rootView.findViewById(R.id.trip_recycler_view);

        /*// get a reference to Empty view Fetch the empty view from the layout and set it on the new recycler view
        View emptyView = rootView.findViewById(R.id.trip_recycler_empty_view);
        menu_recycler_view.setEmptyView(emptyView);*/
// get a reference to Empty view Fetch the empty view from the layout and set it on the new recycler view
        if (bundle.containsKey("user")) {

            View emptyView = rootView.findViewById(R.id.trip_recycler_empty_view);
            emptyView.setVisibility(View.GONE);

            menu_recycler_view.setEmptyView(emptyView);
        } else if (!bundle.containsKey("user")) {
            fab1.setVisibility(View.GONE);
            View emptyView = rootView.findViewById(R.id.trip_recycler_empty_view);
            emptyView.setVisibility(View.GONE);
            menu_recycler_view.setVisibility(View.VISIBLE);
        }

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
