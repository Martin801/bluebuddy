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
import com.bluebuddy.adapters.PeopleProfileAllEventAdapter;
import com.bluebuddy.models.PeopleEventItems;

import java.util.ArrayList;

/**
 * Created by admin on 5/3/2017.
 */

public class PeopleProfileEventFragment extends Fragment {

    public static PeopleProfileEventFragment createInstance(int itemsCount) {
        PeopleProfileEventFragment eventFragment = new PeopleProfileEventFragment();
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
        ArrayList<PeopleEventItems> EVENTList = new ArrayList<>();

        String[] Enames = {"Scuba Diving", "Scuba Diving", "Scuba Diving", "Scuba Diving"};

        String[] Eloc = {"London Docklands", "London Docklands", "London Docklands", "London Docklands"};

        String[] Edesc = {"Active music and movement session for toddlers and carers.", "Active music and movement session for toddlers and carers.", "Active music and movement session for toddlers and carers.", "Active music and movement session for toddlers and carers."};

        String[] Efrdate = {"21 March 2017", "21 March 2017", "21 March 2017", "21 March 2017"};



        for (int i = 0; i < Enames.length; i++) {
            PeopleEventItems peopleEventItems = new PeopleEventItems(Enames[i],Eloc[i],Efrdate[i],Edesc[i],i + 1);
            EVENTList.add(peopleEventItems);
        }
        PeopleProfileAllEventAdapter peopleProfileAllEventAdapter = new PeopleProfileAllEventAdapter(EVENTList);
        rv.setAdapter(peopleProfileAllEventAdapter);
        peopleProfileAllEventAdapter.notifyDataSetChanged();
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
