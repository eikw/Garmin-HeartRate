class CTLModel {
	hidden var mTimer;
	hidden var mSession;
	hidden var mSamples;
	hidden var mSampleCounter;
    hidden var mZones;
	
	var mEnvironmentSensor;
	
	function initialize() {
		Sensor.setEnabledSensors([Sensor.SENSOR_HEARTRATE]);
        Sensor.enableSensorEvents( method( :onSensor ) );
        
        // Create a new FIT recording session
        mSession = ActivityRecording.createSession({:name=>"Generic", :sport=>ActivityRecording.SPORT_GENERIC});        
	}
	
	function onSensor(sensorInfo) {
   		System.println( "Speed: " + sensorInfo.speed );
    	System.println( "candence: " + sensorInfo.cadence );
    	System.println( "Heart Rate: " + sensorInfo.heartRate );    
    	System.println( "temp: " + sensorInfo.temperature );
    	System.println( "altitude: " + sensorInfo.altitude );
    	System.println( "pressure: " + sensorInfo.pressure );
    	System.println( "heading: " + sensorInfo.heading );
    
    	System.println( "--------------------" );
	}
}