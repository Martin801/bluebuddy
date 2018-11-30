package com.bluebuddy.models;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluebuddy.R;

public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView Srchpname;
    public ImageView Srchppic;
    public CardView CVPS;


    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        Srchpname = (TextView) itemView.findViewById(R.id.pplname);
        Srchppic = (ImageView) itemView.findViewById(R.id.pplimg);
        CVPS = (CardView) itemView.findViewById(R.id.card_view44);
    }

    @Override
    public void onClick(View view) {
        //  Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
        //  Intent i = new Intent(PeopleSearchGallery.this , PeopleSearch.class);


    }
}