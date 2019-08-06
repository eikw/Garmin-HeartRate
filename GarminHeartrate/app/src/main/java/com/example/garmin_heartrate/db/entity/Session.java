package com.example.garmin_heartrate.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "session_table")
public class Session implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "userId")
    private int userId;

    @NonNull
    @ColumnInfo(name = "startTime")
    private Date startTime;

    @NonNull
    @ColumnInfo(name = "endTime")
    private Date endTime;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public Date getStartTime() { return startTime; }

    public void setStartTime(Date startTime) { this.startTime = startTime; }

    public Date getEndTime() {  return endTime; }

    public void setEndTime(Date endTime) { this.endTime = endTime; }

    public Session(int id, @NonNull int userId, @NonNull Date startTime, @NonNull Date endTime) {
        this.id = id;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
