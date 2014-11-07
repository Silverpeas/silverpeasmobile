package com.silverpeas.mobile.shared.exceptions;

public class Taskexception extends Exception {

	private static final long serialVersionUID = 1L;

	public Taskexception() {
		super();
	}

	public Taskexception(String message, Throwable caught) {
		super(message, caught);
	}

	public Taskexception(String message) {
		super(message);
	}

	public Taskexception(Throwable caught) {
		super(caught);
	}

}
