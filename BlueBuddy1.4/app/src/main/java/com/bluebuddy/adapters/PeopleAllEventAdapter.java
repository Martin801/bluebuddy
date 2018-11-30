package com.bluebuddy.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluebuddy.R;
import com.bluebuddy.models.AllEventItems;

import java.util.List;

/**
 * Created by admin on 5/13/2017.
 */

public class PeopleAllEventAdapter extends RecyclerView.Adapter<PeopleAllEventAdapter.ViewHolder> {

    private List<AllEventItems> EVENTList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //each data item is just a string in this case
        public TextView tvuname, tvevtName, tvfrdate, tvtodate, tvloc, tvdesc;
        public ImageView ivupic;

        public ViewHolder(View v) {
            super(v);
            tvuname = (TextView) v.findViewById(R.id.txtunameid);
            tvevtName = (TextView) v.findViewById(R.id.txtevttitleid);
            tvfrdate = (TextView) v.findViewById(R.id.txtevtdatefromid);
            tvtodate = (TextView) v.findViewById(R.id.txtevtdattid);
            tvloc = (TextView) v.findViewById(R.id.txtevtlocid);
            tvdesc = (TextView) v.findViewById(R.id.txtevtdescid);
            ivupic = (ImageView) v.findViewById(R.id.txtupicid);
        }
    }

    public PeopleAllEventAdapter(List<AllEventItems> EVENTList) {
        this.EVENTList = EVENTList;
    }

    @Override
    public PeopleAllEventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Creating a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_events_card, parent, false);

        //set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PeopleAllEventAdapter.ViewHolder holder, int position) {

        AllEventItems allEventItems = EVENTList.get(position);
        holder.tvuname.setText(String.valueOf(allEventItems.getUname()));
        holder.tvevtName.setText(allEventItems.getEname());
        holder.tvfrdate.setText(allEventItems.getEfromdate());
        holder.tvtodate.setText(allEventItems.getEtodate());
        holder.tvloc.setText(allEventItems.getEloc());
        holder.tvdesc.setText(allEventItems.getEdesc());
        holder.ivupic.setImageResource(allEventItems.getUpic());

    }

    @Override
    public int getItemCount() {
        return EVENTList.size();
    }
}
