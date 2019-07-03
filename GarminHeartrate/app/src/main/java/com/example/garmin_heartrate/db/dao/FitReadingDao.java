package com.example.garmin_heartrate.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.garmin_heartrate.db.entity.FitReading;

import java.util.List;

@Dao
public interface FitReadingDao {

    @Insert
    void insert(FitReading reading);

    @Query("SELECT * FROM reading_table WHERE sessionId = :sessionId")
    LiveData<List<FitReading>> loadReadings(int sessionId);

    @Query("SELECT * FROM reading_table WHERE sessionId = :sessionId")
    List<FitReading> loadReadingsSync(int sessionId);
}
