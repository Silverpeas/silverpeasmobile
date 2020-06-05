/*
 * Copyright (C) 2000 - 2020 Silverpeas
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

package org.silverpeas.mobile.shared.dto.formsonline;

import org.silverpeas.mobile.shared.dto.FormFieldDTO;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * @author svu
 */
public class FormRequestDTO implements Serializable {

  public static final int STATE_UNREAD = 1;
  public static final int STATE_READ = 2;
  public static final int STATE_VALIDATED = 3;
  public static final int STATE_REFUSED = 4;
  public static final int STATE_ARCHIVED = 5;

  private String id;
  private String comments;
  private String title;
  private String description;
  private String creator;
  private String creationDate;
  private int state;
  private String stateLabel;
  private String formId;
  private List<FormFieldDTO> data;

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(final String comments) {
    this.comments = comments;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    if (title == null) {
      this.title = "";
    } else {
      this.title = title;
    }
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    if (description == null) {
      this.description = "";
    } else {
      this.description = description;
    }
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

  public int getState() {
    return state;
  }

  public void setState(final int state) {
    this.state = state;
  }

  public List<FormFieldDTO> getData() {
    return data;
  }

  public void setData(final List<FormFieldDTO> data) {
    this.data = data;
  }

  public String getFormId() {
    return formId;
  }

  public void setFormId(final String formId) {
    this.formId = formId;
  }

  public String getStateLabel() {
    return stateLabel;
  }

  public void setStateLabel(final String stateLabel) {
    this.stateLabel = stateLabel;
  }
}
