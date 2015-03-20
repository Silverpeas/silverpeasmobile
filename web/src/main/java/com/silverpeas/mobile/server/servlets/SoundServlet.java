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
public class SoundServlet extends HttpServlet {

  private GalleryBm galleryBm;
  private static final int BUFFER_LENGTH = 9000;
  private static final long EXPIRE_TIME = 1000 * 60 * 60; // one hour

  protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

    String id = request.getParameter("id");
    Media sound = getGalleryBm().getMedia(new MediaPK(id)).getSound();
    SilverpeasFile f = sound.getFile(MediaResolution.ORIGINAL);

    String range = request.getHeader("Range");
    if (range == null) range = "bytes=0-";
    long length = f.length();
    long start = 0;
    long end = length - 1;

    String[] ranges = range.substring("bytes=".length()).split("-");
    start = Integer.valueOf(ranges[0]);
    if (ranges.length == 2) {
      end = Integer.valueOf(ranges[1]);
    }
    if (end > length - 1) end = length - 1;
    long contentLength = end - start + 1;
    long lastModified = f.lastModified();

    response.reset();
    response.resetBuffer();
    response.setBufferSize(BUFFER_LENGTH);
    response.setHeader("Content-Disposition", String.format("inline;filename=\"%s\"", f.getName()));
    response.setHeader("Accept-Ranges", "bytes");
    response.setDateHeader("Expires", System.currentTimeMillis() + EXPIRE_TIME);
    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
    response.setHeader("Content-Range", String.format("bytes %s-%s/%s", start, end, length));
    response.setHeader("Content-Length", String.format("%s", contentLength));
    response.setContentType(f.getMimeType());
    response.setDateHeader("Last-Modified", lastModified);

    InputStream in = f.inputStream();
    OutputStream out = response.getOutputStream();
    response.setContentLength((int)contentLength);
    response.setBufferSize(BUFFER_LENGTH);
    int bytesRead = 0;
    int bytesLeft = (int) contentLength;
    byte[] buffer = new byte[BUFFER_LENGTH];
    in.mark((int)start);
    for (;;) {
      bytesRead = in.read(buffer);
      if (!(bytesRead != -1 && bytesLeft > 0)) {
        break;
      }
      out.write(buffer, 0, bytesLeft < bytesRead ? bytesLeft : bytesRead);
      bytesLeft -= bytesRead;
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
