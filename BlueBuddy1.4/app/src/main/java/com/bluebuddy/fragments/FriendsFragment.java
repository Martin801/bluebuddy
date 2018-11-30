package com.bluebuddy.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.bluebuddy.R;
import com.bluebuddy.Utilities.Utility;
import com.bluebuddy.activities.ChatMessagingActivity;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.data.FriendDB;
import com.bluebuddy.data.StaticConfig;
import com.bluebuddy.models.Friend;
import com.bluebuddy.models.ListFriend;
import com.bluebuddy.models.UserChat;
import com.bluebuddy.services.ServiceUtils;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsFragment extends Fragment {

    public static final String TAG = "FriendsFragment";
    public static final String ACTION_DELETE_FRIEND = "com.android.rivchat.DELETE_FRIEND";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static int ACTION_START_CHAT = 1;
    public FragFriendClickFloatButton onClickFloatButton;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    /* implements SwipeRefreshLayout.OnRefreshListener */
    private RecyclerView recyclerListFrends;
    private ListFriendsAdapter adapter;
    private ListFriend dataListFriend = null;
    private ArrayList<String> listFriendID = null;
    private LovelyProgressDialog dialogFindAllFriend;
    //private SwipeRefreshLayout mSwipeRefreshLayout;
    private CountDownTimer detectFriendOnline;
    private String token;
    private ArrayList<UserChat> userChatArrayList;

    //private BroadcastReceiver deleteFriendReceiver;

    public FriendsFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static FriendsFragment newInstance(String token) {

        FriendsFragment fragment = new FriendsFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, token);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onClickFloatButton = new FragFriendClickFloatButton();
        if (getArguments() != null) {
            this.token = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        detectFriendOnline = new CountDownTimer(System.currentTimeMillis(), StaticConfig.TIME_TO_REFRESH) {
            @Override
            public void onTick(long l) {
                ServiceUtils.updateFriendStatus(getContext(), dataListFriend);
                ServiceUtils.updateUserStatus(getContext());
            }

            @Override
            public void onFinish() {
            }
        };

        if (dataListFriend == null) {
            dataListFriend = FriendDB.getInstance(getContext()).getListFriend();
            if (dataListFriend.getListFriend().size() > 0) {

                listFriendID = new ArrayList<>();
                for (Friend friend : dataListFriend.getListFriend()) {
                    listFriendID.add(friend.id);
                }
                detectFriendOnline.start();

            }
        }

        View layout = inflater.inflate(R.layout.fragment_people, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerListFrends = (RecyclerView) layout.findViewById(R.id.recycleListFriend);
        recyclerListFrends.setLayoutManager(linearLayoutManager);
        // mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeRefreshLayout);
        //mSwipeRefreshLayout.setOnRefreshListener(this);

        userChatArrayList = new ArrayList<>();
        adapter = new ListFriendsAdapter(getContext(), userChatArrayList, this, token);

        recyclerListFrends.setAdapter(adapter);

        dialogFindAllFriend = new LovelyProgressDialog(getContext());

        if (listFriendID == null) {

            listFriendID = new ArrayList<>();
            dialogFindAllFriend.setCancelable(false)
                    .setIcon(R.drawable.ic_add)
                    .setTitle("Get all friend....")
                    .setTopColorRes(R.color.colorPrimary)
                    .show();

            getListFriendUId();

        }

      /*  deleteFriendReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String idDeleted = intent.getExtras().getString("idFriend");
                for (Friend friend : dataListFriend.getListFriend()) {
                    if (idDeleted.equals(friend.id)) {
                        ArrayList<Friend> friends = dataListFriend.getListFriend();
                        friends.remove(friend);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        };*/

        /*IntentFilter intentFilter = new IntentFilter(ACTION_DELETE_FRIEND);
        getContext().registerReceiver(deleteFriendReceiver, intentFilter);
*/
        return layout;
    }
  /*  @Override
    public void onStop()
    {
        getContext().unregisterReceiver(deleteFriendReceiver);
        super.onStop();
    }


    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(deleteFriendReceiver);
    }

   @Override
    public void onDestroyView() {
        super.onDestroyView();
        getContext().unregisterReceiver(deleteFriendReceiver);
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ACTION_START_CHAT == requestCode && data != null && ListFriendsAdapter.mapMark != null) {
            ListFriendsAdapter.mapMark.put(data.getStringExtra("idFriend"), false);
        }
    }

 /*   @Override
    public void onRefresh() {
        listFriendID.clear();
        dataListFriend.getListFriend().clear();
        adapter.notifyDataSetChanged();
        FriendDB.getInstance(getContext()).dropDB();
        detectFriendOnline.cancel();
        getListFriendUId();
    }*/

    private void getListFriendUId() {

//        FirebaseDatabase.getInstance().getReference().child("friend/" + StaticConfig.UID).addListenerForSingleValueEvent(new ValueEventListener() {


//        FirebaseDatabase.getInstance().getReference().child("friend/").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getValue() != null) {
//                    HashMap mapRecord = (HashMap) dataSnapshot.getValue();
//                    Iterator listKey = mapRecord.keySet().iterator();
//                    while (listKey.hasNext()) {
//                        String key = listKey.next().toString();
//                        listFriendID.add(mapRecord.get(key).toString());
//
//                        Log.d("FriendsFragment", "onDataChange: " + listFriendID.toString());
//                    }
//                    getAllFriendInfo(0);
//                } else {
//                    dialogFindAllFriend.dismiss();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String activeUserId = user.getUid();
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getKey() != null && dataSnapshot.getValue() != null) {

                    HashMap userId = (HashMap) dataSnapshot.getValue();
                    Iterator listKey = userId.keySet().iterator();

                    while (listKey.hasNext()) {

                        UserChat userChat = null;
                        String key = listKey.next().toString();
                        if (!activeUserId.equalsIgnoreCase(key)) {
                            HashMap userData = (HashMap) userId.get(key);
                            Iterator listDataKey = userData.keySet().iterator();

                            while (listDataKey.hasNext()) {
                                String dataKey = listDataKey.next().toString();

                                if (dataKey.equalsIgnoreCase("credentials")) {
                                    HashMap userCredentials = (HashMap) userData.get(dataKey);

                                    Log.d(TAG, "onDataChange: " + userCredentials);

                                    try {
                                        userChat = new UserChat(key, userCredentials.get("name").toString(), userCredentials.get("email").toString(), userCredentials.get("image").toString(), "", "", null);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            userChatArrayList.add(userChat);

                            Log.w(TAG, "onDataChange: " + key);
                        }
//                        listFriendID.add(mapRecord.get(key).toString());

//                        Log.d("FriendsFragment", "onDataChange: " + listFriendID.toString());
                    }
//                    getAllFriendInfo(0);

                    adapter.notifyDataSetChanged();
                    dialogFindAllFriend.dismiss();

                } else {
                    dialogFindAllFriend.dismiss();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    private void getAllFriendInfo(final int index) {
        if (index == listFriendID.size()) {
            //save list friend
            adapter.notifyDataSetChanged();
            dialogFindAllFriend.dismiss();
            //   mSwipeRefreshLayout.setRefreshing(false);
            detectFriendOnline.start();
        } else {
            final String id = listFriendID.get(index);
            FirebaseDatabase.getInstance().getReference().child("user/" + id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {
                        Friend user = new Friend();
                        HashMap mapUserInfo = (HashMap) dataSnapshot.getValue();
                        user.name = (String) mapUserInfo.get("name");
                        user.email = (String) mapUserInfo.get("email");
                        user.avata = (String) mapUserInfo.get("avata");
                        user.id = id;
                        user.idRoom = id.compareTo(StaticConfig.UID) > 0 ? (StaticConfig.UID + id).hashCode() + "" : "" + (id + StaticConfig.UID).hashCode();
                        dataListFriend.getListFriend().add(user);
                        FriendDB.getInstance(getContext()).addFriend(user);
                    }

                    getAllFriendInfo(index + 1);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public class FragFriendClickFloatButton implements View.OnClickListener {
        Context context;
        LovelyProgressDialog dialogWait;

        public FragFriendClickFloatButton() {
        }

        public FragFriendClickFloatButton getInstance(Context context) {
            this.context = context;
            dialogWait = new LovelyProgressDialog(context);
            return this;
        }

        @Override
        public void onClick(final View view) {
            new LovelyTextInputDialog(view.getContext(), R.style.EditTextTintTheme)
                    .setTopColorRes(R.color.colorPrimary)
                    .setTitle("Add friend")
                    .setMessage("Enter friend email")
                    .setIcon(R.drawable.ic_add)
                    .setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                    .setInputFilter("Email not found", new LovelyTextInputDialog.TextFilter() {
                        @Override
                        public boolean check(String text) {
                            Pattern VALID_EMAIL_ADDRESS_REGEX =
                                    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
                            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(text);
                            return matcher.find();
                        }
                    })
                    .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                        @Override
                        public void onTextInputConfirmed(String text) {
                            //Tim id user id
                            findIDEmail(text);
                            //Check xem da ton tai ban ghi friend chua
                            //Ghi them 1 ban ghi
                        }
                    })
                    .show();
        }

        /**
         * TIm id cua email tren server
         *
         * @param email
         */
        private void findIDEmail(String email) {

            dialogWait.setCancelable(false)
                    .setIcon(R.drawable.ic_add)
                    .setTitle("Finding friend....")
                    .setTopColorRes(R.color.colorPrimary)
                    .show();

            FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dialogWait.dismiss();
                    if (dataSnapshot.getValue() == null) {
                        //email not found
                        new LovelyInfoDialog(context)
                                .setTopColorRes(R.color.colorAccent)
                                .setIcon(R.drawable.ic_add)
                                .setTitle("Fail")
                                .setMessage("Email not found")
                                .show();
                    } else {
                        String id = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();
                        if (id.equals(StaticConfig.UID)) {
                            new LovelyInfoDialog(context)
                                    .setTopColorRes(R.color.colorAccent)
                                    .setIcon(R.drawable.ic_add)
                                    .setTitle("Fail")
                                    .setMessage("Email not valid")
                                    .show();
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

        /**
         * Lay danh sach friend cua một UID
         */

        private void checkBeforAddFriend(final String idFriend, Friend userInfo) {
            dialogWait.setCancelable(false)
                    .setIcon(R.drawable.ic_add)
                    .setTitle("Add friend....")
                    .setTopColorRes(R.color.colorPrimary)
                    .show();

            //Check xem da ton tai id trong danh sach id chua
            if (listFriendID.contains(idFriend)) {
                dialogWait.dismiss();
                new LovelyInfoDialog(context)
                        .setTopColorRes(R.color.colorPrimary)
                        .setIcon(R.drawable.ic_add)
                        .setTitle("Friend")
                        .setMessage("User " + userInfo.email + " has been friend")
                        .show();
            } else {
                addFriend(idFriend, true);
                listFriendID.add(idFriend);
                dataListFriend.getListFriend().add(userInfo);
                FriendDB.getInstance(getContext()).addFriend(userInfo);
                adapter.notifyDataSetChanged();
            }
        }

        /**
         * Add friend
         *
         * @param idFriend
         */
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
                                    dialogWait.dismiss();
                                    new LovelyInfoDialog(context)
                                            .setTopColorRes(R.color.colorAccent)
                                            .setIcon(R.drawable.ic_add)
                                            .setTitle("False")
                                            .setMessage("False to add friend success")
                                            .show();
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
                                    dialogWait.dismiss();
                                    new LovelyInfoDialog(context)
                                            .setTopColorRes(R.color.colorAccent)
                                            .setIcon(R.drawable.ic_add)
                                            .setTitle("False")
                                            .setMessage("False to add friend success")
                                            .show();
                                }
                            });
                }
            } else {
                dialogWait.dismiss();
                new LovelyInfoDialog(context)
                        .setTopColorRes(R.color.colorPrimary)
                        .setIcon(R.drawable.ic_add)
                        .setTitle("Success")
                        .setMessage("Add friend success")
                        .show();
            }
        }

    }

}

class ListFriendsAdapter extends RecyclerView.Adapter<ListFriendsAdapter.ViewHolder> {

    public static final String TAG = "ListFriendsAdapter";
    public static Map<String, Query> mapQuery;
    public static Map<String, DatabaseReference> mapQueryOnline;
    public static Map<String, ChildEventListener> mapChildListener;
    public static Map<String, ChildEventListener> mapChildListenerOnline;
    public static Map<String, Boolean> mapMark;
    LovelyProgressDialog dialogWaitDeleting;
    //    private ListFriend listFriend;
    private Context context;
    private String token;
    private FriendsFragment fragment;
    private ArrayList<UserChat> userChatArrayList;

    //    public ListFriendsAdapter(Context context, ListFriend listFriend, FriendsFragment fragment, String token) {
    public ListFriendsAdapter(Context context, ArrayList<UserChat> userChatArrayList, FriendsFragment fragment, String token) {

//        this.listFriend = listFriend;
        this.userChatArrayList = userChatArrayList;
        this.context = context;
        this.token = token;
        mapQuery = new HashMap<>();
        mapChildListener = new HashMap<>();
        mapMark = new HashMap<>();
        mapChildListenerOnline = new HashMap<>();
        mapQueryOnline = new HashMap<>();
        this.fragment = fragment;
        dialogWaitDeleting = new LovelyProgressDialog(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rc_item_friend, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        UserChat userChat = userChatArrayList.get(position);

        final String name = Utility.makeFirstLetterCap(userChat.getName().trim());
        final String email = userChat.getEmail().trim();
        final String id = userChat.getUser_id();
        final String image = userChat.getImage().trim();
//        final String idRoom = listFriend.getListFriend().get(position).idRoom;

//        final String avata = listFriend.getListFriend().get(position).avata;
        final CircleImageView img = holder.avata;
        if (email == null || email.matches("")) {
            return;
        }

        holder.txtName.setText(name);
        holder.txtMessage.setVisibility(View.GONE);

        Log.d(TAG, "onBindViewHolder: " + image);

        holder.nameletter.setText(String.valueOf(name.charAt(0)));
        holder.nameletter.setBackgroundResource(R.drawable.badge1);
        holder.avata.setVisibility(View.GONE);

//        if (image.trim().equalsIgnoreCase("default")) {
//            holder.nameletter.setText(String.valueOf(name.charAt(0)));
//            holder.nameletter.setBackgroundResource(R.drawable.badge1);
//            holder.avata.setVisibility(View.GONE);
//        } else {
//            holder.avata.setVisibility(View.VISIBLE);
//            holder.nameletter.setVisibility(View.GONE);
//            Glide.with(context).load(image).override(300, 300).into(holder.avata);
//        }

        holder.chatItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatMessagingActivity.class);
                intent.putExtra("receiver_id", id);
                intent.putExtra("email", email);
                intent.putExtra("image", image);
                intent.putExtra("name", name);
                context.startActivity(intent);
            }
        });

      /*  BuddyActivity b=new BuddyActivity();
        SharedPreferences preferences;
        preferences=b.getPref();
        final String token=preferences.getString(ACCESS_TOKEN, "");*/

//        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
//        Call<AllChatHistory> userCall = service.ch("Bearer " + token, email);
//
//        userCall.enqueue(new Callback<AllChatHistory>() {
//            @Override
//            public void onResponse(Call<AllChatHistory> call, Response<AllChatHistory> response) {
//                if (!response.body().isStatus() == false) {
//                    ArrayList<AllChatHisDetails> CHATList = response.body().getResponse();
//                    final AllChatHisDetails allChatHisDetails = CHATList.get(0);
//                    if (allChatHisDetails != null) {
//                        holder.txtName.setText(allChatHisDetails.getFname() + " " + allChatHisDetails.getLname());
//
//
///*if(allChatHisDetails.getFname().charAt(0)=='H' || allChatHisDetails.getFname().charAt(0)=='h'){
//    holder.avata.setImageResource(R.drawable.ic_add);
//}*/
//
//                        holder.nameletter.setText(String.valueOf(allChatHisDetails.getFname().charAt(0)));
//
//                        holder.nameletter.setBackgroundResource(R.drawable.badge1);
////holder.nameletter.setBackground(R.drawable.badge1);
//   /* Glide.with(img.getContext()).load(IMG_URL + allChatHisDetails.getProfile_img()).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.avata) {
//        @Override
//        protected void setResource(Bitmap resource) {
//            RoundedBitmapDrawable circularBitmapDrawable =
//                    RoundedBitmapDrawableFactory.create(img.getContext().getResources(), resource);
//            circularBitmapDrawable.setCircular(true);
//holder.avata.setImageDrawable(circularBitmapDrawable);
//        }
//    });*/
//
//                        ((View) holder.txtName.getParent().getParent().getParent())
//                                .setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//
//                                        holder.txtMessage.setTypeface(Typeface.DEFAULT);
//                                        holder.txtName.setTypeface(Typeface.DEFAULT);
//                                        Intent intent = new Intent(context, ChatActivity2.class);
//                                        intent.putExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND, name);
//                                        intent.putExtra("profile_pic", allChatHisDetails.getProfile_img());
//                                        intent.putExtra("access_token", allChatHisDetails.getFirebase_API_key());
//                                        intent.putExtra("name", allChatHisDetails.getFname() + " " + allChatHisDetails.getLname());
//                                        ArrayList<CharSequence> idFriend = new ArrayList<CharSequence>();
//                                        idFriend.add(id);
//                                        intent.putCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID, idFriend);
////                                        intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID, idRoom);
//                                        ChatActivity2.bitmapAvataFriend = new HashMap<>();
//                                        mapMark.put(id, null);
//                                        fragment.startActivityForResult(intent, FriendsFragment.ACTION_START_CHAT);
//
//                                    }
//                                });
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<AllChatHistory> call, Throwable t) {
//            }
//        });

        //nhấn giữ để xóa bạn
//        ((View)  holder.txtName.getParent().getParent().getParent())
//                .setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View view) {
//                        String friendName = (String) holder.txtName.getText();
//
//                        new AlertDialog.Builder(context)
//                                .setTitle("Delete Friend")
//                                .setMessage("Are you sure want to delete " + friendName + "?")
//                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        dialogInterface.dismiss();
//                                        final String idFriendRemoval = listFriend.getListFriend().get(position).id;
//                                        dialogWaitDeleting.setTitle("Deleting...")
//                                                .setCancelable(false)
//                                                .setTopColorRes(R.color.colorAccent)
//                                                .show();
//                                        deleteFriend(idFriendRemoval);
//                                    }
//                                })
//                                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        dialogInterface.dismiss();
//                                    }
//                                }).show();
//
//                        return true;
//                    }
//                });

        /*if (listFriend.getListFriend().get(position).message.text.length() > 0) {

            holder.txtMessage.setVisibility(View.VISIBLE);
            holder.txtTime.setVisibility(View.VISIBLE);

            if (!listFriend.getListFriend().get(position).message.text.startsWith(id)) {

                holder.txtMessage.setText(listFriend.getListFriend().get(position).message.text);
                holder.txtMessage.setTypeface(Typeface.DEFAULT);
                holder.txtName.setTypeface(Typeface.DEFAULT);

            } else {

                holder.txtMessage.setText(listFriend.getListFriend().get(position).message.text.substring((id + "").length()));
                holder.txtMessage.setTypeface(Typeface.DEFAULT_BOLD);
                holder.txtName.setTypeface(Typeface.DEFAULT_BOLD);

            }

            String time = new SimpleDateFormat("EEE, d MMM yyyy").format(new Date(listFriend.getListFriend().get(position).message.timestamp));
            String today = new SimpleDateFormat("EEE, d MMM yyyy").format(new Date(System.currentTimeMillis()));

            if (today.equals(time)) {

                holder.txtTime.setText(new SimpleDateFormat("HH:mm").format(new Date(listFriend.getListFriend().get(position).message.timestamp)));

            } else {

                holder.txtTime.setText(new SimpleDateFormat("MMM d").format(new Date(listFriend.getListFriend().get(position).message.timestamp)));

            }
        } else {

            holder.txtMessage.setVisibility(View.GONE);
            holder.txtTime.setVisibility(View.GONE);

            if (mapQuery.get(id) == null && mapChildListener.get(id) == null) {

                mapQuery.put(id, FirebaseDatabase.getInstance().getReference().child("message/" + idRoom).limitToLast(1));

                mapChildListener.put(id, new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        //  if(listFriend!=null && listFriend.equals("")){
                        HashMap mapMessage = (HashMap) dataSnapshot.getValue();
                        if (mapMark.get(id) != null) {
                            *//*&& mapMark.get(id).equals("")*//*
                            if (!mapMark.get(id)) {
                                listFriend.getListFriend().get(position).message.text = id + mapMessage.get("text");
                            } else if (mapMark.get(id)) {
                                listFriend.getListFriend().get(position).message.text = (String) mapMessage.get("text");
                            }
                            notifyDataSetChanged();
                            mapMark.put(id, false);
                        } else {
                            listFriend.getListFriend().get(position).message.text = (String) mapMessage.get("text");
                            notifyDataSetChanged();
                        }
                        listFriend.getListFriend().get(position).message.timestamp = (long) mapMessage.get("timestamp");
                        //}
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                mapQuery.get(id).addChildEventListener(mapChildListener.get(id));
                mapMark.put(id, true);

            } else {

                mapQuery.get(id).removeEventListener(mapChildListener.get(id));
                mapQuery.get(id).addChildEventListener(mapChildListener.get(id));
                mapMark.put(id, true);

            }
        }*/



       /* if (listFriend.getListFriend().get(position).avata.equals(StaticConfig.STR_DEFAULT_BASE64)) {
            ((ItemFriendViewHolder) holder).avata.setImageResource(R.drawable.default_avata);
        } else {
            byte[] decodedString = Base64.decode(listFriend.getListFriend().get(position).avata, Base64.DEFAULT);
            Bitmap src = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ((ItemFriendViewHolder) holder).avata.setImageBitmap(src);
        }*/

//        if (mapQueryOnline.get(id) == null && mapChildListenerOnline.get(id) == null) {
//            mapQueryOnline.put(id, FirebaseDatabase.getInstance().getReference().child("user/" + id + "/status"));
//            mapChildListenerOnline.put(id, new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    if (dataSnapshot.getValue() != null && dataSnapshot.getKey().equals("isOnline")) {
//                        Log.d("FriendsFragment add " + id, (boolean) dataSnapshot.getValue() + "");
//                        // listFriend.getListFriend().get(position).status.isOnline = (boolean) dataSnapshot.getValue();
//                        notifyDataSetChanged();
//                    }
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                    if (dataSnapshot.getValue() != null && dataSnapshot.getKey().equals("isOnline")) {
//                        Log.d("FriendsFragment change " + id, (boolean) dataSnapshot.getValue() + "");
//                        // listFriend.getListFriend().get(position).status.isOnline = (boolean) dataSnapshot.getValue();
//                        notifyDataSetChanged();
//                    }
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//            mapQueryOnline.get(id).addChildEventListener(mapChildListenerOnline.get(id));
//        }

//        if (listFriend.getListFriend().get(position).status.isOnline) {
//            holder.avata.setMaxWidth(10);
//        } else {
//            holder.avata.setMaxWidth(0);
//        }

    }

    @Override
    public int getItemCount() {
//        Integer ss = listFriend.getListFriend().size();
//        Log.d("size", String.valueOf(ss));
        //  return listFriend.getListFriend().size();
        return userChatArrayList == null ? 0 : userChatArrayList.size();
        /*return EVENTList == null ? 0 : EVENTList.size();*/

        //  return listFriend.getListFriend() != null ? listFriend.getListFriend().size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //each data item is just a string in this case
        public CircleImageView avata;
        public MyTextView txtName, txtTime, txtMessage;
        private MyTextView nameletter;
        private Context context;
        private CardView chatItem;

        public ViewHolder(View v) {

            super(v);
            avata = (CircleImageView) itemView.findViewById(R.id.icon_avata);
            txtName = (MyTextView) itemView.findViewById(R.id.txtName);
            txtTime = (MyTextView) itemView.findViewById(R.id.txtTime);
            txtMessage = (MyTextView) itemView.findViewById(R.id.txtMessage);
            nameletter = (MyTextView) itemView.findViewById(R.id.nameletter);
            chatItem = (CardView) itemView.findViewById(R.id.chatItem);
        }
    }

    /**
     * Delete friend
     *
     * @param idFriend
     */
//    private void deleteFriend(final String idFriend) {
//        if (idFriend != null) {
//            FirebaseDatabase.getInstance().getReference().child("friend").child(StaticConfig.UID)
//                    .orderByValue().equalTo(idFriend).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                    if (dataSnapshot.getValue() == null) {
//                        //email not found
//                        dialogWaitDeleting.dismiss();
//                        new LovelyInfoDialog(context)
//                                .setTopColorRes(R.color.colorAccent)
//                                .setTitle("Error")
//                                .setMessage("Error occurred during deleting friend")
//                                .show();
//                    } else {
//                        String idRemoval = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();
//                        FirebaseDatabase.getInstance().getReference().child("friend")
//                                .child(StaticConfig.UID).child(idRemoval).removeValue()
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        dialogWaitDeleting.dismiss();
//
//                                        new LovelyInfoDialog(context)
//                                                .setTopColorRes(R.color.colorAccent)
//                                                .setTitle("Success")
//                                                .setMessage("Friend deleting successfully")
//                                                .show();
//
//                                        Intent intentDeleted = new Intent(FriendsFragment.ACTION_DELETE_FRIEND);
//                                        intentDeleted.putExtra("idFriend", idFriend);
//                                        context.sendBroadcast(intentDeleted);
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        dialogWaitDeleting.dismiss();
//                                        new LovelyInfoDialog(context)
//                                                .setTopColorRes(R.color.colorAccent)
//                                                .setTitle("Error")
//                                                .setMessage("Error occurred during deleting friend")
//                                                .show();
//                                    }
//                                });
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        } else {
//            dialogWaitDeleting.dismiss();
//            new LovelyInfoDialog(context)
//                    .setTopColorRes(R.color.colorPrimary)
//                    .setTitle("Error")
//                    .setMessage("Error occurred during deleting friend")
//                    .show();
//        }
//    }
}

