package com.bluebuddy.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bluebuddy.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jsibbold.zoomage.ZoomageView;

import java.util.List;

import static com.bluebuddy.app.AppConfig.IMG_URL;

public class ImageSliderAdapter extends PagerAdapter {

    private Context ctx;
    private List<String> BMCList;
    private LayoutInflater inflater;

    public ImageSliderAdapter(Context ctx, List<String> BMCList) {
        this.ctx = ctx;
        this.BMCList = BMCList;
    }

    @Override
    public int getCount() {
        return BMCList != null ? BMCList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.slider_image_view, container, false);
        ZoomageView img = v.findViewById(R.id.imageView);
        Glide.with(ctx).asBitmap().load(IMG_URL + BMCList.get(position)).into(new BitmapImageViewTarget(img));
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        container.refreshDrawableState();
    }
}
