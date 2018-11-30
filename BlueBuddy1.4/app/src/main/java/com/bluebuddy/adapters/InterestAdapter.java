package com.bluebuddy.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluebuddy.R;
import com.bluebuddy.models.Interest;

import java.util.ArrayList;



public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.ViewHolder> {

    private ArrayList<Interest> SubjectValues;
    private Context context;
    private Activity activity;
    private View view1;
    private ViewHolder viewHolder1;
    private boolean _validation = true;
    private String[] checkInterst;
    private ArrayList<String> selectedStrings = new ArrayList<String>();
    private TextView textView;

    public InterestAdapter(Context context1, Activity a, ArrayList<Interest> i, boolean _validation) {
        this.activity = a;
        this.SubjectValues = i;
        this.context = context1;
        this._validation = _validation;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imginterest;
        public Button btnInterest;
        public LinearLayout llcp2;

        public ViewHolder(View v) {
            super(v);

            textView = (TextView) v.findViewById(R.id.txtinterest);
            imginterest = (ImageView) v.findViewById(R.id.imginterest);
            btnInterest = (Button) v.findViewById(R.id.btnInterest);
            llcp2 = (LinearLayout) v.findViewById(R.id.llcp2);
        }
    }

    public void showHideOtherField(ViewHolder holder, int position) {
        EditText editText = new EditText(context);
        editText.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        editText.setHint("Other Interest");
        editText.setTag(position);
        int paddingPixel = 25;
        float density = context.getResources().getDisplayMetrics().density;
        int paddingDp = (int)(paddingPixel * density);
        editText.setPadding(50,70,0,70);
        //editText.setPadding(20,20,20,20);
        // editText.setTextColor(Color.parseColor("#757575"));a39f9f
        editText.setTextColor(Color.parseColor("#757575"));
        editText.setHintTextColor(Color.parseColor("#cecccc"));
        editText.setBackgroundResource(R.drawable.border2);
        editText.setVisibility(View.GONE);

        holder.llcp2.addView(editText);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view1 = LayoutInflater.from(context).inflate(R.layout.interestitems, parent, false);
        viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    public ArrayList<String> getSelectedStrings() {
        return selectedStrings;
    }

    private void uncheckInterest(){
        selectedStrings.clear();

        for(int i = 0; i < SubjectValues.size(); i++){
            SubjectValues.get(i).setImgInterest(false);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textView.setText(SubjectValues.get(position).getInterestName());

        if (SubjectValues.get(position).getInterestName() == "Other") {
         //   showHideOtherField(holder, position);
        }

        if (SubjectValues.get(position).getImgInterest()) {
            holder.imginterest.setImageResource(R.drawable.checked);
        }else{
            holder.imginterest.setImageResource(R.drawable.unchecked);
        }

        holder.btnInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uncheckInterest();

                if(_validation==false) {
                    selectInterest(position, holder);
                }else if(_validation==true){
                    selectInterest1(position, holder);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return SubjectValues.size();
    }

    private void selectInterest1(int position, ViewHolder holder) {
        if(selectedStrings.size()<4 && _validation) {
            if (SubjectValues.get(position).getImgInterest()) {
                SubjectValues.get(position).setImgInterest(false);
                holder.imginterest.setImageResource(R.drawable.unchecked);

                for (int ik = 0; ik < selectedStrings.size(); ik++) {
                    if (selectedStrings.get(ik).equals(SubjectValues.get(position).getInterestName())) {
                        selectedStrings.remove(ik);
                    }
                }

                if (SubjectValues.get(position).getInterestName() == "Other") {
                   /* EditText editText = (EditText) holder.llcp2.findViewWithTag(position);
                    editText.setVisibility(View.GONE);
                    editText.setPadding(5, 0, 0, 0);*/
                }
            } else {
                SubjectValues.get(position).setImgInterest(true);
                holder.imginterest.setImageResource(R.drawable.checked);
                selectedStrings.add(SubjectValues.get(position).getInterestName());

                if (SubjectValues.get(position).getInterestName() == "Other") {
                   /* EditText editText = (EditText) holder.llcp2.findViewWithTag(position);
                    editText.setVisibility(View.VISIBLE);
                    editText.setPadding(5, 5, 5, 5);
                    editText.setTextSize(15);
                    editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                    editText.setSingleLine(true);*/
                }
            }

        }else if(selectedStrings.size()==4){
            if (SubjectValues.get(position).getImgInterest()) {
                SubjectValues.get(position).setImgInterest(false);
                holder.imginterest.setImageResource(R.drawable.unchecked);

                for (int ik = 0; ik < selectedStrings.size(); ik++) {
                    if (selectedStrings.get(ik).equals(SubjectValues.get(position).getInterestName())) {
                        selectedStrings.remove(ik);
                    }
                }

                if (SubjectValues.get(position).getInterestName() == "Other") {
                  /*  EditText editText = (EditText) holder.llcp2.findViewWithTag(position);
                    editText.setVisibility(View.GONE);
                    editText.setPadding(5, 0, 0, 0);*/
                }
            }
        }
    }

    private void selectInterest(int position, ViewHolder holder){
        if (SubjectValues.get(position).getImgInterest()) {
            SubjectValues.get(position).setImgInterest(false);
            holder.imginterest.setImageResource(R.drawable.unchecked);

            for (int ik = 0; ik < selectedStrings.size(); ik++) {
                if (selectedStrings.get(ik).equals(SubjectValues.get(position).getInterestName())) {
                    selectedStrings.remove(ik);
                }
            }

            if (SubjectValues.get(position).getInterestName() == "Other") {
              /*  EditText editText = (EditText) holder.llcp2.findViewWithTag(position);
                editText.setVisibility(View.GONE);
                editText.setPadding(5, 0, 0, 0);*/
            }
        } else {
            SubjectValues.get(position).setImgInterest(true);
            holder.imginterest.setImageResource(R.drawable.checked);
            selectedStrings.add(SubjectValues.get(position).getInterestName());

            if (SubjectValues.get(position).getInterestName() == "Other") {
               /* EditText editText = (EditText) holder.llcp2.findViewWithTag(position);
                editText.setVisibility(View.VISIBLE);
                editText.setPadding(5, 5, 5, 5);
                editText.setTextSize(15);
                editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                editText.setSingleLine(true);*/
            }
        }
    }

    public void checkUserInterst(String[] checkInterst){
        //Log.d("CHECKINTEREST", "HERE 1");
        for(int i=0; i<SubjectValues.size(); i++){
            //Log.d("CHECKINTERESTNAME", SubjectValues.get(i).getInterestName());
            for(int j=0; j<checkInterst.length; j++){
                //Log.d("SELECTEDINTERESTNAME", checkInterst[j]);
                if(SubjectValues.get(i).getInterestName().equals(checkInterst[j])){
                    //Log.d("CHECKINTEREST", "HERE 2");
                    //Log.d("CHECKINTEREST", checkInterst[j]);
                    SubjectValues.get(i).setImgInterest(true);
                    selectedStrings.add(SubjectValues.get(i).getInterestName().trim());
                }
            }
        }

        notifyDataSetChanged();
    }
}