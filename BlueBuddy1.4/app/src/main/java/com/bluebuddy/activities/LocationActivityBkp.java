package com.bluebuddy.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluebuddy.R;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.ServerResponseLoc;
import com.bluebuddy.interfaces.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_STEP;
import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.API_KEY;
import static com.bluebuddy.app.AppConfig.LOG_TAG;
import static com.bluebuddy.app.AppConfig.OUT_JSON;
import static com.bluebuddy.app.AppConfig.PLACES_API_BASE;
import static com.bluebuddy.app.AppConfig.TYPE_AUTOCOMPLETE;

//MAP

public class LocationActivityBkp extends BuddyActivity implements OnItemClickListener {
    private EditText cp6weblnk;
    private AutoCompleteTextView autoCompView;
    private Button cp6nxt, cp6skp;
    private Activity _myActivity;
    private ImageView back;
    private SharedPreferences pref;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_location);
        super.onCreate(savedInstanceState);
        this.initializeElements();

        _myActivity = this;

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
    }

    private void initializeElements() {
        //map
        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item_for_map_address));
        autoCompView.setOnItemClickListener(this);
        //map
        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMyPref(ACCESS_STEP, "6"); // CreateProfile6
                Intent i = new Intent(LocationActivityBkp.this, CreateProfileActivity6.class);
                startActivity(i);
            }
        });


        //comment today below line2 cp6weblink
        cp6nxt = (Button) findViewById(R.id.weblnknxtid);
        TextView tx = (TextView) findViewById(R.id.idtxt1);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");
        tx.setTypeface(custom_font);

        cp6nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cp6nxt();
            }
        });
    }

    public void cp6nxt() {
        String location = autoCompView.getText().toString().trim();

        if (location.isEmpty()) {
            oDialog("Enter your Location", "Close", "", false, _myActivity, "", 0);
        } else {
            location = autoCompView.getText().toString();
            //String[] add = getLocationFromAddress(_mContext, location);

            String[] add = null;

            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<ServerResponseLoc> userCall = service.Loc("Bearer " + token, "put", location, add[0], add[1], 8);
            userCall.enqueue(new Callback<ServerResponseLoc>() {
                @Override
                public void onResponse(Call<ServerResponseLoc> call, Response<ServerResponseLoc> response) {

                    Log.d("onResponse", "" + response.body().getMessage());

                    if (response.body().getStatus() == "true") {
                        setMyPref(ACCESS_STEP, "8"); // CreateProfile8
                        Log.d("onResponse", "" + response.body().getStatus());


                        Intent intent = new Intent(LocationActivityBkp.this, CreateProfileActivity7.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.d("onResponse", "" + response.body().getStatus());
                    }
                }

                @Override
                public void onFailure(Call<ServerResponseLoc> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                }
            });

        }
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    //MAP
    //1
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
    }

    //2
    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:USA");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    //3
    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }

    }

    protected void setMyPref(String key, String value) {
        super.setPref(key, value);
    }

    public String[] getLocationFromAddress(Context context, String location) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        Address location1;
        String[] l;
        String[] myStringArray = new String[2];

        try {
            // May throw an IOException
            address = coder.getFromLocationName(location, 5);
            if (address == null) {
                // return null;
            }

            location1 = address.get(0);
            location1.getLatitude();
            location1.getLongitude();

            double Lat = location1.getLatitude();
            double Long = location1.getLongitude();

            Log.d("lat:", String.valueOf(location1.getLatitude()));
            Log.d("long:", String.valueOf(location1.getLongitude()));

            //p1 = new LatLng(location1.getLatitude(), location1.getLongitude() );

            myStringArray[0] = String.valueOf(location1.getLatitude());
            myStringArray[1] = String.valueOf(location1.getLongitude());

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return myStringArray;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setMyPref(ACCESS_STEP, "6"); // CreateProfile6
        Intent i = new Intent(LocationActivityBkp.this, CreateProfileActivity6.class);
        startActivity(i);
    }
}