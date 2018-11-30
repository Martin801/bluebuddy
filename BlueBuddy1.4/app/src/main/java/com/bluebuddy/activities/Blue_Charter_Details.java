package com.bluebuddy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
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
import com.bluebuddy.models.CharterDetail;
import com.bluebuddy.models.CharterDetailResponse;
import com.bluebuddy.models.Friend;
import com.bluebuddy.models.ListFriend;
import com.bluebuddy.models.ReviewDetail;
import com.bluebuddy.models.ReviewDetails;
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

public class Blue_Charter_Details extends BuddyActivity {

    public static int ACTION_START_CHAT = 1;
    public static Map<String, Boolean> mapMark;
    public RatingBar ratingBar1, ratingBar;
    MyTextView navtxt, tvhp, tvfp, tvbtname, tvbtloc, tvbtdesc, tvbttype, tvbtcap, bell_counter, tvcrtrname, tvcrtmail, tvcrtrphone, rvdesc, rvtitle, rv_date, rviewer_name;
    FirebaseDatabase firebaseDatabase;
    int pass = 0;
    //  private CharterDetailResponse charterDetailResponse;
    LinearLayout linearlayout_title;
    Button btnShowHideReview;
    boolean isShowReview = false;
    LinearLayout ll_reviewList;
    TextView tv_no_review;
    private Activity _activity;
    private EditText rating_title, desc;
    private Context _context;
    private ArrayList<String> listFriendID = null;
    private ListFriend dataListFriend = null;
    private SharedPreferences pref;
    private String token, bellcounter, charter_name, AllCharter_FirebaseApi, firebase_id, firebase_email, charter_pro_pic, name, uid, idRoom, AllCharterId, rvw_title, description, rating_value, avata = "";
    private CollapsingToolbarLayout collapsingToolbar;
    private CardView rvcard, rvcardReview;
    private LinearLayout linearReviewList, linearReviewProduct;
    private ImageView boatimg, back, feattag;
    private RecyclerView reviewRecycler;
    private ReviewRecyclerAdapter reviewRecyclerAdapter;
    private ProgressDialog mProgressDialog;
    private Button button5, button2, rate_and_rvw;
    private boolean _isShow = false;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private CardView cv_contactDetails;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.blue_charter_details123);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.loader();
        super.notice();
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        uid = pref.getString(FIRE_REG_ID, "");

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

        _activity = this;
        _context = this;

        mapMark = new HashMap<>();

        initFirebase();

        cv_contactDetails = (CardView) findViewById(R.id.cv_contactDetails);

        feattag = (ImageView) findViewById(R.id.feattag);
        button5 = (Button) findViewById(R.id.button5);
        rvcard = (CardView) findViewById(R.id.ratecard);
        button2 = (Button) findViewById(R.id.button2);
        ratingBar1 = (RatingBar) findViewById(R.id.ratingBar1);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        linearReviewProduct = (LinearLayout) findViewById(R.id.linearReviewProduct);
        rating_title = (EditText) findViewById(R.id.rating_title);
        desc = (EditText) findViewById(R.id.desc);
        rate_and_rvw = (Button) findViewById(R.id.rate_and_rvw);
        btnShowHideReview = (Button) findViewById(R.id.btnShowHideReview);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");

        Bundle bundle = getIntent().getExtras();
        final String id = bundle.getString("AllCharter");
        pass = bundle.getInt("pass");

        if (pass == 2 || pass == 101) {
            super.setImageResource1(5);
        } else {
            super.setImageResource1(3);
        }

        AllCharterId = id;

        apicharterdetail(id);

        firebase_id = bundle.getString("AllCharter_Firebaseid");
        firebase_email = bundle.getString("AllCharter_FirebaseEmail");
        charter_pro_pic = bundle.getString("charter_pro_pic");
        charter_name = bundle.getString("charter_name");
        AllCharter_FirebaseApi = bundle.getString("AllCharter_FirebaseApi");

        chatWithCharter();

//        if (firebase_id != null && firebase_id.equals(uid)) {
//
//            button5.setVisibility(View.GONE);
//            // nipa comments
//            rvcard.setVisibility(View.GONE);
//
//        }
//
//        button5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                findIDEmail(firebase_email);
//            }
//        });


        //  Log.d("Token Receive in cp3", id.toString());
        navtxt = (MyTextView) findViewById(R.id.navtxt);
        navtxt.setText("BOAT RENTAL DETAILS");
        back = (ImageView) findViewById(R.id.imgNotification2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

//                if (pass == 1) {
//                    Intent i = new Intent(Blue_Charter_Details.this, BlueMarketCharterActivityNew.class);
//                    startActivity(i);
//                } else if (pass == 2) {
//                    // Intent i = new Intent(Blue_Charter_Details.this, BlueMarketActivity22.class);
//                    Intent i = new Intent(Blue_Charter_Details.this, MyListingActivity.class);
//                    startActivity(i);
//                }
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating_value = String.valueOf(ratingBar.getRating()).trim();
            }
        });


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
                Call<ReviewDetail> userCall = service.review("Bearer " + token, rvw_title, description, rating_value, "charter", id);
                userCall.enqueue(new Callback<ReviewDetail>() {
                    @Override
                    public void onResponse(Call<ReviewDetail> call, Response<ReviewDetail> response) {

                        if (response.code() == 200) {
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
        tvbtname = (MyTextView) findViewById(R.id.tvbtname);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        String tvbtname1 = tvbtname.getText().toString();


        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        tvhp = (MyTextView) findViewById(R.id.tvhp);
        tvfp = (MyTextView) findViewById(R.id.tvfp);
        tvbtloc = (MyTextView) findViewById(R.id.tvbtloc);
        tvbtdesc = (MyTextView) findViewById(R.id.tvbtdesc);
        tvbttype = (MyTextView) findViewById(R.id.tvbttype);
        tvbtcap = (MyTextView) findViewById(R.id.tvbtcap);
        tvcrtrname = (MyTextView) findViewById(R.id.tvcrtrname);
        tvcrtmail = (MyTextView) findViewById(R.id.tvcrtmail);
        tvcrtrphone = (MyTextView) findViewById(R.id.tvcrtrphone);
        rvcardReview = (CardView) findViewById(R.id.rvcardReview);
        tv_no_review = (TextView) findViewById(R.id.tv_no_review);
        ll_reviewList = (LinearLayout) findViewById(R.id.ll_reviewList);
        linearlayout_title = (LinearLayout) findViewById(R.id.linearlayout_title);
        linearReviewList = (LinearLayout) findViewById(R.id.linearReviewList);
        boatimg = (ImageView) findViewById(R.id.boatimg);
        reviewRecycler = (RecyclerView) findViewById(R.id.reviewRecycler);


        reviewRecycler.setLayoutManager(new LinearLayoutManager(this));
        reviewRecyclerAdapter = new ReviewRecyclerAdapter(_activity, _context, null);
        reviewRecycler.setAdapter(reviewRecyclerAdapter);
        bellcount();
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
    }

    private void chatWithCharter() {

        if (firebase_id != null && firebase_id != "" && firebase_id.equals(uid)) {

            button5.setVisibility(View.GONE);
            // nipa comments
            rvcard.setVisibility(View.GONE);

        }

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), ChatMessagingActivity.class);
                intent.putExtra("receiver_id", firebase_id);
                intent.putExtra("email", firebase_email);
                intent.putExtra("image", charter_pro_pic);
                intent.putExtra("name", charter_name);

                v.getContext().startActivity(intent);

//                findIDEmail(firebase_email);
            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    void callForReviewChange(ReviewDetails reviewDetails) {

        rvcard.setVisibility(View.GONE);
        isShowReview = true;
        reviewRecyclerAdapter.addReview(reviewDetails);
        tv_no_review.setVisibility(View.GONE);
        reviewRecycler.setVisibility(View.VISIBLE);
        checkForReviewString();

    }

    private void apicharterdetail(String id) {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<CharterDetailResponse> userCall = service.chrtDt("Bearer " + token, id);

        userCall.enqueue(new Callback<CharterDetailResponse>() {
            @Override
            public void onResponse(Call<CharterDetailResponse> call, Response<CharterDetailResponse> response) {

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                if (response.body() != null && response.body().getStatus()) {

                    Log.d("Got status", "" + response.body().getStatus());
                    CharterDetail details = response.body().getDetails();
                    //List<ReviewDetails> p = response.body().getReview();

                    if (response.body().getReview().size() > 0) {
                        tv_no_review.setVisibility(View.GONE);
                        // reviewRecyclerAdapter.updateAllReview(response.body().getReview(), uid, "charter", AllCharterId, "", _context);
                        reviewRecycler.setVisibility(View.VISIBLE);

                    } else {
                        reviewRecycler.setVisibility(View.GONE);
                        tv_no_review.setVisibility(View.VISIBLE);
                    }

                    reviewRecyclerAdapter.updateAllReview(response.body().getReview(), uid, "charter", AllCharterId, "", _context);

                    if (details != null) {
                        // nipa added code
                        if (!details.getIs_reviewed().matches("1")) {
                            rvcard.setVisibility(View.VISIBLE);
                        } else {
                            rvcard.setVisibility(View.GONE);
                        }

                        try {
                            if (details.getHide_details().trim().equalsIgnoreCase("1")) {
                                cv_contactDetails.setVisibility(View.GONE);
                            } else if (details.getHide_details().trim().equalsIgnoreCase("0")) {
                                cv_contactDetails.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.getStackTrace();
                        }

                        float rating = Float.parseFloat(details.getRating());
                        // Log.d("rate:", details.getRating());
                        ratingBar1.setRating(rating);
                        tvcrtrname.setText(details.getFname() + " " + details.getLname());
                        tvhp.setText("$" + details.getHalf_price());
                        tvfp.setText("$" + details.getFull_price());
                        tvbtname.setText(details.getName());
                        tvbtloc.setText(details.getLocation());
                        tvbtdesc.setText(details.getDescription());
                        tvbttype.setText(details.getType_of_boat());
                        tvbtcap.setText(details.getCapacity());
                        tvcrtmail.setText(details.getEmail());
                        tvcrtrphone.setText(details.getPhone());
                        String boat_rental_image = details.getImage();
                        Glide.with(Blue_Charter_Details.this).asBitmap().load(IMG_URL + boat_rental_image).into(new BitmapImageViewTarget(boatimg));

                        boatimg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ArrayList<String> images = new ArrayList<>();
                                images.add(boat_rental_image);
                                Intent intent = new Intent(Blue_Charter_Details.this, ImageSliderActivity.class);
                                intent.putStringArrayListExtra("images", images);
                                startActivity(intent);
                            }
                        });

                        if (details.getFeatured().equals("1")) {
                            feattag.setVisibility(View.VISIBLE);
                        } else if (details.getFeatured().equals("0")) {
                            feattag.setVisibility(View.GONE);
                        }

                        firebase_email = details.getEmail();
                        charter_name = details.getFname() + " " + details.getLname();
                        firebase_id = details.getFirebase_reg();
                        AllCharter_FirebaseApi = details.getFirebase_api();


                        // chat with the owner b.d.
                        chatWithCharter();

                    }

                } else {

                    Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CharterDetailResponse> call, Throwable t) {
                Log.d("onFailure", t.toString());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void generateReviewLayout(String reviewerName, String reviewingDate, String reviewtitle, String reviewingDesc) { // linearReviewList
        LinearLayout l1 = new LinearLayout(this);
        LinearLayout.LayoutParams l1params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        l1.setLayoutParams(l1params);
        l1.setOrientation(LinearLayout.VERTICAL);

        // For Textview
        TextView tv1 = new TextView(this);

        LinearLayout.LayoutParams tv1params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv1params.gravity = Gravity.LEFT;
        tv1params.bottomMargin = 5;
        tv1params.topMargin = 5;

        tv1.setLayoutParams(tv1params);
        tv1.setTextSize(16);
        tv1.setText(reviewerName.toString());
        //**********************

        // For Textview
        TextView tv2 = new TextView(this);

        LinearLayout.LayoutParams tv2params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv2params.gravity = Gravity.LEFT;
        tv2params.bottomMargin = 5;
        tv2params.topMargin = 5;

        tv2.setLayoutParams(tv2params);
        tv2.setTextSize(15);
        tv2.setText(reviewingDate.toString());
        //**********************

        // For Textview
        TextView tv3 = new TextView(this);

        LinearLayout.LayoutParams tv3params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv3params.gravity = Gravity.LEFT;
        tv3params.bottomMargin = 5;
        tv3params.topMargin = 5;

        tv3.setLayoutParams(tv3params);
        tv3.setTextSize(15);
        tv3.setText(reviewtitle.toString());

        TextView tv4 = new TextView(this);

        LinearLayout.LayoutParams tv4params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv4params.gravity = Gravity.LEFT;
        tv4params.bottomMargin = 5;
        tv4params.topMargin = 5;

        tv4.setLayoutParams(tv4params);
        tv4.setTextSize(15);
        tv4.setText(reviewingDesc.toString());


        View v = new View(this);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
        v.setPadding(0, 50, 0, 10);
        v.setBackgroundColor(Color.parseColor("#757575"));
        l1.addView(v);
        l1.addView(tv1);
        l1.addView(tv2);
        l1.addView(tv3);
        l1.addView(tv4);


        linearReviewList.addView(l1);
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, String data) {
        super.openDialog6(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, data);
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    @Override
    public void onBackPressed() {

        /*  Intent i = new Intent(Blue_Charter_Details.this, BlueMarketCharterActivityNew.class);
        startActivity(i);*/
//        if (pass == 1) {
//            Intent i = new Intent(Blue_Charter_Details.this, BlueMarketCharterActivityNew.class);
//            startActivity(i);
//        } else if (pass == 2) {
//            Intent i = new Intent(Blue_Charter_Details.this, BlueMarketActivity22.class);
//            startActivity(i);
//        } else if (pass == 101) {
//            Intent i = new Intent(Blue_Charter_Details.this, ActivityMyReviews.class);
//            startActivity(i);
//        }
        finish();

    }

    private void bellcount() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllNotice> userCall = service.allnotice1("Bearer " + token);

        userCall.enqueue(new Callback<AllNotice>() {
            @Override
            public void onResponse(Call<AllNotice> call, Response<AllNotice> response) {
                //    if (mProgressDialog.isShowing())
                //      mProgressDialog.dismiss();
                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("onResponse:", "" + response.body().getNotification_details());
                bellcounter = response.body().getCounter();
                if (bellcounter.equals("0")) {
                    bell_counter.setVisibility(View.GONE);
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
                    /*--*/
                    String name = (String) userMap.get("name");
                    /*--*/
                    //user.name = (String) userMap.get("name");
                    user.email = (String) userMap.get("email");
                    user.avata = (String) userMap.get("avata");
                    user.id = id;
                    user.idRoom = id.compareTo(StaticConfig.UID) > 0 ? (StaticConfig.UID + id).hashCode() + "" : "" + (id + StaticConfig.UID).hashCode();

                    Intent intent = new Intent(Blue_Charter_Details.this, SingleChatActivity.class);
                    ArrayList<CharSequence> idFriend = new ArrayList<CharSequence>();
                    idFriend.add(user.id);

                    intent.putCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID, idFriend);
                    intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID, user.idRoom);
                    intent.putExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND, name);

                    intent.putExtra("AllCharter", id);
                    intent.putExtra("AllCharter_Firebaseid", firebase_id);
                    intent.putExtra("AllCharter_FirebaseEmail", firebase_email);
                    intent.putExtra("charter_pro_pic", charter_pro_pic);
                    intent.putExtra("charter_name", charter_name);
                    intent.putExtra("AllCharter_FirebaseApi", AllCharter_FirebaseApi);
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

    private void initFirebase() {

        auth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    StaticConfig.UID = user.getUid();
                } else {
                    Blue_Charter_Details.this.finish();
                    // User is signed in
                    startActivity(new Intent(Blue_Charter_Details.this, LoginActivity.class));
                    Log.d("STATUS", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
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

}
