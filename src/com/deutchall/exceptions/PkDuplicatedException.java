package com.deutchall.exceptions;

public class PkDuplicatedException extends Exception {

	private static final long serialVersionUID = 8398549357472134576L;
	
	public PkDuplicatedException(String msg) {
		super(msg);
	}
}
