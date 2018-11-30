package com.bluebuddy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluebuddy.R;
import com.bluebuddy.activities.BlueMarketActivity;
import com.bluebuddy.activities.FeaturedListingActivity;
import com.bluebuddy.activities.FreeListingActivity;
import com.bluebuddy.activities.MyListDetailsActivity;
import com.bluebuddy.models.BlueMarketItems;

import java.util.List;

/**
 * Created by admin on 5/13/2017.
 */

public class MyListingAdapter extends RecyclerView.Adapter<MyListingAdapter.ViewHolder> {

    private List<BlueMarketItems> BMCList;
    private Activity _activity;
    private Context _context;
    private BlueMarketActivity blueMarketActivity;
    private FreeListingActivity freeListingActivity;
    private FeaturedListingActivity featuredListingActivity;

    public MyListingAdapter() {
    }


    public MyListingAdapter(Activity a, Context c, List<BlueMarketItems> BMCList) {
        this._activity = a;
//        this.blueMarketActivity = (BlueMarketActivity) a;

        //  this.freeListingActivity = (FreeListingActivity)a;
        //this.featuredListingActivity = (FeaturedListingActivity)a;
        this._context = c;
        this.BMCList = BMCList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //each data item is just a string in this case
        public CardView cardView;
        public TextView Bmcname, Bmcprice, Bmcloc, Bmcposted;
        public ImageView Bmcpic;
        public Button bmcDt;

        public ViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.card_view_bm);
            Bmcname = (TextView) v.findViewById(R.id.bmcname);
            Bmcprice = (TextView) v.findViewById(R.id.bmcprice);
            Bmcloc = (TextView) v.findViewById(R.id.bmcloc);
            Bmcposted = (TextView) v.findViewById(R.id.bmcposted);
            Bmcpic = (ImageView) v.findViewById(R.id.bmcpic);
            bmcDt = (Button) v.findViewById(R.id.bmcdet);
        }

    }

    @Override
    public MyListingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Creating a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blue_market_card, parent, false);

        //set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyListingAdapter.ViewHolder holder, int position) {

        BlueMarketItems blueMarketItems = BMCList.get(position);
        holder.Bmcname.setText(String.valueOf(blueMarketItems.getBMCname()));
        holder.Bmcprice.setText(blueMarketItems.getBMCprice());
        holder.Bmcloc.setText(blueMarketItems.getBMCloc());
        holder.Bmcposted.setText(blueMarketItems.getBMCposted());
        holder.Bmcpic.setImageResource(blueMarketItems.getBMCpic());

        holder.bmcDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_activity, MyListDetailsActivity.class);
                _activity.startActivity(intent);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Toast.makeText(_context,"Click me",Toast.LENGTH_SHORT).show();
                final String rc = "MyAccountMenu";
                blueMarketActivity.oDialog("", "Unfeatured", "Featured this list", "Delete", "Cancel", true, _activity, rc, 0);
                // final String rc = "BlueMarketActivity";
                //   freeListingActivity.oDialog("","Unfeatured", "Featured this list","Delete", "Cancel",true, _activity, rc,0);
                // featuredListingActivity.oDialog("","Edit", "Unfeatured","Delete", "Cancel",true, _activity, rc,0);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return BMCList.size();
    }
}
