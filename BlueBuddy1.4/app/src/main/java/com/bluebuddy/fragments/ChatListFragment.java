package com.bluebuddy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluebuddy.R;
import com.bluebuddy.adapters.ChatListAdapter;
import com.bluebuddy.models.MessageDetails;
import com.bluebuddy.models.UserChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ChatListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "ChatListFragment";
    ChatListAdapter adapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String token;
    private LovelyProgressDialog dialogFindAllFriend;
    private ArrayList<UserChat> userChatArrayList;
    private RecyclerView recyclerChatList;

    public ChatListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ChatListFragment newInstance(String token) {
        ChatListFragment fragment = new ChatListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, token);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.token = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_chat_list, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerChatList = (RecyclerView) layout.findViewById(R.id.recyclerChatList);

        recyclerChatList.setLayoutManager(linearLayoutManager);
        // mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeRefreshLayout);
        //mSwipeRefreshLayout.setOnRefreshListener(this);

        userChatArrayList = new ArrayList<>();

        adapter = new ChatListAdapter(getContext(), userChatArrayList, token);

        recyclerChatList.setAdapter(adapter);

        dialogFindAllFriend = new LovelyProgressDialog(getContext());
        dialogFindAllFriend.setCancelable(false)
                .setIcon(R.drawable.ic_add)
                .setTitle("Get all chat data....")
                .setTopColorRes(R.color.colorPrimary)
                .show();

        getListFriendUId();
        // Inflate the layout for this fragment
        return layout;
    }

    private void getListFriendUId() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUserId = user.getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("conversations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getKey() != null && dataSnapshot.getValue() != null) {

                    HashMap userId = (HashMap) dataSnapshot.getValue();
                    Iterator listKey = userId.keySet().iterator();

                    while (listKey.hasNext()) {

                        UserChat userChat = null;
                        String key = listKey.next().toString();

                        Log.w(TAG, "onDataChange: ChatList" + key);
                        HashMap userData = (HashMap) userId.get(key);
                        String messageLocation = userData.get("location").toString().trim();

                        userChat = new UserChat(key, "", "", "", messageLocation, "", null);

                        getUserDetails(userChat);

                    }
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

    private void getUserDetails(final UserChat userChat) {

        FirebaseDatabase.getInstance().getReference().child("users").child(userChat.getUser_id()).child("credentials").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    HashMap userDetails = (HashMap) dataSnapshot.getValue();

                    try {
                        userChat.setName(userDetails.get("name").toString());
                        userChat.setEmail(userDetails.get("email").toString());
                        userChat.setImage(userDetails.get("image").toString());

                        getUserLastMessage(userChat);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

    private void getUserLastMessage(final UserChat userChat) {

        FirebaseDatabase.getInstance().getReference().child("conversations").child(userChat.getMessageLocation()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    String lastMessage = null;
                    Long lastMessageTime = null;
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        MessageDetails note = noteDataSnapshot.getValue(MessageDetails.class);
                        lastMessage = note.getContent();
                        lastMessageTime = (Long) note.getTimestamp();
                        Log.d(TAG, "onDataChange: Chat " + note.getContent());
                    }

                    userChat.setLastMessage(lastMessage);
                    userChat.setLastMessageTime(lastMessageTime);

                    boolean status = true;
                    int position = -1;

                    for (UserChat userChat1 : userChatArrayList) {
                        if (userChat1.getEmail().equalsIgnoreCase(userChat.getEmail())) {
                            status = false;
                            position = userChatArrayList.indexOf(userChat1);
                        }
                    }

                    if (!status && position != -1) {
                        userChatArrayList.remove(position);
                        userChatArrayList.add(userChat);
                    } else {
                        userChatArrayList.add(userChat);
                    }

                } else {

                }
                dialogFindAllFriend.dismiss();

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

}
