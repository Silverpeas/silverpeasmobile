/*
 * Copyright (C) 2000 - 2026 Silverpeas
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

package org.silverpeas.mobile.shared.services.rest;

import elemental2.core.Global;
import elemental2.core.JsArray;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.client.common.network.rest.RestCallback;
import org.silverpeas.mobile.shared.StreamingList;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationBoxDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationReceivedDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationSendedDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationToSendDTO;
import org.silverpeas.mobile.shared.helpers.UserFieldHelper;

import java.util.List;

/**
 * Service to manage requests related to notifications.
 * @author svu
 */
public class ServiceNotifications extends AbstractService {

  private static final String PATH = "/silverpeas/services/mobile/notification";

  /**
   * Retrieves allowed users and groups for a given component and content.
   * @param componentId The ID of the component.
   * @param contentId The ID of the content.
   * @param callback The callback to handle the response (list of BaseDTO).
   */
  public void getAllowedUsersAndGroups(String componentId, String contentId, RestCallback<List<BaseDTO>> callback) {
    String url = PATH + "/allowedUsersAndGroups/" + encode(componentId) + "/" + encode(contentId);
    get(url, result -> mapArray(result, UserFieldHelper::userFieldFromJSON), callback);
  }

  /**
   * Marks a notification as read by its ID.
   * @param id The ID of the notification to mark as read.
   * @param callback The callback to handle the response (no data returned).
   */
  public void markAsReaden(long id, RestCallback<Void> callback) {
    String url = PATH + "/readed/" + id;
    put(url, null, result -> null, callback);
  }

  /**
   * Marks a list of notifications as read.
   * @param selection The list of notifications to mark as read.
   * @param callback The callback to handle the response (no data returned).
   */
  public void markAsRead(List<NotificationBoxDTO> selection, RestCallback<Void> callback) {
    String url = PATH + "/readed/";

    JsArray<Object> array = new JsArray<>();
    for (NotificationBoxDTO sel : selection) {
      array.push(sel.toJSON());
    }
    String payload = Global.JSON.stringify(array);

    put(
            url,
            payload,
            result -> null,
            callback
    );
  }

  /**
   * Deletes a list of notifications.
   * @param selection The list of notifications to delete.
   * @param callback The callback to handle the response (no data returned).
   */
  public void delete(List<NotificationBoxDTO> selection, RestCallback<Void> callback) {
    String url = PATH;

    JsArray<Object> array = new JsArray<>();
    for (NotificationBoxDTO sel : selection) {
      array.push(sel.toJSON());
    }
    String payload = Global.JSON.stringify(array);

    delete(
            url,
            payload,
            result -> null,
            callback
    );
  }

  /**
   * Sends a notification.
   * @param notification The notification to send.
   * @param callback The callback to handle the response (no data returned).
   */
  public void send(NotificationToSendDTO notification, RestCallback<Void> callback) {
    String url = PATH + "/send/";
    put(
            url,
            Global.JSON.stringify(Js.asAny(notification.toJSON())),
            result -> null,
            callback
    );
  }

  /**
   * Retrieves sent notifications for the current user.
   * @param callNumber The call number for pagination.
   * @param callback The callback to handle the response (StreamingList of NotificationSendedDTO).
   */
  public void getUserSendedNotifications(int callNumber, RestCallback<StreamingList<NotificationSendedDTO>> callback) {
    String url = PATH + "/sended/" + callNumber;
    get(url, result -> StreamingList.fromJSON((JsPropertyMap<Object>) result), callback);
  }

  /**
   * Retrieves received notifications for the current user.
   * @param callNumber The call number for pagination.
   * @param callback The callback to handle the response (StreamingList of NotificationReceivedDTO).
   */
  public void getUserNotifications(int callNumber, RestCallback<StreamingList<NotificationReceivedDTO>> callback) {
    String url = PATH + "/received/" + callNumber;
    get(url, result -> StreamingList.fromJSON((JsPropertyMap<Object>) result), callback);
  }
}