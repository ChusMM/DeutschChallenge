package com.deutchall.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.deutchall.identification.PFUser;
import com.deutchall.activities.R;
import com.deutchall.adapters.UserAdapter;

public class UserActivity extends Activity {
	
	public final static String EXTRA_MESSAGE = "com.deutchall.src.USER";
	
	private ListView listView;
	private String selectedUser = "";
	private int androidVersion;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
        
        this.listView = (ListView)findViewById(R.id.listUsers);
        this.androidVersion = Integer.parseInt(android.os.Build.VERSION.RELEASE.substring(0, 1));
        
        this.fillListView();
        this.setListListener();
    }
	
	@Override
	public void onResume() {
		super.onResume();
		this.selectedUser = "";
		this.fillListView();
	}
	
	private void fillListView() {
		UserAdapter userAdapter = new UserAdapter(this, PFUser.select(this));
		this.listView.setFastScrollEnabled(true);
		listView.setAdapter(userAdapter);
	}
	
	private void setListListener() {
		
		this.listView.setOnItemClickListener(new OnItemClickListener () {
			@Override
	        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				
				int desiredPos;
				
				if (androidVersion >= 4) {
					desiredPos = pos;
				} else {
					int lastPos = parent.getLastVisiblePosition();
					desiredPos = lastPos - pos;
				}
	        	for (int i = 0; i < parent.getChildCount(); i++) {
	        		parent.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.shadow));
	        	}
	        	
	        	parent.getChildAt(desiredPos).setBackgroundColor(getResources().getColor(R.color.selected));
	        	TextView usr = (TextView) parent.getItemAtPosition(pos);
	        	selectedUser = usr.getText().toString();
	        }
		});
	}
	
	public void menu(View view) {
		this.back();
	}
	
	public void newProfile(View view) {
		
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
		super.onPause();
	}
	
	public void next(View view) {
		
		if(this.selectedUser.length() > 0) {
			
			Intent intent = new Intent(this, SelectGameActivity.class);
			intent.putExtra(EXTRA_MESSAGE, this.selectedUser);
	    	startActivity(intent);
	    	
	    	this.terminate();
		}
		else {
			this.alertMsg("Please, select an existing profile from the list");
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	this.back();
	    }
	    return super.onKeyDown(keyCode, event);
	}

	@Override
    public void onConfigurationChanged(Configuration newConfig) {
		
		super.onConfigurationChanged(newConfig);
		
		if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) { } 
		else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { }
    }
	
	private void back() {
    	
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setMessage("Back to main menu?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		    	   
		    	   public void onClick(DialogInterface dialog, int id) {
		    		   terminate();
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
	
	private void alertMsg(String msg) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Attention");
        builder.setMessage(msg);
        builder.setPositiveButton("OK", null);
        builder.create();
        builder.show();
	}
	
	private void terminate() {
		UserActivity.this.finish();
	}
}
