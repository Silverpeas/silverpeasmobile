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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.agenda.events.TimeRange;
import org.silverpeas.mobile.client.apps.agenda.events.app.CalendarLoadEvent;
import org.silverpeas.mobile.client.apps.agenda.events.pages.AbstractAgendaPagesEvent;
import org.silverpeas.mobile.client.apps.agenda.events.pages.CalendarLoadedEvent;
import org.silverpeas.mobile.client.apps.agenda.events.pages.AgendaPagesEventHandler;
import org.silverpeas.mobile.client.apps.agenda.pages.widgets.EventItem;
import org.silverpeas.mobile.client.apps.agenda.resources.AgendaMessages;
import org.silverpeas.mobile.client.apps.blog.resources.BlogMessages;
import org.silverpeas.mobile.client.apps.favorites.pages.widgets.AddToFavoritesButton;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.almanach.CalendarDTO;
import org.silverpeas.mobile.shared.dto.almanach.CalendarEventDTO;

import java.util.List;

public class AgendaPage extends PageContent implements AgendaPagesEventHandler {

  private static AgendaPageUiBinder uiBinder = GWT.create(AgendaPageUiBinder.class);
  private List<CalendarDTO> calendars = null;
  private TimeRange currentTimeRange = TimeRange.weeks;

  @UiField(provided = true) protected AgendaMessages msg = null;

  @UiField
  ActionsMenu actionsMenu;

  @UiField
  UnorderedList events;

  private AddToFavoritesButton favorite = new AddToFavoritesButton();
  private String instanceId;

  public void setCalendars(final List<CalendarDTO> calendars) {
    this.calendars = calendars;
    EventBus.getInstance().fireEvent(new CalendarLoadEvent(calendars.get(0), currentTimeRange));
  }

  @Override
  public void onCalendarEventsLoaded(final CalendarLoadedEvent event) {
    //TODO : events groupments
    for (CalendarEventDTO dto : event.getEvents()) {
      EventItem item = new EventItem();
      item.setData(dto);
      events.add(item);
    }
  }

  interface AgendaPageUiBinder extends UiBinder<Widget, AgendaPage> {
  }

  public AgendaPage() {
    msg = GWT.create(AgendaMessages.class);
    setPageTitle(msg.title());
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractAgendaPagesEvent.TYPE, this);
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractAgendaPagesEvent.TYPE, this);
  }
}