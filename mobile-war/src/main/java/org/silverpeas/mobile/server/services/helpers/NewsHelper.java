/*
 * Copyright (C) 2000 - 2025 Silverpeas
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

package org.silverpeas.mobile.server.services.helpers;

import org.silverpeas.components.delegatednews.model.DelegatedNews;
import org.silverpeas.components.delegatednews.service.DelegatedNewsService;
import org.silverpeas.components.quickinfo.NewsByStatus;
import org.silverpeas.components.quickinfo.model.News;
import org.silverpeas.components.quickinfo.model.QuickInfoService;
import org.silverpeas.components.quickinfo.model.QuickInfoServiceProvider;
import org.silverpeas.core.admin.service.AdminException;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.contribution.publication.model.PublicationDetail;
import org.silverpeas.core.contribution.model.Thumbnail;
import org.silverpeas.core.util.CollectionUtil;
import org.silverpeas.core.web.util.viewgenerator.html.GraphicElementFactory;
import org.silverpeas.kernel.bundle.ResourceLocator;
import org.silverpeas.kernel.bundle.SettingBundle;
import org.silverpeas.kernel.logging.SilverLogger;
import org.silverpeas.kernel.util.StringUtil;
import org.silverpeas.mobile.shared.dto.news.NewsDTO;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author svu
 */
public class NewsHelper {

  private static NewsHelper instance;
  private final OrganizationController organizationController = OrganizationController.get();
  private final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

  public static NewsHelper getInstance() {
    if (instance == null) {
      instance = new NewsHelper();
    }
    return instance;
  }

  public List<News> getLastNews(String userId, String spaceId, int maxNews) throws Exception {
    if (spaceId != null) {
      List<String> appIds = new ArrayList<>();
      String[] cIds = organizationController.getAvailCompoIds(spaceId, userId);
      for (String id : cIds) {
        if (StringUtil.startsWithIgnoreCase(id, "quickinfo")) {
          appIds.add(id);
        }
      }
      QuickInfoService service = QuickInfoServiceProvider.getQuickInfoService();
      List<News> news = new ArrayList<>();
      for (String appId : appIds) {
        news.addAll(service.getVisibleNews(appId));
      }
      news = sortAndTruncate(maxNews, news, false);
      return news;
    } else {
      // News on main page
      List<News> news = new ArrayList<>();
      SettingBundle settings = GraphicElementFactory.getLookSettings(UserHelper.getInstance().getUserLook(userId));
      String newsSource = null;
      try{
        newsSource = settings.getString("home.news");
      } catch (MissingResourceException e) {}
      if (newsSource != null && newsSource.isEmpty() == false) {
          if (newsSource.trim().startsWith("quickinfo")) {
            String [] sources = newsSource.split(" ");
            for (String source : sources) {
              news.addAll(getNewsByComponentId(source.trim(), false, userId, maxNews, true));
            }
            news = sortAndTruncate(maxNews, news, true);
          } else if (newsSource.trim().equals("*")) {
            news = getAllNews(userId, maxNews);
            news = sortAndTruncate(maxNews, news, true);
          } else {
            news = getDelegatedNews(userId, maxNews);
          }
      }
      return news;
    }
  }

  private boolean isVisibleNews(PublicationDetail news) {
    return news.isValid() && news.isVisible();
  }

  private List<News> getDelegatedNews(String userId, int maxNews) throws Exception {
    List<News> news = new ArrayList();
    List<DelegatedNews> delegatedNews = DelegatedNewsService.get().getAllValidDelegatedNews();
    UserDetail u = Administration.get().getUserDetail(userId);
    Date now = new Date();
    int count = 0;
    for (DelegatedNews delegated : delegatedNews) {
      if (count >= maxNews) break;
      News aNews = null;
      if (delegated.getBeginDate() == null && delegated.getEndDate() == null) {
        aNews = getNews(delegated, u);
      } else if (delegated.getBeginDate() != null && now.after(delegated.getBeginDate())
              && delegated.getEndDate() != null && now.before(delegated.getEndDate())) {
        aNews = getNews(delegated, u);
      } else if (delegated.getBeginDate() != null && now.after(delegated.getBeginDate()) && delegated.getEndDate() == null) {
        aNews = getNews(delegated, u);
      } else if (delegated.getEndDate() != null && now.before(delegated.getEndDate())) {
         aNews = getNews(delegated, u);
      }
      if (aNews != null) {
        news.add(aNews);
        count++;
      }
    }
    return news;
  }

  private News getNews(DelegatedNews delegated, UserDetail u) {
    News aNews = QuickInfoService.get().getNewsByForeignId(delegated.getPubId());
    if (!aNews.canBeAccessedBy(u)) aNews = null;
    return aNews;
  }

  public List<News> getNewsByAppId(String appId, boolean managerAccess, String userId) {
    QuickInfoService service = QuickInfoServiceProvider.getQuickInfoService();
    List<News> news = new ArrayList<>();
    if (managerAccess) {

      NewsByStatus nbs = service.getAllNewsByStatus(appId, userId);
      news.addAll(nbs.getDrafts());
      news.addAll(nbs.getNotYetVisibles());
      news.addAll(nbs.getVisibles());
      news.addAll(nbs.getNoMoreVisibles());
    } else {
      news = service.getVisibleNews(appId);
    }

    return news;
  }

  private List<News> getAllNews(String userId, int maxNews) throws AdminException {
    List<News> news = new ArrayList<>();
    List<String> apps = CollectionUtil
        .asList(organizationController.getComponentIdsForUser(userId, "quickinfo"));
    for (String appId : apps) {
      news.addAll(getNewsByComponentId(appId, false, userId, maxNews, true));
    }
    news = sortAndTruncate(maxNews, news, false);

    return news;
  }

  private List<News> getNewsByComponentId(String appId, boolean managerAccess, String userId, int maxNews, boolean reverse) throws AdminException {
    QuickInfoService service = QuickInfoServiceProvider.getQuickInfoService();
    List<News> news = new ArrayList<>();

    if (Administration.get().isComponentAvailableToUser(appId, userId)) {
      if (managerAccess) {
        news = service.getAllNews(appId);
      } else {
        news = service.getVisibleNews(appId);
      }
    }
    news = sortAndTruncate(maxNews, news, reverse);

    return news;
  }

  private static List<News> sortAndTruncate(int maxNews, List<News> news, boolean reverse) {
    Collections.sort(news, (o1, o2) -> o1.getUpdateDate().compareTo(o2.getUpdateDate()));
    if (reverse) Collections.reverse(news);
    if (news != null && news.size() > maxNews) {
      news = news.subList(0, maxNews);
    }
    return news;
  }

  public NewsDTO populate(News n, boolean resizeImage) {
    NewsDTO news = new NewsDTO();
    news.setId(n.getPublication().getId());
    news.setTitle(n.getPublication().getTitle());
    news.setDescription(n.getPublication().getDescription());
    news.setImportant(n.isImportant());
    news.setUpdateDate(sdf.format(n.getLastUpdateDate()));
    news.setContent(n.getContent());
    news.setDraft(n.getPublication().isDraft());
    news.setVisible(n.getPublication().isVisible());
    news.setStartDate(n.getVisibility().getPeriod().getStartDate().toString().replaceAll("Z", ""));
    news.setEndDate(n.getVisibility().getPeriod().getEndDate().toString().replaceAll("Z", ""));
    news.setIdNews(n.getId());
    try {
      news.setVignette(getThumbnailUrl(n.getPublication()));
    } catch(Exception e) {SilverLogger.getLogger(this).error(e);}
    news.setInstanceId(n.getPublication().getInstanceId());
    return news;
  }

  public List<NewsDTO> populate(List<News> news, boolean managerAccess) {
    List<NewsDTO> dtos = new ArrayList<NewsDTO>();
    if (news != null) {
      for (News n : news) {
        NewsDTO dto = populate(n, true);
        dto.setManagable(managerAccess);
        dtos.add(dto);
      }
    }
    return dtos;
  }

  public NewsDTO populate(PublicationDetail pub, boolean resizeImage) {
    NewsDTO news = new NewsDTO();
    news.setId(pub.getId());
    news.setTitle(pub.getTitle());
    news.setDescription(pub.getDescription());
    news.setUpdateDate(sdf.format(pub.getLastUpdateDate()));
    news.setDraft(pub.isDraft());
    news.setVisible(pub.isVisible());
    try {
      news.setVignette(getThumbnailUrl(pub));
    } catch(Exception e) {SilverLogger.getLogger(this).error(e);}
    news.setInstanceId(pub.getInstanceId());
    return news;
  }

  public List<NewsDTO> populatePub(List<PublicationDetail> pubs, boolean managerAccess, boolean resizeImage) {
    List<NewsDTO> dtos = new ArrayList<NewsDTO>();
    if (pubs != null) {
      for (PublicationDetail pub : pubs) {
        NewsDTO dto = populate(pub, resizeImage);
        dto.setManagable(managerAccess);
        dtos.add(dto);
      }
    }
    return dtos;
  }


  private String getThumbnailUrl(PublicationDetail publication) {
    Thumbnail thumbnail = publication.getThumbnail();
    return thumbnail != null ? thumbnail.getURL() : null;
  }
}
