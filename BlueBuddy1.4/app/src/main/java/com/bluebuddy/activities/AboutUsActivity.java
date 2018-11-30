package com.bluebuddy.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bluebuddy.R;

public class AboutUsActivity extends BuddyActivity {

    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_aboutus);
        super.onCreate(savedInstanceState);

        initializeElements();
    }

    private void initializeElements(){
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void back(){
        super.onBackPressed();
    }
}
