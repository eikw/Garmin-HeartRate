using Toybox.Timer;
using Toybox.Application;
using Toybox.WatchUi;

using Toybox.WatchUi;
using Toybox.System;

class CTLController {
	var mModel;
	
	function initialize() {
		mModel = Application.getApp().model;
	}
	
	function start() {
		mModel.startMeasure();
	}
	
	function startStop() {
		if (isRunning()) {
			mModel.stopMeasure();
		} else {
			mModel.startMeasure();
		}
	}
	
	function isRunning() {
		return mModel.isReadingSensor;
	}
	
	function getCurrentSession() {
		return mModel.mSensorData;
	}	
}