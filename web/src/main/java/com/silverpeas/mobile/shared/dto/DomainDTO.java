package com.silverpeas.mobile.shared.dto;

import java.io.Serializable;

public class DomainDTO implements  Serializable {

  private static final long serialVersionUID = 3763707876636751540L;
  private String id;
  private String name;

  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
}
