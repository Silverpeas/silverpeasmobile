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

import com.google.gwt.user.client.Window;
import elemental2.core.Global;
import elemental2.core.JsArray;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.client.common.network.rest.RestCallback;
import org.silverpeas.mobile.shared.dto.reminder.ReminderDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Service to manage requests related to reminders.
 * @author svu
 */
public class ServiceReminder extends AbstractService {

  private static final String PATH = "/silverpeas/services/reminder";

  /**
   * Retrieves possible durations for a reminder.
   * @param componentInstanceId The ID of the component instance.
   * @param type The type of the reminder.
   * @param localId The local ID.
   * @param property The property for which to get possible durations.
   * @param callback The callback to handle the response (list of strings).
   */
  public void getPossibleDurations(
          String componentInstanceId,
          String type,
          String localId,
          String property,
          RestCallback<List<String>> callback) {
    String url = PATH + "/" + encode(componentInstanceId) + "/" +
            encode(type) + "/" + encode(localId) +
            "/possibledurations/" + encode(property);
    get(url, result -> mapArrayToListOfStrings(result), callback);
  }

  /**
   * Retrieves reminders for a given component instance, type, and local ID.
   * @param componentInstanceId The ID of the component instance.
   * @param type The type of the reminder.
   * @param localId The local ID.
   * @param callback The callback to handle the response (list of ReminderDTO).
   */
  public void getReminders(
          String componentInstanceId,
          String type,
          String localId,
          RestCallback<List<ReminderDTO>> callback) {
    String url = PATH + "/" + encode(componentInstanceId) + "/" +
            encode(type) + "/" + encode(localId);
    get(url, result -> mapArray(result, ReminderDTO::fromJSON), callback);
  }

  /**
   * Creates a new reminder.
   * @param componentInstanceId The ID of the component instance.
   * @param type The type of the reminder.
   * @param localId The local ID.
   * @param reminderDTO The reminder data to create.
   * @param callback The callback to handle the response (ReminderDTO).
   */
  public void createReminder(
          String componentInstanceId,
          String type,
          String localId,
          ReminderDTO reminderDTO,
          RestCallback<ReminderDTO> callback) {
    String url = PATH + "/" + encode(componentInstanceId) + "/" +
            encode(type) + "/" + encode(localId);
    post(
            url,
            Global.JSON.stringify(Js.asAny(reminderDTO.toJSON())),
            result -> ReminderDTO.fromJSON((JsPropertyMap<Object>) result),
            callback
    );
  }

  /**
   * Deletes a reminder.
   * @param componentInstanceId The ID of the component instance.
   * @param type The type of the reminder.
   * @param localId The local ID.
   * @param id The ID of the reminder to delete.
   * @param callback The callback to handle the response (no data returned).
   */
  public void deleteReminder(
          String componentInstanceId,
          String type,
          String localId,
          String id,
          RestCallback<Void> callback) {
    String url = PATH + "/" + encode(componentInstanceId) + "/" +
            encode(type) + "/" + encode(localId) + "/" + encode(id);
    delete(url, null, result -> null, callback);
  }

  /**
   * Updates an existing reminder.
   * @param componentInstanceId The ID of the component instance.
   * @param type The type of the reminder.
   * @param localId The local ID.
   * @param id The ID of the reminder to update.
   * @param reminderDTO The reminder data to update.
   * @param callback The callback to handle the response (ReminderDTO).
   */
  public void updateReminder(
          String componentInstanceId,
          String type,
          String localId,
          String id,
          ReminderDTO reminderDTO,
          RestCallback<ReminderDTO> callback) {
    String url = PATH + "/" + encode(componentInstanceId) + "/" +
            encode(type) + "/" + encode(localId) + "/" + encode(id);
    put(
            url,
            Global.JSON.stringify(Js.asAny(reminderDTO.toJSON())),
            result -> ReminderDTO.fromJSON((JsPropertyMap<Object>) result),
            callback
    );
  }

  private List<String> mapArrayToListOfStrings(Object json) {
      JsArray<Object> list = (JsArray<Object>) json;
      List<String> result = new ArrayList<>();
      for (int i = 0; i < list.length; i++) {
        result.add((String) list.getAt(i));
      }
      return result;
  }
}