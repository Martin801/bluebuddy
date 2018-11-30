package com.bluebuddy.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.bluebuddy.R;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.api.ApiClient2;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.data.SharedPreferenceHelper;
import com.bluebuddy.data.StaticConfig;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AfterLoginStatus;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.Consersation;
import com.bluebuddy.models.Message;
import com.bluebuddy.models.ProfileDetails;
import com.bluebuddy.models.PushNotification;
import com.bluebuddy.models.PushRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.FIRE_REG_ID;
import static com.bluebuddy.app.AppConfig.IMG_URL;
import static com.bluebuddy.app.AppConfig.USER_FULL_NAME;


public class ChatActivity2 extends BuddyActivity implements View.OnClickListener {

    private RecyclerView recyclerChat;
    public static final int VIEW_TYPE_USER_MESSAGE = 0;
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 1;
    private ListMessageAdapter adapter;
    private String roomId, nameFriend, AllCharter, email;
    private ArrayList<CharSequence> idFriend;
    private Consersation consersation;
    private ImageButton btnSend;
    private EditText editWriteMessage;
    private TextView nvxtxt;
    private ImageView back, pro_pic;
    private String token, access_token, uid, bellcounter, id, firebase_id, firebase_email, charter_pro_pic, charter_name, profile_pic, name;
    private SharedPreferences pref;
    private LinearLayoutManager linearLayoutManager;
    public static HashMap<String, Bitmap> bitmapAvataFriend;
    public Bitmap bitmapAvataUser;
    private ProgressDialog mProgressDialog;
    private MyTextView bell_counter, title;
    String serverkey = "AAAAmFQzmz4:APA91bGHHt9rZT18X3cfD9c4FHadRluzyo2_slaUpXTK9XYSSWmpqUHA5aQrZMtb_RJv7HPwnIK1dP_9D65pGhBPSqej_42ZV3u3yfL4dScEXJnfNavZ72xpyfFxP4-nKwW64hlOj_6Y";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.setLayout(R.layout.activity_chat);
        super.onCreate(savedInstanceState);
        super.loader();
        super.notice();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        uid = pref.getString(FIRE_REG_ID, "");

        Intent intentData = getIntent();

        nameFriend = intentData.getStringExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND);
        idFriend = intentData.getCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID);
        roomId = intentData.getStringExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID);
        Bundle bundle = getIntent().getExtras();
        profile_pic = bundle.getString("profile_pic");
        name = bundle.getString("name");
        access_token = bundle.getString("access_token");

        pro_pic = (ImageView) findViewById(R.id.pro_pic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        nvxtxt = (TextView) findViewById(R.id.navtxt);
        nvxtxt.setText(charter_name);
        back = (ImageView) findViewById(R.id.imgNotification2);
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        bellcount();

        consersation = new Consersation();
        btnSend = (ImageButton) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        String base64AvataUser = SharedPreferenceHelper.getInstance(this).getUserInfo().avata;
        if (!base64AvataUser.equals(StaticConfig.STR_DEFAULT_BASE64)) {
            byte[] decodedString = Base64.decode(base64AvataUser, Base64.DEFAULT);
            bitmapAvataUser = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } else {
            bitmapAvataUser = null;
        }

        editWriteMessage = (EditText) findViewById(R.id.editWriteMessage);
        if (idFriend != null && nameFriend != null) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            title = (MyTextView) findViewById(R.id.navtxt);
            title.setText(name);
            Glide.with(ChatActivity2.this).asBitmap().load(IMG_URL + profile_pic).into(new BitmapImageViewTarget(pro_pic) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(ChatActivity2.this.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    pro_pic.setImageDrawable(circularBitmapDrawable);
                }
            });

            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerChat = (RecyclerView) findViewById(R.id.recyclerChat);
            recyclerChat.setLayoutManager(linearLayoutManager);
            adapter = new ListMessageAdapter(this, consersation, bitmapAvataFriend, bitmapAvataUser);
            FirebaseDatabase.getInstance().getReference().child("message/" + roomId).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.getValue() != null) {
                        HashMap mapMessage = (HashMap) dataSnapshot.getValue();
                        if ((String) mapMessage.get("idSender") != "") {
                            Message newMessage = new Message();
                            newMessage.idSender = (String) mapMessage.get("idSender");
                            newMessage.idReceiver = (String) mapMessage.get("idReceiver");
                            newMessage.text = (String) mapMessage.get("text");
                            newMessage.timestamp = (long) mapMessage.get("timestamp");
                            consersation.getListMessageData().add(newMessage);
                            adapter.notifyDataSetChanged();
                            linearLayoutManager.scrollToPosition(consersation.getListMessageData().size() - 1);
                        }
                    }
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

            recyclerChat.setAdapter(adapter);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChatActivity2.this, ChatActivity.class);
                startActivity(i);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent result = new Intent();
            result.putExtra("idFriend", idFriend.get(0));
            setResult(RESULT_OK, result);
            this.finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ChatActivity2.this, ChatActivity.class);
        i.putExtra("idFriend", idFriend.get(0));
        startActivity(i);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSend) {
            String content = editWriteMessage.getText().toString().trim();
            if (content.length() > 0) {
                editWriteMessage.setText("");
                Message newMessage = new Message();
                newMessage.text = content;
                newMessage.idSender = StaticConfig.UID;
                newMessage.idReceiver = roomId;
                newMessage.timestamp = System.currentTimeMillis();
                FirebaseDatabase.getInstance().getReference().child("message/" + roomId).push().setValue(newMessage);

                String creator = pref.getString(USER_FULL_NAME, "");
                HashMap<String, String> notification = new HashMap<String, String>();
                notification.put("body", creator + ": " + content);
                notification.put("icon", "appicon");

                sendNotification(serverkey, new PushRequest(access_token, notification));
            }
        }
    }

    class ListMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context context;
        private Consersation consersation;
        private HashMap<String, Bitmap> bitmapAvata;
        private HashMap<String, DatabaseReference> bitmapAvataDB;
        private Bitmap bitmapAvataUser;

        public ListMessageAdapter(Context context, Consersation conversation, HashMap<String, Bitmap> bitmapAvata, Bitmap bitmapAvataUser) {
            this.context = context;
            this.consersation = conversation;
            this.bitmapAvata = bitmapAvata;
            this.bitmapAvataUser = bitmapAvataUser;
            bitmapAvataDB = new HashMap<>();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == ChatActivity2.VIEW_TYPE_FRIEND_MESSAGE) {
                View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_friend, parent, false);
                return new ItemMessageFriendHolder(view);
            } else if (viewType == ChatActivity2.VIEW_TYPE_USER_MESSAGE) {
                View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_user, parent, false);
                return new ItemMessageUserHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ItemMessageFriendHolder) {

                ((ItemMessageFriendHolder) holder).txtContent.setText(consersation.getListMessageData().get(position).text);

                String time = new SimpleDateFormat("EEE, d MMM yyyy").format(new Date((consersation.getListMessageData().get(position).timestamp)));
                String today = new SimpleDateFormat("EEE, d MMM yyyy").format(new Date(System.currentTimeMillis()));

                if (today.equals(time)) {
                    ((ChatActivity2.ItemMessageFriendHolder) holder).time.setText(new SimpleDateFormat("HH:mm").format(new Date(consersation.getListMessageData().get(position).timestamp)));
                } else {
                    ((ChatActivity2.ItemMessageFriendHolder) holder).time.setText(new SimpleDateFormat("MMM d").format(new Date(consersation.getListMessageData().get(position).timestamp)));
                }

                if (profile_pic != null) {
                    Glide.with(ChatActivity2.this).asBitmap().load(IMG_URL + profile_pic).into(new BitmapImageViewTarget(((ItemMessageFriendHolder) holder).avata) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(ChatActivity2.this.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ((ItemMessageFriendHolder) holder).avata.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }

            } else if (holder instanceof ItemMessageUserHolder) {

                ((ItemMessageUserHolder) holder).txtContent.setText(consersation.getListMessageData().get(position).text);

                String time = new SimpleDateFormat("EEE, d MMM yyyy").format(new Date((consersation.getListMessageData().get(position).timestamp)));
                String today = new SimpleDateFormat("EEE, d MMM yyyy").format(new Date(System.currentTimeMillis()));

                if (today.equals(time)) {
                    ((ChatActivity2.ItemMessageUserHolder) holder).time.setText(new SimpleDateFormat("HH:mm").format(new Date(consersation.getListMessageData().get(position).timestamp)));
                } else {
                    ((ChatActivity2.ItemMessageUserHolder) holder).time.setText(new SimpleDateFormat("MMM d").format(new Date(consersation.getListMessageData().get(position).timestamp)));
                }

                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                Call<AfterLoginStatus> userCall = service.mpa("Bearer " + token);

                userCall.enqueue(new Callback<AfterLoginStatus>() {
                    @Override
                    public void onResponse(Call<AfterLoginStatus> call, Response<AfterLoginStatus> response) {

                        if (response.body().getStatus() == true) {
                            Log.d("Got status", "" + response.body().getStatus());
                            Log.d("Profile_details3 sdata", "" + response.body().getProfile_details());
                            ProfileDetails p = response.body().getProfile_details();

                            if (p != null) {
                                String profile_Pic = p.getProfile_pic();
                                Glide.with(ChatActivity2.this).asBitmap().load(IMG_URL + profile_Pic).into(new BitmapImageViewTarget(((ItemMessageUserHolder) holder).avata) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable circularBitmapDrawable =
                                                RoundedBitmapDrawableFactory.create(ChatActivity2.this.getResources(), resource);
                                        circularBitmapDrawable.setCircular(true);
                                        ((ItemMessageUserHolder) holder).avata.setImageDrawable(circularBitmapDrawable);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AfterLoginStatus> call, Throwable t) {
                        Log.d("onFailure", t.toString());
                    }
                });
            }
        }

        @Override
        public int getItemViewType(int position) {
            return consersation.getListMessageData().get(position).idSender.equals(StaticConfig.UID) ? ChatActivity2.VIEW_TYPE_USER_MESSAGE : ChatActivity2.VIEW_TYPE_FRIEND_MESSAGE;
        }

        @Override
        public int getItemCount() {
            return consersation.getListMessageData().size();
        }
    }

    class ItemMessageUserHolder extends RecyclerView.ViewHolder {
        public MyTextView txtContent, time;
        public ImageView avata;

        public ItemMessageUserHolder(View itemView) {
            super(itemView);
            txtContent = (MyTextView) itemView.findViewById(R.id.textContentUser);
            time = (MyTextView) itemView.findViewById(R.id.timecontent);
            avata = (ImageView) itemView.findViewById(R.id.imageView2);
        }
    }

    class ItemMessageFriendHolder extends RecyclerView.ViewHolder {
        public MyTextView txtContent, time;
        public ImageView avata;

        public ItemMessageFriendHolder(View itemView) {
            super(itemView);
            txtContent = (MyTextView) itemView.findViewById(R.id.textContentFriend);
            time = (MyTextView) itemView.findViewById(R.id.timecontent);
            avata = (ImageView) itemView.findViewById(R.id.imageView3);
        }
    }

    class ItemtTodayHolder extends RecyclerView.ViewHolder {
        public MyTextView txtContent;


        public ItemtTodayHolder(View itemView) {
            super(itemView);
            txtContent = (MyTextView) itemView.findViewById(R.id.timecontent);
        }
    }

    private void bellcount() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllNotice> userCall = service.allnotice1("Bearer " + token);

        userCall.enqueue(new Callback<AllNotice>() {
            @Override
            public void onResponse(Call<AllNotice> call, Response<AllNotice> response) {
                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("onResponse:", "" + response.body().getNotification_details());
                bellcounter = response.body().getCounter();
                if (bellcounter != null) {
                    if (bellcounter.equals("0")) {
                        bell_counter.setVisibility(View.GONE);
                    } else if (!bellcounter.equals("0")) {
                        bell_counter.setVisibility(View.VISIBLE);
                    }
                    bell_counter.setText(response.body().getCounter());
                }
            }

            @Override
            public void onFailure(Call<AllNotice> call, Throwable t) {

            }
        });
    }

    private void sendNotification(String serverKey, PushRequest notification) {

        ApiInterface service = ApiClient2.getClient2().create(ApiInterface.class);
        Call<PushNotification> pushCall = service.push("key=" + serverKey, notification);

        pushCall.enqueue(new Callback<PushNotification>() {
            @Override
            public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {
                Log.d("SUCCESS_REGISTER", "SENT");
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t) {
                Log.d("FAILURE_REGISTER", "HERE I M");
                t.printStackTrace();
            }
        });
    }
}

