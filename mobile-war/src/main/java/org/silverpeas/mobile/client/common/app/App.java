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

package org.silverpeas.mobile.client.common.app;

import com.google.gwt.user.client.ui.SimplePanel;
import org.fusesource.restygwt.client.Method;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.event.ErrorEvent;
import org.silverpeas.mobile.client.common.navigation.PageHistory;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.components.base.events.apps.AbstractAppEvent;
import org.silverpeas.mobile.client.components.base.events.apps.AppEvent;
import org.silverpeas.mobile.client.components.base.events.apps.AppEventHandler;
import org.silverpeas.mobile.client.components.base.events.apps.StopLoadingDataEvent;
import org.silverpeas.mobile.shared.dto.ContentDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;

public abstract class App implements AppEventHandler, NavigationEventHandler {

  private PageContent mainPage;
  protected SimplePanel container;
  private boolean stopLoading = false;
  private String appName = "";

  private ApplicationInstanceDTO instance = null;

  public App() {
    EventBus.getInstance().addHandler(AbstractAppEvent.TYPE, this);
  }

  public App(String appName) {
    this.appName = appName;
    EventBus.getInstance().addHandler(AbstractAppEvent.TYPE, this);
  }

  public void start() {
    PageHistory.getInstance().goTo(mainPage);
  }

  public void startWithContent(ContentDTO content) {}

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

  public String getAppName() {
    return appName;
  }

  public void setStopLoading(final boolean stopLoading) {
    this.stopLoading = stopLoading;
  }

  public boolean isStopLoading() {
    return stopLoading;
  }

  @Override
  public void stopLoadingDataEvent(final StopLoadingDataEvent stopLoadingDataEvent) {
    if(getAppName().equals(stopLoadingDataEvent.getAppName())) {
      stopLoading = true;
    }
  }

  @Override
  public void receiveEvent(AppEvent event) {
    // for compatibility
  }

  public ApplicationInstanceDTO getApplicationInstance() {
    return instance;
  }

  protected void setApplicationInstance (ApplicationInstanceDTO instance) {
    this.instance = instance;
  }

  @Override
  public void showContent(final NavigationShowContentEvent event) {
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<ApplicationInstanceDTO>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceNavigation().getApp(event.getContent().getInstanceId(), null, null, this);
      }

      @Override
      public void onSuccess(final Method method,
          final ApplicationInstanceDTO applicationInstanceDTO) {
        super.onSuccess(method, applicationInstanceDTO);
        NavigationAppInstanceChangedEvent evt = new NavigationAppInstanceChangedEvent(instance);
        appInstanceChanged(evt);
      }

      @Override
      public void onFailure(final Method method, final Throwable t) {
        super.onFailure(method, t);
        EventBus.getInstance().fireEvent(new ErrorEvent(t));
      }
    };

  }
}
