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

package org.silverpeas.mobile.server.services.helpers;

import org.apache.commons.codec.binary.Base64;
import org.silverpeas.components.delegatednews.model.DelegatedNews;
import org.silverpeas.components.delegatednews.service.DelegatedNewsServiceProvider;
import org.silverpeas.components.gallery.model.MediaPK;
import org.silverpeas.components.gallery.model.Photo;
import org.silverpeas.components.gallery.service.MediaServiceProvider;
import org.silverpeas.components.quickinfo.model.News;
import org.silverpeas.components.quickinfo.model.QuickInfoService;
import org.silverpeas.components.quickinfo.model.QuickInfoServiceProvider;
import org.silverpeas.core.admin.service.AdminException;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.contribution.publication.model.PublicationDetail;
import org.silverpeas.core.io.file.ImageResizingProcessor;
import org.silverpeas.core.io.file.SilverpeasFileProcessor;
import org.silverpeas.core.util.CollectionUtil;
import org.silverpeas.core.util.ServiceProvider;
import org.silverpeas.core.util.file.FileRepositoryManager;
import org.silverpeas.core.web.util.viewgenerator.html.GraphicElementFactory;
import org.silverpeas.kernel.bundle.ResourceLocator;
import org.silverpeas.kernel.bundle.SettingBundle;
import org.silverpeas.kernel.logging.SilverLogger;
import org.silverpeas.kernel.util.StringUtil;
import org.silverpeas.mobile.shared.dto.news.NewsDTO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author svu
 */
public class NewsHelper {

  private static NewsHelper instance;
  private static final ImageResizingProcessor processor = ServiceProvider.getService(ImageResizingProcessor.class);
  private final OrganizationController organizationController = OrganizationController.get();
  private final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
  private final SettingBundle publicationSettings = ResourceLocator.getSettingBundle("org.silverpeas.publication.publicationSettings");

  public static NewsHelper getInstance() {
    if (instance == null) {
      instance = new NewsHelper();
    }
    return instance;
  }

  public List<News> getLastNews(String userId, String spaceId, int maxNews) throws Exception {
    if (spaceId != null) {
      List<String> appIds = new ArrayList<String>();
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
              news.addAll(getNewsByComponentId(source.trim(), false, userId, maxNews));
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
    List<DelegatedNews> delegatedNews = DelegatedNewsServiceProvider.getDelegatedNewsService().getAllValidDelegatedNews();
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

  private List<News> getAllNews(String userId, int maxNews) throws AdminException {
    List<News> news = new ArrayList<>();
    List<String> apps = CollectionUtil
        .asList(organizationController.getComponentIdsForUser(userId, "quickinfo"));
    for (String appId : apps) {
      news.addAll(getNewsByComponentId(appId, false, userId, maxNews));
    }
    news = sortAndTruncate(maxNews, news, false);

    return news;
  }

  private List<News> getNewsByComponentId(String appId, boolean managerAccess, String userId, int maxNews) throws AdminException {
    QuickInfoService service = QuickInfoServiceProvider.getQuickInfoService();
    List<News> news = new ArrayList<>();

    if (Administration.get().isComponentAvailableToUser(appId, userId)) {
      if (managerAccess) {
        news = service.getAllNews(appId);
      } else {
        news = service.getVisibleNews(appId);
      }
    }
    news = sortAndTruncate(maxNews, news, false);

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

  public NewsDTO populate(News n) {
    NewsDTO news = new NewsDTO();
    news.setId(n.getPublication().getId());
    news.setTitle(n.getPublication().getTitle());
    news.setDescription(n.getPublication().getDescription());
    news.setUpdateDate(sdf.format(n.getPublication().getLastUpdateDate()));
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
    news.setUpdateDate(sdf.format(pub.getLastUpdateDate()));
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
    if (pub.getImage().contains("GalleryInWysiwyg")) {
      return convertSpImageUrlToDataUrl(pub.getImage());
    } else {
      File f = getActuThumb(instanceId, pub);
      FileInputStream is = new FileInputStream(f);
      byte[] binaryData = new byte[(int) f.length()];
      is.read(binaryData);
      is.close();
      String type = pub.getImageMimeType();
      String data = "data:" + type + ";base64," + new String(Base64.encodeBase64(binaryData));
      return data;
    }
  }

  private File getActuThumb(String componentId,	PublicationDetail pub) throws IOException {
    String path = FileRepositoryManager.getAbsolutePath(componentId) + publicationSettings.getString("imagesSubDirectory", "fr") + File.separatorChar;
    path += pub.getImage();
    File originalFile = new File(path);
    return resizeImage(originalFile);
  }

  private File resizeImage(final File originalFile) {
    final String path;
    String askedPath = pathForOriginalImageSize(originalFile, originalFile.getName(), "x90");
    path = processor.processBefore(askedPath, SilverpeasFileProcessor.ProcessingContext.GETTING);
    return new File(path);
  }

  private String convertSpImageUrlToDataUrl(String url) {
    String data = url;
    if (url.contains("GalleryInWysiwyg")) {
      try {
        String instanceId = url.substring(url.indexOf("ComponentId") + "ComponentId".length() + 1);
        instanceId = instanceId.substring(0, instanceId.indexOf("&"));
        String imageId = url.substring(url.indexOf("ImageId") + "ImageId".length() + 1);
        imageId = imageId.substring(0, imageId.indexOf("&"));
        Photo photo = MediaServiceProvider.getMediaService().getPhoto(new MediaPK(imageId));
        String[] rep = {"image" + imageId};

        String path = FileRepositoryManager.getAbsolutePath(instanceId, rep);
        File f = new File(path + photo.getFileName());

        File fResized = resizeImage(f);
        FileInputStream is = new FileInputStream(fResized);
        byte[] binaryData = new byte[(int) f.length()];
        is.read(binaryData);
        is.close();
        data = "data:" + photo.getFileMimeType().getMimeType().toLowerCase() + ";base64," +
            new String(Base64.encodeBase64(binaryData));
      } catch (Exception e) {
        SilverLogger.getLogger(this)
            .error("NewsHelper.convertSpImageUrlToDataUrl", "root.EX_NO_MESSAGE", e);
      }
    }
    return data;
  }


  private String pathForOriginalImageSize(File originalImage,String name, String size) {
    return originalImage.getParent() + File.separator + size + File.separator + name;
  }
}
