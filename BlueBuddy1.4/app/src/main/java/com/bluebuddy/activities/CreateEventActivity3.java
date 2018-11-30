package com.bluebuddy.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.bluebuddy.adapters.PlaceAutocompleteAdapter;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.classes.ServerResponseCE;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.models.AllNotice;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_TOKEN;
import static com.bluebuddy.app.AppConfig.activityPath;

public class CreateEventActivity3 extends BuddyActivity implements GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemSelectedListener {


    private static final String TAG = "CreateEventActivity3";
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(new LatLng(-34.041458, 150.790100), new LatLng(40.719268, -74.006287));
    /**
     * GoogleApiClient wraps our service connection to Google Play Services and provides access
     * to the user's sign in state as well as the Google's APIs.
     */
    protected GoogleApiClient mGoogleApiClient;
    EditText certlvlid1;
    String EvtType, othtxt, dcdlst;
    String interestList = "";
    private Spinner spin;
    private ImageView ivdf1, ivt1;
    private ImageView back;
    private Activity _myActivity;
    private Context context;
    private Calendar myCalendar = Calendar.getInstance();
    private LinearLayout newlvl1;
    private String spinneritem, _lat, _long = "";
    private EditText cedf1, tellstid, cet1;
    private MyTextView bell_counter;
    private Button createEvent, frm_date1, to_date1, minus_level1;
    private View certlvlid1_view;
    private SharedPreferences pref;
    private String token, Othertxt;
    private AutoCompleteTextView cp3cnum;
    private boolean _isValid = false;
    private String _lat1, _long1, bellcounter;
    private PlaceAutocompleteAdapter mAdapter;
    // nipa add
    private ProgressDialog progressDialog;
    //String[] interests = {"Fishing", "Scuba Diving", "Freediving", "SpearFishing", "Kayaking", "Photography", "Others", "Select Activity Type"};
    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                // Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }

            // Get the Place object from the buffer.
            final Place place = places.get(0);
            _isValid = true;
            _lat = String.valueOf(place.getLatLng().latitude);
            _long = String.valueOf(place.getLatLng().longitude);

            //  Log.d("LATLONG:", place.getLatLng().toString());
            //  Log.i(TAG, "Place details received: " + place.getName());
            places.release();
        }
    };
    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
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
        super.setLayout(R.layout.activity_create_event2);
        super.onCreate(savedInstanceState);

        // Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using AutoManage
        // functionality, which automatically sets up the API client to handle Activity lifecycle
        // events. If your activity does not extend FragmentActivity, make sure to call connect()
        // and disconnect() explicitly.
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, 0 /* clientId */, this).addApi(Places.GEO_DATA_API).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.initialize();
        super.notice();
        super.setTitle("CREATE YOUR TRIP");

        context = getApplicationContext();

        pref = super.getPref();
        token = pref.getString(ACCESS_TOKEN, "");
        bellcount();

        frm_date1 = (Button) findViewById(R.id.frm_date1);
        certlvlid1 = (EditText) findViewById(R.id.certlvlid1);
        cedf1 = (EditText) findViewById(R.id.CEdf1);

        ivdf1 = (ImageView) findViewById(R.id.IVdf1);

        certlvlid1_view = (View) findViewById(R.id.certlvlid1_view);
        bell_counter = (MyTextView) findViewById(R.id.bell_counter);
        //  minus_level1 = (Button) findViewById(R.id.minus_level1);
        newlvl1 = (LinearLayout) findViewById(R.id.newlvl1);
        spin = (Spinner) findViewById(R.id.spacttype3);
        spin.setOnItemSelectedListener(this);
        //Log.d("nipaerror","hi");
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), Utility.interests);
        spin.setAdapter(customAdapter);
        // spin.setSelection(Utility.interests.length - 1);
        //   spinneritem = spin.getSelectedItem().toString();
      /*  minus_level1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spin.setSelection(Utility.interests.length - 1);
                newlvl1.setVisibility(View.GONE);
                certlvlid1_view.setVisibility(View.GONE);
                certlvlid1.setText("");
            }
        });*/
        final Bundle bundle = getIntent().getExtras();
        final int pass = bundle.getInt("pass");
        interestList = bundle.getString("interestList");
        if (interestList == null) {
            interestList = "";
        }
        back = (ImageView) findViewById(R.id.imgNotification2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pass == 1 && bundle.containsKey("interestList")) {
                    Intent i = new Intent(CreateEventActivity3.this, AllEventsActivityNew.class);
                    i.putExtra("interestList", bundle.getString("interestList"));
                    startActivity(i);
                } else /*if (pass == 2)*/ {
                    Intent i = new Intent(CreateEventActivity3.this, MyProfileActivity.class);
                    startActivity(i);
                }

            }

        });
        //map
        cp3cnum = (AutoCompleteTextView) findViewById(R.id.cp3cnum);
        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_GREATER_SYDNEY, null);
        cp3cnum.setAdapter(mAdapter);
        cp3cnum.setOnItemClickListener(mAutocompleteClickListener);

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

        _myActivity = this;
        tellstid = (EditText) findViewById(R.id.tellstid);
        createEvent = (Button) findViewById(R.id.btn_cevent);

        //from
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
                String myFormat = "dd.MM.yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                cedf1.setText(sdf.format(myCalendar.getTime()));
            }

        };

        frm_date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField();
            }

            private void setDateTimeField() {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEventActivity3.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cp3cnum.getText().toString().trim().isEmpty()) {
                    oDialog("Enter your trip location", "Close", "", false, _myActivity, "", 0);
                } else if (cedf1.getText().toString().trim().isEmpty()) {
                    oDialog("Enter your event date", "Close", "", false, _myActivity, "", 0);
                } else if (String.valueOf(spin.getSelectedItem()) == "Select Activity Type") {
                    oDialog("Please select the activity type", "Close", "", false, _myActivity, "", 0);
                }
                //today
                else if (String.valueOf(spin.getSelectedItem()) == "Others" && certlvlid1.getText().toString().trim().isEmpty()) {
                    oDialog("Please enter an activity type", "Close", "", false, _myActivity, "", 0);
                }
                //today
                else if (tellstid.getText().toString().trim().isEmpty()) {
                    oDialog("Enter trip description", "Close", "", false, _myActivity, "", 0);
                } else {
                    if (_isValid) {
                        openDialogTrip("Thanks for creating your Trips.You can now Invite your buddies to join the trip. ", "Invite Buddy", "Skip", true, true, _myActivity, 0);
                    } else {
                        openDialogTrip("Select trip location!", "Close", "", false, false, _myActivity, 0);
                    }
                }
            }
        });

    }

    private void createTrip(final String redirectClass) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(_myActivity);
            progressDialog.setTitle(getString(R.string.loading_title));
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage(getString(R.string.loading_message));
        }
        progressDialog.show();
        String Loc = cp3cnum.getText().toString().trim();
        String EvtDate = cedf1.getText().toString().trim();
        String EvtDesc = tellstid.getText().toString().trim();

        Loc = cp3cnum.getText().toString();
        double _lat3 = Double.parseDouble(_lat);
        double _long3 = Double.parseDouble(_long);
        _lat1 = String.format("%.6f", _lat3);
        _long1 = String.format("%.6f", _long3);

        if (String.valueOf(spin.getSelectedItem()) == "Others") {
            //TODAY
            othtxt = certlvlid1.getText().toString().trim();
            // Log.d("Othertxt1", othtxt);
            Othertxt = "Others:" + othtxt;
            //  Log.d("Othertxt2", Othertxt);
            //TODAY
            EvtType = Othertxt;
            //Log.d("evttype", EvtType);
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<ServerResponseCE> userCall = service.Ce("Bearer " + token, _lat1, _long1, Loc, EvtDate, EvtType, EvtDesc);
            userCall.enqueue(new Callback<ServerResponseCE>() {
                @Override
                public void onResponse(Call<ServerResponseCE> call, Response<ServerResponseCE> response) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (response.body() != null && response.body().getStatus() == "true") {
                        try {
                            Intent intent = new Intent(CreateEventActivity3.this, Class.forName(activityPath + "." + redirectClass));
                            intent.putExtra("interestList", EvtType);
                            intent.putExtra("AllEventsDetails", response.body().getTrip_details());
                            startActivity(intent);
                            finish();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        String msg = (String) getApplication().getText(R.string.goeswrong);
                        String sMesage = "";
                        if (response.body().getMessage() != null) {
                            sMesage = response.body().getMessage();
                        }
                        oDialog(msg + " " + sMesage, "Ok", "", false, _myActivity, "", 0);
                    }
                }

                @Override
                public void onFailure(Call<ServerResponseCE> call, Throwable t) {
                    //  Log.d("onFailure", t.toString());
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }
            });
        } else {
            EvtType = spin.getSelectedItem().toString().trim();
            //Log.d("evttype", EvtType);
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<ServerResponseCE> userCall = service.Ce("Bearer " + token, _lat1, _long1, Loc, EvtDate, EvtType, EvtDesc);
            userCall.enqueue(new Callback<ServerResponseCE>() {
                @Override
                public void onResponse(Call<ServerResponseCE> call, Response<ServerResponseCE> response) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (response.body() != null && response.body().getStatus() == "true") {
                        try {
                            Intent intent = new Intent(CreateEventActivity3.this, Class.forName(activityPath + "." + redirectClass));
                            intent.putExtra("interestList", EvtType);
                            intent.putExtra("AllEventsDetails", response.body().getTrip_details());
                            startActivity(intent);
                            finish();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        String msg = (String) getApplication().getText(R.string.goeswrong);
                        String sMesage = "";
                        if (response.body().getMessage() != null) {
                            sMesage = response.body().getMessage();
                        }
                        oDialog(msg + " " + sMesage, "Ok", "", false, _myActivity, "", 0);
                    }
                }

                @Override
                public void onFailure(Call<ServerResponseCE> call, Throwable t) {
                    //Log.d("onFailure", t.toString());
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }
            });
        }

    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final boolean _redirect1, final Activity activity, final String _redirectClass, final String _redirectClass1, final int layout) {
        super.openDialog7(msg, btnText, btnText2, _redirect, _redirect1, activity, _redirectClass, _redirectClass1, layout);
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

    public void openDialogTrip(String msg, String btnTxt, String btnTxt2, final boolean _redirect, final boolean _redirect1, final Activity activity, int dialogLayout) {
        final Dialog _dialog = new Dialog(this);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setCancelable(false);
        _dialog.setCanceledOnTouchOutside(true);

        _dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

        if (dialogLayout != 0) {
            _dialog.setContentView(dialogLayout);
        } else {
            _dialog.setContentView(R.layout.custom_popup_layout_2btn);
        }

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        int pxHeight = (int) (outMetrics.heightPixels / getResources().getDisplayMetrics().density) / 3;

        WindowManager.LayoutParams wmlp = _dialog.getWindow().getAttributes();

        wmlp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;

        wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmlp.y = pxHeight;

        TextView tvSampleText = (TextView) _dialog.findViewById(R.id.txtMessage);
        tvSampleText.setText(msg);

        final Button btnContinue = (Button) _dialog.findViewById(R.id.btnContinue);
        final Button btnContinue2 = (Button) _dialog.findViewById(R.id.btnContinue2);

        btnContinue.setText(btnTxt);
        btnContinue2.setText(btnTxt2);

        btnContinue.setVisibility(btnTxt.isEmpty() == true ? View.GONE : View.VISIBLE);
        btnContinue2.setVisibility(btnTxt2.isEmpty() == true ? View.GONE : View.VISIBLE);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnContinue.setClickable(false);
                createTrip("InviteBuddiesActivity");
            }
        });

        btnContinue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // createTrip("AllEventsActivityNew");
                btnContinue2.setClickable(false);
                createTrip("EventsDetailsActivity2");
            }
        });

        _dialog.show();
        _dialog.getWindow().setAttributes(wmlp);
    }

    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this, "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinneritem = Utility.interests[position];

        if (spinneritem.equals("Others")) {
            newlvl1.setVisibility(View.VISIBLE);
            certlvlid1_view.setVisibility(View.VISIBLE);
           /* if (newlvl1.getVisibility() == View.GONE) {



            } else {
                newlvl1.setVisibility(View.GONE);
                certlvlid1_view.setVisibility(View.GONE);
            }*/
        } else if (!spinneritem.equals("Others")) {
            newlvl1.setVisibility(View.GONE);
            certlvlid1_view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CreateEventActivity3.this, AllEventsActivityNew.class);
        startActivity(i);
    }

    private void bellcount() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllNotice> userCall = service.allnotice1("Bearer " + token);

        userCall.enqueue(new Callback<AllNotice>() {
            @Override
            public void onResponse(Call<AllNotice> call, Response<AllNotice> response) {
                //    if (mProgressDialog.isShowing())
                //      mProgressDialog.dismiss();
                // Log.d("STATUS", String.valueOf(response.body().getStatus()));
                // Log.d("onResponse:", "" + response.body().getNotification_details());
                bellcounter = response.body().getCounter();
                // Log.d("123456:", bellcounter);
                if (bellcounter.equals("0")) {
                    bell_counter.setVisibility(View.GONE);
                } else if (!bellcounter.equals("0")) {
                    bell_counter.setVisibility(View.VISIBLE);
                }
                bell_counter.setText(bellcounter);

            }

            @Override
            public void onFailure(Call<AllNotice> call, Throwable t) {

            }
        });
    }
}
