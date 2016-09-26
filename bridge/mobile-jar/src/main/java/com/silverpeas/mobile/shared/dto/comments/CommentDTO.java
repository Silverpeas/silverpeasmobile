package com.silverpeas.mobile.shared.dto.comments;

import com.silverpeas.mobile.shared.dto.BaseDTO;

import java.io.Serializable;

/**
 * @author: svu
 */
public class CommentDTO extends BaseDTO implements Serializable {

  public final static String TYPE_PUBLICATION = "Publication";
  public final static String TYPE_PHOTO = "Photo";
  public final static String TYPE_VIDEO = "Video";
  public final static String TYPE_SOUND = "Sound";
  public final static String TYPE_STREAMING = "Streaming";

  private String content;
  private String avatar;
  private String date;
  private String userName;

  public String getContent() {
    return content;
  }

  public void setContent(final String content) {
    this.content = content;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(final String avatar) {
    this.avatar = avatar;
  }

  public String getDate() {
    return date;
  }

  public void setDate(final String date) {
    this.date = date;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(final String userName) {
    this.userName = userName;
  }
}
