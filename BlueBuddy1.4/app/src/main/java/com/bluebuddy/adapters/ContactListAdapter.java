package com.bluebuddy.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluebuddy.R;
import com.bluebuddy.models.Glossary;


public class ContactListAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private ArrayList<Glossary> glossariesList;
    private boolean checkable = true;
    /**
     * This is mainly for listview search
     **/
    private ArrayList<Glossary> glossariesListForSearch;
    private LayoutInflater mInflater;
    private AlphabetIndexer mIndexer;

    public ContactListAdapter(Context context, ArrayList<Glossary> glossaries) {
        super();
        this.mContext = context;
        this.glossariesList = glossaries;
        glossariesListForSearch = glossaries;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return glossariesList.size();
    }

    @Override
    public Object getItem(int position) {
        return glossariesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.contact_item, null);
            holder = new ViewHolder();

            holder.check=(ImageButton)convertView.findViewById(R.id.check);
            holder.checkconatct=(LinearLayout)convertView.findViewById(R.id.checkconatct);
            holder.sortKeyLayout = (LinearLayout) convertView.findViewById(R.id.sort_key_layout);
            holder.sortKey = (TextView) convertView.findViewById(R.id.sort_key);
            holder.contactName = (TextView) convertView.findViewById(R.id.contact_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Glossary glossary = glossariesList.get(position);
        holder.contactName.setText(glossary.getName());
        int section = mIndexer.getSectionForPosition(position);
        final ViewHolder finalHolder = holder;

        holder.checkconatct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!glossariesList.get(position).getCheck()) {
                    glossariesList.get(position).setCheck(true);
                } else {
                    glossariesList.get(position).setCheck(false);
                }

                notifyDataSetChanged();
            }
        });

        // This piece of code is for select / unselect all contacts
        if(glossariesList.get(position).getCheck()){
            finalHolder.check.setImageResource(R.drawable.checked3);
        }else{
            finalHolder.check.setImageResource(R.drawable.unchecked2);
        }

        if (position == mIndexer.getPositionForSection(section)) {
            holder.sortKey.setText(glossary.getSortKey());
            holder.sortKeyLayout.setVisibility(View.VISIBLE);
        } else {
            holder.sortKeyLayout.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void selectContactList(boolean c){
        for(int i = 0; i < glossariesList.size(); i++){
            Glossary glossary = glossariesList.get(i);
            glossary.setCheck(c);
        }

        this.notifyDataSetChanged();
    }

    /**
     * @param indexer
     */
    public void setIndexer(AlphabetIndexer indexer) {
        mIndexer = indexer;
    }

    private static class ViewHolder {
        LinearLayout sortKeyLayout,checkconatct;
        TextView sortKey;
        TextView contactName;
        ImageButton check;
    }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        return myFilter;
    }

    Filter myFilter = new Filter() {

        @SuppressWarnings("unchecked")
        @Override
        public void publishResults(CharSequence constraint, FilterResults results) {
            glossariesList = (ArrayList<Glossary>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

        @Override
        public FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            ArrayList<Glossary> tempGlossaryList = new ArrayList<>();

            if (constraint != null && glossariesListForSearch != null) {
                int length = glossariesListForSearch.size();
                Log.i("Filtering", "glossaries size" + length);
                int i = 0;
                while (i < length) {
                    Glossary item = glossariesListForSearch.get(i);
                    // Real filtering:
                    if (item.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempGlossaryList.add(item);
                    }
                    i++;
                }

                filterResults.values = tempGlossaryList;
                filterResults.count = tempGlossaryList.size();
                Log.i("Filtering", "Filter result count size" + filterResults.count);
            }

            return filterResults;
        }
    };

    public void updateList(ArrayList<Glossary> filteredList){
        glossariesList = filteredList;
        this.notifyDataSetChanged();
    }

}