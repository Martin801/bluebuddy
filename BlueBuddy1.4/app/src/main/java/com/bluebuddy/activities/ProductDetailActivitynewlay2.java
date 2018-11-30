package com.bluebuddy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.bluebuddy.R;
import com.bluebuddy.adapters.CustomAdapterforProDetails;
import com.bluebuddy.adapters.ReviewRecyclerAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.data.StaticConfig;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.Friend;
import com.bluebuddy.models.ProductDetail;
import com.bluebuddy.models.ProductDetailResponse;
import com.bluebuddy.models.ProductImage;
import com.bluebuddy.models.ReviewDetail;
import com.bluebuddy.models.ReviewDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.FIRE_REG_ID;

public class ProductDetailActivitynewlay2 extends BuddyActivity {

    public static Map<String, Boolean> mapMark;
    private static int currentpage = 0;
    public RatingBar ratingBar1;
    ViewPager viewPager;
    RatingBar ratingBar;
    Activity _activity;
    FirebaseDatabase firebaseDatabase;
    LinearLayout linearlayout_title;
    Button btnShowHideReview;
    boolean isShowReview = false;
    LinearLayout ll_reviewList;
    TextView tv_no_review;
    int currentPage = 0;
    private EditText rating_title, desc;
    private CustomAdapterforProDetails adapter;
    private CirclePageIndicator indicator;
    private MyTextView pro_name, pro_desc, pro_pname, pro_pmail, pro_pnum, bell_counter;
    private TextView pro_price;
    private String rating_value, description, rvw_title;
    private String image1, image2, image3, image4 = "";
    private ArrayList<String> imagelist = new ArrayList<>();
    private Context _context;
    private CardView rvwcard;
    private SharedPreferences pref;
    private String token, bellcounter;
    private Button button2, rate_and_rvw, button5;
    private boolean _isShow = false;
    private LinearLayout linearReviewProduct;
    private String id, uid = "", cat;
    private RecyclerView reviewRecycler;
    private ReviewRecyclerAdapter reviewRecyclerAdapter;
    private ImageView back, feattag;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_product);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.loader();
        super.notice();
        _context = this;
        _activity = this;
        initializeElements();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;
        super.setTitle("PRODUCT DETAILS");
        _activity = this;
        mapMark = new HashMap<>();

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        uid = pref.getString(FIRE_REG_ID, "");
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("AllProduct");
        final String AllProduct_Firebaseid = bundle.getString("AllProduct_Firebaseid");
        final String AllProduct_FirebaseEmail = bundle.getString("AllProduct_FirebaseEmail");
        Log.d("Token Receive product", id.toString());
        button5 = (Button) findViewById(R.id.button5);
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        button2 = (Button) findViewById(R.id.button2);
        apiproductdetail(id);
        if (AllProduct_Firebaseid.equals(uid)) {
            button5.setVisibility(View.GONE);
            rvwcard.setVisibility(View.GONE);
        }
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findIDEmail(AllProduct_FirebaseEmail);
            }
        });

        ratingBar1 = (RatingBar) findViewById(R.id.ratingBar1);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        linearReviewProduct = (LinearLayout) findViewById(R.id.linearReviewProduct);
        rating_title = (EditText) findViewById(R.id.rating_title);
        desc = (EditText) findViewById(R.id.desc);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating_value = String.valueOf(ratingBar.getRating()).trim();
                Toast.makeText(ProductDetailActivitynewlay2.this, rating_value, Toast.LENGTH_LONG).show();
            }
        });
        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent i = new Intent(ProductDetailActivitynewlay2.this, Categories_bluemarketActivity2.class);
                startActivity(i);*/
                backPressCall();
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
                Call<ReviewDetail> userCall = service.review("Bearer " + token, rvw_title, description, rating_value, "product", id);
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

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bellcount();
    }

    void callForReviewChange(ReviewDetails reviewDetails) {
        rvwcard.setVisibility(View.GONE);
        isShowReview = true;
        reviewRecyclerAdapter.addReview(reviewDetails);
        tv_no_review.setVisibility(View.GONE);
        reviewRecycler.setVisibility(View.VISIBLE);
        checkForReviewString();
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
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<ProductDetailResponse> userCall = service.productDt("Bearer " + token, id);

        userCall.enqueue(new Callback<ProductDetailResponse>() {
            @Override
            public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                if (response.body() != null && response.body().getStatus() == true) {

                   /* Log.d("Got status", "" + response.body().getStatus());
                    Log.d("IMAGE:", response.body().getDetails().getImage());*/
//                    String images = response.body().getDetails().getImage();
//
//                    String img[] = images.split(",");
//                    int imgLen = img == null ? -1 : img.length;
//                    imagelist = Arrays.asList(img);
//
//                    if (imgLen >= 0) {
//                        for (int ij = 0; ij < imgLen; ij++) {
//                           // Log.d("sssssss", img[ij]);
//                        }
//                    }

                    if (!response.body().getDetails().getProductImages().isEmpty()) {

                        for (ProductImage productImage : response.body().getDetails().getProductImages()) {
                            imagelist.add(productImage.getImage());
                        }

                    }

                    setImageViewAdapter(_activity, _context, imagelist);

                    ProductDetail details = response.body().getDetails();
                    //    List<ReviewDetails> p = response.body().getReview();
                    // nipa added code
                    if (response.body().getReview() != null && response.body().getReview().size() > 0) {
                        tv_no_review.setVisibility(View.GONE);
                        //  reviewRecyclerAdapter.updateAllReview(response.body().getReview(),uid,"product", id,cat,_context);
                        reviewRecycler.setVisibility(View.VISIBLE);

                    } else {
                        reviewRecycler.setVisibility(View.GONE);
                        tv_no_review.setVisibility(View.VISIBLE);
                    }
                    reviewRecyclerAdapter.updateAllReview(response.body().getReview(), uid, "product", id, cat, _context);

                    if (details != null) {
                        // np add
                        if (!details.getIsReviewed().matches("1")) {
                            rvwcard.setVisibility(View.VISIBLE);
                        } else {
                            rvwcard.setVisibility(View.GONE);
                        }
                        float rating = Float.parseFloat(String.valueOf(details.getRating()));
                        //Log.d("rate:", details.getRating());
                        ratingBar1.setRating(rating);
                        pro_name.setText(details.getName());
                        pro_desc.setText(details.getDescription());
                        pro_price.setText("$" + details.getPrice());
                        pro_pname.setText(details.getFname() + " " + details.getLname());
                        pro_pmail.setText(details.getEmail());
                        pro_pnum.setText(details.getPhone());


                    }
                    if (details.getFeatured().equals("1")) {
                        feattag.setVisibility(View.VISIBLE);
                    } else if (details.getFeatured().equals("0")) {
                        feattag.setVisibility(View.GONE);
                    }
                   /* if (p != null && p.size() > 0) {
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
                Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
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
        super.onBackPressed();
        backPressCall();
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

                    Intent intent = new Intent(ProductDetailActivitynewlay2.this, ChatActivity2.class);
                    ArrayList<CharSequence> idFriend = new ArrayList<CharSequence>();
                    idFriend.add(user.id);

                    intent.putCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID, idFriend);
                    intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID, user.idRoom);
                    intent.putExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND, name);

                    mapMark.put(user.id, null);
                    _activity.startActivityForResult(intent, Blue_Charter_Details.ACTION_START_CHAT);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void backPressCall() {
        Intent i = new Intent(ProductDetailActivitynewlay2.this, MyListingActivity.class);
        startActivity(i);
    }

    public class TimerSlide extends TimerTask {

        @Override
        public void run() {
            ProductDetailActivitynewlay2.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /*if (viewPager.getCurrentItem() == 0) {
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
                        viewPager.setCurrentItem(0);
                    }*/
                    if (currentPage == imagelist.size()) {
                        currentPage = 0;
                    }
                    viewPager.setCurrentItem(currentPage++, true);
                }
            });
        }
    }
}
