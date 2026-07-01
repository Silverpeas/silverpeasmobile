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

import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.client.common.network.rest.RestCallback;
import org.silverpeas.mobile.shared.dto.almanach.CalendarDTO;
import org.silverpeas.mobile.shared.dto.almanach.CalendarEventDTO;

import java.util.List;

/**
 * Service to manage requests related to user calendars.
 * @author svu
 */
public class ServiceUserCalendar extends AbstractService {

  private static final String PATH = "/silverpeas/services/usercalendar";

  /**
   * Retrieves calendars for a given component instance.
   * @param componentInstanceId The ID of the component instance.
   * @param callback The callback to handle the response (list of CalendarDTO).
   */
  public void getCalendars(String componentInstanceId, RestCallback<List<CalendarDTO>> callback) {
    String url = PATH + "/" + encode(componentInstanceId) + "/";
    get(url, result -> mapArray(result, CalendarDTO::fromJSON), callback);
  }

  /**
   * Retrieves occurrences of calendar events for a given calendar and time window.
   * @param componentInstanceId The ID of the component instance.
   * @param calendarId The ID of the calendar.
   * @param startDateOfWindowTime The start date of the time window.
   * @param endDateOfWindowTime The end date of the time window.
   * @param zoneid The timezone ID.
   * @param callback The callback to handle the response (list of CalendarEventDTO).
   */
  public void getOccurrences(
          String componentInstanceId,
          String calendarId,
          String startDateOfWindowTime,
          String endDateOfWindowTime,
          String zoneid,
          RestCallback<List<CalendarEventDTO>> callback) {
    String url = PATH + "/" + encode(componentInstanceId) + "/" + encode(calendarId) + "/events/occurrences/" +
            "?startDateOfWindowTime=" + encode(startDateOfWindowTime) +
            "&endDateOfWindowTime=" + encode(endDateOfWindowTime) +
            "&zoneid=" + encode(zoneid);
    get(url, result -> mapArray(result, CalendarEventDTO::fromJSON), callback);
  }

  /**
   * Retrieves a specific event from a calendar.
   * @param calendarId The ID of the calendar.
   * @param eventId The ID of the event.
   * @param callback The callback to handle the response (CalendarEventDTO).
   */
  public void getEvent(String calendarId, String eventId, RestCallback<CalendarEventDTO> callback) {
    String url = PATH + "/" + encode(calendarId) + "/events/" + encode(eventId);
    get(url, result -> CalendarEventDTO.fromJSON((JsPropertyMap<Object>) result), callback);
  }
}