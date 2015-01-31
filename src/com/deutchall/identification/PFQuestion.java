package com.deutchall.identification;

import android.database.sqlite.SQLiteException;

import java.io.IOException;
import java.util.List;

import com.deutchall.exceptions.InvalidGameIdException;
import com.deutchall.persistence.DBAgent;
import com.deutchall.persistence.Sql;

public class PFQuestion {
	
	public static List<Question> getGameQuestions(int idGame) throws SQLiteException, IndexOutOfBoundsException, IOException, InvalidGameIdException {
		switch(idGame) {
		case Sql.DERDIEDAS_ID:
			return select(Sql.DER_DIE_DAS);
		case Sql.VERBEN_ID:
			return select(Sql.VERBEN);
		case Sql.GRAMATIK_ID:
			return select(Sql.GRAMATIK);
		default:
			throw new InvalidGameIdException();
		}
	}
	
	public static List<Question> select(String questionTable) throws SQLiteException, IndexOutOfBoundsException, IOException {
		String[] columns = new String[] {Sql.QUESTION_KEY, Sql.HEADING, Sql.ANS1, Sql.ANS2, Sql.ANS3, Sql.CORRECT};
		List<Question> questions = DBAgent.getInstance().selectQuestion(questionTable, columns, null, null, null, null, null);
		return questions;
	}
	
	public int count(String questionTable) throws SQLiteException, IndexOutOfBoundsException, IOException {
		String[] columns = new String[] {Sql.QUESTION_KEY};
		List<Question> questions = DBAgent.getInstance().selectQuestion(questionTable, columns, null, null, null, null, null);
		return questions.size();
	}
}
