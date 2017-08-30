/*
 * Copyright (C) 2000 - 2017 Silverpeas
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
 *
 */

package com.silverpeas.mobile.shared.dto.blog;

import com.silverpeas.mobile.shared.dto.BaseDTO;

import java.io.Serializable;

public class PostDTO extends BaseDTO implements Serializable {

  private static final long serialVersionUID = 2921606984249560882L;

  private String title;
  private String description;
  private String categoryId;
  private String categoryName;
  private String creatorName;
  private String dateEvent;
  private String creationDate;

  public PostDTO() {
  }

  @Override
  public boolean equals(Object obj) {
    return ((PostDTO) obj).getId().equals(getId());
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public void setCategoryId(final String categoryId) {
    this.categoryId = categoryId;
  }

  public void setCategoryName(final String categoryName) {
    this.categoryName = categoryName;
  }

  public String getCategoryId() {
    return categoryId;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCreatorName(final String creatorName) {
    this.creatorName = creatorName;
  }

  public String getCreatorName() {
    return creatorName;
  }

  public void setDateEvent(final String dateEvent) {
    this.dateEvent = dateEvent;
  }

  public void setCreationDate(final String creationDate) {
    this.creationDate = creationDate;
  }

  public String getDateEvent() {
    return dateEvent;
  }

  public String getCreationDate() {
    return creationDate;
  }
}
