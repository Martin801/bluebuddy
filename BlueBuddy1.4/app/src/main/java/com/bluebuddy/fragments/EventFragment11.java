package com.bluebuddy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluebuddy.R;

/**
 * Created by admin on 5/3/2017.
 */

public class EventFragment11 extends Fragment  {

    public static EventFragment11 createInstance(int itemsCount) {
        EventFragment11 eventFragment = new EventFragment11();
        Bundle bundle = new Bundle();
        eventFragment.setArguments(bundle);

        return eventFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.events_card, container, false);
        return rootView;
    }

}
