package com.bluebuddy.app;

import android.app.Application;
import android.content.Context;

import com.bluebuddy.data.RegPrefHelper;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class AppController extends Application {
    // LogCat tag
    public static final String TAG = AppController.class.getSimpleName();
    //*private RequestQueue mRequestQueue;*//*
    private static AppController mInstance;
    private static boolean activityVisible;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        RegPrefHelper.instance(this.getApplicationContext());

        mInstance = this;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}
