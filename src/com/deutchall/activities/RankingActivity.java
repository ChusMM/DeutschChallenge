package com.deutchall.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.deutchall.identification.PFRanking;
import com.deutchall.persistence.DBAgent;
import com.deutchall.activities.R;
import com.deutchall.adapters.RankingAdapter;

public class RankingActivity extends Activity {

	private ListView listView;
	private boolean created  = false;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking);
        this.listView = (ListView)findViewById(R.id.contentlist);
        this.created = true;
        fillListView();
    }

	@Override
	public void onResume() {
		super.onResume();
		if(created) {
			fillListView();
		}
	}
	
	private void fillListView() {
        RankingAdapter rankingAdapter = new RankingAdapter(this, PFRanking.getRanking(this, DBAgent.DERDIEDAS_ID));
		listView.setFastScrollEnabled(true);
		listView.setAdapter(rankingAdapter);
	}
	
	public void menu(View view) {
		terminate();
	}
	
	public void clear(View view) {
		clearConfRequest();
	}
	 
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) { } 
		else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { }
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	terminate();;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	private void clearConfRequest() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you really want to clear all ranking registers?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		    	   
		    	   public void onClick(DialogInterface dialog, int id) {
		    		   onClear();
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

	private void onClear() {
		fillListView();
		PFRanking.deleteRankingGame(this, DBAgent.DERDIEDAS_ID);
		alertMsg("Ranking cleared");
	}
	
	private void terminate() {
		RankingActivity.this.finish();
	}
}
