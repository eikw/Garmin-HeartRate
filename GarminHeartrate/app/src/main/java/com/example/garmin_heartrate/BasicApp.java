package com.example.garmin_heartrate;

import android.app.Application;

import com.example.garmin_heartrate.db.AppRoomDatabase;

public class BasicApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public AppRoomDatabase getDatabase() { return AppRoomDatabase.getDatabase(this); }

    public DataRepository getRepository() { return DataRepository.getInstance(getDatabase()); }
}
