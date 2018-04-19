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

package org.silverpeas.mobile.client.apps.agenda;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.apps.agenda.events.app.AbstractAgendaAppEvent;
import org.silverpeas.mobile.client.apps.agenda.events.app.AgendaAppEventHandler;
import org.silverpeas.mobile.client.apps.agenda.events.app.AgendaLoadEvent;
import org.silverpeas.mobile.client.apps.agenda.events.pages.AgendaLoadedEvent;
import org.silverpeas.mobile.client.apps.agenda.pages.AgendaPage;
import org.silverpeas.mobile.client.apps.agenda.resources.AgendaMessages;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOrOffline;
import org.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import org.silverpeas.mobile.shared.dto.almanach.CalendarDTO;
import org.silverpeas.mobile.shared.dto.almanach.CalendarEventDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.navigation.Apps;

import java.util.ArrayList;
import java.util.List;

public class AgendaApp extends App implements AgendaAppEventHandler, NavigationEventHandler {

  private AgendaMessages msg;
  private ApplicationInstanceDTO instance;

  public AgendaApp(){
    super();
    msg = GWT.create(AgendaMessages.class);
    EventBus.getInstance().addHandler(AbstractAgendaAppEvent.TYPE, this);
    EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
  }

  public void start(){
    // always start
  }

  @Override
  public void stop() {
    // nevers stop
  }

  @Override
  public void appInstanceChanged(final NavigationAppInstanceChangedEvent event) {
    if (event.getInstance().getType().equals(Apps.almanach.name())) {
      this.instance = event.getInstance();
      AgendaPage page = new AgendaPage();
      page.setPageTitle(event.getInstance().getLabel());
      page.show();
    }
  }

  @Override
  public void showContent(final NavigationShowContentEvent event) {

  }

  @Override
  public void loadAgenda(final AgendaLoadEvent event) {
    final String key = "events_" + instance.getId();
    final String keyCalendars = "calendars_" + instance.getId();

    Command offlineAction = new Command() {
      @Override
      public void execute() {
        List<CalendarDTO> calendars = LocalStorageHelper.load(keyCalendars, List.class);
        if (calendars == null) {
          calendars = new ArrayList<CalendarDTO>();
        }
        for (CalendarDTO calendar : calendars) {
          List<CalendarEventDTO> events = LocalStorageHelper.load(key+calendar.getId(), List.class);
          if (events == null) {
            events = new ArrayList<CalendarEventDTO>();
          }
          EventBus.getInstance().fireEvent(new AgendaLoadedEvent(calendars, events));
        }
      }
    };

    MethodCallbackOnlineOrOffline action = new MethodCallbackOnlineOrOffline<List<CalendarDTO>>(offlineAction) {
      @Override
      public void attempt() {
        ServicesLocator.getServiceAlmanach().getCalendars(instance.getId(), this);
      }

      @Override
      public void onSuccess(final Method method, final List<CalendarDTO> calendars) {
        super.onSuccess(method, calendars);
        for(CalendarDTO calendar : calendars) {

          LocalStorageHelper.store(keyCalendars, List.class, calendars);
          MethodCallbackOnlineOrOffline subAction = new MethodCallbackOnlineOrOffline<List<CalendarEventDTO>>(offlineAction) {
            @Override
            public void attempt() {
              //TODO : manager date range
              ServicesLocator.getServiceAlmanach()
                  .getOccurences(instance.getId(), calendar.getId(),
                      "2018-03-24T23:00:00.000Z", "2018-05-14T21:59:59.999Z", SpMobil.getUser().getZone(), this);
            }

            @Override
            public void onSuccess(final Method method, final List<CalendarEventDTO> events) {
              super.onSuccess(method, events);
              LocalStorageHelper.store(key + calendar.getId(), List.class, events);
              EventBus.getInstance().fireEvent(new AgendaLoadedEvent(calendars, events));
            }
          };
          subAction.attempt();
        }
      }
    };
    action.attempt();
  }
}
