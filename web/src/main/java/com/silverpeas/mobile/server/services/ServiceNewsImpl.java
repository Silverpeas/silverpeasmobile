package com.silverpeas.mobile.server.services;

import com.silverpeas.delegatednews.model.DelegatedNews;
import com.silverpeas.delegatednews.service.DelegatedNewsService;
import com.silverpeas.delegatednews.service.ServicesFactory;
import com.silverpeas.mobile.server.common.SpMobileLogModule;
import com.silverpeas.mobile.server.comparator.DelegatedNewsBeginDateComparatorAsc;
import com.silverpeas.mobile.shared.dto.news.NewsDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.NewsException;
import com.silverpeas.mobile.shared.services.ServiceNews;
import com.stratelia.silverpeas.silvertrace.SilverTrace;
import com.stratelia.webactiv.util.FileRepositoryManager;
import com.stratelia.webactiv.util.ResourceLocator;
import com.stratelia.webactiv.util.publication.model.PublicationDetail;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Service de gestion des news.
 * @author svu
 */
public class ServiceNewsImpl extends AbstractAuthenticateService implements ServiceNews {

  private static final long serialVersionUID = 1L;
  private DelegatedNewsService delegatedNewsService = null;
  private SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

  @Override
  public List<NewsDTO> loadNews() throws NewsException, AuthenticationException {
    checkUserInSession();
    ArrayList<NewsDTO> news = new ArrayList<NewsDTO>();
    //TODO
    List<DelegatedNews> delegatedNews = getDelegatedNewsService().getAllValidDelegatedNews();

    // Keep only visible news
    List<DelegatedNews> visibleNews = new ArrayList<DelegatedNews>();
    Date now = new Date();
    for (DelegatedNews delegated : delegatedNews) {
      Date beginDate = delegated.getBeginDate();
      Date endDate = delegated.getEndDate();
      if ((beginDate == null || (beginDate != null && now.after(beginDate))) &&
          (endDate == null || (endDate != null && now.before(endDate)))) {
        visibleNews.add(delegated);
      }
    }

    Collections.sort(visibleNews, new DelegatedNewsBeginDateComparatorAsc());
    Collections.reverse(visibleNews);

    // Sort news according to begin date
    for (DelegatedNews delegated : visibleNews) {
      try {
        delegated.getPublicationDetail().setBeginDate(delegated.getBeginDate());
        news.add(populate(delegated.getPublicationDetail()));
      } catch (Exception e) {
        // delegated news refers a deleted publication
        SilverTrace.warn(SpMobileLogModule.getName(), "ServiceNewsImpl.getNews", "CANT_GET_NEWS", "PublicationId = "+delegated.getPubId());
        throw new NewsException(e.getMessage());
      }
    }
    return news;
  }

  private NewsDTO populate(PublicationDetail pub) throws NewsException {
    NewsDTO news = new NewsDTO();
    news.setId(Integer.parseInt(pub.getId()));
    news.setTitle(pub.getTitle());
    news.setDescription(pub.getDescription());
    news.setUpdateDate(sdf.format(pub.getUpdateDate()));
    news.setVignette(getBase64ImageData(pub.getInstanceId(), pub));
    news.setInstanceId(pub.getInstanceId());
    return news;
  }

  private String getBase64ImageData(String instanceId, PublicationDetail pub) throws NewsException {
    String data = "";
    if (!pub.getImage().contains("GalleryInWysiwyg")) {
      String[] rep = {"images"};
      String path = FileRepositoryManager.getAbsolutePath(null, instanceId, rep);
      File f = new File(path + pub.getImage());
      try {
        FileInputStream is = new FileInputStream(f);
        byte[] binaryData = new byte[(int) f.length()];
        is.read(binaryData);
        is.close();
        data = "data:" + pub.getImageMimeType() + ";base64," +
            new String(Base64.encodeBase64(binaryData));
      } catch (Exception e) {
        throw new NewsException(e);
      }
    } else {
      String id = pub.getImage().substring(pub.getImage().indexOf("ImageId") + "ImageId".length() +1);
      id = id.substring(0, id.indexOf("&"));
      String imageMimeType = pub.getImageMimeType();
      instanceId = pub.getImage().substring(pub.getImage().indexOf("ComponentId") + "ComponentId".length() +1);
      instanceId = instanceId.substring(0, instanceId.indexOf("&"));
      try {
        ResourceLocator gallerySettings = new ResourceLocator("com.silverpeas.gallery.settings.gallerySettings", "");
        String nomRep = gallerySettings.getString("imagesSubDirectory") + id;
        String[] rep = {nomRep};
        String path = FileRepositoryManager.getAbsolutePath(null, instanceId, rep);
        /*File f = new File(path + id + PhotoSize.SMALL.getPrefix());
        FileInputStream is = new FileInputStream(f);
        byte[] binaryData = new byte[(int) f.length()];
        is.read(binaryData);
        is.close();
        data = "data:" + imageMimeType + ";base64," + new String(Base64.encodeBase64(binaryData));*/
      } catch (Exception e) {
        throw new NewsException(e);
      }
    }
    return data;
  }

  private DelegatedNewsService getDelegatedNewsService() {
    if (delegatedNewsService == null) {
      delegatedNewsService = ServicesFactory.getDelegatedNewsService();
    }
    return delegatedNewsService;
  }
}
