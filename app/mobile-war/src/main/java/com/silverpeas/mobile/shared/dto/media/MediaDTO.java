package com.silverpeas.mobile.shared.dto.media;

import com.silverpeas.mobile.shared.dto.BaseDTO;

import java.io.Serializable;

public class MediaDTO extends BaseDTO implements Serializable {

  private static final long serialVersionUID = 1L;
  private boolean download;
  private String title;
  private String name;
  private String updateDate;
  private String updater;
  private int commentsNumber;
  private String instance;
  private String mimeType;
  private long size;

  public boolean isDownload() {
    return download;
  }
  public void setDownload(boolean download) {
    this.download = download;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public void setName(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(final String updateDate) {
    this.updateDate = updateDate;
  }

  public String getUpdater() {
    return updater;
  }

  public void setUpdater(final String updater) {
    this.updater = updater;
  }

  public void setCommentsNumber(final int commentsNumber) {
    this.commentsNumber = commentsNumber;
  }

  public int getCommentsNumber() {
    return commentsNumber;
  }

  public String getInstance() {
    return instance;
  }

  public void setInstance(final String instance) {
    this.instance = instance;
  }

  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(final String mimeType) {
    this.mimeType = mimeType;
  }

  public void setSize(final long size) {
    this.size = size;
  }

  public long getSize() {
    return size;
  }
}
