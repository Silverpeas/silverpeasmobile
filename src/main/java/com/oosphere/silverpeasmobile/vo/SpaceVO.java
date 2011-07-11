package com.oosphere.silverpeasmobile.vo;

import java.util.ArrayList;

public class SpaceVO {

  private String id;
  private String name;
  private ArrayList<ComponentVO> components;
  
  public SpaceVO(String id, String name) {
    this.id = id;
    this.name = name;
    components = new ArrayList<ComponentVO>();
  }
  
  public String getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
  
  public ArrayList<ComponentVO> getComponents() {
    return components;
  }
  
  public void addComponent(ComponentVO component) {
    components.add(component);
  }
  
}
