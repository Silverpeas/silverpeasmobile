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

package com.silverpeas.mobile.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.MetaElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.blog.BlogApp;
import com.silverpeas.mobile.client.apps.documents.DocumentsApp;
import com.silverpeas.mobile.client.apps.favorites.FavoritesApp;
import com.silverpeas.mobile.client.apps.media.MediaApp;
import com.silverpeas.mobile.client.apps.navigation.NavigationApp;
import com.silverpeas.mobile.client.apps.navigation.events.pages.HomePageLoadedEvent;
import com.silverpeas.mobile.client.apps.news.NewsApp;
import com.silverpeas.mobile.client.apps.tasks.TasksApp;
import com.silverpeas.mobile.client.apps.webpage.WebPageApp;
import com.silverpeas.mobile.client.apps.workflow.WorkflowApp;
import com.silverpeas.mobile.client.common.AuthentificationManager;
import com.silverpeas.mobile.client.common.ErrorManager;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.ShortCutRouter;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.event.ExceptionEvent;
import com.silverpeas.mobile.client.common.mobil.MobilUtils;
import com.silverpeas.mobile.client.common.mobil.Orientation;
import com.silverpeas.mobile.client.common.navigation.PageHistory;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOnly;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import com.silverpeas.mobile.client.common.reconizer.swipe.SwipeEndEvent;
import com.silverpeas.mobile.client.common.reconizer.swipe.SwipeEndHandler;
import com.silverpeas.mobile.client.common.reconizer.swipe.SwipeEvent;
import com.silverpeas.mobile.client.common.reconizer.swipe.SwipeRecognizer;
import com.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import com.silverpeas.mobile.client.components.base.Page;
import com.silverpeas.mobile.client.components.base.events.window.OrientationChangeEvent;
import com.silverpeas.mobile.client.pages.connexion.ConnexionPage;
import com.silverpeas.mobile.client.pages.main.HomePage;
import com.silverpeas.mobile.client.pages.search.SearchResultPage;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;
import com.silverpeas.mobile.shared.dto.FullUserDTO;
import com.silverpeas.mobile.shared.dto.HomePageDTO;
import com.silverpeas.mobile.shared.dto.configuration.Config;
import com.silverpeas.mobile.shared.dto.search.ResultDTO;

import java.util.ArrayList;
import java.util.List;

public class SpMobil implements EntryPoint {

  private static Page mainPage = null;
  public static DetailUserDTO user;
  private static String userToken;
  private static String viewport, bodyClass, bodyId;
  private static ApplicationMessages msg;
  private static String shortcutAppId;
  private static String shortcutContentType;
  private static String shortcutContentId;
  private static SpMobil instance = null;
  private static Orientation orientation = null;
  private static List<App> apps = new ArrayList<App>();

  /**
   * Init. spmobile.
   */
  public void onModuleLoad() {
    instance = this;
    shortcutAppId = Window.Location.getParameter("shortcutAppId");
    shortcutContentType = Window.Location.getParameter("shortcutContentType");
    shortcutContentId = Window.Location.getParameter("shortcutContentId");
    msg = GWT.create(ApplicationMessages.class);
    EventBus.getInstance().addHandler(ExceptionEvent.TYPE, new ErrorManager());
    loadIds(null);

    NodeList<Element> tags = Document.get().getElementsByTagName("meta");
    for (int i = 0; i < tags.getLength(); i++) {
      MetaElement metaTag = ((MetaElement) tags.getItem(i));
      if (metaTag.getName().equals("viewport")) {
        viewport = metaTag.getContent();
      }
    }

    orientation = MobilUtils.getOrientation();
    Window.addResizeHandler(new ResizeHandler() {
      @Override
      public void onResize(final ResizeEvent resizeEvent) {

        if (!MobilUtils.getOrientation().equals(orientation)) {
          orientation = MobilUtils.getOrientation();
          EventBus.getInstance().fireEvent(new OrientationChangeEvent(orientation));
        }

      }
    });

    // Instanciate apps
    apps.add(new DocumentsApp());
    apps.add(new MediaApp());
    apps.add(new NewsApp());
    apps.add(new NavigationApp());
    apps.add(new TasksApp());
    apps.add(new FavoritesApp());
    apps.add(new WebPageApp());
    apps.add(new BlogApp());
    apps.add(new WorkflowApp());
  }

  public static Page getMainPage() {
    if (mainPage == null) {
      mainPage = new Page();
    }
    return mainPage;
  }

  public static String getUserToken() {
    return userToken;
  }

  public static SpMobil getInstance() {
    return instance;
  }

  /**
   * Auto login.
   * @param login
   * @param password
   * @param domainId
   */
  private void login(final String login, final String password, final String domainId,
      final Command attempt) {
    Notification.activityStart();
    FullUserDTO user = AuthentificationManager.getInstance().loadUser();
    SpMobil.setUserToken(user.getToken());
    if (user != null) {

      ServicesLocator.getServiceNavigation()
          .isUserSessionOpened(user, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(final Throwable throwable) {
              Notification.activityStop();
            }

            @Override
            public void onSuccess(final Boolean open) {
              Notification.activityStop();
              if (!open) {
                FormPanel form = new FormPanel();
                FlowPanel content = new FlowPanel();
                form.add(content);
                TextBox lg = new TextBox();
                lg.setName("Login");
                lg.setValue(login);
                PasswordTextBox pwd = new PasswordTextBox();
                pwd.setName("Password");
                pwd.setValue(password);
                ListBox dom = new ListBox();
                dom.setName("DomainId");
                dom.addItem("", domainId);
                content.add(lg);
                content.add(pwd);
                content.add(dom);
                form.setAction("/silverpeas/AuthenticationServlet");
                form.setMethod("POST");
                form.setVisible(false);
                RootPanel.get().add(form);
                form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
                  @Override
                  public void onSubmitComplete(
                      final FormPanel.SubmitCompleteEvent submitCompleteEvent) {
                    ServicesLocator.getServiceNavigation()
                        .isUserSessionOpened(user, new AsyncCallback<Boolean>() {
                          @Override
                          public void onFailure(final Throwable throwable) {
                            Notification.activityStop();
                          }

                          @Override
                          public void onSuccess(final Boolean open) {
                            Notification.activityStop();
                            if (open) {
                              displayMainPage(user);
                            } else {
                              displayLoginPage();
                            }
                          }
                        });
                  }
                });
                form.submit();
              } else {
                displayMainPage(user);
              }
            }
          });
    }
  }

  public static void displayMainPage(final DetailUserDTO user) {

    if (!Window.Location.getHref().contains("?locale=") &&
        !user.getLanguage().equalsIgnoreCase("fr")) {
      Window.Location.replace(Window.Location.getHref() + "?locale=" + user.getLanguage());
    }
    SpMobil.user = user;
    getMainPage().setUser(user);
    RootPanel.get().clear();
    RootPanel.get().add(getMainPage());
    PageHistory.getInstance().goTo(new HomePage());

    if (shortcutAppId != null && shortcutContentType != null && shortcutContentId != null) {
      ShortCutRouter.route(user, shortcutAppId, shortcutContentType, shortcutContentId);
    } else {
      final String key = "MainHomePage_";
      AsyncCallbackOnlineOrOffline action =
          new AsyncCallbackOnlineOrOffline<HomePageDTO>(getOfflineAction(key)) {

            @Override
            public void attempt() {
              ServicesLocator.getServiceNavigation().getHomePageData(null, this);
            }

            @Override
            public void onSuccess(HomePageDTO result) {
              super.onSuccess(result);
              // send event to main home page
              EventBus.getInstance().fireEvent(new HomePageLoadedEvent(result));
              LocalStorageHelper.store(key, HomePageDTO.class, result);
            }
          };
      action.attempt();
    }
  }

  private static Command getOfflineAction(final String key) {
    Command offlineAction = new Command() {

      public void execute() {
        HomePageDTO result = LocalStorageHelper.load(key, HomePageDTO.class);
        if (result == null) {
          result = new HomePageDTO();
        }
        // send event to main home page
        EventBus.getInstance().fireEvent(new HomePageLoadedEvent(result));
      }
    };
    return offlineAction;
  }

  /**
   * Load ids in SQL Web Storage.
   */
  public void loadIds(Command attempt) {
    FullUserDTO user = AuthentificationManager.getInstance().loadUser();
    if (user != null) {
      String password = AuthentificationManager.getInstance().decryptPassword(user.getPassword());
      if (password != null) {
        SpMobil.userToken = user.getToken();
        login(user.getLogin(), password, user.getDomainId(), attempt);
      }
    } else {
      tabletGesture(false);
      displayLoginPage();
    }
  }

  private void tabletGesture(boolean connected) {
    if (MobilUtils.isTablet()) {
      if (connected) {
        ServicesLocator.getServiceNavigation().setTabletMode(new AsyncCallback<Boolean>() {
          @Override
          public void onFailure(final Throwable throwable) {
            Notification.activityStop();
          }

          @Override
          public void onSuccess(final Boolean desktopMode) {
            Notification.activityStop();
            if (desktopMode) {
              String url = Window.Location.getHref();
              url = url.substring(0, url.indexOf("silverpeas") + "silverpeas".length());
              Window.Location.replace(url);
            }
          }
        });
      } else {
        ServicesLocator.getServiceConnection().setTabletMode(new AsyncCallback<Boolean>() {
          @Override
          public void onFailure(final Throwable throwable) {
            Notification.activityStop();
          }

          @Override
          public void onSuccess(final Boolean desktopMode) {
            Notification.activityStop();
            if (desktopMode) {
              String url = Window.Location.getHref();
              url = url.substring(0, url.indexOf("silverpeas") + "silverpeas".length());
              Window.Location.replace(url);
            }
          }
        });
      }
    }
  }

  private void displayLoginPage() {
    AuthentificationManager.getInstance().clearUserStorage();
    ConnexionPage connexionPage = new ConnexionPage();
    RootPanel.get().clear();
    RootPanel.get().add(connexionPage);
  }

  public static void search(final String query) {
    Notification.activityStart();
    AsyncCallbackOnlineOnly action = new AsyncCallbackOnlineOnly<List<ResultDTO>>() {
      @Override
      public void attempt() {
        ServicesLocator.getServiceSearch().search(query, this);
      }

      @Override
      public void onSuccess(final List<ResultDTO> results) {
        super.onSuccess(results);
        getMainPage().resetSearchField();
        getMainPage().closeMenu();
        SearchResultPage page = new SearchResultPage();
        page.setPageTitle(msg.results());
        page.setResults(results);
        page.show();
        Notification.activityStop();
      }
    };
    action.attempt();
  }

  public static void showFullScreen(final Widget content, final boolean zoomable, String bodyClass,
      String bodyId) {
    PageHistory.getInstance().gotoToFullScreen("viewer");
    RootPanel.get().clear();
    RootPanel.get().add(content);

    if (zoomable) {
      NodeList<Element> tags = Document.get().getElementsByTagName("meta");
      for (int i = 0; i < tags.getLength(); i++) {
        MetaElement metaTag = ((MetaElement) tags.getItem(i));
        if (metaTag.getName().equals("viewport")) {
          metaTag.setContent("");
        }
      }
    }
    SpMobil.bodyClass = Document.get().getBody().getClassName();
    SpMobil.bodyId = Document.get().getBody().getId();
    Document.get().getBody().setClassName(bodyClass);
    Document.get().getBody().setId(bodyId);
    Document.get().getBody().getStyle().setPaddingTop(0, Style.Unit.PX);
  }

  public static void restoreMainPage() {
    RootPanel.get().clear();
    RootPanel.get().add(SpMobil.getMainPage());

    Document.get().getBody().setId(bodyId);
    Document.get().getBody().setClassName(bodyClass);
    Document.get().getBody().getStyle().clearPaddingTop();

    NodeList<Element> tags = Document.get().getElementsByTagName("meta");
    for (int i = 0; i < tags.getLength(); i++) {
      MetaElement metaTag = ((MetaElement) tags.getItem(i));
      if (metaTag.getName().equals("viewport")) {
        metaTag.setContent(viewport);
      }
    }

  }

  public static void destroyMainPage() {
    mainPage = null;
  }

  public static Config getConfiguration() {
    Config conf = LocalStorageHelper.load("config", Config.class);
    if (conf == null) {
      conf = Config.getDefaultConfig();
    }
    return conf;
  }

  public static void setUserToken(final String token) {
    SpMobil.userToken = token;
  }
}
