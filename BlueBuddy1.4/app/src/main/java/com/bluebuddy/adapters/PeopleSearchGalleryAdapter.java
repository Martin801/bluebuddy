package com.bluebuddy.adapters;

/**
 * Created by admin on 5/29/2017.
 */

public class PeopleSearchGalleryAdapter {
/*

    private List<PeopleSearchGalleryItems> EVENTList2;
    private Activity _activity;
    private Context _context;

    public PeopleSearchGalleryAdapter(){}


    public PeopleSearchGalleryAdapter(Activity a, Context c, List<PeopleSearchGalleryItems> EVENTList2){
        this._activity=a;
        this._context=c;
        this.EVENTList2 = EVENTList2;


    }

    public class ViewHolder extends PeopleSearchGalleryAdapter.ViewHolder {

        public TextView Srchpname;
        public ImageView Srchppic;
        */
/* public Button evtDt;*//*

        public ViewHolder(View v) {
            super(v);
            Srchpname = (TextView)v.findViewById(R.id.pplname);
            Srchppic = (ImageView) v.findViewById(R.id.pplimg);
            */
/*evtDt = (Button) v.findViewById(R.id.evtdetail);*//*


        }

    }

    @Override
    public PeopleSearchGalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Creating a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.peoplesearchgallerycard,parent,false);

        //set the view's size, margins, paddings and layout parameters

        PeopleSearchGalleryAdapter.ViewHolder vh = new PeopleSearchGalleryAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PeopleSearchGalleryAdapter.ViewHolder holder, int position) {

        PeopleSearchGalleryItems peopleSearchGalleryItems = PeopleSearchGalleryItems.get(position);
        holder.Srchpname.setText(String.valueOf(peopleSearchGalleryItems.getSrchpname()));
        holder.Srchppic.setImageResource(peopleSearchGalleryItems.getSrchppic());

        */
/*holder.evtDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_activity, EventsDetailsActivity.class);
                _activity.startActivity(intent);
            }
        });*//*

    }

    @Override
    public int getItemCount() {
        return EVENTList2.size();
    }

*/

}
