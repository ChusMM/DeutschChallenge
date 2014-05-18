package com.deutchall.exceptions;

public class RegisterNotFoundException extends Exception {

	private static final long serialVersionUID = 598984225353529374L;

	public RegisterNotFoundException(String msg) {
		super(msg);
	}
}
