package com.bluebuddy.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluebuddy.R;
import com.bluebuddy.adapters.BlueCharterAdapter;
import com.bluebuddy.adapters.BlueCharterAdapter2;
import com.bluebuddy.api.ApiClient;
import com.bluebuddy.app.AppConfig;
import com.bluebuddy.classes.MyTextView;
import com.bluebuddy.classes.ServerResponseCp7;
import com.bluebuddy.data.FriendDB;
import com.bluebuddy.helpers.SessionManager;
import com.bluebuddy.interfaces.ApiInterface;
import com.bluebuddy.interfaces.BuddyInterface;
import com.bluebuddy.models.AllNotice;
import com.bluebuddy.models.FlagResponse;
import com.bluebuddy.services.ServiceUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bluebuddy.app.AppConfig.ACCESS_STEP;
import static com.bluebuddy.app.AppConfig.activityPath;

public class BuddyActivity extends AppCompatActivity implements BuddyInterface {

    private static final String TAG = "BuddyActivity";
    final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";
    ProgressDialog mProgressDialog;
    // Define variables for custom tabs and its builder
    CustomTabsIntent customTabsIntent;
    CustomTabsIntent.Builder intentBuilder;
    CustomTabsClient mCustomTabsClient;
    CustomTabsSession mCustomTabsSession;
    CustomTabsServiceConnection mCustomTabsServiceConnection;
    CustomTabsIntent mCustomTabsIntent;
    private boolean checkable = true;
    private int layout;
    private MyTextView navtxt;
    private ImageView allevt_img, evtsrch_img, bmrkt_img, chat_img, accnt_img, notice;
    private SessionManager session;

    public void loader() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
    }

    public void notice() {
        notice = (ImageView) findViewById(R.id.imgNotification);
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplication().getApplicationContext(), NotificationActivity.class);
                startActivity(i);

            }
        });
    }

    public void initialize() {

        allevt_img = (ImageView) findViewById(R.id.img_allevt);
        evtsrch_img = (ImageView) findViewById(R.id.img_evtsrch);
        bmrkt_img = (ImageView) findViewById(R.id.img_bmrkt);
        chat_img = (ImageView) findViewById(R.id.img_chat);
        accnt_img = (ImageView) findViewById(R.id.img_accnt);

        final Button btnAEvent = (Button) findViewById(R.id.btn_AllEventft);
        final Button btnESearch = (Button) findViewById(R.id.btn_evtsrch);
        final Button btnBMarket = (Button) findViewById(R.id.btn_bmrkt);
        final Button btnChat = (Button) findViewById(R.id.btn_chat);
        final Button btnAccount = (Button) findViewById(R.id.btn_Account);

        final Button btnBuddyUp = (Button) findViewById(R.id.Buddyupid);
        final Button btnbluemrktcourse = (Button) findViewById(R.id.bmcoursecontid);

        btnAEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplication().getApplicationContext(), CreateProfileActivity2.class);
                intent.putExtra("pass", 2);
                startActivity(intent);
                finish();

            }
        });

        btnESearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplication().getApplicationContext(), PeopleSearchActivitynew.class);
                startActivity(intent);
                finish();

            }
        });

        btnBMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplication().getApplicationContext(), Categories_bluemarketActivity.class);
                startActivity(intent);
                finish();

            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplication().getApplicationContext(), ChatActivity.class);
                startActivity(intent);
                finish();

            }
        });

        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplication().getApplicationContext(), MyAccountMenu.class);
                startActivity(intent);
                finish();

            }
        });

    }

    private void setImageResource() {
        allevt_img.setImageResource(R.drawable.date);
        evtsrch_img.setImageResource(R.drawable.zoom);
        bmrkt_img.setImageResource(R.drawable.blue_market);
        chat_img.setImageResource(R.drawable.chat);
        accnt_img.setImageResource(R.drawable.user);
    }

    public void setImageResource1(int x) {
        switch (x) {

            case 1:
                allevt_img.setImageResource(R.drawable.calender);
                break;

            case 2:
                evtsrch_img.setImageResource(R.drawable.zoom_icon1);
                break;

            case 3:
                bmrkt_img.setImageResource(R.drawable.market_icon1);
                break;

            case 4:
                chat_img.setImageResource(R.drawable.chat1);
                final Button btnChat = (Button) findViewById(R.id.btn_chat);
                btnChat.setClickable(false);
                break;

            case 5:
                accnt_img.setImageResource(R.drawable.user_icon1);
                break;

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(layout);
        } catch (Exception e) {
            Log.d(TAG, "onCreate: " + e.getMessage());
        }
        session = new SessionManager(this);

//        customTabs();
    }

    protected void setPref(String key, String val) {
        session.setPref(key, val);
    }

    protected SharedPreferences getPref() {
        return session.getPref();
    }

    @Override
    public void setLayout(int layout) {
        this.layout = layout;
    }

    @Override
    public void setTitle(String title) {
        navtxt = (MyTextView) findViewById(R.id.navtxt);
        navtxt.setText(title);
    }

    @Override
    public void setNotificationCount(int count) {

    }

    public void customTabs() {
        // Initialize intentBuilder
//        intentBuilder = new CustomTabsIntent.Builder();
//
//        // Set toolbar(tab) color of your chrome browser
//        intentBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
//
//        // build it by setting up all
//        customTabsIntent = intentBuilder.build();

        mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                mCustomTabsClient = customTabsClient;
                mCustomTabsClient.warmup(0L);
                mCustomTabsSession = mCustomTabsClient.newSession(null);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mCustomTabsClient = null;
            }
        };

        CustomTabsClient.bindCustomTabsService(this, CUSTOM_TAB_PACKAGE_NAME, mCustomTabsServiceConnection);

        mCustomTabsIntent = new CustomTabsIntent.Builder(mCustomTabsSession)
                .setShowTitle(true)
                .build();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                onBackPressed();
                return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();

            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }

        return super.dispatchTouchEvent(event);
    }

    public void logOut() {
        session.setLogout();
//        SharedPrefManager.getInstance(getApplicationContext()).setFirebaseLogout();
    }

    public void openDialogReturn(String msg, String btnTxt, String btnTxt2, final boolean _redirect, final Activity activity, final String _redirectClass, int dialogLayout, final String returnParam) {
        final Dialog _dialog = new Dialog(this);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setCancelable(false);
        _dialog.setCanceledOnTouchOutside(true);

        _dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

        if (dialogLayout != 0) {
            _dialog.setContentView(dialogLayout);
        } else {
            _dialog.setContentView(R.layout.custom_popup_layout);
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

        Button btnContinue = (Button) _dialog.findViewById(R.id.btnContinue);
        Button btnContinue2 = (Button) _dialog.findViewById(R.id.btnContinue2);

        btnContinue.setText(btnTxt);
        btnContinue2.setText(btnTxt2);

        btnContinue.setVisibility(btnTxt.isEmpty() == true ? View.GONE : View.VISIBLE);
        btnContinue2.setVisibility(btnTxt2.isEmpty() == true ? View.GONE : View.VISIBLE);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (_redirect) {
                    try {

                        Intent intent = new Intent(activity, Class.forName(activityPath + "." + _redirectClass));
                        intent.putExtra("category", returnParam);
                        activity.startActivity(intent);
                        activity.finish();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    _dialog.dismiss();
                }
            }
        });

        btnContinue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialog.dismiss();
            }
        });

        _dialog.show();
        _dialog.getWindow().setAttributes(wmlp);
    }

    //Custom Dialog code
    public void openDialog(String msg, String btnTxt, String btnTxt2, final boolean _redirect, final Activity activity, final String _redirectClass, int dialogLayout) {
        final Dialog _dialog = new Dialog(this);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setCancelable(false);
        _dialog.setCanceledOnTouchOutside(true);

        _dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

        if (dialogLayout != 0) {
            _dialog.setContentView(dialogLayout);
        } else {
            _dialog.setContentView(R.layout.custom_popup_layout);
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

        Button btnContinue = (Button) _dialog.findViewById(R.id.btnContinue);
        Button btnContinue2 = (Button) _dialog.findViewById(R.id.btnContinue2);

        btnContinue.setText(btnTxt);
        btnContinue2.setText(btnTxt2);

        btnContinue.setVisibility(btnTxt.isEmpty() == true ? View.GONE : View.VISIBLE);
        btnContinue2.setVisibility(btnTxt2.isEmpty() == true ? View.GONE : View.VISIBLE);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_redirect) {
                    try {

                        Intent intent = new Intent(activity, Class.forName(activityPath + "." + _redirectClass));
                        activity.startActivity(intent);
                        activity.finish();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    _dialog.dismiss();
                }
            }
        });

        btnContinue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialog.dismiss();
            }
        });

        _dialog.show();
        _dialog.getWindow().setAttributes(wmlp);
    }

    //2ndopendialog
    public void openDialog2(String msg, String btnTxt, String btnTxt2, final boolean _redirect, final Activity activity, final String _redirectClass, int dialogLayout) {
        final Dialog _dialog = new Dialog(this);

        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setCancelable(false);
        _dialog.setCanceledOnTouchOutside(true);

        _dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

        //
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

        Button btnContinue = (Button) _dialog.findViewById(R.id.btnContinue);
        Button btnContinue2 = (Button) _dialog.findViewById(R.id.btnContinue2);

        btnContinue.setText(btnTxt);
        btnContinue2.setText(btnTxt2);

        btnContinue.setVisibility(btnTxt.isEmpty() == true ? View.GONE : View.VISIBLE);
        btnContinue2.setVisibility(btnTxt2.isEmpty() == true ? View.GONE : View.VISIBLE);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (_redirect) {
                    try {
                        Intent intent = new Intent(activity, Class.forName(activityPath + "." + _redirectClass));
                        activity.startActivity(intent);
                        activity.finish();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    _dialog.dismiss();
                }
            }
        });

        btnContinue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialog.dismiss();
            }
        });

        _dialog.show();
        _dialog.getWindow().setAttributes(wmlp);
    }

    //3rdopen
    public void openDialog3(String msg, String btnTxt, String btnTxt2, final boolean _redirect, final Activity activity, final String _redirectClass, int dialogLayout) {
        final Dialog _dialog = new Dialog(this);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setCancelable(false);
        _dialog.setCanceledOnTouchOutside(true);

        _dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);


        if (dialogLayout != 0) {
            _dialog.setContentView(dialogLayout);
        } else {
            _dialog.setContentView(R.layout.custom_popup_layout_camerapp);
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


        Button btnContinue = (Button) _dialog.findViewById(R.id.btnContinue);
        Button btnContinue2 = (Button) _dialog.findViewById(R.id.btnContinue2);

        btnContinue.setText(btnTxt);
        btnContinue2.setText(btnTxt2);

        btnContinue.setVisibility(btnTxt.isEmpty() == true ? View.GONE : View.VISIBLE);
        btnContinue2.setVisibility(btnTxt2.isEmpty() == true ? View.GONE : View.VISIBLE);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (_redirect) {
                    try {

                        Intent intent = new Intent(activity, Class.forName(activityPath + "." + _redirectClass));
                        activity.startActivity(intent);
                        activity.finish();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    _dialog.dismiss();
                }
            }
        });

        btnContinue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialog.dismiss();
            }
        });

        _dialog.show();
        _dialog.getWindow().setAttributes(wmlp);
    }

    //4thopen
    public void openDialog4(String msg, String btnTxt, String btnTxt2, String btnTxt3, String btnTxt4, final boolean _redirect, final Activity activity, final String _redirectClass, int dialogLayout) {

        final Dialog _dialog = new Dialog(this);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setCancelable(false);
        _dialog.setCanceledOnTouchOutside(true);

        _dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

        if (dialogLayout != 0) {
            _dialog.setContentView(dialogLayout);
        } else {
            _dialog.setContentView(R.layout.listing_popup);
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

        Button btnContinue = (Button) _dialog.findViewById(R.id.btnContinue);
        Button btnContinue2 = (Button) _dialog.findViewById(R.id.btnContinue2);
        Button btnContinue3 = (Button) _dialog.findViewById(R.id.btnContinue3);
        Button btnContinue4 = (Button) _dialog.findViewById(R.id.btnContinue4);
        btnContinue.setText(btnTxt);
        btnContinue2.setText(btnTxt2);
        btnContinue3.setText(btnTxt3);
        btnContinue4.setText(btnTxt4);

        btnContinue.setVisibility(btnTxt.isEmpty() == true ? View.GONE : View.VISIBLE);
        btnContinue2.setVisibility(btnTxt2.isEmpty() == true ? View.GONE : View.VISIBLE);
        btnContinue3.setVisibility(btnTxt3.isEmpty() == true ? View.GONE : View.VISIBLE);
        btnContinue4.setVisibility(btnTxt4.isEmpty() == true ? View.GONE : View.VISIBLE);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialog.dismiss();

            }
        });

        btnContinue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialog.dismiss();
            }
        });
        btnContinue3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialog.dismiss();
            }
        });
        btnContinue4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_redirect) {
                    try {

                        Intent intent = new Intent(activity, Class.forName(activityPath + "." + _redirectClass));
                        activity.startActivity(intent);
                        activity.finish();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    _dialog.dismiss();
                }
            }
        });
        _dialog.show();
        _dialog.getWindow().setAttributes(wmlp);

    }

    public void openDialog6(String msg, String btnTxt, String btnTxt2, final boolean _redirect, final Activity activity, final String _redirectClass, int dialogLayout, final String data) {

        final Dialog _dialog = new Dialog(this);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setCancelable(false);
        _dialog.setCanceledOnTouchOutside(false);

        _dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

        if (dialogLayout != 0) {
            _dialog.setContentView(dialogLayout);
        } else {
            _dialog.setContentView(R.layout.custom_popup_layout);
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

        Button btnContinue = (Button) _dialog.findViewById(R.id.btnContinue);
        Button btnContinue2 = (Button) _dialog.findViewById(R.id.btnContinue2);

        btnContinue.setText(btnTxt);
        btnContinue2.setText(btnTxt2);

        btnContinue.setVisibility(btnTxt.isEmpty() == true ? View.GONE : View.VISIBLE);
        btnContinue2.setVisibility(btnTxt2.isEmpty() == true ? View.GONE : View.VISIBLE);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (_redirect) {
                    try {

                        Intent intent = new Intent(activity, Class.forName(activityPath + "." + _redirectClass));


                        Bundle bundle = new Bundle();
                        bundle.putString("DATA_VALUE", data);
                        intent.putExtras(bundle);

                        activity.startActivity(intent);
                        activity.finish();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    _dialog.dismiss();
                }
            }
        });

        btnContinue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialog.dismiss();
            }
        });

        _dialog.show();
        _dialog.getWindow().setAttributes(wmlp);

    }

    public void openDialog6Category(String msg, String btnTxt, String btnTxt2, final boolean _redirect, final Activity activity, final String _redirectClass, int dialogLayout, final String data) {

        final Dialog _dialog = new Dialog(this);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setCancelable(false);
        _dialog.setCanceledOnTouchOutside(false);

        _dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

        if (dialogLayout != 0) {
            _dialog.setContentView(dialogLayout);
        } else {
            _dialog.setContentView(R.layout.custom_popup_layout);
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

        Button btnContinue = (Button) _dialog.findViewById(R.id.btnContinue);
        Button btnContinue2 = (Button) _dialog.findViewById(R.id.btnContinue2);

        btnContinue.setText(btnTxt);
        btnContinue2.setText(btnTxt2);

        btnContinue.setVisibility(btnTxt.isEmpty() == true ? View.GONE : View.VISIBLE);
        btnContinue2.setVisibility(btnTxt2.isEmpty() == true ? View.GONE : View.VISIBLE);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (_redirect) {
                    try {

                        Intent intent = new Intent(activity, Class.forName(activityPath + "." + _redirectClass));


                        Bundle bundle = new Bundle();
                        bundle.putString("category", data);
                        intent.putExtras(bundle);

                        activity.startActivity(intent);
                        activity.finish();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    _dialog.dismiss();
                }
            }
        });

        btnContinue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialog.dismiss();
            }
        });

        _dialog.show();
        _dialog.getWindow().setAttributes(wmlp);

    }

    public void openDialog7(String msg, String btnTxt, String btnTxt2, final boolean _redirect, final boolean _redirect1, final Activity activity, final String _redirectClass, final String _redirectClass1, int dialogLayout) {

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

        Button btnContinue = (Button) _dialog.findViewById(R.id.btnContinue);
        Button btnContinue2 = (Button) _dialog.findViewById(R.id.btnContinue2);

        btnContinue.setText(btnTxt);
        btnContinue2.setText(btnTxt2);

        btnContinue.setVisibility(btnTxt.isEmpty() == true ? View.GONE : View.VISIBLE);
        btnContinue2.setVisibility(btnTxt2.isEmpty() == true ? View.GONE : View.VISIBLE);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_redirect) {
                    try {
                        Intent intent = new Intent(activity, Class.forName(activityPath + "." + _redirectClass));
                        activity.startActivity(intent);
                        activity.finish();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    _dialog.dismiss();
                }
            }
        });

        btnContinue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_redirect1) {
                    try {
                        Intent intent = new Intent(activity, Class.forName(activityPath + "." + _redirectClass1));
                        activity.startActivity(intent);
                        activity.finish();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    _dialog.dismiss();
                }
            }
        });
        _dialog.show();
        _dialog.getWindow().setAttributes(wmlp);
    }

    /* 8 opendialog */
    public void openDialog8(String msg, String btnTxt, String btnTxt2, final boolean _redirect, final Activity activity, final String _redirectClass, int dialogLayout) {

        final Dialog _dialog = new Dialog(this);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setCancelable(false);
        _dialog.setCanceledOnTouchOutside(true);
        _dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

        //
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

        Button btnContinue = (Button) _dialog.findViewById(R.id.btnContinue);
        Button btnContinue2 = (Button) _dialog.findViewById(R.id.btnContinue2);

        btnContinue.setText(btnTxt);
        btnContinue2.setText(btnTxt2);

        btnContinue.setVisibility(btnTxt.isEmpty() == true ? View.GONE : View.VISIBLE);
        btnContinue2.setVisibility(btnTxt2.isEmpty() == true ? View.GONE : View.VISIBLE);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_redirect) {

                    if (_redirectClass.equals("LoginActivity")) {
                        try {

                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... params) {
                                    {
                                        try {
                                            FirebaseInstanceId.getInstance().deleteInstanceId();

                                            Log.d(TAG, "Getting new token");
                                            FirebaseInstanceId.getInstance().getToken();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void result) {
                                    FirebaseAuth.getInstance().signOut();
                                    FriendDB.getInstance(activity).dropDB();
                                    ServiceUtils.stopServiceFriendChat(activity.getApplicationContext(), true);
                                    logOut();
                                    Intent intent = null;
                                    try {
                                        intent = new Intent(activity, Class.forName(activityPath + "." + _redirectClass));
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    activity.startActivity(intent);
                                    activity.finish();
                                }
                            }.execute();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {

                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... params) {
                                    {
                                        try {
                                            FirebaseInstanceId.getInstance().deleteInstanceId();

                                            Log.d(TAG, "Getting new token");
                                            FirebaseInstanceId.getInstance().getToken();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void result) {
                                    //call your activity where you want to land after log out
                                    FirebaseAuth.getInstance().signOut();
                                    FriendDB.getInstance(activity).dropDB();
                                    ServiceUtils.stopServiceFriendChat(activity.getApplicationContext(), true);
                                    logOut();
                                    Intent intent = null;
                                    try {
                                        intent = new Intent(activity, Class.forName(activityPath + "." + _redirectClass));
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    activity.startActivity(intent);
                                    activity.finish();
                                }
                            }.execute();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    _dialog.dismiss();
                }
            }
        });

        btnContinue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialog.dismiss();
            }
        });

        _dialog.show();
        _dialog.getWindow().setAttributes(wmlp);

    }

    public void logoutAndSignInPage(final Activity activity) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                {
                    try {
                        FirebaseInstanceId.getInstance().deleteInstanceId();

                        Log.d(TAG, "Getting new token");
                        FirebaseInstanceId.getInstance().getToken();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                //call your activity where you want to land after log out
                FirebaseAuth.getInstance().signOut();

                FriendDB.getInstance(activity).dropDB();
                ServiceUtils.stopServiceFriendChat(activity.getApplicationContext(), true);
                logOut();
                Intent intent = new Intent(BuddyActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                activity.startActivity(intent);
                activity.finish();
            }
        }.execute();


    }

    // 3 vertical button
    public void openDialog5(String msg, String btnTxt, String btnTxt2, String btnTxt3, final boolean _redirect, final Activity activity, final String _redirectClass, int dialogLayout) {
        final Dialog _dialog = new Dialog(this);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setCancelable(false);
        _dialog.setCanceledOnTouchOutside(true);
        _dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

        if (dialogLayout != 0) {
            _dialog.setContentView(dialogLayout);
        } else {
            _dialog.setContentView(R.layout.listing_popup3);
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

        Button btnContinue = (Button) _dialog.findViewById(R.id.btnContinue);
        Button btnContinue2 = (Button) _dialog.findViewById(R.id.btnContinue2);
        Button btnContinue3 = (Button) _dialog.findViewById(R.id.btnContinue3);

        btnContinue.setText(btnTxt);
        btnContinue2.setText(btnTxt2);
        btnContinue3.setText(btnTxt3);


        btnContinue.setVisibility(btnTxt.isEmpty() == true ? View.GONE : View.VISIBLE);
        btnContinue2.setVisibility(btnTxt2.isEmpty() == true ? View.GONE : View.VISIBLE);
        btnContinue3.setVisibility(btnTxt3.isEmpty() == true ? View.GONE : View.VISIBLE);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialog.dismiss();

            }
        });

        btnContinue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialog.dismiss();
            }
        });
        btnContinue3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialog.dismiss();
            }
        });

        _dialog.show();
        _dialog.getWindow().setAttributes(wmlp);
    }

    public void openDialog9(String msg, String btnTxt, String btnTxt2, final boolean _redirect, final Activity activity, final String _redirectClass, int dialogLayout, final String tOKEN) {
        final Dialog _dialog = new Dialog(this);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setCancelable(false);
//        _dialog.setCanceledOnTouchOutside(true);
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

        Button btnContinue = (Button) _dialog.findViewById(R.id.btnContinue);
        Button btnContinue2 = (Button) _dialog.findViewById(R.id.btnContinue2);

        btnContinue.setText(btnTxt);
        btnContinue2.setText(btnTxt2);

        btnContinue.setVisibility(btnTxt.isEmpty() == true ? View.GONE : View.VISIBLE);
        btnContinue2.setVisibility(btnTxt2.isEmpty() == true ? View.GONE : View.VISIBLE);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_redirect) {
                    try {
                        setPref(ACCESS_STEP, "9");
                        Intent intent = new Intent(activity, Class.forName(activityPath + "." + _redirectClass));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Bundle bundle = new Bundle();
                        bundle.putString("TOKEN_VALUE", tOKEN);
                        intent.putExtras(bundle);
                        //
                        activity.startActivity(intent);
                        activity.finish();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    _dialog.dismiss();
                }
            }
        });

        btnContinue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                Call<ServerResponseCp7> userCall = service.uploadImage("Bearer " + tOKEN, "", 8);
                userCall.enqueue(new Callback<ServerResponseCp7>() {
                    @Override
                    public void onResponse(Call<ServerResponseCp7> call, Response<ServerResponseCp7> response) {
//                        Log.d("onResponse", "" + response.body().getMessage());
//                        setPref(ACCESS_STEP, "8");
//                        Intent i = new Intent(BuddyActivity.this, CreateProfileActivity7.class);
//                        startActivity(i);
                    }

                    @Override
                    public void onFailure(Call<ServerResponseCp7> call, Throwable t) {
                        Log.d("onFailure", t.toString());
                    }
                });
                _dialog.dismiss();
            }
        });

        _dialog.show();
        _dialog.getWindow().setAttributes(wmlp);
    }

    /*10dialog*/
    public void openDialog10(String msg, String btnTxt, String btnTxt2, final boolean _redirect, final Activity activity, final String _redirectClass, int dialogLayout, final String data) {
        final Dialog _dialog = new Dialog(this);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setCancelable(false);
        _dialog.setCanceledOnTouchOutside(false);
        _dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

        if (dialogLayout != 0) {
            _dialog.setContentView(dialogLayout);
        } else {
            _dialog.setContentView(R.layout.custom_popup_layout);
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

        Button btnContinue = (Button) _dialog.findViewById(R.id.btnContinue);
        Button btnContinue2 = (Button) _dialog.findViewById(R.id.btnContinue2);

        btnContinue.setText(btnTxt);
        btnContinue2.setText(btnTxt2);

        btnContinue.setVisibility(btnTxt.isEmpty() == true ? View.GONE : View.VISIBLE);
        btnContinue2.setVisibility(btnTxt2.isEmpty() == true ? View.GONE : View.VISIBLE);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (_redirect) {
                    try {

                        Intent intent = new Intent(activity, Class.forName(activityPath + "." + _redirectClass));

                        Bundle bundle = new Bundle();
                        bundle.putString("DATA_VALUE", data);
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                        activity.finish();

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    _dialog.dismiss();
                }
            }
        });

        btnContinue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialog.dismiss();
            }
        });

        _dialog.show();
        _dialog.getWindow().setAttributes(wmlp);
    }

    public void openDialog11(String msg, String btnTxt, String btnTxt2, final boolean _redirect, final Activity activity, final String _redirectClass, int dialogLayout, final String data) {
        final Dialog _dialog = new Dialog(this);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setCancelable(false);
        _dialog.setCanceledOnTouchOutside(true);

        _dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);


        if (dialogLayout != 0) {
            _dialog.setContentView(dialogLayout);
        } else {
            _dialog.setContentView(R.layout.custom_popup_layout);
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

        Button btnContinue = (Button) _dialog.findViewById(R.id.btnContinue);
        Button btnContinue2 = (Button) _dialog.findViewById(R.id.btnContinue2);

        btnContinue.setText(btnTxt);
        btnContinue2.setText(btnTxt2);

        btnContinue.setVisibility(btnTxt.isEmpty() == true ? View.GONE : View.VISIBLE);
        btnContinue2.setVisibility(btnTxt2.isEmpty() == true ? View.GONE : View.VISIBLE);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (_redirect) {
                    try {

                        Intent intent = new Intent(activity, Class.forName(activityPath + "." + _redirectClass));
                        Bundle bundle = new Bundle();
                        bundle.putString("DATA_VALUE", data);
                        intent.putExtras(bundle);

                        activity.startActivity(intent);
                        activity.finish();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    _dialog.dismiss();
                }
            }
        });

        btnContinue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialog.dismiss();
            }
        });

        _dialog.show();
        _dialog.getWindow().setAttributes(wmlp);
    }

    public void openDialog12(String msg, String btnTxt, String btnTxt2, final boolean _redirect, final Activity activity, final String _redirectClass, int dialogLayout, final String encoded_image, final String encodedImageList) {
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

        Button btnContinue = (Button) _dialog.findViewById(R.id.btnContinue);
        Button btnContinue2 = (Button) _dialog.findViewById(R.id.btnContinue2);

        btnContinue.setText(btnTxt);
        btnContinue2.setText(btnTxt2);

        btnContinue.setVisibility(btnTxt.isEmpty() == true ? View.GONE : View.VISIBLE);
        btnContinue2.setVisibility(btnTxt2.isEmpty() == true ? View.GONE : View.VISIBLE);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (_redirect) {
                    try {

                        Intent intent = new Intent(activity, Class.forName(activityPath + "." + _redirectClass));

                        Bundle bundle = new Bundle();
                        bundle.putString("encoded_image", encoded_image);
                        bundle.putString("DATA_VALUE", encodedImageList);
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                        activity.finish();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    _dialog.dismiss();
                }
            }
        });

        btnContinue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialog.dismiss();
            }
        });

        _dialog.show();
        _dialog.getWindow().setAttributes(wmlp);
    }

    public void openDialog13(String msg, String btnTxt, String btnTxt2, final boolean _redirect, final Activity activity, final String _redirectClass, int dialogLayout, final String data) {
        final Dialog _dialog = new Dialog(this);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setCancelable(false);
        _dialog.setCanceledOnTouchOutside(false);
        _dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

        if (dialogLayout != 0) {
            _dialog.setContentView(dialogLayout);
        } else {
            _dialog.setContentView(R.layout.custom_popup_layout);
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

        Button btnContinue = (Button) _dialog.findViewById(R.id.btnContinue);
        Button btnContinue2 = (Button) _dialog.findViewById(R.id.btnContinue2);

        btnContinue.setText(btnTxt);
        btnContinue2.setText(btnTxt2);

        btnContinue.setVisibility(btnTxt.isEmpty() == true ? View.GONE : View.VISIBLE);
        btnContinue2.setVisibility(btnTxt2.isEmpty() == true ? View.GONE : View.VISIBLE);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_redirect) {
                    try {

                        Intent intent = new Intent(activity, Class.forName(activityPath + "." + _redirectClass));

                        Bundle bundle = new Bundle();
                        bundle.putString("category", data);
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                        activity.finish();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    _dialog.dismiss();
                }
            }
        });

        btnContinue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialog.dismiss();
            }
        });

        _dialog.show();
        _dialog.getWindow().setAttributes(wmlp);
    }

    /* 14 opendialog */
    public void openDialog14(String msg, String btnTxt, String btnTxt2, final boolean _redirect, final Activity activity, final String _redirectClass, int dialogLayout, final String type, final String type_id, final String token, final BlueCharterAdapter.ViewHolder holder) {
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

        Button btnContinue = (Button) _dialog.findViewById(R.id.btnContinue);
        Button btnContinue2 = (Button) _dialog.findViewById(R.id.btnContinue2);

        btnContinue.setText(btnTxt);
        btnContinue2.setText(btnTxt2);

        btnContinue.setVisibility(btnTxt.isEmpty() == true ? View.GONE : View.VISIBLE);
        btnContinue2.setVisibility(btnTxt2.isEmpty() == true ? View.GONE : View.VISIBLE);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
                LayoutInflater inflater = activity.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_popup_inputdialog, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
                edt.setHint("https://www.facebook.com/");
                dialogBuilder.setMessage("Enter Your Facebook Url");

                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final String comment = edt.getText().toString().trim();

                        holder.flag.setImageResource(R.drawable.ic_flag);
                        Log.d("fr comment", comment);

                        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                        Call<FlagResponse> userCall = service.fr("Bearer " + token, "post", type, type_id);


                        userCall.enqueue(new Callback<FlagResponse>() {
                            @Override
                            public void onResponse(Call<FlagResponse> call, Response<FlagResponse> response) {
                                if (response.body().getStatus() == "true") {
                                    Log.d("fr comment", "" + response.body().getStatus());
                                }
                            }

                            @Override
                            public void onFailure(Call<FlagResponse> call, Throwable t) {
                                Log.d("onFailure", t.toString());
                                t.printStackTrace();
                            }
                        });

                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();

            }
        });

        btnContinue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialog.dismiss();
            }
        });

        _dialog.show();
        _dialog.getWindow().setAttributes(wmlp);
    }

    public void openDialog15(String msg, String btnTxt, String btnTxt2, final boolean _redirect, final Activity activity, final String _redirectClass, int dialogLayout, final String type, final String type_id, final String token, final BlueCharterAdapter2.ViewHolder holder) {
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

        Button btnContinue = (Button) _dialog.findViewById(R.id.btnContinue);
        Button btnContinue2 = (Button) _dialog.findViewById(R.id.btnContinue2);

        btnContinue.setText(btnTxt);
        btnContinue2.setText(btnTxt2);

        btnContinue.setVisibility(btnTxt.isEmpty() == true ? View.GONE : View.VISIBLE);
        btnContinue2.setVisibility(btnTxt2.isEmpty() == true ? View.GONE : View.VISIBLE);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
                LayoutInflater inflater = activity.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_popup_inputdialog, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
                edt.setHint("https://www.facebook.com/");
                dialogBuilder.setMessage("Enter Your Facebook Url");

                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final String comment = edt.getText().toString().trim();

                        holder.flag_report.setImageResource(R.drawable.ic_flag);
                        Log.d("fr comment", comment);

                        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                        Call<FlagResponse> userCall = service.fr("Bearer " + token, "post", type, type_id);

                        userCall.enqueue(new Callback<FlagResponse>() {
                            @Override
                            public void onResponse(Call<FlagResponse> call, Response<FlagResponse> response) {
                                if (response.body().getStatus() == "true") {
                                    Log.d("fr comment", "" + response.body().getStatus());
                                }
                            }

                            @Override
                            public void onFailure(Call<FlagResponse> call, Throwable t) {
                                Log.d("onFailure", t.toString());
                                t.printStackTrace();
                            }
                        });

                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();

            }
        });

        btnContinue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialog.dismiss();
            }
        });

        _dialog.show();
        _dialog.getWindow().setAttributes(wmlp);
    }

    public void openDialog16(String msg, String btnTxt, String btnTxt2, final boolean _redirect, final Context context, final String _redirectClass, int dialogLayout) {
        final Dialog _dialog = new Dialog(this);
        _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _dialog.setCancelable(false);
        _dialog.setCanceledOnTouchOutside(true);

        _dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

        if (dialogLayout != 0) {
            _dialog.setContentView(dialogLayout);
        } else {
            _dialog.setContentView(R.layout.custom_popup_layout);
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

        Button btnContinue = (Button) _dialog.findViewById(R.id.btnContinue);
        Button btnContinue2 = (Button) _dialog.findViewById(R.id.btnContinue2);

        btnContinue.setText(btnTxt);
        btnContinue2.setText(btnTxt2);

        btnContinue.setVisibility(btnTxt.isEmpty() == true ? View.GONE : View.VISIBLE);
        btnContinue2.setVisibility(btnTxt2.isEmpty() == true ? View.GONE : View.VISIBLE);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_redirect) {
                    try {
                        Activity activity = (Activity) context.getApplicationContext();
                        Intent intent = new Intent(activity, Class.forName(activityPath + "." + _redirectClass));
                        activity.startActivity(intent);
                        activity.finish();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    _dialog.dismiss();
                }
            }
        });

        btnContinue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialog.dismiss();
            }
        });

        _dialog.show();
        _dialog.getWindow().setAttributes(wmlp);
    }

    public void general_bell_counter(String token, final BellCounterInterface bellCounterInterface) {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<AllNotice> userCall = service.allnotice1("Bearer " + token);

        userCall.enqueue(new Callback<AllNotice>() {
            @Override
            public void onResponse(Call<AllNotice> call, Response<AllNotice> response) {
                // Log.d("STATUS", String.valueOf(response.body().getStatus()));
                // Log.d("onResponse:", "" + response.body().getNotification_details());

                if (response.code() == AppConfig.SUCCESS_STATUS) {
                    String bellcounter = response.body().getCounter();
                    if (bellCounterInterface != null) {
                        bellCounterInterface.getBellCount(bellcounter);
                    }
                } else if (response.code() == AppConfig.ERROR_STATUS_LOG_AGAIN) {
                    // login again
                    // logoutAndSignInPage(BuddyActivity.this);

                } else {


                }

            }

            @Override
            public void onFailure(Call<AllNotice> call, Throwable t) {

            }
        });
    }

    public interface BellCounterInterface {
        // you can define any parameter as per your requirement
        public void getBellCount(String bellcounter);
    }

}
