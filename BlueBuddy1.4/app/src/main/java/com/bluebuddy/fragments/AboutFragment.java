package com.bluebuddy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluebuddy.R;

/**
 * Created by admin on 5/3/2017.
 */

public class AboutFragment extends Fragment {

    private TextView abtid;
    private String strtext = "";

    public static AboutFragment createInstance(int i) {
        AboutFragment aboutFragment = new AboutFragment();
        Bundle bundle = new Bundle();
        aboutFragment.setArguments(bundle);
        return aboutFragment;
    }

    public static AboutFragment createInstance(String about) {
        AboutFragment aboutFragment = new AboutFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ABOUT", about);
        aboutFragment.setArguments(bundle);
        return aboutFragment;
    }

    public static AboutFragment newInstance(){
        AboutFragment aboutFragment = new AboutFragment();
        return aboutFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        Log.d("ABOUT", "" + bundle.getString("ABOUT"));
        strtext = bundle.getString("ABOUT");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // Bundle bundle = this.getArguments();

        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        abtid= (TextView) rootView.findViewById(R.id.abtid);
        abtid.setText(strtext);
      //  Log.d("abt",strtext);
        return rootView;
    }

}
