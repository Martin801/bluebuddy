package com.bluebuddy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluebuddy.R;
import com.bluebuddy.activities.LocationBasedSearchActivity;
import com.bluebuddy.models.PeopleSearchGalleryItems;
import com.bluebuddy.models.RecyclerViewHolders;

import java.util.List;

public class LocationBasedRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    private List<PeopleSearchGalleryItems> itemList;
    private Activity _activity;
    private Context context;

    public LocationBasedRecyclerViewAdapter(Activity a, Context context, List<PeopleSearchGalleryItems> itemList) {
        this._activity = a;
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.peoplesearchgallerycard, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        holder.Srchpname.setText(itemList.get(position).getSrchpname());
        holder.Srchppic.setImageResource(itemList.get(position).getSrchppic());
        holder.CVPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_activity, LocationBasedSearchActivity.class);
                _activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
