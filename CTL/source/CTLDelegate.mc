using Toybox.WatchUi;

class CTLDelegate extends WatchUi.BehaviorDelegate {

	var mController;
	
    function initialize() {
        BehaviorDelegate.initialize();
    	mController = Application.getApp().controller;    
    }

    function onMenu() {
        // WatchUi.pushView(new Rez.Menus.MainMenu(), new CTLMenuDelegate(), WatchUi.SLIDE_UP);
        return true;
    }
    
    function onSelect() {
    	mController.startStop();
    	return true;
    }

}