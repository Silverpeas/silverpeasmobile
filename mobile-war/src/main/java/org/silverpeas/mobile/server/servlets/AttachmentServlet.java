/*
 * Copyright (C) 2000 - 2018 Silverpeas
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
 */

package org.silverpeas.mobile.server.servlets;

import org.silverpeas.core.contribution.attachment.AttachmentServiceProvider;
import org.silverpeas.core.contribution.attachment.model.SimpleDocument;
import org.silverpeas.core.contribution.attachment.model.SimpleDocumentPK;
import org.silverpeas.core.util.logging.SilverLogger;
import org.silverpeas.mobile.server.common.SpMobileLogModule;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("serial")
public class AttachmentServlet extends AbstractSilverpeasMobileServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String lang = request.getParameter("lang");

        SimpleDocument attachment = AttachmentServiceProvider.getAttachmentService().
                searchDocumentById(new SimpleDocumentPK(id), lang);
        if (attachment.canBeAccessedBy(getUserInSession(request))) {
            response.setContentType(attachment.getContentType());
            response.setHeader("content-disposition", "filename=" + attachment.getFilename());
            AttachmentServiceProvider.getAttachmentService().getBinaryContent(response.getOutputStream(), new SimpleDocumentPK(id), lang);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

      try {
        checkUserInSession(request, response);
        processRequest(request, response);
      } catch (Exception e) {
        SilverLogger.getLogger(SpMobileLogModule.getName()).error("AttachmentServlet", e);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      }
    }
}
