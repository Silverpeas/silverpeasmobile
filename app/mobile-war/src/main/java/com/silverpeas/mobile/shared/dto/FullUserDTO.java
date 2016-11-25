package com.silverpeas.mobile.shared.dto;

public class FullUserDTO extends DetailUserDTO {
  private String login;
  private String password;
  private String domainId;

  public FullUserDTO() {
    super();
  }

  public FullUserDTO(String login, String password, String domainId, DetailUserDTO user) {
    super();
    this.setLogin(login);
    this.setPassword(password);
    this.setDomainId(domainId);

    this.setId(user.getId());
    this.setAvatar(user.getAvatar());
    this.setCellularPhoneNumber(user.getCellularPhoneNumber());
    this.seteMail(user.geteMail());
    this.setFaxPhoneNumber(user.getFaxPhoneNumber());
    this.setFirstName(user.getFirstName());
    this.setLastName(user.getLastName());
    this.setPhoneNumber(user.getPhoneNumber());
    this.setStatus(user.getStatus());
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getDomainId() {
    return domainId;
  }

  public void setDomainId(String domainId) {
    this.domainId = domainId;
  }
}