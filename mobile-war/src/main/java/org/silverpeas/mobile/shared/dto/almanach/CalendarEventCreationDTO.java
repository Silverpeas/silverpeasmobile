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

package org.silverpeas.mobile.shared.dto.almanach;

import org.silverpeas.mobile.shared.dto.reminder.ReminderDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author svu
 */
public class CalendarEventCreationDTO implements Serializable {

  private String eventType;
  private String occurrenceType;
  private String startDate;
  private String endDate;
  private String eventId;
  private CalendarDTO calendar;
  private String title;
  private boolean onAllDay;
  private String visibility;
  private String priority;
  private String description;
  private CalendarEventRecurrenceDTO recurrence;

  // "attributes": [{"name": "externalUrl"}],
  // attachmentParameters": []
  private List<CalendarEventAttendeeDTO> attendees;

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public String getOccurrenceType() {
    return occurrenceType;
  }

  public void setOccurrenceType(String occurrenceType) {
    this.occurrenceType = occurrenceType;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getEventId() {
    return eventId;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  public CalendarDTO getCalendar() {
    return calendar;
  }

  public void setCalendar(CalendarDTO calendar) {
    this.calendar = calendar;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean getOnAllDay() {
    return onAllDay;
  }

  public void setOnAllDay(boolean onAllDay) {
    this.onAllDay = onAllDay;
  }

  public String getVisibility() {
    return visibility;
  }

  public void setVisibility(String visibility) {
    this.visibility = visibility;
  }

  public String getPriority() {
    return priority;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public CalendarEventRecurrenceDTO getRecurrence() {
    return recurrence;
  }

  public void setRecurrence(CalendarEventRecurrenceDTO recurrence) {
    this.recurrence = recurrence;
  }

  public List<CalendarEventAttendeeDTO> getAttendees() {
    return attendees;
  }

  public void setAttendees(List<CalendarEventAttendeeDTO> attendees) {
    this.attendees = attendees;
  }
}
