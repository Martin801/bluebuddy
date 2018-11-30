package com.bluebuddy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.bluebuddy.R;
import com.bluebuddy.activities.AllEventsActivityNew;
import com.bluebuddy.activities.EditEventActivity;
import com.bluebuddy.activities.EventsDetailsActivity2;
import com.bluebuddy.activities.InviteBuddiesActivity;
import com.bluebuddy.activities.PeopleProfileActivity;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.api.ApiClient2;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.CommonResponse;
import com.bluebuddy.models.EventDetails;
import com.bluebuddy.models.FlagResponse;
import com.bluebuddy.models.PushNotification;
import com.bluebuddy.models.PushRequest;
import com.bluebuddy.models.TripDetail;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.IMG_URL;

public class AllEventAdapter extends RecyclerView.Adapter<AllEventAdapter.ViewHolder> {

    boolean flag = true;
    String serverkey;
    private List<EventDetails> EVENTList;
    private Activity _activity;
    private Context _context;
    private String token, token2, curru;
    private SharedPreferences pref;
    private FirebaseAuth auth;

    public AllEventAdapter() {
    }

    public AllEventAdapter(Activity a, Context c, List<EventDetails> EVENTList, String token, String curru) {
        this._activity = a;
        this._context = c;
        this.EVENTList = EVENTList;
        this.token = token;
        this.curru = curru;
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Creating a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_card, parent, false);

        //set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final EventDetails allEventsDetails = EVENTList.get(position);
        holder.tvuname.setText(String.valueOf(allEventsDetails.getCreator_fname()) + " " + allEventsDetails.getCreator_lname());
        holder.tvfrdate.setText(allEventsDetails.getEvent_date());
        holder.tvloc.setText(allEventsDetails.getLocation());
        holder.tvdesc.setText(allEventsDetails.getDescription());
        //  holder.tvevtName.setText(allEventsDetails.getActivity());
        //serverkey=allEventsDetails.getCreator_firebase_api_key();
        serverkey = holder.itemView.getContext().getResources().getString(R.string.firebase_server_key);

        if (allEventsDetails.getActivity().contains(":")) {
            String[] separated = allEventsDetails.getActivity().split(":");
            // separated[0];
            // separated[1];
            separated[1] = separated[1].trim();
            holder.tvevtName.setText(WordUtils.capitalize(separated[1]));
        } else if (!allEventsDetails.getActivity().contains(":")) {
            holder.tvevtName.setText(WordUtils.capitalize(allEventsDetails.getActivity()));
        }

        final ImageView imageView = holder.ivupic;

        if (allEventsDetails.getUser_id().equals(curru)) {
            holder.report_flag.setVisibility(View.GONE);
        } else if (!allEventsDetails.getUser_id().equals(curru)) {
            if (allEventsDetails.getIs_flagged().equals("0")) {
                holder.report_flag.setVisibility(View.VISIBLE);
                holder.report_flag.setImageResource(R.drawable.ic_flag);
                holder.report_flag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
                        _context = v.getContext().getApplicationContext();
                        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View dialogView = inflater.inflate(R.layout.custom_popup_inputdialog1, null);
                        dialogBuilder.setView(dialogView);
                        dialogBuilder.setMessage("Report");
                        dialogBuilder.setPositiveButton("Report", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                                Call<FlagResponse> userCall = service.fr("Bearer " + token, "post", "trip", allEventsDetails.getEvent_id());

                                userCall.enqueue(new Callback<FlagResponse>() {
                                    @Override
                                    public void onResponse(Call<FlagResponse> call, Response<FlagResponse> response) {
                                        if (response.body().getStatus() == "true") {
                                            // Log.d("fr comment", "" + response.body().getStatus());
                                            holder.report_flag.setImageResource(R.drawable.ic_flagred);
                                            holder.report_flag.setClickable(false);
                                        }
                              /*  else if (response.body().getMessage().equals("Oops! Looks like you have already reported it as flag.")){
                                    Log.d("fr comment", "" + response.body().getStatus());
                                }*/
                                    }

                                    @Override
                                    public void onFailure(Call<FlagResponse> call, Throwable t) {
                                        Log.d("onFailure", t.toString());
                                        t.printStackTrace();
                                    }
                                });

                            }
                        });
                        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //pass
                            }
                        });
                        AlertDialog b = dialogBuilder.create();
                        b.show();
/**
 * Back button listener.
 * Will close the application if the back button pressed twice.
 */
             /*   @Override
                public void onBackPressed()
                {
                    if(backButtonCount >= 1)
                    {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
                        backButtonCount++;
                    }
                }*/


                    }
                });
            } else if (allEventsDetails.getIs_flagged().equals("1")) {
                holder.report_flag.setVisibility(View.VISIBLE);
                holder.report_flag.setImageResource(R.drawable.ic_flagred);
            }

        }

        Glide.with(imageView.getContext()).asBitmap().load(IMG_URL + allEventsDetails.getCreator_dp()).into(new BitmapImageViewTarget(holder.ivupic) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(imageView.getContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.ivupic.setImageDrawable(circularBitmapDrawable);
            }
        });

      /*  if(!allEventsDetails.getCreator_dp().equals("")) {

            Glide.with(_activity).load(IMG_URL + allEventsDetails.getCreator_dp()).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.ivupic) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(_activity.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.ivupic.setImageDrawable(circularBitmapDrawable);
                }
            });
        }*/

        holder.evtDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EventsDetailsActivity2.class);
                intent.putExtra("AllEventsDetails", allEventsDetails);
                intent.putExtra("token", token);
                v.getContext().startActivity(intent);
            }
        });

        holder.imin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getevtid(position, holder, allEventsDetails.getEvent_id(), allEventsDetails.getCreator_firebase_api_key(), serverkey);
            }
        });

        holder.btnRequestSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteParticipantFromEvent(position, holder, allEventsDetails.getEvent_id(), allEventsDetails.getCreator_firebase_api_key(), serverkey, curru);
            }
        });

        holder.imin.setTag(position);
        holder.btnRequestSent.setTag(position);

        if (Integer.valueOf(allEventsDetails.getIs_editable()) > 0) {
            holder.linearImin.setVisibility(View.GONE);
            holder.linearEdit.setVisibility(View.VISIBLE);
        } else {
            holder.linearEdit.setVisibility(View.GONE);
            holder.linearImin.setVisibility(View.VISIBLE);

            holder.imin.setVisibility(View.VISIBLE);
            holder.btnRequestSent.setVisibility(View.GONE);

            if (Integer.valueOf(allEventsDetails.getParticipated()) > 0) {
                holder.imin.setVisibility(View.GONE);
                holder.btnRequestSent.setVisibility(View.VISIBLE);
            }
        }

        holder.EDTevt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditEventActivity.class);
                intent.putExtra("TripDetail", new TripDetail(allEventsDetails.getUser_id(), allEventsDetails.getEvent_id(), allEventsDetails.getLocation(), allEventsDetails.getEvent_date(), allEventsDetails.getActivity(), allEventsDetails.getDescription(), allEventsDetails.getParticipants(), allEventsDetails.getCreator_fname(), allEventsDetails.getCreator_lname(), allEventsDetails.getCreator_dp()));
                v.getContext().startActivity(intent);
            }
        });

        holder.btnProfileImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), PeopleProfileActivity.class);
                i.putExtra("uid", String.valueOf(allEventsDetails.getUser_id()));
                v.getContext().startActivity(i);
            }
        });
        String othersid = allEventsDetails.getUser_id();
        if (!allEventsDetails.getUser_id().equals(curru)) {
            holder.tripOptions.setVisibility(View.GONE);
        } else if (allEventsDetails.getUser_id().equals(curru)) {
            holder.tripOptions.setVisibility(View.VISIBLE);
            holder.tripOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(v.getContext(), holder.tripOptions);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.trip_option_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu1:
                                    //handle menu1 click
                                    EventDetails eventDetails = new EventDetails(allEventsDetails.getEvent_id(), allEventsDetails.getUser_id(), allEventsDetails.getLocation(), allEventsDetails.getEvent_date(), allEventsDetails.getActivity(), "", allEventsDetails.getDescription(), allEventsDetails.getParticipants(), allEventsDetails.getCreator_fname(), allEventsDetails.getCreator_lname(), allEventsDetails.getCreator_dp(), "", "", "", "", "", "1");
                                    Intent i = new Intent(v.getContext(), InviteBuddiesActivity.class);
                                    i.putExtra("AllEventsDetails", eventDetails);
                                    i.putExtra("normal_backpressed", "1");
                                    v.getContext().startActivity(i);

                                    break;
                         /*   case R.id.menu2:
                                //handle menu2 click
                                break;
                            case R.id.menu3:
                                //handle menu3 click
                                break;*/
                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return EVENTList == null ? 0 : EVENTList.size();
    }

    public void updateAllTrip(ArrayList<EventDetails> details) {
        if (EVENTList != null)
            EVENTList.clear();
//        EVENTList.clear();
        EVENTList = details;
        this.notifyDataSetChanged();
    }

    public void getevtid(final int position, final ViewHolder holder, String event_id, final String accessToken, final String serverkey) {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<CommonResponse> userCall = service.participateToEvent("Bearer " + token, event_id);

        userCall.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        holder.imin.findViewWithTag(position).setVisibility(View.GONE);
                        holder.btnRequestSent.findViewWithTag(position).setVisibility(View.VISIBLE);

                        EVENTList.get(position).setParticipated("1");
                    }
                }
                //   EVENTList
                //  EventDetails allEventsDetailsDataNeedToChange = EVENTList.get(position);


//                HashMap<String, String> notification = new HashMap<String, String>();
//                notification.put("body", "A new user wanted to participate");
//                notification.put("title", "Participation Request");
//                notification.put("icon", "appicon");
//                sendNotification(new PushRequest(accessToken, notification), serverkey);
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void deleteParticipantFromEvent(final int position, final ViewHolder holder, String event_id, final String accessToken, final String serverkey, String currentUserId) {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<CommonResponse> userCall = service.deleteParticipantFromEvent("Bearer " + token, event_id, currentUserId);

        userCall.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        holder.imin.findViewWithTag(position).setVisibility(View.VISIBLE);
                        holder.btnRequestSent.findViewWithTag(position).setVisibility(View.GONE);
                        EVENTList.get(position).setParticipated("0");
                    }
                }

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void sendNotification(PushRequest notification, String serverKey) {

        //String serverKey = _context.getResources().getString(R.string.firebase_server_key);

        ApiInterface service = ApiClient2.getClient2().create(ApiInterface.class);
        Call<PushNotification> pushCall = service.push("key=" + serverKey, notification);

        pushCall.enqueue(new Callback<PushNotification>() {
            @Override
            public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {
                //  Log.d("SUCCESS_REGISTER", "SENT");
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t) {
                Log.d("FAILURE_REGISTER", "HERE I M");
                t.printStackTrace();
            }
        });
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Context context, final String _redirectClass, final int layout) {
        AllEventsActivityNew allEventsActivityNew = new AllEventsActivityNew();
        allEventsActivityNew.openDialog16(msg, btnText, btnText2, _redirect, context, _redirectClass, layout);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //each data item is just a string in this case
        public TextView tvuname, tvevtName, tvfrdate, tvtodate, tvloc, tvdesc;
        public ImageView ivupic, report_flag;
        public Button evtDt, imin, btnRequestSent, EDTevt, btnProfileImageBtn, tripOptions;
        public LinearLayout linearImin, linearEdit;

        public ViewHolder(View v) {
            super(v);

            report_flag = (ImageView) v.findViewById(R.id.report_flag);
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
}
