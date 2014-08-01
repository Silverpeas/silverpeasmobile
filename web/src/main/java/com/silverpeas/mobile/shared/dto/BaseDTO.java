package com.silverpeas.mobile.shared.dto;

import java.io.Serializable;

/**
 * @author: svu
 */
public abstract class BaseDTO implements Serializable{

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  private String id;

}
