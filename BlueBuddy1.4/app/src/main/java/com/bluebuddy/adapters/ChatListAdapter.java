package com.bluebuddy.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.bluebuddy.R;
import com.bluebuddy.Utilities.Utility;
import com.bluebuddy.activities.ChatActivity2;
import com.bluebuddy.activities.ChatMessagingActivity;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.models.UserChat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bluebuddy.api.ApiClient.BASE_URL;

/**
 * Created by Aquarious Technology on 1/31/2018.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    public static final String TAG = "ChatListAdapter";
    //    private ListFriend listFriend;
    private Context context;
    private String token;
    public static Map<String, Query> mapQuery;
    public static Map<String, DatabaseReference> mapQueryOnline;
    public static Map<String, ChildEventListener> mapChildListener;
    public static Map<String, ChildEventListener> mapChildListenerOnline;
    public static Map<String, Boolean> mapMark;

    private ArrayList<UserChat> userChatArrayList;

    //    public ListFriendsAdapter(Context context, ListFriend listFriend, FriendsFragment fragment, String token) {
    public ChatListAdapter(Context context, ArrayList<UserChat> userChatArrayList, String token) {

        this.userChatArrayList = userChatArrayList;

        this.context = context;
        this.token = token;
        mapQuery = new HashMap<>();
        mapChildListener = new HashMap<>();
        mapMark = new HashMap<>();
        mapChildListenerOnline = new HashMap<>();
        mapQueryOnline = new HashMap<>();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //each data item is just a string in this case
        public CircleImageView avata;
        public MyTextView txtName, txtTime, txtMessage;
        private MyTextView nameletter;
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

    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rc_item_friend, parent, false);
        ChatListAdapter.ViewHolder vh = new ChatListAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ChatListAdapter.ViewHolder holder, final int position) {

        UserChat userChat = userChatArrayList.get(position);

        final String name = Utility.makeFirstLetterCap(userChat.getName().trim());
        final String email = userChat.getEmail().trim();
        final String id = userChat.getUser_id();
        final String image = userChat.getImage().trim();
        final String lastMessage = userChat.getLastMessage();
        final Long lastMessageTime = userChat.getLastMessageTime();

        holder.txtName.setText(name);

        Log.d(TAG, "onBindViewHolder: " + image);

//        if (image.contains(BASE_URL)) {
//            holder.avata.setVisibility(View.VISIBLE);
//            holder.nameletter.setVisibility(View.GONE);
//            Glide.with(holder.avata.getContext()).load(image).override(300, 300).into(holder.avata);
//        } else {
//            holder.nameletter.setText(String.valueOf(name.charAt(0)));
//            holder.nameletter.setBackgroundResource(R.drawable.badge1);
//            holder.avata.setVisibility(View.GONE);
//        }

        holder.nameletter.setText(String.valueOf(name.charAt(0)));
        holder.nameletter.setBackgroundResource(R.drawable.badge1);
        holder.avata.setVisibility(View.GONE);

        holder.txtMessage.setText(lastMessage);

        String time = new SimpleDateFormat("EEE, d MMM yyyy").format(new Date(lastMessageTime));
        String today = new SimpleDateFormat("EEE, d MMM yyyy").format(new Date(System.currentTimeMillis()));

        if (today.equals(time)) {

            holder.txtTime.setText(new SimpleDateFormat("HH:mm").format(new Date(lastMessageTime)));

        } else {

            holder.txtTime.setText(new SimpleDateFormat("MMM d").format(new Date(lastMessageTime)));

        }

        holder.chatItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ChatMessagingActivity.class);
                intent.putExtra("receiver_id", id);
                intent.putExtra("email", email);
                intent.putExtra("image", image);
                intent.putExtra("name", name);
                context.startActivity(intent);
//                intent.putExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND, name);
//                intent.putExtra("profile_pic", allChatHisDetails.getProfile_img());
//                intent.putExtra("access_token", allChatHisDetails.getFirebase_API_key());
//                intent.putExtra("name", allChatHisDetails.getFname() + " " + allChatHisDetails.getLname());
//                ArrayList<CharSequence> idFriend = new ArrayList<CharSequence>();
//                idFriend.add(id);
//                intent.putCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID, idFriend);
//                intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID, idRoom);
//                ChatActivity2.bitmapAvataFriend = new HashMap<>();
//                mapMark.put(id, null);
//                fragment.startActivityForResult(intent, FriendsFragment.ACTION_START_CHAT);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userChatArrayList == null ? 0 : userChatArrayList.size();
    }

}