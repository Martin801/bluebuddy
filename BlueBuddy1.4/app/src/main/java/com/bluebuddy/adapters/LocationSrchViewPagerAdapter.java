package com.bluebuddy.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bluebuddy.fragments.PeopleSearchFragment2;



public class LocationSrchViewPagerAdapter extends FragmentPagerAdapter {
    String token;
    Double clat,clong;
    String rad;
    public LocationSrchViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public LocationSrchViewPagerAdapter(FragmentManager fm,String tkn,Double clt ,Double clg) {
        super(fm);
        this.token=tkn;
        this.clat=clt;
        this.clong=clg;
     //   this.rad =rd;


    }
    public LocationSrchViewPagerAdapter(FragmentManager fm,String tkn,Double clt ,Double clg,String rd) {
        super(fm);
        this.token=tkn;
        this.clat=clt;
        this.clong=clg;
        this.rad =rd;


    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new PeopleSearchFragment2(token,clat,clong,rad);
        }
        /*else if (position == 1)
        {
            fragment = new LocationSearchFragment();
        }*/
       /* else if (position == 2)
        {
            fragment = new PeopleSearchFragC();
        }*/
        return fragment;
    }



    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // return super.getPageTitle(position);
        String title = null;
        if (position == 0)
        {
            title = "PEOPLE";
        }
        /*else if (position == 1)
        {
            title = "LOCATION";
        }*/
        /*else if (position == 2)
        {
            title = "EVENTS";
        }*/
        return title;
    }
}

