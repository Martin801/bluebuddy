package com.bluebuddy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.bluebuddy.R;
import com.bluebuddy.adapters.ContactListAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.Glossary;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

public class MyContactsActivity extends BuddyActivity implements SearchView.OnQueryTextListener {
    protected static final String TAG = "ChatActivity";
    private LinearLayout mIndexerLayout;
    private ListView mListView;
    private FrameLayout mTitleLayout;
    private MyTextView mTitleText;
    private RelativeLayout mSectionToastLayout;
    private MyTextView mSectionToastText, bell_counter;
    private ArrayList<Glossary> glossaries = new ArrayList<Glossary>();
    private String alphabet = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private AlphabetIndexer mIndexer;
    private ContactListAdapter mAdapter;
    private int lastSelectedPosition = -1;
    private Button cntlstinvt;
    private ImageView back;
    private String bellcounter, token;
    private Activity _myActivity;
    private SearchView mSearchView;
    private ProgressDialog mProgressDialog;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_my_contacts);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.notice();
        super.loader();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog = super.mProgressDialog;
        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        super.setTitle("My Contacts");
        initView();
        _myActivity = this;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyContactsActivity.this, MyAccountMenu.class);
                startActivity(i);
            }
        });
        bellcount();
    }


    @SuppressWarnings("deprecation")
    private void initView() {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();

        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        back = (ImageView) findViewById(R.id.imgNotification2);
        mIndexerLayout = (LinearLayout) findViewById(R.id.indexer_layout);
        mListView = (ListView) findViewById(R.id.contacts_list);
        mTitleLayout = (FrameLayout) findViewById(R.id.title_layout);
        mTitleText = (MyTextView) findViewById(R.id.title_text);

        mSectionToastLayout = (RelativeLayout) findViewById(R.id.section_toast_layout);
        mSectionToastText = (MyTextView) findViewById(R.id.section_toast_text);
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
        Cursor cursor = getContentResolver().query(uri,
                new String[]{"display_name", "sort_key"}, null, null, "sort_key");
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                String sortKey = getSortKey(cursor.getString(1));
                Log.e("sortKey from cursor", "" + sortKey);
                Glossary glossary = new Glossary();
                glossary.setName(name);
                glossary.setSortKey(sortKey);
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
            } else {
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount) {

            int sectionIndex = mIndexer.getSectionForPosition(firstVisibleItem);
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        setupSearchView(searchItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(MyContactsActivity.this, MyAccountMenu.class);
        startActivity(i);
    }

    private void bellcount() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllNotice> userCall = service.allnotice1("Bearer " + token);

        userCall.enqueue(new Callback<AllNotice>() {
            @Override
            public void onResponse(Call<AllNotice> call, Response<AllNotice> response) {

                Log.d("STATUS", String.valueOf(response.body().getStatus()));
                Log.d("onResponse:", "" + response.body().getNotification_details());
                bellcounter = response.body().getCounter();
                if (bellcounter.equals("0")) {
                    bell_counter.setVisibility(View.GONE);
                } else if (!bellcounter.equals("0")) {
                    bell_counter.setVisibility(View.VISIBLE);
                }
                bell_counter.setText(response.body().getCounter());

            }

            @Override
            public void onFailure(Call<AllNotice> call, Throwable t) {

            }
        });
    }
}
