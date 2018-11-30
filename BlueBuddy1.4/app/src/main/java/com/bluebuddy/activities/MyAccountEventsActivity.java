package com.bluebuddy.activities;

import android.os.Bundle;

import com.bluebuddy.R;

public class MyAccountEventsActivity extends BuddyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_my_account_events);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.setTitle("TRIPS");
    }
}
