package com.oosphere.silverpeasmobile.vo;

import java.util.ArrayList;
import java.util.Date;

import com.silverpeas.util.EncodeHelper;

public class PublicationVO {

  private String id;
  private String name;
  private String description;
  private String creatorId;
  private String creatorName;
  private Date creationDate;
  private String updaterId;
  private String updaterName;
  private Date updateDate;
  private ArrayList<AttachmentVO> attachments;
  
  public PublicationVO(String id, String name, String description, String creatorId,
      String creatorName, Date creationDate) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.creatorId = creatorId;
    this.creatorName = creatorName;
    this.creationDate = creationDate;
    attachments = new ArrayList<AttachmentVO>();
  }
  
  public void setUpdaterData(String id, String name, Date date) {
    updaterId = id;
    updaterName = name;
    updateDate = date;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }
  
  public String getDescription() {
    return description;
  }

  public String getCreatorId() {
    return creatorId;
  }

  public String getCreatorName() {
    return creatorName;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public String getUpdaterId() {
    return updaterId;
  }

  public String getUpdaterName() {
    return updaterName;
  }

  public Date getUpdateDate() {
    return updateDate;
  }
  
  public String getFormattedDescription() {
    return EncodeHelper.javaStringToHtmlParagraphe(description);
  }
  
  public ArrayList<AttachmentVO> getAttachments() {
    return attachments;
  }
  
  public void addAttachment(AttachmentVO attachment) {
    attachments.add(attachment);
  }
  
}