package com.bluebuddy.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bluebuddy.R;
import com.bluebuddy.activities.ChatMessagingActivity;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AfterLoginStatus;
import com.bluebuddy.models.MessageDetails;
import com.bluebuddy.models.ProfileDetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.api.ApiClient.BASE_URL;
import static com.bluebuddy.app.AppConfig.IMG_URL;

public class ListMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<MessageDetails> consersation;
    private String currentUserId;
    private String token;
    private String image;
    private String name;

    public ListMessageAdapter(Context context, ArrayList<MessageDetails> conversation, String currentUserId, String token, String image, String name) {

        this.context = context;
        this.consersation = conversation;
        this.currentUserId = currentUserId;
        this.token = token;
        this.image = image;
        this.name = name;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ChatMessagingActivity.VIEW_TYPE_FRIEND_MESSAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_friend, parent, false);
            return new ItemMessageFriendHolder(view);
        } else if (viewType == ChatMessagingActivity.VIEW_TYPE_USER_MESSAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_user, parent, false);
            return new ItemMessageUserHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemMessageFriendHolder) {
            ((ItemMessageFriendHolder) holder).txtContent.setText(consersation.get(position).getContent());
            String time = new SimpleDateFormat("EEE, d MMM yyyy").format(new Date((Long) consersation.get(position).getTimestamp()));
            String today = new SimpleDateFormat("EEE, d MMM yyyy").format(new Date(System.currentTimeMillis()));
            if (today.equals(time)) {
                ((ItemMessageFriendHolder) holder).time.setText(new SimpleDateFormat("HH:mm").format(new Date((Long) consersation.get(position).getTimestamp())));
            } else {
                ((ItemMessageFriendHolder) holder).time.setText(new SimpleDateFormat("MMM d").format(new Date((Long) consersation.get(position).getTimestamp())));
            }

            if (image.contains(BASE_URL)) {
                ((ItemMessageFriendHolder) holder).nameletter.setVisibility(View.GONE);
                ((ItemMessageFriendHolder) holder).avata.setVisibility(View.VISIBLE);
                Glide.with(context).asBitmap().load(image).into(new BitmapImageViewTarget(((ItemMessageFriendHolder) holder).avata) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        ((ItemMessageFriendHolder) holder).avata.setImageDrawable(circularBitmapDrawable);
                    }
                });

//            pro_pic.setImageBitmap(textAsBitmap(String.valueOf(name.charAt(0)), 23, R.color.text_image_background));

//            Glide.with(ChatMessagingActivity.this).load(bitmapToByte(this,R.drawable.badge1,String.valueOf(name.charAt(0)))).asBitmap().centerCrop().into(new BitmapImageViewTarget(pro_pic) {
//                @Override
//                protected void setResource(Bitmap resource) {
//                    RoundedBitmapDrawable circularBitmapDrawable =
//                            RoundedBitmapDrawableFactory.create(ChatMessagingActivity.this.getResources(), resource);
//                    circularBitmapDrawable.setCircular(true);
//                    pro_pic.setImageDrawable(circularBitmapDrawable);
//                }
//            });

            } else {

                ((ItemMessageFriendHolder) holder).nameletter.setVisibility(View.VISIBLE);
                ((ItemMessageFriendHolder) holder).nameletter.setText(String.valueOf(name.charAt(0)));
//                ((ItemMessageFriendHolder) holder).nameletter.setBackgroundResource(R.drawable.badge1);
                ((ItemMessageFriendHolder) holder).avata.setVisibility(View.GONE);

            }

//            if (image != null) {
//                Glide.with(context).load(image).asBitmap().centerCrop().into(new BitmapImageViewTarget(((ItemMessageFriendHolder) holder).avata) {
//                    @Override
//                    protected void setResource(Bitmap resource) {
//                        RoundedBitmapDrawable circularBitmapDrawable =
//                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
//                        circularBitmapDrawable.setCircular(true);
//                        ((ItemMessageFriendHolder) holder).avata.setImageDrawable(circularBitmapDrawable);
//                    }
//                });
//            }
        } else if (holder instanceof ItemMessageUserHolder) {

            ((ItemMessageUserHolder) holder).txtContent.setText(consersation.get(position).getContent());
            String time = new SimpleDateFormat("EEE, d MMM yyyy").format(new Date((Long) consersation.get(position).getTimestamp()));
            String today = new SimpleDateFormat("EEE, d MMM yyyy").format(new Date(System.currentTimeMillis()));
            if (today.equals(time)) {
                ((ItemMessageUserHolder) holder).time.setText(new SimpleDateFormat("HH:mm").format(new Date((long) consersation.get(position).getTimestamp())));
            } else {
                ((ItemMessageUserHolder) holder).time.setText(new SimpleDateFormat("MMM d").format(new Date((long) consersation.get(position).getTimestamp())));

            }

            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<AfterLoginStatus> userCall = service.mpa("Bearer " + token);

            userCall.enqueue(new Callback<AfterLoginStatus>() {
                @Override
                public void onResponse(Call<AfterLoginStatus> call, Response<AfterLoginStatus> response) {

                    if (response.body().getStatus() == true) {
                        Log.d("Got status", "" + response.body().getStatus());
                        Log.d("Profile_details3 sdata", "" + response.body().getProfile_details());
                        ProfileDetails p = response.body().getProfile_details();

                        if (p != null) {
                            String profile_Pic = p.getProfile_pic();
                            Glide.with(((ItemMessageUserHolder) holder).avata.getRootView().getContext()).asBitmap().load(IMG_URL + profile_Pic).into(new BitmapImageViewTarget(((ItemMessageUserHolder) holder).avata) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    ((ItemMessageUserHolder) holder).avata.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                        }
                    }
                }

                @Override
                public void onFailure(Call<AfterLoginStatus> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                }
            });

        }
    }

    @Override
    public int getItemViewType(int position) {
        return consersation.get(position).getFromID().equalsIgnoreCase(currentUserId) ? ChatMessagingActivity.VIEW_TYPE_USER_MESSAGE : ChatMessagingActivity.VIEW_TYPE_FRIEND_MESSAGE;
    }

    @Override
    public int getItemCount() {

        return consersation == null ? 0 : consersation.size();

    }

    class ItemMessageFriendHolder extends RecyclerView.ViewHolder {
//        public RelativeLayout rl_nameletter;
        public MyTextView txtContent, time, nameletter;
        public ImageView avata;

        public ItemMessageFriendHolder(View itemView) {
            super(itemView);
            txtContent = (MyTextView) itemView.findViewById(R.id.textContentFriend);
            time = (MyTextView) itemView.findViewById(R.id.timecontent);
            nameletter = (MyTextView) itemView.findViewById(R.id.nameletter);
            avata = (ImageView) itemView.findViewById(R.id.imageView3);
//            rl_nameletter = (RelativeLayout) itemView.findViewById(R.id.rl_nameletter);
        }
    }

    class ItemMessageUserHolder extends RecyclerView.ViewHolder {
        public MyTextView txtContent, time;
        public ImageView avata;

        public ItemMessageUserHolder(View itemView) {
            super(itemView);
            txtContent = (MyTextView) itemView.findViewById(R.id.textContentUser);
            time = (MyTextView) itemView.findViewById(R.id.timecontent);
            avata = (ImageView) itemView.findViewById(R.id.imageView2);
        }
    }

}