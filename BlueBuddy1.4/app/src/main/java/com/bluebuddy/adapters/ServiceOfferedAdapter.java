package com.bluebuddy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.bluebuddy.R;
import com.bluebuddy.activities.ActivityAddYourServicesEdit2;
import com.bluebuddy.activities.ChatMessagingActivity;
import com.bluebuddy.activities.ServiceDetailsActivity;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.data.FriendDB;
import com.bluebuddy.data.StaticConfig;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllServiceDetails;
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

public class ServiceOfferedAdapter extends RecyclerView.Adapter<ServiceOfferedAdapter.ViewHolder> {

    public static Map<String, Boolean> mapMark;
    boolean flag = true;
    private List<AllServiceDetails> BCCList;
    private Activity _activity;
    private Context _context;
    private String token, curru, cat;
    private FirebaseAuth auth;
    private ArrayList<String> listFriendID = null;
    private ListFriend dataListFriend = null;

    public ServiceOfferedAdapter() {

    }

    public ServiceOfferedAdapter(Activity a, Context c, List<AllServiceDetails> BCCList, String token, String curru) {
        this._activity = a;
        this._context = c;
        this.BCCList = BCCList;
        this.token = token;
        this.curru = curru;
        auth = FirebaseAuth.getInstance();
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

    public ServiceOfferedAdapter(Activity a, Context c, List<AllServiceDetails> BCCList, String token, String curru, String category) {

        this._activity = a;
        this._context = c;
        this.BCCList = BCCList;
        this.token = token;
        this.curru = curru;
        this.cat = category;
        auth = FirebaseAuth.getInstance();
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
    public ServiceOfferedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_offered_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        mapMark = new HashMap<>();

        return vh;
    }

    @Override
    public void onBindViewHolder(final ServiceOfferedAdapter.ViewHolder holder, final int position) {
        final AllServiceDetails allServiceDetails = BCCList.get(position);

        float rating = Float.parseFloat(allServiceDetails.getRating());
        Log.d("rate:", allServiceDetails.getRating());
        //ratingBar1.setRating(rating);

        holder.service_offred.setText(String.valueOf(allServiceDetails.getService_type()));

        try {
            if (allServiceDetails.getHide_details().toString().trim().equalsIgnoreCase("1")) {
                holder.ll_contact.setVisibility(View.GONE);
                holder.ll_mail.setVisibility(View.GONE);
            } else if (allServiceDetails.getHide_details().toString().trim().equalsIgnoreCase("0")) {
                holder.ll_contact.setVisibility(View.VISIBLE);
                holder.ll_mail.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }

        final ImageView imageView = holder.bmspic;

        holder.mail.setText(allServiceDetails.getEmail());

        holder.contact.setText(allServiceDetails.getPhone());

        holder.bmsname.setText(allServiceDetails.getFname() + " " + allServiceDetails.getLname());

        Glide.with(imageView.getContext()).asBitmap().load(IMG_URL + allServiceDetails.getPicture()).into(new BitmapImageViewTarget(holder.bmspic));

        holder.ratingBar.setRating(rating);
        /*if(allCharterDetails.getImage() != ""){
            Glide.with(_activity).load(IMG_URL+allCharterDetails.getImage()).asBitmap().into(new BitmapImageViewTarget(holder.bmspic));
        }*/

        if (allServiceDetails.getUser_id().equals(curru)) {
            holder.flag.setVisibility(View.GONE);
        } else if (!allServiceDetails.getUser_id().equals(curru)) {
            if (allServiceDetails.getIs_flagged().equals("0")) {
                holder.flag.setVisibility(View.VISIBLE);
                holder.flag.setImageResource(R.drawable.ic_flag);
                holder.report_flag.setOnClickListener(new View.OnClickListener() {
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
                                Call<FlagResponse> userCall = service.fr("Bearer " + token, "post", "service", allServiceDetails.getId());

                                userCall.enqueue(new Callback<FlagResponse>() {
                                    @Override
                                    public void onResponse(Call<FlagResponse> call, Response<FlagResponse> response) {
                                        if (response.body().getStatus() == "true") {
                                            //Log.d("fr comment", "" + response.body().getStatus());
                                            holder.flag.setImageResource(R.drawable.ic_flagred);
                                            holder.report_flag.setClickable(false);
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
            } else if (allServiceDetails.getIs_flagged().equals("1")) {
                holder.flag.setVisibility(View.VISIBLE);
                holder.flag.setImageResource(R.drawable.ic_flagred);
            }
        }

        holder.bmsdet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /*Intent i = new Intent(_activity, ServiceDetailsActivity.class);
               _activity.startActivity(i);*/
                Intent intent = new Intent(v.getContext(), ServiceDetailsActivity.class);
                intent.putExtra("AllService", String.valueOf(allServiceDetails.getId()));
                intent.putExtra("firebaseid", allServiceDetails.getFirebase_reg());
                intent.putExtra("AllService_FirebaseEmail", allServiceDetails.getEmail());
                intent.putExtra("service_pro_pic", allServiceDetails.getProfile_pic());
                intent.putExtra("service_name", allServiceDetails.getFname() + " " + allServiceDetails.getLname());
                intent.putExtra("service_fireapi", allServiceDetails.getFirebase_api());
                intent.putExtra("pass", 1);

                intent.putExtra("category", cat);
                /*intent.putExtra("curr_user_id",allCharterDetails.getCurrent_user_details().get(0).getFirebase_reg());*/
                v.getContext().startActivity(intent);
            }
        });

        holder.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
//                findIDEmail1(allServiceDetails.getEmail());
//                FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(allServiceDetails.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.getValue() != null) {
//                            String id = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();
//
//                            HashMap userMap = (HashMap) ((HashMap) dataSnapshot.getValue()).get(id);
//                            Friend user = new Friend();
//                     /*--*/
//                            String name = (String) userMap.get("name");
//                     /*--*/
//                            //user.name = (String) userMap.get("name");
//                            user.email = (String) userMap.get("email");
//                            user.avata = (String) userMap.get("avata");
//                            user.id = id;
//                            user.idRoom = id.compareTo(StaticConfig.UID) > 0 ? (StaticConfig.UID + id).hashCode() + "" : "" + (id + StaticConfig.UID).hashCode();
//
//                            Intent intent = new Intent(v.getContext(), SingleChatActivityservicechat.class);
//                            ArrayList<CharSequence> idFriend = new ArrayList<CharSequence>();
//                            idFriend.add(user.id);
//                    /*--*/
//                            //  intent.putExtra("AllCharter",AllCharterId);
//                            // intent.putExtra("email",email);
//                    /*---*/
//                            intent.putCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID, idFriend);
//                            intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID, user.idRoom);
//                            intent.putExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND, name);
//                            intent.putExtra("service_pro_pic", allServiceDetails.getProfile_pic());
//                            intent.putExtra("service_name", allServiceDetails.getFname() + " " + allServiceDetails.getLname());
//                            intent.putExtra("service_fireapi", allServiceDetails.getFirebase_api());
//                            intent.putExtra("category", cat);
//
//                            mapMark.put(user.id, null);
//                            v.getContext().startActivity(intent);
//                            //  _activity.startActivityForResult(intent, Blue_Charter_Details.ACTION_START_CHAT);
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });

                Intent intent = new Intent(v.getContext(), ChatMessagingActivity.class);
                intent.putExtra("receiver_id", allServiceDetails.getFirebase_reg());
                intent.putExtra("email", allServiceDetails.getEmail());
                intent.putExtra("image", allServiceDetails.getProfile_pic());
                intent.putExtra("name", allServiceDetails.getFname() + " " + allServiceDetails.getLname());

                v.getContext().startActivity(intent);

            }
        });

        if (Integer.valueOf(allServiceDetails.getIs_editable()) > 0) {
            holder.linearCourseChat.setVisibility(View.GONE);
            holder.linearCourseEdit.setVisibility(View.VISIBLE);
        } else {
            holder.linearCourseEdit.setVisibility(View.GONE);
            holder.linearCourseChat.setVisibility(View.VISIBLE);
        }

        holder.bmcedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ActivityAddYourServicesEdit2.class);
                i.putExtra("AllService", allServiceDetails.getId());
                // i.putExtra("category", category);

                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return BCCList == null ? 0 : BCCList.size();
    }

    public void updateAllService(ArrayList<AllServiceDetails> details) {
        BCCList = details;
        this.notifyDataSetChanged();
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

        public TextView bmsname, service_offred, contact, mail, holiday1, holiday11, holiday2, holiday22;
        public ImageView bmspic, flag;
        public RatingBar ratingBar;
        public LinearLayout ll_contact, ll_mail;
        public Button bmsdet, contact_me, report_flag, bmcedt, chatBtn;

        //      public Button bccDt,chatBtn;
        public LinearLayout linearCourseChat, linearCourseEdit;

        public ViewHolder(View v) {
            super(v);

            ll_contact = (LinearLayout) v.findViewById(R.id.ll_contact);
            ll_mail = (LinearLayout) v.findViewById(R.id.ll_mail);

            bmspic = (ImageView) v.findViewById(R.id.bmspic);
            report_flag = (Button) v.findViewById(R.id.report_flag);
            bmsname = (MyTextView) v.findViewById(R.id.bmsname);
            bmcedt = (Button) v.findViewById(R.id.bmcedt);

            contact = (MyTextView) v.findViewById(R.id.contact);
            service_offred = (MyTextView) v.findViewById(R.id.service_offred);
            mail = (MyTextView) v.findViewById(R.id.mail);
            ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
            bmsdet = (Button) v.findViewById(R.id.bmsdet);
            flag = (ImageView) v.findViewById(R.id.flag);
            chatBtn = (Button) v.findViewById(R.id.bmsmsg);
            linearCourseChat = (LinearLayout) v.findViewById(R.id.linearCourseChat);
            linearCourseEdit = (LinearLayout) v.findViewById(R.id.linearCourseEdit);

        }
    }
}
