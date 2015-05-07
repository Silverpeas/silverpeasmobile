package com.silverpeas.mobile.client.apps.navigation;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.navigation.events.app.AbstractNavigationAppEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.LoadSpacesAndAppsEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.NavigationAppEventHandler;
import com.silverpeas.mobile.client.apps.navigation.events.pages.SpacesAndAppsLoadedEvent;
import com.silverpeas.mobile.client.apps.navigation.pages.NavigationPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.common.network.OfflineHelper;
import com.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import com.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;

public class NavigationApp extends App implements NavigationAppEventHandler {

  private String type, title;

  public NavigationApp() {
    super();
    EventBus.getInstance().addHandler(AbstractNavigationAppEvent.TYPE, this);
  }

  @Override
  public void start() {
    NavigationPage mainPage = new NavigationPage();
    mainPage.setPageTitle(title);
    mainPage.setRootSpaceId(null);
    setMainPage(mainPage);
    // no "super.start(lauchingPage);" this app is used in another app
  }

  public void setTypeApp(String type) {
    this.type = type;
  }

  @Override
  public void stop() {
    EventBus.getInstance().removeHandler(AbstractNavigationAppEvent.TYPE, this);
    super.stop();
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public void loadSpacesAndApps(LoadSpacesAndAppsEvent event) {
    final String key = "spaceapp_" + event.getRootSpaceId() + "_" + type;
    ServicesLocator.getServiceNavigation().getSpacesAndApps(event.getRootSpaceId(), type, new AsyncCallback<List<SilverpeasObjectDTO>>() {

      @Override
      public void onSuccess(List<SilverpeasObjectDTO> result) {
        LocalStorageHelper.store(key, List.class, result);
        EventBus.getInstance().fireEvent(new SpacesAndAppsLoadedEvent(result));
      }

      @Override
      public void onFailure(Throwable caught) {
        if (OfflineHelper.needToGoOffine(caught)) {
          List<SilverpeasObjectDTO> result = LocalStorageHelper.load(key, List.class);
          if (result == null) result = new ArrayList<SilverpeasObjectDTO>();
          EventBus.getInstance().fireEvent(new SpacesAndAppsLoadedEvent(result));
        } else {
          EventBus.getInstance().fireEvent(new ErrorEvent(caught));
        }
      }
    });
  }
}
