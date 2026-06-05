package org.silverpeas.mobile.shared.dto.authentication;

/**
 * @author svu
 */
public interface IUserProfile {
  public String getUri();

  public void setUri(final String uri);

  public String getContactsUri();

  public void setContactsUri(final String contactsUri);

  public String getWebPage();

  public void setWebPage(final String webPage);

  public String getAvatar();

  public void setAvatar(final String avatar);

  public String getDomainName();

  public void setDomainName(final String domainName);

  public String getFullName();

  public void setFullName(final String fullName);

  public String getLanguage();

  public void setLanguage(final String language);

  public boolean isConnected();

  public void setConnected(final boolean connected);

  public boolean isAnonymous();

  public void setAnonymous(final boolean anonymous);

  public String getApiToken();

  public void setApiToken(final String apiToken);

  public String getId();

  public void setId(final String id);

  public String getSpecificId();

  public void setSpecificId(final String specificId);

  public String getDomainId();

  public void setDomainId(final String domainId);

  public String getLogin();

  public void setLogin(final String login);

  public String getFirstName();

  public void setFirstName(final String firstName);

  public String getLastName();

  public void setLastName(final String lastName);

  public String getEmailAddress();

  public void setEmailAddress(final String eMail);

  public String getAccessLevel();

  public void setAccessLevel(final String accessLevel);

}
