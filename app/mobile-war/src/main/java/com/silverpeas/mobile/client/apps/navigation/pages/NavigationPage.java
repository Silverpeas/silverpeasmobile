package com.silverpeas.mobile.client.apps.navigation.pages;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.navigation.events.app.LoadSpacesAndAppsEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.pages.AbstractNavigationPagesEvent;
import com.silverpeas.mobile.client.apps.navigation.events.pages.ClickItemEvent;
import com.silverpeas.mobile.client.apps.navigation.events.pages.HomePageLoadedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.pages.NavigationPagesEventHandler;
import com.silverpeas.mobile.client.apps.navigation.pages.widgets.NavigationItem;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.components.UnorderedList;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.client.components.homepage.HomePageContent;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import com.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;
import com.silverpeas.mobile.shared.dto.navigation.SpaceDTO;

public class NavigationPage extends PageContent implements NavigationPagesEventHandler {

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

  @Override
  public void homePageLoaded(HomePageLoadedEvent event) {
    if (isVisible() && dataLoaded == false) {
      content.setData(event.getData());
      dataLoaded = true;
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


}
