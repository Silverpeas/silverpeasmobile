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

package org.silverpeas.mobile.client.apps.agenda.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.agenda.events.app.RemindersLoadEvent;
import org.silverpeas.mobile.client.apps.agenda.events.pages.AbstractEventPagesEvent;
import org.silverpeas.mobile.client.apps.agenda.events.pages.EventPagesEventHandler;
import org.silverpeas.mobile.client.apps.agenda.events.pages.RemindersLoadedEvent;
import org.silverpeas.mobile.client.apps.agenda.resources.AgendaMessages;
import org.silverpeas.mobile.client.apps.favorites.pages.widgets.AddToFavoritesButton;
import org.silverpeas.mobile.client.common.DateUtil;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.almanach.CalendarDTO;
import org.silverpeas.mobile.shared.dto.almanach.CalendarEventAttributeDTO;
import org.silverpeas.mobile.shared.dto.almanach.CalendarEventDTO;
import org.silverpeas.mobile.shared.dto.reminder.ReminderDTO;

import java.util.Date;

/**
 * @author svu
 */
public class EventPage  extends PageContent implements EventPagesEventHandler {
  private static EventPageUiBinder uiBinder = GWT.create(EventPageUiBinder.class);

  @UiField(provided = true) protected AgendaMessages msg = null;

  @UiField
  HTMLPanel container;

  @UiField
  HeadingElement header;
  @UiField
  ParagraphElement description;

  @UiField
  DivElement reminder, location, dateEvent;
  @UiField
  Anchor delete, link, content;
  @UiField
  ListBox reminderDurations;

  @UiField
  ActionsMenu actionsMenu;

  private CalendarEventDTO event;
  private CalendarDTO calendar;

  private DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mmZZZ");
  private DateTimeFormat df = DateTimeFormat.getFormat("yyyy-MM-dd");

  private DateTimeFormat displayDf = DateTimeFormat.getFormat("dd/MM/yyyy");
  private DateTimeFormat displayDtf = DateTimeFormat.getFormat("HH:mm");

  private AddToFavoritesButton favorite = new AddToFavoritesButton();

  interface EventPageUiBinder extends UiBinder<Widget, EventPage> {
  }

  public EventPage() {
    msg = GWT.create(AgendaMessages.class);
    setPageTitle(msg.titleEvent());
    initWidget(uiBinder.createAndBindUi(this));
    container.getElement().setId("event");
    dateEvent.setId("date-event");
    EventBus.getInstance().addHandler(AbstractEventPagesEvent.TYPE, this);
  }

  public void setData(CalendarEventDTO event, CalendarDTO calendar) {
    EventBus.getInstance().fireEvent(new RemindersLoadEvent(event));
    this.event = event;
    this.calendar = calendar;
    setPageTitle(calendar.getTitle());
    header.setInnerText(event.getTitle());
    description.setInnerText(event.getDescription());
    location.setInnerText(event.getLocation());

    if (event.isOnAllDay()) {
      Date start = df.parse(event.getStartDate());
      Date end = df.parse(event.getEndDate());
      end = DateUtil.addDays(end, -1);
      if (DateUtil.isSameDate(start, end)) {
        dateEvent.setInnerText(displayDf.format(start));
      } else {
        dateEvent.setInnerText(msg.from() + " " + displayDf.format(start) + " " + msg.toDay() + " " + displayDf.format(end));
      }
    } else {
      Date start = dtf.parse(event.getStartDate());
      Date end = dtf.parse(event.getEndDate());
      dateEvent.setInnerText(displayDf.format(start)+ " - " + displayDtf.format(start) + " " + msg.to() + " " + displayDtf.format(end));
    }

    for (CalendarEventAttributeDTO attr : event.getAttributes()) {
      if (attr.getName().equalsIgnoreCase("externalUrl")) {
        link.setHref(attr.getValue());
        link.setText(attr.getValue());
        break;
      }
    }

    //TODO : recurrence
    //TODO : attachment
    //TODO : attendee
  }

  @Override
  public void onRemindersLoaded(final RemindersLoadedEvent event) {
    if (!event.getReminders().isEmpty()) {
      ReminderDTO r = event.getReminders().get(0);
      reminderDurations.addItem(""+r.getDuration());
    } else {
      reminder.getStyle().setDisplay(Style.Display.NONE);
    }
  }

  @UiHandler("content")
  protected void showContent(ClickEvent event) {
    //TODO : show content
    Window.alert("TODO");
  }
}