package com.bluebuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.bluebuddy.R;
import com.bluebuddy.adapters.ImageSliderAdapter;

import java.util.ArrayList;

public class ImageSliderActivity extends AppCompatActivity {

    private ViewPager vp_imageSlider;
    private Intent intent;
    private ArrayList<String> images;
    private ImageSliderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slider_view);

        intent = getIntent();
        images = intent.getStringArrayListExtra("images");
        vp_imageSlider = findViewById(R.id.vp_imageSlider);

        adapter = new ImageSliderAdapter(this, images);
        vp_imageSlider.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
