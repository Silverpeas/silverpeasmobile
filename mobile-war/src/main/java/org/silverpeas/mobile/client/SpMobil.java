/*
 * Copyright (C) 2000 - 2021 Silverpeas
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

package org.silverpeas.mobile.client;

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
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.silverpeas.mobile.client.apps.agenda.AgendaApp;
import org.silverpeas.mobile.client.apps.blog.BlogApp;
import org.silverpeas.mobile.client.apps.classifieds.ClassifiedsApp;
import org.silverpeas.mobile.client.apps.contacts.ContactsApp;
import org.silverpeas.mobile.client.apps.documents.DocumentsApp;
import org.silverpeas.mobile.client.apps.faq.FaqApp;
import org.silverpeas.mobile.client.apps.favorites.FavoritesApp;
import org.silverpeas.mobile.client.apps.formsonline.FormsOnlineApp;
import org.silverpeas.mobile.client.apps.hyperlink.HyperLinkApp;
import org.silverpeas.mobile.client.apps.media.MediaApp;
import org.silverpeas.mobile.client.apps.navigation.NavigationApp;
import org.silverpeas.mobile.client.apps.navigation.events.pages.HomePageLoadedEvent;
import org.silverpeas.mobile.client.apps.news.NewsApp;
import org.silverpeas.mobile.client.apps.notificationsbox.NotificationsBoxApp;
import org.silverpeas.mobile.client.apps.resourcesManager.ResourcesManagerApp;
import org.silverpeas.mobile.client.apps.survey.SurveyApp;
import org.silverpeas.mobile.client.apps.tasks.TasksApp;
import org.silverpeas.mobile.client.apps.webpage.WebPageApp;
import org.silverpeas.mobile.client.apps.workflow.WorkflowApp;
import org.silverpeas.mobile.client.common.AuthentificationManager;
import org.silverpeas.mobile.client.common.ErrorManager;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.ShortCutRouter;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.event.ErrorEvent;
import org.silverpeas.mobile.client.common.event.ExceptionEvent;
import org.silverpeas.mobile.client.common.event.authentication.AbstractAuthenticationErrorEvent;
import org.silverpeas.mobile.client.common.event.authentication.AuthenticationEventHandler;
import org.silverpeas.mobile.client.common.mobil.MobilUtils;
import org.silverpeas.mobile.client.common.mobil.Orientation;
import org.silverpeas.mobile.client.common.navigation.PageHistory;
import org.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.common.network.NetworkHelper;
import org.silverpeas.mobile.client.common.storage.CacheStorageHelper;
import org.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import org.silverpeas.mobile.client.components.base.Page;
import org.silverpeas.mobile.client.components.base.events.window.OrientationChangeEvent;
import org.silverpeas.mobile.client.pages.connexion.ConnexionPage;
import org.silverpeas.mobile.client.pages.main.HomePage;
import org.silverpeas.mobile.client.pages.search.SearchResultPage;
import org.silverpeas.mobile.client.pages.termsofservice.TermsOfServicePage;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.FullUserDTO;
import org.silverpeas.mobile.shared.dto.HomePageDTO;
import org.silverpeas.mobile.shared.dto.ShortCutLinkDTO;
import org.silverpeas.mobile.shared.dto.authentication.UserProfileDTO;
import org.silverpeas.mobile.shared.dto.configuration.Config;
import org.silverpeas.mobile.shared.dto.search.ResultDTO;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;

import java.util.ArrayList;
import java.util.List;

public class SpMobil implements EntryPoint, AuthenticationEventHandler {

  private static Page mainPage = null;
  private static DetailUserDTO user;
  private static UserProfileDTO userProfile;

  private static String viewport, bodyClass, bodyId;
  private static ApplicationMessages msg;
  private static String shortcutAppId;
  private static String shortcutContentType;
  private static String shortcutContentId;
  private static String shortcutContributionId;
  private static String shortcutRole;
  private static String token;
  private static String XSession;
  private static SpMobil instance = null;
  private static Orientation orientation = null;
  private static List<App> apps = new ArrayList<App>();

  public static DetailUserDTO getUser() {
    return user;
  }

  public static void setUserProfile(final UserProfileDTO userProfile) {
    SpMobil.userProfile = userProfile;
  }

  public static UserProfileDTO getUserProfile() {
    return userProfile;
  }

  public static void setUser(final DetailUserDTO user) {
    SpMobil.user = user;
  }


  /**
   * Init. spmobile.
   */
  public void onModuleLoad() {
    // init connexion supervision
    NetworkHelper.getInstance();

    instance = this;
    shortcutAppId = Window.Location.getParameter("shortcutAppId");
    shortcutContentType = Window.Location.getParameter("shortcutContentType");
    shortcutContentId = Window.Location.getParameter("shortcutContentId");
    shortcutContributionId = Window.Location.getParameter("shortcutContributionId");
    shortcutRole = Window.Location.getParameter("shortcutRole");

    NodeList<Element> node = Document.get().getElementsByTagName("meta");
    for (int i = 0; i < node.getLength(); i++) {
      if (node.getItem(i).getAttribute("name").equalsIgnoreCase("sp_token")) {
        token = node.getItem(i).getAttribute("content");
      } else if (node.getItem(i).getAttribute("name").equalsIgnoreCase("sp_session")) {
        XSession = node.getItem(i).getAttribute("content");
      }
    }

    if (token != null && !token.isEmpty()) {
      AuthentificationManager.getInstance().addHeader("X-STKN", token);
    }

    if (XSession != null && !XSession.isEmpty()) {
      AuthentificationManager.getInstance().addHeader("X-Silverpeas-Session", XSession);
    }

    if (token == null) {
      token = AuthentificationManager.getInstance().getHeader("X-STKN");
    }

    msg = GWT.create(ApplicationMessages.class);
    EventBus.getInstance().addHandler(ExceptionEvent.TYPE, new ErrorManager());
    EventBus.getInstance().addHandler(AbstractAuthenticationErrorEvent.TYPE, this);

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
    apps.add(new HyperLinkApp());
    apps.add(new AgendaApp());
    apps.add(new FormsOnlineApp());
    apps.add(new ContactsApp());
    apps.add(new ClassifiedsApp());
    apps.add(new SurveyApp());
    apps.add(new NotificationsBoxApp());
    apps.add(new FaqApp());
    apps.add(new ResourcesManagerApp());
  }

  public static Page getMainPage() {
    if (mainPage == null) {
      mainPage = new Page();
    }
    return mainPage;
  }

  public static String getUserToken() {
    if (getUser() != null) {
      return getUser().getToken();
    } else if (userProfile != null) {
      return userProfile.getApiToken();
    } else {
      return null;
    }
  }

  public static SpMobil getInstance() {
    return instance;
  }

  /**
   * Auto login.
   * @param user
   * @param password
   */
  private static void login(final FullUserDTO user, final String password, Command attempt) {
    Notification.activityStart();
    AuthentificationManager.getInstance()
        .authenticateOnSilverpeas(user.getLogin(), password, user.getDomainId(), attempt);
  }

  public static void displayMainPage() {

    if (!Window.Location.getHref().contains("?locale=") &&
        !user.getLanguage().equalsIgnoreCase("fr")) {
      Window.Location.replace(Window.Location.getHref() + "?locale=" + user.getLanguage());
    }
    getMainPage().setUser(user);
    RootPanel.get().clear();
    RootPanel.get().add(getMainPage());
    PageHistory.getInstance().goTo(new HomePage());

    if ( (shortcutAppId != null && shortcutContentType != null && shortcutContentId != null) || shortcutContributionId != null
        || (shortcutContentType != null && shortcutContentType.equals("Component") && shortcutAppId != null) ) {
      ShortCutRouter.route(user, shortcutAppId, shortcutContentType, shortcutContentId, shortcutContributionId, shortcutRole);
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

              // caching for offline mode
              LocalStorageHelper.store(key, HomePageDTO.class, result);
              for (ShortCutLinkDTO shortCut : result.getShortCuts()) {
                CacheStorageHelper.store(shortCut.getIcon());
              }
            }
          };
      action.attempt();
    }
  }

  public static void displayTermsOfServicePage() {
    RootPanel.get().clear();
    RootPanel.get().add(getMainPage());
    SpMobil.getMainPage().setContent(new TermsOfServicePage());
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
    if (token != null) {
      ServicesLocator.getServiceNavigation().getUser(Cookies.getCookie("svpLogin"), Cookies.getCookie("defaultDomain"), new AsyncCallback
          <DetailUserDTO>() {
        @Override
        public void onFailure(final Throwable throwable) {
          FullUserDTO user = AuthentificationManager.getInstance().loadUser();
          if (user != null) {
            String password = AuthentificationManager.getInstance().decryptPassword(user.getPassword());
            if (password != null) {
              login(user, password, attempt);
            }
          } else {
            //Login
            tabletGesture(false);
            displayLoginPage(null);
          }
        }

        @Override
        public void onSuccess(final DetailUserDTO detailUserDTO) {
          setUser(detailUserDTO);
          setUserProfile(LocalStorageHelper.load(AuthentificationManager.USER_PROFIL, UserProfileDTO.class));

          ServicesLocator.getServiceTermsOfService().show(new MethodCallback<Boolean>() {
            @Override
            public void onFailure(final Method method, final Throwable throwable) {
              Notification.activityStop();
              EventBus.getInstance().fireEvent(new ErrorEvent(throwable));
            }

            @Override
            public void onSuccess(final Method method, final Boolean showTerms) {
              if (showTerms) {
                SpMobil.displayTermsOfServicePage();
              } else {
                SpMobil.displayMainPage();
              }
            }
          });
        }
      });
    } else {
      //Login
      tabletGesture(false);
      displayLoginPage(null);
    }
  }

  public static void tabletGesture(boolean connected) {
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

  public static void displayLoginPage(AuthenticationException error) {
    ConnexionPage connexionPage = new ConnexionPage();
    connexionPage.setAuthenticateError(error);
    RootPanel.get().clear();
    RootPanel.get().add(connexionPage);
  }

  public static void search(final String query) {
    Notification.activityStart();
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<ResultDTO>>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceSearch().search(query, this);
      }

      @Override
      public void onSuccess(final Method method, final List<ResultDTO> results) {
        super.onSuccess(method, results);
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

  @Override
  public void onAuthenticationError(final AbstractAuthenticationErrorEvent event) {
    displayLoginPage(((AuthenticationException) event.getException()));
  }
}
