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
import com.bluebuddy.activities.LocationBasedSearchActivity2Product;
import com.bluebuddy.activities.LocationBasedSearchActivityProduct;

/**
 * Created by USER on 5/7/2017.
 */

public class LocationSearchFragmentProduct extends Fragment {
    String token,textcat1;



    public static LocationSearchFragmentProduct createInstance(int i) {
        LocationSearchFragmentProduct locationSearchFragment = new LocationSearchFragmentProduct();
        Bundle bundle = new Bundle();
        locationSearchFragment.setArguments(bundle);
        return locationSearchFragment;
    }

    public static LocationSearchFragmentProduct createInstance(String category) {
        LocationSearchFragmentProduct locationSearchFragment = new LocationSearchFragmentProduct();
        Bundle bundle = new Bundle();
        bundle.putString("CAT", category);
        locationSearchFragment.setArguments(bundle);
        return locationSearchFragment;
    }

    public static LocationSearchFragmentProduct createInstance(String category, String token) {
        LocationSearchFragmentProduct locationSearchFragment = new LocationSearchFragmentProduct();
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
        Button btcl = (Button) rootView.findViewById(R.id.curr_loc);
        Button btol = (Button) rootView.findViewById(R.id.other_loc);
        btcl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LocationSearchFragmentProduct.super.getContext(), LocationBasedSearchActivityProduct.class);
                i.putExtra("cat1",textcat1);
                startActivity(i);
            }
        });
        btol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(LocationSearchFragmentProduct.super.getContext(), LocationBasedSearchActivity2Product.class);
                j.putExtra("cat1",textcat1);
                startActivity(j);
            }
        });
        return rootView;
    }

}
