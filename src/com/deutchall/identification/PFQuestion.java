package com.deutchall.identification;

import android.content.Context;
import android.database.sqlite.SQLiteException;

import java.io.IOException;
import java.util.List;

import com.deutchall.persistence.DBAgent;

public class PFQuestion {
	
	public static List<Question> getGameQuestions(Context context, int idGame) throws SQLiteException, IndexOutOfBoundsException, IOException {
		switch(idGame) {
		case DBAgent.DERDIEDAS_ID:
			return select(context, DBAgent.DER_DIE_DAS);
		case DBAgent.VERBEN_ID:
			return select(context, DBAgent.VERBEN);
		case DBAgent.GRAMATIK_ID:
			return select(context, DBAgent.GRAMATIK);
		default:
			throw new Error("GameActivity: invalid game identificator");
		}
	}
	
	public static List<Question> select(Context context, String questionTable) throws SQLiteException, IndexOutOfBoundsException, IOException {
		String[] columns = new String[] {DBAgent.QUESTION_KEY, DBAgent.HEADING, DBAgent.ANS1, DBAgent.ANS2, DBAgent.ANS3, DBAgent.CORRECT};
		List<Question> questions = DBAgent.getInstance(context).selectQuestion(questionTable, columns, null, null, null, null, null);
		return questions;
	}
	
	public int count(Context context, String questionTable) throws SQLiteException, IndexOutOfBoundsException, IOException {
		String[] columns = new String[] {DBAgent.QUESTION_KEY};
		List<Question> questions = DBAgent.getInstance(context).selectQuestion(questionTable, columns, null, null, null, null, null);
		return questions.size();
	}
}
