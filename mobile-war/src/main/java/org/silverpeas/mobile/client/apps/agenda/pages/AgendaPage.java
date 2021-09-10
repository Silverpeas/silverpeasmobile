/*
 * Copyright (C) 2000 - 2021 Silverpeas
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
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.agenda.events.TimeRange;
import org.silverpeas.mobile.client.apps.agenda.events.app.CalendarLoadEvent;
import org.silverpeas.mobile.client.apps.agenda.events.pages.AbstractAgendaPagesEvent;
import org.silverpeas.mobile.client.apps.agenda.events.pages.CalendarLoadedEvent;
import org.silverpeas.mobile.client.apps.agenda.events.pages.AgendaPagesEventHandler;
import org.silverpeas.mobile.client.apps.agenda.pages.widgets.EventItem;
import org.silverpeas.mobile.client.apps.agenda.pages.widgets.GroupItem;
import org.silverpeas.mobile.client.apps.agenda.resources.AgendaMessages;
import org.silverpeas.mobile.client.apps.favorites.pages.widgets.AddToFavoritesButton;
import org.silverpeas.mobile.client.common.DateUtil;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.almanach.CalendarDTO;
import org.silverpeas.mobile.shared.dto.almanach.CalendarEventDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AgendaPage extends PageContent implements AgendaPagesEventHandler {

  private static AgendaPageUiBinder uiBinder = GWT.create(AgendaPageUiBinder.class);
  private List<CalendarDTO> calendars = null;
  private TimeRange currentTimeRange = TimeRange.weeks;
  private List<GroupItem> groups = new ArrayList<>();

  @UiField(provided = true) protected AgendaMessages msg = null;

  @UiField
  ActionsMenu actionsMenu;

  @UiField
  UnorderedList events;

  @UiField
  HTMLPanel container;

  @UiField
  ListBox calendarsList;

  @UiField
  Anchor week, mouth;

  @UiField
  SpanElement message;

  private AddToFavoritesButton favorite = new AddToFavoritesButton();

  interface AgendaPageUiBinder extends UiBinder<Widget, AgendaPage> {
  }

  public AgendaPage() {
    msg = GWT.create(AgendaMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));
    container.getElement().setId("list-events");
    week.getElement().setId("btn-week");
    mouth.getElement().setId("btn-month");
    EventBus.getInstance().addHandler(AbstractAgendaPagesEvent.TYPE, this);
  }

  public void setCalendars(final List<CalendarDTO> cals) {
    this.calendars = cals;
    if (cals.size() == 1) {
      calendarsList.setVisible(false);
    } else {
      calendarsList.addItem("Tous", "all");
    }
    for (CalendarDTO cal : cals) {
      calendarsList.addItem(cal.getTitle(), cal.getId());
    }
    EventBus.getInstance().fireEvent(new CalendarLoadEvent(null, currentTimeRange));
  }

  @Override
  public void onCalendarEventsLoaded(final CalendarLoadedEvent event) {
    if (!event.getEvents().isEmpty()) {
      events.clear();
      groups.clear();
      DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mmZZZ");
      DateTimeFormat df = DateTimeFormat.getFormat("yyyy-MM-dd");
      if (currentTimeRange.equals(TimeRange.weeks)) {
        int weekNumber = DateUtil.getWeek(new Date());
        int yearNumber = DateUtil.getYear(new Date());
        for (int i = 0; i <= 3; i++) {
          GroupItem groupItem = new GroupItem();
          groupItem.setTimeRange(TimeRange.weeks);
          if (weekNumber+i > 52) {
            weekNumber = 1 - i;
            yearNumber++;
          }
          groupItem.setNumber(weekNumber+i);
          groupItem.setYear(yearNumber);
          groups.add(groupItem);
        }
        for (CalendarEventDTO dto : event.getEvents()) {
          Date startDate;
          Date endDate;
          if (dto.isOnAllDay()) {
            startDate = df.parse(dto.getStartDate());
            endDate = df.parse(dto.getEndDate());
            endDate.setTime(endDate.getTime()-1000);
          } else {
            startDate = dtf.parse(dto.getStartDate());
            endDate = dtf.parse(dto.getEndDate());
          }

          int startWeek = DateUtil.getWeek(startDate);
          int endWeek = DateUtil.getWeek(endDate);
          int startYear = DateUtil.getYear(startDate);
          int endYear = DateUtil.getYear(endDate);
          for (GroupItem groupItem : groups) {
            if ((groupItem.getNumber() >= startWeek && groupItem.getNumber() <= endWeek) || (groupItem.getNumber() >= startWeek && endYear > startYear)) {
              EventItem item = new EventItem();
              item.showCalendarName(isMultiCalendar());
              item.setData(event.getInstance(), dto, getCalendar(dto.getCalendarId()));
              groupItem.addEvent(item);
            }
          }
        }
      } else if (currentTimeRange.equals(TimeRange.months)) {
        Date today = new Date();
        int monthNumber = DateUtil.getMonth(today) - 1;
        int yearNumber = DateUtil.getYear(today);
        int number = monthNumber;
        for (int i = 0; i < 12; i++) {
          GroupItem groupItem = new GroupItem();
          groupItem.setTimeRange(TimeRange.months);
          number++;
          if (number > 12) {
            number = 1;
            yearNumber++;
          }
          groupItem.setNumber(number);
          groupItem.setYear(yearNumber);
          groups.add(groupItem);
        }

        for (CalendarEventDTO dto : event.getEvents()) {
          Date startDate;
          Date endDate;
          if (dto.isOnAllDay()) {
            startDate = df.parse(dto.getStartDate());
            endDate = df.parse(dto.getEndDate());
          } else {
            startDate = dtf.parse(dto.getStartDate());
            endDate = dtf.parse(dto.getEndDate());
          }

          int startMonth = DateUtil.getMonth(startDate);
          int endMonth = DateUtil.getMonth(endDate);

          int startYear = DateUtil.getYear(startDate);
          int endYear = DateUtil.getYear(endDate);

          for (GroupItem groupItem : groups) {
            if ((groupItem.getNumber() >= startMonth && groupItem.getNumber() <= endMonth) && groupItem.getYear() == startYear && groupItem.getYear() == endYear  || (groupItem.getNumber() >= startMonth && endYear > startYear)) {
              EventItem item = new EventItem();
              item.showCalendarName(isMultiCalendar());
              item.setData(event.getInstance(), dto, getCalendar(dto.getCalendarId()));
              groupItem.addEvent(item);
            }
          }
        }
      }

      // render list
      for (GroupItem groupItem : groups) {
        if (!groupItem.isEmpty()) {
          groupItem.render();
          events.add(groupItem);
        }
      }
    } else {
      message.setInnerText(msg.noEvent());
    }
    actionsMenu.addAction(favorite);
    favorite.init(getApp().getApplicationInstance().getId(), getApp().getApplicationInstance().getId(), ContentsTypes.Component.name(), getPageTitle());
  }

  private CalendarDTO getCalendar(final String calendarId) {
    for (CalendarDTO calendar : calendars) {
      if (calendar.getId().equals(calendarId)) return calendar;
    }
    return null;
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractAgendaPagesEvent.TYPE, this);
  }

  @UiHandler("week")
  protected void week(ClickEvent event) {
    currentTimeRange = TimeRange.weeks;
    changeGroupSelector();
  }

  @UiHandler("mouth")
  protected void mouth(ClickEvent event) {
    currentTimeRange = TimeRange.months;
    changeGroupSelector();
  }

  private void changeGroupSelector() {
    week.getElement().toggleClassName("ui-btn-active");
    mouth.getElement().toggleClassName("ui-btn-active");
    EventBus.getInstance().fireEvent(new CalendarLoadEvent(getSelectedCalendar(), currentTimeRange));
  }

  @UiHandler("calendarsList")
  protected void changeCalendar(ChangeEvent event) {
    EventBus.getInstance().fireEvent(new CalendarLoadEvent(getSelectedCalendar(), currentTimeRange));
  }

  private boolean isMultiCalendar() {
    return calendars != null && !calendars.isEmpty() && calendars.size() > 1;
  }

  private CalendarDTO getSelectedCalendar() {
    String id = calendarsList.getSelectedValue();
    for (CalendarDTO cal : calendars) {
      if (cal.getId().equals(id)) {
        return cal;
      }
    }
    return null;
  }
}