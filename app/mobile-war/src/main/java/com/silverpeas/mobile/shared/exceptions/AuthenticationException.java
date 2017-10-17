package com.silverpeas.mobile.shared.exceptions;

public class AuthenticationException extends Exception {

  private static final long serialVersionUID = -1322336397375722938L;
  private AuthenticationError error;

  public enum AuthenticationError {
    BadCredential, Host, PwdNotAvailable, NotAuthenticate, LoginNotAvailable;
  }

  public AuthenticationException() {
    super();
  }

  public AuthenticationException(final Throwable throwable) {
    super(throwable);
  }

  public AuthenticationException(AuthenticationError error) {
    super();
    this.error = error;
  }

  public AuthenticationError getError() {
    return error;
  }
}
