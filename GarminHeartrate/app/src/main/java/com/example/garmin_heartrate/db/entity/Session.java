package com.example.garmin_heartrate.db.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "session_table",
        foreignKeys = {
            @ForeignKey(entity = User.class,
                    parentColumns = "id",
                    childColumns = "userId",
                    onDelete = ForeignKey.CASCADE)},
        indices = {@Index(value = "userId")
        })
public class Session implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private Date startTime;
    private Date endTime;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public Date getStartTime() { return startTime; }

    public void setStartTime(Date startTime) { this.startTime = startTime; }

    public Date getEndTime() {  return endTime; }

    public void setEndTime(Date endTime) { this.endTime = endTime; }

    public Session(int id, int userId, Date startTime, Date endTime) {
        this.id = id;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
