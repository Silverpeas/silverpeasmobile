package com.silverpeas.mobile.shared.dto;

import java.io.Serializable;

/**
 * @author: svu
 */
public class RightDTO implements Serializable {

  private static final long serialVersionUID = 1186851689918190659L;

  private boolean reader = false;
  private boolean writer = false;
  private boolean publisher = false;
  private boolean manager = false;

  public boolean isWriter() {
    return writer;
  }

  public void setWriter(final boolean writer) {
    this.writer = writer;
  }

  public boolean isPublisher() {
    return publisher;
  }

  public void setPublisher(final boolean publisher) {
    this.publisher = publisher;
  }

  public boolean isManager() {
    return manager;
  }

  public void setManager(final boolean manager) {
    this.manager = manager;
  }

  public boolean isReader() {
    return reader;
  }

  public void setReader(final boolean reader) {
    this.reader = reader;
  }
}
