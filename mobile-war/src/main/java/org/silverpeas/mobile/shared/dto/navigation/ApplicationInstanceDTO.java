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

package org.silverpeas.mobile.shared.dto.navigation;

import org.silverpeas.mobile.shared.dto.RightDTO;

import java.io.Serializable;
import java.util.Map;


public class ApplicationInstanceDTO extends SilverpeasObjectDTO implements Serializable {

  private String type;
  private RightDTO rights;
  private boolean commentable;
  private boolean notifiable;
  private boolean ableToStoreContent;
  private boolean workflow;
  private String extraId;
  private int folderSharing;
  private int publicationSharing;
  private int fileSharing;

  private Map<String, String> parameters;

  private boolean personnal;

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public RightDTO getRights() {
    return rights;
  }

  public void setRights(final RightDTO rights) {
    this.rights = rights;
  }

  public boolean getCommentable() {
    return commentable;
  }

  public void setCommentable(final boolean commentable) {
    this.commentable = commentable;
  }

  public boolean getNotifiable() {
    return notifiable;
  }

  public void setNotifiable(final boolean notifiable) {
    this.notifiable = notifiable;
  }

  public boolean getAbleToStoreContent() {
    return ableToStoreContent;
  }

  public void setAbleToStoreContent(final boolean ableToStoreContent) {
    this.ableToStoreContent = ableToStoreContent;
  }

  public boolean getWorkflow() {
    return workflow;
  }

  public void setWorkflow(final boolean workflow) {
    this.workflow = workflow;
  }

  public String getExtraId() {
    return extraId;
  }

  public void setExtraId(final String extraId) {
    this.extraId = extraId;
  }

  public void setFolderSharing(int folderSharing) { this.folderSharing = folderSharing; }
  public void setPublicationSharing(int publicationSharing) { this.publicationSharing = publicationSharing; }
  public void setFileSharing(int fileSharing) { this.fileSharing = fileSharing; }
  public int getFolderSharing() { return folderSharing; }
  public int getPublicationSharing() { return publicationSharing; }
  public int getFileSharing() { return fileSharing; }

  public boolean getPersonnal() {
    return personnal;
  }

  public void setPersonnal(boolean personnal) {
    this.personnal = personnal;
  }

  public void setParameters(Map<String, String> parameters) {
    this.parameters = parameters;
  }

  public Map<String, String> getParameters() {
    return parameters;
  }
}
