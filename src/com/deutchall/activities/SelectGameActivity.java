package com.deutchall.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.deutchall.persistence.Sql;

public class SelectGameActivity extends Activity implements OnClickListener {
	private static final String TAG = "SelectGameActivity";
	
	private String name = null;
	private int gameSelected;	
	public final static String GAME = "game";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectgame);
		if((this.name = getIntent().getStringExtra(UserActivity.USER)) == null) {
			throw new Error(TAG + ": user name not received from intent");
		}
		((Button) findViewById(R.id.btDdd)).setOnClickListener(this);
		((Button) findViewById(R.id.btVerb)).setOnClickListener(this);
		((Button) findViewById(R.id.btGram)).setOnClickListener(this);
		setButtonsDefaultStyle();
	}
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);	
		if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) { } 
		else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { }
    }
	
	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch(id) {
		case R.id.btDdd:
			gameSelected = Sql.DERDIEDAS_ID;
			break;
		case R.id.btVerb:
			gameSelected = Sql.VERBEN_ID;
			break;
		case R.id.btGram:
			gameSelected = Sql.GRAMATIK_ID;
			break;
		default:
			return;
		}
		setButtonsDefaultStyle();
		((Button) findViewById(id)).setBackgroundResource(R.drawable.button_gamesel_selected);
		((Button) findViewById(id)).setTextColor(getResources().getColor(R.color.white));
	}
	
	private void setButtonsDefaultStyle() {
		((Button) findViewById(R.id.btDdd)).setBackgroundResource(R.drawable.button_gamesel);
		((Button) findViewById(R.id.btVerb)).setBackgroundResource(R.drawable.button_gamesel);
		((Button) findViewById(R.id.btGram)).setBackgroundResource(R.drawable.button_gamesel);
		
		((Button) findViewById(R.id.btDdd)).setTextColor(getResources().getColor(R.color.black));
		((Button) findViewById(R.id.btVerb)).setTextColor(getResources().getColor(R.color.black));
		((Button) findViewById(R.id.btGram)).setTextColor(getResources().getColor(R.color.black));
	}
		
	public void play(View view) {
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra(UserActivity.USER, name);
		intent.putExtra(GAME, gameSelected);
    	startActivity(intent);
    	SelectGameActivity.this.finish();
	}
	
	public void backUserSelection(View view) {
		this.toUserScreen();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	this.toUserScreen();
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	private void toUserScreen() {
		Intent intent = new Intent(this, UserActivity.class);
    	startActivity(intent);
		SelectGameActivity.this.finish();
	}
}
