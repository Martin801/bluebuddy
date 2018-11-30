package com.bluebuddy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluebuddy.R;
import com.bluebuddy.activities.CreateProfileActivityfreedivingedit;
import com.bluebuddy.activities.MyCertificationActivity;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.CertificateDetail;
import com.bluebuddy.models.CommonResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 5/13/2017.
 */

public class CertAdapter extends RecyclerView.Adapter<CertAdapter.ViewHolder> {

    private List<CertificateDetail> BMCList;
    private Activity _activity;
    private Context _context;
    private String token, category;

    public CertAdapter() {
    }

    public CertAdapter(Activity a, Context c, List<CertificateDetail> BMCList, String token) {
        this._activity = a;
        this._context = c;
        this.BMCList = BMCList;
        this.token = token;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //each data item is just a string in this case
        public CardView cardView;
        public TextView ctype, cname, cnum, clvl, clvl2, clvl3;
        public ImageView Bmcpic;
        public Button bmcDlt, bmcedt, edtbtn;
        public LinearLayout linearCourseChat, linearCourseEdit, crtlstv2, crtlstv3;

        public ViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.card_view_cert);
            ctype = (TextView) v.findViewById(R.id.ctype);
            cname = (TextView) v.findViewById(R.id.cname);
            cnum = (TextView) v.findViewById(R.id.cnum);
            clvl = (TextView) v.findViewById(R.id.clvl);
            clvl2 = (TextView) v.findViewById(R.id.clvl2);
            clvl3 = (TextView) v.findViewById(R.id.clvl3);
            edtbtn = (Button) v.findViewById(R.id.chatBtn);
            bmcDlt = (Button) v.findViewById(R.id.bmcdet);
            crtlstv2 = (LinearLayout) v.findViewById(R.id.crtlstv2);
            crtlstv3 = (LinearLayout) v.findViewById(R.id.crtlstv3);
          /*  Bmcname = (TextView) v.findViewById(R.id.bmcname);
            Bmcprice = (TextView) v.findViewById(R.id.bmcprice);
            Bmcloc = (TextView) v.findViewById(R.id.bmcloc);
            Bmcposted = (TextView) v.findViewById(R.id.bmcposted);
            Bmcpic = (ImageView) v.findViewById(R.id.bmcpic);

            bmcDt = (Button) v.findViewById(R.id.bmcdet);
            bmcedt = (Button) v.findViewById(R.id.bmcedt);
            chatBtn = (Button) v.findViewById(R.id.chatBtn);

            linearCourseChat = (LinearLayout) v.findViewById(R.id.linearCourseChat);
            linearCourseEdit = (LinearLayout) v.findViewById(R.id.linearCourseEdit);*/
        }

    }

    @Override
    public CertAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Creating a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cert_card, parent, false);

        //set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CertAdapter.ViewHolder holder, int position) {
        final CertificateDetail detail = BMCList.get(position);

        if (detail.getCert_type().equals("Scuba_Diving")) {
            holder.ctype.setText("Scuba Diving");
        } else if (detail.getCert_type().equals("Free_Diving")) {
            holder.ctype.setText("Free Diving");
        }

        // holder.ctype.setText(detail.getCert_type());
        holder.cname.setText(detail.getCert_name());
        holder.cnum.setText(detail.getCert_no());
        //   holder.clvl.setText(detail.getCert_level());
       // holder.clvl.setText(detail.getCert_level());
        if (detail.getCert_level().contains(":")) {
            String[] separated = detail.getCert_level().split(":");
            // separated[0];
            // separated[1];
            if (separated.length == 0) {
                separated[0] = separated[0].trim();
                holder.clvl.setText(separated[0]);
                Log.d("lvl 1", separated[0]);
            } else if (separated.length == 1) {
                separated[0] = separated[0].trim();
                separated[1] = separated[1].trim();
                holder.clvl.setText(separated[0]);
                holder.clvl2.setText(separated[1]);
                Log.d("lvl 1", separated[0]);
                Log.d("lvl 2", separated[1]);
            } else if (separated.length == 2) {
                holder.crtlstv2.setVisibility(View.VISIBLE);
                separated[0] = separated[0].trim();
                separated[1] = separated[1].trim();
                // separated[2] = separated[2].trim();
                holder.clvl.setText(separated[0]);
                holder.clvl2.setText(separated[1]);
                //   holder.clvl3.setText(separated[2]);
                Log.d("lvl 1", separated[0]);
                Log.d("lvl 2", separated[1]);
                //   Log.d("lvl 3",separated[2]);

            } else if (separated.length == 3) {
                holder.crtlstv2.setVisibility(View.VISIBLE);
                holder.crtlstv3.setVisibility(View.VISIBLE);
                separated[0] = separated[0].trim();
                separated[1] = separated[1].trim();
                separated[2] = separated[2].trim();
                holder.clvl.setText(separated[0]);
                holder.clvl2.setText(separated[1]);
                holder.clvl3.setText(separated[2]);
                Log.d("lvl 1", separated[0]);
                Log.d("lvl 2", separated[1]);
                Log.d("lvl 3", separated[2]);
            }
           /* separated[0] = separated[0].trim();
            separated[1] = separated[1].trim();
            separated[2] = separated[2].trim();*/
           /* holder.clvl.setText(separated[0]);
            holder.clvl2.setText(separated[1]);
            holder.clvl3.setText(separated[2]);*/
        } else if (!detail.getCert_level().contains(":")) {
            holder.clvl.setText(detail.getCert_level());
            /*String[] separated = detail.getCert_level().split(":");
            separated[0] = separated[0].trim();
            holder.clvl.setText(separated[0]);*/

        }

      /*  final AllCourseDetails allCourseDetails = BMCList.get(position);
        holder.Bmcname.setText(String.valueOf(allCourseDetails.getCategory()));
        holder.Bmcprice.setText("$"+allCourseDetails.getPrice());
        holder.Bmcloc.setText(allCourseDetails.getLocation());
        holder.Bmcposted.setText(allCourseDetails.getFname()+allCourseDetails.getLname());

        Glide.with(_activity).load(IMG_URL+allCourseDetails.getImage()).asBitmap().into(new BitmapImageViewTarget(holder.Bmcpic));

        holder.bmcDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_activity, CourseDetailsActivitynewlay.class);
               // intent.putExtra("AllCourse", String.valueOf(allCourseDetails.getId()));
              //  intent.putExtra("category", category);
                _activity.startActivity(intent);
            }
        });

        if(Integer.valueOf(allCourseDetails.getIs_editable()) > 0){
            holder.linearCourseChat.setVisibility(View.GONE);
            holder.linearCourseEdit.setVisibility(View.VISIBLE);
        }else{
            holder.linearCourseEdit.setVisibility(View.GONE);
            holder.linearCourseChat.setVisibility(View.VISIBLE);
        }

        holder.bmcedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(_activity, BlueMarketCoursePicEditActivity.class);
               // Intent i = new Intent(_activity, CourseEditActivity.class);
            //    i.putExtra("COURSE_ID", allCourseDetails.getId());
              //  i.putExtra("category", category);
                _activity.startActivity(i);
            }
        });*/
        holder.bmcDlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                Call<CommonResponse> userCall = service.deleteapi("Bearer " + token, "certification", detail.getId());

                userCall.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        if (response.body().getStatus() == true) {
                            Log.d("onResponse", "" + response.body().getStatus());
                            Intent i = new Intent(_context, MyCertificationActivity.class);
                            _context.startActivity(i);
                            // oDialog("Product Image updated Successfully.", "Submit", "", true, _myActivity, "BlueMarketProductEditActivity", 0, id);


                        } else {
                            Log.d("onResponse", "" + response.body().getStatus());
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {

                    }
                });
            }
        });
        holder.edtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*try {
                  Intent   intent = new Intent(_activity, Class.forName("com.example.admin.bluebuddy.activities."+edtrc));
                  _activity.startActivity(intent);
              } catch (ClassNotFoundException e) {
                  e.printStackTrace();
              }*/

                Intent i = new Intent(_activity, CreateProfileActivityfreedivingedit.class);
                i.putExtra("cert_id", detail.getId());
                i.putExtra("cert_type", detail.getCert_type());
                i.putExtra("cert_name", detail.getCert_name());
                i.putExtra("cert_num", detail.getCert_no());
                i.putExtra("cert_lvl", detail.getCert_level());
                _activity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return BMCList == null ? 0 : BMCList.size();
    }

    public void updateAllCert(ArrayList<CertificateDetail> details) {
        BMCList = details;
        this.notifyDataSetChanged();
    }

}
