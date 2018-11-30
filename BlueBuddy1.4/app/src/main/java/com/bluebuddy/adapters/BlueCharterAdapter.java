package com.bluebuddy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.bluebuddy.R;
import com.bluebuddy.activities.BlueMarketActivity;
import com.bluebuddy.activities.BlueMarketActivity2;
import com.bluebuddy.activities.BlueMarketCharterPicEditActivity;
import com.bluebuddy.activities.Blue_Charter_Details;
import com.bluebuddy.activities.ChatMessagingActivity;
import com.bluebuddy.activities.FeaturedListingActivity;
import com.bluebuddy.activities.FreeListingActivity;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.data.FriendDB;
import com.bluebuddy.data.StaticConfig;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllCharterDetails;
import com.bluebuddy.models.FlagResponse;
import com.bluebuddy.models.Friend;
import com.bluebuddy.models.ListFriend;

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

public class BlueCharterAdapter extends RecyclerView.Adapter<BlueCharterAdapter.ViewHolder> {

    private static final String TAG = "BlueCharterAdapter";
    // private List<current_user_details> BCCList1;
    public static Map<String, Boolean> mapMark;
    boolean flag1 = true;
    private List<AllCharterDetails> BCCList;
    private Activity _activity;
    private Context _context;
    private BlueMarketActivity blueMarketActivity;
    private FreeListingActivity freeListingActivity;
    private FeaturedListingActivity featuredListingActivity;
    private String token, curru;
    private SharedPreferences pref;
    private ArrayList<String> listFriendID = null;
    private ListFriend dataListFriend = null;

    public BlueCharterAdapter() {
    }


    public BlueCharterAdapter(Activity a, Context c, List<AllCharterDetails> BCCList, String token, String curru) {
        this._activity = a;

//        this.blueMarketActivity = (BlueMarketActivity) a;
        //  this.freeListingActivity = (FreeListingActivity)a;
        //this.featuredListingActivity = (FeaturedListingActivity)a;

        this._context = c;
        this.BCCList = BCCList;
        this.token = token;
        this.curru = curru;
        if (dataListFriend == null) {
            dataListFriend = FriendDB.getInstance(_context).getListFriend();
            if (dataListFriend.getListFriend().size() > 0) {
                listFriendID = new ArrayList<>();
                for (Friend friend : dataListFriend.getListFriend()) {
                    listFriendID.add(friend.id);
                }
            }
        }
    }

    @Override
    public BlueCharterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Creating a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blue_charter_card, parent, false);

        //set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        mapMark = new HashMap<>();

        return vh;
    }

    @Override
    public void onBindViewHolder(final BlueCharterAdapter.ViewHolder holder, final int position) {
        final AllCharterDetails allCharterDetails = BCCList.get(position);
        // Log.d("allCharterDetails", allCharterDetails.toString());

        holder.Bccname.setText(String.valueOf(allCharterDetails.getCharter_nm()));
        holder.Bccprice.setText("$" + allCharterDetails.getPrice());
        holder.Bccloc.setText(allCharterDetails.getLocation());
        holder.Bccposted.setText(allCharterDetails.getFname() + " " + allCharterDetails.getLname());

        if (allCharterDetails.getUser_id().equals(curru)) {
            holder.flag.setVisibility(View.GONE);
        } else if (!allCharterDetails.getUser_id().equals(curru)) {
            if (allCharterDetails.getIs_flagged().equals("0")) {
                holder.flag.setVisibility(View.VISIBLE);
                holder.flag.setImageResource(R.drawable.ic_flag);
                holder.flag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
                        _context = v.getContext().getApplicationContext();
                        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View dialogView = inflater.inflate(R.layout.custom_popup_inputdialog1, null);
                        dialogBuilder.setView(dialogView);
                        dialogBuilder.setMessage("Report");


                        dialogBuilder.setPositiveButton("Report", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                                Call<FlagResponse> userCall = service.fr("Bearer " + token, "post", "charter", allCharterDetails.getId());

                                userCall.enqueue(new Callback<FlagResponse>() {
                                    @Override
                                    public void onResponse(Call<FlagResponse> call, Response<FlagResponse> response) {
                                        if (response.body().getStatus() == "true") {
                                            //  Log.d("fr comment", "" + response.body().getStatus());
                                            holder.flag.setImageResource(R.drawable.ic_flagred);
                                            holder.flag.setClickable(false);
                                        }
                              /*  else if (response.body().getMessage().equals("Oops! Looks like you have already reported it as flag.")){
                                    Log.d("fr comment", "" + response.body().getStatus());
                                }*/
                                    }

                                    @Override
                                    public void onFailure(Call<FlagResponse> call, Throwable t) {
                                        //   Log.d("onFailure", t.toString());
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
            } else if (allCharterDetails.getIs_flagged().equals("1")) {
                holder.flag.setVisibility(View.VISIBLE);
                holder.flag.setImageResource(R.drawable.ic_flagred);
            }

        }

        final ImageView imageView = holder.Bccpic;


       /* Log.d("freg",allCharterDetails.getFirebase_reg());
        Log.d("fid",allCharterDetails.getFirebase_id());
        Log.d("fmail",allCharterDetails.getEmail());*/


        if (allCharterDetails.getImage() != "") {
            // Glide.with(imageView.getContext()).load(IMG_URL+allCharterDetails.getImage()).asBitmap().into(new BitmapImageViewTarget(holder.Bccpic));
            Glide.with(imageView.getContext()).asBitmap().load(IMG_URL + allCharterDetails.getImage()).into(new BitmapImageViewTarget(holder.Bccpic));

        }
        //
        if (allCharterDetails.getFeatured().equals("1")) {
            holder.feattag.setVisibility(View.VISIBLE);
        } else if (allCharterDetails.getFeatured().equals("0")) {
            holder.feattag.setVisibility(View.GONE);
        }
        //
        holder.bccDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  apicharterdetail(holder, String.valueOf(allCharterDetails.getId()));

                Log.d(TAG, "onClick: charter id " + String.valueOf(allCharterDetails.getId()) + "  Firebase_id  " + allCharterDetails.getFirebase_id() + " Email " + allCharterDetails.getEmail() + "  Firebase_api  " + allCharterDetails.getFirebase_api() + " Fname " + allCharterDetails.getFname() + " Lname  " + allCharterDetails.getLname());

                Intent intent = new Intent(v.getContext(), Blue_Charter_Details.class);
                intent.putExtra("AllCharter", String.valueOf(allCharterDetails.getId()));
                intent.putExtra("AllCharter_Firebaseid", allCharterDetails.getFirebase_id());
                intent.putExtra("AllCharter_FirebaseEmail", allCharterDetails.getEmail());
                intent.putExtra("AllCharter_FirebaseApi", allCharterDetails.getFirebase_api());
                intent.putExtra("charter_pro_pic", allCharterDetails.getProfile_pic());
                intent.putExtra("charter_name", allCharterDetails.getFname() + " " + allCharterDetails.getLname());
                intent.putExtra("pass", 1);

                v.getContext().startActivity(intent);

            }
        });
        holder.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Intent intent = new Intent(v.getContext(), ChatMessagingActivity.class);
                intent.putExtra("receiver_id", allCharterDetails.getFirebase_id());
                intent.putExtra("email", allCharterDetails.getEmail());
                intent.putExtra("image", allCharterDetails.getProfile_pic());
                intent.putExtra("name", allCharterDetails.getFname() + " " + allCharterDetails.getLname());

                v.getContext().startActivity(intent);

//                findIDEmail1(allCharterDetails.getEmail());
//
//                FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(allCharterDetails.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.getValue() != null) {
//                            String id = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();
//
//                            HashMap userMap = (HashMap) ((HashMap) dataSnapshot.getValue()).get(id);
//                            Friend user = new Friend();
//
//                            String name = (String) userMap.get("name");
//                            user.email = (String) userMap.get("email");
//                            user.avata = (String) userMap.get("avata");
//                            user.id = id;
//
//                            user.idRoom = id.compareTo(StaticConfig.UID) > 0 ? (StaticConfig.UID + id).hashCode() + "" : "" + (id + StaticConfig.UID).hashCode();
//
//                            Toast.makeText(_activity, "StaticConfig.UID"+StaticConfig.UID+"     user id"+id+"   room id"+user.idRoom, Toast.LENGTH_SHORT).show();
//
//                            Intent intent = new Intent(v.getContext(), SingleChatActivitychat.class);
//                            ArrayList<CharSequence> idFriend = new ArrayList<CharSequence>();
//                            idFriend.add(user.id);
//                            intent.putCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID, idFriend);
//                            intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID, user.idRoom);
//                            intent.putExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND, name);
//
//                            intent.putExtra("AllCharter", String.valueOf(allCharterDetails.getId()));
//                            intent.putExtra("AllCharter_Firebaseid", allCharterDetails.getFirebase_id());
//                            intent.putExtra("AllCharter_FirebaseEmail", allCharterDetails.getEmail());
//                            intent.putExtra("charter_pro_pic", allCharterDetails.getProfile_pic());
//                            intent.putExtra("charter_name", allCharterDetails.getFname()+" "+allCharterDetails.getLname());
//                            intent.putExtra("AllCharter_FirebaseApi", allCharterDetails.getFirebase_api());
//                            mapMark.put(user.id, null);
//                            v.getContext().startActivity(intent);
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });

            }
        });

        if (Integer.valueOf(allCharterDetails.getIs_editable()) > 0) {
            holder.linearCourseChat.setVisibility(View.GONE);
            holder.linearCourseEdit.setVisibility(View.VISIBLE);
        } else {
            holder.linearCourseEdit.setVisibility(View.GONE);
            holder.linearCourseChat.setVisibility(View.VISIBLE);
        }

        holder.bmcedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), BlueMarketCharterPicEditActivity.class);
                i.putExtra("AllCharter", allCharterDetails.getId());
                i.putExtra("pass", "1");
                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        //return BCCList.size();
        return BCCList == null ? 0 : BCCList.size();
    }

    public void updateAllCharter(ArrayList<AllCharterDetails> details) {
        BCCList = details;
        this.notifyDataSetChanged();
    }

    public void oDialog(String msg, String btnTxt, String btnTxt2, final boolean _redirect, final Activity activity, final String _redirectClass, int dialogLayout, final String type, final String type_id, final String token, final ViewHolder holder) {
        BlueMarketActivity2 b = new BlueMarketActivity2();
        b.openDialog14(msg, btnTxt, btnTxt2, _redirect, activity, _redirectClass, dialogLayout, type, type_id, token, holder);
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        BlueMarketActivity2 b = new BlueMarketActivity2();
        b.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    private void findIDEmail1(String email) {

        FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null) {
                    //email not found
                } else {
                    String id = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();
                    if (id.equals(StaticConfig.UID)) {

                    } else {
                        HashMap userMap = (HashMap) ((HashMap) dataSnapshot.getValue()).get(id);
                        Friend user = new Friend();
                        user.name = (String) userMap.get("name");
                        user.email = (String) userMap.get("email");
                        user.avata = (String) userMap.get("avata");
                        user.id = id;
                        user.idRoom = id.compareTo(StaticConfig.UID) > 0 ? (StaticConfig.UID + id).hashCode() + "" : "" + (id + StaticConfig.UID).hashCode();
                        checkBeforAddFriend(id, user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkBeforAddFriend(final String idFriend, Friend userInfo) {


        //Check xem da ton tai id trong danh sach id chua
        if (listFriendID != null) {
            if (listFriendID.contains(idFriend)) {

            } else {
                addFriend(idFriend, true);
                listFriendID.add(idFriend);
                dataListFriend.getListFriend().add(userInfo);
                FriendDB.getInstance(_context).addFriend(userInfo);
                //  FriendDB.getInstance(getContext()).addFriend(userInfo);
                // adapter.notifyDataSetChanged();
            }
        }
    }

    private void addFriend(final String idFriend, boolean isIdFriend) {
        if (idFriend != null) {
            if (isIdFriend) {
                FirebaseDatabase.getInstance().getReference().child("friend/" + StaticConfig.UID).push().setValue(idFriend)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    addFriend(idFriend, false);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            } else {
                FirebaseDatabase.getInstance().getReference().child("friend/" + idFriend).push().setValue(StaticConfig.UID).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            addFriend(null, false);
                        }
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        } else {

        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //each data item is just a string in this case
        public CardView cardView;
        public TextView Bccname, Bccprice, Bccloc, Bccposted;
        public ImageView Bccpic, flag, feattag;
        public Button bccDt, bmcedt, chatBtn;
        public LinearLayout linearCourseChat, linearCourseEdit;
        private TextView tvhp, tvfp, tvbtname, tvbtloc, tvbtdesc, tvbttype, tvbtcap, tvcrtrname, tvcrtmail, tvcrtrphone;

        public ViewHolder(View v) {
            super(v);
            feattag = (ImageView) v.findViewById(R.id.feattag);
            flag = (ImageView) v.findViewById(R.id.report_flag);
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
            tvhp = (TextView) v.findViewById(R.id.tvhp);
            tvfp = (TextView) v.findViewById(R.id.tvfp);
            // tvbtname=(TextView)findViewById(R.id.tvbtname);
            tvbtloc = (TextView) v.findViewById(R.id.tvbtloc);
            tvbtdesc = (TextView) v.findViewById(R.id.tvbtdesc);
            tvbttype = (TextView) v.findViewById(R.id.tvbttype);
            tvbtcap = (TextView) v.findViewById(R.id.tvbtcap);
            tvcrtrname = (TextView) v.findViewById(R.id.tvcrtrname);
            tvcrtmail = (TextView) v.findViewById(R.id.tvcrtmail);
            tvcrtrphone = (TextView) v.findViewById(R.id.tvcrtrphone);
            //details
            linearCourseChat = (LinearLayout) v.findViewById(R.id.linearCourseChat);
            linearCourseEdit = (LinearLayout) v.findViewById(R.id.linearCourseEdit);
        }

    }
}
