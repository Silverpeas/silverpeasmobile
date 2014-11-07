package com.silverpeas.mobile.shared.dto;

import java.io.Serializable;
import java.util.Date;

public class TaskDTO implements Serializable {

  private static final long serialVersionUID = 2921606984249560882L;

  private int id;
  private String name = "";

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  @Override
  public boolean equals(Object obj) {
    return ((TaskDTO) obj).getId() == id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }
}
