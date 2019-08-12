using Toybox.WatchUi as Ui;
using Toybox.Application;
using Toybox.Timer;
using Toybox.Lang;

class CTLView extends Ui.View {
	hidden var mModel;
    hidden var mController;
    hidden var mLabel;
    hidden var mTimer;
    hidden var mPrompt;
    hidden var mCounter;
    
    function initialize() {
        View.initialize();
        
        mModel = Application.getApp().model;
        mController = Application.getApp().controller;
        mPrompt = Ui.loadResource(Rez.Strings.prompt);
        mLabel = null;
        mTimer = new Timer.Timer();
        mCounter = 0;
    }

    // Load your resources here
    function onLayout(dc) {
        setLayout(Rez.Layouts.MainLayout(dc));
        mLabel = View.findDrawableById("MainLabel");
    }

    // Called when this View is brought to the foreground. Restore
    // the state of this View and prepare it to be shown. This includes
    // loading resources into memory.
    function onShow() {
    	mTimer.start(method(:onTimer), 1000, true);
    }

    // Update the view
    function onUpdate(dc) {
    	if(mController.isRunning()) {
    		/*
    		var measure = mController.getCurrentSession();
    		var keys = measure.keys();
    		var values = measure.values();
    		
    		for( var i = 0; i < measure.size(); i++ ) {
    			System.println("key "+ i + " : " + keys[i].toString());
    			System.println("Value "+ i + " : " + values[i].heartRate);
    			text = i.toString() + " - " + values[i].heartRate.toString();
			}
			*/
			var text = "Measuring.";
			for( var i = 0; i < mCounter; i++ ) {
				text = text + '.';
			}
			mLabel.setText( text );
			if(mCounter == 2) {
				mCounter = 0;
			} else {
				mCounter++;
			}
    	} else {
    		mLabel.setText( mPrompt );
    	}
    
        // Call the parent onUpdate function to redraw the layout
        View.onUpdate(dc);
    }

    // Called when this View is removed from the screen. Save the
    // state of this View here. This includes freeing resources from
    // memory.
    function onHide() {
    	mTimer.stop();
    }
    
    function onTimer() {
        Ui.requestUpdate();
    }

}
