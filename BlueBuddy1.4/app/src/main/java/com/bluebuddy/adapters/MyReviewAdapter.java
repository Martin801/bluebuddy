package com.bluebuddy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bluebuddy.R;
import com.bluebuddy.activities.Blue_Charter_Details;
import com.bluebuddy.activities.CourseDetailsActivitynewlayall;
import com.bluebuddy.activities.ProductDetailActivitynewlay;
import com.bluebuddy.activities.ServiceDetailsActivity;
import com.bluebuddy.models.MyReviews;

import java.util.ArrayList;
import java.util.List;

import static com.bluebuddy.Utilities.Utility.makeFirstLetterCap;

/**
 * Created by USER on 5/28/2017.
 */

public class MyReviewAdapter extends RecyclerView.Adapter<MyReviewAdapter.ViewHolder> {

    private List<MyReviews> RVWList;
    private Activity _activity;
    private Context _context;
    private String token;

    public MyReviewAdapter(Activity a, Context c, List<MyReviews> RVWList, String token) {
        this._activity = a;
        this._context = c;
        this.RVWList = RVWList;
        this.token = token;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //each data item is just a string in this case
        public TextView rviewer_name, rview_date, rview_status, rview;
        public ImageView rview_stars;
        public RatingBar ratingBar;
        public CardView review_card_view;

        public ViewHolder(View v) {
            super(v);
            rviewer_name = (TextView) v.findViewById(R.id.rviewer_name);
            rview_date = (TextView) v.findViewById(R.id.rview_date);
            rview_status = (TextView) v.findViewById(R.id.rview_status);
            review_card_view = (CardView) v.findViewById(R.id.review_card_view);
           // rview_stars = (ImageView) v.findViewById(R.id.rview_stars);
            rview = (TextView) v.findViewById(R.id.rview);
            ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        }
    }

    @Override
    public MyReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Creating a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_reviews_card, parent, false);

        //set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyReviewAdapter.ViewHolder holder, int position) {

        final MyReviews myReviews = RVWList.get(position);

        if(myReviews.getRev_for().equalsIgnoreCase("service")) {

            holder.rviewer_name.setText(makeFirstLetterCap( myReviews.getFordetails().get(0).getService_type()));

        } else if(myReviews.getRev_for().equalsIgnoreCase("charter")) {

            holder.rviewer_name.setText(makeFirstLetterCap( myReviews.getFordetails().get(0).getCh_name()));

        } else if(myReviews.getRev_for().equalsIgnoreCase("product")) {

            holder.rviewer_name.setText(makeFirstLetterCap( myReviews.getFordetails().get(0).getPr_name()));

        } else if(myReviews.getRev_for().equalsIgnoreCase("course")) {

            holder.rviewer_name.setText(makeFirstLetterCap( myReviews.getFordetails().get(0).getAgency_name()));

        }

//        holder.rviewer_name.setText(myReviews.getFname() + " " + myReviews.getLname());

        holder.rview_date.setText(myReviews.getReview_dt());
        holder.rview_status.setText(myReviews.getTitle());
        holder.rview.setText(myReviews.getDescription());

        float rating = Float.parseFloat(myReviews.getRating());
        Log.d("rate:",myReviews.getRating());

       // ratingBar.setRating(rating);
        //holder.ratingBar.setRating(Float.parseFloat(myReviews.getReating()));
        holder.ratingBar.setRating(rating);

        //  holder.Bmcname.setText(String.valueOf(myReviews.getCategory()));

       /* MyReviews myReviews = RVWList.get(position);
        holder.rviewer_name.setText(String.valueOf(myReviews.getRvwerName()));
        holder.rview_date.setText(myReviews.getRvwDate());
        holder.rview_status.setText(myReviews.getRvwComplmnt());
        holder.rview.setText(myReviews.getRvw());*/

       holder.review_card_view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent intent = null;

               if(myReviews.getRev_for().equalsIgnoreCase("charter")) {

                   intent = new Intent(_activity, Blue_Charter_Details.class);
                   intent.putExtra("AllCharter", String.valueOf(myReviews.getFor_id()));
                   intent.putExtra("AllCharter_Firebaseid", "");
                   intent.putExtra("AllCharter_FirebaseEmail", "");
                   intent.putExtra("AllCharter_FirebaseApi", "");
                   intent.putExtra("charter_pro_pic", "");
                   intent.putExtra("charter_name", "");

               } else if(myReviews.getRev_for().equalsIgnoreCase("course")) {

                   intent = new Intent(_activity, CourseDetailsActivitynewlayall.class);
                   intent.putExtra("AllCourse", String.valueOf(myReviews.getFor_id()));
                   intent.putExtra("AllCourse_Firebaseid", "");
                   intent.putExtra("AllCourse_FirebaseEmail", "");
                   intent.putExtra("course_pro_pic", "");
                   intent.putExtra("course_name", "");
                   intent.putExtra("course_fireapi", "");

                   intent.putExtra("category", myReviews.getFordetails().get(0).getCategory());

               } else if(myReviews.getRev_for().equalsIgnoreCase("product")) {

                   intent = new Intent(_activity, ProductDetailActivitynewlay.class);
                   intent.putExtra("AllProduct", String.valueOf(myReviews.getFor_id()));
                   intent.putExtra("AllProduct_Firebaseid", "");
                   intent.putExtra("AllProduct_FirebaseEmail", "");
                   intent.putExtra("product_profile_pic", "");
                   intent.putExtra("product_name", "");
                   intent.putExtra("product_fireapi", "");
                   intent.putExtra("Category", myReviews.getFordetails().get(0).getCategory());

//                   holder.rviewer_name.setText(makeFirstLetterCap( myReviews.getFordetails().get(0).getPr_name()));

               } else if(myReviews.getRev_for().equalsIgnoreCase("service")) {

                   intent = new Intent(_activity, ServiceDetailsActivity.class);
                   intent.putExtra("AllService", String.valueOf(myReviews.getFor_id()));
                   intent.putExtra("firebaseid", "");
                   intent.putExtra("AllService_FirebaseEmail", "");
                   intent.putExtra("service_pro_pic", "");
                   intent.putExtra("service_name", "");
                   intent.putExtra("service_fireapi", "");

                   intent.putExtra("category", myReviews.getFordetails().get(0).getService_type());

//                   holder.rviewer_name.setText(makeFirstLetterCap( myReviews.getFordetails().get(0).getService_type()));

               }

                   intent.putExtra("pass", 101);
               _activity.startActivity(intent);
           }
       });

    }

    @Override
    public int getItemCount() {
        /*return RVWList.size();*/
        return RVWList == null ? 0 : RVWList.size();
    }

    public void updateAllReview(ArrayList<MyReviews> details) {
        RVWList = details;
        this.notifyDataSetChanged();
    }
}