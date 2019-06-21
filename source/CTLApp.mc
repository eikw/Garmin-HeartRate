using Toybox.Application;
using Toybox.WatchUi;

class CTLApp extends Application.AppBase {

	var model;
	var controller;

    function initialize() {
        AppBase.initialize();
        model = new $.CTLModel();
        controller = new $.CTLController();
    }

    // onStart() is called on application start up
    function onStart(state) {
    }

    // onStop() is called when your application is exiting
    function onStop(state) {
    }

    // Return the initial view of your application here
    function getInitialView() {
        return [ new CTLView(), new CTLDelegate() ];
    }

}
