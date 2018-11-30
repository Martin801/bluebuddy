package com.bluebuddy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bluebuddy.R;
import com.bluebuddy.activities.PeopleProfileActivity;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.Cert_user;
import com.bluebuddy.models.CommonResponse;
import com.bluebuddy.models.PeopleDetail;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.IMG_URL;


public class PeopleSearchAdapter extends RecyclerView.Adapter<PeopleSearchAdapter.ViewHolder> implements Filterable {

    private ArrayList<PeopleDetail> PEOPLEslist;
    private ArrayList<PeopleDetail> mFilteredlist;
    private Activity _activity;
    private Context _context;
    private String token;
    boolean flag = true;

    private ImageView ivPcert1, jvPcert2;

    public PeopleSearchAdapter(Activity a, Context c, ArrayList<PeopleDetail> PEOPLEslist, String token) {

        this._activity = a;
        this._context = c;
        this.PEOPLEslist = PEOPLEslist;
        this.mFilteredlist = PEOPLEslist;
        this.token = token;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //each data item is just a string in this case
        public Button btn_ppl_srch, showm;
        public TextView Pplname, Pplloc, Pplint;
        public ImageView ivPpic;
        public ImageView ivPcert1, jvPcert2;
        public CardView llv1;

        public ViewHolder(View v) {
            super(v);
            btn_ppl_srch = (Button) v.findViewById(R.id.btn_ppl_srch);
            showm = (Button) v.findViewById(R.id.showm);
            Pplname = (TextView) v.findViewById(R.id.pplname);
            Pplloc = (TextView) v.findViewById(R.id.pplloc);
            Pplint = (TextView) v.findViewById(R.id.pplcert_type);
            ivPpic = (ImageView) v.findViewById(R.id.pplimg);

            ivPcert1 = (ImageView) v.findViewById(R.id.pplcert1);
            jvPcert2 = (ImageView) v.findViewById(R.id.pplcert2);
            llv1 = (CardView) v.findViewById(R.id.ppllist2);
        }
    }


    @Override
    public PeopleSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_search_list, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final PeopleDetail peopleDetail = mFilteredlist.get(position);

        holder.Pplname.setText(String.valueOf(peopleDetail.getFirst_name()) + " " + String.valueOf(peopleDetail.getLast_name()));
        holder.Pplloc.setText(peopleDetail.getLocation());
        holder.Pplint.setText(peopleDetail.getCertification_type());
        String sg = peopleDetail.getProfile_pic().toString().trim();
        Log.d("image", sg);

        ArrayList<Cert_user> certification_details = peopleDetail.getCertification_details();

        if (certification_details != null) {
            int len = certification_details.size();

            if (len == 0) {
                //if (certification_details.get(0).getCert_type().equals("Scuba_Diving") && certification_details.get(1).getCert_type().equals("Free_Diving")||certification_details.get(0).getCert_type().equals("Free_Diving")&& certification_details.get(1).getCert_type().equals("Scuba_Diving"))
                holder.ivPcert1.setVisibility(View.GONE);
                holder.jvPcert2.setVisibility(View.GONE);

            } else if (len == 1) {
                if (certification_details.get(0).getCert_type().equals("Scuba_Diving") && !certification_details.get(0).getCert_type().equals("Free_Diving")) {
                    holder.ivPcert1.setVisibility(View.VISIBLE);
                    holder.jvPcert2.setVisibility(View.GONE);
                } else if (certification_details.get(0).getCert_type().equals("Free_Diving") && !certification_details.get(0).getCert_type().equals("Scuba_Diving")) {
                    holder.jvPcert2.setVisibility(View.VISIBLE);
                    holder.ivPcert1.setVisibility(View.GONE);
                }
            } else if (len == 2) {
                holder.ivPcert1.setVisibility(View.VISIBLE);
                holder.jvPcert2.setVisibility(View.VISIBLE);
            }
        }

        Glide.with(_context).asBitmap().load(IMG_URL + peopleDetail.getProfile_pic()).into(new BitmapImageViewTarget(holder.ivPpic) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(_activity.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.ivPpic.setImageDrawable(circularBitmapDrawable);
            }
        });

        holder.showm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), holder.showm);
                popup.inflate(R.menu.trip_option_menu1);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                                Call<CommonResponse> userCall = service.addBuddy("Bearer " + token, peopleDetail.getId());

                                userCall.enqueue(new Callback<CommonResponse>() {
                                    @Override
                                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                                        Log.d("STATUS", String.valueOf(response.body().getStatus()));

                                        if (response.body().getStatus() == true) {
                                            //  openDialog11("Invitation sent successfully", "Ok", "", true,_myactivity, "PeopleProfileActivity", 0,puserid);
                                            MenuItem item1, item2, item3, item4, item5;
                                        } else if (response.body().getStatus() == false) {
                                            MenuItem item1, item2, item3, item4, item5;
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                                        t.printStackTrace();
                                    }
                                });

                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

        holder.btn_ppl_srch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(v.getContext(), PeopleProfileActivity.class);
                i.putExtra("uid", peopleDetail.getId());
                v.getContext().startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mFilteredlist == null ? 0 : mFilteredlist.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                Toast.makeText(_activity, "Entry Text. "+charString, Toast.LENGTH_SHORT).show();

                if (!charString.isEmpty()) {

                    ArrayList<PeopleDetail> filteredlist = new ArrayList<>();

                    for (PeopleDetail peopleDetail : PEOPLEslist) {

                        Toast.makeText(_activity, ""+peopleDetail.getFirst_name()+"All Details", Toast.LENGTH_SHORT).show();
                        if (peopleDetail.getFirst_name().toLowerCase().contains(charString)) {

                            Toast.makeText(_activity, ""+peopleDetail.getFirst_name(), Toast.LENGTH_SHORT).show();
                            filteredlist.add(peopleDetail);

                        }

//                        if (peopleDetail.getFirst_name().toLowerCase().contains(charString) || peopleDetail.getLast_name().toLowerCase().contains(charString) || peopleDetail.getEmail().toLowerCase().contains(charString)) {
//
//                            filteredlist.add(peopleDetail);
//                        }

                    }

                    mFilteredlist = filteredlist;

                } else {
                    mFilteredlist = PEOPLEslist;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredlist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredlist = (ArrayList<PeopleDetail>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void updateAllPeople(ArrayList<PeopleDetail> peopleDetail) {
        PEOPLEslist = peopleDetail;
        this.notifyDataSetChanged();
    }

}
