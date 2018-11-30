package com.bluebuddy.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.bluebuddy.R;
import com.bluebuddy.adapters.CustomAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateEventActivity extends BuddyActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner1;
    View certlvlid1_view;
    EditText cedf1, cet1, certlvlid1;
    ImageView ivdf1, ivt1, back;
    Button createEvent, frm_date1, to_date1, minus_level1;
    Activity _myActivity;
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar2 = Calendar.getInstance();
    String[] activity_type_menu = {"Fishing", "Scuba Diving", "Freediving", "SpearFishing", "Kayaking", "Photography", "Select Activity Type"};
    String spinneritem = "";
    LinearLayout newlvl1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_create_event);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.setTitle("CREATE YOUR TRIP");
        back = (ImageView) findViewById(R.id.imgNotification2);
        newlvl1 = (LinearLayout) findViewById(R.id.newlvl1);
        minus_level1 = (Button) findViewById(R.id.minus_level1);
        certlvlid1_view = (View) findViewById(R.id.certlvlid1_view);
        certlvlid1 = (EditText) findViewById(R.id.certlvlid1);
        frm_date1 = (Button) findViewById(R.id.frm_date1);
        cedf1 = (EditText) findViewById(R.id.CEdf1);
        ivdf1 = (ImageView) findViewById(R.id.IVdf1);

        _myActivity = this;

        createEvent = (Button) findViewById(R.id.btn_cevent);
        spinner1 = (Spinner) findViewById(R.id.spacttype);

        spinner1.setOnItemSelectedListener(this);

        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), activity_type_menu);
        spinner1.setAdapter(customAdapter);
        spinner1.setSelection(activity_type_menu.length - 1);

        spinneritem = spinner1.getSelectedItem().toString();
        minus_level1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner1.setSelection(activity_type_menu.length - 1);
                newlvl1.setVisibility(View.GONE);
                certlvlid1_view.setVisibility(View.GONE); /*Gone is used for ommit the layout space*/
                certlvlid1.setText("");

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
                String myFormat = "dd.MM.yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                cedf1.setText(sdf.format(myCalendar.getTime()));
            }

        };

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String rc1 = "InviteBuddiesActivity";
                final String rc2 = "EventsDeatilsActivity";
                oDialog("Thanks for creating your Trips.You can now Invite your buddies to join the trip. ", "Invite BuddyDetail", "Skip", true, true, _myActivity, rc1, rc2, 0);

            }
        });
        //event_date
        frm_date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField();
            }

            private void setDateTimeField() {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEventActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
    }


    public void addListenerOnSpinnerItemSelection() {

    }


    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final boolean _redirect1, final Activity activity, final String _redirectClass, final String _redirectClass1, final int layout) {
        super.openDialog7(msg, btnText, btnText2, _redirect, _redirect1, activity, _redirectClass, _redirectClass1, layout);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinneritem = activity_type_menu[position];
        if (spinneritem.equals("Others")) {
            if (newlvl1.getVisibility() == View.GONE) {
                newlvl1.setVisibility(View.VISIBLE);
                certlvlid1_view.setVisibility(View.VISIBLE);
            } else {

                newlvl1.setVisibility(View.GONE);
                certlvlid1_view.setVisibility(View.GONE);

            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
