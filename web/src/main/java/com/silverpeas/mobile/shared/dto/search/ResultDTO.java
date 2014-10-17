package com.silverpeas.mobile.shared.dto.search;

import java.io.Serializable;

public class ResultDTO implements Serializable{

  private static final long serialVersionUID = 5388415881024885835L;
  private String id;
  private String title;
  private String type;
  private String componentId;


  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public String getComponentId() {
    return componentId;
  }

  public void setComponentId(final String componentId) {
    this.componentId = componentId;
  }
}
