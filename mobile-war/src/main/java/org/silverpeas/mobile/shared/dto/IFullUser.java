package org.silverpeas.mobile.shared.dto;



/**
 * @author svu
 */
public interface IFullUser {

  public String getLogin();

  public void setLogin(String login);

  public String getPassword();

  public void setPassword(String password);

  public String getDomainId();

  public void setDomainId(String domainId);

  public String getToken();

  public void setToken(final String token);

  public String getId();

  public void setId(String id);

  public String getLastName();

  public void setLastName(String LastName);

  public void seteMail(String EMail);

  public String geteMail();

  public String getFirstName();

  public void setFirstName(String firstName);

  public String getAvatar();

  public void setAvatar(String avatar);

  public String getPhoneNumber();

  public void setPhoneNumber(String phoneNumber);

  public String getCellularPhoneNumber();

  public void setCellularPhoneNumber(String cellularPhoneNumber);

  public String getFaxPhoneNumber();

  public void setFaxPhoneNumber(String faxPhoneNumber);

  public String getStatus();

  public void setStatus(final String status);

  public void setLanguage(String language);

  public String getLanguage();

  public void setConnected(final boolean connected);

  public boolean getConnected();

  public void setZone(final String zone);

  public String getZone();

  public void setSessionKey(final String sessionKey);

  public String getSessionKey();

  public void setNotificationBox(final boolean notificationBox);

  public boolean isNotificationBox();
}
