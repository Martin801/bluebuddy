package com.bluebuddy.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class ViewPagerAdapterforTrips extends FragmentPagerAdapter {
    public ViewPagerAdapterforTrips(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
/*
    String token,category;
    public ViewPagerAdapterforTrips(FragmentManager fm) {
        super(fm);
    }
    public ViewPagerAdapterforTrips(String tkn,String category,FragmentManager fm) {
        super(fm);
       this.token=tkn;
        this.category=category;
    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new AllTripsSearchFragment(token,category);
        }
        else if (position == 1)
        {
            fragment = new LocationSearchFragment(token);
        }
       *//* else if (position == 2)
        {
            fragment = new PeopleSearchFragC();
        }*//*
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
            title = "TRIPS";
        }
        else if (position == 1)
        {
            title = "LOCATION";
        }
        *//*else if (position == 2)
        {
            title = "EVENTS";
        }*//*
        return title;
    }*/
}

