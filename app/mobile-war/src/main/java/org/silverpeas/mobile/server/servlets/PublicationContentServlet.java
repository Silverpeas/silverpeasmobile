/*
 * Copyright (C) 2000 - 2017 Silverpeas
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
 *
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
import org.silverpeas.core.contribution.content.form.DataRecord;
import org.silverpeas.core.contribution.content.form.Form;
import org.silverpeas.core.contribution.content.form.PagesContext;
import org.silverpeas.core.contribution.content.form.RecordSet;
import org.silverpeas.core.contribution.content.form.form.HtmlForm;
import org.silverpeas.core.contribution.content.form.form.XmlForm;
import org.silverpeas.core.contribution.content.wysiwyg.service.WysiwygController;
import org.silverpeas.core.contribution.publication.model.PublicationDetail;
import org.silverpeas.core.contribution.publication.model.PublicationPK;
import org.silverpeas.core.contribution.template.publication.PublicationTemplate;
import org.silverpeas.core.contribution.template.publication.PublicationTemplateManager;
import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.SettingBundle;
import org.silverpeas.core.util.StringUtil;
import org.silverpeas.core.util.file.FileRepositoryManager;
import org.silverpeas.core.util.logging.SilverLogger;
import org.silverpeas.core.web.mvc.controller.MainSessionController;
import org.silverpeas.mobile.server.common.SpMobileLogModule;
import org.silverpeas.mobile.server.services.AbstractAuthenticateService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;

@SuppressWarnings("serial")
public class PublicationContentServlet extends AbstractSilverpeasMobileServlet {

  public static final String MAINSESSIONCONTROLLER_ATTRIBUT_NAME = AbstractAuthenticateService.MAINSESSIONCONTROLLER_ATTRIBUT_NAME;
  public static final String USERKEY_ATTRIBUT_NAME = "key";

  private OrganizationController organizationController = OrganizationController.get();
  private static String rootContext = "";
  private SettingBundle mobileSettings = ResourceLocator.getSettingBundle("org.silverpeas.mobile.mobileSettings");

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {

    if (rootContext.isEmpty()) {
      String url = request.getRequestURL().toString();
      String uri = request.getRequestURI();
      rootContext = url.substring(0, url.indexOf(uri));
    }

    String id = request.getParameter("id");
    String componentId = request.getParameter("componentId");
    String ua = request.getHeader("User-Agent");

    response.getOutputStream().print("<html>");
    response.getOutputStream().print("<head>");
    response.getOutputStream()
        .print("<meta http-equiv='content-type' content='text/html;charset=UTF-8' />");
    response.getOutputStream()
        .print("<link rel='stylesheet' href='/silverpeas/spmobile/spmobile.css'/>");


    String urlCSS = mobileSettings.getString("styleSheet", "");
    if (!urlCSS.isEmpty()) {
      response.getOutputStream().print("<link rel=\"stylesheet\" href=\"" + urlCSS + "\">");
    }

    response.getOutputStream().print("</head>");
    response.getOutputStream().print("<body>");

    String html = "";
    if (componentId != null && !componentId.isEmpty()) {
      //TODO : verify user rights
      if (isXMLTemplateUsed(request, componentId)) {
        PagesContext context = new PagesContext("myForm", "0", getUserInSession(request).getUserPreferences().getLanguage(), false, componentId, "useless");
        context.setObjectId("0");
        context.setBorderPrinted(false);
        PublicationTemplate pubTemplate = getXMLTemplate(request, componentId);
        Method method = pubTemplate.getViewForm().getClass().getDeclaredMethod("display", PrintWriter.class, PagesContext.class, DataRecord.class);
        method.setAccessible(true);
        method.invoke(pubTemplate.getViewForm(), new PrintWriter(response.getOutputStream()), context, getDataRecord(request, componentId));
      } else {
        html = WysiwygController.loadForReadOnly(componentId, id,
            getUserInSession(request).getUserPreferences().getLanguage());
        displayWysiwyg(html, request, response, componentId);
      }
    } else {
      PublicationDetail pub = getKmeliaBm().getPublicationDetail(new PublicationPK(id));
      if (pub.canBeAccessedBy(getUserInSession(request))) {
        if (pub.getInfoId().equals("0")) {
          // wysiwyg
          html = pub.getWysiwyg();
          displayWysiwyg(html, request, response, pub.getInstanceId());
        } else {
          // form xml
          displayFormView(new PrintWriter(response.getOutputStream()), pub, getUserInSession(request),
              ua);
        }
      } else {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
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
    Elements embeds = doc.getElementsByTag("embed");
    for (Element embed : embeds) {
      String htmlPart = embed.outerHtml();
      if (htmlPart.contains("flash")) {
        String attachmentId =
            htmlPart.substring(htmlPart.indexOf("attachmentId/") + "attachmentId/".length());
        attachmentId = attachmentId.substring(0, attachmentId.indexOf("/"));
        SimpleDocument attachment = AttachmentServiceProvider.getAttachmentService()
            .searchDocumentById(new SimpleDocumentPK(attachmentId),
                getUserInSession(request).getUserPreferences().getLanguage());
        String type = attachment.getContentType();
        String url = getServletContext().getContextPath() + "/services/spmobile/Attachment";
        url = url + "?id=" + attachmentId + "&instanceId=" + instanceId + "&lang=" +
            getUserInSession(request).getUserPreferences().getLanguage() + "&userId=" +
            getUserInSession(request).getId();
        if (type.equals("audio/mpeg") || type.equals("audio/ogg") || type.equals("audio/wav")) {
          embed.parent()
              .append("<audio controls><source src='" + url + "' type='" + type + "'></audio>");
          embed.remove();
        } else if (type.equals("video/mp4") || type.equals("video/ogg") ||
            type.equals("video/webm")) {
          embed.parent().append(
              "<video controls='controls'><source src='" + url + "' type='" + type + "' />");
          embed.remove();
        }
      }
    }
    html = doc.outerHtml();
    OutputStreamWriter out = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
    writeContainer(out, html);
    out.flush();
  }

  private void displayFormView(Writer out, PublicationDetail pub, UserDetail user, String ua)
      throws Exception {

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

    StringWriter generatedHtml = new StringWriter();
    PrintWriter outTmp = new PrintWriter(generatedHtml);

    Form xmlForm = pubTemplate.getViewForm();
    if (xmlForm instanceof XmlForm) {
      Method m = XmlForm.class.getDeclaredMethod("display",
          new Class[]{PrintWriter.class, PagesContext.class, DataRecord.class});
      m.setAccessible(true);
      m.invoke(xmlForm, outTmp, xmlContext, xmlData);
      outTmp.flush();
    } else if (xmlForm instanceof HtmlForm) {
      String html = ((HtmlForm) xmlForm).toString(xmlContext, xmlData);
      outTmp.write(html);
      outTmp.flush();
    }
    String html = generatedHtml.toString();

    Document doc = Jsoup.parse(html);
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
    Elements links = doc.getElementsByTag("a");
    for (Element link : links) {
      if (link.attr("href").startsWith("/silverpeas/attached_file/componentId/")) {
        // link to file
        String url = link.attr("href");
        String attachmentId =
            url.substring(url.indexOf("attachmentId/") + "attachmentId/".length());
        attachmentId = attachmentId.substring(0, attachmentId.indexOf("/"));
        url = getServletContext().getContextPath() + "/services/spmobile/Attachment";
        url = url + "?id=" + attachmentId + "&instanceId=" + pub.getInstanceId() + "&lang=" +
            user.getUserPreferences().getLanguage() + "&userId=" + user.getId();
        link.attr("href", url);
        link.attr("target", "_self");

        if (link.attr("id").startsWith("player")) {

          boolean playable = false;

          SimpleDocument attachment = AttachmentServiceProvider.getAttachmentService()
              .searchDocumentById(new SimpleDocumentPK(attachmentId),
                  user.getUserPreferences().getLanguage());
          String type = attachment.getContentType();
          if (type.contains("mp4") || type.contains("ogg") || type.contains("webm")) {
            playable = true;
          }

          if (playable) {
            String style = link.attr("style");
            String width = style.substring(style.indexOf("width") + "width".length() + 1);
            width = width.substring(0, width.indexOf("px"));
            String height = style.substring(style.indexOf("height") + "height".length() + 1);
            height = height.substring(0, height.indexOf("px"));
            link.parent().append("<video width='" + width + "' height='" + height +
                "' controls='controls'><source src='" + url + "' type='" + type + "' />");
            link.remove();
          } else {
            // display image instead of video player
            String style =
                "display:block; width:150px; height:98px; background-repeat: no-repeat; ";
            style +=
                "background-image: url(data:image/jpeg;base64," + "/9j/4AAQSkZJRgABAQEBLAEsAAD" +
                    "/4QYfRXhpZgAATU0AKgAAAAgAAAAAAA4AAgIBAAQAAAABAAAALAICAAQAAAABAAAF6wAAAAD/2P" +
                    "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);";
            link.attr("style", style);
          }
        }
      }
    }

    // remove all scripts
    Elements scripts = doc.getElementsByTag("script");
    for (Element script : scripts) {
      script.remove();
    }
    html = doc.outerHtml();
    writeContainer(out, html);
    out.flush();
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

        String path = FileRepositoryManager.getAbsolutePath(null, instanceId, rep);
        File f = new File(path + photo.getFileName());

        FileInputStream is = new FileInputStream(f);
        byte[] binaryData = new byte[(int) f.length()];
        is.read(binaryData);
        is.close();
        data = "data:" + photo.getFileMimeType() + ";base64," +
            new String(Base64.encodeBase64(binaryData));
      } catch (Exception e) {
        SilverLogger.getLogger(SpMobileLogModule.getName())
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
        byte[] binaryData = new byte[(int) connection.getInputStream().available()];
        connection.getInputStream().read(binaryData);
        data = "data:" + contentType + ";base64," + new String(Base64.encodeBase64(binaryData));
      } catch (Exception e) {
        SilverLogger.getLogger(SpMobileLogModule.getName())
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
      SilverLogger.getLogger(SpMobileLogModule.getName())
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
      e.printStackTrace();
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
    return getMainSessionController(request).getComponentParameterValue(instanceId, "xmlTemplate");
  }

  public boolean isXMLTemplateUsed(HttpServletRequest request, String instanceId) throws Exception {
    return StringUtil.isDefined(getUsedXMLTemplate(request, instanceId));
  }

  protected MainSessionController getMainSessionController(HttpServletRequest request) throws Exception {
    MainSessionController mainSessionController = (MainSessionController) request.getSession().getAttribute(MAINSESSIONCONTROLLER_ATTRIBUT_NAME);
    return mainSessionController;
  }

  protected String getUserKeyInSession(HttpServletRequest request) {
    return (String) request.getSession().getAttribute(USERKEY_ATTRIBUT_NAME);
  }

  private PublicationTemplate getXMLTemplate(HttpServletRequest request, String instanceId)
      throws Exception {
    registerXMLForm(request, instanceId);
    return PublicationTemplateManager.getInstance().getPublicationTemplate(
        instanceId + ":" + getUsedXMLTemplateShortname(request, instanceId));
  }

  private void registerXMLForm(HttpServletRequest request, String instanceId) throws Exception {
    if (isXMLTemplateUsed(request, instanceId)) {
      // register xmlForm to component

      PublicationTemplateManager.getInstance().addDynamicPublicationTemplate(
          instanceId + ":" + getUsedXMLTemplateShortname(request, instanceId),
          getUsedXMLTemplate(request, instanceId));
      getUsedXMLTemplate(request, instanceId);
    }
  }


  private String getUsedXMLTemplateShortname(HttpServletRequest request, String instanceId)
      throws Exception {
    String xmlFormName = getUsedXMLTemplate(request, instanceId);
    return xmlFormName.substring(xmlFormName.indexOf('/') + 1, xmlFormName.indexOf('.'));
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
