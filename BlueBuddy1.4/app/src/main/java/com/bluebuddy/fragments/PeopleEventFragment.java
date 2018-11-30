package com.bluebuddy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluebuddy.R;
import com.bluebuddy.activities.RecyclerItemClickListener;
import com.bluebuddy.adapters.PeopleAllEventAdapter;
import com.bluebuddy.models.AllEventItems;

import java.util.ArrayList;

/**
 * Created by admin on 5/3/2017.
 */

public class PeopleEventFragment extends Fragment {

    public static PeopleEventFragment createInstance(int itemsCount) {
        PeopleEventFragment eventFragment = new PeopleEventFragment();
        Bundle bundle = new Bundle();
        eventFragment.setArguments(bundle);
        return eventFragment;}
    
    //@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_people_myevents, container, false);
        /*
        Button bt = (Button) rootView.findViewById(R.id.Buddyupid);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PeopleEventFragment.super.getContext(), CreateEventActivity.class);
                startActivity(intent);
            }
        });
        */
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.recycler_view_people_event);
        
       // rv.setHasFixedSize(true);
       // PeopleAllEventAdapter adapter = new PeopleAllEventAdapter(new String[]{"test one", "test two", "test three", "test four", "test five" , "test six" , "test seven"});
      //  rv.setAdapter(adapter);

       // LinearLayoutManager llm = new LinearLayoutManager(getActivity());
     //   rv.setLayoutManager(llm);
        //
        if (rv != null) {
            rv.setHasFixedSize(true);
        }
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(mLayoutManager);
        ArrayList<AllEventItems> EVENTList = new ArrayList<>();
        String[] Unames = {"steve", "jhon", "rahul", "karan"};

        String[] Enames = {"Volley", "cricket", "hockey", "basketball"};

        String[] Eloc = {"Delhi", "kolkata", "mumbai", "jharkhand"};

        String[] Edesc = {"Volley match", "cricket match", "hockey match", "basketball match"};

        String[] Efrdate = {"01 05 2017", "01 05 2017", "01 05 2017", "01 05 2017"};

        String[] Etodate = {"02 05 2017", "02 05 2017", "02 05 2017", "02 05 2017"};

        int[] Upics = {
                R.drawable.blue_market,
                R.drawable.chat,
                R.drawable.date,
                R.drawable.checked};
        for (int i = 0; i < Unames.length; i++) {
            AllEventItems allEventItems = new AllEventItems(Unames[i], Enames[i],Eloc[i],Efrdate[i],Etodate[i],Edesc[i],i + 1, Upics[i]);
            EVENTList.add(allEventItems);
        }
        PeopleAllEventAdapter allEventAdapter = new PeopleAllEventAdapter(EVENTList);
        rv.setAdapter(allEventAdapter);
        allEventAdapter.notifyDataSetChanged();
        rv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), "Card at " + position + " is clicked", Toast.LENGTH_SHORT).show();
            }
        }));
        /*
        rv.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), "Card at " + position + " is clicked", Toast.LENGTH_SHORT).show();
            }
        }));
*/
        //today
        
        
        //
        return rootView;
    }

}
