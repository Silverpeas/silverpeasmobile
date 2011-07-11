package com.oosphere.silverpeasmobile.exception;

import com.oosphere.silverpeasmobile.trace.SilverpeasMobileTrace;
import com.oosphere.silverpeasmobile.utils.StringUtils;
import com.stratelia.webactiv.util.exception.SilverpeasException;

public class SilverpeasMobileException extends SilverpeasException {

  private static final long serialVersionUID = 2906394363475505883L;
  
  private String method;
  private String detail;

  public SilverpeasMobileException(Object object, String method, int errorLevel, String message,
      String infos, Exception e) {
    super(getClassName(object, method), errorLevel, message, infos, e);
    this.method = getClassName(object, method);
    detail = SilverpeasMobileTrace.getMessage(message);
    if (StringUtils.isValued(infos)) {
      detail += " - " + infos;
    }
  }

  public SilverpeasMobileException(Object object, String method, int errorLevel, String message,
      String infos) {
    this(object, method, errorLevel, message, infos, null);
  }

  public SilverpeasMobileException(Object object, String method, int errorLevel, String message,
      Exception e) {
    this(object, method, errorLevel, message, "", e);
  }

  public SilverpeasMobileException(Object object, String method, int errorLevel, String message) {
    this(object, method, errorLevel, message, "", null);
  }
  
  public SilverpeasMobileException(Object object, String method, String message, String infos,
      Exception e) {
    this(object, method, ERROR, message, infos, e);
  }
  
  public SilverpeasMobileException(Object object, String method, String message, String infos) {
    this(object, method, ERROR, message, infos, null);
  }
  
  public SilverpeasMobileException(Object object, String method, String message) {
    this(object, method, ERROR, message, "", null);
  }
  
  public SilverpeasMobileException(Object object, String method, String message, Exception e) {
    this(object, method, ERROR, message, "", e);
  }
  
  public String getMethod() {
    return method;
  }
  
  public String getDetail() {
    return detail;
  }
  
  public String getModule() {
    return SilverpeasMobileTrace.MODULE;
  }
  
  public static String getClassName(Object object, String method) {
    return SilverpeasMobileTrace.getClassName(object, method);
  }
  
}