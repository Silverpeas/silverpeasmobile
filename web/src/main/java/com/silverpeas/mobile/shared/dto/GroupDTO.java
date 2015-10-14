package com.silverpeas.mobile.shared.dto;

import java.io.Serializable;

public class GroupDTO extends BaseDTO implements Serializable{

  private static final long serialVersionUID = 5388415881024885835L;
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
