package com.bluebuddy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.bluebuddy.R;
import com.bluebuddy.activities.BlueMarketPicEditActivity;
import com.bluebuddy.activities.ListingQueryActivityProduct2;
import com.bluebuddy.activities.ProductDetailActivitynewlay2;
import com.bluebuddy.activities.SingleChatActivity;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.data.StaticConfig;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllProductDetails;
import com.bluebuddy.models.FlagResponse;
import com.bluebuddy.models.Friend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.IMG_URL;

/**
 * Created by admin on 5/13/2017.
 */

public class BlueMarketProductAdapter2 extends RecyclerView.Adapter<BlueMarketProductAdapter2.ViewHolder> {
    public static Map<String, Boolean> mapMark;
    private List<AllProductDetails> BMCList;
    private Activity _activity;
    private Context _context;
    private String token, curru;
    private SharedPreferences pref;

    public BlueMarketProductAdapter2() {

    }

    public BlueMarketProductAdapter2(Activity a, Context c, List<AllProductDetails> BMCList, String token, String curru) {
        this._activity = a;
        this._context = c;
        this.BMCList = BMCList;
        this.token = token;
        this.curru = curru;
    }

    @Override
    public BlueMarketProductAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Creating a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blue_market_cardco, parent, false);

        //set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        mapMark = new HashMap<>();
        return vh;
    }

    @Override
    public void onBindViewHolder(final BlueMarketProductAdapter2.ViewHolder holder, int position) {
        final AllProductDetails allProductDetails = BMCList.get(position);

        holder.Bmcname.setText(String.valueOf(allProductDetails.getProductNm()));
//        holder.Bmcprice.setText("$" + allProductDetails.getPrice());
        holder.Bmcprice.setText(_context.getString(R.string.price_with_dollar, allProductDetails.getPrice()));
        holder.Bmcloc.setText(allProductDetails.getLocation());
        holder.Bmcposted.setText(allProductDetails.getFname() + allProductDetails.getLname());
//        String IMAGE[] = allProductDetails.getImage().split(",");

//        if(allProductDetails.getProductImages().size() > 0)
//        {
//            String imageName = allProductDetails.getProductImages().get(0).getImage();
//            Glide.with(_activity).load(IMG_URL+imageName).asBitmap().into(new BitmapImageViewTarget(holder.Bmcpic));
//        }else{
//            Glide.with(_activity).load(R.drawable.ic_image_black).asBitmap().into(new BitmapImageViewTarget(holder.Bmcpic));
//        }

//        Glide.with(_activity).load(R.drawable.ic_image_black).asBitmap().into(new BitmapImageViewTarget(holder.Bmcpic));


        Glide.with(_activity).asBitmap().load(IMG_URL + allProductDetails.getImage()).into(new BitmapImageViewTarget(holder.Bmcpic));

        //
        if (allProductDetails.getFeatured().equals("1")) {
            holder.feattag.setVisibility(View.VISIBLE);
        } else if (allProductDetails.getFeatured().equals("0")) {
            holder.feattag.setVisibility(View.GONE);
        }
        //
        holder.bmcedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(_activity, BlueMarketPicEditActivity.class);
//                Intent i = new Intent(v.getContext(), ProductPicEditActivity.class);
                i.putExtra("AllProduct", allProductDetails.getId());
                i.putExtra("category", " ");
                i.putExtra("pass", "2");
                v.getContext().startActivity(i);
            }
        });
        holder.bmcDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_activity, ProductDetailActivitynewlay2.class);
                intent.putExtra("AllProduct_Firebaseid", allProductDetails.getFirebaseId());
                intent.putExtra("AllProduct", String.valueOf(allProductDetails.getId()));
                _activity.startActivity(intent);
            }
        });
        holder.linearCourseChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(allProductDetails.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            String id = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();

                            HashMap userMap = (HashMap) ((HashMap) dataSnapshot.getValue()).get(id);
                            Friend user = new Friend();
                            /*--*/
                            String name = (String) userMap.get("name");
                            /*--*/
                            //user.name = (String) userMap.get("name");
                            user.email = (String) userMap.get("email");
                            user.avata = (String) userMap.get("avata");
                            user.id = id;
                            user.idRoom = id.compareTo(StaticConfig.UID) > 0 ? (StaticConfig.UID + id).hashCode() + "" : "" + (id + StaticConfig.UID).hashCode();

                            Intent intent = new Intent(v.getContext(), SingleChatActivity.class);
                            ArrayList<CharSequence> idFriend = new ArrayList<CharSequence>();
                            idFriend.add(user.id);
                            /*--*/
                            //  intent.putExtra("AllCharter",AllCharterId);
                            // intent.putExtra("email",email);
                            /*---*/
                            intent.putCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID, idFriend);
                            intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID, user.idRoom);
                            intent.putExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND, name);

                            mapMark.put(user.id, null);
                            v.getContext().startActivity(intent);
                            //  _activity.startActivityForResult(intent, Blue_Charter_Details.ACTION_START_CHAT);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        if (Integer.valueOf(allProductDetails.getIsEditable()) > 0) {
            holder.linearCourseChat.setVisibility(View.GONE);
            holder.linearCourseEdit.setVisibility(View.VISIBLE);
        } else {
            holder.linearCourseEdit.setVisibility(View.GONE);
            holder.linearCourseChat.setVisibility(View.VISIBLE);
        }

        if (allProductDetails.getUserId().equals(curru)) {
            holder.flag.setVisibility(View.GONE);
        } else if (!allProductDetails.getUserId().equals(curru)) {
            if (allProductDetails.getIsFlagged().equals("0")) {
                holder.flag.setVisibility(View.VISIBLE);
                holder.flag.setImageResource(R.drawable.ic_flag);
                holder.flag.setOnClickListener(new View.OnClickListener() {
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
                                Call<FlagResponse> userCall = service.fr("Bearer " + token, "post", "product", allProductDetails.getId());

                                userCall.enqueue(new Callback<FlagResponse>() {
                                    @Override
                                    public void onResponse(Call<FlagResponse> call, Response<FlagResponse> response) {
                                        if (response.body().getStatus() == "true") {
                                            Log.d("fr comment", "" + response.body().getStatus());
                                            holder.flag.setImageResource(R.drawable.ic_flagred);
                                            holder.flag.setClickable(false);
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

                    }
                });
            } else if (allProductDetails.getIsFlagged().equals("1")) {
                holder.flag.setVisibility(View.VISIBLE);
                holder.flag.setImageResource(R.drawable.ic_flagred);
            }

        }

        //today 21aug

        if (allProductDetails.getFeatured().equals("0")) {
            holder.tripOptions7.setVisibility(View.VISIBLE);
        } else if (allProductDetails.getFeatured().equals("1")) {
            holder.tripOptions7.setVisibility(View.GONE);
        }

        holder.tripOptions7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(v.getContext(), holder.tripOptions7);
                //inflating menu from xml resource
                popup.inflate(R.menu.trip_option_menufeature);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                Intent i = new Intent(_activity, ListingQueryActivityProduct2.class);
                                i.putExtra("ID", allProductDetails.getId());
                                _activity.startActivity(i);
                                //handle menu1 click
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

        //today 21aug
    }

    @Override
    public int getItemCount() {
        return BMCList == null ? 0 : BMCList.size();
    }

    public void updateAllProduct(ArrayList<AllProductDetails> details) {
        BMCList = details;
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //each data item is just a string in this case
        public CardView cardView;
        public TextView Bmcname, Bmcprice, Bmcloc, Bmcposted;
        public ImageView Bmcpic, flag, feattag;
        public Button bmcDt, tripOptions7, chatBtn, bmcedt;
        public LinearLayout linearCourseChat, linearCourseEdit;

        public ViewHolder(View v) {
            super(v);
            tripOptions7 = (Button) v.findViewById(R.id.tripOptions7);
            feattag = (ImageView) v.findViewById(R.id.feattag);
            cardView = (CardView) v.findViewById(R.id.card_view_bm);
            Bmcname = (TextView) v.findViewById(R.id.bmcname);
            Bmcprice = (TextView) v.findViewById(R.id.bmcprice);
            Bmcloc = (TextView) v.findViewById(R.id.bmcloc);
            Bmcposted = (TextView) v.findViewById(R.id.bmcposted);
            Bmcpic = (ImageView) v.findViewById(R.id.bmcpic);
            flag = (ImageView) v.findViewById(R.id.report_flag);
            bmcDt = (Button) v.findViewById(R.id.bmcdet);
            bmcedt = (Button) v.findViewById(R.id.bmcedt);
            chatBtn = (Button) v.findViewById(R.id.chatBtn);
            linearCourseChat = (LinearLayout) v.findViewById(R.id.linearCourseChat);
            linearCourseEdit = (LinearLayout) v.findViewById(R.id.linearCourseEdit);
        }
    }

}
