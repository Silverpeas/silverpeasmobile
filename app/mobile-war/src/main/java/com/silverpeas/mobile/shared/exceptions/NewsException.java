package com.silverpeas.mobile.shared.exceptions;

public class NewsException extends Exception {

	private static final long serialVersionUID = 1L;

	public NewsException() {
		super();
	}

	public NewsException(String message, Throwable caught) {
		super(message, caught);
	}

	public NewsException(String message) {
		super(message);
	}

	public NewsException(Throwable caught) {
		super(caught);
	}

}
