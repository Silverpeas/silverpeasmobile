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

package org.silverpeas.mobile.client.apps.navigation.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.apps.favorites.pages.widgets.AddToFavoritesButton;
import org.silverpeas.mobile.client.apps.navigation.events.app.LoadSpacesAndAppsEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.pages.AbstractNavigationPagesEvent;
import org.silverpeas.mobile.client.apps.navigation.events.pages.ClickItemEvent;
import org.silverpeas.mobile.client.apps.navigation.events.pages.HomePageLoadedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.pages.NavigationPagesEventHandler;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.ShortCutRouter;
import org.silverpeas.mobile.client.common.navigation.LinksManager;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.client.components.homepage.HomePageContent;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.navigation.HomePages;
import org.silverpeas.mobile.shared.dto.navigation.SpaceDTO;

public class NavigationPage extends PageContent implements NavigationPagesEventHandler {

  @UiField ActionsMenu actionsMenu;

  private AddToFavoritesButton favorite = new AddToFavoritesButton();

  private static NavigationPageUiBinder uiBinder = GWT.create(NavigationPageUiBinder.class);
  private String rootSpaceId;
  private boolean dataLoaded = false;

  protected ApplicationMessages msg = null;

  @UiField
  HomePageContent content;

  interface NavigationPageUiBinder extends UiBinder<Widget, NavigationPage> {
  }

  public NavigationPage() {
    initWidget(uiBinder.createAndBindUi(this));
    setPageTitle("Nav");
    msg = GWT.create(ApplicationMessages.class);
    EventBus.getInstance().addHandler(AbstractNavigationPagesEvent.TYPE, this);
    Notification.activityStart();
  }

  public HomePageContent getContent() {
    return content;
  }

  @Override
  public void homePageLoaded(HomePageLoadedEvent event) {
    if (isVisible() && dataLoaded == false) {
      // Silverpeas Home
      content.setData(event.getData());
      setPageTitle(event.getData().getSpaceName());

      dataLoaded = true;

      actionsMenu.addAction(favorite);
      favorite.init(null, event.getData().getId(), ContentsTypes.Space.name(), getPageTitle());
    }
    Notification.activityStop();
  }

  public void setRootSpaceId(String rootSpaceId) {
    this.rootSpaceId = rootSpaceId;
    EventBus.getInstance().fireEvent(new LoadSpacesAndAppsEvent(rootSpaceId));
  }


  @Override
  public void stop() {
    super.stop();
    content.stop();
    EventBus.getInstance().removeHandler(AbstractNavigationPagesEvent.TYPE, this);
  }

  @Override
  public void clickItem(ClickItemEvent event) {
    if (isVisible()) {
      if (event.getData() instanceof SpaceDTO) {
        SpaceDTO space =(SpaceDTO) event.getData();
        if (space.getHomePageType() == HomePages.SILVERPEAS.getValue()) {
          NavigationPage subPage = new NavigationPage();
          if (space.isPersonal()) {
            subPage.setPageTitle(msg.personalSpace());
          } else {
            subPage.setPageTitle(event.getData().getLabel());
          }
          subPage.setRootSpaceId(event.getData().getId());
          subPage.show();
        } else if (space.getHomePageType() == HomePages.APP.getValue()) {
          // App home
          ShortCutRouter.route(SpMobil.getUser(), space.getHomePageParameter(), "Component", null, null, null);
        } else if (space.getHomePageType() == HomePages.URL.getValue()) {
          // Url App
          LinksManager.processLink(space.getHomePageParameter());
        }
      } else {
        EventBus.getInstance().fireEvent(new NavigationAppInstanceChangedEvent((ApplicationInstanceDTO)event.getData()));
      }
    }
  }


}
