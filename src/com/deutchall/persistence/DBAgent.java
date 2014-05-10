package com.deutchall.persistence;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

public class DBAgent {
	
	private static DBAgent instance = null;
	private static SQLiteAdapter mySQLiteAdapter = null;
	
	private DBAgent () { }
	
	public synchronized static DBAgent getInstance(Context c) {
		
		if(instance == null) {
			instance = new DBAgent();
		}
		mySQLiteAdapter = new SQLiteAdapter(c);
		
		return instance;
	}
	
	public synchronized void closeAdapter() {
		
		if(mySQLiteAdapter != null) {
			
			mySQLiteAdapter.close();
			mySQLiteAdapter = null;
		}	
	}
	
	public void checkAndCreateDB() throws IOException {
		
		mySQLiteAdapter.createDataBase();
	}
	
	public void insertUser(String username, String email) {
		
		if(mySQLiteAdapter.openToWrite().insertUser(username, email) < 0) {
			throw new SQLiteException();
		}
		this.closeAdapter();
	}
	
	public Cursor getUsersCursor() {
		
		Cursor c = null;
		c = mySQLiteAdapter.openToRead().queueUsers();
		
		return c;
	}
	
	public boolean existsUser(String username) {
		
		boolean exists = false;
		
		exists = mySQLiteAdapter.openToRead().existsUser(username);
		this.closeAdapter();
				
		return exists;
	}
	
	public boolean existsEmail(String email) {
		
		boolean exists = false;
		
		exists = mySQLiteAdapter.openToRead().existsEmail(email);
		this.closeAdapter();
				
		return exists;
	}
	
	public Cursor getDerDieDasCursor() {
		
		Cursor c = null;
		c = mySQLiteAdapter.openToRead().queueDerDieDas();
		
		return c;
	}
	
	public int nRowsDerDieDas() {
		
		int count = mySQLiteAdapter.openToRead().countDerDieDas();
		this.closeAdapter();
					
		return count;
	}

	public Cursor getVerbenCursor() {
		
		Cursor c = null;
		c = mySQLiteAdapter.openToRead().queueVerben();
		
		return c;
	}
	
	public int nRowsVerben() {
		
		int count = mySQLiteAdapter.openToRead().countVerben();
		this.closeAdapter();
					
		return count;
	}
	
	public Cursor getGramatikCursor() {
		
		Cursor c = null;
		c = mySQLiteAdapter.openToRead().queueGramatik();
		
		return c;
	}
	
	public int nRowsGramatik() {
		
		int count = mySQLiteAdapter.openToRead().countGramatik();
		this.closeAdapter();
					
		return count;
	}


	
	public void insertDDDRanking(String name, String date, int score) {
		
		mySQLiteAdapter.openToWrite();
		
		if(mySQLiteAdapter.insertDDDRanking(name, date, score) < 0) {
			throw new SQLiteException();
		}
		
		this.closeAdapter();
	}
	
	public Cursor getRankingDDDCursor() {
		
		Cursor c = null;
		c = mySQLiteAdapter.openToRead().queueDDDRanking();

		return c;
	}
	
	public void clearDDDRanking() {
		
		mySQLiteAdapter.openToWrite().deleteDDDRanking();
		this.closeAdapter();
	}

	public void insertVerbRanking(String name, String date, int score) {
		
		mySQLiteAdapter.openToWrite();
		
		if(mySQLiteAdapter.insertVerbRanking(name, date, score) < 0) {
			throw new SQLiteException();
		}
		
		this.closeAdapter();
	}
	
	public Cursor getRankingVerbCursor() {
		
		Cursor c = null;
		c = mySQLiteAdapter.openToRead().queueVerbRanking();

		return c;
	}
	
	public void clearVerbRanking() {
		
		mySQLiteAdapter.openToWrite().deleteVerbRanking();
		this.closeAdapter();
	}
	
	public void insertGramRanking(String name, String date, int score) {
		
		mySQLiteAdapter.openToWrite();
		
		if(mySQLiteAdapter.insertGramRanking(name, date, score) < 0) {
			throw new SQLiteException();
		}
		
		this.closeAdapter();
	}
	
	public Cursor getRankingGramCursor() {
		
		Cursor c = null;
		c = mySQLiteAdapter.openToRead().queueGramRanking();

		return c;
	}
	
	public void clearGramRanking() {
		
		mySQLiteAdapter.openToWrite().deleteGramRanking();
		this.closeAdapter();
	}
}
