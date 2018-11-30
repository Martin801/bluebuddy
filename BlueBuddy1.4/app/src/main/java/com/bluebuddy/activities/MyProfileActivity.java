package com.bluebuddy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bluebuddy.R;
import com.bluebuddy.adapters.TabsPagerAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.app.AppConfig;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.fragments.AboutFragment;
import com.bluebuddy.fragments.MyProEventFragment;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AfterLoginStatus;
import com.bluebuddy.models.CertificateDetail;
import com.bluebuddy.models.ProfileDetails;
import com.bluebuddy.models.TripDetail;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.IMG_URL;

public class MyProfileActivity extends BuddyActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final String TAG = "MyProfileActivity";
    public ImageView notice, fbimgid, twtimgid, instimgid, gglid;
    MyTextView txtUserName, txtLocation, tx, txtUserWebsite, buddy_counter, bell_counter;
    Button noticebell, link;
    int backButtonCount;
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    private AppBarLayout appbar;
    private RelativeLayout bell;
    private CollapsingToolbarLayout collapsing;
    private ImageView proimg, scubaid, freeid;
    private Toolbar toolbar;
    private LinearLayout linearlayoutTitle;
    private String tvbtname1, bellcounter;
    private SharedPreferences pref;
    private String token, Gotfb, fb3, Gottwt, Gotinst, Gotggl;
    private ProgressDialog mProgressDialog;
    private Activity _activity;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    private CertificateDetail detail;
    private CertificateDetail detail1;
// [END declare_auth]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_my_profile);
        super.onCreate(savedInstanceState);

        super.initialize();
        super.setImageResource1(5);
        super.loader();
        super.notice();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
// [END initialize_auth]

        _activity = this;
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        findViews();
        apiMPA();
        bellcount();
        toolbar.setTitle("");
        appbar.addOnOffsetChangedListener(this);
        setSupportActionBar(toolbar);

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyProfileActivity.this, BuddyContactList.class);
                startActivity(i);
               /* startActivity(i, ActivityOptions.makeSceneTransitionAnimation(_activity).toBundle());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                   _activity.finishAfterTransition();
                } else _activity.finish();*/
            }
        });

        bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplication().getApplicationContext(), NotificationActivity.class);
                startActivity(i);
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplication().getApplicationContext(), NotificationActivity.class);
                startActivity(i);
            }
        });

        gglid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fb7 = Gotggl;
                // go to website defined above
                getOpenGoogleIntent(fb7);
            }
        });

        instimgid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fb6 = Gotinst;
//                mCustomTabsIntent.launchUrl(MyProfileActivity.this, Uri.parse(fb6));
                getOpenInstaGramIntent(fb6);
            }
        });

        twtimgid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fb5 = Gottwt;
                getOpenTwitterIntent(fb5);
            }
        });

        fbimgid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fb4 = Gotfb;
                getOpenFacebookIntent(fb4);
            }
        });

// Add code to print out the key hash
      /*  try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.bluebuddy",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
*/
    }

    private void getOpenGoogleIntent(String fb7) {
        Intent intent = null;
        String idgooggle = fb7;
        try {
            getPackageManager().getPackageInfo("com.google.android.apps.plus", 0);
            String url = idgooggle;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + url));
            startActivity(intent);
        } catch (Exception e) {
            String url = idgooggle;
            intent = new Intent(Intent.ACTION_VIEW);
            if (url.contains("https://www.youtube.com"))
                intent.setData(Uri.parse(url));
            else
                intent.setData(Uri.parse("https://www.youtube.com/channel/" + url));
            startActivity(intent);
        }
    }

    private void getOpenInstaGramIntent(String fb6) {
        Intent intent = null;
        String idInstagram = fb6;

        try {

            getPackageManager().getPackageInfo("com.instagram.android", 0);
            String url = idInstagram;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/" + url));
            startActivity(intent);
        } catch (Exception e) {
            String url = idInstagram;
            intent = new Intent(Intent.ACTION_VIEW);
            if (url.contains("https://www.instagram.com"))
                intent.setData(Uri.parse(url));
            else
                intent.setData(Uri.parse("https://www.instagram.com/" + url));

            startActivity(intent);
        }
    }

    private void getOpenTwitterIntent(String fb5) {

        Intent intent = null;
        String idTwitter = fb5;
        try {

            getPackageManager().getPackageInfo("com.twitter.android", 0);
            String url = idTwitter;

            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + url));
            startActivity(intent);
        } catch (Exception e) {
            String url = idTwitter;
            intent = new Intent(Intent.ACTION_VIEW);
            if (url.contains("https://www.twitter.com"))
                intent.setData(Uri.parse(url));
            else
                intent.setData(Uri.parse("https://www.twitter.com/" + url));
            startActivity(intent);
        }
    }

    public void getOpenFacebookIntent(String fb4) {
        Intent intent = null;
        String idFacebook = fb4;

        try {

            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            String url = idFacebook;

            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + url));
            startActivity(intent);
        } catch (Exception e) {
            String url = idFacebook;
            intent = new Intent(Intent.ACTION_VIEW);
            if (url.contains("https://www.facebook.com"))
                intent.setData(Uri.parse(url));
            else
                intent.setData(Uri.parse("https://www.facebook.com/" + url));
            startActivity(intent);
        }
    }

    private void findViews() {
        link = (Button) findViewById(R.id.link);
        bell = (RelativeLayout) findViewById(R.id.bell);
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        gglid = (ImageView) findViewById(R.id.gglid);
        twtimgid = (ImageView) findViewById(R.id.twtimgid);
        instimgid = (ImageView) findViewById(R.id.instimgid);
        fbimgid = (ImageView) findViewById(R.id.fbimgid);
        buddy_counter = (MyTextView) findViewById(R.id.buddy_counter);
        noticebell = (Button) findViewById(R.id.noticebell);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        collapsing = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        notice = (ImageView) findViewById(R.id.imgNotification);
        linearlayoutTitle = (LinearLayout) findViewById(R.id.linearlayout_title);

        txtLocation = (MyTextView) findViewById(R.id.txtLocation);
        txtUserName = (MyTextView) findViewById(R.id.txtUserName);
        txtUserWebsite = (MyTextView) findViewById(R.id.txtUserWebsite);
        tx = (MyTextView) findViewById(R.id.txtUserName);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");
        tx.setTypeface(custom_font);
        proimg = (ImageView) findViewById(R.id.profile_view);
        scubaid = (ImageView) findViewById(R.id.scubaid);
        freeid = (ImageView) findViewById(R.id.freeid);
        MyTextView IDtxt2 = (MyTextView) findViewById(R.id.txtLocation);
        MyTextView IDtxt4 = (MyTextView) findViewById(R.id.txtBuddies);
        IDtxt2.setTypeface(custom_font);
        IDtxt4.setTypeface(custom_font);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        tvbtname1 = tx.getText().toString().trim().toUpperCase();
        String title = tvbtname1.toUpperCase();

    }

    private void bellcount() {

        general_bell_counter(token, new BellCounterInterface() {
            @Override
            public void getBellCount(String bellcounter) {
                if (bellcounter != null) {
                    if (bellcounter.equals("0")) {
                        bell_counter.setVisibility(View.GONE);
                    } else if (!bellcounter.equals("0")) {
                        bell_counter.setVisibility(View.VISIBLE);
                    }
                }
                bell_counter.setText(bellcounter);
            }
        });

      /*  ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllNotice> userCall = service.allnotice1("Bearer " + token);

        userCall.enqueue(new Callback<AllNotice>() {
            @Override
            public void onResponse(Call<AllNotice> call, Response<AllNotice> response) {
               // Log.d("STATUS", String.valueOf(response.body().getStatus()));
               // Log.d("onResponse:", "" + response.body().getNotification_details());
                bellcounter = response.body().getCounter();
                if (bellcounter != null) {
                    if (bellcounter.equals("0")) {
                        bell_counter.setVisibility(View.GONE);
                    } else if (!bellcounter.equals("0")) {
                        bell_counter.setVisibility(View.VISIBLE);
                    }
                }
                bell_counter.setText(response.body().getCounter());

            }

            @Override
            public void onFailure(Call<AllNotice> call, Throwable t) {

            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;
        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
                mIsTheTitleVisible = true;
            }
        } else {
            if (mIsTheTitleVisible) {
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                mIsTheTitleContainerVisible = false;
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    private void initViewPagerAndTabs(String about, ArrayList<TripDetail> tripDetail) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        TabsPagerAdapter pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(AboutFragment.createInstance(about), "ABOUT MYSELF");
        pagerAdapter.addFragment(MyProEventFragment.createInstance(tripDetail), "TRIPS");
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void apiMPA() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AfterLoginStatus> userCall = service.mpa("Bearer " + token);

        userCall.enqueue(new Callback<AfterLoginStatus>() {
            @Override
            public void onResponse(Call<AfterLoginStatus> call, Response<AfterLoginStatus> response) {
                //Log.d("nipaError", String.valueOf(response.code()));

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                if (response.code() == AppConfig.SUCCESS_STATUS) {
                    if (response.body().getStatus() == true) {
                        ProfileDetails p = response.body().getProfile_details();

                        initNewUserInfo(p);

                        Log.d(TAG, "onResponse: " + p.toString());

                        ArrayList<TripDetail> tripDetail = response.body().getTrip_details();
                        final ArrayList<CertificateDetail> certificateDetails = response.body().getCertification_details();

                        String certList = "";
                        int len = certificateDetails.size();

                        int position = 0;

                        if (len == 2) {

                            if (certificateDetails.get(0).getCert_type().equals("Scuba_Diving")) {
                                detail = certificateDetails.get(0);
                            } else {
                                detail1 = certificateDetails.get(0);
                            }

                            if (certificateDetails.get(1).getCert_type().equals("Free_Diving")) {
                                detail1 = certificateDetails.get(1);
                            } else {
                                detail = certificateDetails.get(1);
                            }

                            scubaid.setVisibility(View.VISIBLE);
                            scubaid.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(MyProfileActivity.this, ScubaDivingCertificationDeatils.class);
                                    i.putExtra("cert_id", detail.getId());
                                    i.putExtra("cert_type", detail.getCert_type());
                                    i.putExtra("cert_name", detail.getCert_name());
                                    i.putExtra("cert_num", detail.getCert_no());
                                    i.putExtra("cert_lvl", detail.getCert_level());
                                    i.putExtra("backkey", "myprofile");
                                    startActivity(i);
                                }
                            });
                            freeid.setVisibility(View.VISIBLE);
                            freeid.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(MyProfileActivity.this, ScubaDivingCertificationDeatils.class);
                                    i.putExtra("cert_id", detail1.getId());
                                    i.putExtra("cert_type", detail1.getCert_type());
                                    i.putExtra("cert_name", detail1.getCert_name());
                                    i.putExtra("cert_num", detail1.getCert_no());
                                    i.putExtra("cert_lvl", detail1.getCert_level());
                                    i.putExtra("backkey", "myprofile");
                                    startActivity(i);
                                }
                            });

                        } else if (len == 1) {

                            final CertificateDetail detail = certificateDetails.get(0);
                            if (certificateDetails.get(0).getCert_type().equals("Scuba_Diving")) {
                                scubaid.setVisibility(View.VISIBLE);
                                scubaid.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(MyProfileActivity.this, ScubaDivingCertificationDeatils.class);
                                        i.putExtra("cert_id", detail.getId());
                                        i.putExtra("cert_type", detail.getCert_type());
                                        i.putExtra("cert_name", detail.getCert_name());
                                        i.putExtra("cert_num", detail.getCert_no());
                                        i.putExtra("cert_lvl", detail.getCert_level());
                                        i.putExtra("backkey", "myprofile");
                                        startActivity(i);
                                    }
                                });
                            } else {
                                final CertificateDetail detail1 = certificateDetails.get(0);
                                freeid.setVisibility(View.VISIBLE);
                                freeid.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(MyProfileActivity.this, ScubaDivingCertificationDeatils.class);
                                        i.putExtra("cert_id", detail1.getId());
                                        i.putExtra("cert_type", detail1.getCert_type());
                                        i.putExtra("cert_name", detail1.getCert_name());
                                        i.putExtra("cert_num", detail1.getCert_no());
                                        i.putExtra("cert_lvl", detail1.getCert_level());
                                        i.putExtra("backkey", "myprofile");
                                        startActivity(i);
                                    }
                                });
                            }
                        }

                        if (p != null) {
                            String ABT = p.getAbout();
                            String profile_Pic = p.getProfile_pic();
                            if (mProgressDialog.isShowing())
                                mProgressDialog.dismiss();

                            Glide.with(MyProfileActivity.this).asBitmap().load(IMG_URL + profile_Pic).into(new BitmapImageViewTarget(proimg) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(MyProfileActivity.this.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    proimg.setImageDrawable(circularBitmapDrawable);
                                }
                            });

                            collapsing.setTitle(p.getFirst_name() + " " + p.getLast_name());
                            collapsing.setExpandedTitleColor(getResources().getColor(R.color.transparent));
                            collapsing.setCollapsedTitleTextColor((Color.WHITE));

                            txtUserName.setText(p.getFirst_name() + " " + p.getLast_name());
                            txtLocation.setText(p.getLocation());
                            if (!p.getWebsite_link().trim().equalsIgnoreCase("")) {
                                txtUserWebsite.setVisibility(View.VISIBLE);
                                txtUserWebsite.setText(p.getWebsite_link() + "");
                            } else {
                                txtUserWebsite.setVisibility(View.GONE);
                            }

                            if (!p.getFb_url().trim().matches("")) {
                                fbimgid.setVisibility(View.VISIBLE);
                                Gotfb = p.getFb_url().toString();
                            }
                            if (!p.getTwtr_url().trim().matches("")) {
                                twtimgid.setVisibility(View.VISIBLE);
                                Gottwt = p.getTwtr_url().toString();
                            }
                            if (!p.getIngm_url().trim().matches("")) {
                                Gotinst = p.getIngm_url().toString();
                                instimgid.setVisibility(View.VISIBLE);
                            }
                            if (!p.getGgle_url().trim().matches("")) {
                                Gotggl = p.getGgle_url().toString();
                                gglid.setVisibility(View.VISIBLE);
                            }

                            buddy_counter.setText(p.getBuddy_counter());
                            initViewPagerAndTabs(ABT, tripDetail);
                        }
                    } else {

                    }
                } else if (response.code() == AppConfig.ERROR_STATUS_LOG_AGAIN) {
                    // login again
                    logoutAndSignInPage(_activity);

                } else {
                    Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AfterLoginStatus> call, Throwable t) {

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
            }
        });

    }

    void initNewUserInfo(ProfileDetails userDetails) {
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

//        User newUser = new User();

            HashMap<String, String> updateUser = new HashMap<>();
            updateUser.put("email", user.getEmail());
            updateUser.put("name", userDetails.getFirst_name() + " " + userDetails.getLast_name());

            if (userDetails.getProfile_pic().isEmpty())
                updateUser.put("image", userDetails.getProfile_pic());
            else
                updateUser.put("image", "default");

            FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("credentials").setValue(updateUser);
        } else {
            logoutAndSignInPage(this);
        }
    }

    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}