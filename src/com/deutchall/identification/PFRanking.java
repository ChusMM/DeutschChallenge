package com.deutchall.identification;

import java.io.IOException;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteException;

import com.deutchall.exceptions.InvalidGameIdException;
import com.deutchall.persistence.DBAgent;
import com.deutchall.persistence.Sql;

public class PFRanking {
	
	public static List<Ranking> getRanking(Context context, int idGame) throws SQLiteException, IndexOutOfBoundsException, IOException {
		switch(idGame) {
		case Sql.DERDIEDAS_ID:
			return select(Sql.DDD_RANK);
		case Sql.VERBEN_ID:
			return select(Sql.VERB_RANK);
		case Sql.GRAMATIK_ID:
			return select(Sql.GRAM_RANK);
		default:
			throw new Error("GameActivity: invalid game identificator");
		}
	}
	
	private static List<Ranking> select(String rankingTable) throws SQLiteException, IndexOutOfBoundsException, IOException {
		String[] columns = new String[] {Sql.KEY_RANK, Sql.RANK_USER, Sql.RANK_DATE, Sql.SCORE};
		List<Ranking> ranking = DBAgent.getInstance().selectRanking(rankingTable, columns, null, null, null, null, Sql.SCORE + " DESC");
		return ranking;
	}
	
	public static void insertIntoRankingGame(int idGame, String name, String date, int score) throws SQLiteException, IndexOutOfBoundsException, IOException, InvalidGameIdException {
		switch(idGame) {
		case Sql.DERDIEDAS_ID:
			insert(Sql.DDD_RANK, name, date, score);
			break;
		case Sql.VERBEN_ID:
			insert(Sql.VERB_RANK, name, date, score);
			break;
		case Sql.GRAMATIK_ID:
			insert(Sql.GRAM_RANK, name, date, score);
			break;
		default:
			throw new InvalidGameIdException();
		}
	}
	
	private static void insert(String rankingTable, String name, String date, int score) throws SQLiteException, IndexOutOfBoundsException, IOException {
		ContentValues contentValues = new ContentValues();
		contentValues.put(Sql.RANK_USER, name);
		contentValues.put(Sql.RANK_DATE, date);
		contentValues.put(Sql.SCORE, score);
		DBAgent.getInstance().insert(rankingTable, contentValues);
	}
	
	public static void deleteRankingGame(int idGame) throws SQLiteException, IndexOutOfBoundsException, IOException, InvalidGameIdException {
		switch(idGame) {
		case Sql.DERDIEDAS_ID:
			delete(Sql.DDD_RANK);
			break;
		case Sql.VERBEN_ID:
			delete(Sql.VERB_RANK);
			break;
		case Sql.GRAMATIK_ID:
			delete(Sql.GRAM_RANK);
			break;
		default:
			throw new InvalidGameIdException();
		}
	}
	
	private static void delete(String rankingTable) throws SQLiteException, IndexOutOfBoundsException, IOException {
		DBAgent.getInstance().delete(rankingTable, null, null);
	}
}
