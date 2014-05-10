package com.deutchall.identification;

public enum Game {
	
	derdiedas(0), verben(1), gramatik(2);
	private int gameId;
	
	private Game(int gameId) {
		this.gameId = gameId;
	}
	
	public int getGameId() {
		return this.gameId;
	}
}
