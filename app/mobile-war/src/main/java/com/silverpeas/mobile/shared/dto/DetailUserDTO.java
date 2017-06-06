package com.silverpeas.mobile.shared.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public class DetailUserDTO implements Serializable{

  private static final long serialVersionUID = 5388415881024885835L;
  private String id;
  private String lastName;
  private String eMail;
  private String firstName;
  private String avatar;
  private String phoneNumber;
  private String cellularPhoneNumber;
  private String faxPhoneNumber;
  private String status;
  private String language;
  private String token;
  private LinkedHashMap<String, String> properties = new LinkedHashMap<String, String>();

  public void addProperty(String key, String value) {
    properties.put(key, value);
  }

  public Set<String> getProperties() {
    return properties.keySet();
  }

  public String getPropertieValue(String key) {
    return properties.get(key);
  }

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

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getLanguage() {
    return language;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
