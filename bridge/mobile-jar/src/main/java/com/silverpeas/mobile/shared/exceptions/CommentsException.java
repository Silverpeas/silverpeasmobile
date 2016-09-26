package com.silverpeas.mobile.shared.exceptions;

public class CommentsException extends Exception {

	private static final long serialVersionUID = 1L;

	public CommentsException() {
		super();
	}

	public CommentsException(String message, Throwable caught) {
		super(message, caught);
	}

	public CommentsException(String message) {
		super(message);
	}

	public CommentsException(Throwable caught) {
		super(caught);
	}

}
