package com.silverpeas.mobile.server.servlets;

import com.silverpeas.gallery.constant.MediaResolution;
import com.silverpeas.gallery.control.ejb.GalleryBm;
import com.silverpeas.gallery.model.Media;
import com.silverpeas.gallery.model.MediaPK;
import com.silverpeas.mobile.server.services.AbstractAuthenticateService;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;
import org.silverpeas.file.SilverpeasFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@SuppressWarnings("serial")
public class VideoServlet extends HttpServlet {

  private GalleryBm galleryBm;
  private static final int BUFFER_LENGTH = 1024 * 16;
  private static final long EXPIRE_TIME = 1000 * 60 * 60 * 24;

  protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String id = request.getParameter("id");

    Media video = null;
    try {
      video = getGalleryBm().getMedia(new MediaPK(id)).getVideo();
    } catch (Exception e) {
      throw new IOException(e);
    }
    SilverpeasFile f = video.getFile(MediaResolution.PREVIEW);

    String range = request.getHeader("Range");
    int length = (int) f.length();
    int start = 0;
    int end = length - 1;

    String[] ranges = range.substring("items=".length()).split("-");
    start = Integer.valueOf(ranges[0]);
    if (ranges.length == 2) {
      end = Integer.valueOf(ranges[1]);
    }
    int contentLength = end - start + 1;

    response.reset();
    response.setBufferSize(BUFFER_LENGTH);
    response.setHeader("Content-Disposition", String.format("inline;filename=\"%s\"", f.getName()));
    response.setHeader("Accept-Ranges", "bytes");
    response.setDateHeader("Expires", System.currentTimeMillis() + EXPIRE_TIME);
    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
    response.setHeader("Content-Range", String.format("bytes %s-%s/%s", start, end, length));
    response.setHeader("Content-Length", String.format("%s", contentLength));
    response.setContentType(f.getMimeType());

    try {
      InputStream in = f.inputStream();
      OutputStream out = response.getOutputStream();
      int bytesRead;
      int bytesLeft = contentLength;
      byte[] buffer = new byte[BUFFER_LENGTH];
      in.mark(start);
      while ((bytesRead = in.read(buffer)) != -1 && bytesLeft > 0) {
        out.write(buffer, 0, bytesLeft < bytesRead ? bytesLeft : bytesRead);
        bytesLeft -= bytesRead;
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      checkUserInSession(request);
      processRequest(request, response);
    } catch (Exception e) {
      e.printStackTrace();
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

  protected void checkUserInSession(HttpServletRequest request) throws AuthenticationException {
    if (request.getSession().getAttribute(AbstractAuthenticateService.USER_ATTRIBUT_NAME) == null) {
      throw new AuthenticationException(AuthenticationException.AuthenticationError.NotAuthenticate);
    }
  }

  private GalleryBm getGalleryBm() throws Exception {
    if (galleryBm == null) {
      galleryBm = EJBUtilitaire.getEJBObjectRef(JNDINames.GALLERYBM_EJBHOME, GalleryBm.class);
    }
    return galleryBm;
  }
}
