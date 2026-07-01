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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import elemental2.core.JsArray;
import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.shared.dto.ShortCutLinkDTO;
import org.silverpeas.mobile.shared.dto.reminder.ReminderDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author svu
 */
public class CalendarEventDTO {

    private String eventUri;
    private String calendarUri;
    private String eventPermalinkUrl;
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
    private VisibilityLevelDTO visibility;
    private PriorityDTO priority;
    private CalendarEventRecurrenceDTO recurrence;
    private List<CalendarEventAttendeeDTO> attendees = new ArrayList<>();
    private List<CalendarEventAttributeDTO> attributes = new ArrayList<>();
    private String occurrenceId;
    //private PdcClassificationEntity pdcClassification;
    //private List<AttachmentParameterDTO> attachmentParameters = new ArrayList<>();
    private ReminderDTO reminder;
    private String ownerName;
    private String createDate;
    private String createdById;
    private String lastUpdateDate;
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

    public VisibilityLevelDTO getVisibility() {
        return visibility;
    }

    public void setVisibility(final VisibilityLevelDTO visibility) {
        this.visibility = visibility;
    }


    public PriorityDTO getPriority() {
        return priority;
    }

    public void setPriority(final PriorityDTO priority) {
        this.priority = priority;
    }

    public CalendarEventRecurrenceDTO getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(final CalendarEventRecurrenceDTO recurrence) {
        this.recurrence = recurrence;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(final String createDate) {
        this.createDate = createDate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(final String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public List<CalendarEventAttendeeDTO> getAttendees() {
        return attendees;
    }

    public void setAttendees(final List<CalendarEventAttendeeDTO> attendees) {
        this.attendees = attendees;
    }

    public ReminderDTO getReminder() {
        return reminder;
    }

    public void setReminder(final ReminderDTO reminder) {
        this.reminder = reminder;
    }

    public String getEventUri() {
        return eventUri;
    }

    public void setEventUri(final String eventUri) {
        this.eventUri = eventUri;
    }

    public String getCalendarUri() {
        return calendarUri;
    }

    public void setCalendarUri(final String calendarUri) {
        this.calendarUri = calendarUri;
    }

    public String getEventPermalinkUrl() {
        return eventPermalinkUrl;
    }

    public void setEventPermalinkUrl(final String eventPermalinkUrl) {
        this.eventPermalinkUrl = eventPermalinkUrl;
    }

    public List<CalendarEventAttributeDTO> getAttributes() {
        return attributes;
    }

    public void setAttributes(final List<CalendarEventAttributeDTO> attributes) {
        this.attributes = attributes;
    }

    public String getOccurrenceId() {
        return occurrenceId;
    }

    public void setOccurrenceId(final String occurrenceId) {
        this.occurrenceId = occurrenceId;
    }

    public static CalendarEventDTO fromJSON(JsPropertyMap<Object> json) {
        CalendarEventDTO dto = new CalendarEventDTO();

        if (json == null) {
            return dto;
        }
        try {
            dto.setEventUri(json.get("eventUri") != null ? json.get("eventUri").toString() : "");
            dto.setCalendarUri(json.get("calendarUri") != null ? json.get("calendarUri").toString() : "");
            dto.setEventPermalinkUrl(json.get("eventPermalinkUrl") != null ? json.get("eventPermalinkUrl").toString() : "");
            dto.setEventId(json.get("eventId") != null ? json.get("eventId").toString() : "");
            dto.setCalendarId(json.get("calendarId") != null ? json.get("calendarId").toString() : "");
            dto.setCalendarZoneId(json.get("calendarZoneId") != null ? json.get("calendarZoneId").toString() : "");
            dto.setTitle(json.get("title") != null ? json.get("title").toString() : "");
            dto.setDescription(json.get("description") != null ? json.get("description").toString() : "");
            dto.setContent(json.get("content") != null ? json.get("content").toString() : "");
            dto.setLocation(json.get("location") != null ? json.get("location").toString() : "");
            dto.setStartDate(json.get("startDate") != null ? json.get("startDate").toString() : "");
            dto.setEndDate(json.get("endDate") != null ? json.get("endDate").toString() : "");
            dto.setOwnerName(json.get("ownerName") != null ? json.get("ownerName").toString() : "");
            dto.setCreatedById(json.get("createdById") != null ? json.get("createdById").toString() : "");
            dto.setLastUpdatedById(json.get("lastUpdatedById") != null ? json.get("lastUpdatedById").toString() : "");
            dto.setCreateDate(json.get("createDate") != null ? json.get("createDate").toString() : "");
            dto.setLastUpdateDate(json.get("lastUpdateDate") != null ? json.get("lastUpdateDate").toString() : "");
            dto.setOccurrenceId(json.get("occurrenceId") != null ? json.get("occurrenceId").toString() : "");

            dto.setOnAllDay(json.get("onAllDay") != null ? Boolean.parseBoolean(json.get("onAllDay").toString()) : false);
            dto.setCanBeAccessed(json.get("canBeAccessed") != null ? Boolean.parseBoolean(json.get("canBeAccessed").toString()) : false);
            dto.setCanBeModified(json.get("canBeModified") != null ? Boolean.parseBoolean(json.get("canBeModified").toString()) : false);
            dto.setCanBeDeleted(json.get("canBeDeleted") != null ? Boolean.parseBoolean(json.get("canBeDeleted").toString()) : false);

            dto.setVisibility(json.get("visibility") != null ? VisibilityLevelDTO.valueOf(json.get("visibility").toString()) : null);
            dto.setPriority(json.get("priority") != null ? PriorityDTO.valueOf(json.get("priority").toString()) : null);

            dto.setRecurrence(CalendarEventRecurrenceDTO.fromJSON((JsPropertyMap<Object>) json.get("recurrence")));
            dto.setReminder(ReminderDTO.fromJSON((JsPropertyMap<Object>) json.get("reminder")));


            if (json.get("attendees") != null) {
                JsArray<Object> list = (JsArray<Object>) json.get("attendees");
                List<CalendarEventAttendeeDTO> result = new ArrayList<>();
                for (int i = 0; i < list.length; i++) {
                    JsPropertyMap<Object> map = (JsPropertyMap<Object>) list.getAt(i);
                    result.add(CalendarEventAttendeeDTO.fromJSON(map));
                }

                dto.setAttendees(result);
            }

            if (json.get("attributes") != null) {
                JsArray<Object> list = (JsArray<Object>) json.get("attributes");
                List<CalendarEventAttributeDTO> result = new ArrayList<>();
                for (int i = 0; i < list.length; i++) {
                    JsPropertyMap<Object> map = (JsPropertyMap<Object>) list.getAt(i);
                    result.add(CalendarEventAttributeDTO.fromJSON(map));
                }

                dto.setAttributes(result);
            }
            
        } catch (Throwable t) {
            GWT.log("ERREUR", t);
        }

        return dto;
    }

}
