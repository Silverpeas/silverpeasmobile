package com.silverpeas.mobile.shared.dto;

import java.io.Serializable;

public class UserDTO extends BaseDTO implements Serializable{

  private static final long serialVersionUID = 5388415881024885835L;

  private String lastName;
  private String eMail;
  private String firstName;
  private String avatar;

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String LastName) {
    this.lastName = LastName;
  }

  public String getFirstName(){
    return firstName;
  }

  public void setFirstName(String firstName){
    this.firstName = firstName;
  }

  public String geteMail() {
    return eMail;
  }

  public void seteMail(String eMail) {
    this.eMail = eMail;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    UserDTO other = (UserDTO) obj;
    if (getId() == null) {
      return false;
    } else if (!getId().equals(other.getId()))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    return Integer.parseInt(getId());
  }
}
