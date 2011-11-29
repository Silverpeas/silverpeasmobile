package com.silverpeas.mobile.shared.exceptions;

public class ContactException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5428570578201785885L;
	
	public ContactException() {
		super();
	}

	public ContactException(String message, Throwable caught) {
		super(message, caught);
	}

	public ContactException(String message) {
		super(message);
	}

	public ContactException(Throwable caught) {
		super(caught);
	}
}
