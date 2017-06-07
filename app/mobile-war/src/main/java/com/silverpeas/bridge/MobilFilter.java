package com.silverpeas.bridge;

import org.silverpeas.components.gallery.model.Media;
import org.silverpeas.components.gallery.model.MediaPK;
import org.silverpeas.components.gallery.service.GalleryService;
import org.silverpeas.components.gallery.service.MediaServiceProvider;
import org.silverpeas.core.contribution.publication.model.PublicationDetail;
import org.silverpeas.core.contribution.publication.model.PublicationPK;
import org.silverpeas.core.contribution.publication.service.PublicationService;

import javax.servlet.*;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MobilFilter implements Filter {

  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

    Boolean mob = (Boolean) ((HttpServletRequest) req).getSession().getAttribute("isMobile");
    if (mob != null && !mob) {
      chain.doFilter(req, res);
      return;
    }

    String userAgent = ((HttpServletRequest) req).getHeader("User-Agent");
    if (userAgent != null) {
      boolean isMobile = userAgent.contains("Android") || userAgent.contains("iPhone");
      ((HttpServletRequest) req).getSession().setAttribute("isMobile", new Boolean(isMobile));

      String url = ((HttpServletRequest) req).getRequestURL().toString();
      if (isMobile && !url.contains("spmobile")) {
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
        } else if(url.endsWith("silverpeas") || url.endsWith("silverpeas/") || url.contains("/silverpeas/")) {
          // simple redirection on mobile login page
          params = "";
        } else {
          chain.doFilter(req, res);
          return;
        }
        ((HttpServletResponse) res).sendRedirect("/silverpeas/spmobile/spmobil.jsp" + params);
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

  @Override
  public void init(FilterConfig arg0) throws ServletException {
  }

  private PublicationService getPubBm() {
    return PublicationService.get();
  }

  private GalleryService getGalleryService() {
    return MediaServiceProvider.getMediaService();
  }

}
