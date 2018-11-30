package com.bluebuddy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bluebuddy.R;
import com.bluebuddy.activities.LocationBasedSearchActivity;
import com.bluebuddy.activities.LocationBasedSearchActivity2;

/**
 * Created by USER on 5/7/2017.
 */

public class LocationSearchFragment extends Fragment {
    String token;



    public static LocationSearchFragment createInstance(int i) {
        LocationSearchFragment locationSearchFragment = new LocationSearchFragment();
        Bundle bundle = new Bundle();
        locationSearchFragment.setArguments(bundle);
        return locationSearchFragment;
    }

    public static LocationSearchFragment createInstance(String category) {
        LocationSearchFragment locationSearchFragment = new LocationSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("CAT", category);
        locationSearchFragment.setArguments(bundle);
        return locationSearchFragment;
    }

    public static LocationSearchFragment createInstance(String category, String token) {
        LocationSearchFragment locationSearchFragment = new LocationSearchFragment();
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
        Button btcl = (Button) rootView.findViewById(R.id.curr_loc);
        Button btol = (Button) rootView.findViewById(R.id.other_loc);
        btcl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LocationSearchFragment.super.getContext(), LocationBasedSearchActivity.class);
                startActivity(i);
            }
        });
        btol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(LocationSearchFragment.super.getContext(), LocationBasedSearchActivity2.class);
                startActivity(j);
            }
        });
        return rootView;
    }

}
