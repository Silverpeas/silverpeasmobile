/*
 * Copyright (C) 2000 - 2024 Silverpeas
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

package org.silverpeas.mobile.client.apps.agenda.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.agenda.pages.EventPage;
import org.silverpeas.mobile.client.apps.agenda.resources.AgendaMessages;
import org.silverpeas.mobile.client.common.DateUtil;
import org.silverpeas.mobile.client.components.HTMLPanelClickable;
import org.silverpeas.mobile.shared.dto.almanach.CalendarDTO;
import org.silverpeas.mobile.shared.dto.almanach.CalendarEventDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

import java.util.Date;

public class EventItem extends Composite {

  private ApplicationInstanceDTO instance;
  private CalendarEventDTO event;
  private CalendarDTO calendar;
  private static EventItemUiBinder uiBinder = GWT.create(EventItemUiBinder.class);
  @UiField
  DivElement eventName, calendarName, eventDate;

  @UiField
  HTMLPanelClickable container;

  @UiField
  HTML infos;

  protected AgendaMessages msg = null;

  interface EventItemUiBinder extends UiBinder<Widget, EventItem> {}

  public EventItem() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(AgendaMessages.class);
  }

  public void setData(ApplicationInstanceDTO instance, CalendarEventDTO event, CalendarDTO calendar) {
    this.instance = instance;
    this.event = event;
    this.calendar = calendar;
    String attendee = "";
    if (!event.getAttendees().isEmpty()) {
      attendee = event.getAttendees().get(0).getFullName();
    }
    eventName.setInnerText(event.getTitle());
    calendarName.setInnerText(calendar.getTitle());

    DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd");
    if(event.getStartDate().contains("T")) {
      dtf = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mmZZZ");
    }
    Date startDate = dtf.parse(event.getStartDate());
    Date endDate = dtf.parse(event.getEndDate());

    if (event.isOnAllDay()) endDate.setTime(endDate.getTime()-1000);

    DateTimeFormat hoursformat = DateTimeFormat.getFormat("HH:mm");
    DateTimeFormat dayformat = DateTimeFormat.getFormat("EEEE");
    DateTimeFormat mouthformat = DateTimeFormat.getFormat("MMMM");
    DateTimeFormat df = DateTimeFormat.getFormat("dd");


    String startDateFormated = dayformat.format(startDate);
    String endDateFormated = dayformat.format(endDate);

    String startHourFormated = hoursformat.format(startDate);
    String endHourFormated = hoursformat.format(endDate);

    String date = "";
    if (startDateFormated.equals(endDateFormated) || (event.isOnAllDay() && isOnOneDay())) {
      String start = getDay(startDateFormated) + " " + df.format(startDate) + " " + getMouth(mouthformat.format(startDate));
      date = "<span>" + msg.the() + " <strong>" + start;
      if (!event.isOnAllDay()) date += " " + startHourFormated + " - " + endHourFormated;
      date += "</strong></span>";
    } else {
      String start = getDay(startDateFormated) + " " + df.format(startDate) + " " + getMouth(mouthformat.format(startDate));
      String end = getDay(endDateFormated) + " " + df.format(endDate) + " " + getMouth(mouthformat.format(endDate));
      date = "<span>" + msg.from() + " <strong>" + start;
      if (!event.isOnAllDay()) date += " " + startHourFormated;
      date += "</strong></span> <span>" + msg.toDay() + " " + end;
      if (!event.isOnAllDay()) date += " " + endHourFormated;
      date += "</span>";
    }

    eventDate.setInnerHTML(date);
  }

  private boolean isOnOneDay() {
      DateTimeFormat df = DateTimeFormat.getFormat("yyyy-MM-dd");
      Date start = df.parse(event.getStartDate());
      Date end = df.parse(event.getEndDate());
      end = DateUtil.addDays(end, -1);
      return (DateUtil.isSameDate(start, end));
  }

  public String getDay(String value) {
    String day = "";
    if (value.equalsIgnoreCase("monday")) {
      day = msg.monday();
    } else if (value.equalsIgnoreCase("tuesday")) {
      day = msg.tuesday();
    } else if (value.equalsIgnoreCase("wednesday")) {
      day = msg.wednesday();
    }  else if (value.equalsIgnoreCase("thursday")) {
      day = msg.thursday();
    } else if (value.equalsIgnoreCase("friday")) {
      day = msg.friday();
    } else if (value.equalsIgnoreCase("saturday")) {
      day = msg.saturday();
    } else if (value.equalsIgnoreCase("sunday")) {
      day = msg.sunday();
    }

    return day;
  }

  public String getMouth(String value) {
    String mouth = "";
    if (value.equalsIgnoreCase("january")) {
      mouth = msg.january();
    } else if(value.equalsIgnoreCase("february")) {
      mouth = msg.february();
    } else if(value.equalsIgnoreCase("march")) {
      mouth = msg.march();
    } else if(value.equalsIgnoreCase("april")) {
      mouth = msg.april();
    } else if(value.equalsIgnoreCase("may")) {
      mouth = msg.may();
    } else if(value.equalsIgnoreCase("june")) {
      mouth = msg.june();
    } else if(value.equalsIgnoreCase("july")) {
      mouth = msg.july();
    } else if(value.equalsIgnoreCase("august")) {
      mouth = msg.august();
    } else if(value.equalsIgnoreCase("september")) {
      mouth = msg.september();
    } else if(value.equalsIgnoreCase("october")) {
      mouth = msg.october();
    } else if(value.equalsIgnoreCase("november")) {
      mouth = msg.november();
    } else if(value.equalsIgnoreCase("december")) {
      mouth = msg.december();
    }

    return mouth;
  }

  public void showCalendarName(boolean show) {
    if (show) {
      calendarName.getStyle().clearVisibility();
    } else {
      calendarName.getStyle().setVisibility(Style.Visibility.HIDDEN);
    }
  }

  public String getYear() {
    String year;
    DateTimeFormat df = DateTimeFormat.getFormat("yyyy-MM-dd");
    if (event.getStartDate().contains("T")) {
      df = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mmZZZ");
    }
    Date d = df.parse(event.getStartDate());
    DateTimeFormat yf = DateTimeFormat.getFormat("yyyy");
    year = yf.format(d);
    return year;
  }

  @UiHandler("container")
  void openEvent(ClickEvent event) {
    EventPage page = new EventPage();
    page.setData(this.instance, this.event, calendar);
    page.show();
  }
}
