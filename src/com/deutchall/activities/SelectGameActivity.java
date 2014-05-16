package com.deutchall.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.deutchall.persistence.DBAgent;

public class SelectGameActivity extends Activity {

	private String name = null;
	private int gameSelected;
	
	private Button btDDD;
	private Button btVerb;
	private Button btGram;
	
	public final static String EXTRA_MESSAGE = "com.deutchall.src.SELECT";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectgame);
		this.getNameFromIntent();
		
		this.btDDD = (Button)findViewById(R.id.btDDD);
		this.btVerb = (Button)findViewById(R.id.btVerb);
		this.btGram = (Button)findViewById(R.id.btGram);
		
		this.setButtonsDefaultStyle();
	}
	
	private void getNameFromIntent() {
		Intent intent = getIntent();
		if((this.name = intent.getStringExtra(UserActivity.EXTRA_MESSAGE)) == null) {
			throw new Error("SelectGameActivity: user name null pointer exception");
		}
	}
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);	
		
		if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) { } 
		else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { }
    }
	
	public void derdiedas(View view) {
		this.gameSelected = DBAgent.DERDIEDAS_ID;
		this.setButtonsDefaultStyle();
		this.btDDD.setBackgroundResource(R.drawable.button_gamesel_selected);
		this.btDDD.setTextColor(getResources().getColor(R.color.white));
	}
	
	public void verben(View view) {
		this.gameSelected = DBAgent.VERBEN_ID;
		this.setButtonsDefaultStyle();
		this.btVerb.setBackgroundResource(R.drawable.button_gamesel_selected);
		this.btVerb.setTextColor(getResources().getColor(R.color.white));
	}
	
	public void gramatik(View view) {
		this.gameSelected = DBAgent.GRAMATIK_ID;
		this.setButtonsDefaultStyle();
		this.btGram.setBackgroundResource(R.drawable.button_gamesel_selected);
		this.btGram.setTextColor(getResources().getColor(R.color.white));
	}
	
	private void setButtonsDefaultStyle() {
		this.btDDD.setBackgroundResource(R.drawable.button_gamesel);
		this.btVerb.setBackgroundResource(R.drawable.button_gamesel);
		this.btGram.setBackgroundResource(R.drawable.button_gamesel);
		
		this.btDDD.setTextColor(getResources().getColor(R.color.black));
		this.btVerb.setTextColor(getResources().getColor(R.color.black));
		this.btGram.setTextColor(getResources().getColor(R.color.black));
	}
		
	public void play(View view) {
		String[] args = new String[2];
		args[0] = this.name;
		args[1] = String.valueOf(this.gameSelected);
		
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra(EXTRA_MESSAGE, args);
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
