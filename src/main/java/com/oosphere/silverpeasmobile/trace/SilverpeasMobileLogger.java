package com.oosphere.silverpeasmobile.trace;

import org.apache.log4j.Logger;

public class SilverpeasMobileLogger extends AbstractLogger {

private static Logger log = Logger.getLogger("docclassifier");
  
  public static void info(String line) {
    log.info(line);
  }
  
  public static void info(Object object, String method, String line) {
    log.info(getLine(new String[] {getClassName(object, method), getMessage(line)}));
  }
  
  public static void info(Object object, String method, String line, String infos) {
    log.info(getLine(new String[] {getClassName(object, method), getMessage(line), infos}));
  }
  
}
