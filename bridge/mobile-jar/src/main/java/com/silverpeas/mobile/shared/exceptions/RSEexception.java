package com.silverpeas.mobile.shared.exceptions;

public class RSEexception extends Exception {

	private static final long serialVersionUID = 1L;

	public RSEexception() {
		super();
	}

	public RSEexception(String message, Throwable caught) {
		super(message, caught);
	}

	public RSEexception(String message) {
		super(message);
	}

	public RSEexception(Throwable caught) {
		super(caught);
	}

}
