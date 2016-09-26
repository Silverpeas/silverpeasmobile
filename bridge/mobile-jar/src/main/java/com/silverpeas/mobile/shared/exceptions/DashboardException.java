package com.silverpeas.mobile.shared.exceptions;

public class DashboardException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public DashboardException(){
		super();
	}

	public DashboardException(String message, Throwable caught) {
		super(message, caught);
	}

	public DashboardException(String message) {
		super(message);
	}

	public DashboardException(Throwable caught) {
		super(caught);
	}
}
