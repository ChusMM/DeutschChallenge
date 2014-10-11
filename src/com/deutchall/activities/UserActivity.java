package com.deutchall.activities;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.deutchall.identification.ErrorLauncher;
import com.deutchall.identification.PFUser;
import com.deutchall.adapters.UserAdapter;

public class UserActivity extends Activity implements OnItemClickListener {
	
	public static final String USER = "user";
	public static final String TAG = "com.deutchall.activities.UserActivity";
	
	private boolean created = false;
	private ListView listView;
	private UserAdapter userAdapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
        this.listView = (ListView) findViewById(R.id.listUsers);
        this.fillListView();
        this.created = true;
    }
	
	@Override
	public void onResume() {
		super.onResume();
		if(created) {
			this.fillListView();
		}
	}
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {} 
		else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {} 
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	this.terminate();
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	private void fillListView() {
		try {
			userAdapter = new UserAdapter(this, PFUser.select(this));
		} catch (SQLiteException e) {
			Log.e(TAG, e.toString());
			ErrorLauncher.throwError(e.toString());
		} catch (IndexOutOfBoundsException e) {
			Log.e(TAG, e.toString());
			ErrorLauncher.throwError(e.toString());
		} catch (IOException e) {
			Log.e(TAG, e.toString());
			ErrorLauncher.throwError(e.toString());
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			ErrorLauncher.throwError(e.toString());
		}
		userAdapter.setSelected(null);
		listView.setFastScrollEnabled(true);
		listView.setAdapter(userAdapter);
		listView.setOnItemClickListener(this);
	}
	
	@Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		if (parent.getId() == R.id.listUsers) {
    		userAdapter.setSelected(((TextView) view).getText().toString());
		}
    }
	
	public void menu(View view) {
		this.terminate();
	}
	
	public void newProfile(View view) {
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
		super.onPause();
	}
	
	public void next(View view) {
		if (userAdapter.getSelected() != null) {
			Intent intent = new Intent(this, SelectGameActivity.class);
			intent.putExtra(USER, userAdapter.getSelected());
	    	startActivity(intent);
	    	this.terminate();
		} else {
			this.toastMsg("Please, select an existing profile from the list");
		}
	}
	
	private void toastMsg(String msg) {
		Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
	}
	
	private void terminate() {
		UserActivity.this.finish();
	}
}
