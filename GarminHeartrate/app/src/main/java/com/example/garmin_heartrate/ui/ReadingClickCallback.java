package com.example.garmin_heartrate.ui;

import com.example.garmin_heartrate.db.entity.FitReading;

public interface ReadingClickCallback {

        void onClick(FitReading reading);
}
