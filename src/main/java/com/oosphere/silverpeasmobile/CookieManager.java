package com.oosphere.silverpeasmobile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oosphere.silverpeasmobile.utils.StringUtils;
import com.silverpeas.util.StringUtil;
import com.stratelia.silverpeas.authentication.EncryptionFactory;

public class CookieManager {

  private static final String DOMAIN = "defaultDomain";
  private static final String LOGIN = "svpLogin";
  private static final String PASSWORD = "svpPassword";
  private static final String DO_STORE_PASSWORD = "var2";

  private static final int SECONDS_IN_A_YEAR = 31536000;
  public static String PASSWORD_MUST_BE_STORED = "true";

  /**
   * 
   * 
   * @param response 
   * @param sStorePassword
   * @param password
   * @param isPasswordEncoded
   */
  public void storePassword(HttpServletResponse response, String sStorePassword,
      String password, boolean isPasswordEncoded) {
    if (StringUtil.getBooleanValue(sStorePassword)) {

      if (!isPasswordEncoded) {
        password = EncryptionFactory.getInstance().getEncryption().encode(password);
      }

      writeCookie(response, PASSWORD, password, -1);
      writeCookie(response, PASSWORD, password, SECONDS_IN_A_YEAR);

    }
  }

  public void removeStoredPassword(HttpServletResponse response) {
    writeCookie(response, DO_STORE_PASSWORD, "", 0);
    writeCookie(response, PASSWORD, "", 0);
  }

  public void storeLogin(HttpServletResponse response, String sLogin) {
    writeCookie(response, LOGIN, sLogin, -1);
    writeCookie(response, LOGIN, sLogin, 31536000);
  }

  public void storeDomain(HttpServletResponse response, String sDomainId) {
    writeCookie(response, DOMAIN, sDomainId, -1);
    writeCookie(response, DOMAIN, sDomainId, 31536000);
  }

  public String getStoredDomain(HttpServletRequest request) {
    return getStoredValue(request, DOMAIN);
  }

  public String getStoredLogin(HttpServletRequest request) {
    return getStoredValue(request, LOGIN);
  }

  public String getEncryptedStoredPassword(HttpServletRequest request) {
    return getStoredValue(request, PASSWORD);
  }

  public String getDecryptedStoredPassword(HttpServletRequest request) {
    String decryptedPassword = "";

    String encryptedPassword = getEncryptedStoredPassword(request);
    if (StringUtils.isValued(encryptedPassword)) {
      decryptedPassword = EncryptionFactory.getInstance().getEncryption().decode(encryptedPassword);
    }

    return decryptedPassword;
  }

  private String getStoredValue(HttpServletRequest request, String cookieName) {
    String value = "";
    Cookie[] cookies = request.getCookies();
    if(cookies!=null){
      for (Cookie cookie : cookies) {
        if (cookieName.equals(cookie.getName())) {
          value = cookie.getValue();
          break;
        }
      }
    }
    return value;
  }

  private void writeCookie(HttpServletResponse response, String name, String value, int duration) {
    Cookie cookie;
    try {
      cookie = new Cookie(name, URLEncoder.encode(value, "UTF-8"));
    } catch (UnsupportedEncodingException ex) {
      cookie = new Cookie(name, value);
    }
    cookie.setMaxAge(duration); // Duration in s
    cookie.setPath("/");
    response.addCookie(cookie);
  }

}
