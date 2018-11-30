package com.bluebuddy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.bluebuddy.activities.BlueMarketCoursePicEditActivity;
import com.bluebuddy.activities.ChatMessagingActivity;
import com.bluebuddy.activities.CourseDetailsActivitynewlayall;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.data.FriendDB;
import com.bluebuddy.data.StaticConfig;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllCourseDetails;
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


public class BlueMarketCoursesAdapter extends RecyclerView.Adapter<BlueMarketCoursesAdapter.ViewHolder> {

    public static Map<String, Boolean> mapMark;
    private List<AllCourseDetails> BMCList;
    private Activity _activity;
    private Context _context;
    private String token, category, curru;
    private ArrayList<String> listFriendID = null;
    private ListFriend dataListFriend = null;

    public BlueMarketCoursesAdapter() {

    }

    public BlueMarketCoursesAdapter(Activity a, Context c, List<AllCourseDetails> BMCList, String token, String category, String curru) {

        this._activity = a;
        this._context = c;
        this.BMCList = BMCList;
        this.token = token;
        this.category = category;
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
    public BlueMarketCoursesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Creating a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blue_market_card, parent, false);

        //set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        mapMark = new HashMap<>();

        return vh;

    }

    @Override
    public void onBindViewHolder(final BlueMarketCoursesAdapter.ViewHolder holder, int position) {

        final AllCourseDetails allCourseDetails = BMCList.get(position);
        //  holder.Bmcname.setText(String.valueOf(allCourseDetails.getCategory()));
        holder.Bmcprice.setText("$" + allCourseDetails.getPrice());
        holder.Bmcloc.setText(allCourseDetails.getLocation());
        holder.Bmcposted.setText(allCourseDetails.getFname() + " " + allCourseDetails.getLname());
        final ImageView imageView = holder.Bmcpic;

        Glide.with(imageView.getContext()).asBitmap().load(IMG_URL + allCourseDetails.getImage()).into(new BitmapImageViewTarget(holder.Bmcpic));

        //vvp

        if (allCourseDetails.getCategory().contains(":")) {
            String[] separated = allCourseDetails.getCategory().split(":");
            // separated[0];
            // separated[1];
            separated[1] = separated[1].trim();
            holder.Bmcname.setText(separated[1]);
        } else if (!allCourseDetails.getCategory().contains(":")) {
            //   holder.tvevtName.setText(allEventsDetails.getActivity());
            holder.Bmcname.setText(String.valueOf(allCourseDetails.getCategory()));
        }

        if (allCourseDetails.getFeatured().equals("1")) {
            holder.feattag.setVisibility(View.VISIBLE);
        } else if (allCourseDetails.getFeatured().equals("0")) {
            holder.feattag.setVisibility(View.GONE);
        }

        holder.bmcDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CourseDetailsActivitynewlayall.class);
                intent.putExtra("AllCourse", String.valueOf(allCourseDetails.getId()));
                intent.putExtra("AllCourse_Firebaseid", allCourseDetails.getFirebase_id());
                intent.putExtra("AllCourse_FirebaseEmail", allCourseDetails.getEmail());
                intent.putExtra("course_pro_pic", allCourseDetails.getProfile_pic());
                intent.putExtra("course_name", allCourseDetails.getFname() + " " + allCourseDetails.getLname());
                intent.putExtra("course_fireapi", allCourseDetails.getFirebase_api());

                intent.putExtra("category", category);
                intent.putExtra("pass", 1);
                v.getContext().startActivity(intent);
            }
        });
        holder.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
//                findIDEmail1(allCourseDetails.getEmail());
//                FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(allCourseDetails.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
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
//                            Intent intent = new Intent(v.getContext(), SingleChatActivitycoursechat.class);
//                            ArrayList<CharSequence> idFriend = new ArrayList<CharSequence>();
//                            idFriend.add(user.id);
//                    /*--*/
//                            //  intent.putExtra("AllCharter",AllCharterId);
//                            // intent.putExtra("email",email);
//                    /*---*/
//                            intent.putCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID, idFriend);
//                            intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID, user.idRoom);
//                            intent.putExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND, name);
//                            intent.putExtra("category", category);
//                            intent.putExtra("course_pro_pic", allCourseDetails.getProfile_pic());
//                            intent.putExtra("course_name", allCourseDetails.getFname() + " " + allCourseDetails.getLname());
//                            intent.putExtra("course_fireapi", allCourseDetails.getFirebase_api());
//                            //  mapMark.put(user.id, null);
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
                intent.putExtra("receiver_id", allCourseDetails.getFirebase_id());
                intent.putExtra("email", allCourseDetails.getEmail());
                intent.putExtra("image", allCourseDetails.getProfile_pic());
                intent.putExtra("name", allCourseDetails.getFname() + " " + allCourseDetails.getLname());

                v.getContext().startActivity(intent);

            }
        });

        if (Integer.valueOf(allCourseDetails.getIs_editable()) > 0) {
            holder.linearCourseChat.setVisibility(View.GONE);
            holder.linearCourseEdit.setVisibility(View.VISIBLE);
        } else {
            holder.linearCourseEdit.setVisibility(View.GONE);
            holder.linearCourseChat.setVisibility(View.VISIBLE);
        }

        holder.bmcedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), BlueMarketCoursePicEditActivity.class);
                // Intent i = new Intent(_activity, CourseEditActivity.class);
                i.putExtra("COURSE_ID", allCourseDetails.getId());
                i.putExtra("category", category);
                v.getContext().startActivity(i);
            }
        });
        if (allCourseDetails.getUser_id().equals(curru)) {
            holder.flag.setVisibility(View.GONE);
        } else if (!allCourseDetails.getUser_id().equals(curru)) {
            if (allCourseDetails.getIs_flagged().equals("0")) {
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
                                Call<FlagResponse> userCall = service.fr("Bearer " + token, "post", "course", allCourseDetails.getId());

                                userCall.enqueue(new Callback<FlagResponse>() {
                                    @Override
                                    public void onResponse(Call<FlagResponse> call, Response<FlagResponse> response) {
                                        if (response.body().getStatus() == "true") {
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
            } else if (allCourseDetails.getIs_flagged().equals("1")) {
                holder.flag.setVisibility(View.VISIBLE);
                holder.flag.setImageResource(R.drawable.ic_flagred);
            }
        }
    }

    @Override
    public int getItemCount() {
        return BMCList == null ? 0 : BMCList.size();
    }

    public void updateAllCourses(ArrayList<AllCourseDetails> details) {
        BMCList = details;
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

        //each data item is just a string in this case
        public CardView cardView;
        public TextView Bmcname, Bmcprice, Bmcloc, Bmcposted;
        public ImageView Bmcpic, flag, feattag;
        public Button bmcDt, bmcedt, chatBtn;
        public LinearLayout linearCourseChat, linearCourseEdit;

        public ViewHolder(View v) {
            super(v);
            feattag = (ImageView) v.findViewById(R.id.feattag);
            flag = (ImageView) v.findViewById(R.id.report_flag);
            cardView = (CardView) v.findViewById(R.id.card_view_bm);
            Bmcname = (TextView) v.findViewById(R.id.bmcname);
            Bmcprice = (TextView) v.findViewById(R.id.bmcprice);
            Bmcloc = (TextView) v.findViewById(R.id.bmcloc);
            Bmcposted = (TextView) v.findViewById(R.id.bmcposted);
            Bmcpic = (ImageView) v.findViewById(R.id.bmcpic);

            bmcDt = (Button) v.findViewById(R.id.bmcdet);
            bmcedt = (Button) v.findViewById(R.id.bmcedt);
            chatBtn = (Button) v.findViewById(R.id.chatBtn);

            linearCourseChat = (LinearLayout) v.findViewById(R.id.linearCourseChat);
            linearCourseEdit = (LinearLayout) v.findViewById(R.id.linearCourseEdit);
        }

    }
}
