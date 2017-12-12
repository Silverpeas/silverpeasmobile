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

package org.silverpeas.mobile.server.services;

import org.apache.commons.lang3.EnumUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.silverpeas.components.gallery.model.Media;
import org.silverpeas.components.gallery.model.MediaPK;
import org.silverpeas.components.gallery.service.MediaServiceProvider;
import org.silverpeas.core.admin.component.model.ComponentInstLight;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.space.SpaceInstLight;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.contribution.publication.model.PublicationDetail;
import org.silverpeas.core.contribution.publication.model.PublicationPK;
import org.silverpeas.core.contribution.publication.service.PublicationService;
import org.silverpeas.core.mylinks.model.LinkDetail;
import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.SettingBundle;
import org.silverpeas.core.util.logging.SilverLogger;
import org.silverpeas.core.web.look.PublicationHelper;
import org.silverpeas.core.web.util.viewgenerator.html.GraphicElementFactory;
import org.silverpeas.mobile.server.common.SpMobileLogModule;
import org.silverpeas.mobile.server.services.helpers.FavoritesHelper;
import org.silverpeas.mobile.server.services.helpers.NewsHelper;
import org.silverpeas.mobile.server.services.helpers.UserHelper;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.HomePageDTO;
import org.silverpeas.mobile.shared.dto.RightDTO;
import org.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.navigation.Apps;
import org.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;
import org.silverpeas.mobile.shared.dto.navigation.SpaceDTO;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.silverpeas.mobile.shared.exceptions.NavigationException;
import org.silverpeas.mobile.shared.services.navigation.ServiceNavigation;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service de gestion de la navigation dans les espaces et apps.
 * @author svuillet
 */
public class ServiceNavigationImpl extends AbstractAuthenticateService implements ServiceNavigation {

  private static final long serialVersionUID = 1L;
  private static boolean showLastPublicationsOnHomePage;
  private static boolean showLastPublicationsOnSpaceHomePage;
  private static boolean useGUImobileForTablets;
  private OrganizationController organizationController = OrganizationController.get();

  static {
    SettingBundle mobileSettings = ResourceLocator.getSettingBundle("org.silverpeas.mobile.mobileSettings");
    showLastPublicationsOnHomePage = mobileSettings.getBoolean("homepage.lastpublications", true);
    showLastPublicationsOnSpaceHomePage = mobileSettings.getBoolean("spacehomepage.lastpublications", true);
    useGUImobileForTablets = mobileSettings.getBoolean("guiMobileForTablets", true);
  }

  @Override
  public boolean setTabletMode() throws NavigationException, AuthenticationException {
    if (!useGUImobileForTablets) {
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

    }
    getThreadLocalRequest().getSession().invalidate();
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
    Object token = getThreadLocalRequest().getSession().getAttribute("X-STKN");

    if (user != null) {
      UserDetail usr = organizationController.getUserDetail(user.getId());
      setUserInSession(usr);
      return UserHelper.getInstance().populate(usr);
    } else {
      return null;
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
  public HomePageDTO getHomePageData(String spaceId) throws NavigationException, AuthenticationException {
    checkUserInSession();
    HomePageDTO data = new HomePageDTO();
    data.setId(spaceId);
    try {
      if (spaceId != null) {
        SpaceInstLight space = Administration.get().getSpaceInstLightById(spaceId);
        data.setSpaceName(space.getName(getUserInSession().getUserPreferences().getLanguage()));
      }
    } catch (Exception e) {
      throw new NavigationException(e);
    }

    List<PublicationDetail> lastNews = NewsHelper
        .getInstance().getLastNews(getUserInSession().getId(), spaceId);
    data.setNews(NewsHelper.getInstance().populate(lastNews, false));

    if (spaceId == null || spaceId.isEmpty()) {
      List<LinkDetail> links = FavoritesHelper.getInstance().getBookmarkPersoVisible(getUserInSession().getId());
      data.setFavorites(FavoritesHelper.getInstance().populate(links));
    }
    data.setSpacesAndApps(getSpacesAndApps(spaceId));

    // last publications

    if ((spaceId == null && showLastPublicationsOnHomePage) || (spaceId != null && showLastPublicationsOnSpaceHomePage)) {
      try {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
        ArrayList<PublicationDTO> lastPubs = new ArrayList<PublicationDTO>();
        SettingBundle settings = GraphicElementFactory.getLookSettings(GraphicElementFactory.defaultLookName);
        int max;
        if (spaceId == null) {
          max = settings.getInteger("home.publications.nb", 3);
        } else {
          max = settings.getInteger("space.homepage.latestpublications.nb", 3);
        }
        List<PublicationDetail> pubs = getPublicationHelper().getPublications(spaceId, max);
        for (PublicationDetail pub : pubs) {
          PublicationDTO dto = new PublicationDTO();
          dto.setId(pub.getId());
          dto.setName(pub.getName());
          dto.setUpdateDate(sdf.format(pub.getUpdateDate()));
          dto.setInstanceId(pub.getInstanceId());
          lastPubs.add(dto);
        }
        data.setLastPublications(lastPubs);

      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return data;
  }

  private boolean isSupportedApp(ComponentInstLight app) {
    if (EnumUtils.isValidEnum(Apps.class, app.getName())) {
      return true;
    }
    return isWorkflowApp(app);
  }

  private boolean isWorkflowApp(ComponentInstLight app) {
    try {
      return app.isWorkflow();
    } catch(Throwable t) {
      return false;
    }
  }

  //TODO : remove appType
  @Override
  public List<SilverpeasObjectDTO> getSpacesAndApps(String rootSpaceId) throws NavigationException, AuthenticationException {
    checkUserInSession();
    ArrayList<SilverpeasObjectDTO> results = new ArrayList<SilverpeasObjectDTO>();
    try {
      if (rootSpaceId == null) {
        String [] spaceIds = Administration.get().getAllSpaceIds(getUserInSession().getId());
        for (String spaceId : spaceIds) {
          SpaceInstLight space = Administration.get().getSpaceInstLightById(spaceId);
          if (space.getFatherId().equals("0")) {
            if (containApp(space)) {
              results.add(populate(space));
            }
          }
        }
        Collections.sort(results);
      } else {
        String [] spaceIds = Administration.get().getAllowedSubSpaceIds(getUserInSession().getId(), rootSpaceId);
        for (String spaceId : spaceIds) {
          SpaceInstLight space = Administration.get().getSpaceInstLightById(spaceId);
          if (("WA"+space.getFatherId()).equals(rootSpaceId)) {
            if (containApp(space)) {
              results.add(populate(space));
            }
          }
        }
        Collections.sort(results);
        ArrayList<SilverpeasObjectDTO> partialResults = new ArrayList<SilverpeasObjectDTO>();
        String [] appsIds = Administration.get().getAvailCompoIds(rootSpaceId, getUserInSession().getId());
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
      SilverLogger.getLogger(SpMobileLogModule.getName()).error("ServiceNavigationImpl.getSpacesAndApps", "root.EX_NO_MESSAGE", e);
    }
    return results;
  }

  @Override
  public ApplicationInstanceDTO getApp(String instanceId, String contentId, String contentType) throws NavigationException, AuthenticationException {
    if (instanceId == null) {
      if (contentType.equals(ContentsTypes.Publication.name())) {
        PublicationDetail pub = PublicationService.get().getDetail(new PublicationPK(contentId));
        instanceId = pub.getInstanceId();
      } else if(contentType.equals(ContentsTypes.Media.name())) {
        Media media = MediaServiceProvider.getMediaService().getMedia(new MediaPK(contentId));
        instanceId = media.getInstanceId();
      }
    }
    return getApplicationInstanceDTO(instanceId);
  }

  private ApplicationInstanceDTO getApplicationInstanceDTO(final String instanceId) {
    ApplicationInstanceDTO dto = null;
    try {
      ComponentInstLight app = Administration.get().getComponentInstLight(instanceId);
      dto = populate(app);
    } catch (Exception e) {
      SilverLogger.getLogger(SpMobileLogModule.getName())
          .error("ServiceNavigationImpl.getApp", "root.EX_NO_MESSAGE", e);
    }
    return dto;
  }

  private boolean containApp(SpaceInstLight space) throws Exception {
    String [] appsIds = Administration.get().getAvailCompoIds(space.getId(), getUserInSession().getId());
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

  private SpaceDTO populate(SpaceInstLight space) {
    SpaceDTO dto = new SpaceDTO();
    dto.setId(space.getId());
    dto.setLabel(space.getName());
    dto.setPersonal(space.isPersonalSpace());
    dto.setOrderNum(space.getOrderNum());
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
      } catch(Exception e) {
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
    SettingBundle settings = GraphicElementFactory.getLookSettings(GraphicElementFactory.defaultLookName);
    String helperClassName = settings.getString("publicationHelper", "org.silverpeas.components.kmelia.KmeliaTransversal");
    Class<?> helperClass = Class.forName(helperClassName);
    PublicationHelper kmeliaTransversal = (PublicationHelper) helperClass.newInstance();
    kmeliaTransversal.setMainSessionController(getMainSessionController());

    return kmeliaTransversal;
  }

}
