package com.example.garmin_heartrate.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.garmin_heartrate.db.entity.Session;

import java.util.List;

@Dao
public interface SessionDao {

    @Query("SELECT * FROM session_table WHERE id = :sessionId")
    LiveData<Session> loadSession(int sessionId);

    @Query("SELECT * FROM session_table WHERE userId = :userId")
    LiveData<List<Session>> loadSessions(int userId);

    @Query("SELECT * FROM session_table WHERE userId = :userId")
    List<Session> loadSessionSync(int userId);

    @Insert
    long insert(Session session);
}
