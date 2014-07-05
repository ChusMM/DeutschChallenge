package com.deutchall.activities;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.deutchall.identification.PFRanking;
import com.deutchall.persistence.Sql;
import com.deutchall.activities.R;
import com.deutchall.adapters.RankingAdapter;

public class RankingActivity extends Activity implements OnClickListener {

	private static final String TAG = "com.deutchall.activities.RankingActivity";
	private ListView listView;
	private int gameId;
	private boolean created  = false;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking);
        if ((this.gameId = getIntent().getIntExtra(SelectGameActivity.GAME, -1)) == -1) {
        	this.gameId = Sql.DERDIEDAS_ID;
        }
        this.listView = (ListView)findViewById(R.id.contentlist);
        this.created = true;
        ((Button) findViewById(R.id.bt_ddd)).setOnClickListener(this);
        ((Button) findViewById(R.id.bt_verb)).setOnClickListener(this);
        ((Button) findViewById(R.id.bt_gram)).setOnClickListener(this);
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
        RankingAdapter rankingAdapter;
		try {
			rankingAdapter = new RankingAdapter(this, PFRanking.getRanking(this, gameId));
			listView.setFastScrollEnabled(true);
			listView.setAdapter(rankingAdapter);
		} catch (SQLiteException e) {
			Log.e(TAG, e.toString());
		} catch (IndexOutOfBoundsException e) {
			Log.e(TAG, e.toString());
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		}
	}
	
	public void menu(View view) {
		terminate();
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
	    	terminate();
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.bt_ddd:
			gameId = Sql.DERDIEDAS_ID;
			break;
		case R.id.bt_verb:
			gameId = Sql.VERBEN_ID;
			break;
		case R.id.bt_gram:
			gameId = Sql.GRAMATIK_ID;
			break;
		default:
			return;
		}
		fillListView();
	}
	
	public void clear(View view)  {
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
		try {
			PFRanking.deleteRankingGame(this, gameId);
			alertMsg("Ranking cleared");
			fillListView();
		} catch (SQLiteException e) {
			Log.e(TAG, e.toString());
		} catch (IndexOutOfBoundsException e) {
			Log.e(TAG, e.toString());
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		}
	}
	
	private void terminate() {
		RankingActivity.this.finish();
	}
}
