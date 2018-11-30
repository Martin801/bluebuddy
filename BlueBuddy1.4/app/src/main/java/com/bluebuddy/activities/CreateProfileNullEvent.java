package com.bluebuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bluebuddy.R;


public class CreateProfileNullEvent extends BuddyActivity {
    Button bup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.create_profile_null_event);
        super.onCreate(savedInstanceState);
        bup = (Button) findViewById(R.id.Buddyupid);
        bup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateProfileNullEvent.this, AllEventsActivityNew.class);
                startActivity(i);
                finish();
            }
        });

    }
}
