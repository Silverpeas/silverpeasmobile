package com.silverpeas.mobile.shared.dto.documents;

import java.io.Serializable;

import com.silverpeas.mobile.shared.dto.BaseDTO;

public class TopicDTO extends BaseDTO implements Serializable, Comparable<TopicDTO> {

  private static final long serialVersionUID = 1L;
  private String name;
  private boolean terminal;
  private int pubCount;
  private boolean root = false;

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  @Override
  public int compareTo(TopicDTO o) {
    return name.compareTo(o.getName());
  }
  public boolean isTerminal() {
    return terminal;
  }
  public void setTerminal(boolean terminal) {
    this.terminal = terminal;
  }

  public boolean isRoot() {
    return root;
  }
  public void setRoot(boolean root) {
    this.root = root;
  }

  public int getPubCount() {
    return pubCount;
  }

  public void setPubCount(final int pubCount) {
    this.pubCount = pubCount;
  }
}
