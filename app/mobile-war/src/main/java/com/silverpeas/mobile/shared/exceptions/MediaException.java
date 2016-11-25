package com.silverpeas.mobile.shared.exceptions;

public class MediaException extends Exception {

	private static final long serialVersionUID = 1L;

	public MediaException() {
		super();
	}

	public MediaException(String message, Throwable caught) {
		super(message, caught);
	}

	public MediaException(String message) {
		super(message);
	}

	public MediaException(Throwable caught) {
		super(caught);
	}

}
