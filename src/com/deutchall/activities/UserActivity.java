package com.deutchall.activities;

import android.app.Activity;
import android.app.AlertDialog;
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

public class UserActivity extends Activity  implements OnItemClickListener {
	
	public static final String EXTRA_MESSAGE = "com.deutchall.src.USER";
	public static final int ANDROID_4 = 4;
	
	private boolean created = false;
	private ListView listView;
	private TextView selected;
	private String selectedUser = "";
	private int androidVersion;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
        
        this.listView = (ListView)findViewById(R.id.listUsers);
        this.androidVersion = Integer.parseInt(android.os.Build.VERSION.RELEASE.substring(0, 1));
        
        this.fillListView();
        this.listView.setOnItemClickListener(this);
        this.created = true;
    }
	
	@Override
	public void onResume() {
		super.onResume();
		if(created) {
			this.fillListView();
		}
	}
	
	private void fillListView() {
		this.selectedUser = "";
		UserAdapter userAdapter = new UserAdapter(this, PFUser.select(this));
		this.listView.setFastScrollEnabled(true);
		listView.setAdapter(userAdapter);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		int desiredPos;
		if (view.getId() == R.id.txRowUser && parent.getId() == R.id.listUsers) {
			selected = (TextView) view;
			listView = (ListView) parent;
			//selectedUser = selected.getText().toString();
			if (androidVersion >= ANDROID_4) {
				desiredPos = pos;
			} else {
				int lastPos = parent.getLastVisiblePosition();
				desiredPos = lastPos - pos;
			}
			for (int i = 0; i < parent.getChildCount(); i++) {
    			listView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.shadow));
    		}
			//selected.setBackgroundColor(getResources().getColor(R.color.selected));
			((UserAdapter) listView.getAdapter()).getTextView(desiredPos, listView).setBackgroundColor(getResources().getColor(R.color.selected));
			selected.toString();
		}
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
		} else {
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
		
		if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {} 
		else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {} 
    }
	
	private void back() {
		this.terminate();
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
