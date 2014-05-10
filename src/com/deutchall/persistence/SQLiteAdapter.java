package com.deutchall.persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteAdapter extends SQLiteOpenHelper{
	 
	public static final String DATABASE_NAME = "DEUTCHA_DB";
	public static final String DATABASE_PATH = "/data/data/com.deutchall.activities/databases/";
	public static final int DATABASE_VERSION = 1;
	
	public static final String DER_DIE_DAS = "DER_DIE_DAS";
	public static final String VERBEN      = "VERBEN";
	public static final String GRAMATIK    = "GRAMATIK";
	public static final String DDD_RANK    = "DDD_RANK";
	public static final String VERB_RANK   = "VERB_RANK";
	public static final String GRAM_RANK   = "GRAM_RANK";
	public static final String USERS       = "USERS";
	
	public static final String DDD_KEY   = "_id";
	public static final String VERB_KEY  = "_id";
	public static final String GRAM_KEY  = "_id";
	
	public static final String QUESTION = "QUESTION";
	public static final String ANS1     = "ANS1";
	public static final String ANS2     = "ANS2";
	public static final String ANS3     = "ANS3";
	public static final String CORRECT  = "CORRECT";
	
	public static final String KEY_USER = "_id";
	public static final String USERNAME = "USERNAME";
	public static final String EMAIL    = "EMAIL";
	
	public static final String KEY_RANK = "_id";
	public static final String UID      = "UID";
	public static final String SCORE    = "SCORE";
	
	private SQLiteDatabase sqLiteDatabase;
	private Context context;
	
	public SQLiteAdapter(Context c) {
		
		super(c, DATABASE_NAME, null, DATABASE_VERSION);
		context = c;
	}
	
	public void createDataBase() throws IOException {
		
		boolean dbExists = checkDataBase();
		
		if(!dbExists) {
			this.getReadableDatabase();
			
			try {
				copyDataBase();
				
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}
	
    private boolean checkDataBase() {
    	
        boolean checkDB = false;
        
        try {
        	
        	String myPath = DATABASE_PATH + DATABASE_NAME;
        	File dbFile = new File(myPath);
        	checkDB = dbFile.exists();	
        } 
        catch (SQLiteException e) { 
        	throw new SQLiteException();
        }
        
        return checkDB;
    }
	
    private void copyDataBase() throws IOException{
    	
    		int length;
    		byte[] buffer = new byte[1024];
    		
    		InputStream input = context.getAssets().open(DATABASE_NAME);
    		
    		String outPutFileName = DATABASE_PATH + DATABASE_NAME ;
            
    		OutputStream output = new FileOutputStream(outPutFileName); 
            
            while ((length = input.read(buffer)) > 0) {
            	output.write(buffer, 0, length);
            }
            
            output.flush();
            output.close();
            input.close();
    }
    	
	public SQLiteAdapter openToRead() throws SQLException {
		
		boolean mDataBaseExist = checkDataBase();
	    
		if(!mDataBaseExist) {
			
	        this.getReadableDatabase();
	    }
		
	    String myPath = DATABASE_PATH  +  DATABASE_NAME;
	    sqLiteDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	    
	    return this;
	}
	
	public SQLiteAdapter openToWrite() throws SQLException {
		
		sqLiteDatabase = this.getWritableDatabase();
		
		return this;
	}
	
	@Override
	public synchronized void close() throws SQLiteException {
		 
		if(sqLiteDatabase != null) {
			 sqLiteDatabase.close();
		}
 	    
		super.close();
	}
	
	public long insertUser(String username, String email) throws SQLException, SQLiteException {
		
		ContentValues contentValues = new ContentValues();
		contentValues.put(USERNAME, username);
		contentValues.put(EMAIL, email);
		
		return sqLiteDatabase.insert(USERS, null, contentValues);	
	}
	
	public Cursor queueUsers() throws SQLException, SQLiteException {
		
		String[] columns = new String[] {KEY_USER, USERNAME, EMAIL};
		
		Cursor cursor = sqLiteDatabase.query(USERS, columns, null, null, null, null, null);
		return cursor;
	}
	
	public boolean existsUser(String user) throws SQLException, SQLiteException{
		
		String[] columns = new String[] {KEY_USER, USERNAME, EMAIL};
		
		Cursor cursor = sqLiteDatabase.query(USERS, 
											 columns, 
											 USERNAME + " =  ?", 
											 new String[] {user}, 
											 null, null, null);
		return cursor.getCount() > 0;
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
