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
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.apps.agenda.events.app.AttachmentsLoadEvent;
import org.silverpeas.mobile.client.apps.agenda.events.app.ReminderDeleteEvent;
import org.silverpeas.mobile.client.apps.agenda.events.app.ReminderUpdateEvent;
import org.silverpeas.mobile.client.apps.agenda.events.app.RemindersLoadEvent;
import org.silverpeas.mobile.client.apps.agenda.events.pages.AbstractEventPagesEvent;
import org.silverpeas.mobile.client.apps.agenda.events.pages.AttachmentsLoadedEvent;
import org.silverpeas.mobile.client.apps.agenda.events.pages.EventPagesEventHandler;
import org.silverpeas.mobile.client.apps.agenda.events.pages.ReminderDeletedEvent;
import org.silverpeas.mobile.client.apps.agenda.events.pages.RemindersLoadedEvent;
import org.silverpeas.mobile.client.apps.agenda.pages.widgets.AttendeeItem;
import org.silverpeas.mobile.client.apps.agenda.resources.AgendaMessages;
import org.silverpeas.mobile.client.apps.favorites.pages.widgets.AddToFavoritesButton;
import org.silverpeas.mobile.client.common.DateUtil;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.navigation.UrlUtils;
import org.silverpeas.mobile.client.components.IframePage;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.attachments.Attachment;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.almanach.CalendarDTO;
import org.silverpeas.mobile.shared.dto.almanach.CalendarEventAttendeeDTO;
import org.silverpeas.mobile.shared.dto.almanach.CalendarEventAttributeDTO;
import org.silverpeas.mobile.shared.dto.almanach.CalendarEventDTO;
import org.silverpeas.mobile.shared.dto.almanach.TimeUnitDTO;
import org.silverpeas.mobile.shared.dto.documents.SimpleDocumentDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
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
  SpanElement repeat, duration, frequency;

  @UiField
  DivElement reminder, location, dateEvent, reccurence;
  @UiField
  Anchor delete, link, content;
  @UiField
  ListBox reminderDurations;

  @UiField
  UnorderedList attachments, attendees;

  @UiField
  ActionsMenu actionsMenu;

  private CalendarEventDTO event;
  private CalendarDTO calendar;
  private ReminderDTO reminderDTO;

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
    attachments.setId("attachments");
    EventBus.getInstance().addHandler(AbstractEventPagesEvent.TYPE, this);
  }

  public void setData(ApplicationInstanceDTO instance, CalendarEventDTO event, CalendarDTO calendar) {
    EventBus.getInstance().fireEvent(new RemindersLoadEvent(event));
    EventBus.getInstance().fireEvent(new AttachmentsLoadEvent(event));
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

    if (event.getRecurrence() == null) {
      reccurence.getStyle().setDisplay(Style.Display.NONE);
    } else {
      reccurence.getStyle().setDisplay(Style.Display.BLOCK);
      String frequencyLabel = "";
      if (event.getRecurrence().getFrequency().getTimeUnit().name().equalsIgnoreCase("DAY")) {
        frequencyLabel = msg.DAY();
      } else if (event.getRecurrence().getFrequency().getTimeUnit().name().equalsIgnoreCase("WEEK")) {
        frequencyLabel = msg.WEEK();
      } else if (event.getRecurrence().getFrequency().getTimeUnit().name().equalsIgnoreCase("MONTH")) {
        frequencyLabel = msg.MONTH();
      } else if (event.getRecurrence().getFrequency().getTimeUnit().name().equalsIgnoreCase("YEAR")) {
        frequencyLabel = msg.YEAR();
      }
      frequency.setInnerText(frequencyLabel);

      duration.setInnerText(""+event.getRecurrence().getFrequency().getInterval());
      if (event.getRecurrence().getEndDate() == null) {
        if (event.getRecurrence().getCount() == 0) {
          repeat.setInnerText(msg.NEVER());
        } else {
          repeat.setInnerText("" + event.getRecurrence().getCount());
        }
      } else {
        Date end = df.parse(event.getRecurrence().getEndDate());
        repeat.setInnerText(displayDf.format(end));
      }
    }

    for (CalendarEventAttendeeDTO attendee : event.getAttendees()) {
      AttendeeItem item = new AttendeeItem();
      item.setData(attendee);
      attendees.add(item);
    }
  }

  @Override
  public void onRemindersLoaded(final RemindersLoadedEvent event) {
    if (!event.getReminders().isEmpty()) {
      ReminderDTO r = event.getReminders().get(0);
      this.reminderDTO = r;
      for (String duration : event.getDurations()) {

        String durationLabel = "";
        if (duration.equals("0MINUTE")) {
          durationLabel = msg.ATTIME();
        } else if(duration.equals("15MINUTE")) {
          durationLabel = msg.MINUTES("15");
        } else if(duration.equals("30MINUTE")) {
          durationLabel = msg.MINUTES("30");
        } else if(duration.equals("1HOUR")) {
          durationLabel = msg.HOUR();
        } else if(duration.equals("2HOUR")) {
          durationLabel = msg.HOURS(r.getDuration().toString());
        } else if(duration.equals("1DAY")) {
          durationLabel = msg.OneDAY();
        } else if(duration.equals("2DAY")) {
          durationLabel = msg.TwoDay();
        } else if(duration.equals("1WEEK")) {
          durationLabel = msg.OneWeek();
        }
        reminderDurations.addItem(durationLabel, duration);
      }

      String duration = r.getDuration() + r.getTimeUnit().name();
      for (int i = 0; i < reminderDurations.getItemCount(); i++) {
        if (reminderDurations.getValue(i).equals(duration)) {
          reminderDurations.setSelectedIndex(i);
          break;
        }
      }
    } else {
      reminder.getStyle().setDisplay(Style.Display.NONE);
    }
  }

  @Override
  public void onRemindersDeleted(final ReminderDeletedEvent event) {
    this.reminderDTO = null;
    reminder.getStyle().setDisplay(Style.Display.NONE);
    //TODO : prepare for create
    delete.setVisible(false);
    reminderDurations.setSelectedIndex(0);
  }

  @Override
  public void onAttachmentLoaded(final AttachmentsLoadedEvent event) {
    for (SimpleDocumentDTO attachment : event.getAttachments()) {
      Attachment item = new Attachment();
      item.setAttachment(attachment);
      attachments.add(item);
    }
  }

  @UiHandler("content")
  protected void showContent(ClickEvent event) {
    //TODO : use content return by rest service
    showContent(this.event.getEventId(), "almanach4", this.event.getTitle());
  }

  @UiHandler("delete")
  protected void deleteReminder(ClickEvent event) {
    EventBus.getInstance().fireEvent(new ReminderDeleteEvent(this.event, this.reminderDTO));
  }

  @UiHandler("reminderDurations")
  protected  void changeReminderDuration(ChangeEvent event) {
    RegExp regExp = RegExp.compile("(\\d+)(.*)$");
    MatchResult r = regExp.exec(reminderDurations.getSelectedValue());
    String d = r.getGroup(1);
    String u = r.getGroup(2);
    this.reminderDTO.setDuration(Integer.parseInt(d));
    this.reminderDTO.setTimeUnit(TimeUnitDTO.valueOf(u));
    EventBus.getInstance().fireEvent(new ReminderUpdateEvent(this.event, this.reminderDTO));
  }

  public static void showContent(String eventId, String componentId, String title) {
    // compute height available for content
    int heightAvailable = Window.getClientHeight() - (SpMobil.getMainPage().getHeaderHeight() + SpMobil.getMainPage().getFooterHeight());
    int widthAvailable = Window.getClientWidth();
    // display content
    String url = UrlUtils.getServicesLocation();
    url += "PublicationContent";
    url += "?id=" + eventId;
    url += "&componentId=" + componentId;
    IframePage page = new IframePage(url);
    page.setSize(widthAvailable + "px", heightAvailable + "px");
    page.setPageTitle(title);
    page.show();
  }

}