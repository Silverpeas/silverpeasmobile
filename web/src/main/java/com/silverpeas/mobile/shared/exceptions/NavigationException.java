package com.silverpeas.mobile.shared.exceptions;

public class NavigationException extends Exception {

	private static final long serialVersionUID = 1L;

	public NavigationException() {
		super();
	}

	public NavigationException(String message, Throwable caught) {
		super(message, caught);
	}

	public NavigationException(String message) {
		super(message);
	}

	public NavigationException(Throwable caught) {
		super(caught);
	}

}
