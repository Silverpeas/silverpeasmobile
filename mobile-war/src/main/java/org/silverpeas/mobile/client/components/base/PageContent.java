/*
 * Copyright (C) 2000 - 2020 Silverpeas
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

package org.silverpeas.mobile.client.components.base;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.Composite;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.app.View;
import org.silverpeas.mobile.client.common.navigation.PageHistory;
import org.silverpeas.mobile.client.common.reconizer.swipe.SwipeRecognizer;
import org.silverpeas.mobile.client.common.resources.ResourcesManager;
import org.silverpeas.mobile.client.components.base.events.apps.StopLoadingDataEvent;
import org.silverpeas.mobile.client.components.base.events.page.AbstractPageEvent;
import org.silverpeas.mobile.client.components.base.events.page.DataLoadedEvent;
import org.silverpeas.mobile.client.components.base.events.page.LoadingDataFinishEvent;
import org.silverpeas.mobile.client.components.base.events.page.MoreDataLoadedEvent;
import org.silverpeas.mobile.client.components.base.events.page.PageEvent;
import org.silverpeas.mobile.client.components.base.events.page.PageEventHandler;

public abstract class PageContent extends Composite implements View, NativePreviewHandler, PageEventHandler {

  private App app;
  protected boolean clicked = false;
  protected String pageTitle;
  private HandlerRegistration registration;
  private SwipeRecognizer swipeRecognizer;

  public PageContent() {
    super();
    pageTitle = ResourcesManager.getLabel("mainpage.title");
    setViewport();
    registration = Event.addNativePreviewHandler(this);
    EventBus.getInstance().addHandler(AbstractPageEvent.TYPE, this);
  }

  protected void setViewport() {
    NodeList<Element> metas = Document.get().getHead().getElementsByTagName("meta");
    for (int i = 0; i < metas.getLength(); i++) {
      if (metas.getItem(i).getAttribute("name").equals("viewport")) {
        metas.getItem(i).setAttribute("content","width=device-width, target-densitydpi=device-dpi, initial-scale=1.0, maximum-scale=1.0, user-scalable=0");
      }
    }
  }

  public String getPageTitle() {
    return pageTitle;
  }

  public void setPageTitle(String pageTitle) {
    this.pageTitle = pageTitle;
    SpMobil.getMainPage().header.setPageTitle(pageTitle);
  }

  public void show() {
    PageHistory.getInstance().goTo(this);
  }

  public boolean isVisible() {
    return PageHistory.getInstance().isVisible(this);
  }

  public void back() {
    PageHistory.getInstance().back();
  }

  public void hide() {
    String appName = "";
    if (getApp() != null) appName = getApp().getAppName();
    EventBus.getInstance().fireEvent(new StopLoadingDataEvent(appName));
  }

  public void clickGesture(Command call) {
    if (!clicked) {
      clicked = true;
      call.execute();
      Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
        @Override
        public boolean execute() {
          clicked = false;
          return false;
        }}, 400);
    }
  }

  @Override
  public void stop() {
    EventBus.getInstance().removeHandler(AbstractPageEvent.TYPE, this);
    registration.removeHandler();
    if (app != null) app.stop();
  }

  @Override
  public void onPreviewNativeEvent(NativePreviewEvent event) {
    if (event.getTypeInt() == Event.ONCLICK) {
      Element target = event.getNativeEvent().getEventTarget().cast();
      while(target.getParentElement() != null) {
        if (target.getId().equals("silverpeas-navmenu-panel") || target.getId().equals("menu")) {
          return;
        }
        target = target.getParentElement();
      }
      SpMobil.getMainPage().closeMenu();
    }
  }

  public App getApp() {
    return app;
  }

  public void setApp(App app) {
    this.app = app;
  }

  @Override
  public void receiveEvent(PageEvent event) {
    // for compatibility
  }

  @Override
  public void finishLoadingData(final LoadingDataFinishEvent loadingDataFinishEvent) {
    // to be override if necessary
  }

  @Override
  public void loadedDataEvent(final DataLoadedEvent dataLoadedEvent) {
    // to be override if necessary
  }

  @Override
  public void loadedMoreDataEvent(final MoreDataLoadedEvent moreDataLoadedEvent) {
    // to be override if necessary
  }
}
