/*
 * Copyright (C) 2000 - 2022 Silverpeas
 *
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
 * "https://www.silverpeas.org/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.shared.dto.media;

import org.silverpeas.mobile.shared.dto.BaseDTO;

import java.io.Serializable;

public class MediaDTO extends BaseDTO implements Serializable {

  private static final long serialVersionUID = 1L;
  private boolean download;
  private String title;
  private String name;
  private String updateDate;
  private String updater;
  private String creator;
  private String creationDate;
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

  public String getCreator() {
    return creator;
  }

  public void setCreator(final String creator) {
    this.creator = creator;
  }

  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(final String creationDate) {
    this.creationDate = creationDate;
  }
}
