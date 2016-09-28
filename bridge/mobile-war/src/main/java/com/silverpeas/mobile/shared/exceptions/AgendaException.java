package com.silverpeas.mobile.shared.exceptions;

public class AgendaException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5537677233193111082L;

	public AgendaException() {
		super();
	}

	public AgendaException(String message, Throwable caught) {
		super(message, caught);
	}

	public AgendaException(String message) {
		super(message);
	}

	public AgendaException(Throwable caught) {
		super(caught);
	}
}
