package com.bluebuddy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bluebuddy.R;
import com.bluebuddy.activities.EditReviewActivity;
import com.bluebuddy.activities.PeopleProfileActivity;
import com.bluebuddy.models.ReviewDetails;

import java.util.ArrayList;
import java.util.List;

import static com.bluebuddy.app.AppConfig.IMG_URL;

/**
 * Created by admin on 7/12/2017.
 */

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ViewHolder> {

    private List<ReviewDetails> _reviews;
    private String uid,bm_category,for_id,cat;
    private Activity _activity;
    private Context _context;
    private String token;
    private SharedPreferences pref;


    public ReviewRecyclerAdapter(Activity _activity, Context _context, Object o) {
        super();
    }

    public ReviewRecyclerAdapter(Activity a, Context c, List<ReviewDetails> r, String token) {
        this._activity = a;
        this._context = c;
        this._reviews = r;
        this.token = token;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blue_review_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final ReviewRecyclerAdapter.ViewHolder holder, final int position) {

        final ReviewDetails reviewDetails = _reviews.get(position);
        if(uid.equals(reviewDetails.getFirebase_reg_id())){
            holder.rvw_edt.setVisibility(View.VISIBLE);
        }
        else if(!uid.equals(reviewDetails.getFirebase_reg_id())){
            holder.rvwer_img.setVisibility(View.VISIBLE);
        }

       // Log.d("reviewDetails", reviewDetails.toString());
        TextView rviewer_name, rv_date, rvtitle, rvdesc;

        // BlueCharterItems blueCharterItems = BCCList.get(position);

        holder.rv_date.setText(reviewDetails.getDate());
        holder.rvtitle.setText(reviewDetails.getTitle());
        holder.rvdesc.setText(reviewDetails.getDescription());
        holder.rviewer_name.setText(reviewDetails.getReviewer_fname() + " " + reviewDetails.getReviewer_lname());

       Glide.with(_context).asBitmap().load(IMG_URL + reviewDetails.getProfile_pic()).into(new BitmapImageViewTarget(holder.rvwer_img) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(_context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.rvwer_img.setImageDrawable(circularBitmapDrawable);
            }
        });
        holder.rvwer_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), PeopleProfileActivity.class);
                i.putExtra("uid", String.valueOf(reviewDetails.getUser_id()));
                v.getContext().startActivity(i);
            }
        });
        final float rating = Float.parseFloat(reviewDetails.getRating());
        holder.ratingBar1.setRating(rating);
        holder.rvw_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(v.getContext(), EditReviewActivity.class);
                /*rvw_title, description, rating_value, "course", id*/
                i.putExtra("rating_value",reviewDetails.getRating());
                i.putExtra("description",reviewDetails.getDescription());
                i.putExtra("rvw_title",reviewDetails.getTitle());
                i.putExtra("bm_category",bm_category);
                i.putExtra("for_id",for_id);
                i.putExtra("id",reviewDetails.getId());
                i.putExtra("category",cat);
                v.getContext().startActivity(i);
            }
        });
        /*if(reviewDetails.getImage() != ""){
            Glide.with(_activity).load(IMG_URL+reviewDetails.getImage()).asBitmap().into(new BitmapImageViewTarget(holder.Bccpic));
        }*/

       /* holder.bccDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_activity, Blue_Charter_Details.class);
                intent.putExtra("AllCharter", String.valueOf(allCharterDetails.getId()));
                _activity.startActivity(intent);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return _reviews == null ? 0 : _reviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView rviewer_name, rv_date, rvtitle, rvdesc;
        RatingBar ratingBar1;
        ImageButton rvw_edt;
        ImageView rvwer_img;

        public ViewHolder(View v) {
            super(v);
            ratingBar1 = (RatingBar) v.findViewById(R.id.rateservice);
            rviewer_name = (TextView) v.findViewById(R.id.rviewer_name);
            rv_date = (TextView) v.findViewById(R.id.rv_date);
            rvtitle = (TextView) v.findViewById(R.id.rvtitle);
            rvdesc = (TextView) v.findViewById(R.id.rvdesc);
            rvw_edt=(ImageButton)v.findViewById(R.id.rvw_edt);
            rvwer_img=(ImageView) v.findViewById(R.id.rvwer_img);
        }
    }
    public void addReview(ReviewDetails r) {

        if(_reviews==null){
            _reviews=new ArrayList<ReviewDetails>();

        }
        _reviews.add(r);
        this.notifyDataSetChanged();
    }
    public void updateAllReview(List<ReviewDetails> r, String uid,String bm_category,String for_id,String cat,Context context) {
        this._reviews = r;
        this.uid=uid;
        this.cat=cat;
        this._context=context;
        this.for_id=for_id;
        this.bm_category=bm_category;
        this.notifyDataSetChanged();
    }
}
