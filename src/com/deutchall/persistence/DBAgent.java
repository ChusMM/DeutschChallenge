package com.deutchall.persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
	
	private static DBAgent instance = null;
	private static Context context;
	private SQLiteDatabase sqLiteDatabase;
	
	private  DBAgent(Context context) throws SQLiteException, IOException, IndexOutOfBoundsException {
		super(context, Sql.DATABASE_NAME, null, Sql.DATABASE_VERSION);
		try {
			createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized static DBAgent getInstance(Context context) throws SQLiteException, IndexOutOfBoundsException, IOException {
		//if (instance  != null) {
		DBAgent.context = context;
		instance = new DBAgent(context);
		//}
		return instance;
	}
	
	private void createDataBase() throws SQLiteException, IOException, IndexOutOfBoundsException {
		if (!checkDataBase()) {
			getReadableDatabase();
			copyDataBase() ;
		}
	}
	
    private boolean checkDataBase() {
        String myPath = context.getDatabasePath(Sql.DATABASE_NAME).getPath();
        return new File(myPath).exists();
    }
	
    private void copyDataBase() throws IOException, IndexOutOfBoundsException {
    		int length;
    		byte[] buffer = new byte[1024];
    		
    		InputStream input = context.getAssets().open(Sql.DATABASE_NAME);
    		
    		String outputFileName = context.getDatabasePath(Sql.DATABASE_NAME).getPath();
    		OutputStream output = new FileOutputStream(outputFileName);
    		
            while ((length = input.read(buffer)) > 0) {
            	output.write(buffer, 0, length);
            	output.flush();
            }
            output.close();
            input.close();
    }
    	
	private SQLiteDatabase openToRead() throws SQLiteException {		
	    sqLiteDatabase = SQLiteDatabase.openDatabase(context.getDatabasePath(Sql.DATABASE_NAME).getPath(), null, SQLiteDatabase.OPEN_READONLY);
	    return sqLiteDatabase;
	}
	
	private SQLiteDatabase openToWrite() throws SQLiteException {
		sqLiteDatabase = this.getWritableDatabase();
		return sqLiteDatabase;
	}
	
	@Override
	public void close() {
		if (sqLiteDatabase != null) {
			 sqLiteDatabase.close();
		}
		super.close();
	}
	
	public List<User> selectUser(String[] columns, String selection, String[] selectionArgs, 
													  		 String groupBy, String having, String orderBy) throws SQLiteException {
		Cursor cursor = openToRead().query(Sql.USERS, columns, selection, selectionArgs, groupBy, having, orderBy);
		List<User> users = CursorFactory.cursorToUserList(cursor);
		close();
		return users;
	}
	
	public List<Question> selectQuestion(String table, String[] columns, String selection, String[] selectionArgs, 
													  							String groupBy, String having, String orderBy) throws SQLiteException{
		Cursor cursor = openToRead().query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		List<Question> questions = CursorFactory.cursorToQuestionList(cursor);
		cursor.close();
		close();
		return questions;
	}
	
	public List<Ranking> selectRanking(String table, String[] columns, String selection, String[] selectionArgs, 
																			String groupBy, String having, String orderBy)  throws SQLiteException {
		Cursor cursor = openToRead().query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		List<Ranking> ranking = CursorFactory.cursorToRankingList(cursor);
		close();
		return ranking;
	}
	
	public long insert(String table, ContentValues contentValues)  throws SQLiteException {
		long rowsAffected = openToWrite().insert(table, null, contentValues);
		close();
		return rowsAffected;
	}
	
	public long delete(String table, String whereClause, String[] whereArgs) throws SQLiteException {
		long rowsAffected = openToWrite().delete(table, whereClause, whereArgs);
		close();
		return rowsAffected;
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
