package com.bluebuddy.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluebuddy.R;
import com.bluebuddy.models.PeopleEventItems;

import java.util.List;

/**
 * Created by admin on 5/13/2017.
 */

public class PeopleProfileAllEventAdapter extends RecyclerView.Adapter<PeopleProfileAllEventAdapter.ViewHolder> {

    private List<PeopleEventItems> EVENTList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //each data item is just a string in this case
        public TextView tvuname, tvevtName, tvfrdate, tvtodate, tvloc, tvdesc;
        public ImageView ivupic;

        public ViewHolder(View v) {
            super(v);
            tvevtName = (TextView) v.findViewById(R.id.txtevttitleid);
            tvfrdate = (TextView) v.findViewById(R.id.txtevtdatefromid);
            tvloc = (TextView) v.findViewById(R.id.txtevtlocid);
            tvdesc = (TextView) v.findViewById(R.id.txtevtdescid);
        }
    }

    public PeopleProfileAllEventAdapter(List<PeopleEventItems> EVENTList) {
        this.EVENTList = EVENTList;
    }

    @Override
    public PeopleProfileAllEventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Creating a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_event_card, parent, false);

        //set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PeopleProfileAllEventAdapter.ViewHolder holder, int position) {

        PeopleEventItems peopleEventItems = EVENTList.get(position);

        holder.tvevtName.setText(peopleEventItems.getEname());
        holder.tvfrdate.setText(peopleEventItems.getEfromdate());
        holder.tvloc.setText(peopleEventItems.getEloc());
        holder.tvdesc.setText(peopleEventItems.getEdesc());


    }

    @Override
    public int getItemCount() {
        return EVENTList.size();
    }
}
