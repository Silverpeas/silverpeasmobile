package com.silverpeas.mobile.client.apps.navigation;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.silverpeas.mobile.client.apps.navigation.events.app.AbstractNavigationAppEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.LoadSpacesAndAppsEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.NavigationAppEventHandler;
import com.silverpeas.mobile.client.apps.navigation.events.pages.HomePageLoadedEvent;
import com.silverpeas.mobile.client.apps.navigation.pages.NavigationPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import com.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.HomePageDTO;
import com.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;

public class NavigationApp extends App implements NavigationAppEventHandler {

    private String title;
    private ApplicationMessages msg;

    public NavigationApp() {
        super();
        msg = GWT.create(ApplicationMessages.class);
        EventBus.getInstance().addHandler(AbstractNavigationAppEvent.TYPE, this);
    }

    @Override
    public void start() {
        NavigationPage mainPage = new NavigationPage();
        mainPage.setPageTitle(title);
        mainPage.setRootSpaceId(null);
        setMainPage(mainPage);
        mainPage.show();
        // no "super.start(lauchingPage);" this apps is used in another apps
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
    public void loadSpacesAndApps(final LoadSpacesAndAppsEvent event) {
        //TODO : replace call getSpaceAndApps by getHomePage
        final String key = "spaceapp_" + event.getRootSpaceId();

      AsyncCallbackOnlineOrOffline action = new AsyncCallbackOnlineOrOffline<HomePageDTO>(getOfflineAction(key)) {

        @Override
        public void attempt() {
          Notification.activityStart();
          ServicesLocator.getServiceNavigation().getHomePageData(event.getRootSpaceId(), this);
        }

        @Override
        public void onSuccess(HomePageDTO result) {
          super.onSuccess(result);
          EventBus.getInstance().fireEvent(new HomePageLoadedEvent(result));
          LocalStorageHelper.store(key, HomePageDTO.class, result);
        }
      };
      action.attempt();
    }

    private Command getOfflineAction(final String key) {
        Command offlineAction = new Command() {

            public void execute() {
                HomePageDTO result = LocalStorageHelper.load(key, HomePageDTO.class);
                if (result == null) {
                    result = new HomePageDTO();
                }
                EventBus.getInstance().fireEvent(new HomePageLoadedEvent(result));
            }
        };
        return offlineAction;
    }
}
