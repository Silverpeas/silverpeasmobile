/*
 * Copyright (C) 2000 - 2017 Silverpeas
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
 *
 */

package com.silverpeas.mobile.client.common.app;

import com.google.gwt.user.client.ui.SimplePanel;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.navigation.PageHistory;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.client.components.base.events.apps.AbstractAppEvent;
import com.silverpeas.mobile.client.components.base.events.apps.AppEvent;
import com.silverpeas.mobile.client.components.base.events.apps.AppEventHandler;

public abstract class App implements AppEventHandler {

  private PageContent mainPage;
  protected SimplePanel container;

  public App() {
    EventBus.getInstance().addHandler(AbstractAppEvent.TYPE, this);
  }

  public void start() {
    PageHistory.getInstance().goTo(mainPage);
  }

  public void startAsWidget(SimplePanel container){
    this.container = container;
  }

  public void startWithContent(String appId, String contentType, String contentId) {

  }

  public void stop() {
    EventBus.getInstance().removeHandler(AbstractAppEvent.TYPE, this);
  }

  public PageContent getMainPage() {
    return mainPage;
  }

  protected void setMainPage(PageContent mainPage) {
    this.mainPage = mainPage;
    this.mainPage.setApp(this);

  }

  @Override
  public void receiveEvent(AppEvent event) {
    // for compatibility
  }
}
