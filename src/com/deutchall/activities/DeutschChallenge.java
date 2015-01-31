package com.deutchall.activities;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class DeutschChallenge extends Application {
private static final String TAG = "com.deutchall.DeutschChallenge";
	
	private static Context context = null;
	
	@Override
    public void onCreate(){
        super.onCreate();
        DeutschChallenge.context = getApplicationContext();
    }

    public static Context getAppContext() {
    	if (context != null) {
    		return DeutschChallenge.context;
    	} else {
    		Log.e(TAG, "DeutschChallenge was not successfully created");
    		throw new Error("DeutschChallenge was not successfully created");
    	}
    }
}
