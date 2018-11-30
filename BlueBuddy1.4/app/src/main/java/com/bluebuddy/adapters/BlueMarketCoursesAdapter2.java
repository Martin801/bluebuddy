package com.bluebuddy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.bluebuddy.activities.BlueMarketCoursePicEditActivity;
import com.bluebuddy.activities.CourseDetailsActivitynewlay;
import com.bluebuddy.activities.ListingQueryCourseActivity;
import com.bluebuddy.activities.SingleChatActivity;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.data.StaticConfig;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllCourseDetails;
import com.bluebuddy.models.FlagResponse;
import com.bluebuddy.models.Friend;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.IMG_URL;

/**
 * Created by admin on 5/13/2017.
 */

public class BlueMarketCoursesAdapter2 extends RecyclerView.Adapter<BlueMarketCoursesAdapter2.ViewHolder> {

    private List<AllCourseDetails> BMCList;
    private Activity _activity;
    private Context _context;
    private String token, category,curru;
    public static Map<String, Boolean> mapMark;

    public BlueMarketCoursesAdapter2() {

    }

    public BlueMarketCoursesAdapter2(Activity a, Context c, List<AllCourseDetails> BMCList, String token, String category,String curru) {
        this._activity = a;
        this._context = c;
        this.BMCList = BMCList;
        this.token = token;
        this.category = category;
        this.curru = curru;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //each data item is just a string in this case
        public CardView cardView;
        public TextView Bmcname, Bmcprice, Bmcloc, Bmcposted;
        public ImageView Bmcpic,flag,feattag;
        public Button bmcDt, bmcedt, chatBtn,tripOptions7;
        public LinearLayout linearCourseChat, linearCourseEdit;

        public ViewHolder(View v) {
            super(v);
            tripOptions7 = (Button) v.findViewById(R.id.tripOptions7);
            feattag = (ImageView)v.findViewById(R.id.feattag);
            cardView = (CardView) v.findViewById(R.id.card_view_bm);
            Bmcname = (TextView) v.findViewById(R.id.bmcname);
            Bmcprice = (TextView) v.findViewById(R.id.bmcprice);
            Bmcloc = (TextView) v.findViewById(R.id.bmcloc);
            Bmcposted = (TextView) v.findViewById(R.id.bmcposted);
            Bmcpic = (ImageView) v.findViewById(R.id.bmcpic);
            flag = (ImageView) v.findViewById(R.id.report_flag);

            bmcDt = (Button) v.findViewById(R.id.bmcdet);
            bmcedt = (Button) v.findViewById(R.id.bmcedt);
            chatBtn = (Button) v.findViewById(R.id.chatBtn);

            linearCourseChat = (LinearLayout) v.findViewById(R.id.linearCourseChat);
            linearCourseEdit = (LinearLayout) v.findViewById(R.id.linearCourseEdit);
        }

    }

    @Override
    public BlueMarketCoursesAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Creating a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blue_market_cardco, parent, false);

        //set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        mapMark = new HashMap<>();
        return vh;
    }

    @Override
    public void onBindViewHolder(final BlueMarketCoursesAdapter2.ViewHolder holder, int position) {
        final AllCourseDetails allCourseDetails = BMCList.get(position);
     //   holder.Bmcname.setText(String.valueOf(allCourseDetails.getCategory()));
        holder.Bmcprice.setText("$"+allCourseDetails.getPrice());
        holder.Bmcloc.setText(allCourseDetails.getLocation());
        holder.Bmcposted.setText(allCourseDetails.getFname()+allCourseDetails.getLname());

        Glide.with(_activity).asBitmap().load(IMG_URL+allCourseDetails.getImage()).into(new BitmapImageViewTarget(holder.Bmcpic));

        //   holder.Bmcname.setText(String.valueOf(allCourseDetails.getCategory()));

        //vvp

        if(allCourseDetails.getCategory().contains(":"))
        {
            String[] separated = allCourseDetails.getCategory().split(":");
            // separated[0];
            // separated[1];
            separated[1] = separated[1].trim();
            holder.Bmcname.setText(separated[1]);
        } else if(!allCourseDetails.getCategory().contains(":"))
        {
         //   holder.tvevtName.setText(allEventsDetails.getActivity());
            holder.Bmcname.setText(String.valueOf(allCourseDetails.getCategory()));
        }




        //vvp

        //
        if(allCourseDetails.getFeatured().equals("1")){
            holder.feattag.setVisibility(View.VISIBLE);
        }
        else if(allCourseDetails.getFeatured().equals("0")){
            holder.feattag.setVisibility(View.GONE);
        }
        //

        holder.bmcDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_activity, CourseDetailsActivitynewlay.class);
                intent.putExtra("AllCourse", String.valueOf(allCourseDetails.getId()));
                intent.putExtra("AllCourse_Firebaseid", String.valueOf(allCourseDetails.getFirebase_id()));
                intent.putExtra("AllCourse_FirebaseEmail", allCourseDetails.getEmail());

                intent.putExtra("category", category);
                _activity.startActivity(intent);
            }
        });
        holder.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(allCourseDetails.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            String id = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();

                            HashMap userMap = (HashMap) ((HashMap) dataSnapshot.getValue()).get(id);
                            Friend user = new Friend();
                     /*--*/
                            String name = (String) userMap.get("name");
                     /*--*/
                            //user.name = (String) userMap.get("name");
                            user.email = (String) userMap.get("email");
                            user.avata = (String) userMap.get("avata");
                            user.id = id;
                            user.idRoom = id.compareTo(StaticConfig.UID) > 0 ? (StaticConfig.UID + id).hashCode() + "" : "" + (id + StaticConfig.UID).hashCode();

                            Intent intent = new Intent(v.getContext(), SingleChatActivity.class);
                            ArrayList<CharSequence> idFriend = new ArrayList<CharSequence>();
                            idFriend.add(user.id);
                    /*--*/
                            //  intent.putExtra("AllCharter",AllCharterId);
                            // intent.putExtra("email",email);
                    /*---*/
                            intent.putCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID, idFriend);
                            intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID, user.idRoom);
                            intent.putExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND,  name);

                            mapMark.put(user.id, null);
                            v.getContext().startActivity(intent);
                            //  _activity.startActivityForResult(intent, Blue_Charter_Details.ACTION_START_CHAT);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        if(Integer.valueOf(allCourseDetails.getIs_editable()) > 0){
            holder.linearCourseChat.setVisibility(View.GONE);
            holder.linearCourseEdit.setVisibility(View.VISIBLE);
        }else{
            holder.linearCourseEdit.setVisibility(View.GONE);
            holder.linearCourseChat.setVisibility(View.VISIBLE);
        }

        holder.bmcedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(_activity, BlueMarketCoursePicEditActivity.class);
                i.putExtra("COURSE_ID", String.valueOf(allCourseDetails.getId()));
                i.putExtra("category", category);
                _activity.startActivity(i);
            }
        });
        if(allCourseDetails.getUser_id().equals(curru)){
            holder.flag.setVisibility(View.GONE);
        }else if(!allCourseDetails.getUser_id().equals(curru)){
            if(allCourseDetails.getIs_flagged().equals("0")){
                holder.flag.setVisibility(View.VISIBLE);
                holder.flag.setImageResource(R.drawable.ic_flag);
                holder.flag.setOnClickListener(new View.OnClickListener() {
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
                                Call<FlagResponse> userCall = service.fr("Bearer "+token,"post","course", allCourseDetails.getId());

                                userCall.enqueue(new Callback<FlagResponse>() {
                                    @Override
                                    public void onResponse(Call<FlagResponse> call, Response<FlagResponse> response) {
                                        if (response.body().getStatus() == "true"){
                                            Log.d("fr comment", "" + response.body().getStatus());
                                            holder.flag.setImageResource(R.drawable.ic_flagred);
                                            holder.flag.setClickable(false);
                                        }
                              /*  else if (response.body().getMessage().equals("Oops! Looks like you have already reported it as flag.")){
                                    Log.d("fr comment", "" + response.body().getStatus());
                                }*/
                                    }
                                    @Override
                                    public void onFailure(Call<FlagResponse> call, Throwable t) {
                                        Log.d("onFailure", t.toString());
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
            else if(allCourseDetails.getIs_flagged().equals("1")){
                holder.flag.setVisibility(View.VISIBLE);
                holder.flag.setImageResource(R.drawable.ic_flagred);
            }

        }

        //today 21aug

        if(allCourseDetails.getFeatured().equals("0")){
            holder.tripOptions7.setVisibility(View.VISIBLE);
        }
        else if(allCourseDetails.getFeatured().equals("1")){
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
                                Intent i = new Intent(_activity,ListingQueryCourseActivity.class);
                                i.putExtra("ID",allCourseDetails.getId());
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
        return BMCList == null ? 0 : BMCList.size();
    }

    public void updateAllCourses(ArrayList<AllCourseDetails> details){
        BMCList = details;
        this.notifyDataSetChanged();
    }

}
