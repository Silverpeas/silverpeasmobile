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

import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.page.Transition;
import com.silverpeas.mobile.client.apps.navigation.events.app.AbstractNavigationEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.NavigationEventHandler;
import com.silverpeas.mobile.client.apps.navigation.events.app.internal.NavigationStopEvent;
import com.silverpeas.mobile.client.apps.navigation.pages.NavigationPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.app.App;

public class NavigationApp extends App implements NavigationEventHandler,
    com.silverpeas.mobile.client.apps.navigation.events.app.internal.NavigationEventHandler {

  private String type, title;

  public NavigationApp() {
    super();
    transition = Transition.SLIDEDOWN;
    EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
    EventBus
        .getInstance()
        .addHandler(
            com.silverpeas.mobile.client.apps.navigation.events.app.internal.AbstractNavigationEvent.TYPE,
            this);
  }

  @Override
  public void start(Page lauchingPage) {
    setController(new NavigationController(type));
    NavigationPage mainPage = new NavigationPage();
    mainPage.setTitle(title);
    mainPage.setRootSpaceId(null);
    setMainPage(mainPage);
    super.start(lauchingPage);
  }

  @Override
  public void stop() {
    EventBus.getInstance().removeHandler(AbstractNavigationEvent.TYPE, this);
    EventBus
        .getInstance()
        .removeHandler(
            com.silverpeas.mobile.client.apps.navigation.events.app.internal.AbstractNavigationEvent.TYPE,
            this);
    super.stop();
  }

  public void setTypeApp(String type) {
    this.type = type;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public void appInstanceChanged(NavigationAppInstanceChangedEvent event) {
    stop();
  }

  @Override
  public void onStop(NavigationStopEvent event) {
    stop();
  }
}
