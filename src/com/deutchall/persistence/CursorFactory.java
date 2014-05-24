package com.deutchall.persistence;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.deutchall.identification.Question;
import com.deutchall.identification.Ranking;
import com.deutchall.identification.User;

public class CursorFactory {
	
	public static List<User> cursorToUserList(Cursor cursor) {
		List<User> users = new ArrayList<User>();
		if (cursor.moveToFirst()) {
			do {
				String name = cursor.getString(cursor.getColumnIndex(Sql.USERNAME));
				String email = cursor.getString(cursor.getColumnIndex(Sql.EMAIL));
				users.add(new User(name, email));
			} while(cursor.moveToNext());	
		} 
		cursor.close();
		return users;
	}
	
	public static List<Question> cursorToQuestionList(Cursor cursor) {
		List<Question> questions = new ArrayList<Question>();
		if (cursor.moveToFirst()) {
			do {
				String heading = cursor.getString(cursor.getColumnIndex(Sql.HEADING));
				String ans1 = cursor.getString(cursor.getColumnIndex(Sql.ANS1));
				String ans2 = cursor.getString(cursor.getColumnIndex(Sql.ANS2));
				String ans3 = cursor.getString(cursor.getColumnIndex(Sql.ANS3));
				int correct = cursor.getInt(cursor.getColumnIndex(Sql.CORRECT));
				questions.add(new Question(heading, ans1, ans2, ans3, correct));
			} while(cursor.moveToNext());
		}
		cursor.close();
		return questions;
	}
	
	public static List<Ranking> cursorToRankingList(Cursor cursor) {
		List<Ranking> ranking = new ArrayList<Ranking>();
		if (cursor.moveToFirst()) {
			do {
				String name = cursor.getString(cursor.getColumnIndex(Sql.RANK_USER));
				String date = cursor.getString(cursor.getColumnIndex(Sql.RANK_DATE));
				int score = cursor.getInt(cursor.getColumnIndex(Sql.SCORE));
				ranking.add(new Ranking(name, date, score));
			} while(cursor.moveToNext());
		}
		cursor.close();
		return ranking;
	}
}
