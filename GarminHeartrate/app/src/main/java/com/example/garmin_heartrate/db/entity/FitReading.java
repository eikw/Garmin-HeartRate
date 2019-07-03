package com.example.garmin_heartrate.db.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.garmin_heartrate.db.entity.Session;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "reading_table",
        foreignKeys = {
                @ForeignKey(entity = Session.class,
                        parentColumns = "id",
                        childColumns = "sessionId",
                        onDelete = ForeignKey.CASCADE)},
        indices = {@Index(value = "sessionId")
        })
public class FitReading implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int sessionId;
    private Date timestamp;
    private double speed;
    private double cadence;
    private double heartRate;
    private double temperature;
    private double altitude;
    private double pressure;
    private double heading;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getCadence() {
        return cadence;
    }

    public void setCadence(double cadence) {
        this.cadence = cadence;
    }

    public double getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(double heartRate) {
        this.heartRate = heartRate;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public FitReading(int id, int sessionId, Date timestamp, double speed, double cadence, double heartRate, double temperature, double altitude, double pressure, double heading) {
        this.id = id;
        this.sessionId = sessionId;
        this.timestamp = timestamp;
        this.speed = speed;
        this.cadence = cadence;
        this.heartRate = heartRate;
        this.temperature = temperature;
        this.altitude = altitude;
        this.pressure = pressure;
        this.heading = heading;
    }
}
