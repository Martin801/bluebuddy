package com.bluebuddy.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.bluebuddy.R;
import com.bluebuddy.Utilities.Utility;
import com.bluebuddy.adapters.CustomAdapter;
import com.bluebuddy.adapters.InterestAdapter;
import com.bluebuddy.adapters.PlaceAutocompleteAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.CommonResponse;
import com.bluebuddy.models.TripDetail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;

//import android.support.v7.app.AlertDialog;

public class EditEventActivity extends BuddyActivity implements AdapterView.OnItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {
    private static final LatLngBounds BOUNDS_NEWYORK = new LatLngBounds(new LatLng(-34.041458, 150.790100), new LatLng(40.719268, -74.006287));
    protected GoogleApiClient mGoogleApiClient;
    String[] interests = {"Fishing", "Scuba Diving", "Freediving", "SpearFishing", "Kayaking", "Photography", "Others", "Select Activity Type"};
    private Button edEvent, btnSubmit, frm_date1, minus_level1, btn_dltevnt;
    private Spinner spinner2;
    private AutoCompleteTextView cp3cnum;
    private EditText CEdf1, tellstid, certlvlid1;
    private ImageView back;
    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    private TripDetail tripDetail;
    private RecyclerView recyclerView;
    private InterestAdapter interestAdapter;
    private Activity _myActivity;
    private Context _myContext;
    private SharedPreferences pref;
    private String token, trip_id, TAG;
    private String _lat, _long;
    private String _lat1, _long1;
    private boolean isChecked = false;
    private Spinner spin;
    private String spinneritem, bellcounter;
    private LinearLayout newlvl1;
    private View certlvlid1_view;
    private MyTextView bell_counter;
    private ProgressDialog mProgressDialog;
    private boolean _isValid = false;
    private PlaceAutocompleteAdapter mAdapter;
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }

            // Get the Place object from the buffer.
            final Place place = places.get(0);
            _isValid = true;
            //_lat = place.getLatLng().latitude;
            //_long=place.getLatLng().longitude;
           /* _lat = place.getLatLng().latitude;
            _long = place.getLatLng().longitude;*/

            _lat = String.valueOf(place.getLatLng().latitude);
            _long = String.valueOf(place.getLatLng().longitude);

            //  _lat1 =  String.format("%.2f", _lat);
            // _long1 =  String.format("%.2f", _long);
            Log.d("LATLONG:", place.getLatLng().toString());
            Log.i(TAG, "Place details received: " + place.getName());
            places.release();
        }
    };
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
            */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            //Toast.makeText(getApplicationContext(), "Clicked: " + primaryText, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_edit_event);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.notice();
        super.setTitle("EDIT YOUR TRIPS");


        _myActivity = this;
        _myContext = this;

        TAG = "LocationActivity";
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, 0, this).addApi(Places.GEO_DATA_API).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");

        certlvlid1_view = (View) findViewById(R.id.certlvlid1_view);
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        certlvlid1 = (EditText) findViewById(R.id.certlvlid1);
        minus_level1 = (Button) findViewById(R.id.minus_level1);
        newlvl1 = (LinearLayout) findViewById(R.id.newlvl1);
        spin = (Spinner) findViewById(R.id.spacttype3);
        spin.setOnItemSelectedListener(this);

        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), Utility.interests);
        spin.setAdapter(customAdapter);
        spinneritem = spin.getSelectedItem().toString();

        cp3cnum = (AutoCompleteTextView) findViewById(R.id.cp3cnum);
        cp3cnum.setOnItemClickListener(mAutocompleteClickListener);
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_NEWYORK, null);
        cp3cnum.setAdapter(mAdapter);


        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        CEdf1 = (EditText) findViewById(R.id.CEdf1);
        tellstid = (EditText) findViewById(R.id.tellstid);
        edEvent = (Button) findViewById(R.id.btn_edent);
        btn_dltevnt = (Button) findViewById(R.id.btn_dltevnt);
        frm_date1 = (Button) findViewById(R.id.frm_date1);

        cp3cnum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                _isValid = false;

                _lat = "";
                _long = "";

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_dltevnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                Call<CommonResponse> userCall = service.deleteapi("Bearer " + token, "trip", trip_id);

                userCall.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                        if (response.body().getStatus() == true) {
                            Log.d("onResponse", "" + response.body().getStatus());
                            oDialog("Trip Deleted Successfully.", "Submit", "", true, _myActivity, "MyEventsActivityNewLay", 0);

                        } else {
                            Log.d("onResponse", "" + response.body().getStatus());
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {

                    }
                });
            }
        });
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar);
            }

            private void updateLabel(Calendar myCalendar) {
                String myFormat = "dd.MM.yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                Log.d("date return:", sdf.format(myCalendar.getTime()));
                CEdf1.setText(sdf.format(myCalendar.getTime()));
            }

        };

        frm_date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField();
            }

            private void setDateTimeField() {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditEventActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        edEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });
        tripDetail = (TripDetail) getIntent().getSerializableExtra("TripDetail");
        Log.d("tripDetail", tripDetail.toString());
        setEventValues(tripDetail);
        Log.d("Interest Adapter", tripDetail.getActivity());

        bellcount();
    }

    public void updateUserProfile() {
        String dateInString = CEdf1.getText().toString();
        final String EvtType = spin.getSelectedItem().toString().trim();
        if (cp3cnum.getText().toString().trim().equals("")) {
            oDialog("Enter your trip location", "Close", "", false, _myActivity, "", 0);
        } else if (CEdf1.getText().toString().trim().equals("")) {
            oDialog("Enter your trip date", "Close", "", false, _myActivity, "", 0);
        } else {
            String test = cp3cnum.getText().toString();
            Log.d(TAG, "location:" + test + ",lat:" + _lat + ",long:" + _long);

            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            //  Call<CommonResponse> tripUpdate = service.updateTrip("Bearer " + token, "put", tripDetail.getEvent_id(), cp3cnum.getText().toString().trim(), eventTypeList, tellstid.getText().toString().trim(), String.valueOf(dateInString));
            Call<CommonResponse> tripUpdate = service.updateTrip("Bearer " + token, tripDetail.getEvent_id(), cp3cnum.getText().toString().trim(), EvtType, tellstid.getText().toString().trim(), String.valueOf(dateInString), _lat, _long);

            tripUpdate.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                    oDialog("Trip details updated successfully", "Close", "", true, _myActivity, "EventsDetailsActivity22", 0, trip_id);
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout, String data) {
        super.openDialog10(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout, data);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setEventValues(TripDetail tripDetail) {
        String inputFormat = "dd MMM yyyy";
        String outputFormat = "dd.MM.yyyy";
        String inputTimeStamp = (tripDetail.getEvent_date()).replace("th", "").replace(",", "");

        try {
            String newDate = new SimpleDateFormat(outputFormat).format(new SimpleDateFormat(inputFormat).parse(inputTimeStamp));
            CEdf1.setText(newDate.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("tripDetail.date", tripDetail.getEvent_date());
        String myString = tripDetail.getActivity();
        int pos = 0;
        for (int i = 0; i < interests.length; i++)
            if (interests[i].equals(myString))
                pos = i;

        spin.setSelection(pos);
        trip_id = tripDetail.getEvent_id();
        cp3cnum.setText(tripDetail.getLocation());
        tellstid.setText(tripDetail.getDescription());

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this, "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
    }

    /* @Override
     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

     }

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

             myStringArray[0] = String.valueOf(location1.getLatitude());
             myStringArray[1] = String.valueOf(location1.getLongitude());

         } catch (IOException ex) {
             ex.printStackTrace();
         }

         return myStringArray;
     }*/
//other
/*@Override
public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    spinneritem = interests[position];
    if (spinneritem.equals("Others")) {
        if (newlvl1.getVisibility() == View.GONE) {
            newlvl1.setVisibility(View.VISIBLE);
            certlvlid1_view.setVisibility(View.VISIBLE);
        } else {
            newlvl1.setVisibility(View.GONE);
            certlvlid1_view.setVisibility(View.GONE);
        }
    } else if (!spinneritem.equals("Others")) {
        newlvl1.setVisibility(View.GONE);
        certlvlid1_view.setVisibility(View.GONE);
    }
}*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent i = new Intent(EditEventActivity.this, MyEventsActivity.class);
//        startActivity(i);
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
                if (bellcounter.equals("0")) {
                    bell_counter.setVisibility(View.GONE);
                } else if (!bellcounter.equals("0")) {
                    bell_counter.setVisibility(View.VISIBLE);
                }
                bell_counter.setText(response.body().getCounter());

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
