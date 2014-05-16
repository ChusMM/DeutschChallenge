package com.deutchall.activities;

import com.deutchall.activities.MainActivity;
import com.deutchall.activities.RankingActivity;
import com.deutchall.activities.UserActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class MainActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void start(View view) {
    	
    	Intent intent = new Intent(this, UserActivity.class);
    	startActivity(intent);
    }
    
    public void showRanking(View view) {
    	
    	Intent intent = new Intent(this, RankingActivity.class);
		startActivity(intent);
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	this.terminate();
	    }
	    return super.onKeyDown(keyCode, event);
	}
    
    public void exit(View view) {
    	this.terminate();
    }
    
	private void terminate() {
		MainActivity.this.finish();          
    }

	@Override
    public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) { } 
		else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { }
    }
}
