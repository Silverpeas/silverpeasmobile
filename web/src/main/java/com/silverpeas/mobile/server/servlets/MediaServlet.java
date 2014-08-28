package com.silverpeas.mobile.server.servlets;

import com.silverpeas.comment.service.CommentServiceFactory;
import com.silverpeas.gallery.control.ejb.GalleryBm;
import com.silverpeas.gallery.model.PhotoDetail;
import com.silverpeas.gallery.model.PhotoPK;
import com.silverpeas.gallery.model.PhotoSize;
import com.silverpeas.mobile.server.services.AbstractAuthenticateService;
import com.silverpeas.mobile.shared.dto.media.PhotoDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.stratelia.webactiv.beans.admin.OrganizationController;
import com.stratelia.webactiv.beans.admin.UserFull;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.FileRepositoryManager;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.ResourceLocator;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.sanselan.common.BinaryInputStream;
import org.springframework.social.facebook.api.Photo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;

@SuppressWarnings("serial")
public class MediaServlet extends HttpServlet {

  private GalleryBm galleryBm;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String id = request.getParameter("id");
    String instanceId = request.getParameter("instanceId");
    PhotoDetail photo =  getPicture(id);
    getFile(response, instanceId, photo);
		response.getOutputStream().flush();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      checkUserInSession(request);
      processRequest(request, response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private PhotoDetail getPicture(String pictureId) throws Exception {
    PhotoDetail photoDetail = getGalleryBm().getPhoto(new PhotoPK(pictureId));
    return photoDetail;
  }

  protected void checkUserInSession(HttpServletRequest request) throws AuthenticationException {
    if (request.getSession().getAttribute(AbstractAuthenticateService.USER_ATTRIBUT_NAME) == null) {
      throw new AuthenticationException(AuthenticationException.AuthenticationError.NotAuthenticate);
    }
  }

	public void getFile(HttpServletResponse response, String instanceId, PhotoDetail photo) {
		InputStream input = null;

		try {
      ResourceLocator gallerySettings = new ResourceLocator("com.silverpeas.gallery.settings.gallerySettings", "");
      String nomRep = gallerySettings.getString("imagesSubDirectory") + photo.getPhotoPK().getId();
      String[] rep = {nomRep};
      String path = FileRepositoryManager.getAbsolutePath(null, instanceId, rep);
      File f = new File(path+ photo.getImageName());


      FileInputStream is = new FileInputStream(f);
      byte[] binaryData = new byte[(int) f.length()];
      is.read(binaryData);
      is.close();

      input = new ByteArrayInputStream(binaryData);
			response.setContentType(photo.getImageMimeType());
			response.setHeader("content-disposition", "attachment; filename=" + photo.getImageName());

			byte[] buffer = new byte[1024];
			int read;
			while ((read = input.read(buffer)) > 0) {
				response.getOutputStream().write(buffer, 0, read);
			}
		} catch (IOException e) {
			System.out.println("Error while trying to download the media.");
			e.printStackTrace();
		}
	}

  private GalleryBm getGalleryBm() throws Exception {
    if (galleryBm == null) {
      galleryBm = EJBUtilitaire.getEJBObjectRef(JNDINames.GALLERYBM_EJBHOME, GalleryBm.class);
    }
    return galleryBm;
  }
}
