
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

package org.silverpeas.mobile.client.pages.main;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.apps.config.events.app.AbstractConfigAppEvent;
import org.silverpeas.mobile.client.apps.config.events.app.ConfigAppEventHandler;
import org.silverpeas.mobile.client.apps.config.events.app.LoadConfigEvent;
import org.silverpeas.mobile.client.apps.config.events.app.UpdateConfigEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.pages.AbstractNavigationPagesEvent;
import org.silverpeas.mobile.client.apps.navigation.events.pages.ClickItemEvent;
import org.silverpeas.mobile.client.apps.navigation.events.pages.HomePageLoadedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.pages.NavigationPagesEventHandler;
import org.silverpeas.mobile.client.apps.navigation.pages.NavigationPage;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.PushNotificationsManager;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.components.homepage.HomePageContent;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.configuration.Config;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.navigation.SpaceDTO;

public class HomePage extends PageContent implements ConfigAppEventHandler, NavigationPagesEventHandler {

  private static HomePageUiBinder uiBinder = GWT.create(HomePageUiBinder.class);
  private int currentNewsIndex = 0;

  @UiField(provided = true) protected ApplicationMessages msg = null;
  private boolean dataLoaded = false;

  @UiField
  HomePageContent content;

  interface HomePageUiBinder extends UiBinder<Widget, HomePage> {
  }

  public HomePage() {
    msg = GWT.create(ApplicationMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractConfigAppEvent.TYPE, this);
    EventBus.getInstance().addHandler(AbstractNavigationPagesEvent.TYPE, this);
    Config conf = SpMobil.getConfiguration();
  }

  @Override
  public void homePageLoaded(final HomePageLoadedEvent event) {
    PushNotificationsManager.getInstance().inject();


    if (isVisible() && dataLoaded == false) {
      content.setData(event.getData());
      dataLoaded = true;
    }
    Notification.activityStop();
  }

  @Override
  public void clickItem(final ClickItemEvent event) {
    if (isVisible()) {
      if (event.getData() instanceof SpaceDTO) {
        NavigationPage subPage = new NavigationPage();
        if (((SpaceDTO) event.getData()).isPersonal()) {
          subPage.setPageTitle(msg.personalSpace());
        } else {
          subPage.setPageTitle(event.getData().getLabel());
        }
        subPage.setRootSpaceId(event.getData().getId());
        subPage.show();
      } else {
        EventBus.getInstance().fireEvent(new NavigationAppInstanceChangedEvent((ApplicationInstanceDTO)event.getData()));
      }
    }
  }

  @Override
  public void stop() {
    super.stop();
    content.stop();
    EventBus.getInstance().removeHandler(AbstractConfigAppEvent.TYPE, this);
    EventBus.getInstance().removeHandler(AbstractNavigationPagesEvent.TYPE, this);
  }

  @Override
  public void updateConfig(UpdateConfigEvent event) {
    content.setConfig(event.getConfig());
  }

  @Override
  public void loadConfig(LoadConfigEvent event) {}
}
