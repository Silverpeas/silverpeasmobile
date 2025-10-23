/*
 * Copyright (C) 2000 - 2025 Silverpeas
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

package org.silverpeas.mobile.server.servlets;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.silverpeas.components.gallery.model.MediaPK;
import org.silverpeas.components.gallery.model.Photo;
import org.silverpeas.components.gallery.service.GalleryService;
import org.silverpeas.components.gallery.service.MediaServiceProvider;
import org.silverpeas.components.kmelia.service.KmeliaService;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.contribution.attachment.AttachmentServiceProvider;
import org.silverpeas.core.contribution.attachment.model.SimpleDocument;
import org.silverpeas.core.contribution.attachment.model.SimpleDocumentPK;
import org.silverpeas.core.contribution.content.form.*;
import org.silverpeas.core.contribution.content.form.field.FileField;
import org.silverpeas.core.contribution.content.wysiwyg.service.WysiwygController;
import org.silverpeas.core.contribution.publication.model.PublicationDetail;
import org.silverpeas.core.contribution.publication.model.PublicationPK;
import org.silverpeas.core.contribution.template.publication.PublicationTemplate;
import org.silverpeas.core.contribution.template.publication.PublicationTemplateManager;
import org.silverpeas.core.util.file.FileRepositoryManager;
import org.silverpeas.kernel.bundle.ResourceLocator;
import org.silverpeas.kernel.bundle.SettingBundle;
import org.silverpeas.kernel.logging.SilverLogger;
import org.silverpeas.mobile.server.helpers.FormXMLHelper;
import org.silverpeas.mobile.server.services.AbstractAuthenticateService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.FileNameMap;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Properties;

@SuppressWarnings("serial")
public class PublicationContentServlet extends AbstractSilverpeasMobileServlet {
  public static final String USERKEY_ATTRIBUT_NAME = "key";

  private OrganizationController organizationController = OrganizationController.get();
  private static String rootContext = "";
  private SettingBundle mobileSettings = ResourceLocator.getSettingBundle("org.silverpeas.mobile.mobileSettings");

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {

    if (rootContext.isEmpty()) {
      try{
        String confPath = System.getenv("SILVERPEAS_HOME") + File.separator + "configuration" + File.separator + "config.properties";
        Properties conf = new Properties();
        InputStream input = new FileInputStream(confPath);
        conf.load(input);
        String port = conf.getProperty("SERVER_PORT");
        if (port == null) port = "8000";
        rootContext= "http://127.0.0.1:"+ port;
        input.close();
      } catch(Exception e) {
        rootContext= "http://127.0.0.1:8000";
      }
    }

    String id = request.getParameter("id");
    String componentId = request.getParameter("componentId");
    String ua = request.getHeader("User-Agent");

    response.setContentType("text/html; charset=UTF-8");
    response.setCharacterEncoding("UTF-8");

    response.getWriter().print("<html>");
    response.getWriter().print("<head>");
    response.getWriter().print("<meta http-equiv='content-type' content='text/html;charset=UTF-8' />");

    response.getWriter().print("<link rel=\"stylesheet\" href=\"" + "/silverpeas/spmobile/spmobile.css" + "\">");

    String urlCSS = mobileSettings.getString("styleSheet", "");
    if (!urlCSS.isEmpty()) {
      response.getWriter().print("<link rel=\"stylesheet\" href=\"" + urlCSS + "\">");
    }
    String urlCSSWysiwyg = mobileSettings.getString("wysiwyg.styleSheet", "");
    if (!urlCSSWysiwyg.isEmpty()) {
      response.getWriter().print("<link rel=\"stylesheet\" href=\"" + urlCSSWysiwyg + "\">");
    }

    response.getWriter().print("</head>");
    response.getWriter().print("<body>");

    String html = "";
    if (componentId != null && !componentId.isEmpty() && !componentId.startsWith("kmelia")) {
      if (isXMLTemplateUsed(request, componentId)) {
        PagesContext context = new PagesContext("myForm", "0", getUserInSession(request).getUserPreferences().getLanguage(), false, componentId, "useless");
        context.setObjectId("0");
        context.setBorderPrinted(false);
        PublicationTemplate pubTemplate = getXMLTemplate(request, componentId);
        DataRecord record = getDataRecord(request, componentId);
        html = pubTemplate.getViewForm().toString(context, record);
        html = tranformContent(request, record, html);
        response.getWriter().print(html);
      } else {
        html = WysiwygController.loadForReadOnly(componentId, id,
            getUserInSession(request).getUserPreferences().getLanguage());
        displayWysiwyg(html, request, response, componentId);
      }
    } else {
      PublicationDetail pub = getKmeliaBm().getPublicationDetail(new PublicationPK(id));
      if (pub.getInfoId().equals("0")) {
        // wysiwyg
        html = pub.getContent().getRenderer().renderView();
        displayWysiwyg(html, request, response, pub.getInstanceId());
      } else {
        // form xml
        displayFormView(response.getWriter(), pub, request, ua);
      }
    }


    response.getOutputStream().print("</body></html>");
    ((OutputStream) response.getOutputStream()).flush();
  }

  private void displayWysiwyg(String html, HttpServletRequest request, HttpServletResponse response,
      String instanceId) throws IOException {
    html = "<html><body>" + html + "</body></html>";
    Document doc = Jsoup.parse(html);

    Elements body = doc.getElementsByTag("body");
    if (!body.isEmpty()) {
      html = body.first().html();
    }

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
    Elements anchors = doc.getElementsByTag("a");
    for (Element a : anchors) {
      String href = a.attr("href");
      String target = a.attr("target");
      if (!target.equalsIgnoreCase("_blank")) {
        if (href.contains("/silverpeas/") && !href.contains(".jsp")) {
          a.attr("href", "#");
          a.attr("onclick", "parent.navigate('" + href + "');");
        }
      }
    }

    html = doc.outerHtml();
    writeContainer(response.getWriter(), html);
    response.getWriter().flush();
  }

  private void displayFormView(Writer out, PublicationDetail pub, HttpServletRequest request, String ua)
      throws Exception {

    UserDetail user = getUserInSession(request);
    PublicationTemplate pubTemplate = PublicationTemplateManager.getInstance()
        .getPublicationTemplate(pub.getInstanceId() + ":" + pub.getInfoId());
    DataRecord xmlData = pubTemplate.getRecordSet().getRecord(pub.getId());


    PagesContext xmlContext =
        new PagesContext("myForm", "0", user.getUserPreferences().getLanguage(), false,
            pub.getInstanceId(), "useless");
    xmlContext.setObjectId(pub.getId());
    xmlContext.setDesignMode(false);
    xmlContext.setBorderPrinted(false);
    xmlContext.setContentLanguage(user.getUserPreferences().getLanguage());
    xmlContext.setCreation(false);

    Form xmlForm = pubTemplate.getViewForm();
    String html = xmlForm.toString(xmlContext, xmlData);

    html = tranformContent(request, xmlData, html);
    writeContainer(out, html);
    out.flush();
  }

  private String tranformContent(final HttpServletRequest request, final DataRecord xmlData,
      final String html) throws FormException {
    Document doc = Jsoup.parse(html);

    // transform user fields
    Iterator it = doc.getElementsByAttributeValueStarting("id", "select-user-group-").iterator();
    while (it.hasNext()) {
      Element el = (Element) it.next();

      String htmlField = el.parent().html();
      String fieldName = htmlField.substring(htmlField.indexOf("userInputName\":'")+16);
      fieldName = fieldName.substring(0, fieldName.indexOf("'"));

      //String fieldName = el.id().replace("select-user-group-", "");
      UserDetail u = (UserDetail) xmlData.getField(fieldName).getObjectValue();
      el.text(u.getDisplayedName());
    }

    // use html5 video player
    it = doc.getElementsByAttributeValueStarting("id", "video-player").iterator();
    while (it.hasNext()) {
      Element el = (Element) it.next();
      String fieldName = el.parent().parent().getElementsByTag("label").first().attr("for");
      FileField f = (FileField) xmlData.getField(fieldName);

      //TODO : fix display without desktop session
      String u = "/silverpeas/attached_file/componentId/" + request.getParameter("componentId") + "/attachmentId/"+f.getAttachmentId();
      el.html("<video controls='controls' src='" + u + "' width='100%'></video>");
    }

    Elements images = doc.getElementsByTag("img");
    for (Element img : images) {
      if (img.attr("class").equals("preview-file")) {
        // remove preview for files
        img.remove();
      } else if (img.attr("src").startsWith("/silverpeas/attached_file/componentId/")) {
        // convert url to dataurl
        String data = img.attr("src");
        data = convertImageAttachmentUrl(data, data);
        img.attr("src", data);
      }
    }

    // remove all scripts
    Elements scripts = doc.getElementsByTag("script");
    for (Element script : scripts) {
      script.remove();
    }

    return doc.outerHtml();
  }

  private void writeContainer(Writer out, String html) throws IOException {
    out.write("<div id='gesture-area'>");
    out.write("<div id='scale-element'>");
    out.write("<div id='real-element'>");
    out.write(html);
    out.write("</div>");
    out.write("</div>");
    out.write("</div>");
  }

  private String convertSpImageUrlToDataUrl(String url) {
    String data = url;
    if (url.contains("GalleryInWysiwyg")) {
      try {
        String instanceId = url.substring(url.indexOf("ComponentId") + "ComponentId".length() + 1);
        instanceId = instanceId.substring(0, instanceId.indexOf("&"));
        String imageId = url.substring(url.indexOf("ImageId") + "ImageId".length() + 1);
        imageId = imageId.substring(0, imageId.indexOf("&"));
        Photo photo = getGalleryService().getPhoto(new MediaPK(imageId));
        String[] rep = {"image" + imageId};

        String path = FileRepositoryManager.getAbsolutePath(instanceId, rep);
        File f = new File(path + photo.getFileName());

        FileInputStream is = new FileInputStream(f);
        byte[] binaryData = new byte[(int) f.length()];
        is.read(binaryData);
        is.close();
        data = "data:" + photo.getFileMimeType() + ";base64," +
            new String(Base64.encodeBase64(binaryData));
      } catch (Exception e) {
        SilverLogger.getLogger(this)
            .error("PublicationContentServlet.convertSpImageUrlToDataUrl", "root.EX_NO_MESSAGE", e);
      }
    } else if (url.contains("attachmentId")) {
      data = convertImageAttachmentUrl(url, data);
    } else {
      try {
        if (url.startsWith("/silverpeas")) {
          url = rootContext + url;
        }
        URL urlObject = new URL(url);
        URLConnection connection = urlObject.openConnection();
        connection.connect();
        String contentType = connection.getContentType();
        if (contentType == null) {
          FileNameMap fileNameMap = URLConnection.getFileNameMap();
          contentType = fileNameMap.getContentTypeFor(url);
        }

        byte[] binaryData = new byte[(int) connection.getInputStream().available()];
        connection.getInputStream().read(binaryData);
        data = "data:" + contentType + ";base64," + new String(Base64.encodeBase64(binaryData));
      } catch (Exception e) {
        SilverLogger.getLogger(this)
            .error("PublicationContentServlet.convertImageUrlToDataUrl", "root.EX_NO_MESSAGE", e);
        // If can't connect to url, return the url without change
      }
    }
    return data;
  }

  private String convertImageAttachmentUrl(final String url, String data) {

    String attachmentId = url.substring(url.indexOf("attachmentId/") + "attachmentId/".length());
    attachmentId = attachmentId.substring(0, attachmentId.indexOf("/"));
    String componentId = "";

    if (url.indexOf("kmelia") != -1) {
      componentId = url.substring(url.indexOf("kmelia"));
    } else if (url.indexOf("quickinfo") != -1) {
      componentId = url.substring(url.indexOf("quickinfo"));
    } else if (url.indexOf("webPages") != -1) {
      componentId = url.substring(url.indexOf("webPages"));
    }
    componentId = componentId.substring(0, componentId.indexOf("/"));

    SimpleDocument attachment = AttachmentServiceProvider.getAttachmentService()
        .searchDocumentById(new SimpleDocumentPK(attachmentId, componentId), null);

    try {
      File f = new File(attachment.getAttachmentPath());
      FileInputStream is = new FileInputStream(f);
      byte[] binaryData = new byte[(int) f.length()];
      is.read(binaryData);
      is.close();
      data = "data:" + attachment.getContentType() + ";base64," +
          new String(Base64.encodeBase64(binaryData));
    } catch (Exception e) {
      SilverLogger.getLogger(this)
          .error("PublicationContentServlet.convertSpImageUrlToDataUrl", "root.EX_NO_MESSAGE", e);
    }
    return data;
  }


  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      checkUserInSession(request, response);
      processRequest(request, response);
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
    }
  }

  private KmeliaService getKmeliaBm() {
    return KmeliaService.get();
  }

  private GalleryService getGalleryService() throws Exception {
    return MediaServiceProvider.getMediaService();
  }

  private String getUsedXMLTemplate(HttpServletRequest request, String instanceId)
      throws Exception {
    return FormXMLHelper.getUsedXMLTemplate(request, instanceId);
  }

  public boolean isXMLTemplateUsed(HttpServletRequest request, String instanceId) throws Exception {
    return FormXMLHelper.isXMLTemplateUsed(request, instanceId);
  }
  protected String getUserKeyInSession(HttpServletRequest request) {
    return (String) request.getSession().getAttribute(USERKEY_ATTRIBUT_NAME);
  }

  private PublicationTemplate getXMLTemplate(HttpServletRequest request, String instanceId)
      throws Exception {
    return FormXMLHelper.getXMLTemplate(request, instanceId);
  }

  private void registerXMLForm(HttpServletRequest request, String instanceId) throws Exception {
   FormXMLHelper.registerXMLForm(request, instanceId);
  }


  private String getUsedXMLTemplateShortname(HttpServletRequest request, String instanceId)
      throws Exception {
    return FormXMLHelper.getUsedXMLTemplateShortname(request, instanceId);
  }

  private DataRecord getDataRecord(HttpServletRequest request, String instanceId) throws Exception {
    PublicationTemplate pubTemplate = getXMLTemplate(request, instanceId);
    RecordSet recordSet = pubTemplate.getRecordSet();
    DataRecord data = recordSet.getRecord("0");
    if (data == null) {
      data = recordSet.getEmptyRecord();
      data.setId("0");
    }
    return data;
  }

}
