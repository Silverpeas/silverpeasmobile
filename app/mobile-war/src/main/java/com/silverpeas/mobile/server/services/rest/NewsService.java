package com.silverpeas.mobile.server.services.rest;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.silverpeas.mobile.server.comparator.DelegatedNewsBeginDateComparatorAsc;

import org.apache.commons.codec.binary.Base64;
import org.silverpeas.components.delegatednews.model.DelegatedNews;
import org.silverpeas.components.delegatednews.service.DelegatedNewsServiceProvider;
import org.silverpeas.components.quickinfo.model.News;
import org.silverpeas.components.quickinfo.model.QuickInfoServiceProvider;
import org.silverpeas.core.admin.component.model.WAComponent;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.annotation.RequestScoped;
import org.silverpeas.core.annotation.Service;
import org.silverpeas.core.contribution.publication.model.PublicationDetail;
import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.SettingBundle;
import org.silverpeas.core.util.file.FileRepositoryManager;
import org.silverpeas.core.webapi.base.RESTWebService;
import org.silverpeas.core.webapi.base.annotation.Authenticated;

@Service
@RequestScoped
@Path("/fragments/news")
@Authenticated
public class NewsService extends RESTWebService {
	
	@Context 
	HttpServletRequest request;
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("last/{max}")
	public String getLastNews(@PathParam("max") int max){
	    StringBuffer html = new StringBuffer();
		html.append("<ul>");

        if (isDelegatedNewsUsed()) {
            List<DelegatedNews> delegatedNews = DelegatedNewsServiceProvider.getDelegatedNewsService().getAllValidDelegatedNews();
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

            // limit news number
            if (visibleNews.size() > max) visibleNews.subList(0, max-1);

            // Sort news according to begin date
            for (DelegatedNews delegated : visibleNews) {
                html.append(render(delegated.getPublicationDetail()));
            }
        } else {
            List<News> news = QuickInfoServiceProvider.getQuickInfoService().getPlatformNews(getUserDetail().getId());
            if (news.size() > max) news.subList(0, max-1);
            for (News n : news) {
                html.append(render(n.getPublication()));
            }
        }

        html.append("</ul>");
        return html.toString();
    }

    private boolean isDelegatedNewsUsed() {
        Map<String, WAComponent> allcomponents = Administration.get().getAllComponents();
        Set<String> ids = allcomponents.keySet();
        for (String id:ids) {
            if (id.startsWith("delegatednews")) {
                return true;
            }
        }
        return false;
    }

    @Override
	public String getComponentId() {
		return null;
	}


	private String render(PublicationDetail news) {
		StringBuffer html = new StringBuffer();
		html.append("<li data-id='");
		html.append(news.getId());
		html.append("' data-app-id='");
		html.append(news.getComponentInstanceId());
		html.append("'>");
		html.append("<a href='");
		html.append(news.getPermalink());
		html.append("'>");
		html.append("<img src='");
		html.append(getBase64ImageData(news.getInstanceId(), news));
		html.append("'>");
		html.append("</a>");
		html.append("<div class='caption'>");
		html.append("<h2><a href='");
		html.append(news.getPermalink());
		html.append("'>");
		html.append(news.getTitle());
		html.append("</a></h2>");
		html.append("<p></p>");
		html.append("</div>");
		html.append("</li>");
		return html.toString();
	}

	private String getBase64ImageData(String instanceId, PublicationDetail pub) {
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

			}
		} else {
			String id = pub.getImage().substring(pub.getImage().indexOf("ImageId") + "ImageId".length() +1);
			id = id.substring(0, id.indexOf("&"));
			String imageMimeType = pub.getImageMimeType();
			instanceId = pub.getImage().substring(pub.getImage().indexOf("ComponentId") + "ComponentId".length() +1);
			instanceId = instanceId.substring(0, instanceId.indexOf("&"));
			try {
				SettingBundle gallerySettings = ResourceLocator.getSettingBundle("com.silverpeas.gallery.settings.gallerySettings");
				String nomRep = gallerySettings.getString("imagesSubDirectory") + id;
				String[] rep = {nomRep};
				String path = FileRepositoryManager.getAbsolutePath(null, instanceId, rep);
			} catch (Exception e) {

			}
		}
		return data;
	}

}
