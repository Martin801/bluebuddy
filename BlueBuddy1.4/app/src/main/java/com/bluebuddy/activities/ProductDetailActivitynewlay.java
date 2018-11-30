package com.bluebuddy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.bluebuddy.R;
import com.bluebuddy.adapters.CustomAdapterforProDetails;
import com.bluebuddy.adapters.ReviewRecyclerAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.data.FriendDB;
import com.bluebuddy.data.StaticConfig;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.Friend;
import com.bluebuddy.models.ListFriend;
import com.bluebuddy.models.ProductDetail;
import com.bluebuddy.models.ProductDetailResponse;
import com.bluebuddy.models.ProductImage;
import com.bluebuddy.models.ReviewDetail;
import com.bluebuddy.models.ReviewDetails;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.FIRE_REG_ID;

public class ProductDetailActivitynewlay extends BuddyActivity {

    private static final String TAG = "ProductDetActnewlay";
    public static Map<String, Boolean> mapMark;
    private static int currentpage = 0;
    public RatingBar ratingBar1;
    ViewPager viewPager;
    RatingBar ratingBar;
    Activity _activity;
    String AllProduct_Firebaseid, product_profile_pic, AllProduct_FirebaseEmail;
    FirebaseDatabase firebaseDatabase;
    LinearLayout linearlayout_title;
    Button btnShowHideReview;
    boolean isShowReview = false;
    LinearLayout ll_reviewList;
    TextView tv_no_review;
    int currentPage = 0;
    int pass = 0;
    private ArrayList<String> listFriendID = null;
    private ListFriend dataListFriend = null;
    private EditText rating_title, desc;
    private CustomAdapterforProDetails adapter;
    private CirclePageIndicator indicator;
    private MyTextView pro_name, pro_desc, pro_pname, pro_pmail, pro_pnum, bell_counter;
    private TextView pro_price;
    private String rating_value, description, rvw_title, bellcounter;
    private String image1, product_fireapi, image2, image3, product_name, image4 = "";
    private ArrayList<String> imagelist = new ArrayList<>();
    private Context _context;
    private CardView rvwcard;
    private SharedPreferences pref;
    private String token;
    private Button button2, rate_and_rvw, button5;
    private boolean _isShow = false;
    private LinearLayout linearReviewProduct;
    private String id1, uid = "", cat;
    private RecyclerView reviewRecycler;
    private ReviewRecyclerAdapter reviewRecyclerAdapter;
    private ImageView back, feattag;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private ProgressDialog progressDialog;

    private CardView cv_contactDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.setLayout(R.layout.activity_product);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.notice();

        _context = this;
        _activity = this;

        initializeElements();

        mapMark = new HashMap<>();

        super.setTitle("PRODUCT DETAILS");

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
        Bundle bundle = getIntent().getExtras();

        id1 = bundle.getString("AllProduct");
        cat = bundle.getString("Category");
        pass = bundle.getInt("pass");

        if (pass == 101) {
            super.setImageResource1(5);
        } else if (pass == 1) {
            super.setImageResource1(3);
        }

        //  Log.d("cat Receive product", cat);
        AllProduct_Firebaseid = bundle.getString("AllProduct_Firebaseid");
        AllProduct_FirebaseEmail = bundle.getString("AllProduct_FirebaseEmail");
        product_name = bundle.getString("product_name");
        product_profile_pic = bundle.getString("product_profile_pic");
        product_fireapi = bundle.getString("product_fireapi");

//        Log.d(TAG, "onCreate: Firebaseid"+AllProduct_Firebaseid+" Email "+AllProduct_FirebaseEmail+" product_name  "+product_name+" profi_pic "+product_profile_pic);

        cv_contactDetails = (CardView) findViewById(R.id.cv_contactDetails);

        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        button5 = (Button) findViewById(R.id.button5);
        button2 = (Button) findViewById(R.id.button2);

        chatWithProductSeller();

        apiproductdetail(id1);

        _activity = this;
        ratingBar1 = (RatingBar) findViewById(R.id.ratingBar1);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        linearReviewProduct = (LinearLayout) findViewById(R.id.linearReviewProduct);
        rating_title = (EditText) findViewById(R.id.rating_title);
        desc = (EditText) findViewById(R.id.desc);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating_value = String.valueOf(ratingBar.getRating()).trim();
            }
        });
        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

        rate_and_rvw = (Button) findViewById(R.id.rate_and_rvw);
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
                Call<ReviewDetail> userCall = service.review("Bearer " + token, rvw_title, description, rating_value, "product", id1);
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
                        // t.printStackTrace();
                        Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bellcount();
    }

    private void chatWithProductSeller() {
        if (AllProduct_Firebaseid != null && AllProduct_Firebaseid != "" && AllProduct_Firebaseid.equals(uid)) {
            button5.setVisibility(View.GONE);
            rvwcard.setVisibility(View.GONE);
        }

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                findIDEmail(AllProduct_FirebaseEmail);
                Intent intent = new Intent(v.getContext(), ChatMessagingActivity.class);
                intent.putExtra("receiver_id", AllProduct_Firebaseid);
                intent.putExtra("email", AllProduct_FirebaseEmail);
                intent.putExtra("image", product_profile_pic);
                intent.putExtra("name", product_name);

                v.getContext().startActivity(intent);
            }
        });
    }

    private void initializeElements() {

        feattag = (ImageView) findViewById(R.id.feattag);
        rvwcard = (CardView) findViewById(R.id.rvwcard);
        pro_name = (MyTextView) findViewById(R.id.proname);
        pro_desc = (MyTextView) findViewById(R.id.prodesc);
        pro_price = (TextView) findViewById(R.id.proprice);
        pro_pname = (MyTextView) findViewById(R.id.procp);
        pro_pmail = (MyTextView) findViewById(R.id.procpmail);
        pro_pnum = (MyTextView) findViewById(R.id.procpnum);
        reviewRecycler = (RecyclerView) findViewById(R.id.reviewRecycler);
        reviewRecycler.setLayoutManager(new LinearLayoutManager(this));
        reviewRecyclerAdapter = new ReviewRecyclerAdapter(_activity, _context, null);
        reviewRecycler.setAdapter(reviewRecyclerAdapter);

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

    }

    void callForReviewChange(ReviewDetails reviewDetails) {
        rvwcard.setVisibility(View.GONE);
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

    public void setImageViewAdapter(Activity activity, Context ctx, ArrayList<String> imgList) {
        Timer timer = new Timer();
        adapter = new CustomAdapterforProDetails(activity, ctx, imgList);
        viewPager.setAdapter(adapter);

        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentpage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        timer.scheduleAtFixedRate(new TimerSlide(), 1000, 2000);
    }

    private void apiproductdetail(final String id) {

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(_activity);
            progressDialog.setTitle(getString(R.string.loading_title));
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.loading_message));
        }

        progressDialog.show();
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<ProductDetailResponse> userCall = service.productDt("Bearer " + token, id);

        userCall.enqueue(new Callback<ProductDetailResponse>() {
            @Override
            public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {

                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                if (response.body() != null && response.body().getStatus() == true) {
                   /* Log.d("Got status", "" + response.body().getStatus());
                    Log.d("IMAGE:", response.body().getDetails().getImage());*/
//                    String images = response.body().getDetails().getImage();
//
//                    String img[] = images.split(",");
//                    int imgLen = img == null ? -1 : img.length;
//                    imagelist = Arrays.asList(img);

                    if (!response.body().getDetails().getProductImages().isEmpty()) {

                        for (ProductImage productImage : response.body().getDetails().getProductImages()) {
                            imagelist.add(productImage.getImage());
                        }

                    }

//                    if (imgLen >= 0) {
//                        for (int ij = 0; ij < imgLen; ij++) {
//                            //  Log.d("sssssss", img[ij]);
//                        }
//                    }

                    setImageViewAdapter(_activity, _context, imagelist);

                    ProductDetail details = response.body().getDetails();

                    if (response.body().getReview() != null && response.body().getReview().size() > 0) {
                        tv_no_review.setVisibility(View.GONE);
                        // reviewRecyclerAdapter.updateAllReview(response.body().getReview(),uid,"product",id,cat,_context);
                        reviewRecycler.setVisibility(View.VISIBLE);

                    } else {
                        reviewRecycler.setVisibility(View.GONE);
                        tv_no_review.setVisibility(View.VISIBLE);
                    }
                    // List<ReviewDetails> p = response.body().getReview();
                    reviewRecyclerAdapter.updateAllReview(response.body().getReview(), uid, "product", id, cat, _context);

                    if (details != null) {

                        Log.d(TAG, "onResponse: " + details.toString());

                        // np add
                        if (!details.getIsReviewed().matches("1")) {
                            rvwcard.setVisibility(View.VISIBLE);
                        } else {
                            rvwcard.setVisibility(View.GONE);
                        }

                        float rating = 0;
                        try {
                            rating = Float.parseFloat(String.valueOf(details.getRating()));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                        if (details.getHideDetails().trim().equalsIgnoreCase("1")) {
                            cv_contactDetails.setVisibility(View.GONE);
                        } else if (details.getHideDetails().trim().equalsIgnoreCase("0")) {
                            cv_contactDetails.setVisibility(View.VISIBLE);
                        }

                        // Log.d("rate:", details.getRating());
                        ratingBar1.setRating(rating);
                        pro_name.setText(details.getName());
                        pro_desc.setText(details.getDescription());
                        pro_price.setText("$" + details.getPrice());
                        pro_pname.setText(details.getFname() + " " + details.getLname());
                        pro_pmail.setText(details.getEmail());
                        pro_pnum.setText(details.getPhone());

                        if (details.getFeatured().equals("1")) {
                            feattag.setVisibility(View.VISIBLE);
                        } else if (details.getFeatured().equals("0")) {
                            feattag.setVisibility(View.GONE);
                        }

                        AllProduct_Firebaseid = details.getFirebaseReg();
                        AllProduct_FirebaseEmail = details.getEmail();
                        product_name = details.getFname();
//                        product_profile_pic = details;
                        product_fireapi = details.getFirebaseApi();

                        chatWithProductSeller();

                    }

                  /*  if (p != null && p.size() > 0) {
                        for (int ir = 0; ir < p.size(); ir++) {
                        }
                    } else {

                    }*/
                } else {
                    Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductDetailResponse> call, Throwable t) {
                //Log.d("onFailure", t.toString());
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

    @Override
    public void onBackPressed() {

//        Intent i = null;
//
//        if (pass == 1) {
//
//            i = new Intent(ProductDetailActivitynewlay.this, BlueMarketProductActivityNew.class);
//            i.putExtra("category", cat);
//            i.putExtra("pass", pass);
//            Log.d("details back", cat);
//            startActivity(i);
//
//        } else if (pass == 101) {
//            i = new Intent(ProductDetailActivitynewlay.this, ActivityMyReviews.class);
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

    public class TimerSlide extends TimerTask {

        @Override
        public void run() {
            ProductDetailActivitynewlay.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   /* if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    } else if (viewPager.getCurrentItem() == 1) {
                        viewPager.setCurrentItem(2);
                    } else if (viewPager.getCurrentItem() == 2) {
                        viewPager.setCurrentItem(3);
                    } else if (viewPager.getCurrentItem() == 3) {
                        viewPager.setCurrentItem(4);
                    } else if (viewPager.getCurrentItem() == 4) {
                        viewPager.setCurrentItem(0);
                    } else {
                        viewPager.setCurrentItem(1);
                    }*/
                    //   Log.d("nipaerror","img length-"+imagelist.size());
                    if (currentPage == imagelist.size()) {
                        currentPage = 0;
                    }
                    viewPager.setCurrentItem(currentPage++, true);
                }
            });
        }
    }
}
