package com.deutchall.identification;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;

import com.deutchall.persistence.DBAgent;

public class PFRanking {
	
	public static List<Ranking> getRanking(Context context, int idGame) {
		switch(idGame) {
		case DBAgent.DERDIEDAS_ID:
			return select(context, DBAgent.DDD_RANK);
		case DBAgent.VERBEN_ID:
			return select(context, DBAgent.VERB_RANK);
		case DBAgent.GRAMATIK_ID:
			return select(context, DBAgent.GRAM_RANK);
		default:
			throw new Error("GameActivity: invalid game identificator");
		}
	}
	
	private static List<Ranking> select(Context context, String rankingTable) {
		String[] columns = new String[] {DBAgent.KEY_RANK, DBAgent.RANK_NAME, DBAgent.SCORE};
		
		List<Ranking> ranking = DBAgent.getInstance(context).selectRanking(rankingTable, columns, null, null, null, null, DBAgent.SCORE + " DESC");
		return ranking;
	}
	
	public static void insertIntoRankingGame(Context context, int idGame, String name, String date, int score) {
		switch(idGame) {
		case DBAgent.DERDIEDAS_ID:
			insert(context, DBAgent.DDD_RANK, name, date, score);
			break;
		case DBAgent.VERBEN_ID:
			insert(context, DBAgent.VERB_RANK, name, date, score);
			break;
		case DBAgent.GRAMATIK_ID:
			insert(context, DBAgent.GRAM_RANK, name, date, score);
			break;
		default:
			throw new Error("GameActivity: invalid game identificator");
		}
	}
	
	private static void insert(Context context, String rankingTable, String name, String date, int score) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBAgent.RANK_NAME, name + " - " + date);
		contentValues.put(DBAgent.SCORE, score);
		DBAgent.getInstance(context).insert(rankingTable, contentValues);
	}
	
	public static void deleteRankingGame(Context context, int idGame) {
		switch(idGame) {
		case DBAgent.DERDIEDAS_ID:
			delete(context, DBAgent.DDD_RANK);
			break;
		case DBAgent.VERBEN_ID:
			delete(context, DBAgent.VERBEN);
			break;
		case DBAgent.GRAMATIK_ID:
			delete(context, DBAgent.GRAM_RANK);
			break;
		default:
			throw new Error("GameActivity: invalid game identificator");
		}
	}
	
	private static void delete(Context context, String rankingTable) {
		DBAgent.getInstance(context).delete(rankingTable, null, null);
	}
}
