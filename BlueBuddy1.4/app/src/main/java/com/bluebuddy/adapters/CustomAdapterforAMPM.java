package com.bluebuddy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bluebuddy.R;



public class CustomAdapterforAMPM extends BaseAdapter {
    Context context;
    String[] activityname;
    LayoutInflater inflter;

    public CustomAdapterforAMPM(Context applicationContext, String[] activityname) {
        this.context = applicationContext;
        this.activityname = activityname;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return activityname.length;
    }

    @Override
    public Object getItem(int i) {
        return activityname[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_rows2, null);
        TextView activity_type = (TextView) view.findViewById(R.id.textView);
        activity_type.setText(activityname[i]);
        return view;
    }
}