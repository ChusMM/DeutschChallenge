package com.deutchall.exceptions;

public class InvalidGameIdException extends Exception {
	
	private static final long serialVersionUID = 1391807992424489620L;
	
	public InvalidGameIdException() {
		super("Game identifier not valid for this version");
	}
}
