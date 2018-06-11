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

package org.silverpeas.mobile.client.apps.agenda.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.agenda.pages.EventPage;
import org.silverpeas.mobile.client.apps.agenda.resources.AgendaMessages;
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
  HTMLPanel container;

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
    DateTimeFormat df = DateTimeFormat.getFormat(msg.dateFormat());



    String start = df.format(dtf.parse(event.getStartDate())) ;
    String end = df.format(dtf.parse(event.getEndDate()));


    String date = "<span>" + msg.from() + " <strong>"+start+"</strong></span> <span>" + msg.toDay() + " "+end+"</span>";
    eventDate.setInnerHTML(date);
  }

  public String getYear() {
    String year;
    DateTimeFormat df = DateTimeFormat.getFormat("yyyy-MM-dd");
    Date d = df.parse(event.getStartDate());
    DateTimeFormat yf = DateTimeFormat.getFormat("yyyy");
    year = yf.format(d);
    return year;
  }

  @UiHandler("infos")
  void openEvent(ClickEvent event) {
    EventPage page = new EventPage();
    page.setData(this.instance, this.event, calendar);
    page.show();
  }
}
