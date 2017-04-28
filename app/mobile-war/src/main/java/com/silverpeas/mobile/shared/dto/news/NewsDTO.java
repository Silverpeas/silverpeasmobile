package com.silverpeas.mobile.shared.dto.news;

import com.silverpeas.mobile.shared.dto.BaseDTO;

import java.io.Serializable;

public class NewsDTO extends BaseDTO implements Serializable {

  private static final long serialVersionUID = 2921606984249560882L;

  private String title;
  private String description;
  private String updateDate;
  private String vignette;
  private String instanceId;

  @Override
  public boolean equals(Object obj) {
    return ((NewsDTO) obj).getId().equals(getId());
  }

  public String getVignette() {
    return vignette;
  }

  public void setVignette(final String vignette) {
    this.vignette = vignette;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(final String updateDate) {
    this.updateDate = updateDate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public void setInstanceId(final String instanceId) {
    this.instanceId = instanceId;
  }

  public String getInstanceId() {
    return instanceId;
  }
}
