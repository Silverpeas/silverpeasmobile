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

package org.silverpeas.mobile.shared.dto.classifieds;

import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.FormFieldDTO;

import java.util.List;

/**
 * @author svu
 */
public class ClassifiedDTO extends BaseDTO {
  private String title;
  private String description;
  private String price;
  private String type;
  private String category;
  private List<String> pictures;
  private String creatorName;
  private String creatorId;
  private String creationDate;
  private String updateDate;
  private int commentsNumber;
  private List<FormFieldDTO> fields;

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

  public String getPrice() {
    return price;
  }

  public void setPrice(final String price) {
    this.price = price;
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(final String category) {
    this.category = category;
  }

  public List<String> getPictures() {
    return pictures;
  }

  public void setPictures(final List<String> pictures) {
    this.pictures = pictures;
  }

  public String getCreatorName() {
    return creatorName;
  }

  public void setCreatorName(final String creatorName) {
    this.creatorName = creatorName;
  }

  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(final String creationDate) {
    this.creationDate = creationDate;
  }

  public String getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(final String updateDate) {
    this.updateDate = updateDate;
  }

  public int getCommentsNumber() {
    return commentsNumber;
  }

  public void setCommentsNumber(final int commentsNumber) {
    this.commentsNumber = commentsNumber;
  }

  public String getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(final String creatorId) {
    this.creatorId = creatorId;
  }

  public List<FormFieldDTO> getFields() {
    return fields;
  }

  public void setFields(final List<FormFieldDTO> fields) {
    this.fields = fields;
  }
}
