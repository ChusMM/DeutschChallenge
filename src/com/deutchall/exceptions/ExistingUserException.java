package com.deutchall.exceptions;

public class ExistingUserException extends Exception {

	private static final long serialVersionUID = 2414187874263915639L;
	
	public ExistingUserException() {
		super("Usuario existente en la base de datos");
	}
}
