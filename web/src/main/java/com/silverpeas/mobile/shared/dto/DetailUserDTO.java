package com.silverpeas.mobile.shared.dto;

import java.io.Serializable;

public class DetailUserDTO implements Serializable{

  private static final long serialVersionUID = 5388415881024885835L;
  public String id;
  public String lastName;
  public String eMail;
  public String firstName;
  public String avatar;
  public String phoneNumber;
  public String cellularPhoneNumber;
  public String faxPhoneNumber;
  public String status;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String LastName) {
    this.lastName = LastName;
  }

  public void seteMail(String EMail) {
    eMail = EMail;
  }

  public String geteMail() {
    return eMail;
  }

  public String getFirstName(){
    return firstName;
  }

  public void setFirstName(String firstName){
    this.firstName = firstName;
  }

  public String getAvatar(){
    return avatar;
  }

  public void setAvatar(String avatar){
    this.avatar = avatar;
  }

  public String getPhoneNumber(){
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber){
    this.phoneNumber = phoneNumber;
  }

  public String getCellularPhoneNumber() {
    return cellularPhoneNumber;
  }

  public void setCellularPhoneNumber(String cellularPhoneNumber) {
    this.cellularPhoneNumber = cellularPhoneNumber;
  }

  public String getFaxPhoneNumber() {
    return faxPhoneNumber;
  }

  public void setFaxPhoneNumber(String faxPhoneNumber) {
    this.faxPhoneNumber = faxPhoneNumber;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(final String status) {
    this.status = status;
  }
}
