package com.bluebuddy.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.bluebuddy.R;
import com.bluebuddy.adapters.ListMessageAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.data.SharedPreferenceHelper;
import com.bluebuddy.data.StaticConfig;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.MessageDetails;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static com.bluebuddy.api.ApiClient.BASE_URL;
import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.FIRE_REG_ID;
import static com.bluebuddy.app.AppConfig.USER_FULL_NAME;

public class ChatMessagingActivity extends BuddyActivity implements View.OnClickListener {

    public static final int VIEW_TYPE_USER_MESSAGE = 0;
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 1;
    private static final String TAG = "ChatMessagingActivity";
    public static HashMap<String, Bitmap> bitmapAvataFriend;
    public Bitmap bitmapAvataUser;
    String serverkey = "AAAAmFQzmz4:APA91bGHHt9rZT18X3cfD9c4FHadRluzyo2_slaUpXTK9XYSSWmpqUHA5aQrZMtb_RJv7HPwnIK1dP_9D65pGhBPSqej_42ZV3u3yfL4dScEXJnfNavZ72xpyfFxP4-nKwW64hlOj_6Y";
    private RecyclerView recyclerChat;
    private ListMessageAdapter adapter;
    //    private String roomId, nameFriend, AllCharter, email;
    private ArrayList<CharSequence> idFriend;
    private ImageButton btnSend;
    private EditText editWriteMessage;
    private TextView nvxtxt;
    private ImageView back, pro_pic;
    private String token, access_token, uid, bellcounter, firebase_id, firebase_email, charter_pro_pic, charter_name, profile_pic;
    private SharedPreferences pref;
    private LinearLayoutManager linearLayoutManager;
    //    private ProgressDialog mProgressDialog;
    private MyTextView bell_counter, title;
    private String receiver_id, email, image, name;
    private String currentUserId;
    private ArrayList<MessageDetails> userChatArrayList;

    private LovelyProgressDialog dialogFindAllFriend;
    private TextView nameletter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.setLayout(R.layout.activity_chat_messaging);
        super.onCreate(savedInstanceState);
        super.loader();
        super.notice();
//        mProgressDialog = new ProgressDialog(this);
//        mProgressDialog = super.mProgressDialog;
        pro_pic = (ImageView) findViewById(R.id.pro_pic);
        nameletter = (TextView) findViewById(R.id.nameletter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        back = (ImageView) findViewById(R.id.imgNotification2);

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        uid = pref.getString(FIRE_REG_ID, "");

        nvxtxt = (TextView) findViewById(R.id.navtxt);
        nvxtxt.setText(charter_name);
        back = (ImageView) findViewById(R.id.imgNotification2);
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        editWriteMessage = (EditText) findViewById(R.id.editWriteMessage);
        bellcount();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        dialogFindAllFriend = new LovelyProgressDialog(ChatMessagingActivity.this);
        dialogFindAllFriend.setCancelable(false)
//                .setIcon(R.drawable.ic_add)
                .setTitle("Get all chat data....")
                .setTopColorRes(R.color.colorPrimary)
                .show();

        btnSend = (ImageButton) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = user.getUid();

        Intent intent = getIntent();
        receiver_id = intent.getStringExtra("receiver_id");
        email = intent.getStringExtra("email");
        image = intent.getStringExtra("image");
        name = intent.getStringExtra("name");

        // Message to user details //

        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();

        title = (MyTextView) findViewById(R.id.navtxt);
        title.setText(name);

        if (image.contains(BASE_URL)) {
            nameletter.setVisibility(View.GONE);
            pro_pic.setVisibility(View.VISIBLE);
            Glide.with(ChatMessagingActivity.this).asBitmap().load(image)
                    .into(new BitmapImageViewTarget(pro_pic) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(ChatMessagingActivity.this.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            pro_pic.setImageDrawable(circularBitmapDrawable);
                        }
                    });

//            pro_pic.setImageBitmap(textAsBitmap(String.valueOf(name.charAt(0)), 23, R.color.text_image_background));

//            Glide.with(ChatMessagingActivity.this).load(bitmapToByte(this,R.drawable.badge1,String.valueOf(name.charAt(0)))).asBitmap().centerCrop().into(new BitmapImageViewTarget(pro_pic) {
//                @Override
//                protected void setResource(Bitmap resource) {
//                    RoundedBitmapDrawable circularBitmapDrawable =
//                            RoundedBitmapDrawableFactory.create(ChatMessagingActivity.this.getResources(), resource);
//                    circularBitmapDrawable.setCircular(true);
//                    pro_pic.setImageDrawable(circularBitmapDrawable);
//                }
//            });

        } else {

            nameletter.setVisibility(View.VISIBLE);
            nameletter.setText(String.valueOf(name.charAt(0)));
            nameletter.setBackgroundResource(R.drawable.badge1);
            pro_pic.setVisibility(View.GONE);

        }

        // Message to user details //
        userChatArrayList = new ArrayList<>();
        getConversationLocation();

        String base64AvataUser = SharedPreferenceHelper.getInstance(this).getUserInfo().avata;
        if (!base64AvataUser.equals(StaticConfig.STR_DEFAULT_BASE64)) {
            byte[] decodedString = Base64.decode(base64AvataUser, Base64.DEFAULT);
            bitmapAvataUser = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } else {
            bitmapAvataUser = null;
        }

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerChat = (RecyclerView) findViewById(R.id.recyclerChat);
        recyclerChat.setLayoutManager(linearLayoutManager);
        adapter = new ListMessageAdapter(this, userChatArrayList, currentUserId, token, image, name);

//        FirebaseDatabase.getInstance().getReference().child("message/" + roomId).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                if (dataSnapshot.getValue() != null) {
//                    HashMap mapMessage = (HashMap) dataSnapshot.getValue();
//                    if ((String) mapMessage.get("idSender") != "") {
//                        Message newMessage = new Message();
//                        newMessage.idSender = (String) mapMessage.get("idSender");
//                        newMessage.idReceiver = (String) mapMessage.get("idReceiver");
//                        newMessage.text = (String) mapMessage.get("text");
//                        newMessage.timestamp = (long) mapMessage.get("timestamp");
//                        consersation.getListMessageData().add(newMessage);
//                        adapter.notifyDataSetChanged();
//                        linearLayoutManager.scrollToPosition(consersation.getListMessageData().size() - 1);
//                    }
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        recyclerChat.setAdapter(adapter);
    }

    public Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);

        return image;
    }

    private byte[] bitmapToByte(Context gContext, int gResId, String gText) {

        Bitmap bitmap = drawTextToBitmap(gContext, gResId, gText);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public Bitmap drawTextToBitmap(Context gContext, int gResId, String gText) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(61, 61, 61));
        // text size in pixels
        paint.setTextSize((int) (14 * scale));
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width()) / 2;
        int y = (bitmap.getHeight() + bounds.height()) / 2;
        canvas.drawText(gText, x, y, paint);
        return bitmap;
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

    private void getConversationLocation() {

        FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("conversations").child(receiver_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getKey() != null && dataSnapshot.getValue() != null) {

                    HashMap userData = (HashMap) dataSnapshot.getValue();
                    String messageLocation = userData.get("location").toString().trim();

                    getUsersChatHistory(messageLocation);
                    dialogFindAllFriend.dismiss();
                } else {
                    dialogFindAllFriend.dismiss();
                }
                dialogFindAllFriend.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    private void getUsersChatHistory(String messageLocation) {

        FirebaseDatabase.getInstance().getReference().child("conversations").child(messageLocation).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.getValue() != null) {

                    MessageDetails messageDetails = dataSnapshot.getValue(MessageDetails.class);
//                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
//                        Log.d(TAG, "onDataChange: Chat " + noteDataSnapshot);
//                        MessageDetails messageDetails = noteDataSnapshot.getValue(MessageDetails.class);
//                        Log.d(TAG, "onDataChange: Chat " + messageDetails.getContent());
//                        userChatArrayList.add(messageDetails);
//                    }
//                    dialogFindAllFriend.dismiss();

                    userChatArrayList.add(messageDetails);
                } else {
//                    dialogFindAllFriend.dismiss();
                }
                dialogFindAllFriend.dismiss();
                adapter.notifyDataSetChanged();
                linearLayoutManager.scrollToPosition(userChatArrayList.size() - 1);

//                Iterator i = dataSnapshot.getChildren().iterator();
//                while (i.hasNext()) {
//
//                    my_value1 =
//                            (String) ((DataSnapshot) i.next()).getValue();
//                    my_value2 =
//                            (String) ((DataSnapshot) i.next()).getValue();
//
//                    My_Model my_model = new My_Model(my_value1,
//                            my_value2);
//
//                    my_list.add(my_model);
//
//                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot,
                                       String s) {


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot,
                                     String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        FirebaseDatabase.getInstance().getReference().child("conversations").child(messageLocation).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                if (dataSnapshot.getValue() != null) {
//                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
//                        MessageDetails messageDetails = noteDataSnapshot.getValue(MessageDetails.class);
//                        Log.d(TAG, "onDataChange: Chat " + messageDetails.getContent());
//                        userChatArrayList.add(messageDetails);
//                    }
//                    dialogFindAllFriend.dismiss();
//                } else {
//                    dialogFindAllFriend.dismiss();
//                }
//                adapter.notifyDataSetChanged();
//                linearLayoutManager.scrollToPosition(userChatArrayList.size() - 1);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });

    }

    @Override
    public void onBackPressed() {

//        Intent i = new Intent(ChatMessagingActivity.this, ChatActivity.class);
////        i.putExtra("idFriend", idFriend.get(0));
//        startActivity(i);
        finish();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSend) {
            String content = editWriteMessage.getText().toString().trim();

//            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
//            cal.setTimeInMillis(timestamp);
//            String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();

            if (content.length() > 0) {
                editWriteMessage.setText("");
                final MessageDetails newMessage = new MessageDetails(currentUserId, receiver_id, content, false);
//                newMessage.setContent(content);
//                newMessage.setFromID(currentUserId);
//                newMessage.setToID(receiver_id);
//                newMessage.setTimestamp(ServerValue.TIMESTAMP);
//                newMessage.setTimestamp(new Timestamp(System.currentTimeMillis()).getTime());

//                newMessage.setIsRead(false);

                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                mDatabase.child("users").child(currentUserId).child("conversations").child(receiver_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getKey() != null && dataSnapshot.getValue() != null) {

                            HashMap userData = (HashMap) dataSnapshot.getValue();
                            String messageLocation = userData.get("location").toString().trim();

                            Log.d(TAG, "onDataChange1: " + messageLocation);

                            if (messageLocation != null) {
                                FirebaseDatabase.getInstance().getReference().child("conversations").child(messageLocation).push().setValue(newMessage);
                            }

                            dialogFindAllFriend.dismiss();
                        } else {


                            // Creating new messageLocation node, which returns the unique key value
                            // new conversations node would be /users/messageLocation/
                            String messageLocation = mDatabase.child("conversations").push().getKey();

                            Log.d(TAG, "onDataChange2: " + messageLocation);

                            // pushing messageDetails to 'conversations' node using the messageLocation
                            mDatabase.child("conversations").child(messageLocation).push().setValue(newMessage);

                            HashMap<String, String> locationId = new HashMap<>();
                            locationId.put("location", messageLocation);

                            mDatabase.child("users").child(currentUserId).child("conversations").child(receiver_id).setValue(locationId);
                            mDatabase.child("users").child(receiver_id).child("conversations").child(currentUserId).setValue(locationId);

                            dialogFindAllFriend.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });

                String creator = pref.getString(USER_FULL_NAME, "");
                HashMap<String, String> notification = new HashMap<String, String>();
                notification.put("body", creator + ": " + content);
                notification.put("icon", "appicon");

//                sendNotification(serverkey, new PushRequest(access_token, notification));
                sendNotification(email, content);
            }
        }
    }


    private void bellcount() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllNotice> userCall = service.allnotice1("Bearer " + token);

        userCall.enqueue(new Callback<AllNotice>() {
            @Override
            public void onResponse(Call<AllNotice> call, Response<AllNotice> response) {
//                Log.d("STATUS", String.valueOf(response.body().getStatus()));
//                Log.d("onResponse:", "" + response.body().getNotification_details());
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

//    private void sendNotification(String serverKey, PushRequest notification) {
//
//        ApiInterface service = ApiClient2.getClient2().create(ApiInterface.class);
//        Call<PushNotification> pushCall = service.push("key=" + serverKey, notification);
//
//        pushCall.enqueue(new Callback<PushNotification>() {
//            @Override
//            public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {
//                Log.d("SUCCESS_REGISTER", "SENT");
//            }
//
//            @Override
//            public void onFailure(Call<PushNotification> call, Throwable t) {
//                Log.d("FAILURE_REGISTER", "HERE I M");
//                t.printStackTrace();
//            }
//        });
//    }

    public void sendNotification(String email, String message) {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<String> userCall = service.sendChatNotification("Bearer " + token, email, message);
//        Call<String> userCall = service.sendChatNotification("Bearer " + token, email, message, "android");
        userCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });

    }

}
