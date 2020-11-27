/*
 * Copyright (C) 2000 - 2020 Silverpeas
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
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
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

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import org.silverpeas.mobile.shared.dto.almanach.CalendarDTO;
import org.silverpeas.mobile.shared.dto.almanach.CalendarEventAttendeeDTO;
import org.silverpeas.mobile.shared.dto.almanach.CalendarEventDTO;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * @author svu
 */
@Path("/almanach")
public interface ServiceAlmanach extends RestService {

  @GET
  @Path("{componentInstanceId}/")
  public void getCalendars(@PathParam("componentInstanceId") String componentInstanceId,
      MethodCallback<List<CalendarDTO>> callback);

  @GET
  @Path("{componentInstanceId}/{calendarId}/events/occurrences/")
  public void getOccurences(@PathParam("componentInstanceId") String componentInstanceId,
      @PathParam("calendarId") String calendarId,
      @QueryParam("startDateOfWindowTime") String startDateOfWindowTime,
      @QueryParam("endDateOfWindowTime") String endDateOfWindowTime,
      @QueryParam("zoneid") String zoneid, MethodCallback<List<CalendarEventDTO>> callback);

  @GET
  @Path("{calendarId}/events/{eventId}")
  public void getEvent(@PathParam("calendarId") String calendarId,
      @PathParam("eventId") String eventId, MethodCallback<CalendarEventDTO> callback);

  @PUT
  @Path("{componentInstanceId}/{calendarId}/events/{eventId}/occurrences/{occurenceId}/attendees" +
      "/{attendeeId}")
  public void updateParticipation(@PathParam("componentInstanceId") String componentInstanceId,
      @PathParam("calendarId") String calendarId,
      @PathParam("eventId") String eventId,
      @PathParam("occurenceId") String occurenceId,
      @PathParam("attendeeId") String attendeeId,
      @QueryParam("zoneid") String zoneid, CalendarEventAttendeeDTO dto,
      MethodCallback<CalendarEventDTO> callback);

}
