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

package org.silverpeas.mobile.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.silverpeas.core.notification.sse.CDIServerEventListener;
import org.silverpeas.core.notification.user.UserNotificationServerEvent;
import org.silverpeas.core.notification.user.server.channel.silvermail.SILVERMAILMessage;
import org.silverpeas.core.notification.user.server.channel.silvermail.SILVERMAILPersistence;
import org.silverpeas.core.util.logging.SilverLogger;
import org.silverpeas.mobile.server.services.helpers.NotificationsPushHelper;

import java.util.Map;

/**
 * @author svu
 */
public class UserNotificationServerEventMobileListener extends
    CDIServerEventListener<UserNotificationServerEvent> {

  private static ObjectMapper mapper = new ObjectMapper();

  @SuppressWarnings("unchecked")
  @Override
  public void on(final UserNotificationServerEvent event) {
    try {
      String emitterUserId = event.getEmitterUserId();
      String json = event.getData("", null);
      Map<String, Object> data = mapper.readValue(json, Map.class);
      Boolean isCreation = (Boolean) data.get("isCreation");
      String id = (String) data.get("id");
      if (Boolean.TRUE.equals(isCreation)) {
        SILVERMAILMessage msg =  SILVERMAILPersistence.getMessage(event.getEmitterUserId(), Long.valueOf(id));
        data.put("body", Jsoup.parse(msg.getBody()).wholeText());
        NotificationsPushHelper.getInstance().sendNotification(emitterUserId, data);
      }
    } catch(Exception e) {
      SilverLogger.getLogger(this).error(e);
    }
  }
}
