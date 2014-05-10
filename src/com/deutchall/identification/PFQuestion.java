package com.deutchall.identification;

import java.util.ArrayList;

import com.deutchall.persistence.DBAgent;
import com.deutchall.persistence.SQLiteAdapter;

import android.content.Context;
import android.database.Cursor;

public class PFQuestion {
	
	public static ArrayList<Question> retrieveQuestions(Context c, int idGame) {
		
		ArrayList<Question> questions = new ArrayList<Question>();
		Cursor cursor = getGameCursor(c, idGame);
				
		if(cursor.moveToFirst()) {
			
			do {
				
				String question = cursor.getString(cursor.getColumnIndex(SQLiteAdapter.QUESTION));
				String ans1 = cursor.getString(cursor.getColumnIndex(SQLiteAdapter.ANS1));
				String ans2 = cursor.getString(cursor.getColumnIndex(SQLiteAdapter.ANS2));
				String ans3 = cursor.getString(cursor.getColumnIndex(SQLiteAdapter.ANS3));
				int correct = cursor.getInt(cursor.getColumnIndex(SQLiteAdapter.CORRECT));
				
				questions.add(new Question(question, ans1, ans2, ans3, correct));
						
			} while(cursor.moveToNext());
			
			cursor.close();
			DBAgent.getInstance(c).closeAdapter();
		}
		return questions;
	}
	
	private static Cursor getGameCursor(Context c, int idGame) {
	
		switch(idGame) {
		case 0:
			return DBAgent.getInstance(c).getDerDieDasCursor();
		case 1:
			return DBAgent.getInstance(c).getVerbenCursor();
		case 2:
			return DBAgent.getInstance(c).getGramatikCursor();
		default:
			throw new Error("GameActivity: invalid game identificator");
		}
	}
}
