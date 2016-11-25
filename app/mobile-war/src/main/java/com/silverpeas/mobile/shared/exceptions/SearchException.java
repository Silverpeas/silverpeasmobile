package com.silverpeas.mobile.shared.exceptions;

public class SearchException extends Exception{

  /**
   *
   */
  private static final long serialVersionUID = -5537677233193111082L;

  public SearchException() {
    super();
  }

  public SearchException(String message, Throwable caught) {
    super(message, caught);
  }

  public SearchException(String message) {
    super(message);
  }

  public SearchException(Throwable caught) {
    super(caught);
  }
}
