package com.silverpeas.mobile.shared.dto.documents;

import java.io.Serializable;
import java.util.Date;

public class AttachmentDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String type = null;
  private Date creationDate;
  private long size;
  private String author = null;
  private String title = null;
  private int orderNum;
  private String id;
  private String instanceId;
  private String lang;
  private String userId;
  private boolean downloadAllowed;


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(int orderNum) {
    this.orderNum = orderNum;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setDownloadAllowed(final boolean downloadAllowed) {
    this.downloadAllowed = downloadAllowed;
  }

  public boolean isDownloadAllowed() {
    return downloadAllowed;
  }
}
