/*
 * Copyright (C) 2000 - 2024 Silverpeas
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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.silverpeas.components.kmelia.service.KmeliaService;
import org.silverpeas.core.contribution.publication.model.PublicationDetail;
import org.silverpeas.core.contribution.publication.model.PublicationPK;
import org.silverpeas.core.node.model.NodePK;
import org.silverpeas.core.subscription.SubscriptionServiceProvider;
import org.silverpeas.core.subscription.service.NodeSubscriptionResource;
import org.silverpeas.core.subscription.util.SubscriptionSubscriberList;
import org.silverpeas.core.util.file.FileRepositoryManager;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("serial")
public class FileServlet extends AbstractSilverpeasMobileServlet {

  private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
  private static long MAX_FILE_SIZE      = 1024 * 1024 * 100; // 100MB
  private static long MAX_REQUEST_SIZE   = 1024 * 1024 * 110; // 110MB

  @Override
  public void init(final ServletConfig config) throws ServletException {
    super.init(config);
    MAX_FILE_SIZE = FileRepositoryManager.getUploadMaximumFileSize();
    MAX_REQUEST_SIZE = (long) (MAX_FILE_SIZE * 1.1);
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

  protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      checkUserInSession(request, response);
      processRequest(request, response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



    String componentId = "";
    String publicationId = "";
    String folderId = "";
    String tempDir = FileRepositoryManager.getTemporaryPath();

    // configures upload settings
    DiskFileItemFactory factory = new DiskFileItemFactory();
    // sets memory threshold - beyond which files are stored in disk
    factory.setSizeThreshold(MEMORY_THRESHOLD);
    // sets temporary location to store files
    factory.setRepository(new File(tempDir));

    ServletFileUpload upload = new ServletFileUpload(factory);

    // sets maximum size of upload file
    upload.setFileSizeMax(MAX_FILE_SIZE);

    // sets maximum size of request (include file + form data)
    upload.setSizeMax(MAX_REQUEST_SIZE);

    // Parse the request
    @SuppressWarnings("unchecked")
    List<FileItem> items = null;
    try {
      items = upload.parseRequest(request);
    } catch(FileUploadBase.FileSizeLimitExceededException eu) {
      response.sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
      return;
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
        if (item.getFieldName().equals("folderId")) folderId = item.getString();
        if (item.getFieldName().equals("publicationId")) publicationId = item.getString();

      }
      else {
        String fileName = item.getName();
        File file = new File(tempDir + File.separator + fileName);
        try {
          item.write(file);
          if(folderId.isEmpty()) {
            addFileToPublication(request, fileName, componentId, publicationId, file);
          } else {
            createPublication(request, fileName, componentId, folderId, file);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void addFileToPublication(HttpServletRequest request, String name, String componentId, String publicationId,
                                    File file) throws Exception {

    PublicationPK pk = new PublicationPK(publicationId, componentId);
    KmeliaService.get().addAttachmentToPublication(pk, getUserInSession(request).getId(), name, "",
            FileUtils.readFileToByteArray(file));
  }

  private String createPublication(HttpServletRequest request, String name,
                                   String componentId, String folderId, File file) throws Exception {

    PublicationDetail pub = PublicationDetail.builder().build();
    pub.setName(file.getName());
    PublicationPK pk = new PublicationPK("", componentId);
    pub.setPk(pk);
    pub.setCreatorId(getUserInSession(request).getId());

    NodePK node = new NodePK(folderId);
    node.setComponentName(componentId);

    String pubId = KmeliaService.get().createPublicationIntoTopic(pub, node);
    pk.setId(pubId);

    KmeliaService.get().addAttachmentToPublication(pk, getUserInSession(request).getId(), name, "", FileUtils.readFileToByteArray(file));


    SubscriptionSubscriberList l = SubscriptionServiceProvider.getSubscribeService()
            .getSubscribers(NodeSubscriptionResource.from(node));


    KmeliaService.get().getUserNotification(node).send();

    return pubId;
  }

}
