/*
 * Copyright (C) 2000 - 2024 Silverpeas
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

package org.silverpeas.mobile.shared.dto.notifications;

import org.silverpeas.mobile.shared.dto.BaseDTO;

import java.io.Serializable;

/**
 * @author: svu
 */
public class NotificationDTO implements Serializable {

  public final static String TYPE_PUBLICATION = "Publication";

  public final static String TYPE_DOCUMENT = "Document";
  public final static String TYPE_PHOTO = "Photo";
  public final static String TYPE_VIDEO = "Video";
  public final static String TYPE_SOUND = "Sound";
  public final static String TYPE_STREAMING = "Streaming";
  public final static String TYPE_EVENT = "Event";

  private String contentId;
  private String contentType;
  private String message;
  private String instanceId;

  public NotificationDTO() {
    super();
  }

  public NotificationDTO(String message) {
    this.message = message;
  }

  public String getContentId() {
    return contentId;
  }

  public void setContentId(String contentId) {
    this.contentId = contentId;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }
}
