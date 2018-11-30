package com.bluebuddy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.bluebuddy.R;
import com.bluebuddy.activities.EventsDetailsActivity2fornoti;
import com.bluebuddy.activities.PeopleProfileActivity;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.api.ApiClient2;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AfterLoginStatus;
import com.bluebuddy.models.NoticeDetails;
import com.bluebuddy.models.PushNotification;
import com.bluebuddy.models.PushRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.IMG_URL;
import static com.bluebuddy.app.AppConfig.USER_FULL_NAME;

public class AllNoticeAdapter extends RecyclerView.Adapter<AllNoticeAdapter.ViewHolder> {

    boolean flag = true;
    String notid;
    boolean visibility = true;
    // String accessToken ;
    String serverkey = "AAAAmFQzmz4:APA91bGHHt9rZT18X3cfD9c4FHadRluzyo2_slaUpXTK9XYSSWmpqUHA5aQrZMtb_RJv7HPwnIK1dP_9D65pGhBPSqej_42ZV3u3yfL4dScEXJnfNavZ72xpyfFxP4-nKwW64hlOj_6Y";
    private List<NoticeDetails> EVENTList;
    private Activity _activity;
    private Context _context;
    private String token, forid, status, participant_id;
    // private Integer status;
    private SharedPreferences pref;
    private FirebaseAuth auth;

    public AllNoticeAdapter() {

    }

    public AllNoticeAdapter(Activity a, Context c, List<NoticeDetails> EVENTList, String token, SharedPreferences pref) {
        this._activity = a;
        this._context = c;
        this.EVENTList = EVENTList;
        this.token = token;
        this.pref = pref;
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Creating a new view

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_card, parent, false);

        //set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final NoticeDetails noticeDetails = EVENTList.get(position);
        noticeDetails.toString();
        notid = noticeDetails.getId();
        // changeclr(notid);
        //  holder.card_view.setBackgroundColor(5);
        //  holder.card_view.setBackgroundColor( Color.parseColor("#0021110"));


        //
//        holder.tvuname.setText(noticeDetails.getRequestor_id());

        /*holder.tvloc.setText(noticeDetails.getNotify_for());*/
        // if (noticeDetails.getNotify_for().equals("trip")){
        // holder.tvloc.setText(noticeDetails.getActivity_type());
        //message
        /*holder.tvdesc.setText(noticeDetails.getMessage());*/

        holder.tvevtName.setText(noticeDetails.getReceiver_id());
        holder.tvevtName.setText(noticeDetails.getFirst_name() + " " + noticeDetails.getLast_name());
        Glide.with(_activity).asBitmap().load(IMG_URL + noticeDetails.getProfile_pic()).into(new BitmapImageViewTarget(holder.userpic) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(_activity.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.userpic.setImageDrawable(circularBitmapDrawable);
            }
        });
        // Log.d("TXT:", noticeDetails.toString());

        if (noticeDetails.getViewed_at().equals("1")) {
            holder.card_view.setCardBackgroundColor(Color.LTGRAY);
        } else if (noticeDetails.getViewed_at().equals("0")) {
            //   holder.card_view.setCardBackgroundColor(Color.LTGRAY);
        }

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // holder.card_view.setBackgroundColor( Color.parseColor("#0021110"));
                // notid = noticeDetails.getId();
                changeclr(notid);
                //   holder.card_view.setCardBackgroundColor(Color.LTGRAY);
            }
        });


        if (noticeDetails.getNotify_for().equals("join_trip")) {

            holder.idacpt.setVisibility(View.VISIBLE);
            holder.idrjt.setVisibility(View.VISIBLE);
            holder.tvloc.setText(noticeDetails.getActivity_type());
            holder.tvfrdate.setText(noticeDetails.getMessage());

        } else if (noticeDetails.getNotify_for().equals("buddy_up")) {

            holder.idacpt.setVisibility(View.VISIBLE);
            holder.idrjt.setVisibility(View.VISIBLE);
            holder.tvloc.setText(noticeDetails.getCreate_dt());
            holder.tvfrdate.setText(noticeDetails.getFirst_name() + " wants to be your buddy");

        } else if (noticeDetails.getNotify_for().equals("invite_trip")) {

            holder.idacpt.setVisibility(View.GONE);
            holder.idrjt.setVisibility(View.GONE);
            holder.tvfrdate.setText(noticeDetails.getMessage());
            holder.card_noti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    String notification_id = noticeDetails.getId();
                    participant_id = noticeDetails.getRequestor_id();
                    forid = noticeDetails.getFor_id();
                    status = "1";
//Toast.makeText(v.getContext(),forid,Toast.LENGTH_LONG).show();
                    changeclr(notid);
                    Intent i = new Intent(v.getContext(), EventsDetailsActivity2fornoti.class);
                    i.putExtra("event_id", forid);
                    i.putExtra("user_id", participant_id);
                    v.getContext().startActivity(i);

                }
            });

        }

        holder.idacpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // changeclr(notid);
                if (noticeDetails.getNotify_for().equals("join_trip")) {
                    String notification_id = noticeDetails.getId();
                    participant_id = noticeDetails.getRequestor_id();
                    forid = noticeDetails.getFor_id();
                    status = "1";

                    //  Toast.makeText(v.getContext(), noticeDetails.getFor_id() + " : " + notification_id, Toast.LENGTH_LONG).show();
                    actnotice(forid, status, participant_id, notification_id);
                    holder.tvfrdate.setText(noticeDetails.getFirst_name() + " is now " + "a participant of your trip!");
                    holder.idacpt.setVisibility(View.GONE);
                    holder.idrjt.setVisibility(View.GONE);

//final String serverkey = "AAAApXAGQ6s:APA91bGDm1U0Wb9ZVqjXmKff8eX1GhSMQ-0SrfptZQljhYw87_0RZFKoRFmPq5UZg8r-5mJ_2I5uEJT6vK_hLO7zlUhmYffmqincbQgxsUte0ooc0V8V-_7JHYHPoRB32l7eXucwFeKq";
                    // final String serverkey=holder.idacpt.getContext().getResources().getString(R.string.firebase_server_key);

                    //  accessToken = FirebaseInstanceId.getInstance().getToken();
                    final String accessToken = noticeDetails.getFirebase_API_key();

                    String creator = pref.getString(USER_FULL_NAME, "");
                    HashMap<String, String> notification = new HashMap<String, String>();
                    notification.put("body", "Congrats! You have participated in the trip of " + creator);
                    notification.put("title", "Trip participation Request Accepted");
                    notification.put("icon", "appicon");
                    //sendNotification(new PushRequest(accessToken, notification),serverkey);
//                    sendNotification(serverkey, new PushRequest(accessToken, notification));

                } else if (noticeDetails.getNotify_for().equals("buddy_up")) {
                    String notification_id = noticeDetails.getId();
                    participant_id = noticeDetails.getRequestor_id();
                    forid = noticeDetails.getUser_id();
                    status = "1";

                    //   Toast.makeText(v.getContext(), noticeDetails.getFor_id() + " : " + notification_id, Toast.LENGTH_LONG).show();
                    buddyacceptdec(forid, status, notification_id);
                    holder.tvfrdate.setText("You are now buddy with " + noticeDetails.getFirst_name());
                    holder.idacpt.setVisibility(View.GONE);
                    holder.idrjt.setVisibility(View.GONE);

                    //    final String serverkey=holder.idacpt.getContext().getResources().getString(R.string.firebase_server_key);
                    //      accessToken = FirebaseInstanceId.getInstance().getToken();
                    final String accessToken = noticeDetails.getFirebase_API_key();
                    String creator = pref.getString(USER_FULL_NAME, "");
                    HashMap<String, String> notification = new HashMap<String, String>();
                    notification.put("body", "Congrats! You are buddy with " + creator);
                    notification.put("title", "Buddy Request Accepted");
                    notification.put("icon", "appicon");
                    // sendNotification(new PushRequest(accessToken, notification),serverkey);
//                    sendNotification(serverkey, new PushRequest(accessToken, notification));
                }
            }
        });

        holder.btnProfileImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeclr(notid);
                Intent i = new Intent(v.getContext(), PeopleProfileActivity.class);
                i.putExtra("uid", String.valueOf(noticeDetails.getUser_id()));
                v.getContext().startActivity(i);
            }
        });

        holder.idrjt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // changeclr(notid);
                if (noticeDetails.getNotify_for().equals("join_trip")) {
                    forid = noticeDetails.getFor_id();
                    participant_id = noticeDetails.getRequestor_id();
                    String notification_id = noticeDetails.getId();
                    String status = "0";
                    actnotice(forid, status, participant_id, notification_id);
                    holder.tvfrdate.setText("You have rejected " + noticeDetails.getFirst_name() + "'s trip request");

                    holder.idacpt.setVisibility(View.GONE);
                    holder.idrjt.setVisibility(View.GONE);

                    // final String serverkey=holder.idacpt.getContext().getResources().getString(R.string.firebase_server_key);
                    //    accessToken = FirebaseInstanceId.getInstance().getToken();
                    final String accessToken = noticeDetails.getFirebase_API_key();
                    String creator = pref.getString(USER_FULL_NAME, "");
                    HashMap<String, String> notification = new HashMap<String, String>();
                    notification.put("body", "Sorry! You can not participate in this trip.Your Request has been rejected by " + creator);
                    notification.put("title", "Trip participation Request Accepted");
                    notification.put("icon", "appicon");
                    //  sendNotification(new PushRequest(accessToken, notification),serverkey);
                    //sendNotification(serverkey, new PushRequest(accessToken, notification));
                } else if (noticeDetails.getNotify_for().equals("buddy_up")) {
                    String notification_id = noticeDetails.getId();
                    forid = noticeDetails.getUser_id();
                    participant_id = noticeDetails.getRequestor_id();
                    status = "0";

                    //  Toast.makeText(v.getContext(), noticeDetails.getFor_id() + " : " + notification_id, Toast.LENGTH_LONG).show();
                    buddyacceptdec(forid, status, notification_id);
                    holder.tvfrdate.setText("You have rejected " + noticeDetails.getFirst_name() + " buddy request.");
                    holder.idacpt.setVisibility(View.GONE);
                    holder.idrjt.setVisibility(View.GONE);

                    //    final String serverkey=holder.idacpt.getContext().getResources().getString(R.string.firebase_server_key);
                    //   accessToken = FirebaseInstanceId.getInstance().getToken();
                    final String accessToken = noticeDetails.getFirebase_API_key();
                    String creator = pref.getString(USER_FULL_NAME, "");
                    HashMap<String, String> notification = new HashMap<String, String>();
                    notification.put("body", "Sorry " + creator + " has rejected your buddy request!");
                    notification.put("title", "Rejected Buddy Request!");
                    notification.put("icon", "appicon");
                    // sendNotification(serverkey, new PushRequest(accessToken, notification));

                }

            }
        });

    }

    private void changeclr(String notid) {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AfterLoginStatus> userCall = service.chclr("Bearer " + token, notid);

        userCall.enqueue(new Callback<AfterLoginStatus>() {
            @Override
            public void onResponse(Call<AfterLoginStatus> call, Response<AfterLoginStatus> response) {

                Log.d("STATUS", String.valueOf(response.body().getStatus()));

            }

            @Override
            public void onFailure(Call<AfterLoginStatus> call, Throwable t) {
                Log.d("STATUS", String.valueOf(t.getMessage()));
            }
        });
    }

    private void actnotice(String forid, String status, String participant_id, String notification_id) {
        changeclr(notid);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AfterLoginStatus> userCall = service.actnot("Bearer " + token, forid, status, participant_id, notification_id);

        userCall.enqueue(new Callback<AfterLoginStatus>() {
            @Override
            public void onResponse(Call<AfterLoginStatus> call, Response<AfterLoginStatus> response) {
             /*   if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();*/
                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                //   Log.d("onResponse:", "" + response.body().getNotification_details());
                //  Log.d("TOKEN:", "" + token);
                //     ArrayList<AllNotice> allNotices = response.body().getNotification_details();
                //    allNoticeAdapter.updateAllNotice(response.body().getNotification_details());
            }

            @Override
            public void onFailure(Call<AfterLoginStatus> call, Throwable t) {

            }
        });
    }

    private void buddyacceptdec(String forid, String status, String notification_id) {
        changeclr(notid);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AfterLoginStatus> userCall = service.buddyacceptdec("Bearer " + token, forid, status, notification_id);

        userCall.enqueue(new Callback<AfterLoginStatus>() {
            @Override
            public void onResponse(Call<AfterLoginStatus> call, Response<AfterLoginStatus> response) {
             /*   if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();*/
                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                //   Log.d("onResponse:", "" + response.body().getNotification_details());
                //  Log.d("TOKEN:", "" + token);
                //     ArrayList<AllNotice> allNotices = response.body().getNotification_details();
                //    allNoticeAdapter.updateAllNotice(response.body().getNotification_details());
            }

            @Override
            public void onFailure(Call<AfterLoginStatus> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return EVENTList == null ? 0 : EVENTList.size();
    }

    public void updateAllNotice(ArrayList<NoticeDetails> getNotification_details) {
        EVENTList = getNotification_details;

        this.notifyDataSetChanged();
    }

    private void sendNotification(String serverKey, PushRequest notification) {

        //String serverKey = _context.getResources().getString(R.string.firebase_server_key);

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //each data item is just a string in this case
        public MyTextView tvuname, tvevtName, tvfrdate, tvtodate, tvloc, tvdesc, txtparticipated, txtdeclined;
        public ImageView ivupic, report_flag, userpic, idacpted, iddeclined;
        public Button idacpt, idrjt, btnRequestSent, EDTevt, btnProfileImageBtn;
        public LinearLayout linearImin, linearEdit, card_noti;
        public CardView card_view;

        public ViewHolder(View v) {
            super(v);
            btnProfileImageBtn = (Button) v.findViewById(R.id.btnProfileImageBtn);
            card_view = (CardView) v.findViewById(R.id.card_view);
            // txtdeclined=(TextView)v.findViewById(R.id.txtdeclined);
            report_flag = (ImageView) v.findViewById(R.id.report_flag);
            /* tvuname = (MyTextView) v.findViewById(R.id.txtunameid);*/

            tvevtName = (MyTextView) v.findViewById(R.id.txtevttitleid);
            tvloc = (MyTextView) v.findViewById(R.id.txtevtlocid);
            tvfrdate = (MyTextView) v.findViewById(R.id.txtevtdatefromid);

          /*  tvtodate = (MyTextView) v.findViewById(R.id.txtevtdattid);

            tvdesc = (MyTextView) v.findViewById(R.id.txtevtdescid);*/
            userpic = (ImageView) v.findViewById(R.id.userpic);
            // txtparticipated=(TextView)v.findViewById(R.id.txtparticipated);

            idacpt = (Button) v.findViewById(R.id.idacpt);
            idrjt = (Button) v.findViewById(R.id.idrjt);
            EDTevt = (Button) v.findViewById(R.id.edtevt);

            btnRequestSent = (Button) v.findViewById(R.id.btnRequestSent);

            linearImin = (LinearLayout) v.findViewById(R.id.linearImin);
            linearEdit = (LinearLayout) v.findViewById(R.id.linearEdit);
            card_noti = (LinearLayout) v.findViewById(R.id.card_noti);
            //  idacpted=(ImageView) v.findViewById(R.id.idacpted);
            iddeclined = (ImageView) v.findViewById(R.id.iddeclined);
        }
    }

}
