package com.bluebuddy.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bluebuddy.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreateEventActivity2 extends BuddyActivity {
    private Spinner spinner1;
    EditText cedf1, cet1;
    ImageView ivdf1, ivt1;
    Button createEvent, frm_date1, to_date1;
    Activity _myActivity;
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar2 = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_create_event2);
        super.onCreate(savedInstanceState);
        super.initialize();
        super.setTitle("CREATE YOUR TRIP");
        frm_date1 = (Button) findViewById(R.id.frm_date1);
        cedf1 = (EditText) findViewById(R.id.CEdf1);
        ivdf1 = (ImageView) findViewById(R.id.IVdf1);
        _myActivity = this;

        createEvent = (Button) findViewById(R.id.btn_cevent);
        spinner1 = (Spinner) findViewById(R.id.spacttype);
        List<String> list = new ArrayList<String>();
        list.add("Fishing");
        list.add("Scuba Diving");
        list.add("Freediving");
        list.add("SpearFishing");
        list.add("Kayaking");
        list.add("Photography");

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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(dataAdapter);

        // Spinner item selection Listener
        addListenerOnSpinnerItemSelection();
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String rc1 = "AllEventsActivity";
                oDialog("Thanks for creating your Trips.", "Close", "", true, _myActivity, rc1, 0);

            }
        });
        //date from
        frm_date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField();
            }

            private void setDateTimeField() {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEventActivity2.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
    }


    public void addListenerOnSpinnerItemSelection() {
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }

}
