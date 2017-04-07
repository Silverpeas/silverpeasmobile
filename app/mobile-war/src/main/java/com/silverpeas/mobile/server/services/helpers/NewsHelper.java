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

package com.silverpeas.mobile.server.services.helpers;

import com.silverpeas.mobile.shared.dto.news.NewsDTO;
import com.silverpeas.mobile.shared.exceptions.NewsException;
import org.apache.commons.codec.binary.Base64;
import org.silverpeas.components.delegatednews.model.DelegatedNews;
import org.silverpeas.components.delegatednews.service.DelegatedNewsServiceProvider;
import org.silverpeas.components.quickinfo.model.News;
import org.silverpeas.components.quickinfo.model.QuickInfoService;
import org.silverpeas.components.quickinfo.model.QuickInfoServiceProvider;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.contribution.publication.model.PublicationDetail;
import org.silverpeas.core.contribution.publication.model.PublicationPK;
import org.silverpeas.core.contribution.publication.service.PublicationService;
import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.SettingBundle;
import org.silverpeas.core.util.StringUtil;
import org.silverpeas.core.util.file.FileRepositoryManager;
import org.silverpeas.core.web.look.PublicationUpdateDateComparator;
import org.silverpeas.core.web.util.viewgenerator.html.GraphicElementFactory;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author: svu
 */
public class NewsHelper {

  private static NewsHelper instance;
  private OrganizationController organizationController = OrganizationController.get();
  private SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

  public static NewsHelper getInstance() {
    if (instance == null) {
      instance = new NewsHelper();
    }
    return instance;
  }

  public List<PublicationDetail> getLastNews(String userId, String spaceId) {
    if(spaceId != null) {
      List<String> appIds = new ArrayList<String>();
      String[] cIds = organizationController.getAvailCompoIds(spaceId, userId);
      for (String id : cIds) {
        if (StringUtil.startsWithIgnoreCase(id, "quickinfo")) {
          appIds.add(id);
        }
      }

      List<PublicationDetail> news = new ArrayList<PublicationDetail>();
      for (String appId : appIds) {
        Collection<PublicationDetail> someNews =
            PublicationService.get().getOrphanPublications(new PublicationPK("", appId));
        for (PublicationDetail aNews : someNews) {
          if (isVisibleNews(aNews)) {
            news.add(aNews);
          }
        }
      }

      Collections.sort(news, PublicationUpdateDateComparator.comparator);
      return news;
    } else {
      // News on main page
      SettingBundle settings = GraphicElementFactory.getLookSettings(GraphicElementFactory.defaultLookName);
      String newsSource = settings.getString("home.news");
      if (newsSource != null && newsSource.isEmpty() == false) {
          if (newsSource.startsWith("quickinfo")) {
            return getNewsByComponentId(newsSource);
          } else {
            return getDelegatedNews();
          }
      }
      return null;
    }
  }

  private boolean isVisibleNews(PublicationDetail news) {
    return news.isValid() && news.getVisibilityPeriod().contains(new Date());
  }

  private List<PublicationDetail> getDelegatedNews() {
    List<PublicationDetail> news = new ArrayList<PublicationDetail>();
    List<DelegatedNews> delegatedNews = DelegatedNewsServiceProvider.getDelegatedNewsService().getAllValidDelegatedNews();

    for (DelegatedNews delegated : delegatedNews) {
      News aNews = new News(delegated.getPublicationDetail());
      aNews.setPublicationId(delegated.getPublicationDetail().getId());
      news.add(aNews.getPublication());
    }

    return news;
  }

  private List<PublicationDetail> getNewsByComponentId(String appId) {
    QuickInfoService service = QuickInfoServiceProvider.getQuickInfoService();
    List<PublicationDetail> allNews = new ArrayList<PublicationDetail>();
    List<News> news = service.getVisibleNews(appId);
    for (News aNews: news) {
      allNews.add(aNews.getPublication());
    }

    return allNews;
  }

  public NewsDTO populate(PublicationDetail pub) {
    NewsDTO news = new NewsDTO();
    news.setId(Integer.parseInt(pub.getId()));
    news.setTitle(pub.getTitle());
    news.setDescription(pub.getDescription());
    news.setUpdateDate(sdf.format(pub.getUpdateDate()));
    try {
      news.setVignette(getBase64ImageData(pub.getInstanceId(), pub));
    } catch(Exception e) {e.printStackTrace();}
    news.setInstanceId(pub.getInstanceId());
    return news;
  }

  public List<NewsDTO> populate(List<PublicationDetail> pubs) {
    List<NewsDTO> dtos = new ArrayList<NewsDTO>();
    for (PublicationDetail pub: pubs) {
      NewsDTO dto = populate(pub);
      dtos.add(dto);
    }
    return dtos;
  }

  private String getBase64ImageData(String instanceId, PublicationDetail pub) throws Exception {
    String data = "";
    if (!pub.getImage().contains("GalleryInWysiwyg")) {
      String[] rep = {"images"};
      String path = FileRepositoryManager.getAbsolutePath(null, instanceId, rep);
      File f = new File(path + pub.getImage());

      FileInputStream is = new FileInputStream(f);
      byte[] binaryData = new byte[(int) f.length()];
      is.read(binaryData);
      is.close();
      data = "data:" + pub.getImageMimeType() + ";base64," +
            new String(Base64.encodeBase64(binaryData));

    } else {
      String id = pub.getImage().substring(pub.getImage().indexOf("ImageId") + "ImageId".length() +1);
      id = id.substring(0, id.indexOf("&"));
      String imageMimeType = pub.getImageMimeType();
      instanceId = pub.getImage().substring(pub.getImage().indexOf("ComponentId") + "ComponentId".length() +1);
      instanceId = instanceId.substring(0, instanceId.indexOf("&"));

      SettingBundle gallerySettings = ResourceLocator.getSettingBundle("com.silverpeas.gallery.settings.gallerySettings");
      String nomRep = gallerySettings.getString("imagesSubDirectory") + id;
      String[] rep = {nomRep};
      String path = FileRepositoryManager.getAbsolutePath(null, instanceId, rep);

    }
    return data;
  }
}
