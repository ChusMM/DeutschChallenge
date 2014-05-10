package com.deutchall.activities;

import java.io.IOException;

import com.deutchall.activities.MainActivity;
import com.deutchall.activities.RankingActivity;
import com.deutchall.activities.UserActivity;
import com.deutchall.persistence.DBAgent;
import com.deutchall.activities.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
        
        try {
			DBAgent.getInstance(this).checkAndCreateDB();
		} catch (IOException e) {
			throw new Error("Error: copy database from file");
		}
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
		
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	
		builder.setMessage("¿Are you sure you want to leave?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		    	   
		    	   public void onClick(DialogInterface dialog, int id) {
		        	  MainActivity.this.finish();
		           }
		       })
		       
		       .setNegativeButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		
		AlertDialog alert = builder.create();
		alert.show();
    }

	@Override
    public void onConfigurationChanged(Configuration newConfig) {
		
		super.onConfigurationChanged(newConfig);
		
		if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
         //TODO
		} 
		else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
         //TODO
		}
    }
}
