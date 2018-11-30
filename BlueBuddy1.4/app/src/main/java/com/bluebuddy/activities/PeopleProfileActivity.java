package com.bluebuddy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.bluebuddy.R;
import com.bluebuddy.adapters.TabsPagerAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.app.AppConfig;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.classes.ServerResponseUnf;
import com.bluebuddy.data.FriendDB;
import com.bluebuddy.data.StaticConfig;
import com.bluebuddy.fragments.AboutFragment;
import com.bluebuddy.fragments.EventFragment;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.CertificateDetail;
import com.bluebuddy.models.CommonResponse;
import com.bluebuddy.models.Friend;
import com.bluebuddy.models.ListFriend;
import com.bluebuddy.models.PeopleProfileResponse;
import com.bluebuddy.models.TripDetail;
import com.bluebuddy.models.UserDetails;
import com.bluebuddy.services.ServiceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.IMG_URL;
import static com.bluebuddy.app.AppConfig.USER_ID;


public class PeopleProfileActivity extends BuddyActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    public static Map<String, Boolean> mapMark;
    public static int ACTION_START_CHAT = 1;
    public ImageView fbimgid, twtimgid, instimgid, gglid;
    FirebaseDatabase firebaseDatabase;
    String userid, puserid;
    Button addbuddy, rqst_sent, buddy, btnRejected, chat, link;
    Activity _myactivity;
    boolean rqst = true;
    private ArrayList<String> listFriendID = null;
    private ListFriend dataListFriend = null;
    private CountDownTimer detectFriendOnline;
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsing;
    private ImageView coverImage, back;
    private ImageView proimg, scubaid, freeid;
    private FrameLayout framelayoutTitle;
    private LinearLayout linearlayoutTitle;
    private RelativeLayout chatt;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Toolbar toolbar;
    private MyTextView buddycounter, bell_counter;
    private MyTextView textviewTitle, txtLocation, txtUserName, txtUserProfession;
    private ProgressDialog mProgressDialog;

    private SharedPreferences pref;
    private String token, uid, buddy_id, Gottwt, Gotinst, Gotggl, Gotfb, bellcounter, mailid;
    private String Firebase_reg_id;
    private String profilePic;
    private String personName;
    private CertificateDetail detail1;
    private CertificateDetail detail;
    private MyTextView txtUserWebsite;

    private void setInitializeElements(String location, String userName, String userProfession, String profile_Pic, String websiteLink) {
        txtLocation.setText(location);
        txtUserName.setText(userName);

        if (websiteLink != null && !websiteLink.trim().equalsIgnoreCase("")) {
            txtUserWebsite.setVisibility(View.VISIBLE);
            txtUserWebsite.setText(websiteLink);
        } else {
            txtUserWebsite.setVisibility(View.INVISIBLE);
        }

        if (!profile_Pic.equals("")) {
            Glide.with(PeopleProfileActivity.this).asBitmap().load(IMG_URL + profile_Pic).into(new BitmapImageViewTarget(proimg) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(PeopleProfileActivity.this.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    proimg.setImageDrawable(circularBitmapDrawable);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_people_profile);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.loader();
        super.notice();
        super.setImageResource1(2);
        _myactivity = this;
        mapMark = new HashMap<>();
        initFirebase();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;
        detectFriendOnline = new CountDownTimer(System.currentTimeMillis(), StaticConfig.TIME_TO_REFRESH) {
            @Override
            public void onTick(long l) {
                ServiceUtils.updateFriendStatus(PeopleProfileActivity.this, dataListFriend);
                ServiceUtils.updateUserStatus(PeopleProfileActivity.this);
            }

            @Override
            public void onFinish() {

            }
        };
        if (dataListFriend == null) {
            dataListFriend = FriendDB.getInstance(this).getListFriend();
            if (dataListFriend.getListFriend().size() > 0) {
                listFriendID = new ArrayList<>();
                for (Friend friend : dataListFriend.getListFriend()) {
                    listFriendID.add(friend.id);
                }
                detectFriendOnline.start();
            }
        }
        if (listFriendID == null) {
            listFriendID = new ArrayList<>();
            getListFriendUId();
        }
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        uid = pref.getString(USER_ID, "");

        findViews();

        Bundle bundle = getIntent().getExtras();


        userid = bundle.getString("uid");

        //Log.d("Token Receive in cp3", userid.toString());

        if (bundle.containsKey("DATA_VALUE")) {
            puserid = bundle.getString("DATA_VALUE");
        }

        if (userid.equals(uid)) {
            addbuddy.setVisibility(View.GONE);
            chatt.setVisibility(View.GONE);
            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(PeopleProfileActivity.this, BuddyContactList.class);
                    startActivity(i);
                }
            });
        } else if (!userid.equals(uid)) {
            addbuddy.setVisibility(View.VISIBLE);
            chatt.setVisibility(View.VISIBLE);
        }
        apiUPA(userid);


        addbuddy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buddy_id = userid;
                AddBuddy(buddy_id);
            }
        });
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplication().getApplicationContext(), NotificationActivity.class);
                startActivity(i);
            }
        });
        bellcount();

        gglid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fb7 = Gotggl;
                getOpenGoogleIntent(fb7);
            }
        });

        instimgid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fb6 = Gotinst;
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

        buddy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(_myactivity);
                LayoutInflater inflater = (LayoutInflater) _myactivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.custom_popup_inputdialog2, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setMessage("Unfriend");

                dialogBuilder.setPositiveButton("Unfriend", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                        Call<ServerResponseUnf> userCall = service.unf("Bearer " + token, "post", userid);

                        userCall.enqueue(new Callback<ServerResponseUnf>() {
                            @Override
                            public void onResponse(Call<ServerResponseUnf> call, Response<ServerResponseUnf> response) {
                                if (response.body().getStatus() == "true") {
                                    Log.d("fr comment", "" + response.body().getStatus());
                                    buddy.setVisibility(View.GONE);
                                    addbuddy.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onFailure(Call<ServerResponseUnf> call, Throwable t) {
                                Log.d("onFailure", t.toString());
                                t.printStackTrace();
                            }
                        });

                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
            }
        });
    }


    private void findViews() {
        link = (Button) findViewById(R.id.link);
        chatt = (RelativeLayout) findViewById(R.id.chatt);
        chat = (Button) findViewById(R.id.chat);
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        gglid = (ImageView) findViewById(R.id.gglid);
        twtimgid = (ImageView) findViewById(R.id.twtimgid);
        instimgid = (ImageView) findViewById(R.id.instimgid);
        fbimgid = (ImageView) findViewById(R.id.fbimgid);
        buddycounter = (MyTextView) findViewById(R.id.buddycounter);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        collapsing = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        addbuddy = (Button) findViewById(R.id.addbuddy);
        rqst_sent = (Button) findViewById(R.id.btnRequestSent);
        buddy = (Button) findViewById(R.id.buddy);
        btnRejected = (Button) findViewById(R.id.btnRejected);

        linearlayoutTitle = (LinearLayout) findViewById(R.id.linearlayout_title);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");
        proimg = (ImageView) findViewById(R.id.profile_view);
        scubaid = (ImageView) findViewById(R.id.scubaid);
        freeid = (ImageView) findViewById(R.id.freeid);

        txtLocation = (MyTextView) findViewById(R.id.txtLocation);
        txtUserName = (MyTextView) findViewById(R.id.txtUserName);
        txtUserWebsite = (MyTextView) findViewById(R.id.txtUserWebsite);

        txtLocation.setTypeface(custom_font);
        txtUserName.setTypeface(custom_font);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        collapsing.setCollapsedTitleTypeface(custom_font);

        toolbar.setTitle("");
        appbar.addOnOffsetChangedListener(this);
        setSupportActionBar(toolbar);
    }

    private void getOpenGoogleIntent(String fb7) {
        Intent intent = null;
        String idgooggle = fb7;

        try {

            getPackageManager().getPackageInfo("com.google.android.apps.plus", 0);
            String url = idgooggle;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + url));
        } catch (Exception e) {
            String url = idgooggle;
            intent = new Intent(Intent.ACTION_VIEW);
            if (url.contains("https://www.youtube.com"))
                intent.setData(Uri.parse(url));
            else
                intent.setData(Uri.parse("https://www.youtube.com/channel/" + url));
        }
        this.startActivity(intent);
    }


    private void getOpenInstaGramIntent(String fb6) {
        Intent intent = null;
        String idInstagram = fb6;

        try {
            getPackageManager().getPackageInfo("com.instagram.android", 0);
            String url = idInstagram;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/" + url));
        } catch (Exception e) {
            String url = idInstagram;
            intent = new Intent(Intent.ACTION_VIEW);
            if (url.contains("https://www.instagram.com"))
                intent.setData(Uri.parse(url));
            else
                intent.setData(Uri.parse("https://www.instagram.com/" + url));
        }
        this.startActivity(intent);
    }

    private void getOpenTwitterIntent(String fb5) {
        Intent intent = null;
        String idTwitter = fb5;
        try {

            getPackageManager().getPackageInfo("com.twitter.android", 0);
            String url = idTwitter;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + url));
        } catch (Exception e) {
            String url = idTwitter;
            intent = new Intent(Intent.ACTION_VIEW);
            if (url.contains("https://www.twitter.com"))
                intent.setData(Uri.parse(url));
            else
                intent.setData(Uri.parse("https://www.twitter.com/" + url));
        }
        this.startActivity(intent);
    }

    public void getOpenFacebookIntent(String fb4) {
        Intent intent = null;
        String idFacebook = fb4;

        try {

            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            String url = idFacebook;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + url));
        } catch (Exception e) {
            String url = idFacebook;
            intent = new Intent(Intent.ACTION_VIEW);
            if (url.contains("https://www.facebook.com"))
                intent.setData(Uri.parse(url));
            else
                intent.setData(Uri.parse("https://www.facebook.com/" + url));
        }
        this.startActivity(intent);

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
        pagerAdapter.addFragment(AboutFragment.createInstance(about), "ABOUT ME");
        if (!userid.equals(uid)) {
            pagerAdapter.addFragment(EventFragment.createInstance(tripDetail, false), "TRIPS");
        } else if (userid.equals(uid)) {
            pagerAdapter.addFragment(EventFragment.createInstance(tripDetail, true, true), "TRIPS");
        }
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }


    private void apiUPA(final String userid) {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<PeopleProfileResponse> userCall = service.upa("Bearer " + token, userid);

        userCall.enqueue(new Callback<PeopleProfileResponse>() {
            @Override
            public void onResponse(Call<PeopleProfileResponse> call, Response<PeopleProfileResponse> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                if (response.code() == AppConfig.SUCCESS_STATUS) {
                    if (response.body().getStatus() == true) {

                        final UserDetails userDetails = response.body().getDetails();

                        //  Log.d("Got status", "" + response.body().getStatus());
                        ArrayList<TripDetail> tripDetail = response.body().getTrip_details();
                        ArrayList<CertificateDetail> certificateDetails = response.body().getCertification_details();

                        //  Log.d("PeopleProfileActivity", userDetails.toString());
                        String ABT = userDetails.getAbout();

                        int len = certificateDetails.size();
                        buddycounter.setText(userDetails.getBuddy_counter());
                        mailid = userDetails.getEmail();
                        Firebase_reg_id = userDetails.getFirebase_reg_id();

                        profilePic = userDetails.getProfile_pic();
                        personName = userDetails.getFirst_name() + " " + userDetails.getLast_name();

                        if (profilePic == null) {
                            profilePic = "default";
                        }

                        chat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                            findIDEmail1(mailid);
//                            FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(mailid).addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    if (dataSnapshot.getValue() != null) {
//                                        String id = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();
//
//                                        HashMap userMap = (HashMap) ((HashMap) dataSnapshot.getValue()).get(id);
//                                        Friend user = new Friend();
//
//                                        String name = (String) userMap.get("name");
//
//                                        user.email = (String) userMap.get("email");
//                                        user.avata = (String) userMap.get("avata");
//                                        user.id = id;
//                                        user.idRoom = id.compareTo(StaticConfig.UID) > 0 ? (StaticConfig.UID + id).hashCode() + "" : "" + (id + StaticConfig.UID).hashCode();
//
//                                        Intent intent = new Intent(PeopleProfileActivity.this, SingleChatActivitypeoplepro.class);
//                                        ArrayList<CharSequence> idFriend = new ArrayList<CharSequence>();
//                                        idFriend.add(user.id);
//
//                                        intent.putCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID, idFriend);
//                                        intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID, user.idRoom);
//                                        intent.putExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND, name);
//
//                                        intent.putExtra("peopleChatid", id);
//                                        intent.putExtra("people_pro_pic", userDetails.getProfile_pic());
//                                        intent.putExtra("people_name", userDetails.getFirst_name() + " " + userDetails.getLast_name());
//                                        intent.putExtra("peopl_fireapi", userDetails.getFirebase_API_key());
//
//                                        intent.putExtra("uid", userid);
//                                        mapMark.put(user.id, null);
//                                        PeopleProfileActivity.this.startActivityForResult(intent, PeopleProfileActivity.ACTION_START_CHAT);
//
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
                                Intent intent = new Intent(v.getContext(), ChatMessagingActivity.class);
                                intent.putExtra("receiver_id", Firebase_reg_id);
                                intent.putExtra("email", mailid);
                                intent.putExtra("image", profilePic);
                                intent.putExtra("name", personName);

                                v.getContext().startActivity(intent);

                            }
                        });
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
                            freeid.setVisibility(View.VISIBLE);

                            scubaid.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(PeopleProfileActivity.this, ScubaDivingCertificationDeatils.class);
                                    i.putExtra("cert_id", detail.getId());
                                    i.putExtra("cert_type", detail.getCert_type());
                                    i.putExtra("cert_name", detail.getCert_name());
                                    i.putExtra("cert_num", detail.getCert_no());
                                    i.putExtra("cert_lvl", detail.getCert_level());
                                    i.putExtra("uid", userid);
                                    i.putExtra("backkey", "peopleprofile");
                                    startActivity(i);
                                }
                            });
                            freeid.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(PeopleProfileActivity.this, ScubaDivingCertificationDeatils.class);
                                    i.putExtra("cert_id", detail1.getId());
                                    i.putExtra("cert_type", detail1.getCert_type());
                                    i.putExtra("cert_name", detail1.getCert_name());
                                    i.putExtra("cert_num", detail1.getCert_no());
                                    i.putExtra("cert_lvl", detail1.getCert_level());
                                    i.putExtra("uid", userid);
                                    i.putExtra("backkey", "peopleprofile");
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
                                        Intent i = new Intent(PeopleProfileActivity.this, ScubaDivingCertificationDeatils.class);
                                        i.putExtra("cert_id", detail.getId());
                                        i.putExtra("cert_type", detail.getCert_type());
                                        i.putExtra("cert_name", detail.getCert_name());
                                        i.putExtra("cert_num", detail.getCert_no());
                                        i.putExtra("cert_lvl", detail.getCert_level());
                                        i.putExtra("uid", userid);
                                        i.putExtra("backkey", "peopleprofile");
                                        startActivity(i);
                                    }
                                });
                            } else {
                                final CertificateDetail detail1 = certificateDetails.get(0);
                                freeid.setVisibility(View.VISIBLE);
                                freeid.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(PeopleProfileActivity.this, ScubaDivingCertificationDeatils.class);
                                        i.putExtra("cert_id", detail1.getId());
                                        i.putExtra("cert_type", detail1.getCert_type());
                                        i.putExtra("cert_name", detail1.getCert_name());
                                        i.putExtra("cert_num", detail1.getCert_no());
                                        i.putExtra("cert_lvl", detail1.getCert_level());
                                        i.putExtra("uid", userid);
                                        i.putExtra("backkey", "peopleprofile");
                                        startActivity(i);
                                    }
                                });

                            }
                        }
                        if (response.body().getIs_buddy().equals("0") && !userid.equals(uid)) {
                            rqst_sent.setVisibility(View.VISIBLE);
                            addbuddy.setVisibility(View.GONE);
                        } else if (response.body().getIs_buddy().equals("1") && !userid.equals(uid)) {
                            buddy.setVisibility(View.VISIBLE);
                            addbuddy.setVisibility(View.GONE);
                        } else if (response.body().getIs_buddy().equals("2") && !userid.equals(uid)) {
                            btnRejected.setVisibility(View.VISIBLE);
                            addbuddy.setVisibility(View.GONE);
                        } else if (response.body().getIs_buddy().equals("Add") && !userid.equals(uid)) {
                            addbuddy.setVisibility(View.VISIBLE);
                        }
                        setInitializeElements(userDetails.getLocation(), userDetails.getFirst_name() + " " + userDetails.getLast_name(), "", userDetails.getProfile_pic(), userDetails.getWebsite_link());

                        collapsing.setTitle(userDetails.getFirst_name() + " " + userDetails.getLast_name());
                        collapsing.setExpandedTitleColor(getResources().getColor(R.color.transparent));
                        collapsing.setCollapsedTitleTextColor((Color.WHITE));

                        if (userDetails.getFb_url().trim().length() > 1) {
                            fbimgid.setVisibility(View.VISIBLE);
                            Gotfb = userDetails.getFb_url();
                        }
                        if (userDetails.getTwtr_url().trim().length() > 1) {
                            twtimgid.setVisibility(View.VISIBLE);
                            Gottwt = userDetails.getTwtr_url();
                        }
                        if (userDetails.getIngm_url().trim().length() > 1) {
                            instimgid.setVisibility(View.VISIBLE);
                            Gotinst = userDetails.getIngm_url();
                        }
                        if (userDetails.getGgle_url().trim().length() > 1) {
                            gglid.setVisibility(View.VISIBLE);
                            Gotggl = userDetails.getGgle_url();
                        }
                        /* if (userDetails.getFb_url() != "" || userDetails.getTwtr_url() != "" || userDetails.getIngm_url() != "" || userDetails.getGgle_url() != "") {
                         *//* Gotfb = userDetails.getFb_url().toString();
                        Gottwt = userDetails.getTwtr_url().toString();
                        Gotinst = userDetails.getIngm_url().toString();
                        Gotggl = userDetails.getGgle_url().toString();*//*
                        Gotfb = userDetails.getFb_url();
                        Gottwt = userDetails.getTwtr_url();
                        Gotinst = userDetails.getIngm_url();
                        Gotggl = userDetails.getGgle_url();
                    }*/

                        initViewPagerAndTabs(ABT, tripDetail);
                    } else {
                        Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
                    }


                } else if (response.code() == AppConfig.ERROR_STATUS_LOG_AGAIN) {
                    // login again
                    logoutAndSignInPage(_myactivity);

                } else {
                    Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PeopleProfileResponse> call, Throwable t) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                //  Log.d("onFailure", t.toString());
            }
        });

    }

    private void AddBuddy(String buddy_id) {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<CommonResponse> userCall = service.addBuddy("Bearer " + token, buddy_id);

        userCall.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                //  Log.d("STATUS", String.valueOf(response.body().getStatus()));
                if (response.code() == AppConfig.SUCCESS_STATUS) {
                    if (response.body().getStatus() == true) {
                        openDialog("Invitation sent successfully", "", "Ok", false, _myactivity, "", 0);
                        rqst_sent.setVisibility(View.VISIBLE);
                        addbuddy.setVisibility(View.GONE);
                        rqst = false;

                    } else if (response.body().getStatus() == false) {
                        openDialog(response.body().getMessage(), "", "Ok", false, _myactivity, "", 0);
                    }
                } else if (response.code() == AppConfig.ERROR_STATUS_LOG_AGAIN) {
                    // login again
                    logoutAndSignInPage(_myactivity);

                } else {
                    Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), getApplication().getText(R.string.goeswrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    public void openDialog11(String msg, String btnTxt, String btnTxt2, final boolean _redirect, final Activity activity, final String _redirectClass, int dialogLayout, final String data) {
        super.openDialog11(msg, btnTxt, btnTxt2, _redirect, activity, _redirectClass, dialogLayout, data);

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


        if (listFriendID.contains(idFriend)) {

        } else {
            addFriend(idFriend, true);
            listFriendID.add(idFriend);
            dataListFriend.getListFriend().add(userInfo);
            FriendDB.getInstance(this).addFriend(userInfo);
            FriendDB.getInstance(PeopleProfileActivity.this).addFriend(userInfo);
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
                    PeopleProfileActivity.this.finish();
                    startActivity(new Intent(PeopleProfileActivity.this, LoginActivity.class));
                    Log.d("STATUS", "onAuthStateChanged:signed_out");
                }
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