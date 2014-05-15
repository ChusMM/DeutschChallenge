package com.deutchall.identification;

public class Ranking {
	String name;
	int score;
	
	public Ranking(String name, int score) {
		this.name = name;
		this.score = score;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Ranking) {
			Ranking aux = (Ranking) o;
			return aux.getName().equals(this.getName());
		} else {
			return false;
		}
	}
}
