package com.example.garmin_heartrate.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.garmin_heartrate.db.entity.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Query("DELETE FROM user_table")
    void deleteAll();

    @Query("SELECT * from user_table ORDER BY fullName ASC")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * from user_table where id = :userId")
    LiveData<User> loadUser(int userId);
}
