/*
 * Copyright (C) 2000 - 2022 Silverpeas
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NextEventsDate {
  
  private Date date;
  private List<Event> events = new ArrayList<>();
  private Calendar calendar;
  
  public NextEventsDate(Date date) {
    this.date = date;
  }

  public void addEvent(CalendarEventOccurrenceEntity event) {
    events.add(new Event(event));
  }

  public Date getDate() {
    return date;
  }

  public List<Event> getEvents() {
    return events;
  }

  public int getDayInMonth() {
    initCalendar();
    return calendar.get(Calendar.DATE);
  }

  public int getMonth() {
    initCalendar();
    return calendar.get(Calendar.MONTH)+1;
  }

  private void initCalendar() {
    if (calendar == null) {
      calendar = Calendar.getInstance();
      calendar.setTime(date);
    }
  }

}