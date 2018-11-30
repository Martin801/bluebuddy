package com.bluebuddy.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
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
    /*String token;
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public ViewPagerAdapter(String tkn ,FragmentManager fm) {
        super(fm);
       this.token=tkn;

    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new PeopleSearchFragment(token);
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
            title = "PEOPLE";
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

