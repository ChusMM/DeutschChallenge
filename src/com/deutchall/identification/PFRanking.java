package com.deutchall.identification;

import java.io.IOException;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteException;

import com.deutchall.persistence.DBAgent;
import com.deutchall.persistence.Sql;

public class PFRanking {
	
	public static List<Ranking> getRanking(Context context, int idGame) throws SQLiteException, IndexOutOfBoundsException, IOException {
		switch(idGame) {
		case Sql.DERDIEDAS_ID:
			return select(context, Sql.DDD_RANK);
		case Sql.VERBEN_ID:
			return select(context, Sql.VERB_RANK);
		case Sql.GRAMATIK_ID:
			return select(context, Sql.GRAM_RANK);
		default:
			throw new Error("GameActivity: invalid game identificator");
		}
	}
	
	private static List<Ranking> select(Context context, String rankingTable) throws SQLiteException, IndexOutOfBoundsException, IOException {
		String[] columns = new String[] {Sql.KEY_RANK, Sql.RANK_USER, Sql.RANK_DATE, Sql.SCORE};
		List<Ranking> ranking = DBAgent.getInstance(context).selectRanking(rankingTable, columns, null, null, null, null, Sql.SCORE + " DESC");
		return ranking;
	}
	
	public static void insertIntoRankingGame(Context context, int idGame, String name, String date, int score) throws SQLiteException, IndexOutOfBoundsException, IOException {
		switch(idGame) {
		case Sql.DERDIEDAS_ID:
			insert(context, Sql.DDD_RANK, name, date, score);
			break;
		case Sql.VERBEN_ID:
			insert(context, Sql.VERB_RANK, name, date, score);
			break;
		case Sql.GRAMATIK_ID:
			insert(context, Sql.GRAM_RANK, name, date, score);
			break;
		default:
			throw new Error("GameActivity: invalid game identificator");
		}
	}
	
	private static void insert(Context context, String rankingTable, String name, String date, int score) throws SQLiteException, IndexOutOfBoundsException, IOException {
		ContentValues contentValues = new ContentValues();
		contentValues.put(Sql.RANK_USER, name);
		contentValues.put(Sql.RANK_DATE, date);
		contentValues.put(Sql.SCORE, score);
		DBAgent.getInstance(context).insert(rankingTable, contentValues);
	}
	
	public static void deleteRankingGame(Context context, int idGame) throws SQLiteException, IndexOutOfBoundsException, IOException {
		switch(idGame) {
		case Sql.DERDIEDAS_ID:
			delete(context, Sql.DDD_RANK);
			break;
		case Sql.VERBEN_ID:
			delete(context, Sql.VERBEN);
			break;
		case Sql.GRAMATIK_ID:
			delete(context, Sql.GRAM_RANK);
			break;
		default:
			throw new Error("GameActivity: invalid game identificator");
		}
	}
	
	private static void delete(Context context, String rankingTable) throws SQLiteException, IndexOutOfBoundsException, IOException {
		DBAgent.getInstance(context).delete(rankingTable, null, null);
	}
}
