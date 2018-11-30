package com.bluebuddy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bluebuddy.R;
import com.bluebuddy.activities.BlueMarketActivity;
import com.bluebuddy.activities.BlueMarketActivity22;
import com.bluebuddy.activities.BlueMarketCharterPicEditActivity;
import com.bluebuddy.activities.Blue_Charter_Details;
import com.bluebuddy.activities.FeaturedListingActivity;
import com.bluebuddy.activities.FreeListingActivity;
import com.bluebuddy.activities.ListingQueryActivityCharter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllCharterDetails;
import com.bluebuddy.models.FlagResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.IMG_URL;

/**
 * Created by admin on 5/13/2017.
 */

public class BlueCharterAdapter2 extends RecyclerView.Adapter<BlueCharterAdapter2.ViewHolder> {

    private List<AllCharterDetails> BCCList;
    private Activity _activity;
    private Context _context;
    private BlueMarketActivity blueMarketActivity;
    private FreeListingActivity freeListingActivity;
    private FeaturedListingActivity featuredListingActivity;
    private String token,curru;
    private SharedPreferences pref;
    private boolean flag1;

    public BlueCharterAdapter2() {
    }


    public BlueCharterAdapter2(Activity a, Context c, List<AllCharterDetails> BCCList, String token,String curru) {
        this._activity = a;
//        this.blueMarketActivity = (BlueMarketActivity) a;

        //  this.freeListingActivity = (FreeListingActivity)a;
        //this.featuredListingActivity = (FeaturedListingActivity)a;
        this._context = c;
        this.BCCList = BCCList;
        this.token = token;
        this.curru=curru;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //each data item is just a string in this case
        public CardView cardView;
        public TextView Bccname, Bccprice, Bccloc, Bccposted;
        private  TextView tvhp,tvfp,tvbtname,tvbtloc,tvbtdesc,tvbttype,tvbtcap,tvcrtrname,tvcrtmail,tvcrtrphone;

        public ImageView Bccpic,flag_report,feattag;
        public Button bccDt, bmcedt, chatBtn,tripOptions7;
        public LinearLayout linearCourseChat, linearCourseEdit;

        public ViewHolder(View v) {
            super(v);
            tripOptions7 = (Button) v.findViewById(R.id.tripOptions7);
            feattag = (ImageView)v.findViewById(R.id.feattag);
            flag_report= (ImageView) v.findViewById(R.id.report_flag);
            cardView = (CardView) v.findViewById(R.id.card_view_bm);
            Bccname = (TextView) v.findViewById(R.id.bmcname);
            Bccprice = (TextView) v.findViewById(R.id.bmcprice);
            Bccloc = (TextView) v.findViewById(R.id.bmcloc);
            Bccposted = (TextView) v.findViewById(R.id.bmcposted);
            Bccpic = (ImageView) v.findViewById(R.id.bmcpic);
            bccDt = (Button) v.findViewById(R.id.bmcdet);
            bmcedt = (Button) v.findViewById(R.id.bmcedt);
            chatBtn = (Button) v.findViewById(R.id.chatBtn);
            //details
            tvhp=(TextView)v.findViewById(R.id.tvhp);
            tvfp=(TextView)v.findViewById(R.id.tvfp);
            // tvbtname=(TextView)findViewById(R.id.tvbtname);
            tvbtloc=(TextView)v.findViewById(R.id.tvbtloc);
            tvbtdesc=(TextView)v.findViewById(R.id.tvbtdesc);
            tvbttype=(TextView)v.findViewById(R.id.tvbttype);
            tvbtcap=(TextView)v.findViewById(R.id.tvbtcap);
            tvcrtrname=(TextView)v.findViewById(R.id.tvcrtrname);
            tvcrtmail=(TextView)v.findViewById(R.id.tvcrtmail);
            tvcrtrphone=(TextView)v.findViewById(R.id.tvcrtrphone);
            //details
            linearCourseChat = (LinearLayout) v.findViewById(R.id.linearCourseChat);
            linearCourseEdit = (LinearLayout) v.findViewById(R.id.linearCourseEdit);
        }

    }

    @Override
    public BlueCharterAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Creating a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blue_charter_card, parent, false);

        //set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final BlueCharterAdapter2.ViewHolder holder, final int position) {
        final AllCharterDetails allCharterDetails = BCCList.get(position);

        //Log.d("allCharterDetails", allCharterDetails.toString());

       // BlueCharterItems blueCharterItems = BCCList.get(position);
        holder.Bccname.setText(String.valueOf(allCharterDetails.getCharter_nm()));
        holder.Bccprice.setText("$"+allCharterDetails.getPrice());
        holder.Bccloc.setText(allCharterDetails.getLocation());
        holder.Bccposted.setText(allCharterDetails.getFname()+" "+allCharterDetails.getLname());
        if(allCharterDetails.getUser_id().equals(curru)){
            holder.flag_report.setVisibility(View.GONE);
        }
        else if(allCharterDetails.getUser_id().equals(curru)){
            holder.flag_report.setVisibility(View.VISIBLE);
        }
       /* holder.flag_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oDialog("Are you sure? You want to logout!", "Yes", "No", true, _activity, "", 0,"charter",allCharterDetails.getId(),token,holder);

              *//*  if(flag1){
                    holder.flag_report.setImageResource(R.drawable.ic_flag);
                    flag1=false;

                } else {
                    holder.flag_report.setImageResource(R.drawable.ic_flagred);
                    flag1 = true;
                }*//*
            }
        });*/
        if(allCharterDetails.getUser_id().equals(curru)){
            holder.flag_report.setVisibility(View.GONE);
        }else if(!allCharterDetails.getUser_id().equals(curru)){
            if(allCharterDetails.getIs_flagged().equals("0")){
                holder.flag_report.setVisibility(View.VISIBLE);
                holder.flag_report.setImageResource(R.drawable.ic_flag);
                holder.flag_report.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
                        _context=v.getContext().getApplicationContext();
                        LayoutInflater inflater = (LayoutInflater) _context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                        final View dialogView = inflater.inflate(R.layout.custom_popup_inputdialog1, null);
                        dialogBuilder.setView(dialogView);
                        dialogBuilder.setMessage("Report");
                        dialogBuilder.setPositiveButton("Report", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                                Call<FlagResponse> userCall = service.fr("Bearer "+token,"post","charter", allCharterDetails.getId());

                                userCall.enqueue(new Callback<FlagResponse>() {
                                    @Override
                                    public void onResponse(Call<FlagResponse> call, Response<FlagResponse> response) {
                                        if (response.body().getStatus() == "true"){
                                            //Log.d("fr comment", "" + response.body().getStatus());
                                            holder.flag_report.setImageResource(R.drawable.ic_flagred);
                                            holder.flag_report.setClickable(false);
                                        }
                              /*  else if (response.body().getMessage().equals("Oops! Looks like you have already reported it as flag.")){
                                    Log.d("fr comment", "" + response.body().getStatus());
                                }*/
                                    }
                                    @Override
                                    public void onFailure(Call<FlagResponse> call, Throwable t) {
                                      //  Log.d("onFailure", t.toString());
                                        t.printStackTrace();
                                    }
                                });

                            }
                        });
                        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //pass
                            }
                        });
                        AlertDialog b = dialogBuilder.create();
                        b.show();

                    }
                });
            }
            else if(allCharterDetails.getIs_flagged().equals("1")){
                holder.flag_report.setVisibility(View.VISIBLE);
                holder.flag_report.setImageResource(R.drawable.ic_flagred);
            }

        }


        if(allCharterDetails.getImage() != ""){
            Glide.with(_activity).asBitmap().load(IMG_URL+allCharterDetails.getImage()).into(new BitmapImageViewTarget(holder.Bccpic));
        }

        //
        if(allCharterDetails.getFeatured().equals("1")){
            holder.feattag.setVisibility(View.VISIBLE);
        }
        else if(allCharterDetails.getFeatured().equals("0")){
            holder.feattag.setVisibility(View.GONE);
        }
        holder.bccDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  apicharterdetail(holder, String.valueOf(allCharterDetails.getId()));
                Intent intent = new Intent(_activity, Blue_Charter_Details.class);
                intent.putExtra("AllCharter", String.valueOf(allCharterDetails.getId()));
                intent.putExtra("AllCharter_Firebaseid", String.valueOf(allCharterDetails.getFirebase_id()));
                intent.putExtra("pass",2);
                _activity.startActivity(intent);
            }
        });

        if(Integer.valueOf(allCharterDetails.getIs_editable()) > 0){
            holder.linearCourseChat.setVisibility(View.GONE);
            holder.linearCourseEdit.setVisibility(View.VISIBLE);
        }else{
            holder.linearCourseEdit.setVisibility(View.GONE);
            holder.linearCourseChat.setVisibility(View.VISIBLE);
        }

        holder.bmcedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(_activity, BlueMarketCharterPicEditActivity.class);
                i.putExtra("AllCharter", allCharterDetails.getId());
                i.putExtra("pass","2");
               // i.putExtra("category", category);
                _activity.startActivity(i);
            }
        });


        //today 21aug

        if(allCharterDetails.getFeatured().equals("0")){
            holder.tripOptions7.setVisibility(View.VISIBLE);
        }
        else if(allCharterDetails.getFeatured().equals("1")){
            holder.tripOptions7.setVisibility(View.GONE);
        }

        holder.tripOptions7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(v.getContext(), holder.tripOptions7);
                //inflating menu from xml resource
                popup.inflate(R.menu.trip_option_menufeature);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                Intent i = new Intent(_activity,ListingQueryActivityCharter.class);
                                i.putExtra("ID",allCharterDetails.getId());
                                _activity.startActivity(i);
                                //handle menu1 click
                                break;
                         /*   case R.id.menu2:
                                //handle menu2 click
                                break;
                            case R.id.menu3:
                                //handle menu3 click
                                break;*/
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });

        //today 21aug


    }

    @Override
    public int getItemCount() {
        //return BCCList.size();
        return BCCList == null ? 0 : BCCList.size();
    }
    public void updateAllCharter(ArrayList<AllCharterDetails> details){
        BCCList = details;
        this.notifyDataSetChanged();
    }
    public void oDialog(String msg, String btnTxt, String btnTxt2, final boolean _redirect, final Activity activity, final String _redirectClass, int dialogLayout,final String type, final String type_id, final String token, final ViewHolder holder) {
        BlueMarketActivity22 b=new BlueMarketActivity22();
        b.openDialog15(msg,btnTxt,btnTxt2,_redirect,activity,_redirectClass,dialogLayout,type,type_id,token,holder);
    }
    }
