package com.bluebuddy.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bluebuddy.fragments.DetailsFragment;
import com.bluebuddy.fragments.ParticipantsFragment;

public class EventDetailsAdapter extends FragmentPagerAdapter {

    public EventDetailsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new DetailsFragment();
        }
        else if (position == 1)
        {
            fragment = new ParticipantsFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // return super.getPageTitle(position);
        String title = null;
        if (position == 0)
        {
            title = "DETAILS";
        }
        else if (position == 1)
        {
            title = "PARTICIPANTS";
        }

        return title;
    }
}

