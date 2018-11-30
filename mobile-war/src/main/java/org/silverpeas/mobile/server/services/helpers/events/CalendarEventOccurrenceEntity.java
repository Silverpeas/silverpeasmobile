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

package org.silverpeas.mobile.server.services.helpers.events;

import org.silverpeas.core.calendar.Calendar;
import org.silverpeas.core.calendar.CalendarEventOccurrence;
import org.silverpeas.core.calendar.Priority;

import java.net.URI;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.silverpeas.core.calendar.CalendarEventUtil.formatDateWithOffset;

/**
 * @author silveryocha
 */
public class CalendarEventOccurrenceEntity {

  private final CalendarEventOccurrence occurrence;
  private URI occurrencePermalinkUrl;

  public CalendarEventOccurrenceEntity(final CalendarEventOccurrence occurrence) {
    this.occurrence = occurrence;
  }

  public Date getStartDateAsDate() {
    return asDate(formatStartDate());
  }

  public Date getEndDateAsDate() {
    return asDate(formatEndDate());
  }

  public Priority getPriority() {
    return occurrence.getPriority();
  }

  private Calendar getCalendar() {
    return occurrence.getCalendarEvent().getCalendar();
  }

  public URI getOccurrencePermalinkUrl() {
    return occurrencePermalinkUrl;
  }

  public void withOccurrencePermalinkUrl(URI occurrencePermalinkUrl) {
    this.occurrencePermalinkUrl = occurrencePermalinkUrl;
  }

  public String getTitle() {
    return occurrence.getTitle();
  }

  public boolean isOnAllDay() {
    return occurrence.isOnAllDay();
  }

  public String getLocation() {
    return occurrence.getLocation();
  }

  public String getInstanceId() {
    return getCalendar().getComponentInstanceId();
  }

  private Date asDate(final String isoDateAsString) {
    if (occurrence.isOnAllDay()) {
      final ZoneId zoneId = getCalendar().getZoneId();
      return Date.from(LocalDate.parse(isoDateAsString).atStartOfDay(zoneId).toInstant());
    } else {
      return Date.from(OffsetDateTime.parse(isoDateAsString).toInstant());
    }
  }

  private String formatStartDate() {
    return formatDateWithOffset(occurrence.asCalendarComponent(), occurrence.getStartDate(),
        getCalendar().getZoneId());
  }

  private String formatEndDate() {
    return formatDateWithOffset(occurrence.asCalendarComponent(), occurrence.getEndDate(),
        getCalendar().getZoneId());
  }
}
