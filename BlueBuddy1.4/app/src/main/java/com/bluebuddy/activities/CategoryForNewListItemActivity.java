package com.bluebuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bluebuddy.R;

public class CategoryForNewListItemActivity extends BuddyActivity {
    private Button bmcharterid, bmcourseid, bmproductid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_category_for_new_list_item);
        super.onCreate(savedInstanceState);
        super.initialize();
        findViews();
        bmcharterid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryForNewListItemActivity.this, BlueMarketCharterPicActivity.class);
                i.putExtra("validation", "true");
                startActivity(i);
            }
        });
        bmcourseid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryForNewListItemActivity.this, BlueMarketCoursesActivity.class);
                i.putExtra("validation", true);
                startActivity(i);
            }
        });
        bmproductid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryForNewListItemActivity.this, BlueMarketPicActivity.class);
                i.putExtra("validation", "true");
                startActivity(i);
            }
        });
    }

    private void findViews() {
        bmcharterid = (Button) findViewById(R.id.bmcharterid);
        bmcourseid = (Button) findViewById(R.id.bmcourseid);
        bmproductid = (Button) findViewById(R.id.bmproductid);
    }
}
