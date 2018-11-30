package com.bluebuddy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bluebuddy.R;
import com.bluebuddy.adapters.ReviewRecyclerAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.data.StaticConfig;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.CourseDetail;
import com.bluebuddy.models.CourseDetailResponse;
import com.bluebuddy.models.Friend;
import com.bluebuddy.models.ReviewDetail;
import com.bluebuddy.models.ReviewDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.FIRE_REG_ID;
import static com.bluebuddy.app.AppConfig.IMG_URL;

public class CourseDetailsActivitynewlay extends BuddyActivity {
    private Activity _activity;
    private Context _context;
    private ImageView back, corimg, feattag;
    private MyTextView agency_name, course_title, agency_website, courses_descrip, course_price, course_location;
    private MyTextView course_duration, bell_counter;
    private EditText desc, rating_title;
    private SharedPreferences pref;
    private String token, uid, rating_value, description, bellcounter, rvw_title, AllCourseId = "";
    private Button buttonmsg, button2, rate_and_rvw;
    public static Map<String, Boolean> mapMark;
    private RatingBar ratingBar;
    private boolean _isShow = false;
    private LinearLayout linearReviewProduct;
    private CardView rvwcard;
    String cat;
    public static int ACTION_START_CHAT = 1;

    private ProgressDialog mProgressDialog;

    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    private String id;
    private RecyclerView reviewRecycler;
    private ReviewRecyclerAdapter reviewRecyclerAdapter;

    LinearLayout linearlayout_title;
    Button btnShowHideReview;
    boolean isShowReview=false;
    LinearLayout ll_reviewList;
    TextView tv_no_review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_course_details_activitynewlay);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.setTitle("COURSE DETAILS");
        super.loader();
        super.notice();
        mapMark = new HashMap<>();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;
        initializeElements();
        _activity = this;
        _context = this;
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        uid = pref.getString(FIRE_REG_ID, "");
        Bundle bundle = getIntent().getExtras();
        final String id = bundle.getString("AllCourse");
        AllCourseId = id;
        cat = bundle.getString("category");


        final String AllCourse_Firebaseid = bundle.getString("AllCourse_Firebaseid");
        final String AllCourse_FirebaseEmail = bundle.getString("AllCourse_FirebaseEmail");
       // Log.d("Token Receive in course", id.toString());

        //Log.d("id Receive courses", id.toString());
        initFirebase();
        if (AllCourse_Firebaseid.equals(uid)) {
            buttonmsg.setVisibility(View.GONE);
            rvwcard.setVisibility(View.GONE);
        } else if (AllCourse_Firebaseid.equals("")) {
            buttonmsg.setVisibility(View.GONE);
            rvwcard.setVisibility(View.GONE);
        }

        apicoursedetail(id);
        buttonmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findIDEmail(AllCourse_FirebaseEmail);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent ii = new Intent(CourseDetailsActivitynewlay.this, BlueMarketActivity32.class);
                ii.putExtra("category", cat);
                Log.d("details back", cat);
                startActivity(ii);*/
                backPressCall();

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

        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
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
                Call<ReviewDetail> userCall = service.review("Bearer " + token, rvw_title, description, rating_value, "course", id);
                userCall.enqueue(new Callback<ReviewDetail>() {
                    @Override
                    public void onResponse(Call<ReviewDetail> call, Response<ReviewDetail> response) {
                        if (response.code() == 200) {
                        if(response.body().getReview().get(0)!=null){
                            callForReviewChange(response.body().getReview().get(0));}
                            oDialog(response.body().getMessage(), "Ok", "", false, _activity, "", 0, "");
                        } else {
                            Toast.makeText(getApplicationContext(),getApplication().getText(R.string.goeswrong),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewDetail> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(getApplicationContext(),getApplication().getText(R.string.goeswrong),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        bellcount();
    }
    void callForReviewChange(ReviewDetails reviewDetails){
        rvwcard.setVisibility(View.GONE);
        isShowReview=true;
        reviewRecyclerAdapter.addReview(reviewDetails);
        tv_no_review.setVisibility(View.GONE);
        reviewRecycler.setVisibility(View.VISIBLE);
        checkForReviewString();
    }
    private void initializeElements() {
        feattag = (ImageView) findViewById(R.id.feattag);
        corimg = (ImageView) findViewById(R.id.corimg);
        rvwcard = (CardView) findViewById(R.id.rvwcard);
        button2 = (Button) findViewById(R.id.button2);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        linearReviewProduct = (LinearLayout) findViewById(R.id.linearReviewProduct);
        rating_title = (EditText) findViewById(R.id.rating_title);
        desc = (EditText) findViewById(R.id.desc);
        rate_and_rvw = (Button) findViewById(R.id.rate_and_rvw);
        back = (ImageView) findViewById(R.id.imgNotification2);
        agency_name = (MyTextView) findViewById(R.id.cname);
        course_title = (MyTextView) findViewById(R.id.ctitle);
        courses_descrip = (MyTextView) findViewById(R.id.cdesc);
        course_price = (MyTextView) findViewById(R.id.cprice);
        course_location = (MyTextView) findViewById(R.id.cloc);
        course_duration = (MyTextView) findViewById(R.id.cdays);
        reviewRecycler = (RecyclerView) findViewById(R.id.reviewRecycler);
        reviewRecycler.setLayoutManager(new LinearLayoutManager(this));
        reviewRecyclerAdapter = new ReviewRecyclerAdapter(_activity, _context, null);
        reviewRecycler.setAdapter(reviewRecyclerAdapter);
        buttonmsg = (Button) findViewById(R.id.button5);

        tv_no_review=(TextView) findViewById(R.id.tv_no_review);
        ll_reviewList=(LinearLayout) findViewById(R.id.ll_reviewList);
        linearlayout_title = (LinearLayout) findViewById(R.id.linearlayout_title);
        btnShowHideReview=(Button) findViewById(R.id.btnShowHideReview);
        checkForReviewString();
        btnShowHideReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isShowReview){
                    isShowReview=false;
                }else {
                    isShowReview=true;

                }
                checkForReviewString();

            }
        });
    }

    void  checkForReviewString(){
        if(isShowReview){
            btnShowHideReview.setText(getString(R.string.hide_all_reviews));
            ll_reviewList.setVisibility(View.VISIBLE);
        }else {
            btnShowHideReview.setText(getString(R.string.view_all_reviews));
            ll_reviewList.setVisibility(View.GONE);
        }

    }

    private void apicoursedetail(final String id) {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<CourseDetailResponse> userCall = service.courseDt("Bearer " + token, id);

        userCall.enqueue(new Callback<CourseDetailResponse>() {
            @Override
            public void onResponse(Call<CourseDetailResponse> call, Response<CourseDetailResponse> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                if (response.body()!=null && response.body().getStatus() == true) {
                  //  Log.d("Got status", "" + response.body().getStatus());
                    CourseDetail details = response.body().getDetails();
                    List<ReviewDetails> p = response.body().getReview();
                    // nipa added code
                    if(response.body().getReview().size()>0) {
                        tv_no_review.setVisibility(View.GONE);
                       // reviewRecyclerAdapter.updateAllReview(response.body().getReview(),uid,"course",id,cat,_context);
                        reviewRecycler.setVisibility(View.VISIBLE);

                    }else {
                        reviewRecycler.setVisibility(View.GONE);
                        tv_no_review.setVisibility(View.VISIBLE);
                    }
                    reviewRecyclerAdapter.updateAllReview(response.body().getReview(),uid,"course",id,cat,_context);

                    if (details != null) {
                        // np add
                        if(!details.getIs_reviewed().matches("1")){
                            rvwcard.setVisibility(View.VISIBLE);
                        }else {
                            rvwcard.setVisibility(View.GONE);
                        }
                        agency_name.setText(details.getAgency_name());
                        courses_descrip.setText(details.getDescription());
                        course_price.setText("$" + details.getPrice());
                        course_location.setText(details.getLocation());
                        course_duration.setText(details.getDuration());

                        if (details.getCategory().contains(":")) {
                            String[] separated = details.getCategory().split(":");
                            separated[1] = separated[1].trim();
                            course_title.setText(separated[1]);
                        } else if (!details.getCategory().contains(":")) {
                            course_title.setText(details.getCategory());
                        }

                        String profile_Pic = details.getImage();
                        Glide.with(CourseDetailsActivitynewlay.this).asBitmap().load(IMG_URL + profile_Pic).into(new BitmapImageViewTarget(corimg));
                        if (details.getFeatured().equals("1")) {
                            feattag.setVisibility(View.VISIBLE);
                        } else if (details.getFeatured().equals("0")) {
                            feattag.setVisibility(View.GONE);
                        }
                    }
/*
                    if (p != null && p.size() > 0) {
                    for (int ir = 0; ir < p.size(); ir++) {
                    }
                } else {
                }*/

            } else {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),getApplication().getText(R.string.goeswrong),Toast.LENGTH_SHORT).show();
            }
            }

            @Override
            public void onFailure(Call<CourseDetailResponse> call, Throwable t) {

                Toast.makeText(getApplicationContext(),getApplication().getText(R.string.goeswrong),Toast.LENGTH_SHORT).show();
                Log.d("onFailure", t.toString());
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

     /*   Intent ii = new Intent(CourseDetailsActivitynewlay.this, BlueMarketActivity32.class);
        ii.putExtra("category", cat);
        Log.d("details back", cat);
        startActivity(ii);*/
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

                    Intent intent = new Intent(CourseDetailsActivitynewlay.this, SingleChatActivity.class);
                    ArrayList<CharSequence> idFriend = new ArrayList<CharSequence>();
                    idFriend.add(user.id);

                    intent.putExtra("AllCharter", AllCourseId);
                    intent.putExtra("email", email);

                    intent.putCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID, idFriend);
                    intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID, user.idRoom);
                    intent.putExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND, name);

                    mapMark.put(user.id, null);
                    _activity.startActivityForResult(intent, CourseDetailsActivitynewlay.ACTION_START_CHAT);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                    CourseDetailsActivitynewlay.this.finish();
                    // User is signed in
                    startActivity(new Intent(CourseDetailsActivitynewlay.this, LoginActivity.class));
                    Log.d("STATUS", "onAuthStateChanged:signed_out");
                }

            }
        };
    }

    void  backPressCall(){
        Intent i = new Intent(CourseDetailsActivitynewlay.this, MyListingActivity.class);
        startActivity(i);
    }
}
