package com.silverpeas.mobile.shared.exceptions;

public class NotificationsException extends Exception {

	private static final long serialVersionUID = 1L;

	public NotificationsException() {
		super();
	}

	public NotificationsException(String message, Throwable caught) {
		super(message, caught);
	}

	public NotificationsException(String message) {
		super(message);
	}

	public NotificationsException(Throwable caught) {
		super(caught);
	}

}
