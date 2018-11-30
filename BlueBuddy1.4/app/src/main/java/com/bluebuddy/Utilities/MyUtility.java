package com.bluebuddy.Utilities;

import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;



public class MyUtility {

    public static String namePattern = "[a-zA-Z ]+";
    public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    // public static String phonePattern = "^(?:(?:\\(?(?:0(?:0|11)\\)?[\\s-]?\\(?|\\+)44\\)?[\\s-]?(?:\\(?0\\)?[\\s-]?)?)|(?:\\(?0))(?:(?:\\d{5}\\)?[\\s-]?\\d{4,5})|(?:\\d{4}\\)?[\\s-]?(?:\\d{5}|\\d{3}[\\s-]?\\d{3}))|(?:\\d{3}\\)?[\\s-]?\\d{3}[\\s-]?\\d{3,4})|(?:\\d{2}\\)?[\\s-]?\\d{4}[\\s-]?\\d{4}))(?:[\\s-]?(?:x|ext\\.?|\\#)\\d{3,4})?$";
    public static String phonePattern = "^([0-9\\\\+]|\\\\(\\\\d{1,3}\\\\))[0-9\\\\-\\\\. ]{3,15}$";

    // public static String phonePattern = "[0-9]{10}";
    public MyUtility() {
    }

    public static String appendListData(ArrayList<String> a, String prefix, String delimeter) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < a.size(); i++) {

            if (!a.get(i).isEmpty()) {
                if (!prefix.isEmpty()) {
                    sb.append(prefix);
                }

                sb.append(a.get(i));

                if (i != a.size() - 1) {
                    sb.append(delimeter);
                }
            }
        }

        return sb.toString();
    }

    public static String getTrimText(TextView v, String getType) {
        if (getType == "text") {
            return v.getText().toString().trim();
        } else {
            return v.getTag().toString().trim();
        }
    }

    public static String getTrimText(EditText v, String getType) {
        if (getType == "text") {
            return v.getText().toString().trim();
        } else {
            return v.getTag().toString().trim();
        }
    }
}
