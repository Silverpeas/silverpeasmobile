package com.oosphere.silverpeasmobile.vo;

public class AttachmentVO {

  private String name;
  private String fileId;
  private String fileSize;
  private String fileIcon;
  private String fileUrl;
  
  public AttachmentVO(String name, String fileId, String fileSize, String fileIcon, String fileUrl) {
    this.name = name;
    this.fileId = fileId;
    this.fileSize = fileSize;
    this.fileIcon = fileIcon;
    this.fileUrl = fileUrl;
  }
  
  public String getName() {
    return name;
  }
  
  public String getFileId() {
    return fileId;
  }
  
  public String getFileSize() {
    return fileSize;
  }

  public String getFileIcon() {
    return fileIcon;
  }

  public String getFileUrl() {
    return fileUrl;
  }
  
}
