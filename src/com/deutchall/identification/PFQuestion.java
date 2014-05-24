package com.deutchall.identification;

import android.content.Context;
import android.database.sqlite.SQLiteException;

import java.io.IOException;
import java.util.List;

import com.deutchall.persistence.DBAgent;
import com.deutchall.persistence.Sql;

public class PFQuestion {
	
	public static List<Question> getGameQuestions(Context context, int idGame) throws SQLiteException, IndexOutOfBoundsException, IOException {
		switch(idGame) {
		case Sql.DERDIEDAS_ID:
			return select(context, Sql.DER_DIE_DAS);
		case Sql.VERBEN_ID:
			return select(context, Sql.VERBEN);
		case Sql.GRAMATIK_ID:
			return select(context, Sql.GRAMATIK);
		default:
			throw new Error("GameActivity: invalid game identificator");
		}
	}
	
	public static List<Question> select(Context context, String questionTable) throws SQLiteException, IndexOutOfBoundsException, IOException {
		String[] columns = new String[] {Sql.QUESTION_KEY, Sql.HEADING, Sql.ANS1, Sql.ANS2, Sql.ANS3, Sql.CORRECT};
		List<Question> questions = DBAgent.getInstance(context).selectQuestion(questionTable, columns, null, null, null, null, null);
		return questions;
	}
	
	public int count(Context context, String questionTable) throws SQLiteException, IndexOutOfBoundsException, IOException {
		String[] columns = new String[] {Sql.QUESTION_KEY};
		List<Question> questions = DBAgent.getInstance(context).selectQuestion(questionTable, columns, null, null, null, null, null);
		return questions.size();
	}
}
