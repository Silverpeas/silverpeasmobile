package com.silverpeas.mobile.shared.exceptions;

public class AlmanachException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1720039965391260361L;

	public AlmanachException() {
		super();
	}

	public AlmanachException(String message, Throwable caught) {
		super(message, caught);
	}

	public AlmanachException(String message) {
		super(message);
	}

	public AlmanachException(Throwable caught) {
		super(caught);
	}
}
