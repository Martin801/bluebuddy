package com.bluebuddy.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bluebuddy.R;

public class EnquiryActivity extends BuddyActivity {
    Button EnqPopup;
    Activity _myActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setLayout(R.layout.activity_enquiry);
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_enquiry);
        super.initialize();
        _myActivity = this;
        super.setTitle("ENQUIRY");

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PTC55F.ttf");

        TextView IDtxt1 = (TextView) findViewById(R.id.idtxt1);
        TextView IDtxt2 = (TextView) findViewById(R.id.idtxt2);

        IDtxt1.setTypeface(custom_font);
        IDtxt2.setTypeface(custom_font);

        EnqPopup = (Button) findViewById(R.id.enqpopup);
        EnqPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rc="BlueMarketActivity2";
                oDialog("Thanks for your enquiry. ", "Submit", "", true, _myActivity, rc, 0);

            }
        });
    }

    public void oDialog(final String msg, final String btnText, final String btnText2, final boolean _redirect, final Activity activity, final String _redirectClass, final int layout) {
        super.openDialog(msg, btnText, btnText2, _redirect, activity, _redirectClass, layout);
    }
}
