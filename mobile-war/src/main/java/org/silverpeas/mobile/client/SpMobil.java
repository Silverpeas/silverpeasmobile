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

package org.silverpeas.mobile.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
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
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
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
import org.silverpeas.mobile.client.apps.sharesbox.SharesBoxApp;
import org.silverpeas.mobile.client.apps.survey.SurveyApp;
import org.silverpeas.mobile.client.apps.tasks.TasksApp;
import org.silverpeas.mobile.client.apps.webpage.WebPageApp;
import org.silverpeas.mobile.client.apps.workflow.WorkflowApp;
import org.silverpeas.mobile.client.common.*;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.event.ErrorEvent;
import org.silverpeas.mobile.client.common.event.ExceptionEvent;
import org.silverpeas.mobile.client.common.event.authentication.AbstractAuthenticationErrorEvent;
import org.silverpeas.mobile.client.common.event.authentication.AuthenticationEventHandler;
import org.silverpeas.mobile.client.common.mobil.MobilUtils;
import org.silverpeas.mobile.client.common.mobil.Orientation;
import org.silverpeas.mobile.client.common.navigation.LinksManager;
import org.silverpeas.mobile.client.common.navigation.PageHistory;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.common.network.NetworkHelper;
import org.silverpeas.mobile.client.common.resources.ResourcesManager;
import org.silverpeas.mobile.client.common.storage.CacheStorageHelper;
import org.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import org.silverpeas.mobile.client.components.Snackbar;
import org.silverpeas.mobile.client.components.base.Page;
import org.silverpeas.mobile.client.components.base.events.window.OrientationChangeEvent;
import org.silverpeas.mobile.client.pages.connexion.ConnexionPage;
import org.silverpeas.mobile.client.pages.cookies.CookiesPage;
import org.silverpeas.mobile.client.pages.main.HomePage;
import org.silverpeas.mobile.client.pages.search.SearchResultPage;
import org.silverpeas.mobile.client.pages.termsofservice.TermsOfServicePage;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.FullUserDTO;
import org.silverpeas.mobile.shared.dto.HomePageDTO;
import org.silverpeas.mobile.shared.dto.ShortCutLinkDTO;
import org.silverpeas.mobile.shared.dto.authentication.IUserProfile;
import org.silverpeas.mobile.shared.dto.authentication.UserProfileDTO;
import org.silverpeas.mobile.shared.dto.configuration.Config;
import org.silverpeas.mobile.shared.dto.configuration.IConfig;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.search.ResultDTO;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpMobil implements EntryPoint, AuthenticationEventHandler {

  private static Page mainPage = null;
  private static DetailUserDTO user;
  private static UserProfileDTO userProfile;
  private static int nbRetryLogin = 0;
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

  private static boolean SSO = false;
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

  public static void setUser(final DetailUserDTO user, boolean displayPersonnalApps) {
    SpMobil.user = user;
    if(displayPersonnalApps) displayPersonnalApps();
  }


  /**
   * Init. spmobile.
   */
  public void onModuleLoad() {
    msg = GWT.create(ApplicationMessages.class);

    exportNativeFunctions();
    checkVersion();
    checkInstallation();

    // init connexion supervision
    NetworkHelper.getInstance();

    setFontSize(getConfiguration().getFontSize());
    setFilter(getConfiguration());

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

    EventBus.getInstance().addHandler(ExceptionEvent.TYPE, new ErrorManager());
    EventBus.getInstance().addHandler(AbstractAuthenticationErrorEvent.TYPE, this);

    SSO = !ResourcesManager.getSSOPath().isEmpty();

    displayFirstPage();

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
    apps.add(new SharesBoxApp());
    apps.add(new FaqApp());
    apps.add(new ResourcesManagerApp());
  }

  private static void checkInstallation() {
    PWAHelper.detectAppInstallation();

    Command execNo = new Command() {
      @Override
      public void execute() {
        Date expireAt = new Date();
        long delay = 60*1000*60*24*30;
        expireAt.setTime(expireAt.getTime()+delay);
        Cookies.setCookie("ask_install", "no", expireAt);
      }
    };
    Command execYes = new Command() {
      @Override
      public void execute() {
        PWAHelper.installApp();
      }
    };

    Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
      @Override
      public boolean execute() {
        String cookie = Cookies.getCookie("ask_install");
        if (!PWAHelper.isInstalledApp() && (cookie == null || cookie.isEmpty())) {
          Snackbar.showConfirmation(msg.installation(), execYes, execNo);
        }
        return false;
      }
    }, 1000);
  }

  private static void checkVersion() {
    String  buildDate = ResourcesManager.getParam("build.date");
    String lastBuild = LocalStorageHelper.getInstance().load("build.date");
    if (lastBuild != null && !buildDate.equals(lastBuild)) {
      // clear cache
      AuthentificationManager.getInstance().clearCache();
    }
    if (lastBuild == null || lastBuild.isEmpty()) LocalStorageHelper.getInstance().storeBuildDate();
  }

  public void displayFirstPage() {
    checkVersion();
    boolean displayCookiesInformation = Boolean.parseBoolean(ResourcesManager.getParam("displayCookiesInformation"));
    String cookie = Cookies.getCookie("accept_cookies");
    if (displayCookiesInformation && (cookie == null || cookie.isEmpty())) {
      displayCookiesPage();
    } else {
      loadIds(null);
    }
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
   *
   * @param user
   * @param password
   */
  private static void login(final FullUserDTO user, final String password, Command attempt) {
    Notification.activityStart();
    AuthentificationManager.getInstance()
        .authenticateOnSilverpeas(user.getLogin(), password, user.getDomainId(), attempt);
  }

  public static void displayMainPage() {
    SpMobil.getMainPage().showFooter();
    if (!Window.Location.getHref().contains("?locale=") &&
        !user.getLanguage().equalsIgnoreCase("fr")) {
      Window.Location.replace(Window.Location.getHref() + "?locale=" + user.getLanguage());
    }
    getMainPage().setUser(user);
    RootPanel.get().clear();
    RootPanel.get().add(getMainPage());
    PageHistory.getInstance().clear();
    PageHistory.getInstance().goTo(new HomePage());

    if ((shortcutContentType != null && shortcutContentId != null) ||
            (shortcutAppId != null && shortcutContentType != null && shortcutContentId != null) ||
            shortcutContributionId != null ||
            (shortcutContentType != null && (shortcutContentType.equals("Component") ||
                    shortcutContentType.equals("Space")) && shortcutAppId != null)) {
      ShortCutRouter.route(user, shortcutAppId, shortcutContentType, shortcutContentId,
          shortcutContributionId, shortcutRole);
    } else if (shortcutContentType != null && shortcutContentType.equalsIgnoreCase("Url") && shortcutAppId != null) {
      LinksManager.openIframePage(shortcutAppId);
    } else {
      MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<HomePageDTO>() {

        @Override
        public void attempt() {
          ServicesLocator.getServiceNavigation().getHomePageData(null, SpMobil.getContentZoomLevel(), this);
        }

        @Override
        public void onSuccess(final Method method, final HomePageDTO result) {
          super.onSuccess(method, result);
          // send event to main home page
          EventBus.getInstance().fireEvent(new HomePageLoadedEvent(result));

          // caching for offline mode
          for (ShortCutLinkDTO shortCut : result.getShortCuts()) {
            CacheStorageHelper.store(shortCut.getIcon());
          }
        }
      };
      action.attempt();
    }
  }

  private static void displayPersonnalApps () {
    if (getUser() != null && !getMainPage().isPersonalAppsInitialized()) {
      MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<ApplicationInstanceDTO>>() {
        @Override
        public void attempt() {
          ServicesLocator.getServiceNavigation().getPersonnalSpaceContent(getUser().getId(), this);
        }

        @Override
        public void onSuccess(Method method, List<ApplicationInstanceDTO> applicationInstanceDTOS) {
          getMainPage().setPersonalApps(applicationInstanceDTOS);
        }
      };
      action.attempt();
    }
  }

  public static void displayTermsOfServicePage() {
    RootPanel.get().clear();
    RootPanel.get().add(getMainPage());
    SpMobil.getMainPage().setContent(new TermsOfServicePage(), true);
  }

  public static void displayCookiesPage() {
    RootPanel.get().clear();
    RootPanel.get().add(getMainPage());
    SpMobil.getMainPage().setContent(new CookiesPage(), true);
  }

  private String getInputValueById(String id) {
    Element el = Document.get().getElementById(id);
    if (el != null) return el.getAttribute("value");
    return "";
  }

  /**
   * Load ids in SQL Web Storage.
   */
  public void loadIds(Command attempt) {
    FullUserDTO u = AuthentificationManager.getInstance().loadUser();
    if (u != null) {
      tryToRelogin(null);
    } else {
      String login = getInputValueById("svpLogin");
      String domainId = getInputValueById("defaultDomain");
      if (login != null && domainId != null && !login.isEmpty() && !domainId.isEmpty()) {
        //SSO
        MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<DetailUserDTO>() {
          @Override
          public void attempt() {
            super.attempt();
            ServicesLocator.getServiceNavigation().getUser(login, domainId, this);
          }

          @Override
          public void onFailure(Method method, Throwable t) {
            tabletGesture(false);
            displayLoginPage(null);
          }

          @Override
          public void onSuccess(Method method, DetailUserDTO detailUserDTO) {
            super.onSuccess(method, detailUserDTO);
            setUser(detailUserDTO, true);
            setUserProfile(UserProfileDTO.getBean(
                    LocalStorageHelper.getInstance().load(AuthentificationManager.USER_PROFIL, IUserProfile.class)));
            if (getUserProfile() == null) {
              UserProfileDTO p = new UserProfileDTO();
              p.setFullName(detailUserDTO.getFirstName() + " " + detailUserDTO.getLastName());
              p.setAvatar(detailUserDTO.getAvatar());
              p.setFirstName(detailUserDTO.getFirstName());
              p.setLastName(detailUserDTO.getLastName());
              p.setLanguage(detailUserDTO.getLanguage());
              p.setId(detailUserDTO.getId());
              p.seteMail(detailUserDTO.geteMail());
              setUserProfile(p);
            }
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
        };
        action.attempt();
      } else {
        //Login
        tabletGesture(false);
        displayLoginPage(null);
      }
    }
  }

  public static boolean isSSO() {
    return SSO;
  }

  private void tryToRelogin(final Command attempt) {
    checkVersion();
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

  public static void tabletGesture(boolean connected) {
    if (MobilUtils.isTablet()) {
      if (connected) {

        MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<Boolean>() {
          @Override
          public void attempt() {
            super.attempt();
            ServicesLocator.getServiceNavigation().setTabletMode(this);
          }

          @Override
          public void onSuccess(final Method method, final Boolean desktopMode) {
            super.onSuccess(method, desktopMode);
            if (desktopMode) {
              String url = Window.Location.getHref();
              url = url.substring(0, url.indexOf("silverpeas") + "silverpeas".length());
              Window.Location.replace(url);
            }
          }
        };
        action.attempt();
      } else {
        MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<Boolean>() {
          @Override
          public void attempt() {
            super.attempt();
            ServicesLocator.getServiceConnection().setTabletMode(this);
          }

          @Override
          public void onSuccess(final Method method, final Boolean desktopMode) {
            super.onSuccess(method, desktopMode);
            if (desktopMode) {
              String url = Window.Location.getHref();
              url = url.substring(0, url.indexOf("silverpeas") + "silverpeas".length());
              Window.Location.replace(url);
            }
          }
        };
        action.attempt();
      }
    }
  }

  public static void displayLoginPage(AuthenticationException error) {
    checkVersion();
    ConnexionPage connexionPage = new ConnexionPage();
    connexionPage.setAuthenticateError(error);
    RootPanel.get().clear();
    PageHistory.getInstance().clear();
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
    AutoBean<IConfig> conf = LocalStorageHelper.getInstance().load("config", IConfig.class);
    Config config = null;
    if (conf == null) {
      config = Config.getDefaultConfig();
    } else {
      config = Config.getBean(conf);
    }

    return config;
  }

  public static String getContentZoomLevel() {
    double z = getConfiguration().getFontSize() / 10d;
    return String.valueOf(z);
  }

  @Override
  public void onAuthenticationError(final AbstractAuthenticationErrorEvent event) {
    displayLoginPage(((AuthenticationException) event.getException()));
  }

  public static native void exportNativeFunctions()/*-{
    $wnd.navigateTo = $entry(@org.silverpeas.mobile.client.common.navigation.LinksManager::navigateToPermalink(*));
  }-*/;

  public static void setFontSize(int value) {
    Document.get().getBody().setAttribute("style", "font-size:"+value+"pt;");
  }

  public static void setFilter(Config configuration) {
    if (configuration.isStandard()) {
      Document.get().getElementsByTagName("html").getItem(0).setAttribute("style","");
    } else if (configuration.isGrayscale()) {
      Document.get().getElementsByTagName("html").getItem(0).setAttribute("style","filter:grayscale(1);");
    } else if (configuration.isSepia()) {
      Document.get().getElementsByTagName("html").getItem(0).setAttribute("style","filter:sepia(1);");
    } else if (configuration.isInverse()) {
      Document.get().getElementsByTagName("html").getItem(0).setAttribute("style","filter:invert(1);");
    }
  }

}
