package com.deutchall.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.deutchall.persistence.DBAgent;
import com.deutchall.persistence.SQLiteAdapter;
import com.deutchall.activities.R;

public class RankingActivity extends Activity {

	private ListView listView;
	private Cursor cursor;
	private String[] from;
	private int[] to;
	private SimpleCursorAdapter cursorAdapter;	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking);
        
        this.listView = (ListView)findViewById(R.id.contentlist);
        
        this.startCursor();
        this.setFromTo();
        this.fillListView();
    }

	@Override
	public void onResume() {
		
		super.onResume();
		this.cursor.requery();
		this.fillListView();
	}
	
	private void startCursor() {       
		
		this.cursor = DBAgent.getInstance(this).getRankingDDDCursor();
		startManagingCursor(this.cursor);		
	}
	
	private void setFromTo() {
        
		this.from = new String[] {SQLiteAdapter.UID, SQLiteAdapter.SCORE};
        this.to = new int[] {R.id.txUID, R.id.txScore};
	}
	
	private void fillListView() {
		
        this.cursorAdapter = new SimpleCursorAdapter(this, R.layout.rowrank, cursor, from, to);
        this.listView.setAdapter(cursorAdapter);
	}
	
	public void menu(View view) {
		this.back();
	}
	
	public void clear(View view) {
		this.clearConfRequest();
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	this.back();
	    }
	    return super.onKeyDown(keyCode, event);
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
		
		DBAgent.getInstance(this).clearDDDRanking();
		this.cursor.requery();
		this.fillListView();
		this.alertMsg("Ranking cleared");
	}
	
	private void terminate() {
		
		this.cursor.close();
		DBAgent.getInstance(this).closeAdapter();
		RankingActivity.this.finish();
	}
}
