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

package org.silverpeas.mobile.shared.dto.news;

import org.silverpeas.mobile.shared.dto.BaseDTO;

import java.io.Serializable;

public class NewsDTO extends BaseDTO implements Serializable {

  private static final long serialVersionUID = 2921606984249560882L;

  private String idNews;
  private String title;
  private String description;
  private String updateDate;
  private String vignette;
  private String instanceId;

  private String content;

  private boolean important;
  private boolean draft;
  private boolean visible;

  private boolean managable = false;

  public NewsDTO() {
  }

  @Override
  public boolean equals(Object obj) {
    return ((NewsDTO) obj).getId().equals(getId());
  }

  public String getIdNews() {
    return idNews;
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

  public boolean getManagable() {
    return managable;
  }

  public void setManagable(final boolean managable) {
    this.managable = managable;
  }


  public boolean getVisible() {
    return visible;
  }

  public void setVisible(final boolean visible) {
    this.visible = visible;
  }

  public boolean getDraft() {
    return draft;
  }

  public void setDraft(final boolean draft) {
    this.draft = draft;
  }

  public void setIdNews(final String idNews) {
    this.idNews = idNews;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public boolean getImportant() {
    return important;
  }

  public void setImportant(boolean important) {
    this.important = important;
  }
}
