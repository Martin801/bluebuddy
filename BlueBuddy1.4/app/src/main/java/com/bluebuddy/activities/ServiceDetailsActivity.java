package com.bluebuddy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bluebuddy.R;
import com.bluebuddy.adapters.ReviewRecyclerAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.data.FriendDB;
import com.bluebuddy.data.StaticConfig;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.Friend;
import com.bluebuddy.models.ListFriend;
import com.bluebuddy.models.ReviewDetail;
import com.bluebuddy.models.ReviewDetails;
import com.bluebuddy.models.ServiceDetail;
import com.bluebuddy.models.ServiceDetailResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.FIRE_REG_ID;
import static com.bluebuddy.app.AppConfig.IMG_URL;


public class ServiceDetailsActivity extends BuddyActivity {

    public static Map<String, Boolean> mapMark;
    MyTextView service_provider_name, stype, mail, contact, tvbtdesc;
    ImageView boatimg, back;
    RatingBar ratingBar, ratingBar1;
    String AllServiceId, uid, firebase_id, firebase_email, id1;
    Activity _activity;
    FirebaseDatabase firebaseDatabase;
    int pass = 0;
    LinearLayout linearlayout_title;
    Button btnShowHideReview;
    boolean isShowReview = false;
    LinearLayout ll_reviewList;
    TextView tv_no_review;
    private MyTextView navtxt, bell_counter;
    private ArrayList<String> listFriendID = null;
    private ListFriend dataListFriend = null;
    private CardView rvcard;
    private CardView cv_contactDetails;

    private RecyclerView reviewRecycler;
    private ReviewRecyclerAdapter reviewRecyclerAdapter;
    private Context _context;
    private EditText rating_title, desc;
    private String rating_value, description, rvw_title, bellcounter, service_pro_pic, service_name, service_fireapi;
    private CollapsingToolbarLayout collapsingToolbar;
    private Button button2, rate_and_rvw, button5;
    private LinearLayout linearReviewProduct;
    private boolean _isShow = false;
    private SharedPreferences pref;
    private String token;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private ProgressDialog progressDialog;
    private LinearLayout ll_contact;
    private LinearLayout ll_mail;
    private MyTextView tv_contactPerson;
    private MyTextView tv_mail;
    private MyTextView tv_phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_service_details123);
        super.onCreate(savedInstanceState);
        super.notice();
        super.initialize();
        _context = this;
        _activity = this;

        if (dataListFriend == null) {
            dataListFriend = FriendDB.getInstance(this).getListFriend();
            if (dataListFriend.getListFriend().size() > 0) {
                listFriendID = new ArrayList<>();
                for (Friend friend : dataListFriend.getListFriend()) {
                    listFriendID.add(friend.id);
                }
            }
        }

        if (listFriendID == null) {
            listFriendID = new ArrayList<>();
            getListFriendUId();
        }

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        uid = pref.getString(FIRE_REG_ID, "");

        mapMark = new HashMap<>();

        Bundle bundle = getIntent().getExtras();

        id1 = bundle.getString("AllService");
        AllServiceId = id1;
        service_pro_pic = bundle.getString("service_pro_pic");
        service_fireapi = bundle.getString("service_fireapi");

        firebase_id = bundle.getString("firebaseid");
        firebase_email = bundle.getString("AllService_FirebaseEmail");
        service_name = bundle.getString("service_name");
        pass = bundle.getInt("pass");

        ll_contact = (LinearLayout) findViewById(R.id.ll_contact);
        ll_contact.setVisibility(View.GONE);
        ll_mail = (LinearLayout) findViewById(R.id.ll_mail);
        ll_mail.setVisibility(View.GONE);

        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        contact = (MyTextView) findViewById(R.id.contact);
        mail = (MyTextView) findViewById(R.id.mail);
        stype = (MyTextView) findViewById(R.id.stype);
        boatimg = (ImageView) findViewById(R.id.boatimg);
        navtxt = (MyTextView) findViewById(R.id.navtxt);
        ratingBar1 = (RatingBar) findViewById(R.id.rateservice);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        tvbtdesc = (MyTextView) findViewById(R.id.tvbtdesc);
        linearReviewProduct = (LinearLayout) findViewById(R.id.linearReviewProduct);
        rating_title = (EditText) findViewById(R.id.rating_title);
        desc = (EditText) findViewById(R.id.desc);
        button2 = (Button) findViewById(R.id.button2);

        rate_and_rvw = (Button) findViewById(R.id.rate_and_rvw);

        reviewRecycler = (RecyclerView) findViewById(R.id.reviewRecycler);

        button5 = (Button) findViewById(R.id.button5);
        rvcard = (CardView) findViewById(R.id.rvwcard);

        cv_contactDetails = (CardView) findViewById(R.id.cv_contactDetails);

        tv_contactPerson = (MyTextView) findViewById(R.id.tv_contactPerson);
        tv_mail = findViewById(R.id.tv_mail);
        tv_phoneNo = findViewById(R.id.tv_phoneNo);


        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        service_provider_name = (MyTextView) findViewById(R.id.service_provider_name);
        service_provider_name.setVisibility(View.GONE);

        if (pass == 101 || pass == 2) {
            super.setImageResource1(5);
        } else {
            super.setImageResource1(3);
        }

        chatWithServiceProvider();

        apiservicedetail(id1);

        // Log.d("Token Receive in cp3", id.toString());
        //

        reviewRecycler.setLayoutManager(new LinearLayoutManager(this));
        reviewRecyclerAdapter = new ReviewRecyclerAdapter(_activity, _context, null);
        reviewRecycler.setAdapter(reviewRecyclerAdapter);

        navtxt.setText("SERVICE DETAILS");
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating_value = String.valueOf(ratingBar.getRating()).trim();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_isShow) {
                    linearReviewProduct.setVisibility(View.GONE);
                    _isShow = false;
                } else if (!_isShow) {
                    linearReviewProduct.setVisibility(View.VISIBLE);
                    _isShow = true;
                }
            }
        });

        rvw_title = rating_title.getText().toString().trim();
        description = desc.getText().toString().trim();

        // np add
        tv_no_review = (TextView) findViewById(R.id.tv_no_review);
        ll_reviewList = (LinearLayout) findViewById(R.id.ll_reviewList);
        linearlayout_title = (LinearLayout) findViewById(R.id.linearlayout_title);
        btnShowHideReview = (Button) findViewById(R.id.btnShowHideReview);
        checkForReviewString();
        btnShowHideReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowReview) {
                    isShowReview = false;
                } else {
                    isShowReview = true;
                }
                checkForReviewString();
            }
        });

        rate_and_rvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rvw_title = rating_title.getText().toString().trim();
                String description = desc.getText().toString().trim();
                if (rvw_title.length() == 0) {
                    oDialog("Enter a review title!", "Close", "", false, _activity, "", 0);
                    rating_title.requestFocus();
                } else if (description.length() == 0) {
                    oDialog("Enter your review description!", "Close", "", false, _activity, "", 0);
                    desc.requestFocus();
                }
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                Call<ReviewDetail> userCall = service.review("Bearer " + token, rvw_title, description, rating_value, "service", id1);
                userCall.enqueue(new Callback<ReviewDetail>() {
                    @Override
                    public void onResponse(Call<ReviewDetail> call, Response<ReviewDetail> response) {
                        // Log.d("RESPONSE_ ", String.valueOf(response.body().getStatus()));

                        if (response.code() == 200) {
                           /* linearReviewProduct.setVisibility(View.GONE);
                            _isShow = false;*/
                            if (response.body().getReview().get(0) != null) {
                                callForReviewChange(response.body().getReview().get(0));
                            }
                            oDialog(response.body().getMessage(), "Ok", "", false, _activity, "", 0, "");
                        } else {
                            Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewDetail> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        bellcount();

    }

    private void chatWithServiceProvider() {

        if (firebase_id != null && !firebase_id.equals("") && firebase_id.equals(uid)) {
            button5.setVisibility(View.GONE);
            rvcard.setVisibility(View.GONE);
        }
//        else if (firebase_id != null ) {
//
//            button5.setVisibility(View.GONE);
//            rvcard.setVisibility(View.GONE);
//        }

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                findIDEmail(firebase_email);

                Intent intent = new Intent(v.getContext(), ChatMessagingActivity.class);
                intent.putExtra("receiver_id", firebase_id);
                intent.putExtra("email", firebase_email);
                intent.putExtra("image", service_pro_pic);
                intent.putExtra("name", service_name);

                v.getContext().startActivity(intent);
            }
        });
    }

    void callForReviewChange(ReviewDetails reviewDetails) {
        rvcard.setVisibility(View.GONE);
        isShowReview = true;
        reviewRecyclerAdapter.addReview(reviewDetails);
        tv_no_review.setVisibility(View.GONE);
        reviewRecycler.setVisibility(View.VISIBLE);
        checkForReviewString();
    }

    void checkForReviewString() {
        if (isShowReview) {
            btnShowHideReview.setText(getString(R.string.hide_all_reviews));
            ll_reviewList.setVisibility(View.VISIBLE);
        } else {
            btnShowHideReview.setText(getString(R.string.view_all_reviews));
            ll_reviewList.setVisibility(View.GONE);
        }

    }

    private void apiservicedetail(final String id1) {

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(_activity);
            progressDialog.setTitle(getString(R.string.loading_title));
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.loading_message));
        }

        progressDialog.show();
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<ServiceDetailResponse> userCall = service.serviceDt("Bearer " + token, id1);

        userCall.enqueue(new Callback<ServiceDetailResponse>() {
            @Override
            public void onResponse(Call<ServiceDetailResponse> call, Response<ServiceDetailResponse> response) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                if (response.body() != null && response.body().getStatus() == true) {
                    ServiceDetail details = response.body().getDetails();

                    //   List<ReviewDetails> p = response.body().getReview();
                    if (response.body().getReview() != null && response.body().getReview().size() > 0) {
                        tv_no_review.setVisibility(View.GONE);
                        //reviewRecyclerAdapter.updateAllReview(response.body().getReview(), uid, "service", id1, "", _context);
                        reviewRecycler.setVisibility(View.VISIBLE);

                    } else {
                        reviewRecycler.setVisibility(View.GONE);
                        tv_no_review.setVisibility(View.VISIBLE);
                    }
                    reviewRecyclerAdapter.updateAllReview(response.body().getReview(), uid, "service", id1, "", _context);
                    if (details != null) {

                        if (!details.getIs_reviewed().matches("1")) {
                            rvcard.setVisibility(View.VISIBLE);
                        } else {
                            rvcard.setVisibility(View.GONE);
                        }

                        float rating = Float.parseFloat(details.getRating());
                        ratingBar1.setRating(rating);
                        stype.setText(details.getService_type());
                        tvbtdesc.setText(details.getDescription());

                        service_provider_name.setText(details.getFname() + " " + details.getLname());
                        mail.setText(details.getEmail());
                        contact.setText(details.getPhone());

                        try {
                            if (details.getHide_details().trim().equalsIgnoreCase("1")) {
                                cv_contactDetails.setVisibility(View.GONE);
//                                ll_contact.setVisibility(View.GONE);
//                                ll_mail.setVisibility(View.GONE);
                            } else if (details.getHide_details().trim().equalsIgnoreCase("0")) {
                                cv_contactDetails.setVisibility(View.VISIBLE);
//                                ll_contact.setVisibility(View.VISIBLE);
//                                ll_mail.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.getStackTrace();
                        }

                        tv_contactPerson.setText(details.getFname() + " " + details.getLname());
                        tv_mail.setText(details.getEmail());
                        tv_phoneNo.setText(details.getPhone());

                        Glide.with(ServiceDetailsActivity.this).asBitmap().load(IMG_URL + details.getImage()).into(new BitmapImageViewTarget(boatimg));

                        boatimg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ArrayList<String> images = new ArrayList<>();
                                images.add(details.getImage());
                                Intent intent = new Intent(ServiceDetailsActivity.this, ImageSliderActivity.class);
                                intent.putStringArrayListExtra("images", images);
                                startActivity(intent);
                            }
                        });

//                        service_pro_pic = bundle.getString("service_pro_pic");
                        service_fireapi = details.getFirebase_api();

                        firebase_id = details.getFirebase_reg();
                        firebase_email = details.getEmail();
                        service_name = details.getFname();

                        chatWithServiceProvider();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServiceDetailResponse> call, Throwable t) {
                //  Log.d("onFailure", t.toString());
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, String data) {
        super.openDialog6(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, data);
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
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
                if (bellcounter.equals("0")) {
                    bell_counter.setVisibility(View.GONE);
                } else if (!bellcounter.equals("0")) {
                    bell_counter.setVisibility(View.VISIBLE);
                }
                bell_counter.setText(response.body().getCounter());

            }

            @Override
            public void onFailure(Call<AllNotice> call, Throwable t) {

            }
        });
    }

    private void findIDEmail(final String email) {
        findIDEmail1(email);
        FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    String id = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();

                    HashMap userMap = (HashMap) ((HashMap) dataSnapshot.getValue()).get(id);
                    Friend user = new Friend();
                    String name = (String) userMap.get("name");
                    user.email = (String) userMap.get("email");
                    user.avata = (String) userMap.get("avata");
                    user.id = id;
                    user.idRoom = id.compareTo(StaticConfig.UID) > 0 ? (StaticConfig.UID + id).hashCode() + "" : "" + (id + StaticConfig.UID).hashCode();

                    Intent intent = new Intent(ServiceDetailsActivity.this, SingleChatActivityservice.class);

                    ArrayList<CharSequence> idFriend = new ArrayList<CharSequence>();
                    idFriend.add(user.id);

                    intent.putCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID, idFriend);
                    intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID, user.idRoom);
                    intent.putExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND, name);

                    intent.putExtra("AllService", id1);
                    intent.putExtra("firebaseid", firebase_id);
                    intent.putExtra("AllService_FirebaseEmail", firebase_email);
                    intent.putExtra("service_pro_pic", service_pro_pic);
                    intent.putExtra("service_name", service_name);
                    intent.putExtra("service_fireapi", service_fireapi);
                    intent.putExtra("pass", pass);

                    mapMark.put(user.id, null);
                    _activity.startActivityForResult(intent, Blue_Charter_Details.ACTION_START_CHAT);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void findIDEmail1(String email) {

        FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null) {
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

        if (listFriendID.contains(idFriend)) {

        } else {
            addFriend(idFriend, true);
            listFriendID.add(idFriend);
            dataListFriend.getListFriend().add(userInfo);
            FriendDB.getInstance(this).addFriend(userInfo);

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

    private void getListFriendUId() {
        FirebaseDatabase.getInstance().getReference().child("friend/" + StaticConfig.UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    HashMap mapRecord = (HashMap) dataSnapshot.getValue();
                    Iterator listKey = mapRecord.keySet().iterator();
                    while (listKey.hasNext()) {
                        String key = listKey.next().toString();
                        listFriendID.add(mapRecord.get(key).toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        /*if (pass == 1) {
            Intent i = new Intent(ServiceDetailsActivity.this, BlueMarketServiceActivityNew.class);
            startActivity(i);
        } else if (pass == 2) {
            Intent i = new Intent(ServiceDetailsActivity.this, MyListingActivity.class);
            startActivity(i);
        } else if (pass == 101) {
            Intent i = new Intent(ServiceDetailsActivity.this, ActivityMyReviews.class);
            startActivity(i);
        }*/
        finish();
    }

}
