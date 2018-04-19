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

package org.silverpeas.mobile.shared.dto.almanach;

import java.io.Serializable;

/**
 * @author svu
 */
public class CalendarEventDTO implements Serializable {

  private String eventId;
  private String calendarId;
  private String calendarZoneId;
  private String title;
  private String description;
  private String content;
  private String location;
  private boolean onAllDay;
  private String startDate;
  private String endDate;
  //private VisibilityLevel visibility;
  //private Priority priority;
  //private CalendarEventRecurrenceEntity recurrence;
  //private List<CalendarEventAttendeeEntity> attendees = new ArrayList<>();
  //private List<CalendarEventAttributeEntity> attributes = new ArrayList<>();
  private String ownerName;
  //private Date createDate;
  private String createdById;
  //private Date lastUpdateDate;
  private String lastUpdatedById;
  private boolean canBeAccessed;
  private boolean canBeModified;
  private boolean canBeDeleted;

  public String getEventId() {
    return eventId;
  }

  public void setEventId(final String eventId) {
    this.eventId = eventId;
  }

  public String getCalendarId() {
    return calendarId;
  }

  public void setCalendarId(final String calendarId) {
    this.calendarId = calendarId;
  }

  public String getCalendarZoneId() {
    return calendarZoneId;
  }

  public void setCalendarZoneId(final String calendarZoneId) {
    this.calendarZoneId = calendarZoneId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public String getContent() {
    return content;
  }

  public void setContent(final String content) {
    this.content = content;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(final String location) {
    this.location = location;
  }

  public boolean isOnAllDay() {
    return onAllDay;
  }

  public void setOnAllDay(final boolean onAllDay) {
    this.onAllDay = onAllDay;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(final String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(final String endDate) {
    this.endDate = endDate;
  }

  public String getOwnerName() {
    return ownerName;
  }

  public void setOwnerName(final String ownerName) {
    this.ownerName = ownerName;
  }

  public String getCreatedById() {
    return createdById;
  }

  public void setCreatedById(final String createdById) {
    this.createdById = createdById;
  }

  public String getLastUpdatedById() {
    return lastUpdatedById;
  }

  public void setLastUpdatedById(final String lastUpdatedById) {
    this.lastUpdatedById = lastUpdatedById;
  }

  public boolean isCanBeAccessed() {
    return canBeAccessed;
  }

  public void setCanBeAccessed(final boolean canBeAccessed) {
    this.canBeAccessed = canBeAccessed;
  }

  public boolean isCanBeModified() {
    return canBeModified;
  }

  public void setCanBeModified(final boolean canBeModified) {
    this.canBeModified = canBeModified;
  }

  public boolean isCanBeDeleted() {
    return canBeDeleted;
  }

  public void setCanBeDeleted(final boolean canBeDeleted) {
    this.canBeDeleted = canBeDeleted;
  }
  //private List<AttachmentParameterEntity> attachmentParameters = new ArrayList<>();
  //private ReminderEntity reminder;
  //private PdcClassificationEntity pdcClassification;
}
