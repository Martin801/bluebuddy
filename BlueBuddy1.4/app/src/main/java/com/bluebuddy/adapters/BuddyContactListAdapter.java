package com.bluebuddy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bluebuddy.R;
import com.bluebuddy.activities.PeopleProfileActivity;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.models.BuddyDetail;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import static com.bluebuddy.app.AppConfig.IMG_URL;

public class BuddyContactListAdapter extends RecyclerView.Adapter<BuddyContactListAdapter.ViewHolder> {

    private Activity _activity;
    private Context _context;
    boolean flag = true;
    private String token;
    private FirebaseAuth auth;
    private ArrayList<BuddyDetail> BuddyList22;

    public BuddyContactListAdapter() {
    }

    public BuddyContactListAdapter(Activity a, Context c, ArrayList<BuddyDetail> BuddyList, String token) {
        this.BuddyList22= BuddyList;
        this._activity = a;
        this._context = c;
        this.token = token;
        auth = FirebaseAuth.getInstance();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView chat;
        private ImageView badge2;
        private ImageView badge1;
        private MyTextView name;
        private ImageView pro_img;
        private LinearLayout row;


        public ViewHolder(View v) {
            super(v);

            // chat=(ImageView)v.findViewById(R.id.chat);
            badge1=(ImageView)v.findViewById(R.id.pplcert1);
            badge2=(ImageView)v.findViewById(R.id.pplcert2);
            pro_img=(ImageView)v.findViewById(R.id.pro_img);
            name=(MyTextView)v.findViewById(R.id.name);
            row=(LinearLayout)v.findViewById(R.id.row);


        }
    }

    @Override
    public BuddyContactListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.buddy_contact_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final BuddyContactListAdapter.ViewHolder holder, final int position) {
        final BuddyDetail buddyDetail = BuddyList22.get(position);
/*
        if(buddyDetail.getBadge().size()==2)
*/
        {

        }
        holder.name.setText(buddyDetail.getFname()+" "+buddyDetail.getLname());
        if(!buddyDetail.getDp().equals("")) {

            Glide.with(_activity).asBitmap().load(IMG_URL + buddyDetail.getDp()).into(new BitmapImageViewTarget(holder.pro_img) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(_activity.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.pro_img.setImageDrawable(circularBitmapDrawable);
                }
            });
        }

       /* if (buddyDetail.getBadge().get(0) != null) {
            int len = buddyDetail.getBadge().size();

            if (len == 0) {
                //if (certification_details.get(0).getCert_type().equals("Scuba_Diving") && certification_details.get(1).getCert_type().equals("Free_Diving")||certification_details.get(0).getCert_type().equals("Free_Diving")&& certification_details.get(1).getCert_type().equals("Scuba_Diving"))
                holder.badge1.setVisibility(View.GONE);
                holder.badge2.setVisibility(View.GONE);

            } else if (len == 1) {
                if (buddyDetail.getBadge().get(0).equals("Scuba_Diving") && !buddyDetail.getBadge().get(0).equals("Free_Diving")) {
                    holder.badge1.setVisibility(View.VISIBLE);
                    //      holder.badge2.setVisibility(View.GONE);
                } else if (buddyDetail.getBadge().get(0).equals("Free_Diving") && !buddyDetail.getBadge().get(0).equals("Scuba_Diving")) {
                    holder.badge2.setVisibility(View.VISIBLE);
                    //   holder.badge1.setVisibility(View.GONE);
                }
            } else if (len == 2) {
                holder.badge1.setVisibility(View.VISIBLE);
                holder.badge2.setVisibility(View.VISIBLE);
            }
        }*/
//if(buddyDetail.getBadge().get(0).equals())
      /*  String response = "details";
            JsonArray jAry = new JsonArray(response);
            JsonObject jObj = jAry.getJsonObject(0);

            JsonArray jsonBanner = jObj.getJsonArray("badge");
            JsonObject temp;
            for(int i=0;i<jsonBanner.length;i++){
                if(B)
                temp = jsonBanner.getJsonObject(i);
                String image = temp.optString("image");
            }*/

    /*    JSONObject jsonObject=jsonObj.optJSONObject(0);
        JSONArray subArray = jsonObject.getJSONArray("banner");

        // get code key from `jsonObject`
        String strCode=jsonObject.optString("code");

        // get all images urls from `subArray`
        for(int index=0;index<subArray.length();index++){
            JSONObject imgJSONObject=subArray.optJSONObject(index);
            // get image urls
            String strImgURL=imgJSONObject.optString("image");

        }*/

       /* if(buddyDetail.getBadge().size()==0)
        {
            Toast.makeText(_context,"0",Toast.LENGTH_LONG).show();
            holder.badge1.setVisibility(View.GONE);
            holder.badge2.setVisibility(View.GONE);
        }
        else if(buddyDetail.getBadge().size()==1){
            Toast.makeText(_context,"1",Toast.LENGTH_LONG).show();
           *//* if (buddyDetail.getBadge().get(0).equals("Scuba_Diving") && !buddyDetail.getBadge().get(0).equals("Free_Diving")) {
                holder.badge1.setVisibility(View.VISIBLE);
                //      holder.badge2.setVisibility(View.GONE);
            } else if (buddyDetail.getBadge().get(0).equals("Free_Diving") && !buddyDetail.getBadge().get(0).equals("Scuba_Diving")) {
                holder.badge2.setVisibility(View.VISIBLE);
                //   holder.badge1.setVisibility(View.GONE);
            }*//*
        }
        else if(buddyDetail.getBadge().size()==2){
            Toast.makeText(_context,"2",Toast.LENGTH_LONG).show();
           *//* holder.badge1.setVisibility(View.VISIBLE);
            holder.badge2.setVisibility(View.VISIBLE);*//*
        }*/
       /* ArrayList<BadgeDetail> badgeDetails = buddyDetail.getBadgeDetails();

        if (badgeDetails != null) {
            int len = badgeDetails.size();

            if (len == 0) {
                //if (certification_details.get(0).getCert_type().equals("Scuba_Diving") && certification_details.get(1).getCert_type().equals("Free_Diving")||certification_details.get(0).getCert_type().equals("Free_Diving")&& certification_details.get(1).getCert_type().equals("Scuba_Diving"))
                holder.badge1.setVisibility(View.GONE);
                holder.badge2.setVisibility(View.GONE);

            } else if (len == 1) {
                if (badgeDetails.get(0).getCertification_type().equals("Scuba_Diving") && !badgeDetails.get(0).getCertification_type().equals("Free_Diving")) {
                    holder.badge1.setVisibility(View.VISIBLE);
                    //      holder.badge2.setVisibility(View.GONE);
                } else if (badgeDetails.get(0).getCertification_type().equals("Free_Diving") && !badgeDetails.get(0).getCertification_type().equals("Scuba_Diving")) {
                    holder.badge2.setVisibility(View.VISIBLE);
                    //   holder.badge1.setVisibility(View.GONE);
                }
            } else if (len == 2) {
                holder.badge1.setVisibility(View.VISIBLE);
                holder.badge2.setVisibility(View.VISIBLE);
            }
        }*/
        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), PeopleProfileActivity.class);
                i.putExtra("uid", buddyDetail.getUser_id());

                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return BuddyList22 == null ? 0 : BuddyList22.size();
    }

    public void updateBuddyList(ArrayList<BuddyDetail> details){
        BuddyList22 = details;

        this.notifyDataSetChanged();
    }
}
