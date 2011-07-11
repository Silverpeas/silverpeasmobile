package com.oosphere.silverpeasmobile.trace;

import com.oosphere.silverpeasmobile.utils.StringUtils;

public class AbstractLogger {
  
  private static final String SEPARATOR = " | ";

  protected static String getLine(String[] elements) {
    StringBuilder sb = new StringBuilder();
    for (String element : elements) {
      if (StringUtils.isValued(element)) {
        if (sb.length() > 0) {
          sb.append(SEPARATOR);
        }
        sb.append(element);
      }
    }
    return sb.toString();
  }
  
  protected static String getClassName(Object object, String method) {
    return SilverpeasMobileTrace.getClassName(object, method);
  }
  
  protected static String getMessage(String key) {
    return SilverpeasMobileTrace.getMessage(key);
  }
  
}
