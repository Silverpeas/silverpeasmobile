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

package org.silverpeas.mobile.server.services;

import org.apache.commons.lang3.EnumUtils;
import org.silverpeas.components.gallery.model.Media;
import org.silverpeas.components.gallery.model.MediaPK;
import org.silverpeas.components.gallery.service.MediaServiceProvider;
import org.silverpeas.components.quickinfo.model.News;
import org.silverpeas.core.admin.component.model.ComponentInst;
import org.silverpeas.core.admin.component.model.ComponentInstLight;
import org.silverpeas.core.admin.service.AdminException;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.space.SpaceInst;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.annotation.WebService;
import org.silverpeas.core.contribution.content.wysiwyg.service.WysiwygController;
import org.silverpeas.core.contribution.model.ContributionIdentifier;
import org.silverpeas.core.contribution.publication.model.PublicationDetail;
import org.silverpeas.core.contribution.publication.model.PublicationPK;
import org.silverpeas.core.contribution.publication.service.PublicationService;
import org.silverpeas.core.mylinks.model.LinkDetail;
import org.silverpeas.core.security.token.synchronizer.SynchronizerToken;
import org.silverpeas.core.util.SettingBundle;
import org.silverpeas.core.util.StringUtil;
import org.silverpeas.core.util.logging.SilverLogger;
import org.silverpeas.core.web.look.PublicationHelper;
import org.silverpeas.core.web.rs.UserPrivilegeValidation;
import org.silverpeas.core.web.rs.annotation.Authorized;
import org.silverpeas.core.web.util.viewgenerator.html.GraphicElementFactory;
import org.silverpeas.mobile.server.helpers.DataURLHelper;
import org.silverpeas.mobile.server.services.helpers.FavoritesHelper;
import org.silverpeas.mobile.server.services.helpers.NewsHelper;
import org.silverpeas.mobile.server.services.helpers.NotificationsPushHelper;
import org.silverpeas.mobile.server.services.helpers.UserHelper;
import org.silverpeas.mobile.server.services.helpers.events.EventsHelper;
import org.silverpeas.mobile.server.services.helpers.events.NextEvents;
import org.silverpeas.mobile.shared.dto.*;
import org.silverpeas.mobile.shared.dto.almanach.CalendarEventDTO;
import org.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import org.silverpeas.mobile.shared.dto.navigation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
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
@WebService
@Authorized
@Path(ServiceNavigation.PATH)
public class ServiceNavigation extends AbstractRestWebService {

  @Context
  HttpServletRequest request;

  static final String PATH = "mobile/navigation";


  private final OrganizationController organizationController = OrganizationController.get();

  private boolean isUserGUIMobileForTablets() {
    return getSettings().getBoolean("guiMobileForTablets", true);
  }

  @PUT
  @Path("storeTokenMessaging/{token}/")
  public void storeTokenMessaging(@PathParam("token") String token) {
    NotificationsPushHelper.getInstance().storeToken(getUser().getId(), token);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("user/{login}/{domainId}/")
  public DetailUserDTO getUser(@PathParam("login") String login,
      @PathParam("domainId") String domainId) {

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
      throw new WebApplicationException(e);
    }
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Path("setTabletMode")
  public Boolean setTabletMode() {
    if (!isUserGUIMobileForTablets()) {
      request.getSession().setAttribute("tablet", Boolean.valueOf(true));
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }

  @GET
  @Path("clearAppCache")
  public void clearAppCache() {
    // clear cache
    getHttpServletResponse().setHeader("Clear-Site-Data", "\"cache\", \"cookies\", \"storage\"");
  }

  private static String getBaseUrl(HttpServletRequest request) {
    String scheme = request.getScheme() + "://";
    String serverName = request.getServerName();
    String serverPort = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
    String contextPath = request.getContextPath();
    return scheme + serverName + serverPort + contextPath;
  }

  private DetailUserDTO initSession(DetailUserDTO user) {
    SynchronizerToken token = (SynchronizerToken) request.getSession().getAttribute("X-STKN");
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

  protected void setUserInSession(UserDetail user) {
    request.getSession().setAttribute(AbstractAuthenticateService.USER_ATTRIBUT_NAME, user);
  }

  protected UserDetail getUserInSession() {
    return (UserDetail) request.getSession().getAttribute(AbstractAuthenticateService.USER_ATTRIBUT_NAME);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("homepage/{spaceId}/")
  public HomePageDTO getHomePageData(@PathParam("spaceId") String spaceId) {
    if (spaceId.equals("null")) spaceId = null;

    initSilverpeasSession(request);
    request.getSession().setAttribute("Silverpeas_Portlet_SpaceId", spaceId);

    String look = "";
    try {
      look = getMainSessionController().getFavoriteLook();
    } catch (Exception e) {
      look = "Initial";
      SilverLogger.getLogger(this).error(e);
    }

    SettingBundle settings = GraphicElementFactory.getLookSettings(look);

    HomePageDTO data = new HomePageDTO();

    data.setNewsDisplayer(
        settings.getString("home.news.displayer", HomePageDTO.NEWS_DISPLAYER_CARROUSEL));

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
          if (app.isHidden()) {
            hiddenApps++;
          }
        }
        data.setSpaceName(space.getName(getUser().getUserPreferences().getLanguage()));
      }

      int maxNews;
      if (spaceId == null) {
        maxNews = settings.getInteger("home.news.size", 3);
      } else {
        maxNews = settings.getInteger("home.news.size", 3);
      }
      List<News> lastNews = NewsHelper.getInstance().getLastNews(getUser().getId(), spaceId, maxNews);
      data.setNews(NewsHelper.getInstance().populate(lastNews, false));

      if (spaceId == null || spaceId.isEmpty()) {
        List<LinkDetail> links =
            FavoritesHelper.getInstance().getBookmarkPersoVisible(getUser().getId());
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
            if (pub.canBeAccessedBy(getUser())) {
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
      String lang = getUser().getUserPreferences().getLanguage();
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
        SpaceInst space = Administration.get().getSpaceInstById(spaceId);
        if (space.getFirstPageType() == HomePages.URL.getValue() &&
                getSettings().getBoolean("spacehomepage.displayUrlType")) {
          String url = space.getFirstPageExtraParam();
          if (url.startsWith("/") && !url.startsWith("/silverpeas") && !url.startsWith("$")) url = "/silverpeas" + url;
          String html =
                  "<iframe frameborder='0' style='width:100vw;height:100vh' src='" + url +
                          "'></iframe>";
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
      throw new WebApplicationException(e);
    }

    return data;
  }

  private List<ComponentInstLight> getAllowedComponents(boolean visibleOnly, String name,
      String spaceId) {
    OrganizationController oc = OrganizationController.get();
    String[] appIds = oc.getAvailCompoIdsAtRoot(spaceId, getUser().getId());
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
      appIds = organizationController.getComponentIdsForUser(getUser().getId(), appName);
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
    return Administration.get().isComponentAvailableToUser(componentId, getUser().getId());
  }

  private boolean isSupportedApp(ComponentInstLight app) {
    if (EnumUtils.isValidEnum(Apps.class, app.getName())) {
      String[] supportedApps = getSettings().getList("apps.supported", ",");
      String[] appsExcluded = getSettings().getList("apps.exclude.intances", ",");
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

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("isWorkflowApp/{instanceId}/")
  public Boolean isWorkflowApp(@PathParam("instanceId") String intanceId) {
    try {
      ComponentInstLight app = Administration.get().getComponentInstLight(intanceId);
      return app.isWorkflow();
    } catch (Throwable t) {
      throw new WebApplicationException(t);
    }
  }

  //TODO : remove appType
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("spacesAndApps/{rootSpaceId}/")
  public List<SilverpeasObjectDTO> getSpacesAndApps(@PathParam("rootSpaceId") String rootSpaceId) {
    ArrayList<SilverpeasObjectDTO> results = new ArrayList<>();
    if (rootSpaceId !=null && rootSpaceId.equals("null")) rootSpaceId = null;
    try {
      if (rootSpaceId == null) {
        String[] spaceIds = Administration.get().getAllRootSpaceIds(getUser().getId());
        for (String spaceId : spaceIds) {
          SpaceInst space = Administration.get().getSpaceInstById(spaceId);
          if (!space.isRemoved()) {
            if (space.getLevel() == 0) {
              if (containApp(space)) {
                results.add(populate(space));
              }
            }
          }
        }
        Collections.sort(results);
      } else {
        String[] spaceIds =
            Administration.get().getAllowedSubSpaceIds(getUser().getId(), rootSpaceId);
        for (String spaceId : spaceIds) {
          SpaceInst space = Administration.get().getSpaceInstById(spaceId);
          if (!space.isRemoved()) {
            if (("WA" + space.getDomainFatherId()).equals(rootSpaceId) ||
                space.getDomainFatherId().equals(rootSpaceId)) {
              if (containApp(space)) {
                results.add(populate(space));
              }
            }
          }
        }
        Collections.sort(results);
        ArrayList<SilverpeasObjectDTO> partialResults = new ArrayList<>();
        String[] appsIds = Administration.get().getAvailCompoIds(rootSpaceId, getUser().getId());
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

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("app/{instanceId}/{contentId}/{contentType}/")
  public ApplicationInstanceDTO getApp(@PathParam("instanceId") String instanceId,
      @PathParam("contentId") String contentId, @PathParam("contentType") String contentType) {
    if (instanceId.equals("null")) instanceId = null;
    String localId = "";
    if (instanceId == null) {
      if (contentType.equals(ContentsTypes.Publication.name())) {
        PublicationDetail pub = PublicationService.get().getDetail(new PublicationPK(contentId));
        if (pub == null) throw new NotFoundException();
        instanceId = pub.getInstanceId();
      } else if (contentType.equals(ContentsTypes.Media.name())) {
        Media media = MediaServiceProvider.getMediaService().getMedia(new MediaPK(contentId));
        if (media == null) throw new NotFoundException();
        instanceId = media.getInstanceId();
      } else if (contentType.equals(ContentsTypes.Event.name())) {
        ContributionIdentifier contributionId =
            ContributionIdentifier.decode(new String(StringUtil.fromBase64(contentId)));
        localId = contributionId.getLocalId();
        instanceId = contributionId.getComponentInstanceId();
        if (instanceId.equals("?")) throw new NotFoundException();
      }
    }
    ApplicationInstanceDTO dto = getApplicationInstanceDTO(instanceId);
    if (dto == null) throw new NotFoundException();
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
    String[] appsIds = Administration.get().getAvailCompoIds(space.getId(), getUser().getId());
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
        if (apps.size() == nbHiddenApps || (apps.size() - 1 == nbHiddenApps &&
            appNotHidden.equals(space.getFirstPageExtraParam()))) {
          dto.setHomePageType(HomePages.APP.getValue());
        } else {
          dto.setHomePageType(HomePages.SILVERPEAS.getValue());
        }
      }
    } else {
      dto.setHomePageType(space.getFirstPageType());
    }
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
    String[] roles = getUserRoles(app.getId(), getUser().getId());
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

    if (app.getName().equals("kmelia")) {
      try {
        String value = "";
        value = getMainSessionController().getComponentParameterValue(app.getId(), "useFolderSharing");
        dto.setFolderSharing(Integer.parseInt(value));
        value = getMainSessionController().getComponentParameterValue(app.getId(), "usePublicationSharing");
        dto.setPublicationSharing(Integer.parseInt(value));
        value = getMainSessionController().getComponentParameterValue(app.getId(), "useFileSharing");
        dto.setFileSharing(Integer.parseInt(value));
      } catch(Exception e) {
        dto.setFolderSharing(0);
        dto.setPublicationSharing(0);
        dto.setFileSharing(0);
      }
    }

    return dto;
  }


  private PublicationHelper getPublicationHelper() throws Exception {
    SettingBundle settings =
        GraphicElementFactory.getLookSettings(GraphicElementFactory.DEFAULT_LOOK_NAME);
    String helperClassName = settings.getString("publicationHelper",
        "org.silverpeas.components.kmelia.KmeliaTransversal");
    Class<?> helperClass = Class.forName(helperClassName);
    PublicationHelper kmeliaTransversal =
        (PublicationHelper) helperClass.getDeclaredConstructor().newInstance();
    kmeliaTransversal.setMainSessionController(getMainSessionController());

    return kmeliaTransversal;
  }

  @Override
  protected String getResourceBasePath() {
    return PATH;
  }

  @Override
  public String getComponentId() {
    return null;
  }

  @Override
  public void validateUserAuthorization(final UserPrivilegeValidation validation) {
  }

}
