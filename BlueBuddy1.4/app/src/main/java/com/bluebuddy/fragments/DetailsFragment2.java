package com.bluebuddy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluebuddy.R;
import com.bluebuddy.models.TripDetail;

/**
 * Created by USER on 5/7/2017.
 */

public class DetailsFragment2 extends Fragment {

    private TextView detailfragid;
    private TripDetail tripDetail;

    public static DetailsFragment2 createInstance(int i) {
        DetailsFragment2 detailsFragment = new DetailsFragment2();
        Bundle bundle = new Bundle();
        detailsFragment.setArguments(bundle);

        return detailsFragment;
    }

    public static DetailsFragment2 createInstance(String about) {
        DetailsFragment2 detailsFragment2 = new DetailsFragment2();
        Bundle bundle = new Bundle();
        bundle.putString("DETAILS", about);
        detailsFragment2.setArguments(bundle);
        return detailsFragment2;
    }

    public static DetailsFragment2 createInstance(TripDetail details) {
        DetailsFragment2 detailsFragment2 = new DetailsFragment2();
        Bundle bundle = new Bundle();
        bundle.putSerializable("DETAILS", details);
        detailsFragment2.setArguments(bundle);
        return detailsFragment2;
    }

    public static DetailsFragment2 newInstance(){
        DetailsFragment2 detailsFragment2 = new DetailsFragment2();
        return detailsFragment2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details2, container, false);
        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey("DETAILS")) {
            Log.d("DetailsFragment2: ", "" + bundle.getSerializable("DETAILS").toString());

            tripDetail = (TripDetail) bundle.getSerializable("DETAILS");

            /*strtext = bundle.getString("DETAILS");*/
            detailfragid = (TextView) rootView.findViewById(R.id.detailfragid);
            /*detailfragid.setText(strtext);*/
            String evtdesc = tripDetail.getDescription();
            detailfragid.setText(evtdesc);
        }
        return rootView;
    }
}
