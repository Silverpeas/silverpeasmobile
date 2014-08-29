package com.silverpeas.mobile.server.servlets;

import com.silverpeas.gallery.control.ejb.GalleryBm;
import com.silverpeas.gallery.delegate.PhotoDataCreateDelegate;
import com.silverpeas.gallery.model.PhotoDetail;
import com.silverpeas.gallery.model.PhotoPK;
import com.silverpeas.mobile.server.common.LocalDiskFileItem;
import com.silverpeas.mobile.server.services.AbstractAuthenticateService;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.stratelia.webactiv.beans.admin.UserDetail;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.FileRepositoryManager;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.ResourceLocator;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("serial")
public class MediaServlet extends HttpServlet {

  private GalleryBm galleryBm;

  private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
  private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
  private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String action = request.getParameter("action");
    if (action.equals("view")) {
      String id = request.getParameter("id");
      String instanceId = request.getParameter("instanceId");
      PhotoDetail photo = getPicture(id);
      getFile(response, instanceId, photo);
      response.getOutputStream().flush();
    }
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      checkUserInSession(request);
      processRequest(request, response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    String componentId = "";
    String albumId = "";

    // configures upload settings
    DiskFileItemFactory factory = new DiskFileItemFactory();
    // sets memory threshold - beyond which files are stored in disk
    factory.setSizeThreshold(MEMORY_THRESHOLD);
    // sets temporary location to store files
    factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

    ServletFileUpload upload = new ServletFileUpload(factory);

    String tempDir = System.getProperty("java.io.tmpdir");

    // sets maximum size of upload file
    upload.setFileSizeMax(MAX_FILE_SIZE);

    // sets maximum size of request (include file + form data)
    upload.setSizeMax(MAX_REQUEST_SIZE);

    // Parse the request
    @SuppressWarnings("unchecked")
    List<FileItem> items = null;
    try {
      items = upload.parseRequest(request);
    } catch (FileUploadException e) {
      e.printStackTrace();
    }

    // Process the uploaded items
    Iterator iter = items.iterator();
    while (iter.hasNext())
    {
      FileItem item = (FileItem) iter.next();
      if (item.isFormField())
      {
        if (item.getFieldName().equals("componentId")) componentId = item.getString();
        if (item.getFieldName().equals("albumId")) albumId = item.getString();

      }
      else {
        String fileName = item.getName();
        File file = new File(tempDir + File.separator + fileName);
        try {
          item.write(file);
          createPhoto(request, fileName, getUserInSession(request).getId(), componentId, albumId, file, false, "", "", true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
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

  private String createPhoto(HttpServletRequest request, String name, String userId, String componentId,
      String albumId, File file, boolean watermark, String watermarkHD,
      String watermarkOther, boolean download)
      throws Exception {

    // création de la photo
    PhotoDetail newPhoto = new PhotoDetail(name, null, new Date(), null, null, null, download, false);
    newPhoto.setAlbumId(albumId);
    newPhoto.setCreatorId(userId);
    PhotoPK pk = new PhotoPK("unknown", componentId);
    newPhoto.setPhotoPK(pk);

    List<FileItem> parameters = new ArrayList<FileItem>();
    LocalDiskFileItem item = new LocalDiskFileItem(file);
    parameters.add(item);

    PhotoDataCreateDelegate delegate = new PhotoDataCreateDelegate("fr", albumId, parameters);

    getGalleryBm().createPhoto(getUserInSession(request), componentId, newPhoto, watermark, watermarkHD, watermarkOther, delegate);

    return newPhoto.getId();
  }

  protected UserDetail getUserInSession(HttpServletRequest request) {
    return (UserDetail) request.getSession().getAttribute(AbstractAuthenticateService.USER_ATTRIBUT_NAME);
  }
}
