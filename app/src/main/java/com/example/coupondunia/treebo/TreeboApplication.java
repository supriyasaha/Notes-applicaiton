package com.example.coupondunia.treebo;


import android.app.Application;
import android.content.Context;

public class TreeboApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
    }

    public static Context getAppCOntext() {
        return context;
    }
}
