/*
 * Copyright (C) 2000 - 2021 Silverpeas
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

package org.silverpeas.mobile.shared.dto.documents;

import org.silverpeas.mobile.shared.dto.BaseDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PublicationDTO extends BaseDTO implements Serializable, Comparable<PublicationDTO> {

  private static final long serialVersionUID = 1L;
  private String name;
  private String description;
  private String version;
  private String creator;
  private String creationDate;
  private String updater;
  private String updateDate;
  private int commentsNumber = 0;
  private String instanceId;
  private boolean content;
  private List<PublicationDTO> linkedPublications;
  private String vignette;
  private int viewsNumber;

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  @Override
  public int compareTo(PublicationDTO o) {
    return name.compareTo(o.getName());
  }

  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getVersion() {
    return version;
  }
  public void setVersion(String version) {
    this.version = version;
  }
  public String getCreator() {
    return creator;
  }
  public void setCreator(String creator) {
    this.creator = creator;
  }
  public String getUpdater() {
    return updater;
  }
  public void setUpdater(String updater) {
    this.updater = updater;
  }
  public String getUpdateDate() {
    return updateDate;
  }
  public void setUpdateDate(String updateDate) {
    this.updateDate = updateDate;
  }

  public int getCommentsNumber() {
    return commentsNumber;
  }

  public void setCommentsNumber(final int commentsNumber) {
    this.commentsNumber = commentsNumber;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(final String instanceId) {
    this.instanceId = instanceId;
  }

  public void setContent(boolean content) {
    this.content = content;
  }

  public boolean getContent() {
    return content;
  }

  public List<PublicationDTO> getLinkedPublications() {
    return linkedPublications;
  }

  public void setLinkedPublications(final List<PublicationDTO> linkedPublications) {
    this.linkedPublications = linkedPublications;
  }

  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(final String creationDate) {
    this.creationDate = creationDate;
  }

  public String getVignette() { return vignette; }

  public void setVignette(final String vignette) { this.vignette = vignette; }

  public int getViewsNumber() { return viewsNumber; }

  public void setViewsNumber(final int viewsNumber) { this.viewsNumber = viewsNumber; }
}
