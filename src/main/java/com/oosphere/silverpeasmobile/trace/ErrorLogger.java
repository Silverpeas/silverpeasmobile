package com.oosphere.silverpeasmobile.trace;

import org.apache.log4j.Logger;

import com.oosphere.silverpeasmobile.exception.SilverpeasMobileException;

public class ErrorLogger extends AbstractLogger {

  private static Logger log = Logger.getLogger("errors");

  public static void info(String line) {
    log.info(line);
  }
  
  public static void error(String method, String line, Throwable t) {
    log.error(getLine(new String[] {method, line, t.getMessage()}));
  }
  
  public static void error(String method, String line) {
    log.error(getLine(new String[] {method, line}));
  }
  
  public static void error(SilverpeasMobileException exception) {
    String message = exception.getDetail();
    if (exception.getNested() != null) {
      error(exception.getMethod(), message, exception.getNested());
    } else {
      error(exception.getMethod(), message);
    }
  }

}
