package com.example.metime;

import android.app.Application;

import com.example.metime.Utilities.DataBaseManager;
import com.example.metime.Utilities.TimeManager;


public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DataBaseManager.init(this);
        TimeManager.init(this);
    }
}
