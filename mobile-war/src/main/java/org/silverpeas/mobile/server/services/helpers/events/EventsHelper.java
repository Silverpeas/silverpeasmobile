/*
 * Copyright (C) 2000 - 2025 Silverpeas
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

package org.silverpeas.mobile.server.services.helpers.events;

import org.silverpeas.components.almanach.AlmanachSettings;
import org.silverpeas.core.calendar.Priority;
import org.silverpeas.core.util.DateUtil;
import org.silverpeas.core.webapi.calendar.CalendarResourceURIs;
import org.silverpeas.core.webapi.calendar.CalendarWebManager;
import org.silverpeas.mobile.shared.dto.almanach.CalendarEventDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;

/**
 * @author: svu
 */
public class EventsHelper {

  private static EventsHelper instance;

  public static EventsHelper getInstance() {
    if (instance == null) {
      instance = new EventsHelper();
    }
    return instance;
  }

  public NextEvents getNextEvents(List<String> allowedComponentIds, boolean includeToday,
      int nbDays, boolean onlyImportant) {
    List<NextEventsDate> result = new ArrayList<>();
    if (!allowedComponentIds.isEmpty()) {
      final CalendarResourceURIs uri = CalendarResourceURIs.get();
      List<CalendarEventOccurrenceEntity> events =
          CalendarWebManager.get(allowedComponentIds.get(0))
              .getNextEventOccurrences(allowedComponentIds, emptySet(), emptySet(), emptySet(),
                  AlmanachSettings.getZoneId(),
                  AlmanachSettings.getNbOccurrenceLimitOfShortNextEventView())
              .map(o -> {
                final CalendarEventOccurrenceEntity entity = new CalendarEventOccurrenceEntity(o);
                entity.withOccurrencePermalinkUrl(uri.ofOccurrencePermalink(o));
                return entity;
              })
              .collect(Collectors.toList());

      result = filterNextEvents(events, nbDays, includeToday, onlyImportant);
    }
    NextEvents nextEvents = new NextEvents(result);
    if (allowedComponentIds.size() == 1) {
      nextEvents.setUniqueAppId(allowedComponentIds.get(0));
    }
    return nextEvents;
  }

  private List<NextEventsDate> filterNextEvents(final List<CalendarEventOccurrenceEntity> events,
      final int nbDays, final boolean includeToday, final boolean onlyImportant) {
    Date date = null;
    Date today = new Date();
    NextEventsDate nextEventsDate = null;
    List<NextEventsDate> filteredEventDates = new ArrayList<>();
    for (CalendarEventOccurrenceEntity event : events) {
      Date eventDate = event.getStartDateAsDate();
      if ((!onlyImportant || Priority.HIGH == event.getPriority()) &&
          (includeToday || DateUtil.compareTo(today, eventDate, true) != 0)) {
        if (date == null || DateUtil.compareTo(date, eventDate, true) != 0) {
          nextEventsDate = new NextEventsDate(eventDate);
          if (filteredEventDates.size() == nbDays) {
            break;
          }
          filteredEventDates.add(nextEventsDate);
          date = eventDate;
        }
        nextEventsDate.addEvent(event);
      }
    }
    return filteredEventDates;
  }

  public List<CalendarEventDTO> populate(NextEvents events, String lang) {
    List<CalendarEventDTO> eventsToDisplay = new ArrayList<>();
    for (NextEventsDate eventDate : events.getNextEventsDates()) {
      for (Event event : eventDate.getEvents()) {
        CalendarEventDTO dto = new CalendarEventDTO();
        dto.setTitle(event.getDetail().getTitle());
        SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy");
        if (lang.equalsIgnoreCase("fr")) {
          fmt = new SimpleDateFormat("dd/MM/yyyy");
        }

        String u = event.getDetail().getOccurrencePermalinkUrl().getPath();
        u = u.substring(u.lastIndexOf("/")+1);
        dto.setEventId(u);
        dto.setCalendarId(event.getDetail().getInstanceId());
        dto.setStartDate(fmt.format(event.getDetail().getStartDateAsDate()));
        dto.setEndDate(fmt.format(event.getDetail().getEndDateAsDate()));
        dto.setOnAllDay(event.getDetail().isOnAllDay());
        eventsToDisplay.add(dto);
      }
    }
    return eventsToDisplay;
  }

}
