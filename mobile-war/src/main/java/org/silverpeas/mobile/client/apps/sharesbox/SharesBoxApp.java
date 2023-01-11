/*
 * Copyright (C) 2000 - 2022 Silverpeas
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

package org.silverpeas.mobile.client.apps.sharesbox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import org.fusesource.restygwt.client.Method;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.apps.sharesbox.events.app.AbstractSharesBoxAppEvent;
import org.silverpeas.mobile.client.apps.sharesbox.events.app.SharesBoxAppEventHandler;
import org.silverpeas.mobile.client.apps.sharesbox.pages.SharesBoxPage;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.tickets.TicketDTO;

import java.util.List;

public class SharesBoxApp extends App
    implements SharesBoxAppEventHandler, NavigationEventHandler {

  private ApplicationMessages msg;

  public SharesBoxApp() {
    super();
    msg = GWT.create(ApplicationMessages.class);
    EventBus.getInstance().addHandler(AbstractSharesBoxAppEvent.TYPE, this);
    EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
  }

  public void start() {
    // no "super.start(lauchingPage);" this apps is used in another apps
  }

  @Override
  public void stop() {
    // never stop
  }

  @Override
  public void appInstanceChanged(final NavigationAppInstanceChangedEvent event) {

  }

  @Override
  public void showContent(final NavigationShowContentEvent event) {
    if (event.getContent().getType().equals(ContentsTypes.SharesBox.toString())) {
      MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<TicketDTO>>() {
        @Override
        public void attempt() {
          super.attempt();
          ServicesLocator.getRestServiceTickets().getMyTickets("", this);
        }

        @Override
        public void onSuccess(Method method, List<TicketDTO> ticketDTOS) {
          super.onSuccess(method, ticketDTOS);

          MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<TicketDTO>>() {
            @Override
            public void attempt() {
              super.attempt();
              ServicesLocator.getServiceDocuments().getTickets(null, ticketDTOS, this);
            }
            @Override
            public void onSuccess(Method method, List<TicketDTO> ticketDTOS) {
              super.onSuccess(method, ticketDTOS);
              SharesBoxPage page = new SharesBoxPage();
              page.setData(ticketDTOS);
              setMainPage(page);
              page.show();
            }
          };
          action.attempt();
        }
      };
      action.attempt();
    }
  }
}
