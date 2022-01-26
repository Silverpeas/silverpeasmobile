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

package org.silverpeas.mobile.server.services;

import org.apache.commons.lang3.EnumUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.silverpeas.components.gallery.model.Media;
import org.silverpeas.components.gallery.model.MediaPK;
import org.silverpeas.components.gallery.service.MediaServiceProvider;
import org.silverpeas.components.quickinfo.model.News;
import org.silverpeas.core.SilverpeasException;
import org.silverpeas.core.admin.component.model.ComponentInst;
import org.silverpeas.core.admin.component.model.ComponentInstLight;
import org.silverpeas.core.admin.service.AdminException;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.space.SpaceInst;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.contribution.content.wysiwyg.service.WysiwygController;
import org.silverpeas.core.contribution.model.ContributionIdentifier;
import org.silverpeas.core.contribution.publication.model.PublicationDetail;
import org.silverpeas.core.contribution.publication.model.PublicationPK;
import org.silverpeas.core.contribution.publication.service.PublicationService;
import org.silverpeas.core.mylinks.model.LinkDetail;
import org.silverpeas.core.security.session.SessionInfo;
import org.silverpeas.core.security.session.SessionManagement;
import org.silverpeas.core.security.session.SessionManagementProvider;
import org.silverpeas.core.security.token.synchronizer.SynchronizerToken;
import org.silverpeas.core.util.SettingBundle;
import org.silverpeas.core.util.StringUtil;
import org.silverpeas.core.util.logging.SilverLogger;
import org.silverpeas.core.web.look.PublicationHelper;
import org.silverpeas.core.web.mvc.controller.MainSessionController;
import org.silverpeas.core.web.util.viewgenerator.html.GraphicElementFactory;
import org.silverpeas.mobile.server.helpers.DataURLHelper;
import org.silverpeas.mobile.server.services.helpers.FavoritesHelper;
import org.silverpeas.mobile.server.services.helpers.NewsHelper;
import org.silverpeas.mobile.server.services.helpers.NotificationsPushHelper;
import org.silverpeas.mobile.server.services.helpers.UserHelper;
import org.silverpeas.mobile.server.services.helpers.events.EventsHelper;
import org.silverpeas.mobile.server.services.helpers.events.NextEvents;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.HomePageDTO;
import org.silverpeas.mobile.shared.dto.RightDTO;
import org.silverpeas.mobile.shared.dto.ShortCutLinkDTO;
import org.silverpeas.mobile.shared.dto.almanach.CalendarEventDTO;
import org.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.navigation.Apps;
import org.silverpeas.mobile.shared.dto.navigation.HomePages;
import org.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;
import org.silverpeas.mobile.shared.dto.navigation.SpaceDTO;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.silverpeas.mobile.shared.exceptions.NavigationException;
import org.silverpeas.mobile.shared.services.navigation.ServiceNavigation;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * Service de gestion de la navigation dans les espaces et apps.
 *
 * @author svuillet
 */
public class ServiceNavigationImpl extends AbstractAuthenticateService
    implements ServiceNavigation {

  private static final long serialVersionUID = 1L;
  private OrganizationController organizationController = OrganizationController.get();

  private boolean isUserGUIMobileForTablets() {
    return getSettings().getBoolean("guiMobileForTablets", true);
  }

  @Override
  public void storeTokenMessaging(String token) throws NavigationException,AuthenticationException {
    checkUserInSession();
    NotificationsPushHelper.getInstance().storeToken(getUserInSession().getId(), token);
  }

  @Override
  public DetailUserDTO getUser(String login, String domainId)
      throws NavigationException, AuthenticationException {

    String id = null;
    try {
      id = Administration.get().getUserIdByLoginAndDomain(login, domainId);
      UserDetail user = Administration.get().getUserDetail(id);

      DetailUserDTO userDTO = UserHelper.getInstance().populate(user);
      userDTO = initSession(userDTO);

      String avatar = DataURLHelper.convertAvatarToUrlData(user.getAvatarFileName(),
          getSettings().getString("big.avatar.size", "40x"));
      userDTO.setAvatar(avatar);
      return userDTO;

    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
      throw new NavigationException(e);
    }
  }

  @Override
  public boolean setTabletMode() throws NavigationException, AuthenticationException {
    if (!isUserGUIMobileForTablets()) {
      getThreadLocalRequest().getSession().setAttribute("tablet", new Boolean(true));
      return true;
    }
    return false;
  }

  @Override
  public void logout() throws AuthenticationException {
    try {
      String token = getUserInSession().getToken();
      String url = getBaseUrl(getThreadLocalRequest()) + "/LogoutServlet?X-STKN=" + token;
      Connection c = Jsoup.connect(url);
      Document d = c.get();
    } catch (IOException e) {
      SilverLogger.getLogger(this).error(e);
    }
    getThreadLocalRequest().getSession().invalidate();

    // clear cache
    getThreadLocalResponse().setHeader("Clear-Site-Data","\"cache\", \"cookies\", \"storage\"");
  }

  private static String getBaseUrl(HttpServletRequest request) {
    String scheme = request.getScheme() + "://";
    String serverName = request.getServerName();
    String serverPort = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
    String contextPath = request.getContextPath();
    return scheme + serverName + serverPort + contextPath;
  }

  @Override
  public DetailUserDTO initSession(DetailUserDTO user) throws AuthenticationException {
    SynchronizerToken token =
        (SynchronizerToken) getThreadLocalRequest().getSession().getAttribute("X-STKN");
    if (user != null) {
      UserDetail usr = organizationController.getUserDetail(user.getId());
      setUserInSession(usr);
      DetailUserDTO dto = UserHelper.getInstance().populate(usr);

      if (token == null) {
        // web security turn off
        try {
          dto.setSessionKey(Administration.get().getUserFull(usr.getId()).getToken());
        } catch (Exception e) {
          SilverLogger.getLogger(this).error(e);
        }
      } else {
        dto.setSessionKey(token.getValue());
      }
      return dto;
    } else {
      return null;
    }
  }

  private void initSilverpeasSession() {
    MainSessionController controller = (MainSessionController) getThreadLocalRequest().getSession()
        .getAttribute(MainSessionController.MAIN_SESSION_CONTROLLER_ATT);
    if (controller == null) {
      SessionManagement sessionManagement = SessionManagementProvider.getSessionManagement();
      SessionInfo sessionInfo =
          sessionManagement.validateSession(getThreadLocalRequest().getSession().getId());
      if (sessionInfo.getSessionId() == null) {
        sessionInfo = sessionManagement.openSession(getUserInSession(), getThreadLocalRequest());
      }

      try {
        controller = new MainSessionController(sessionInfo, getThreadLocalRequest().getSession());
      } catch (SilverpeasException e) {
        SilverLogger.getLogger(this).error(e);
      }
      getThreadLocalRequest().getSession()
          .setAttribute(MainSessionController.MAIN_SESSION_CONTROLLER_ATT, controller);
    }

    GraphicElementFactory gef = (GraphicElementFactory) getThreadLocalRequest().getSession()
        .getAttribute(GraphicElementFactory.GE_FACTORY_SESSION_ATT);
    if (gef == null && controller != null) {
      gef = new GraphicElementFactory(controller);
      getThreadLocalRequest().getSession()
          .setAttribute(GraphicElementFactory.GE_FACTORY_SESSION_ATT, gef);
    }
  }

  @Override
  public String getUserToken() {
    String token = "";
    if (getUserInSession() != null) {
      token = getUserInSession().getToken();
    }
    return token;
  }

  @Override
  public HomePageDTO getHomePageData(String spaceId)
      throws NavigationException, AuthenticationException {
    checkUserInSession();
    initSilverpeasSession();

    String look = "";
    try {
      look = getMainSessionController().getFavoriteLook();
    } catch (Exception e) {
      look = "Initial";
      SilverLogger.getLogger(this).error(e);
    }

    SettingBundle settings = GraphicElementFactory.getLookSettings(look);

    HomePageDTO data = new HomePageDTO();
    if (spaceId == null) {
      data.setId("root");
    } else {
      data.setId(spaceId);
    }
    try {
      if (spaceId != null) {
        SpaceInst space = Administration.get().getSpaceInstById(spaceId);
        List<ComponentInst> apps = space.getAllComponentsInst();
        int hiddenApps = 0;
        for (ComponentInst app : apps) {
          if (app.isHidden()) hiddenApps++;
        }
        data.setSpaceName(space.getName(getUserInSession().getUserPreferences().getLanguage()));
      }

      int maxNews;
      if (spaceId == null) {
        maxNews = settings.getInteger("home.news.size", 3);
      } else {
        maxNews = settings.getInteger("home.news.size", 3);
      }
      List<News> lastNews =
          NewsHelper.getInstance().getLastNews(getUserInSession().getId(), spaceId);
      if (lastNews != null && lastNews.size() > maxNews) {
        lastNews = lastNews.subList(0, maxNews);
      }

      data.setNews(NewsHelper.getInstance().populate(lastNews, false));

      if (spaceId == null || spaceId.isEmpty()) {
        List<LinkDetail> links =
            FavoritesHelper.getInstance().getBookmarkPersoVisible(getUserInSession().getId());
        data.setFavorites(FavoritesHelper.getInstance().populate(links));
      }
      data.setSpacesAndApps(getSpacesAndApps(spaceId));


      // last publications
      if ((spaceId == null && getSettings().getBoolean("homepage.lastpublications", true)) ||
          (spaceId != null && getSettings().getBoolean("spacehomepage.lastpublications", true))) {
        try {
          SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
          ArrayList<PublicationDTO> lastPubs = new ArrayList<PublicationDTO>();
          int max;
          if (spaceId == null) {
            max = settings.getInteger("home.publications.nb", 3);
          } else {
            max = settings.getInteger("space.homepage.latestpublications.nb", 3);
          }
          List<PublicationDetail> pubs = getPublicationHelper().getPublications(spaceId, max);
          for (PublicationDetail pub : pubs) {
            if (pub.canBeAccessedBy(getUserInSession())) {
              PublicationDTO dto = new PublicationDTO();
              dto.setId(pub.getId());
              dto.setName(pub.getName());
              dto.setUpdateDate(sdf.format(pub.getLastUpdateDate()));
              dto.setInstanceId(pub.getInstanceId());
              lastPubs.add(dto);
            }
          }
          data.setLastPublications(lastPubs);

        } catch (Exception e) {
          SilverLogger.getLogger(this).error(e);
        }
      }

      // aurora shortcuts
      List<ShortCutLinkDTO> shortCutLinkDTOList = new ArrayList<>();
      if (spaceId == null || spaceId.isEmpty()) {
        int i = 1;
        while (i > 0) {
          String url = settings.getString("Shortcut.home." + i + ".Url", "");
          if (!url.isEmpty()) {
            String text = settings.getString("Shortcut.home." + i + ".AltText", "");
            String icon = settings.getString("Shortcut.home." + i + ".IconUrl", "");
            ShortCutLinkDTO shortCutLinkDTO = new ShortCutLinkDTO();
            shortCutLinkDTO.setUrl(url);
            shortCutLinkDTO.setText(text);
            shortCutLinkDTO.setIcon(icon);
            shortCutLinkDTOList.add(shortCutLinkDTO);
          } else {
            i = 0;
            break;
          }
          i++;
        }
      }
      data.setShortCuts(shortCutLinkDTOList);

      // aurora tools
      shortCutLinkDTOList = new ArrayList<>();
      if (spaceId == null || spaceId.isEmpty()) {
        int i = 1;
        while (i > 0) {
          String url = settings.getString("Shortcut.tool." + i + ".Url", "");
          if (!url.isEmpty()) {
            String text = settings.getString("Shortcut.tool." + i + ".AltText", "");
            String icon = settings.getString("Shortcut.tool." + i + ".IconUrl", "");
            ShortCutLinkDTO shortCutLinkDTO = new ShortCutLinkDTO();
            shortCutLinkDTO.setUrl(url);
            shortCutLinkDTO.setText(text);
            shortCutLinkDTO.setIcon(icon);
            shortCutLinkDTOList.add(shortCutLinkDTO);
          } else {
            i = 0;
            break;
          }
          i++;
        }
      }
      data.setTools(shortCutLinkDTOList);



      // upcomming events
      NextEvents events = null;
      List<CalendarEventDTO> eventsToDisplay = null;
      String lang = getUserInSession().getUserPreferences().getLanguage();
      if ((spaceId == null && getSettings().getBoolean("homepage.lastevents", true))) {
        boolean includeToday = settings.getBoolean("home.events.today.include", true);
        List<String> allowedComponentIds =
            Arrays.asList(getAllowedComponentIds(settings, "home.events.appId", "almanach"));
        int nbDays = settings.getInteger("home.events.maxDays", 3);
        boolean onlyImportant = settings.getBoolean("home.events.importantOnly", false);
        events = EventsHelper.getInstance()
            .getNextEvents(allowedComponentIds, includeToday, nbDays, onlyImportant);
      } else if (spaceId != null && getSettings().getBoolean("spacehomepage.lastevents", true)) {
        List<String> allowedAppIds = new ArrayList<>();
        List<ComponentInstLight> components = getAllowedComponents(false, "almanach", spaceId);
        for (ComponentInstLight component : components) {
          allowedAppIds.add(component.getId());
        }
        events = EventsHelper.getInstance().getNextEvents(allowedAppIds, true, 5, false);
      }
      eventsToDisplay = EventsHelper.getInstance().populate(events, lang);
      data.setLastEvents(eventsToDisplay);

      // freezone

      if ((spaceId == null && getSettings().getBoolean("homepage.freezone", true))) {
        String pageWebAppId = settings.getString("home.freezone.appId", "");
        if (pageWebAppId != null && !pageWebAppId.isEmpty() && isComponentAvailable(pageWebAppId)) {
          String html = WysiwygController.loadForReadOnly(pageWebAppId, pageWebAppId, lang);
          data.setHtmlFreeZone(html);
        }
      } else if (spaceId != null) {
        SpaceInst space =Administration.get().getSpaceInstById(spaceId);
        if (space.getFirstPageType() == HomePages.URL.getValue()) {
          String html =
              "<iframe frameborder='0' onLoad='javaScript:this.height = this.contentWindow.document.body" +
                  ".scrollHeight ;' style='width:100%;' src='" +
                  space.getFirstPageExtraParam() + "'></iframe>";
          data.setHtmlFreeZone(html);
        }
      }

      // freezone thin
      if ((spaceId == null && getSettings().getBoolean("homepage.freezonethin", true))) {
        String pageWebAppId = settings.getString("home.freezone.thin.appId", "");
        if (pageWebAppId != null && !pageWebAppId.isEmpty() && isComponentAvailable(pageWebAppId)) {
          String html = WysiwygController.loadForReadOnly(pageWebAppId, pageWebAppId, lang);
          data.setHtmlFreeZoneThin(html);
        }
      }

    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
      throw new NavigationException(e);
    }

    return data;
  }

  private List<ComponentInstLight> getAllowedComponents(boolean visibleOnly, String name,
      String spaceId) {
    OrganizationController oc = OrganizationController.get();
    String[] appIds = oc.getAvailCompoIdsAtRoot(spaceId, getUserInSession().getId());
    List<ComponentInstLight> components = new ArrayList<>();
    Predicate<ComponentInstLight> canBeGet = c -> !visibleOnly || !c.isHidden();
    Predicate<ComponentInstLight> matchesName =
        c -> StringUtil.isNotDefined(name) || c.getName().equals(name);
    for (String appId : appIds) {
      ComponentInstLight component = oc.getComponentInstLight(appId);
      if (canBeGet.test(component) && matchesName.test(component)) {
        components.add(component);
      }
    }
    return components;
  }

  private String[] getAllowedComponentIds(SettingBundle settings, String param, String appName)
      throws AdminException {
    String paramValue = settings.getString(param, "");
    String[] appIds;
    if (paramValue.trim().equals("*")) {
      appIds = organizationController.getComponentIdsForUser(getUserInSession().getId(), appName);
    } else {
      appIds = StringUtil.split(paramValue, ' ');
    }
    return getAllowedComponents(appIds).toArray(new String[0]);
  }

  private List<String> getAllowedComponents(String... componentIds) throws AdminException {
    List<String> allowedComponentIds = new ArrayList<>();
    for (String componentId : componentIds) {
      if (isComponentAvailable(componentId)) {
        allowedComponentIds.add(componentId);
      }
    }
    return allowedComponentIds;
  }

  private boolean isComponentAvailable(String componentId) throws AdminException {
    return Administration.get().isComponentAvailableToUser(componentId, getUserInSession().getId());
  }

  private boolean isSupportedApp(ComponentInstLight app) {
    if (EnumUtils.isValidEnum(Apps.class, app.getName())) {
      String [] supportedApps = getSettings().getList("apps.supported",",");
      String [] appsExcluded = getSettings().getList("apps.exclude.intances",",");
      if (Arrays.asList(appsExcluded).contains(app.getId())) {
        return false;
      }

      if (Arrays.asList(supportedApps).contains(app.getName())) {
        return true;
      } else {
        return false;
      }
    }
    return isWorkflowApp(app);
  }

  private boolean isWorkflowApp(ComponentInstLight app) {
    try {
      return app.isWorkflow();
    } catch (Throwable t) {
      return false;
    }
  }

  @Override
  public boolean isWorkflowApp(String intanceId) throws NavigationException, AuthenticationException {
    try {
      ComponentInstLight app = Administration.get().getComponentInstLight(intanceId);
      return app.isWorkflow();
    } catch(Throwable t) {
      throw new NavigationException(t);
    }
  }

  //TODO : remove appType
  @Override
  public List<SilverpeasObjectDTO> getSpacesAndApps(String rootSpaceId)
      throws NavigationException, AuthenticationException {
    checkUserInSession();
    ArrayList<SilverpeasObjectDTO> results = new ArrayList<SilverpeasObjectDTO>();
    try {
      if (rootSpaceId == null) {
        String[] spaceIds = Administration.get().getAllSpaceIds(getUserInSession().getId());
        for (String spaceId : spaceIds) {
          SpaceInst space = Administration.get().getSpaceInstById(spaceId);
          if (space.getLevel() == 0) {
            if (containApp(space)) {
              results.add(populate(space));
            }
          }
        }
        Collections.sort(results);
      } else {
        String[] spaceIds =
            Administration.get().getAllowedSubSpaceIds(getUserInSession().getId(), rootSpaceId);
        for (String spaceId : spaceIds) {
          SpaceInst space = Administration.get().getSpaceInstById(spaceId);
          if (("WA" + space.getDomainFatherId()).equals(rootSpaceId) || space.getDomainFatherId().equals(rootSpaceId)) {
            if (containApp(space)) {
              results.add(populate(space));
            }
          }
        }
        Collections.sort(results);
        ArrayList<SilverpeasObjectDTO> partialResults = new ArrayList<SilverpeasObjectDTO>();
        String[] appsIds =
            Administration.get().getAvailCompoIds(rootSpaceId, getUserInSession().getId());
        for (String appId : appsIds) {
          ComponentInstLight app = Administration.get().getComponentInstLight(appId);
          if (isSupportedApp(app) && app.getDomainFatherId().equals(rootSpaceId)) {
            if (!app.isHidden()) {
              partialResults.add(populate(app));
            }
          }
        }
        Collections.sort(partialResults);
        results.addAll(partialResults);
      }

    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
    }
    return results;
  }

  @Override
  public ApplicationInstanceDTO getApp(String instanceId, String contentId, String contentType)
      throws NavigationException, AuthenticationException {
    String localId = "";
    if (instanceId == null) {
      if (contentType.equals(ContentsTypes.Publication.name())) {
        PublicationDetail pub = PublicationService.get().getDetail(new PublicationPK(contentId));
        instanceId = pub.getInstanceId();
      } else if (contentType.equals(ContentsTypes.Media.name())) {
        Media media = MediaServiceProvider.getMediaService().getMedia(new MediaPK(contentId));
        instanceId = media.getInstanceId();
      } else if (contentType.equals(ContentsTypes.Event.name())) {
        ContributionIdentifier contributionId =
            ContributionIdentifier.decode(new String(StringUtil.fromBase64(contentId)));
        localId = contributionId.getLocalId();
        instanceId = contributionId.getComponentInstanceId();
      }
    }
    ApplicationInstanceDTO dto = getApplicationInstanceDTO(instanceId);
    dto.setExtraId(localId);
    return dto;
  }

  private ApplicationInstanceDTO getApplicationInstanceDTO(final String instanceId) {
    ApplicationInstanceDTO dto = null;
    try {
      ComponentInstLight app = Administration.get().getComponentInstLight(instanceId);
      dto = populate(app);
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
    }
    return dto;
  }

  private boolean containApp(SpaceInst space) throws Exception {
    String[] appsIds =
        Administration.get().getAvailCompoIds(space.getId(), getUserInSession().getId());
    for (String appId : appsIds) {
      ComponentInstLight app = Administration.get().getComponentInstLight(appId);
      if (isSupportedApp(app)) {
        return true;
      }
    }
    return false;
  }

  private String[] getUserRoles(String componentId, String userId) {
    return organizationController.getUserProfiles(userId, componentId);
  }

  private SpaceDTO populate(SpaceInst space) {
    SpaceDTO dto = new SpaceDTO();
    dto.setId(space.getId());
    dto.setLabel(space.getName());
    dto.setPersonal(space.isPersonalSpace());
    dto.setOrderNum(space.getOrderNum());

    if (space.getFirstPageType() == HomePages.APP.getValue()) {
      List<ComponentInst> apps = space.getAllComponentsInst();
      int nbHiddenApps = 0;
      String appNotHidden = "";
      if (apps.size() == 1) {
        dto.setHomePageType(HomePages.APP.getValue());
      } else {
        for (ComponentInst app : apps) {
          if (app.isHidden()) {
            nbHiddenApps++;
          } else {
            appNotHidden = app.getId();
          }
        }
        if (apps.size() == nbHiddenApps || (apps.size() -1 == nbHiddenApps && appNotHidden.equals(space.getFirstPageExtraParam()))) {
          dto.setHomePageType(HomePages.APP.getValue());
        } else {
          dto.setHomePageType(HomePages.SILVERPEAS.getValue());
        }
      }
    } else {
      dto.setHomePageType(space.getFirstPageType());    }
    dto.setHomePageParameter(space.getFirstPageExtraParam());

    return dto;
  }

  private ApplicationInstanceDTO populate(ComponentInstLight app) {
    ApplicationInstanceDTO dto = new ApplicationInstanceDTO();
    dto.setId(app.getId());
    dto.setLabel(app.getLabel());
    dto.setType(app.getName());
    dto.setOrderNum(app.getOrderNum());
    dto.setWorkflow(isWorkflowApp(app));

    RightDTO rights = new RightDTO();
    String[] roles = getUserRoles(app.getId(), getUserInSession().getId());
    for (int i = 0; i < roles.length; i++) {
      if (roles[i].equals("admin")) {
        rights.setManager(true);
      }
      if (roles[i].equals("publisher")) {
        rights.setPublisher(true);
      }
      if (roles[i].equals("writer")) {
        rights.setWriter(true);
      }
      if (roles[i].equals("user")) {
        rights.setReader(true);
      }
    }
    dto.setRights(rights);

    try {
      String value = "";
      try {
        value = getMainSessionController().getComponentParameterValue(app.getId(), "notifications");
        dto.setNotifiable(value.equals("yes"));
      } catch (Exception e) {
        dto.setNotifiable(false);
      }
      if (app.getName().equals("kmelia")) {
        value = getMainSessionController().getComponentParameterValue(app.getId(), "tabComments");
      } else if (app.getName().equals("gallery") || app.getName().equals("quickinfo")) {
        value = getMainSessionController().getComponentParameterValue(app.getId(), "comments");
      }

      if (app.getName().equals("kmelia")) {
        dto.setCommentable(value.equals("yes"));
      } else if (app.getName().equals("gallery")) {
        dto.setCommentable(true);
        dto.setNotifiable(true);
      } else if (app.getName().equals("blog")) {
        dto.setCommentable(true);
        dto.setNotifiable(true);
      } else if (app.getName().equals("quickinfo")) {
        dto.setCommentable(value.equals("yes"));
        dto.setNotifiable(true);
      }
    } catch (Exception e) {
      dto.setCommentable(false);
    }

    try {
      String value = "";
      if (app.getName().equals("kmelia")) {
        value = getMainSessionController().getComponentParameterValue(app.getId(), "tabContent");
      }
      dto.setAbleToStoreContent(value.equals("yes"));
    } catch (Exception e) {
      dto.setAbleToStoreContent(false);
    }

    return dto;
  }


  private PublicationHelper getPublicationHelper() throws Exception {
    SettingBundle settings =
        GraphicElementFactory.getLookSettings(GraphicElementFactory.DEFAULT_LOOK_NAME);
    String helperClassName = settings
        .getString("publicationHelper", "org.silverpeas.components.kmelia.KmeliaTransversal");
    Class<?> helperClass = Class.forName(helperClassName);
    PublicationHelper kmeliaTransversal = (PublicationHelper) helperClass.newInstance();
    kmeliaTransversal.setMainSessionController(getMainSessionController());

    return kmeliaTransversal;
  }

}
