/*
 * Copyright (C) 2000 - 2018 Silverpeas
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

package org.silverpeas.mobile.server.services;

import org.silverpeas.core.calendar.CalendarEventOccurrence;
import org.silverpeas.core.webapi.calendar.CalendarWebManager;
import org.silverpeas.mobile.shared.dto.EventDetailDTO;
import org.silverpeas.mobile.shared.exceptions.AgendaException;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.silverpeas.mobile.shared.services.ServiceAgenda;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServiceAgendaImpl extends AbstractAuthenticateService implements ServiceAgenda {

  private static final long serialVersionUID = 1L;

  public List<EventDetailDTO> getTodayEvents(String instanceId) throws AgendaException, AuthenticationException {
    checkUserInSession();
    List<EventDetailDTO> listEventDetailDTO = new ArrayList<>();

    org.silverpeas.core.calendar.Calendar c = org.silverpeas.core.calendar.Calendar.getByComponentInstanceId(instanceId).getMainCalendar().get();
    List<CalendarEventOccurrence> occurrences = CalendarWebManager.get(instanceId).getEventOccurrencesOf(LocalDate.now(), LocalDate.now(), Collections
        .singletonList(c));

    for (CalendarEventOccurrence occurrence : occurrences) {
      listEventDetailDTO.add(populate(occurrence));
    }

    return listEventDetailDTO;
  }

  private EventDetailDTO populate(final CalendarEventOccurrence occurrence) {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    EventDetailDTO dto = new EventDetailDTO();
    dto.setTitle(occurrence.getTitle());
    dto.setLocation(occurrence.getLocation());
    dto.setPriority(occurrence.getPriority().getICalLevel());

    dto.setStartDate(dtf.format(occurrence.getStartDate()));
    dto.setEndDate(dtf.format(occurrence.getEndDate()));

    dto.setAllDay(occurrence.isOnAllDay());
    dto.setDescription(occurrence.getDescription());
    dto.setVisibilityLevel(occurrence.getVisibilityLevel().name());

    return dto;
  }

}