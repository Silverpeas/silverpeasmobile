package com.silverpeas.mobile.shared.dto;

import com.stratelia.webactiv.calendar.model.Priority;

import java.io.Serializable;
import java.util.Date;

public class TaskDTO implements Serializable {

  private static final long serialVersionUID = 2921606984249560882L;

  private int id, percentCompleted, priority;
  private String name = "";
  private String delegator = "";
  private String endDate = "";

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

  public void setPercentCompleted(final int percentCompleted) {
    this.percentCompleted = percentCompleted;
  }

  public void setPriority(final int priority) {
    this.priority = priority;
  }

  public void setDelegator(final String delegator) {
    this.delegator = delegator;
  }

  public String getDelegator() {
    return delegator;
  }

  public void setEndDate(final String endDate) {
    this.endDate = endDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public int getPercentCompleted() {
    return percentCompleted;
  }

  public int getPriority() {
    return priority;
  }
}
