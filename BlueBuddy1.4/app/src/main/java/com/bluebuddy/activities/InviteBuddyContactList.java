package com.bluebuddy.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.bluebuddy.R;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.BadgeDetail;
import com.bluebuddy.models.CommonResponseforBuddy;
import com.bluebuddy.models.EventDetails;
import com.bluebuddy.models.InviteBuddyDetail;
import com.bluebuddy.models.InviteBuddyResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.IMG_URL;

public class InviteBuddyContactList extends BuddyActivity {
    ArrayList<InviteBuddyDetail> buddyUIDSes = new ArrayList<InviteBuddyDetail>();
    private RecyclerView recycler_view_event;
    private InviteBuddyContactListAdapter inviteBuddyContactListAdapter;
    private Activity _activity;
    private Context _context;
    private SharedPreferences pref;
    private String token, bellcounter, trip_id;
    private LinearLayoutManager mLayoutManager;
    private EventDetails eventDetails;
    private ImageView back;
    private MyTextView navtxt;
    private EditText srch;
    private MyTextView bell_counter;
    private Button cntlstinvt;
    private boolean normal_backpress = false;
    //private ArrayList<BuddyUIDS> buddyUIDSes = new ArrayList<BuddyUIDS>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.invite_activity_buddy_contact_list);

        super.onCreate(savedInstanceState);

        super.initialize();

        super.notice();

        super.setImageResource1(5);

        super.setTitle("BUDDIES");

        _activity = this;
        _context = this;

        intializeElements();
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");

        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        Bundle b = getIntent().getExtras();

        eventDetails = (EventDetails) b.getSerializable("AllEventsDetails");

        normal_backpress = b.containsKey("normal_backpressed") ? true : false;

        trip_id = eventDetails.getEvent_id();
        cntlstinvt = (Button) findViewById(R.id.contlstinvt);

        cntlstinvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cntlstinvt.setClickable(false);

                ArrayList<String> inviteBuddyList = InviteBuddyContactListAdapter.getSelectedStrings();
                String Buddyuids = "";

                if (inviteBuddyList.size() > 1) {
                    for (int i = 0; i < inviteBuddyList.size(); i++) {
                        Buddyuids += inviteBuddyList.get(i) + ",";
                    }

                  /*  for (int i = 0, f = buddyUIDSes.size(); i < f; i++) {

                            String _phone = buddyUIDSes.get(i).getUser_id();

                            if (_phone.contains(""))
                                inviteBuddyList.add(buddyUIDSes.get(i).getUser_id());
                            else
                                inviteBuddyList.add("" + buddyUIDSes.get(i).getUser_id());

                    }*/
                    inviteBuddy(trip_id, Buddyuids);
                } else if (inviteBuddyList.size() == 1) {
                    for (int i = 0; i < inviteBuddyList.size(); i++) {
                        Buddyuids += inviteBuddyList.get(i);
                    }

                  /*  for (int i = 0, f = buddyUIDSes.size(); i < f; i++) {

                            String _phone = buddyUIDSes.get(i).getUser_id();

                            if (_phone.contains(""))
                                inviteBuddyList.add(buddyUIDSes.get(i).getUser_id());
                            else
                                inviteBuddyList.add("" + buddyUIDSes.get(i).getUser_id());

                    }*/
                    inviteBuddy(trip_id, Buddyuids);
                }

                // String[] inviteeList = inviteBuddyList.toArray(new String[inviteBuddyList.size()]);

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!normal_backpress) {
                    Intent i = new Intent(InviteBuddyContactList.this, InviteBuddiesActivity.class);
                    startActivity(i);
                } else {
                    onBackPressed();
                }
            }
        });
        if (recycler_view_event != null) {
            recycler_view_event.setHasFixedSize(true);
        }
        mLayoutManager = new LinearLayoutManager(this);
        recycler_view_event.setLayoutManager(mLayoutManager);
        getAllPeople();
        bellcount();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!normal_backpress) {
            Intent i = new Intent(InviteBuddyContactList.this, InviteBuddiesActivity.class);
            startActivity(i);
        }
    }

    private void intializeElements() {
        recycler_view_event = (RecyclerView) findViewById(R.id.recycler_view_event);
        navtxt = (MyTextView) findViewById(R.id.navtxt);
        back = (ImageView) findViewById(R.id.imgNotification2);
        srch = (EditText) findViewById(R.id.srch);
    }

    private void inviteBuddy(String trip_id, String user_id) {

        Log.d("inviteBuddy ", "");

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<CommonResponseforBuddy> userCall = service.inviteBuddiesfriend("Bearer " + token, trip_id, user_id);

        userCall.enqueue(new Callback<CommonResponseforBuddy>() {
            @Override
            public void onResponse(Call<CommonResponseforBuddy> call, Response<CommonResponseforBuddy> response) {
                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                cntlstinvt.setClickable(true);
                if (response.body().getStatus()) {
                    oDialog("Invitation sent successfully", "Ok", "", true, _activity, "MyProfileActivity", 0);
                }
            }

            @Override
            public void onFailure(Call<CommonResponseforBuddy> call, Throwable t) {
                cntlstinvt.setClickable(true);
                t.printStackTrace();
            }
        });
    }

    private void getAllPeople() {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<InviteBuddyResponse> userCall = service.invitebuddyList("Bearer " + token);
        userCall.enqueue(new Callback<InviteBuddyResponse>() {
            @Override
            public void onResponse(Call<InviteBuddyResponse> call, Response<InviteBuddyResponse> response) {

                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("TOKEN:", "" + token);

                if (response.body().getStatus() == "true") {
                    inviteBuddyContactListAdapter = new InviteBuddyContactListAdapter(_activity, _context, response.body().getDetails(), token);
                    recycler_view_event.setAdapter(inviteBuddyContactListAdapter);
                    inviteBuddyContactListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<InviteBuddyResponse> call, Throwable t) {
            }
        });
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
                } else if (!bellcounter.equals("0")) {
                    bell_counter.setVisibility(View.VISIBLE);
                }
                bell_counter.setText(response.body().getCounter());

                //  Log.d("TOKEN:", "" + token);
                //     ArrayList<AllNotice> allNotices = response.body().getNotification_details();
                //    allNoticeAdapter.updateAllNotice(response.body().getNotification_details());
            }

            @Override
            public void onFailure(Call<AllNotice> call, Throwable t) {

            }
        });
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    public static class InviteBuddyContactListAdapter extends RecyclerView.Adapter<com.bluebuddy.adapters.InviteBuddyContactListAdapter.ViewHolder> {

        private static ArrayList<String> selectedStrings = new ArrayList<String>();
        boolean flag = true;
        private Activity _activity;
        private Context _context;
        private String token;
        private FirebaseAuth auth;
        private ArrayList<InviteBuddyDetail> BuddyList22;

        public InviteBuddyContactListAdapter() {
        }

        public InviteBuddyContactListAdapter(Activity a, Context c, ArrayList<InviteBuddyDetail> BuddyList, String token) {
            //    if(!BuddyList.isEmpty())
            this.BuddyList22 = BuddyList;
            this._activity = a;
            this._context = c;
            this.token = token;
            auth = FirebaseAuth.getInstance();
        }

        public static ArrayList<String> getSelectedStrings() {
            return selectedStrings;
        }

        @Override
        public com.bluebuddy.adapters.InviteBuddyContactListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.invite_buddy_contact_card, parent, false);

            com.bluebuddy.adapters.InviteBuddyContactListAdapter.ViewHolder vh = new com.bluebuddy.adapters.InviteBuddyContactListAdapter.ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(final com.bluebuddy.adapters.InviteBuddyContactListAdapter.ViewHolder holder, final int position) {
            final InviteBuddyDetail buddyDetail = BuddyList22.get(position);
            holder.name.setText(buddyDetail.getFname());
            if (!buddyDetail.getDp().equals("")) {

                Glide.with(_activity).asBitmap().load(IMG_URL + buddyDetail.getDp()).into(new BitmapImageViewTarget(holder.pro_img) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(_activity.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.pro_img.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
            ArrayList<BadgeDetail> badgeDetails = buddyDetail.getBadgeDetails();

            if (badgeDetails != null) {
                int len = badgeDetails.size();

                if (len == 0) {
                    //if (certification_details.get(0).getCert_type().equals("Scuba_Diving") && certification_details.get(1).getCert_type().equals("Free_Diving")||certification_details.get(0).getCert_type().equals("Free_Diving")&& certification_details.get(1).getCert_type().equals("Scuba_Diving"))
                    holder.badge1.setVisibility(View.GONE);
                    holder.badge2.setVisibility(View.GONE);

                } else if (len == 1) {
                    if (badgeDetails.get(0).getCertification_type().equals("Scuba_Diving") && !badgeDetails.get(0).getCertification_type().equals("Free_Diving")) {
                        holder.badge1.setVisibility(View.VISIBLE);
                        //      holder.badge2.setVisibility(View.GONE);
                    } else if (badgeDetails.get(0).getCertification_type().equals("Free_Diving") && !badgeDetails.get(0).getCertification_type().equals("Scuba_Diving")) {
                        holder.badge2.setVisibility(View.VISIBLE);
                        //   holder.badge1.setVisibility(View.GONE);
                    }
                } else if (len == 2) {
                    holder.badge1.setVisibility(View.VISIBLE);
                    holder.badge2.setVisibility(View.VISIBLE);
                }
            }
       /* holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), PeopleProfileActivity.class);
                i.putExtra("uid", buddyDetail.getUser_id());
                v.getContext().startActivity(i);
            }
        });*/
            if (BuddyList22.get(position).isImgCheckUser()) {
                holder.imginterest.setImageResource(R.drawable.checked);
            } else {
                holder.imginterest.setImageResource(R.drawable.unchecked);
            }

            holder.imginterest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BuddyList22.get(position).getUser_id();
                    selectBuddy(position, holder);
                }
            });

        }

        @Override
        public int getItemCount() {
            //  return (BuddyList22.size()==0)?  0 : BuddyList22.size();
            return BuddyList22 == null ? 0 : BuddyList22.size();   // return BuddyList22.size();

        }

        public void updateBuddyList(ArrayList<InviteBuddyDetail> details) {
            BuddyList22 = details;

            this.notifyDataSetChanged();
        }

        private void selectBuddy(int position, com.bluebuddy.adapters.InviteBuddyContactListAdapter.ViewHolder holder) {
            if (BuddyList22.get(position).isImgCheckUser()) {
                BuddyList22.get(position).setImgCheckUser(false);
                holder.imginterest.setImageResource(R.drawable.unchecked);

//                for (int ik = 0; ik < selectedStrings.size(); ik++) {
//                    if (selectedStrings.get(ik).equals(BuddyList22.get(position).getUser_id())) {
//                        selectedStrings.remove(ik);
//                    }
//                }

                if (selectedStrings.contains(BuddyList22.get(position).getUser_id())) {
                    selectedStrings.remove(BuddyList22.get(position).getUser_id());
                }

            } else {

                BuddyList22.get(position).setImgCheckUser(true);
                holder.imginterest.setImageResource(R.drawable.checked);
                selectedStrings.add(BuddyList22.get(position).getUser_id());

            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView chat, imginterest;
            private ImageView badge2;
            private ImageView badge1;
            private MyTextView name;
            private ImageView pro_img;
            private LinearLayout row;


            public ViewHolder(View v) {
                super(v);

                // chat=(ImageView)v.findViewById(R.id.chat);
                badge1 = (ImageView) v.findViewById(R.id.pplcert1);
                badge2 = (ImageView) v.findViewById(R.id.pplcert2);
                pro_img = (ImageView) v.findViewById(R.id.pro_img);
                name = (MyTextView) v.findViewById(R.id.name);
                row = (LinearLayout) v.findViewById(R.id.row);
                imginterest = (ImageView) v.findViewById(R.id.imginterest);


            }
        }
    }
}
