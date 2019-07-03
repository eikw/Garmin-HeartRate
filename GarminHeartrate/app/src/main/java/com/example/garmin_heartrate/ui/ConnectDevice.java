package com.example.garmin_heartrate.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.garmin_heartrate.R;

public class ConnectDevice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_device_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ConnectDeviceFragment.newInstance())
                    .commitNow();
        }
    }
}
