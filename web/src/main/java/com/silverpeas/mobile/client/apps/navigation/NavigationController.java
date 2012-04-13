/**
 * Copyright (C) 2000 - 2011 Silverpeas
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
 * "http://www.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.silverpeas.mobile.client.apps.navigation;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.navigation.events.controller.AbstractNavigationControllerEvent;
import com.silverpeas.mobile.client.apps.navigation.events.controller.LoadSpacesAndAppsEvent;
import com.silverpeas.mobile.client.apps.navigation.events.controller.NavigationControllerEventHandler;
import com.silverpeas.mobile.client.apps.navigation.events.pages.SpacesAndAppsLoadedEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.Controller;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;

public class NavigationController implements Controller, NavigationControllerEventHandler {

  private String type;

  public NavigationController(String type) {
    super();
    EventBus.getInstance().addHandler(AbstractNavigationControllerEvent.TYPE, this);
    this.type = type;
  }

  @Override
  public void loadSpacesAndApps(LoadSpacesAndAppsEvent event) {
    ServicesLocator.serviceNavigation.getSpacesAndApps(event.getRootSpaceId(), type,
        new AsyncCallback<List<SilverpeasObjectDTO>>() {

          @Override
          public void onSuccess(List<SilverpeasObjectDTO> result) {
            EventBus.getInstance().fireEvent(new SpacesAndAppsLoadedEvent(result));
          }

          @Override
          public void onFailure(Throwable caught) {
            EventBus.getInstance().fireEvent(new ErrorEvent(new Exception(caught)));
          }
                });
  }

  @Override
  public void stop() {
    EventBus.getInstance().removeHandler(AbstractNavigationControllerEvent.TYPE, this);
  }
}
