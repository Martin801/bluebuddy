package com.bluebuddy.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AlphabetIndexer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.bluebuddy.R;
import com.bluebuddy.Utilities.Utility;
import com.bluebuddy.adapters.ContactListAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.CommonResponse;
import com.bluebuddy.models.EventDetails;
import com.bluebuddy.models.Glossary;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class InviteBuddyContactListActivity extends BuddyActivity implements SearchView.OnQueryTextListener {
    private static final int RECORD_REQUEST_CODE = 101;
    protected static final String TAG = "ChatActivity";
    private ImageView back;
    private ImageButton selcetAll1;
    private FrameLayout selcetAll;
    private LinearLayout mIndexerLayout;
    private ListView mListView;
    private FrameLayout mTitleLayout;
    private TextView mTitleText;
    private MyTextView bell_counter;

    private RelativeLayout mSectionToastLayout;
    private TextView mSectionToastText;
    private ArrayList<Glossary> glossaries = new ArrayList<Glossary>();
    private String alphabet = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private AlphabetIndexer mIndexer;
    private ContactListAdapter mAdapter;
    private int lastSelectedPosition = -1;
    private Button cntlstinvt;
    private boolean all_contacts = true;
    private Activity _myActivity;
    private EventDetails eventDetails;
    private EditText srch;
    private SearchView mSearchView;
    private String token, trip_id, bellcounter;
    private SharedPreferences pref;
    private boolean normal_backpress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_invite_buddy_contact_list);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.notice();
        super.setTitle("INVITE BUDDIES");


        //super.loader();
        //mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;
        // initView();
        _myActivity = this;

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");

        initView();
        Bundle b = getIntent().getExtras();
        eventDetails = (EventDetails) b.getSerializable("AllEventsDetails");

        normal_backpress = b.containsKey("normal_backpressed") ? true : false;
        trip_id = eventDetails.getEvent_id();

        srch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // filter your list from your input
                filter(s.toString().toLowerCase());
            }
        });
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CONTACTS},
                RECORD_REQUEST_CODE);
    }

    private void showDialog() {
        super.loader();
    }

    private void filter(String text) {
        ArrayList<Glossary> temp = new ArrayList<Glossary>();

        for (Glossary g : glossaries) {
            String name = String.valueOf(g.getName()).toLowerCase();

            if (name.contains(text)) {
                temp.add(g);
            }
        }

        //update recyclerview
        mAdapter.updateList(temp);
    }

    @SuppressWarnings("deprecation")
    private void initView() {

        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!normal_backpress) {
                 //   Intent i = new Intent(InviteBuddyContactListActivity.this, EventsDetailsActivity2.class);

                    Intent i = new Intent(InviteBuddyContactListActivity.this, InviteBuddiesActivity.class);
                 //   i.putExtra("AllEventsDetails", eventDetails);
                    startActivity(i);
                } else {
                    onBackPressed();
                }
            }
        });

       bell_counter = (MyTextView) findViewById(R.id.bell_counter);

        selcetAll1 = (ImageButton) findViewById(R.id.selcetAll1);
        selcetAll = (FrameLayout) findViewById(R.id.selcetAll);
        srch = (EditText) findViewById(R.id.srch);
        mIndexerLayout = (LinearLayout) findViewById(R.id.indexer_layout);
        mListView = (ListView) findViewById(R.id.contacts_list);
        mTitleLayout = (FrameLayout) findViewById(R.id.title_layout);
        mTitleText = (TextView) findViewById(R.id.title_text);
        cntlstinvt = (Button) findViewById(R.id.contlstinvt);

        cntlstinvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String rc2 = "AllEventsActivity";
                //oDialog("Thank you for inviting your buddies to join the event. ", "Go to Trips", "", true, _myActivity, rc2, 0);
                ArrayList<String> inviteContactList = new ArrayList<String>();
                String contactStringWhichisClicked="";

                if (glossaries.size() > 0) {
                    for (int i = 0, f = glossaries.size(); i < f; i++) {
                        if (glossaries.get(i).getCheck()) {
                            String _phone = glossaries.get(i).getPhone();


                            if (_phone.length()> Utility.MINIUMPHON_NUMBERS) {
                                if(contactStringWhichisClicked.matches("")){
                                    contactStringWhichisClicked=glossaries.get(i).getPhone().replaceAll("\\s+", "");
                                }else {
                                    contactStringWhichisClicked +=","+glossaries.get(i).getPhone().replaceAll("\\s+", "");
                                }

                            }
                                /*inviteContactList.add(glossaries.get(i).getPhone());
                                if (contactStringWhichisClicked.matches("")) {
                                    if(contactStringWhichisClicked.matches("")){
                                        contactStringWhichisClicked=glossaries.get(i).getPhone();
                                    }else {
                                        contactStringWhichisClicked +=","+contactStringWhichisClicked;
                                    }
                                    contactStringWhichisClicked = glossaries.get(i).getPhone();
                                }*/
                           /* }else {
                                inviteContactList.add("" + glossaries.get(i).getPhone());

                            }*/
                        }
                    }
                }

               // String[] inviteeList = inviteContactList.toArray(new String[inviteContactList.size()]);
               // inviteBuddy(trip_id, inviteContactList, new JSONArray(inviteContactList), inviteeList);
                Log.d("nipaerror",contactStringWhichisClicked);
                inviteBuddy(trip_id, contactStringWhichisClicked);

            }
        });

        mSectionToastLayout = (RelativeLayout) findViewById(R.id.section_toast_layout);
        mSectionToastText = (TextView) findViewById(R.id.section_toast_text);

        for (int i = 0; i < alphabet.length(); i++) {
            TextView letterTextView = new TextView(this);
            letterTextView.setText(alphabet.charAt(i) + "");
            letterTextView.setTextSize(11.8f);
            letterTextView.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(28, 0, 1.0f);
            letterTextView.setLayoutParams(params);
            letterTextView.setPadding(4, 0, 2, 0);
            mIndexerLayout.addView(letterTextView);
            mIndexerLayout.setBackgroundResource(R.color.transparent);
        }

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = null;

        try {
            cursor = getContentResolver().query(uri, new String[]{"display_name", "sort_key", ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, "sort_key");
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                String sortKey = getSortKey(cursor.getString(1));
                String phone = cursor.getString(2);
                //Log.d("CURSOR", "sortKey: " + sortKey + ", name: " + name + ", phone: " + phone);

                Glossary glossary = new Glossary();
                glossary.setName(name);
                glossary.setSortKey(sortKey);
                glossary.setPhone(phone);
                glossaries.add(glossary);
            } while (cursor.moveToNext());
        }

        mAdapter = new ContactListAdapter(this, glossaries);
        startManagingCursor(cursor);
        mIndexer = new AlphabetIndexer(cursor, 1, alphabet);
        mAdapter.setIndexer(mIndexer);

        if (glossaries != null && glossaries.size() > 0) {
            mListView.setAdapter(mAdapter);
            mListView.setOnScrollListener(mOnScrollListener);
            mIndexerLayout.setOnTouchListener(mOnTouchListener);
        }

    }

    //private void inviteBuddy(String trip_id, ArrayList<String> contactList, JSONArray jsonContactList, String[] inviteeList) {
        private void inviteBuddy(String trip_id,  String inviteeList) {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<CommonResponse> userCall = service.inviteBuddy("Bearer " + token, trip_id, inviteeList);

        userCall.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                Log.d("STATUS", String.valueOf(response.body().getStatus()));

                if (response.body().getStatus()) {
                    oDialog("Invitation sent successfully", "Ok", "", true, _myActivity, "MyProfileActivity", 0);
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private String getSortKey(String sortKeyString) {
        String key = sortKeyString.substring(0, 1).toUpperCase();
        if (key.matches("[A-Z]")) {
            return key;
        }
        return "#";
    }

    private AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {

        private int lastFirstVisibleItem = -1;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == SCROLL_STATE_IDLE) {
                //mIndexerLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                //mIndexerLayout.setBackgroundResource(R.drawable.letterslist_bg);
            } else {
                //mIndexerLayout.setBackgroundResource(R.drawable.letterslist_bg);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            // firstVisibleItem corresponding to the index of AlphabetIndexer(eg, B-->Alphabet index is 2)
            int sectionIndex = mIndexer.getSectionForPosition(firstVisibleItem);
            //next section Index corresponding to the positon in the listview
            int nextSectionPosition = mIndexer.getPositionForSection(sectionIndex + 1);
            Log.d(TAG, "onScroll()-->firstVisibleItem=" + firstVisibleItem + ", sectionIndex=" + sectionIndex + ", nextSectionPosition=" + nextSectionPosition);
            if (firstVisibleItem != lastFirstVisibleItem) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mTitleLayout.getLayoutParams();
                params.topMargin = 0;
                mTitleLayout.setLayoutParams(params);
                mTitleText.setText(String.valueOf(alphabet.charAt(sectionIndex)));
                ((TextView) mIndexerLayout.getChildAt(sectionIndex)).setBackgroundColor(getResources().getColor(R.color.transparent));
                lastFirstVisibleItem = firstVisibleItem;
            }

            // update AlphabetIndexer background
            if (sectionIndex != lastSelectedPosition) {
                if (lastSelectedPosition != -1) {
                    ((TextView) mIndexerLayout.getChildAt(lastSelectedPosition)).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                lastSelectedPosition = sectionIndex;
            }

            if (nextSectionPosition == firstVisibleItem + 1) {
                View childView = view.getChildAt(0);
                if (childView != null) {
                    int sortKeyHeight = mTitleLayout.getHeight();
                    int bottom = childView.getBottom();
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mTitleLayout.getLayoutParams();
                    if (params.topMargin != 0) {
                        params.topMargin = 0;
                        mTitleLayout.setLayoutParams(params);
                    }
                }
            }

        }

    };

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float alphabetHeight = mIndexerLayout.getHeight();
            float y = event.getY();
            int sectionPosition = (int) ((y / alphabetHeight) / (1f / 27f));

            if (sectionPosition < 0) {
                sectionPosition = 0;
            } else if (sectionPosition > 26) {
                sectionPosition = 26;
            }

            if (lastSelectedPosition != sectionPosition) {
                if (-1 != lastSelectedPosition) {
                    ((TextView) mIndexerLayout.getChildAt(lastSelectedPosition)).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                lastSelectedPosition = sectionPosition;
            }

            String sectionLetter = String.valueOf(alphabet.charAt(sectionPosition));
            int position = mIndexer.getPositionForSection(sectionPosition);
            TextView textView = (TextView) mIndexerLayout.getChildAt(sectionPosition);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mIndexerLayout.setBackgroundResource(R.color.transparent);
                    textView.setBackgroundColor(getResources().getColor(R.color.letter_bg_color));
                    mSectionToastLayout.setVisibility(View.VISIBLE);
                    mSectionToastText.setText(sectionLetter);
                    mListView.smoothScrollToPositionFromTop(position, 0, 1);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mIndexerLayout.setBackgroundResource(R.color.transparent);

                    textView.setBackgroundColor(getResources().getColor(R.color.letter_bg_color));
                    mSectionToastLayout.setVisibility(View.VISIBLE);
                    mSectionToastText.setText(sectionLetter);
                    mListView.smoothScrollToPositionFromTop(position, 0, 1);
                    break;
                case MotionEvent.ACTION_UP:
                    mSectionToastLayout.setVisibility(View.GONE);
                default:
                    mSectionToastLayout.setVisibility(View.GONE);
                    break;
            }

            return true;
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        setupSearchView(searchItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupSearchView(MenuItem searchItem) {
        mSearchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.i("Action Search Query", query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.i("Action Search Query", newText);
        mAdapter.getFilter().filter(newText);
        Log.i("mAdapter", "" + newText);
        return false;
    }

    public boolean onClose() {
        Log.i("Action Search Query", "^%^%^%^%^");
        return false;
    }

    protected boolean isAlwaysExpanded() {
        return false;
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    public void selcetAll(View view) {
        if (!all_contacts) {
            all_contacts = true;
            selcetAll1.setImageResource(R.drawable.checked3);

            mAdapter.selectContactList(true);
        } else {
            all_contacts = false;
            selcetAll1.setImageResource(R.drawable.unchecked2);
            mAdapter.selectContactList(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!normal_backpress) {
             Intent i = new Intent(InviteBuddyContactListActivity.this, InviteBuddiesActivity.class);
               startActivity(i);
        }
    }

    private void bellcount() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllNotice> userCall = service.allnotice1("Bearer " + token);

        userCall.enqueue(new Callback<AllNotice>() {
            @Override
            public void onResponse(Call<AllNotice> call, Response<AllNotice> response) {
                //    if (mProgressDialog.isShowing())
                //      mProgressDialog.dismiss();
                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("onResponse:", "" + response.body().getNotification_details());
                bellcounter = response.body().getCounter();
                if(bellcounter.equals("0")){
                    bell_counter.setVisibility(View.GONE);
                }
                else if(!bellcounter.equals("0")){
                    bell_counter.setVisibility(View.VISIBLE);
                }
                bell_counter.setText(bellcounter);

                //  Log.d("TOKEN:", "" + token);
                //     ArrayList<AllNotice> allNotices = response.body().getNotification_details();
                //    allNoticeAdapter.updateAllNotice(response.body().getNotification_details());
            }

            @Override
            public void onFailure(Call<AllNotice> call, Throwable t) {

            }
        });
    }
}
