package com.bluebuddy.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.bluebuddy.R;
import com.bluebuddy.adapters.CustomAdapter;
import com.bluebuddy.adapters.InterestAdapter;
import com.bluebuddy.adapters.PlaceAutocompleteAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.CommonResponse;
import com.bluebuddy.models.ReviewDetail;
import com.bluebuddy.models.TripDetail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;


public class EditReviewActivity extends BuddyActivity {
    private Button rate_and_rvw;

    private EditText descrip, rating_title;
    private TextView navtxt;
    private ImageView back,imgNotification;
    private Activity _myActivity;
    private Context _myContext;
    private SharedPreferences pref;
    private String token, rating_value, rvw_title, description, bm_category, for_id,id,cat;
    private RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_edit_review);
        super.onCreate(savedInstanceState);

       /* super.setTitle("EDIT YOUR REVIEW");*/
        _myActivity = this;
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setVisibility(View.GONE);
        navtxt=(TextView)findViewById(R.id.navtxt);
        imgNotification=(ImageView)findViewById(R.id.imgNotification);
        imgNotification.setVisibility(View.GONE);
        navtxt.setText("EDIT YOUR REVIEW");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditReviewActivity.this, CourseDetailsActivitynewlayall.class);
                startActivity(i);
            }
        });
        Bundle bundle = getIntent().getExtras();
        rvw_title = bundle.getString("rvw_title");
        description = bundle.getString("description");
        rating_value = bundle.getString("rating_value");
        for_id = bundle.getString("for_id");
        bm_category = bundle.getString("bm_category");
        cat=bundle.getString("category");
        id = bundle.getString("id");
        float rating = Float.parseFloat(rating_value);
        rate_and_rvw = (Button) findViewById(R.id.rate_and_rvw);
        rating_title = (EditText) findViewById(R.id.rating_title);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        descrip = (EditText) findViewById(R.id.desc);

        ratingBar.setRating(rating);
        descrip.setText(description);
        rating_title.setText(rvw_title);

        rate_and_rvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rvw_title = rating_title.getText().toString().trim();

                String description = descrip.getText().toString().trim();
                if (rvw_title.length() == 0) {
                    oDialog("Enter a review title!", "Close", "", false, _myActivity, "", 0);
                    rating_title.requestFocus();
                } else if (description.length() == 0) {
                    oDialog("Enter your review description!", "Close", "", false, _myActivity, "", 0);
                    descrip.requestFocus();
                }

                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                Call<ReviewDetail> userCall = service.review_update("Bearer " + token, rvw_title, description, rating_value, bm_category, for_id,id);
                userCall.enqueue(new Callback<ReviewDetail>() {
                    @Override
                    public void onResponse(Call<ReviewDetail> call, Response<ReviewDetail> response) {

                        Log.d("RESPONSE_ ", String.valueOf(response.body().getStatus()));

                        if (response.code() == 200) {
if(bm_category.equals("course")) {
    oDialog(response.body().getMessage(), "Close", "", true, _myActivity, "BlueMarketCourseActivityNew", 0, cat);
                            /*(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, data);*/
}
else if(bm_category.equals("product")){
    oDialog(response.body().getMessage(), "Close", "", true, _myActivity, "BlueMarketProductActivityNew", 0, cat);
}
else if(bm_category.equals("charter")){
    oDialog1(response.body().getMessage(), "Close", "", true, _myActivity, "BlueMarketCharterActivityNew", 0, "");
}
else if(bm_category.equals("service")){
    oDialog1(response.body().getMessage(), "Close", "", true, _myActivity, "BlueMarketServiceActivityNew", 0, "");
}
                        } else {
                            Log.d("rate_and_rvw", String.valueOf(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewDetail> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent i = new Intent(EditReviewActivity.this, CourseDetailsActivitynewlayall.class);
        startActivity(i);*/
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, String data) {
        super.openDialog6Category(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, data);
    }
    public void oDialog1(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, String data) {
        super.openDialog6(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, data);
    }
}
