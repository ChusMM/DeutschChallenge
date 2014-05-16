package com.deutchall.persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.deutchall.identification.Question;
import com.deutchall.identification.Ranking;
import com.deutchall.identification.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAgent extends SQLiteOpenHelper {
	 
	public static final String DATABASE_NAME = "DEUTCHA_DB";
	public static final int DATABASE_VERSION  = 1;
	
	public static final String DER_DIE_DAS = "DER_DIE_DAS";
	public static final String VERBEN      = "VERBEN";
	public static final String GRAMATIK    = "GRAMATIK";
	public static final String DDD_RANK    = "DDD_RANK";
	public static final String VERB_RANK   = "VERB_RANK";
	public static final String GRAM_RANK   = "GRAM_RANK";
	public static final String USERS       = "USERS";
	
	public static final String QUESTION_KEY   = "_id";
	public static final String HEADING = "QUESTION";
	public static final String ANS1     = "ANS1";
	public static final String ANS2     = "ANS2";
	public static final String ANS3     = "ANS3";
	public static final String CORRECT  = "CORRECT";
	
	public static final String KEY_USER = "_id";
	public static final String USERNAME = "USERNAME";
	public static final String EMAIL    = "EMAIL";
	
	public static final String KEY_RANK = "_id";
	public static final String RANK_NAME      = "UID";
	public static final String SCORE    = "SCORE";
	
	public static final int DERDIEDAS_ID = 0;
	public static final int VERBEN_ID = 1;
	public static final int GRAMATIK_ID = 2;
	
	private static DBAgent instance = null;
	private static Context context;
	private SQLiteDatabase sqLiteDatabase;
	
	private  DBAgent(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		try {
			createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized static DBAgent getInstance(Context context) {
		//if (instance  != null) {
			DBAgent.context = context;
			instance = new DBAgent(context);
		//}
		return instance;
	}
	
	private void createDataBase() throws IOException {
		if (!checkDataBase()) {
			getReadableDatabase();
			copyDataBase() ;
		}
	}
	
    private boolean checkDataBase() {
        boolean checkDB = false;
        try {	
        	String myPath = context.getDatabasePath(DATABASE_NAME).getPath();
        	File dbFile = new File(myPath);
        	checkDB = dbFile.exists();	
        } catch (SQLiteException e) { 
        	throw new SQLiteException();
        }
        return checkDB;
    }
	
    private void copyDataBase() throws IOException{
    		int length;
    		byte[] buffer = new byte[1024];
    		
    		InputStream input = context.getAssets().open(DATABASE_NAME);
    		
    		String outputFileName = context.getDatabasePath(DATABASE_NAME).getPath();
    		OutputStream output = new FileOutputStream(outputFileName);
    		
            while ((length = input.read(buffer)) > 0) {
            	output.write(buffer, 0, length);
            	output.flush();
            }
            output.close();
            input.close();
    }
    	
	private SQLiteDatabase openToRead() throws SQLException {		
	    sqLiteDatabase = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).getPath(), null, SQLiteDatabase.OPEN_READONLY);
	    return sqLiteDatabase;
	}
	
	private SQLiteDatabase openToWrite() throws SQLException {
		sqLiteDatabase = this.getWritableDatabase();
		return sqLiteDatabase;
	}
	
	@Override
	public void close() throws SQLiteException {
		if (sqLiteDatabase != null) {
			 sqLiteDatabase.close();
		}
		super.close();
	}
	
	public List<User> selectUser(String[] columns, String selection, String[] selectionArgs, 
													  		 String groupBy, String having, String orderBy) {
		Cursor cursor = openToRead().query(USERS, columns, selection, selectionArgs, groupBy, having, orderBy);
		List<User> users = cursorToUserList(cursor);
		close();
		return users;
	}
	
	public List<Question> selectQuestion(String table, String[] columns, String selection, String[] selectionArgs, 
													  							String groupBy, String having, String orderBy) {
		Cursor cursor = openToRead().query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		List<Question> questions = cursorToQuestionList(cursor);
		cursor.close();
		close();
		return questions;
	}
	
	public List<Ranking> selectRanking(String table, String[] columns, String selection, String[] selectionArgs, 
																			String groupBy, String having, String orderBy) {
		Cursor cursor = openToRead().query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		List<Ranking> ranking = cursorToRankingList(cursor);
		close();
		return ranking;
	}
	
	public long insert(String table, ContentValues contentValues) {
		long rowsAffected = openToWrite().insert(table, null, contentValues);
		close();
		return rowsAffected;
	}
	
	public long delete(String table, String whereClause, String[] whereArgs) {
		long rowsAffected = openToWrite().delete(table, whereClause, whereArgs);
		close();
		return rowsAffected;
	}
	
	private List<User> cursorToUserList(Cursor cursor) {
		List<User> users = new ArrayList<User>();
		if (cursor.moveToFirst()) {
			do {
				String name = cursor.getString(cursor.getColumnIndex(USERNAME));
				String email = cursor.getString(cursor.getColumnIndex(EMAIL));
				users.add(new User(name, email));
			} while(cursor.moveToNext());	
		} 
		cursor.close();
		return users;
	}
	
	private List<Question> cursorToQuestionList(Cursor cursor) {
		List<Question> questions = new ArrayList<Question>();
		if (cursor.moveToFirst()) {
			do {
				String heading = cursor.getString(cursor.getColumnIndex(HEADING));
				String ans1 = cursor.getString(cursor.getColumnIndex(ANS1));
				String ans2 = cursor.getString(cursor.getColumnIndex(ANS2));
				String ans3 = cursor.getString(cursor.getColumnIndex(ANS3));
				int correct = cursor.getInt(cursor.getColumnIndex(CORRECT));
				questions.add(new Question(heading, ans1, ans2, ans3, correct));
			} while(cursor.moveToNext());
		}
		cursor.close();
		return questions;
	}
	
	private List<Ranking> cursorToRankingList(Cursor cursor) {
		List<Ranking> ranking = new ArrayList<Ranking>();
		if (cursor.moveToFirst()) {
			do {
				String name = cursor.getString(cursor.getColumnIndex(RANK_NAME));
				int score = cursor.getInt(cursor.getColumnIndex(SCORE));
				ranking.add(new Ranking(name, score));
			} while(cursor.moveToNext());
		}
		cursor.close();
		return ranking;
	}
	
	public void onCreate(SQLiteDatabase db) throws SQLException {
		// TODO Auto-generated method stub
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLException {
		// TODO Auto-generated method stub
		//db.execSQL("DROP TABLE IF EXISTS " + DER_DIE_DAS);
		//db.execSQL("DROP TABLE IF EXISTS " + RANKING);
		//db.execSQL("DROP TABLE IF EXISTS " + USERS);
	}
}
