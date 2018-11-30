package com.bluebuddy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluebuddy.R;
import com.bluebuddy.activities.EditEventActivity;
import com.bluebuddy.activities.EventsDetailsActivity2;
import com.bluebuddy.activities.InviteBuddiesActivity;
import com.bluebuddy.models.EventDetails;
import com.bluebuddy.models.TripDetail;

import java.util.ArrayList;


public class MyEventAdapter extends RecyclerView.Adapter<MyEventAdapter.ViewHolder> {

    private ArrayList<TripDetail> EVENTList22;
    private Activity _activity;
    private Context _context;
    private boolean _editAble = true;
    private boolean _editAble1 = true;

    public MyEventAdapter() {

    }

    public MyEventAdapter(Activity a, Context c, ArrayList<TripDetail> EVENTList22, boolean _editAble) {
        this._activity = a;
        this._context = c;
        this.EVENTList22 = EVENTList22;
        this._editAble = _editAble;
    }

    public MyEventAdapter(Activity a, Context c, ArrayList<TripDetail> EVENTList22) {
        this._activity = a;
        this._context = c;
        this.EVENTList22 = EVENTList22;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Creating a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_events_card, parent, false);

        //set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final TripDetail tripDetail = EVENTList22.get(position);
        // holder.tvevtName.setText(tripDetail.getActivity());
        holder.tvfrdate.setText(tripDetail.getEvent_date());
        holder.Bccprice.setText(tripDetail.getParticipants());

        holder.tvloc.setText(tripDetail.getLocation());
        final String serverkey = holder.itemView.getContext().getResources().getString(R.string.firebase_server_key);
        holder.tvdesc.setText(tripDetail.getDescription());
//vvp
        if (tripDetail.getActivity().contains(":")) {
            String[] separated = tripDetail.getActivity().split(":");
            // separated[0];
            // separated[1];
            separated[1] = separated[1].trim();
            holder.tvevtName.setText(separated[1]);
        } else if (!tripDetail.getActivity().contains(":")) {
            holder.tvevtName.setText(tripDetail.getActivity());
        }

        //vvp
        holder.evtDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_activity, EventsDetailsActivity2.class);
                EventDetails eventDetails = new EventDetails(tripDetail.getEvent_id(), tripDetail.getUser_id(), tripDetail.getLocation(), tripDetail.getEvent_date(), tripDetail.getActivity(), "", tripDetail.getDescription(), tripDetail.getParticipants(), tripDetail.getCreator_fname(), tripDetail.getCreator_lname(), tripDetail.getCreator_dp(), "", "", "", "", "", "1");
                Log.d("eventDetails", eventDetails.toString());
                intent.putExtra("AllEventsDetails", eventDetails);
                _activity.startActivity(intent);
            }
        });

        holder.EDTevt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_activity, EditEventActivity.class);
                intent.putExtra("TripDetail", tripDetail);
                _activity.startActivity(intent);
            }
        });

       /* if(!_editAble){
            holder.EDTevt.setVisibility(View.GONE);
        }
*/
        if (!_editAble) {
            holder.EDTevt.setVisibility(View.GONE);
            holder.tripOptions.setVisibility(View.GONE);
            holder.iminn.setVisibility(View.GONE);
        } else if (_editAble) {
            holder.EDTevt.setVisibility(View.VISIBLE);
            holder.iminn.setVisibility(View.GONE);
        }
      /*  holder.iminn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getevtid(position, holder, tripDetail.getEvent_id(), tripDetail.getCreator_firebase_api_key(), serverkey);

            }
        });*/

        holder.tripOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(_context, holder.tripOptions);
                //inflating menu from xml resource
                popup.inflate(R.menu.mytrip_option_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menuInviteBuddy:
                                EventDetails eventDetails = new EventDetails(tripDetail.getEvent_id(), tripDetail.getUser_id(), tripDetail.getLocation(), tripDetail.getEvent_date(), tripDetail.getActivity(), "", tripDetail.getDescription(), tripDetail.getParticipants(), tripDetail.getCreator_fname(), tripDetail.getCreator_lname(), tripDetail.getCreator_dp(), "", "", "", "", "", "1");
                                Intent i = new Intent(_activity, InviteBuddiesActivity.class);
                                i.putExtra("AllEventsDetails", eventDetails);
                                i.putExtra("normal_backpressed", "1");
                                _activity.startActivity(i);
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return EVENTList22 == null ? 0 : EVENTList22.size();
    }

    public void updateMyTrip(ArrayList<TripDetail> details) {
        EVENTList22 = details;

        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //each data item is just a string in this case
        public TextView tvuname, tvevtName, tvfrdate, tvtodate, tvloc, tvdesc, Bccprice;
        public ImageView ivupic;
        public Button evtDt, EDTevt, tripOptions, iminn, btnRequestSent;

        public ViewHolder(View v) {
            super(v);

            tvevtName = (TextView) v.findViewById(R.id.txtevttitleid);
            tvfrdate = (TextView) v.findViewById(R.id.txtevtdatefromid);
            Bccprice = (TextView) v.findViewById(R.id.bmcprice);
            tvloc = (TextView) v.findViewById(R.id.txtevtlocid);
            tvdesc = (TextView) v.findViewById(R.id.txtevtdescid);
            evtDt = (Button) v.findViewById(R.id.evtdetail);
            EDTevt = (Button) v.findViewById(R.id.edtevt);
            tripOptions = (Button) v.findViewById(R.id.tripOptions);
            iminn = (Button) v.findViewById(R.id.iminn);
            btnRequestSent = (Button) v.findViewById(R.id.btnRequestSent);

        }
    }

   /* public void getevtid(final int position, final ViewHolder holder, String event_id, final String accessToken, final String serverkey) {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<CommonResponse> userCall = service.participateToEvent("Bearer " + token, "post", event_id);

        userCall.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("onResponse:", "" + response.body().getMessage());
                //   Log.d("TOKEN:", "" + token);

                holder.iminn.findViewWithTag(position).setVisibility(View.GONE);
                holder.btnRequestSent.findViewWithTag(position).setVisibility(View.VISIBLE);

                HashMap<String, String> notification = new HashMap<String, String>();
                notification.put("body", "A new user wanted to participate");
                notification.put("title", "Participation Request");
                notification.put("icon", "appicon");

                sendNotification(new PushRequest(accessToken, notification), serverkey);
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }*/

}
