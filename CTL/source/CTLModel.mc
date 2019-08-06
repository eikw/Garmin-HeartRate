using Toybox.Activity;
using Toybox.Sensor;
using Toybox.System;
using Toybox.Attention;
using Toybox.FitContributor;
using Toybox.ActivityRecording;
using Toybox.Timer;
using Toybox.Communications as Comm;

class CTLModel {
	hidden var mTimer;
	hidden var mSession;
	hidden var mSeconds;
	hidden var currentReading;
	hidden var startTime;
	hidden var endTime;
    
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
    	
    	var reading = {
    		"speed" => sensorInfo.speed,
    		"heartRate" => sensorInfo.heartRate,
    		"cadence" => sensorInfo.cadence,
    		"temperature" => sensorInfo.temperature,
    		"altitude" => sensorInfo.altitude,
    		"pressure" => sensorInfo.pressure,
    		"heading" => sensorInfo.heading
     	};
    	currentReading = sensorInfo;
    	// System.println(getTime());
    	// System.println(sensorInfo);
    	// mSensorData.put(getTime().toString(),sensorInfo);
	}
	
	function startMeasure() {
		Sensor.enableSensorEvents( method( :onSensor ) );
		isReadingSensor = true;
		
		startTime = Time.now();
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
		endTime = Time.now();
		
		System.println(mSensorData);
    	var values = mSensorData.values();
    	var keys = mSensorData.keys();
		var data = new [mSensorData.size()];
		for(var i=0; i < mSensorData.size(); i++) {
			data[i] = {
				"heartRate" => values[i].heartRate,
    			"speed" => values[i].speed,
    			"cadence" => values[i].cadence,
    			"temperature" => values[i].temperature,
    			"altitude" => values[i].altitude,
    			"pressure" => values[i].pressure,
    			"heading" => values[i].heading,
    			"timestamp" => keys[i]
			};
		}
		
		var message = {
			"CURRENT_SESSION" => data,
			"START_TIME" => startTime.value(),
			"END_TIME" => endTime.value()
		};
		
		System.println(message);
		Comm.transmit(message, null, new CTLCommListener(method(:onTransmitComplete)));
		
	}
	
	 function onTransmitComplete(status) {
        if (status == CTLCommListener.SUCCESS) {
            System.println("send sucessfull");
        } else {
            System.println("send failure");
        }
    }
	
	function getcurrentSensorData() {
		return mSensorData;
	}
	
	function measure() {
		System.println(currentReading);
		mSensorData.put(Time.now().value(), currentReading);
		mSeconds++;
	}
	
	function getTime() {
		return mSeconds;
	}
	
	function saveSession() {
		mSession.save();
	}
}

//! Handles communication feedback for the RoundView
class CTLCommListener extends Comm.ConnectionListener
{
    static var SUCCESS = 0;
    static var FAILURE = 1;

    hidden var mCallback;

    //! Constructor
    //! @param callback The method to call on a result
    function initialize(callback) {
    	mCallback = callback;
    }

    //! Call the callback with a result of RoundViewCommListener.SUCCESS
    function onComplete() {
        mCallback.invoke(SUCCESS);
    }

    //! Call the callback with a result of RoundViewCommListener.FAILURE
    function onError() {
        mCallback.invoke(FAILURE);
    }
}