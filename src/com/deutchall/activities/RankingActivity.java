package com.deutchall.activities;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;

import com.deutchall.exceptions.InvalidGameIdException;
import com.deutchall.identification.ErrorLauncher;
import com.deutchall.identification.PFRanking;
import com.deutchall.persistence.Sql;
import com.deutchall.activities.R;
import com.deutchall.adapters.RankingAdapter;

public class RankingActivity extends Activity implements OnClickListener {

	private static final String TAG = "com.deutchall.activities.RankingActivity";
	private ListView listView;
	private int gameId;
	private boolean created  = false;
	Timer animTimer = new Timer();
	private final Handler timeHandler = new Handler();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking);
        if ((this.gameId = getIntent().getIntExtra(SelectGameActivity.GAME, -1)) == -1) {
        	this.gameId = Sql.DERDIEDAS_ID;
        }
        this.listView = (ListView)findViewById(R.id.contentlist);
        ((Button) findViewById(R.id.bt_ddd)).setOnClickListener(this);
        ((Button) findViewById(R.id.bt_verb)).setOnClickListener(this);
        ((Button) findViewById(R.id.bt_gram)).setOnClickListener(this);
        
        selectRanking(this.gameId);
        fillListView();
        this.created = true;
    }

	@Override
	public void onResume() {
		super.onResume();
		if(created) {
			selectRanking(this.gameId);
			fillListView();
		}
	}
	
	private void fillListView() {
        RankingAdapter rankingAdapter;
		try {
			rankingAdapter = new RankingAdapter(this, PFRanking.getRanking(this, gameId));
			listView.setFastScrollEnabled(true);
			listView.setAdapter(rankingAdapter);
			showList(listView);
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
		int id = view.getId();
		switch(id) {
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
			ErrorLauncher.throwError("onClick handler coherenceless exception");
		}
		selectRanking(gameId);
		ShowAnimationTask sat = new ShowAnimationTask();
		listView.setVisibility(View.GONE);
		animTimer.schedule(sat, 0);
	}

	private void selectRanking(int gameId) {
		int id;
		switch(gameId) {
		case Sql.DERDIEDAS_ID:
			id = R.id.bt_ddd;
			break;
		case Sql.VERBEN_ID:
			id = R.id.bt_verb;
			break;
		case Sql.GRAMATIK_ID:
			id = R.id.bt_gram;
			break;
		default:
			ErrorLauncher.throwError("Game id not handled");
			return;
		}
		setButtonsDefaultStyle();
		((Button) findViewById(id)).setBackgroundResource(R.drawable.button_gamesel_selected);
		((Button) findViewById(id)).setTextColor(getResources().getColor(R.color.white));
	}
	
	private void setButtonsDefaultStyle() {
		((Button) findViewById(R.id.bt_ddd)).setBackgroundResource(R.drawable.button_gamesel);
		((Button) findViewById(R.id.bt_verb)).setBackgroundResource(R.drawable.button_gamesel);
		((Button) findViewById(R.id.bt_gram)).setBackgroundResource(R.drawable.button_gamesel);
	
		((Button) findViewById(R.id.bt_ddd)).setTextColor(getResources().getColor(R.color.black));
		((Button) findViewById(R.id.bt_verb)).setTextColor(getResources().getColor(R.color.black));
		((Button) findViewById(R.id.bt_gram)).setTextColor(getResources().getColor(R.color.black));
	}
	
	/* private class HideAnimationTask extends TimerTask {
		@Override
		public void run() {
			hide();
		}
	}
	
	private void hide() {
		HideRunnable hr = new HideRunnable();
		timeHandler.post(hr);
	}
	
	private class  HideRunnable implements Runnable {
		public void run() {
			hideList(listView);
		}
	}; */
	
	private class ShowAnimationTask extends TimerTask {
		@Override
		public void run() {
			ShowRunnable sr = new ShowRunnable();
			timeHandler.post(sr);
		}
	}
	
	private class  ShowRunnable implements Runnable {
		public void run() {
			listView.setVisibility(View.GONE);
			fillListView();
			showList(listView);
		}
	};
	
	/*private void hideList(View animationTarget) {
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.rank_list_hide);
		animationTarget.startAnimation(animation);
	}*/
	
	private void showList(View animationTarget) {
		animationTarget.setVisibility(View.VISIBLE);
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.rank_list_show);
		animationTarget.startAnimation(animation);
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
			ErrorLauncher.throwError(e.toString());
		} catch (IndexOutOfBoundsException e) {
			Log.e(TAG, e.toString());
			ErrorLauncher.throwError(e.toString());
		} catch (IOException e) {
			Log.e(TAG, e.toString());
			ErrorLauncher.throwError(e.toString());
		} catch (InvalidGameIdException e) {
			Log.e(TAG,e.toString());
			ErrorLauncher.throwError(e.toString());
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			ErrorLauncher.throwError(e.toString());
		}
	}
	
	private void terminate() {
		RankingActivity.this.finish();
	}
}
