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

package org.silverpeas.mobile.shared.dto.notifications;

import org.silverpeas.mobile.shared.dto.BaseDTO;

import java.io.Serializable;
import java.util.List;

/**
 * @author: svu
 */
public class NotificationToSendDTO implements Serializable {

  private NotificationDTO notification;
  private List<BaseDTO> receivers;
  private String subject;

  public NotificationDTO getNotification() {
    return notification;
  }

  public void setNotification(final NotificationDTO notification) {
    this.notification = notification;
  }

  public List<BaseDTO> getReceivers() {
    return receivers;
  }

  public void setReceivers(final List<BaseDTO> receivers) {
    this.receivers = receivers;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(final String subject) {
    this.subject = subject;
  }
}
