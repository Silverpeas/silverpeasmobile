package com.silverpeas.mobile.shared.exceptions;

public class AdminException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5885919506024352807L;

	public AdminException() {
		super();
	}

	public AdminException(String message, Throwable caught) {
		super(message, caught);
	}

	public AdminException(String message) {
		super(message);
	}

	public AdminException(Throwable caught) {
		super(caught);
	}
}
