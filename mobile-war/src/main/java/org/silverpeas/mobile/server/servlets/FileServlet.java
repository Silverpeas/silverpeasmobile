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

import jakarta.inject.Inject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.silverpeas.components.kmelia.service.KmeliaService;
import org.silverpeas.core.contribution.publication.model.PublicationDetail;
import org.silverpeas.core.contribution.publication.model.PublicationPK;
import org.silverpeas.core.node.model.NodePK;
import org.silverpeas.core.util.file.FileItem;
import org.silverpeas.core.util.file.FileRepositoryManager;
import org.silverpeas.core.util.file.FileUploadSizeLimitException;
import org.silverpeas.core.util.file.FileUploadUtil;
import org.silverpeas.kernel.SilverpeasRuntimeException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileServlet extends AbstractSilverpeasMobileServlet {

  @Inject
  private KmeliaService kmeliaService;

  @Override
  public void init(final ServletConfig config) throws ServletException {
    super.init(config);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String componentId = "";
    String publicationId = "";
    String folderId = "";
    FileUploadUtil.parseRequest(request);
    String tempDir = FileRepositoryManager.getTemporaryPath();

    // Parse the request
    List<FileItem> items;
    try {
      items = FileUploadUtil.parseRequest(request);
    } catch (FileUploadSizeLimitException e) {
      response.sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
      return;
    } catch (SilverpeasRuntimeException e) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    }

    // Process the uploaded items
    for (FileItem item : items) {
      if (item.isFormField()) {
        if (item.getFieldName().equals("componentId")) componentId = item.getContent();
        if (item.getFieldName().equals("folderId")) folderId = item.getContent();
        if (item.getFieldName().equals("publicationId")) publicationId = item.getContent();

      } else {
        String fileName = item.getFileName();
        File file = new File(tempDir + File.separator + fileName);
        try {
          item.saveTo(file);
          if (scanAntivirus(response, file)) return;
          if (folderId.isEmpty()) {
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

  private void addFileToPublication(HttpServletRequest request, String name, String componentId,
      String publicationId,
      File file) throws Exception {

    PublicationPK pk = new PublicationPK(publicationId, componentId);
    kmeliaService.addAttachmentToPublication(pk, getUserInSession(request).getId(), name, "",
        FileUtils.readFileToByteArray(file));
  }

  private void createPublication(HttpServletRequest request, String name,
      String componentId, String folderId, File file) throws Exception {

    PublicationDetail pub = PublicationDetail.builder().build();
    pub.setName(file.getName());
    PublicationPK pk = new PublicationPK("", componentId);
    pub.setPk(pk);
    pub.setCreatorId(getUserInSession(request).getId());

    NodePK node = new NodePK(folderId);
    node.setComponentName(componentId);

    String pubId = kmeliaService.createPublicationIntoTopic(pub, node);
    pk.setId(pubId);

    kmeliaService.addAttachmentToPublication(pk, getUserInSession(request).getId(), name, "",
        FileUtils.readFileToByteArray(file));
    kmeliaService.getUserNotification(node).send();
  }

}
