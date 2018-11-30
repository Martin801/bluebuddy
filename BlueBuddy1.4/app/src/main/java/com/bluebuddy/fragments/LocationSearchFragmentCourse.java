package com.bluebuddy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bluebuddy.R;
import com.bluebuddy.activities.LocationBasedSearchActivity2Course;
import com.bluebuddy.activities.LocationBasedSearchActivityCourse;

/**
 * Created by USER on 5/7/2017.
 */

public class LocationSearchFragmentCourse extends Fragment {
    String token,textcat1,texttoken1;


    public static LocationSearchFragmentCourse createInstance(int i) {
        LocationSearchFragmentCourse locationSearchFragment = new LocationSearchFragmentCourse();
        Bundle bundle = new Bundle();
        locationSearchFragment.setArguments(bundle);
        return locationSearchFragment;
    }

    public static LocationSearchFragmentCourse createInstance(String category) {
        LocationSearchFragmentCourse locationSearchFragment = new LocationSearchFragmentCourse();
        Bundle bundle = new Bundle();
        bundle.putString("CAT", category);
        locationSearchFragment.setArguments(bundle);
        return locationSearchFragment;
    }

    public static LocationSearchFragmentCourse createInstance(String category, String token) {
        LocationSearchFragmentCourse locationSearchFragment = new LocationSearchFragmentCourse();
        Bundle bundle = new Bundle();
        bundle.putString("CAT", category);
        bundle.putString("TOKEN", token);
        locationSearchFragment.setArguments(bundle);
        return locationSearchFragment;
    }

//

    /*public LocationSearchFragment(String tkn) {
        this.token=tkn;
    }

    public LocationSearchFragment() {

    }*/

    /*public static LocationSearchFragment createInstance(int itemsCount) {
        LocationSearchFragment detailsFragment = new LocationSearchFragment();
        Bundle bundle = new Bundle();
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location_search, container, false);
        Bundle bundle = getArguments();
        textcat1 = bundle.getString("CAT");
        Log.d("hello",textcat1);
       // texttoken = bundle.getString("TOKEN");
        Button btcl = (Button) rootView.findViewById(R.id.curr_loc);
        Button btol = (Button) rootView.findViewById(R.id.other_loc);
        btcl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LocationSearchFragmentCourse.super.getContext(), LocationBasedSearchActivityCourse.class);
                i.putExtra("cat1",textcat1);
                startActivity(i);
            }
        });
        btol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(LocationSearchFragmentCourse.super.getContext(), LocationBasedSearchActivity2Course.class);
                j.putExtra("cat11",textcat1);
                startActivity(j);
            }
        });
        return rootView;
    }

}
