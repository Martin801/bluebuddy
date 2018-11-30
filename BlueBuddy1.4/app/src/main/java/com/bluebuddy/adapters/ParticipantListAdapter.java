package com.bluebuddy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bluebuddy.R;
import com.bluebuddy.activities.PeopleProfileActivity;
import com.bluebuddy.activities.PeopleSearchActivity;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.CommonResponse;
import com.bluebuddy.models.Participant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.IMG_URL;

/**
 * Created by USER on 5/28/2017.
 */

public class ParticipantListAdapter extends RecyclerView.Adapter<ParticipantListAdapter.ViewHolder> {

    private String _eventId;
    private boolean _isRemoveVisible;
    private String _token;
    private List<Participant> Participantslist;
    private Activity _activity;
    private PeopleSearchActivity peopleSearchActivity3;

    public ParticipantListAdapter() {

    }

    public ParticipantListAdapter(String eventId, String token, boolean isRemoveVisible, Activity a, Context c, List<Participant> Participantslist) {
        this._activity = a;
        this._token = token;
        this._eventId = eventId;
        this._isRemoveVisible = isRemoveVisible;
        this.Participantslist = Participantslist;
    }

    @Override
    public ParticipantListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Creating a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_participantscheck, parent, false);

        //set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ParticipantListAdapter.ViewHolder holder, final int position) {
        final Participant participant = Participantslist.get(position);
        holder.Pplname.setText(String.valueOf(participant.getFirst_name() + " " + participant.getLast_name()));
        Log.d("Participant", participant.toString());

        if (!_isRemoveVisible)
            holder.btParticipantRemove.setVisibility(View.GONE);

        if (!participant.getProfile_pic().equals(" ")) {

            //   if(!participant.getProfile_pic().equals("")) {
            Glide.with(_activity).asBitmap().load(IMG_URL + participant.getProfile_pic()).into(new BitmapImageViewTarget(holder.ivPpic) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(_activity.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.ivPpic.setImageDrawable(circularBitmapDrawable);
                }
            });
        }

        holder.linearParticipateRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ParticipantListAdapter", String.valueOf(participant.getId()));
                Intent i = new Intent(_activity, PeopleProfileActivity.class);
                i.putExtra("uid", String.valueOf(participant.getId()));

                _activity.startActivity(i);
            }
        });

        holder.btParticipantRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteParticipantFromEvent(position, holder, _eventId, participant.getId());

            }
        });
    }

    public void deleteParticipantFromEvent(final int position, final ViewHolder holder, String event_id, String participantId) {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<CommonResponse> userCall = service.deleteParticipantFromEvent("Bearer " + _token, event_id, participantId);

        userCall.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                if (response.body().getStatus()) {
                    Participantslist.remove(position);
                    notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    @Override
    public int getItemCount() {
        return Participantslist == null ? 0 : Participantslist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Button btParticipantRemove;
        public TextView Pplname;
        public ImageView ivPpic;
        public LinearLayout linearParticipateRow;
        Button btn_ppl_srch;

        public ViewHolder(View v) {
            super(v);
            btParticipantRemove = (Button) v.findViewById(R.id.btParticipantRemove);
            btn_ppl_srch = (Button) v.findViewById(R.id.btn_ppl_srch);
            Pplname = (TextView) v.findViewById(R.id.pplname);
            ivPpic = (ImageView) v.findViewById(R.id.ivPpic);
            linearParticipateRow = (LinearLayout) v.findViewById(R.id.linearParticipateRow);
        }

    }

}
