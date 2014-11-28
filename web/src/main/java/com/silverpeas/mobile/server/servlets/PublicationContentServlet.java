package com.silverpeas.mobile.server.servlets;

import com.silverpeas.gallery.control.ejb.GalleryBm;
import com.silverpeas.gallery.model.PhotoDetail;
import com.silverpeas.gallery.model.PhotoPK;
import com.silverpeas.mobile.server.common.SpMobileLogModule;
import com.silverpeas.mobile.server.services.AbstractAuthenticateService;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.stratelia.silverpeas.silvertrace.SilverTrace;
import com.stratelia.webactiv.beans.admin.OrganizationController;
import com.stratelia.webactiv.kmelia.control.ejb.KmeliaBm;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.FileRepositoryManager;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.publication.model.PublicationDetail;
import com.stratelia.webactiv.util.publication.model.PublicationPK;
import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.silverpeas.attachment.AttachmentServiceFactory;
import org.silverpeas.attachment.model.SimpleDocument;
import org.silverpeas.attachment.model.SimpleDocumentPK;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SuppressWarnings("serial")
public class PublicationContentServlet extends HttpServlet {

  private OrganizationController organizationController = new OrganizationController();
  private KmeliaBm kmeliaBm;
  private GalleryBm galleryBm;

  protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String id = request.getParameter("id");

    PublicationDetail pub = null;
    try {
      pub = getKmeliaBm().getPublicationDetail(new PublicationPK(id));
    } catch (Exception e) {
      e.printStackTrace();
    }
    response.getOutputStream().print("<html><body>");
    String html = pub.getWysiwyg();
    Document doc = Jsoup.parse(html);

    Elements images = doc.getElementsByTag("img");
    for (Element img : images) {
      String source = img.attr("src");
      String newSource = source;
      if (source.contains("/silverpeas")) {
        // need to convert in dataurl
        newSource = convertSpImageUrlToDataUrl(source);
      }
      img.attr("src", newSource);
    }
    html = doc.outerHtml();
    response.getOutputStream().print(html);
    response.getOutputStream().print("</html></body>");
    response.getOutputStream().flush();
  }

  private String convertSpImageUrlToDataUrl(String url) {
    String data = url;
    if (url.contains("GalleryInWysiwyg")) {
      try {
        String instanceId = url.substring(url.indexOf("ComponentId") + "ComponentId".length() + 1);
        instanceId = instanceId.substring(0, instanceId.indexOf("&"));
        String imageId= url.substring(url.indexOf("ImageId") + "ImageId".length() +1);
        imageId = imageId.substring(0, imageId.indexOf("&"));
        PhotoDetail photo = getGalleryBm().getPhoto(new PhotoPK(imageId));
        String[] rep = {"image"+imageId};

        String path = FileRepositoryManager.getAbsolutePath(null, instanceId, rep);
        File f = new File(path + photo.getImageName());

        FileInputStream is = new FileInputStream(f);
        byte[] binaryData = new byte[(int) f.length()];
        is.read(binaryData);
        is.close();
        data = "data:" + photo.getImageMimeType() + ";base64," +
            new String(Base64.encodeBase64(binaryData));
      } catch (Exception e) {
        SilverTrace.error(SpMobileLogModule.getName(),
            "PublicationContentServlet.convertSpImageUrlToDataUrl", "root.EX_NO_MESSAGE", e);
      }
    } else {
      String attachmentId = url.substring(url.indexOf("attachmentId/") + "attachmentId/".length());
      attachmentId = attachmentId.substring(0, attachmentId.indexOf("/"));
      String componentId = url.substring(url.indexOf("kmelia"));
      componentId = componentId.substring(0, componentId.indexOf("/"));

      SimpleDocument attachment =
          AttachmentServiceFactory.getAttachmentService().searchDocumentById(
              new SimpleDocumentPK(attachmentId, componentId), null);

      try {
        File f = new File(attachment.getAttachmentPath());
        FileInputStream is = new FileInputStream(f);
        byte[] binaryData = new byte[(int) f.length()];
        is.read(binaryData);
        is.close();
        data = "data:" + attachment.getFile().getContentType() + ";base64," + new String(Base64.encodeBase64(binaryData));
      } catch (Exception e) {
        SilverTrace.error(SpMobileLogModule.getName(),
            "PublicationContentServlet.convertSpImageUrlToDataUrl", "root.EX_NO_MESSAGE", e);
      }
    }
    return data;
  }


  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      checkUserInSession(request);
      processRequest(request, response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void checkUserInSession(HttpServletRequest request) throws AuthenticationException {
    if (request.getSession().getAttribute(AbstractAuthenticateService.USER_ATTRIBUT_NAME) == null) {
      throw new AuthenticationException(AuthenticationException.AuthenticationError.NotAuthenticate);
    }
  }

  private KmeliaBm getKmeliaBm() throws Exception {
    if (kmeliaBm == null) {
      kmeliaBm = EJBUtilitaire.getEJBObjectRef(JNDINames.KMELIABM_EJBHOME, KmeliaBm.class);
    }
    return kmeliaBm;
  }

  private GalleryBm getGalleryBm() throws Exception {
    if (galleryBm == null) {
      galleryBm = EJBUtilitaire.getEJBObjectRef(JNDINames.GALLERYBM_EJBHOME, GalleryBm.class);
    }
    return galleryBm;
  }
}
