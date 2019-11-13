/*
 * Copyright (C) 2000 - 2019 Silverpeas
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

package org.silverpeas.bridge;

import org.silverpeas.components.gallery.model.Media;
import org.silverpeas.components.gallery.model.MediaPK;
import org.silverpeas.components.gallery.service.GalleryService;
import org.silverpeas.components.gallery.service.MediaServiceProvider;
import org.silverpeas.components.quickinfo.model.News;
import org.silverpeas.components.quickinfo.model.QuickInfoService;
import org.silverpeas.components.quickinfo.model.QuickInfoServiceProvider;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.cache.service.CacheServiceProvider;
import org.silverpeas.core.cache.service.SessionCacheService;
import org.silverpeas.core.contribution.publication.model.PublicationDetail;
import org.silverpeas.core.contribution.publication.model.PublicationPK;
import org.silverpeas.core.contribution.publication.service.PublicationService;
import org.silverpeas.core.security.token.TokenGenerator;
import org.silverpeas.core.security.token.TokenGeneratorProvider;
import org.silverpeas.core.security.token.synchronizer.SynchronizerToken;
import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.SettingBundle;
import org.silverpeas.core.util.logging.SilverLogger;
import org.silverpeas.core.web.mvc.controller.MainSessionController;
import org.silverpeas.mobile.server.services.AbstractAuthenticateService;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MobilFilter implements Filter {

  //private static final SilverLogger logger = SilverLogger.getLogger("silverpeas.core.security");
  public static final String SESSION_TOKEN_KEY = "X-STKN";
  private static String [] urlsAllowed = null;

  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

    String url = ((HttpServletRequest) req).getRequestURL().toString();


    if (CacheServiceProvider.getSessionCacheService().getCache() == null) {
      HttpSession session = ((HttpServletRequest) req).getSession(false);
      if (session != null && session.getAttribute(AbstractAuthenticateService.USER_ATTRIBUT_NAME) != null) {
        ((SessionCacheService) CacheServiceProvider.getSessionCacheService()).newSessionCache((UserDetail)session.getAttribute(AbstractAuthenticateService.USER_ATTRIBUT_NAME));
      }
    }


    if (url.contains("spmobile") && res != null) {
      String csp = ((HttpServletResponse) res).getHeader("Content-Security-Policy");
      if (csp != null && !csp.contains("img-src")) {
        csp += "; img-src * data: blob:;";
        ((HttpServletResponse) res).setHeader("Content-Security-Policy", csp);
      }
    }

    Boolean mob = (Boolean) ((HttpServletRequest) req).getSession().getAttribute("isMobile");
    if (mob != null && !mob) {
      chain.doFilter(req, res);
      return;
    }

    String userAgent = ((HttpServletRequest) req).getHeader("User-Agent");
    if (userAgent != null) {
      boolean isMobile = userAgent.contains("Android") || userAgent.contains("iPhone");
      ((HttpServletRequest) req).getSession().setAttribute("isMobile", new Boolean(isMobile));

      Boolean tablet = (Boolean) ((HttpServletRequest) req).getSession().getAttribute("tablet");
      if (tablet == null) tablet = new Boolean(false);

      boolean redirect = isRedirect(url);

      if (isMobile && !url.contains("sso") && !url.contains("services") && !url.contains("spmobile")
          && (!tablet) && !url.contains("attached_file") && !url.contains("Ticket") && !url.contains("LinkFile/Key") && redirect) {
        String params = "";
        if (url.contains("Publication")) {
          String id = url.substring(url.lastIndexOf("/") + 1);
          PublicationDetail pub = getPubBm().getDetail(new PublicationPK(id));
          String appId = pub.getInstanceId();
          params = "?shortcutContentType=Publication&shortcutContentId=" + id + "&shortcutAppId=" +
              appId;
        } else if (url.contains("Media")) {
          String id = url.substring(url.lastIndexOf("/") + 1);
          Media media = getGalleryService().getMedia(new MediaPK(id));
          String appId = media.getInstanceId();
          params = "?shortcutContentType=Media&shortcutContentId=" + id + "&shortcutAppId=" + appId;
        } else if (url.contains("autoRedirect.jsp")) {
          QuickInfoService service = QuickInfoServiceProvider.getQuickInfoService();
          String subUrl = ((HttpServletRequest) req).getParameter("goto");
          if (subUrl.contains("Rquickinfo")) {
            String appId = subUrl.replace("/Rquickinfo/", "");
            appId = appId.substring(0, appId.indexOf("/"));
            String contributionId = subUrl.substring(subUrl.indexOf("Id=")+3);
            News n = service.getNews(contributionId);
            String id = n.getPublicationId();
            params = "?shortcutContentType=News&shortcutContentId=" + id + "&shortcutAppId=" + appId +
                "&shortcutContributionId=" + contributionId;
          } else if (subUrl.contains("Rblog")) {
            String appId = subUrl.replace("/Rblog/", "");
            appId = appId.substring(0, appId.indexOf("/"));
            String id = subUrl.substring(subUrl.indexOf("Id=")+3);
            params = "?shortcutContentType=Publication&shortcutContentId=" + id + "&shortcutAppId=" + appId;
          } else if(subUrl.contains("Rgallery")) {
            String appId = subUrl.replace("/Rgallery/", "");
            appId = appId.substring(0, appId.indexOf("/"));
            String id = subUrl.substring(subUrl.indexOf("Id=")+3);
            params = "?shortcutContentType=Media&shortcutContentId=" + id + "&shortcutAppId=" + appId;
          }
        } else if (url.contains("Contribution")) {
          String contributionId = url.substring(url.lastIndexOf("/") + 1);
          params = "?shortcutContentType=Event&shortcutContributionId=" + contributionId;
        } else if (url.contains("Component")) {
          String appId = url.substring(url.lastIndexOf("/") + 1);
          params = "?shortcutContentType=Component&shortcutAppId=" + appId;
        } else if(!url.contains("AuthenticationServlet") && (url.endsWith("silverpeas") || url.endsWith("silverpeas/") || url.contains("/silverpeas/"))) {
          // simple redirection on mobile login page
          params = "";
        } else {
          chain.doFilter(req, res);
          return;
        }

        HttpSession session = ((HttpServletRequest) req).getSession(false);
        SynchronizerToken token = (SynchronizerToken) session.getAttribute(SESSION_TOKEN_KEY);
        MainSessionController controller = (MainSessionController) session.getAttribute("SilverSessionController");
        if (controller != null && token == null) {
          //logger.warn("security.web.protection.token is disable");
          // generate fake token for auto login without token security
          TokenGenerator generator = TokenGeneratorProvider.getTokenGenerator(SynchronizerToken.class);
          token = generator.generate();
          session.setAttribute(SESSION_TOKEN_KEY, token);
        }

        String aDestinationPage = "/silverpeas/spmobile/spmobil.jsp" + params;
        String urlWithSessionID = ((HttpServletResponse) res).encodeRedirectURL(aDestinationPage.toString());
        ((HttpServletResponse) res).sendRedirect(urlWithSessionID);
        return;

      } else {
        chain.doFilter(req, res);
        return;
      }
    } else {
      chain.doFilter(req, res);
      return;
    }
  }

  private boolean isRedirect(final String url) {
    String urlRes = url.toLowerCase();

    if (urlsAllowed != null) {
      for (int i = 0; i < urlsAllowed.length; i++) {
        if (!urlsAllowed[i].isEmpty() && urlRes.contains(urlsAllowed[i].toLowerCase())) {
          return false;
        }
      }
    }

    if (urlRes.contains("weblib")) return false;
    if (urlRes.toLowerCase().endsWith(".css")) return false;
    if (urlRes.toLowerCase().endsWith(".gif")) return false;
    if (urlRes.toLowerCase().endsWith(".jpg")) return false;
    if (urlRes.toLowerCase().endsWith(".png")) return false;

    return true;
  }

  @Override
  public void init(FilterConfig arg0) throws ServletException {

    SettingBundle config = ResourceLocator.getSettingBundle("org.silverpeas.mobile.mobileSettings");
    String urls = config.getString("mobile.redirection.disabled.urls");
    urlsAllowed = urls.split(",");
  }

  private PublicationService getPubBm() {
    return PublicationService.get();
  }

  private GalleryService getGalleryService() {
    return MediaServiceProvider.getMediaService();
  }

}
