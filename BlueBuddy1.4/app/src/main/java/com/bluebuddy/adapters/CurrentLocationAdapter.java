package com.bluebuddy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluebuddy.R;

public class CurrentLocationAdapter extends RecyclerView.Adapter<CurrentLocationAdapter.ViewHolder> {

  //  private List<EventDetails> EVENTList;
    private Activity _activity;
    private Context _context;
  //  boolean flag = true;
    private String token;
    private SharedPreferences pref;
   // private FirebaseAuth auth;

    public CurrentLocationAdapter() {
    }

    public CurrentLocationAdapter(Activity a, Context c, String token) {
        this._activity = a;
        this._context = c;
      //  this.EVENTList = EVENTList;
        this.token = token;
      //  auth = FirebaseAuth.getInstance();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //each data item is just a string in this case
        public TextView tvuname, tvevtName, tvfrdate, tvtodate, tvloc, tvdesc;
        public ImageView ivupic,report_flag;
        public Button evtDt, imin, btnRequestSent, EDTevt, btnProfileImageBtn, tripOptions;
        public LinearLayout linearImin, linearEdit;

        public ViewHolder(View v) {
            super(v);

            report_flag=(ImageView)v.findViewById(R.id.report_flag);
            tvuname = (TextView) v.findViewById(R.id.txtunameid);
            tvevtName = (TextView) v.findViewById(R.id.txtevttitleid);
            tvfrdate = (TextView) v.findViewById(R.id.txtevtdatefromid);
            tvtodate = (TextView) v.findViewById(R.id.txtevtdattid);
            tvloc = (TextView) v.findViewById(R.id.txtevtlocid);
            tvdesc = (TextView) v.findViewById(R.id.txtevtdescid);
            ivupic = (ImageView) v.findViewById(R.id.txtupicid);

            evtDt = (Button) v.findViewById(R.id.evtdetail);
            imin = (Button) v.findViewById(R.id.imin);
            EDTevt = (Button) v.findViewById(R.id.edtevt);
            tripOptions = (Button) v.findViewById(R.id.tripOptions);

            btnProfileImageBtn = (Button) v.findViewById(R.id.btnProfileImageBtn);

            btnRequestSent = (Button) v.findViewById(R.id.btnRequestSent);

            linearImin = (LinearLayout) v.findViewById(R.id.linearImin);
            linearEdit = (LinearLayout) v.findViewById(R.id.linearEdit);
        }
    }

    @Override
    public CurrentLocationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Creating a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_card, parent, false);

        //set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final CurrentLocationAdapter.ViewHolder holder, final int position) {
     //   final EventDetails allEventsDetails = EVENTList.get(position);
      /*  holder.tvuname.setText(String.valueOf(allEventsDetails.getCreator_fname())+allEventsDetails.getCreator_lname());
        holder.tvfrdate.setText(allEventsDetails.getEvent_date());
        holder.tvloc.setText(allEventsDetails.getLocation());
        holder.tvdesc.setText(allEventsDetails.getDescription());
        holder.tvevtName.setText(allEventsDetails.getActivity());

        holder.report_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag){
                    holder.report_flag.setImageResource(R.drawable.ic_flag);
                    flag=false;

                } else {
                    holder.report_flag.setImageResource(R.drawable.ic_flagred);
                    flag = true;
                }
            }
        });

        Glide.with(_activity).load(IMG_URL+allEventsDetails.getCreator_dp()).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.ivupic) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(_activity.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
               holder.ivupic.setImageDrawable(circularBitmapDrawable);
            }
        });*/

      /*  holder.evtDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_activity, EventsDetailsActivity2.class);
                intent.putExtra("AllEventsDetails", allEventsDetails);
                intent.putExtra("token",token);
                _activity.startActivity(intent);
            }
        });

        holder.imin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getevtid(position, holder, allEventsDetails.getEvent_id(), allEventsDetails.getCreator_firebase_api_key());
            }
        });

        holder.imin.setTag(position);
        holder.btnRequestSent.setTag(position);

        if(Integer.valueOf(allEventsDetails.getIs_editable()) > 0){
            holder.linearImin.setVisibility(View.GONE);
            holder.linearEdit.setVisibility(View.VISIBLE);
        }else{
            holder.linearEdit.setVisibility(View.GONE);
            holder.linearImin.setVisibility(View.VISIBLE);

            holder.imin.setVisibility(View.VISIBLE);
            holder.btnRequestSent.setVisibility(View.GONE);

            if(Integer.valueOf(allEventsDetails.getParticipated()) > 0){
                holder.imin.setVisibility(View.GONE);
                holder.btnRequestSent.setVisibility(View.VISIBLE);
            }
        }

        holder.EDTevt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_activity, EditEventActivity.class);
                intent.putExtra("TripDetail", new TripDetail(allEventsDetails.getUser_id(), allEventsDetails.getEvent_id(), allEventsDetails.getLocation(), allEventsDetails.getEvent_date(), allEventsDetails.getActivity(), allEventsDetails.getDescription(), allEventsDetails.getParticipants(), allEventsDetails.getCreator_fname(), allEventsDetails.getCreator_lname(), allEventsDetails.getCreator_dp()));
                _activity.startActivity(intent);
            }
        });

        holder.btnProfileImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(_activity, PeopleProfileActivity.class);
                i.putExtra("user_id", String.valueOf(allEventsDetails.getUser_id()));
                _activity.startActivity(i);
            }
        });

        holder.tripOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(_context, holder.tripOptions);
                //inflating menu from xml resource
                popup.inflate(R.menu.trip_option_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                //handle menu1 click
                                break;
                         *//*   case R.id.menu2:
                                //handle menu2 click
                                break;
                            case R.id.menu3:
                                //handle menu3 click
                                break;*//*
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return 0;
    }

  /*  @Override
    public int getItemCount() {
        return EVENTList == null ? 0 : EVENTList.size();
    }
*/
    /*public void updateAllTrip(ArrayList<EventDetails> details){
        EVENTList = details;

        this.notifyDataSetChanged();
    }*/

/*
    public void getevtid(final int position, final CurrentLocationAdapter.ViewHolder holder, String event_id, final String accessToken){
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<CommonResponse> userCall = service.participateToEvent("Bearer "+token, "post", event_id);

        userCall.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("onResponse:", "" + response.body().getMessage());
                Log.d("TOKEN:", "" + token);

                holder.imin.findViewWithTag(position).setVisibility(View.GONE);
                holder.btnRequestSent.findViewWithTag(position).setVisibility(View.VISIBLE);

                HashMap<String, String> notification = new HashMap<String, String>();
                notification.put("body", "A new user wanted to participate");
                notification.put("title", "Participation Request");
                notification.put("icon", "appicon");

                sendNotification(new PushRequest(accessToken, notification));
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
*/

/*
    private void sendNotification(PushRequest notification){
        String serverKey = _activity.getResources().getString(R.string.firebase_server_key);

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
*/
}
