/*
 * Copyright (C) 2000 - 2021 Silverpeas
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

package org.silverpeas.mobile.client.apps.resourcesManager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.apps.resourcesManager.events.app.AbstractResourcesManagerAppEvent;
import org.silverpeas.mobile.client.apps.resourcesManager.events.app.AddReservationEvent;
import org.silverpeas.mobile.client.apps.resourcesManager.events.app.ResourcesManagerAppEventHandler;
import org.silverpeas.mobile.client.apps.resourcesManager.pages.ResourcesManagerPage;
import org.silverpeas.mobile.client.apps.resourcesManager.resources.ResourcesManagerMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.navigation.Apps;

public class ResourcesManagerApp extends App implements ResourcesManagerAppEventHandler, NavigationEventHandler {

  private ResourcesManagerMessages msg;
  private ApplicationInstanceDTO instance;

  public ResourcesManagerApp(){
    super();
    msg = GWT.create(ResourcesManagerMessages.class);
    EventBus.getInstance().addHandler(AbstractResourcesManagerAppEvent.TYPE, this);
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
    if (event.getInstance().getType().equals(Apps.resourcesManager.name())) {
      this.instance = event.getInstance();
      ResourcesManagerPage page = new ResourcesManagerPage();
      page.setPageTitle(event.getInstance().getLabel());
      page.show();
    }
  }

  @Override
  public void showContent(final NavigationShowContentEvent event) {
    if (event.getContent().getType().equals("Component") && event.getContent().getInstanceId().startsWith(Apps.resourcesManager.name())) {
      super.showContent(event);
    }
  }

  @Override
  public void addReservation(final AddReservationEvent event) {
    //TODO
  }
}
