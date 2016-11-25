package com.silverpeas.mobile.shared.exceptions;

public class DocumentsException extends Exception {

	private static final long serialVersionUID = 1L;

	public DocumentsException() {
		super();
	}

	public DocumentsException(String message, Throwable caught) {
		super(message, caught);
	}

	public DocumentsException(String message) {
		super(message);
	}

	public DocumentsException(Throwable caught) {
		super(caught);
	}

}
