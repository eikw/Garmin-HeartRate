using Toybox.Activity;
using Toybox.Sensor;
using Toybox.System;
using Toybox.Attention;
using Toybox.FitContributor;
using Toybox.ActivityRecording;
using Toybox.Timer;

class CTLModel {
	hidden var mTimer;
	hidden var mSession;
	hidden var mSeconds;
	hidden var currentReading;
    
	var mSensorData;
	var isReadingSensor;
	var mEnvironmentSensor;
	
	function initialize() {
		Sensor.setEnabledSensors([Sensor.SENSOR_HEARTRATE]);
        // Sensor.enableSensorEvents( method( :onSensor ) );
        mSensorData = {};
        mSeconds = 0;
        // Create a new FIT recording session
        mSession = ActivityRecording.createSession({:sport=>ActivityRecording.SPORT_GENERIC, :name=>"Generic"});        
	}
	
	function onSensor(sensorInfo) {
   		System.println( "Speed: " + sensorInfo.speed );
    	System.println( "cadence: " + sensorInfo.cadence );
    	System.println( "Heart Rate: " + sensorInfo.heartRate );    
    	System.println( "tempature: " + sensorInfo.temperature );
    	System.println( "altitude: " + sensorInfo.altitude );
    	System.println( "pressure: " + sensorInfo.pressure );
    	System.println( "heading: " + sensorInfo.heading );
    
    	System.println( "--------------------" );
    	
    	currentReading = sensorInfo;
    	// System.println(getTime());
    	// System.println(sensorInfo);
    	// mSensorData.put(getTime().toString(),sensorInfo);
	}
	
	function startMeasure() {
		Sensor.enableSensorEvents( method( :onSensor ) );
		isReadingSensor = true;
		
		mTimer = new Timer.Timer();
        // Process the sensors at 4 Hz
        mTimer.start(method(:measure), 1000, true);
        mSession.start();
	}
	
	function stopMeasure() {
		Sensor.enableSensorEvents(null);
		isReadingSensor = false;
		mTimer.stop();
		mSession.stop();
	}
	
	function getcurrentSensorData() {
		return mSensorData;
	}
	
	function measure() {
		mSensorData.put(mSeconds, currentReading);
		mSeconds++;
	}
	
	function getTime() {
		return mSeconds;
	}
	
	function saveSession() {
		mSession.save();
	}
}