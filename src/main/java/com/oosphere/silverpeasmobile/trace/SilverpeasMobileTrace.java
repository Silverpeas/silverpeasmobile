package com.oosphere.silverpeasmobile.trace;

import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.PropertyConfigurator;

import com.oosphere.silverpeasmobile.exception.SilverpeasMobileException;
import com.stratelia.silverpeas.silvertrace.SilverTrace;

public class SilverpeasMobileTrace extends SilverTrace {
  
  public static String MODULE = "docclassifier";
  
  private static ResourceBundle messages = null;
  
  public static void init(String language)
  throws SilverpeasMobileException {
    messages = ResourceBundle.getBundle("messages_" + language);
    InputStream configFile = SilverpeasMobileTrace.class.getClassLoader()
      .getResourceAsStream("trace.properties");
    Properties traceConf = null;
    if (configFile == null) {
      throw new SilverpeasMobileException(SilverpeasMobileTrace.class, "init",
        "Impossible de trouver le fichier de configuration des traces");
    } else {
      try {
        traceConf = new Properties();
        traceConf.load(configFile);
      } catch (IOException e) {
        throw new SilverpeasMobileException(SilverpeasMobileTrace.class, "init",
          "Impossible de charger le fichier de configuration des traces");
      } finally {
        if (configFile != null) {
          try {
            configFile.close();
          } catch (IOException e) {
            // do nothing
          }
        }
      }
    }
    PropertyConfigurator.configure(traceConf);
  }
  
  public static void error(Object object, String method, String message, String infos, Throwable t) {
    String callingClass = getClassName(object, method);
    String messageKey = MODULE + "." + message;
    SilverTrace.error(MODULE, callingClass, messageKey, infos, t);
    ErrorLogger.error(callingClass, getMessage(messageKey), t);
  }
  
  public static void error(Object object, String method, String message, Throwable t) {
    error(object, method, message, null, t);
  }
  
  public static void error(Object object, String method, String message, String infos) {
    error(object, method, message, infos, null);
  }
  
  public static void error(Object object, String method, String message) {
    error(object, method, message, null, null);
  }
  
  
  public static void info(Object object, String method, String message, String infos, Throwable t) {
    SilverTrace.info(MODULE, getClassName(object, method), MODULE + "." + message, infos, t);
  }
  
  public static void info(Object object, String method, String message, Throwable t) {
    info(object, method, message, null, t);
  }
  
  public static void info(Object object, String method, String message, String infos) {
    info(object, method, message, infos, null);
  }
  
  public static void info(Object object, String method, String message) {
    info(object, method, message, null, null);
  }
  
  
  public static String getClassName(Object object, String method) {
    StringBuffer className = new StringBuffer();
    
    if (object != null) {
      if (object instanceof String) {
        className.append((String)object);
      } else if (object instanceof Class<?>) {
        className.append(((Class<?>)object).getSimpleName());
      } else {
        className.append(object.getClass().getSimpleName());
      }
    }
    
    if (method != null) {
      if (className.length() > 0) {
        className.append(".");
      }
      className.append(method);
      if (!method.endsWith("()")) {
        className.append("()");
      }
    }
    
    return className.toString();
  }
  
  public static String getMessage(String key) {
    try {
      return messages.getString(MODULE + "." +  key);
    } catch (MissingResourceException e) {
      return key;
    }
  }
  
}
