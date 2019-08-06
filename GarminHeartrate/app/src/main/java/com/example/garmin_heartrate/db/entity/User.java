package com.example.garmin_heartrate.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "user_table")
public class User implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "firstName")
    private String mFirstName;

    @NonNull
    @ColumnInfo(name = "lastName")
    private String mLastName;

    @NonNull
    @ColumnInfo(name = "fullName")
    private String mFullName;

    @NonNull
    @ColumnInfo(name = "height")
    private double mHeight;

    @NonNull
    @ColumnInfo(name = "weight")
    private double mWeight;

    @NonNull
    @ColumnInfo(name = "age")
    private int mAge;

    public User(int id, @NonNull String firstName, @NonNull String lastName, @NonNull double height, @NonNull double weight, @NonNull int age) {
        this.id = id;
        this.mFirstName = firstName;
        this.mLastName = lastName;
        this.setFullName(this.mFirstName + ' ' + this.mLastName);
        this.mWeight = weight;
        this.mHeight = height;
        this.mAge = age;
    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getFirstName() {
        return mFirstName;
    }

    @NonNull
    public String getLastName() {
        return mLastName;
    }

    public void setFullName(@NonNull String mFullName) {
        this.mFullName = mFullName;
    }

    public String getFullName() {
        return mFullName;
    }

    public double getHeight() {
        return mHeight;
    }

    public double getWeight() {
        return mWeight;
    }

    public int getAge() {
        return mAge;
    }
}
