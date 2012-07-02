package com.silverpeas.mobile.shared.exceptions;

public class GalleryException extends Exception {

	private static final long serialVersionUID = 1L;

	public GalleryException() {
		super();
	}

	public GalleryException(String message, Throwable caught) {
		super(message, caught);
	}

	public GalleryException(String message) {
		super(message);
	}

	public GalleryException(Throwable caught) {
		super(caught);
	}

}
