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

package org.silverpeas.mobile.shared.dto.comments;

import org.silverpeas.mobile.shared.dto.authentication.UserProfileDTO;

import java.io.Serializable;

/**
 * @author svu
 */
public class CommentDTO implements Serializable {

  public final static String TYPE_PUBLICATION = "Publication";
  public final static String TYPE_PHOTO = "Photo";
  public final static String TYPE_VIDEO = "Video";
  public final static String TYPE_SOUND = "Sound";
  public final static String TYPE_STREAMING = "Streaming";

  private String uri = "";
  private String id = "";
  private String componentId = "";
  private String resourceType = "";
  private String resourceId = "";
  private String text = "";
  private String textForHtml = "";
  private UserProfileDTO author;
  private String currentUserLanguage = "";
  private String creationDate = ""; //date in the format displayed for the current user
  private String modificationDate = ""; //date in the format displayed for the current user
  private boolean indexed = false;

  public String getUri() {
    return uri;
  }

  public void setUri(final String uri) {
    this.uri = uri;
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getComponentId() {
    return componentId;
  }

  public void setComponentId(final String componentId) {
    this.componentId = componentId;
  }

  public String getResourceType() {
    return resourceType;
  }

  public void setResourceType(final String resourceType) {
    this.resourceType = resourceType;
  }

  public String getResourceId() {
    return resourceId;
  }

  public void setResourceId(final String resourceId) {
    this.resourceId = resourceId;
  }

  public String getText() {
    return text;
  }

  public void setText(final String text) {
    this.text = text;
  }

  public String getTextForHtml() {
    return textForHtml;
  }

  public void setTextForHtml(final String textForHtml) {
    this.textForHtml = textForHtml;
  }

  public UserProfileDTO getAuthor() {
    return author;
  }

  public void setAuthor(final UserProfileDTO author) {
    this.author = author;
  }

  public String getCurrentUserLanguage() {
    return currentUserLanguage;
  }

  public void setCurrentUserLanguage(final String currentUserLanguage) {
    this.currentUserLanguage = currentUserLanguage;
  }

  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(final String creationDate) {
    this.creationDate = creationDate;
  }

  public String getModificationDate() {
    return modificationDate;
  }

  public void setModificationDate(final String modificationDate) {
    this.modificationDate = modificationDate;
  }

  public boolean isIndexed() {
    return indexed;
  }

  public void setIndexed(final boolean indexed) {
    this.indexed = indexed;
  }
}
