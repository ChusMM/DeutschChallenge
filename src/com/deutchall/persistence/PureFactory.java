package com.deutchall.persistence;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;

public class PureFactory {
	
	Context context;
	
	public PureFactory(Context context) {
		this.context = context;
	}
	
	
	private void close() {
		DBAgent.getInstance(context).close();
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
	
	public long insertUser(String username, String email) throws SQLException, SQLiteException {
		ContentValues contentValues = new ContentValues();
		contentValues.put(USERNAME, username);
		contentValues.put(EMAIL, email);
		return sqLiteDatabase.insert(USERS, null, contentValues);	
	}
	
	public Cursor selectUsers() throws SQLException, SQLiteException {
		String[] columns = new String[] {KEY_USER, USERNAME, EMAIL};
		Cursor cursor = sqLiteDatabase.query(USERS, columns, null, null, null, null, null);
		return cursor;
	}
	
	public boolean existsUser(String user) throws SQLException, SQLiteException{
		int count;
		String[] columns = new String[] {KEY_USER, USERNAME, EMAIL};
		Cursor cursor = sqLiteDatabase.query(USERS, 
											 columns, 
											 USERNAME + " =  ?", 
											 new String[] {user}, 
											 null, null, null);
		count = cursor.getCount();
		cursor.close();
		return count > 0;
	}
	
	public boolean existsEmail(String email) throws SQLException, SQLiteException{
		
		String[] columns = new String[] {KEY_USER, USERNAME, EMAIL};
	
		Cursor cursor = sqLiteDatabase.query(USERS, 
											 columns, 
											 EMAIL + " =  ?", 
											 new String[] {email}, 
											 null, null, null);
		return cursor.getCount() > 0;
	}
		
	public Cursor queueDerDieDas() throws SQLException, SQLiteException {
		
		String[] columns = new String[] {DDD_KEY, QUESTION, ANS1, ANS2, ANS3, CORRECT};
		
		Cursor cursor = sqLiteDatabase.query(DER_DIE_DAS, columns, null, null, null, null, null);
		return cursor;
	}
	
	public int countDerDieDas() throws SQLException, SQLiteException {
		
		String[] columns = new String[] {DDD_KEY};
		
		Cursor cursor = sqLiteDatabase.query(DER_DIE_DAS, columns, null, null, null, null, null);
		
		return cursor.getCount();
	}
	
	public Cursor queueVerben() throws SQLException, SQLiteException {
		
		String[] columns = new String[] {VERB_KEY, QUESTION, ANS1, ANS2, ANS3, CORRECT};
		
		Cursor cursor = sqLiteDatabase.query(VERBEN, columns, null, null, null, null, null);
		return cursor;
	}
	
	public int countVerben() throws SQLException, SQLiteException {
		
		String[] columns = new String[] {VERB_KEY};
		
		Cursor cursor = sqLiteDatabase.query(VERBEN, columns, null, null, null, null, null);
		
		return cursor.getCount();
	}
	
	public Cursor queueGramatik() throws SQLException, SQLiteException {
		
		String[] columns = new String[] {GRAM_KEY, QUESTION, ANS1, ANS2, ANS3, CORRECT};
		
		Cursor cursor = sqLiteDatabase.query(GRAMATIK, columns, null, null, null, null, null);
		return cursor;
	}
	
	public int countGramatik() throws SQLException, SQLiteException {
		
		String[] columns = new String[] {GRAM_KEY};
		
		Cursor cursor = sqLiteDatabase.query(GRAMATIK, columns, null, null, null, null, null);
		
		return cursor.getCount();
	}
	
	public long insertDDDRanking(String name, String date, int score) throws SQLException, SQLiteException {

		ContentValues contentValues = new ContentValues();
		
		contentValues.put(UID, name + " - " + date);
		contentValues.put(SCORE, score);

		return sqLiteDatabase.insert(DDD_RANK, null, contentValues);
	}
	
	public int deleteDDDRanking() throws SQLiteException {
		
		return sqLiteDatabase.delete(DDD_RANK, null, null);
	}
		
	public Cursor queueDDDRanking() throws SQLException, SQLiteException {
		
		String[] columns = new String[] {KEY_RANK, UID, SCORE};
		
		Cursor cursor = sqLiteDatabase.query(DDD_RANK, 
											 columns, 
										     null, null, null, null, SCORE + " DESC");
		return cursor;
	}
	
	public long insertVerbRanking(String name, String date, int score) throws SQLException, SQLiteException {

		ContentValues contentValues = new ContentValues();
		
		contentValues.put(UID, name + " - " + date);
		contentValues.put(SCORE, score);

		return sqLiteDatabase.insert(VERB_RANK, null, contentValues);
	}
	
	public int deleteVerbRanking() throws SQLiteException {
		
		return sqLiteDatabase.delete(VERB_RANK, null, null);
	}
		
	public Cursor queueVerbRanking() throws SQLException, SQLiteException {
		
		String[] columns = new String[] {KEY_RANK, UID, SCORE};
		
		Cursor cursor = sqLiteDatabase.query(VERB_RANK, 
											 columns, 
										     null, null, null, null, SCORE + " DESC");
		return cursor;
	}

	public long insertGramRanking(String name, String date, int score) throws SQLException, SQLiteException {

		ContentValues contentValues = new ContentValues();
		
		contentValues.put(UID, name + " - " + date);
		contentValues.put(SCORE, score);

		return sqLiteDatabase.insert(GRAM_RANK, null, contentValues);
	}
	
	public int deleteGramRanking() throws SQLiteException {
		
		return sqLiteDatabase.delete(GRAM_RANK, null, null);
	}
		
	public Cursor queueGramRanking() throws SQLException, SQLiteException {
		
		String[] columns = new String[] {KEY_RANK, UID, SCORE};
		
		Cursor cursor = sqLiteDatabase.query(GRAM_RANK, 
											 columns, 
										     null, null, null, null, SCORE + " DESC");
		return cursor;
	}
}
