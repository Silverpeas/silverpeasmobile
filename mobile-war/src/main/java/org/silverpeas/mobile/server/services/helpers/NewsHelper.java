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

package org.silverpeas.mobile.server.services.helpers;

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
import org.silverpeas.core.io.file.ImageResizingProcessor;
import org.silverpeas.core.io.file.SilverpeasFileProcessor;
import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.ServiceProvider;
import org.silverpeas.core.util.SettingBundle;
import org.silverpeas.core.util.StringUtil;
import org.silverpeas.core.util.file.FileRepositoryManager;
import org.silverpeas.core.web.look.PublicationUpdateDateComparator;
import org.silverpeas.core.web.util.viewgenerator.html.GraphicElementFactory;
import org.silverpeas.mobile.shared.dto.news.NewsDTO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.MissingResourceException;

/**
 * @author: svu
 */
public class NewsHelper {

  private static NewsHelper instance;
  private static ImageResizingProcessor processor = ServiceProvider.getService(ImageResizingProcessor.class);
  private OrganizationController organizationController = OrganizationController.get();
  private SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
  private SettingBundle publicationSettings = ResourceLocator.getSettingBundle("org.silverpeas.publication.publicationSettings");

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
      String newsSource = null;
      try{
        newsSource = settings.getString("home.news");
      } catch (MissingResourceException e) {}
      if (newsSource != null && newsSource.isEmpty() == false) {
          if (newsSource.startsWith("quickinfo")) {
            return getNewsByComponentId(newsSource, false);
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

  public List<News> getNewsByAppId(String appId, boolean managerAccess) {
    QuickInfoService service = QuickInfoServiceProvider.getQuickInfoService();
    List<News> news;
    if (managerAccess) {
      news = service.getAllNews(appId);
    } else {
      news = service.getVisibleNews(appId);
    }

    return news;
  }

  private List<PublicationDetail> getNewsByComponentId(String appId, boolean managerAccess) {
    QuickInfoService service = QuickInfoServiceProvider.getQuickInfoService();
    List<PublicationDetail> allNews = new ArrayList<PublicationDetail>();
    List<News> news;
    if (managerAccess) {
      news = service.getAllNews(appId);
    } else {
      news = service.getVisibleNews(appId);
    }
    for (News aNews: news) {
      allNews.add(aNews.getPublication());
    }
    return allNews;
  }

  public NewsDTO populate(News n) {
    NewsDTO news = new NewsDTO();
    news.setId(n.getPublication().getId());
    news.setTitle(n.getPublication().getTitle());
    news.setDescription(n.getPublication().getDescription());
    news.setUpdateDate(sdf.format(n.getPublication().getUpdateDate()));
    news.setDraft(n.getPublication().isDraft());
    news.setVisible(n.getPublication().isVisible());
    news.setIdNews(n.getId());
    try {
      news.setVignette(getBase64ImageData(n.getPublication().getInstanceId(), n.getPublication()));
    } catch(Exception e) {e.printStackTrace();}
    news.setInstanceId(n.getPublication().getInstanceId());
    return news;
  }

  public List<NewsDTO> populate(List<News> news, boolean managerAccess) {
    List<NewsDTO> dtos = new ArrayList<NewsDTO>();
    if (news != null) {
      for (News n : news) {
        NewsDTO dto = populate(n);
        dto.setManagable(managerAccess);
        dtos.add(dto);
      }
    }
    return dtos;
  }

  public NewsDTO populate(PublicationDetail pub) {
    NewsDTO news = new NewsDTO();
    news.setId(pub.getId());
    news.setTitle(pub.getTitle());
    news.setDescription(pub.getDescription());
    news.setUpdateDate(sdf.format(pub.getUpdateDate()));
    news.setDraft(pub.isDraft());
    news.setVisible(pub.isVisible());
    try {
      news.setVignette(getBase64ImageData(pub.getInstanceId(), pub));
    } catch(Exception e) {e.printStackTrace();}
    news.setInstanceId(pub.getInstanceId());
    return news;
  }

  public List<NewsDTO> populatePub(List<PublicationDetail> pubs, boolean managerAccess) {
    List<NewsDTO> dtos = new ArrayList<NewsDTO>();
    if (pubs != null) {
      for (PublicationDetail pub : pubs) {
        NewsDTO dto = populate(pub);
        dto.setManagable(managerAccess);
        dtos.add(dto);
      }
    }
    return dtos;
  }


  private String getBase64ImageData(String instanceId, PublicationDetail pub) throws Exception {
    String data = "";
    File f = getActuThumb(instanceId, pub);

    FileInputStream is = new FileInputStream(f);
    byte[] binaryData = new byte[(int) f.length()];
    is.read(binaryData);
    is.close();
    String type = pub.getImageMimeType();
    if (type.equalsIgnoreCase("jpeg")) type = "jpg";
    data = "data:" + type + ";base64," + new String(Base64.encodeBase64(binaryData));

    return data;
  }

  private File getActuThumb(String componentId,	PublicationDetail pub) throws IOException {
      String path = FileRepositoryManager.getAbsolutePath(componentId) + publicationSettings.getString("imagesSubDirectory", "fr") + File.separatorChar;
      path += pub.getImage();
      File originalFile = new File(path);
      String askedPath = pathForOriginalImageSize(originalFile, originalFile.getName(), "x90");
      path = processor.processBefore(askedPath, SilverpeasFileProcessor.ProcessingContext.GETTING);
      File file = new File(path);
      return file;
  }

  private String pathForOriginalImageSize(File originalImage,String name, String size) {
    return originalImage.getParent() + File.separator + size + File.separator + name;
  }
}
