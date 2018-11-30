package com.bluebuddy.adapters;

import android.app.Activity;
import android.content.Context;
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
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.models.BadgeDetail;
import com.bluebuddy.models.InviteBuddyDetail;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import static com.bluebuddy.app.AppConfig.IMG_URL;

public class InviteBuddyContactListAdapter extends RecyclerView.Adapter<InviteBuddyContactListAdapter.ViewHolder> {

    private Activity _activity;
    private Context _context;
    boolean flag = true;
    private String token;
    private FirebaseAuth auth;
    private ArrayList<InviteBuddyDetail> BuddyList22;
    private ArrayList<String> selectedStrings = new ArrayList<String>();

    public InviteBuddyContactListAdapter() {
    }

    public InviteBuddyContactListAdapter(Activity a, Context c, ArrayList<InviteBuddyDetail> BuddyList, String token) {
        this.BuddyList22= BuddyList;
        this._activity = a;
        this._context = c;
        this.token = token;
        auth = FirebaseAuth.getInstance();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView chat;
        public ImageView imginterest;
        public ImageView badge2;
        public ImageView badge1;
        public MyTextView name;
        public ImageView pro_img;
        private LinearLayout row;


        public ViewHolder(View v) {
            super(v);

            // chat=(ImageView)v.findViewById(R.id.chat);
            badge1=(ImageView)v.findViewById(R.id.pplcert1);
            badge2=(ImageView)v.findViewById(R.id.pplcert2);
            pro_img=(ImageView)v.findViewById(R.id.pro_img);
            name=(MyTextView)v.findViewById(R.id.name);
            row=(LinearLayout)v.findViewById(R.id.row);
            imginterest=(ImageView)v.findViewById(R.id.imginterest);


        }
    }

    @Override
    public InviteBuddyContactListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.invite_buddy_contact_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final InviteBuddyContactListAdapter.ViewHolder holder, final int position) {
        final InviteBuddyDetail buddyDetail = BuddyList22.get(position);
        holder.name.setText(buddyDetail.getFname());
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
        ArrayList<BadgeDetail> badgeDetails = buddyDetail.getBadgeDetails();

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
        }
       /* holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), PeopleProfileActivity.class);
                i.putExtra("uid", buddyDetail.getUser_id());
                v.getContext().startActivity(i);
            }
        });*/
        if (BuddyList22.get(position).isImgCheckUser()) {
            holder.imginterest.setImageResource(R.drawable.checked);
        }else{
            holder.imginterest.setImageResource(R.drawable.unchecked);
        }

        holder.imginterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    selectBuddy(position, holder);
            }
        });

    }

    @Override
    public int getItemCount() {
        return BuddyList22.size();
    }

    public void updateBuddyList(ArrayList<InviteBuddyDetail> details){
        BuddyList22 = details;

        this.notifyDataSetChanged();
    }

    private void selectBuddy(int position, ViewHolder holder){
        if (BuddyList22.get(position).isImgCheckUser()) {
            BuddyList22.get(position).setImgCheckUser(false);
            holder.imginterest.setImageResource(R.drawable.unchecked);

            for (int ik = 0; ik < selectedStrings.size(); ik++) {
                if (selectedStrings.get(ik).equals(BuddyList22.get(position).getUser_id())) {
                    selectedStrings.remove(ik);
                }
            }

        } else {
            BuddyList22.get(position).setImgCheckUser(true);
            holder.imginterest.setImageResource(R.drawable.checked);
            selectedStrings.add(BuddyList22.get(position).getUser_id());

        }
    }
    public ArrayList<String> getSelectedStrings() {
        return selectedStrings;
    }
}
