package com.silverpeas.mobile.shared.dto.navigation;

import java.io.Serializable;

public abstract class SilverpeasObjectDTO implements Serializable, Comparable<SilverpeasObjectDTO> {

  private static final long serialVersionUID = 1L;
  private String id;
  private String label;
  private Integer orderNum;

  public int getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(final int orderNum) {
    this.orderNum = orderNum;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  @Override
  public int compareTo(SilverpeasObjectDTO o) {
    return orderNum.compareTo(o.orderNum);
  }
}
