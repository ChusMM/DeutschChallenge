package com.deutchall.persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAgent extends SQLiteOpenHelper{
	 
	public static final String DATABASE_NAME = "DEUTCHA_DB";
	public static final String DATABASE_PATH = "/data/data/com.deutchall.activities/databases/";
	public static final String DATABASE_LOCATION = DATABASE_PATH + DATABASE_NAME ;
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
	
	private static DBAgent instance = null;
	private static Context context;
	private SQLiteDatabase sqLiteDatabase;
	
	private  DBAgent(Context c) {
		super(c, DATABASE_NAME, null, DATABASE_VERSION);
		try {
			createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized static DBAgent getInstance(Context c) {
		if (instance  != null) {
			instance = new DBAgent(c );
		}
		context = c;
		return instance;
	}
	
	private void createDataBase() throws IOException {
		if (checkDataBase()) {
			this.getReadableDatabase();
			copyDataBase() ;
		}
	}
	
    private boolean checkDataBase() {
        boolean checkDB = false;
        try {
        	
        	String myPath = DATABASE_LOCATION;
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
    		
    		String outPutFileName = DATABASE_LOCATION ;
    		OutputStream output = new FileOutputStream(outPutFileName); 
            
            while ((length = input.read(buffer)) > 0) {
            	output.write(buffer, 0, length);
            	output.flush();
            }
            output.close();
            input.close();
    }
    	
	public SQLiteDatabase openToRead() throws SQLException {		
	    sqLiteDatabase = SQLiteDatabase.openDatabase(DATABASE_LOCATION, null, SQLiteDatabase.OPEN_READONLY);
	    return this.sqLiteDatabase;
	}
	
	public SQLiteDatabase openToWrite() throws SQLException {
		sqLiteDatabase = this.getWritableDatabase();
		return this.sqLiteDatabase;
	}
	
	@Override
	public void close() throws SQLiteException {
		if (sqLiteDatabase != null) {
			 sqLiteDatabase.close();
		}
		super.close();
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
