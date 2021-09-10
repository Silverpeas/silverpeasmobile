/*
 * Copyright (C) 2000 - 2021 Silverpeas
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.shared.dto.documents;

import java.io.Serializable;

/**
 * @author svu
 */
public class SimpleDocumentDTO implements Serializable {


  private String id;
  private String instanceId;
  private String fileName;
  private String description;
  private String contentType;
  private String updatedBy;
  private String title;
  private String createdBy;
  private String lang;
  private String icon;
  private String permalink;
  private String downloadUrl;
  private String versioned;
  private String comment;
  private long size, creationDate, updateDate;


  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(final String instanceId) {
    this.instanceId = instanceId;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(final String fileName) {
    this.fileName = fileName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(final String contentType) {
    this.contentType = contentType;
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(final String updatedBy) {
    this.updatedBy = updatedBy;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(final String createdBy) {
    this.createdBy = createdBy;
  }

  public String getLang() {
    return lang;
  }

  public void setLang(final String lang) {
    this.lang = lang;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(final String icon) {
    this.icon = icon;
  }

  public String getPermalink() {
    return permalink;
  }

  public void setPermalink(final String permalink) {
    this.permalink = permalink;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(final String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public String getVersioned() {
    return versioned;
  }

  public void setVersioned(final String versioned) {
    this.versioned = versioned;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(final String comment) {
    this.comment = comment;
  }

  public long getSize() {
    return size;
  }

  public void setSize(final long size) {
    this.size = size;
  }

  public long getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(final long creationDate) {
    this.creationDate = creationDate;
  }

  public long getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(final long updateDate) {
    this.updateDate = updateDate;
  }
}
