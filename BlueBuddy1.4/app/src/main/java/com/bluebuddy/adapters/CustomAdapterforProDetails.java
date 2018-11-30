package com.bluebuddy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bluebuddy.R;
import com.bluebuddy.activities.ImageSliderActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;

import static com.bluebuddy.app.AppConfig.IMG_URL;

public class CustomAdapterforProDetails extends PagerAdapter {

    //  private int[] images = {R.drawable.mask1, R.drawable.mask2,R.drawable.mask3,R.drawable.mask1, R.drawable.mask5};
    private Context ctx;
    //private ArrayList<AllProductDetails> BMCList;
    private ArrayList<String> BMCList;
    private Activity _activity;
    private LayoutInflater inflater;

    public CustomAdapterforProDetails(Activity _activity, Context ctx, ArrayList<String> BMCList) {
        this._activity = _activity;
        this.ctx = ctx;
        this.BMCList = BMCList;
    }

    @Override
    public int getCount() {
        return BMCList != null ? BMCList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout) object);
        /* return (view ==(RelativeLayout)object);*/
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        //final AllProductDetails allProductDetails = BMCList.get(position);
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.swipe_img, container, false);
        ImageView img = (ImageView) v.findViewById(R.id.imageView);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, ImageSliderActivity.class);
                intent.putStringArrayListExtra("images", BMCList);
                ctx.startActivity(intent);

            }
        });

        //TextView tv  = (TextView)v.findViewById(R.id.textView);
        //String  IMAGE[] = allProductDetails.getImage().split(",");

        //Log.d("CustomAdapterforProDetails", BMCList.get(position).trim());
        // System.out.println("HERE: " + IMG_URL+BMCList.get(position));

        Glide.with(_activity).asBitmap().load(IMG_URL + BMCList.get(position)).into(new BitmapImageViewTarget(img));
        //img.setImageResource(BMCList.get(position));
        /* tv.setText("Image :"+position);*/

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        container.refreshDrawableState();
    }
}
