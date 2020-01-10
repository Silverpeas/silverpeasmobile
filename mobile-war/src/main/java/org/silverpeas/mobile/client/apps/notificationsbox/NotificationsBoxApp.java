/*
 * Copyright (C) 2000 - 2019 Silverpeas
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

package org.silverpeas.mobile.client.apps.notificationsbox;

import com.google.gwt.core.client.GWT;
import org.silverpeas.mobile.client.apps.favorites.events.app.GotoAppEvent;
import org.silverpeas.mobile.client.apps.favorites.pages.FavoritesPage;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.events.app.AbstractNotificationsBoxAppEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.events.app.NotificationsBoxAppEventHandler;
import org.silverpeas.mobile.client.apps.notificationsbox.events.app.NotificationsLoadEvent;
import org.silverpeas.mobile.client.apps.notificationsbox.pages.NotificationsBoxPage;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.ContentsTypes;

public class NotificationsBoxApp extends App implements NotificationsBoxAppEventHandler, NavigationEventHandler {

    private ApplicationMessages msg;

    public NotificationsBoxApp(){
        super();
        msg = GWT.create(ApplicationMessages.class);
        EventBus.getInstance().addHandler(AbstractNotificationsBoxAppEvent.TYPE, this);
        EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
    }

    public void start(){
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
  public void loadNotifications(final NotificationsLoadEvent notificationsLoadEvent) {
    //TODO
  }

  @Override
  public void showContent(final NavigationShowContentEvent event) {
    if (event.getContent().getType().equals(ContentsTypes.NotificationsBox.toString())) {
      NotificationsBoxPage page = new NotificationsBoxPage();
      setMainPage(page);
      page.show();
    }
  }
}
