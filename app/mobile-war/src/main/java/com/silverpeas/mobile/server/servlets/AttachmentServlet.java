package com.silverpeas.mobile.server.servlets;

import com.silverpeas.mobile.server.common.SpMobileLogModule;
import com.silverpeas.mobile.server.services.AbstractAuthenticateService;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.contribution.attachment.AttachmentServiceProvider;
import org.silverpeas.core.contribution.attachment.model.SimpleDocument;
import org.silverpeas.core.contribution.attachment.model.SimpleDocumentPK;
import org.silverpeas.core.util.logging.SilverLogger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

@SuppressWarnings("serial")
public class AttachmentServlet extends HttpServlet {

    private UserDetail user;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String lang = request.getParameter("lang");

        SimpleDocument attachment = AttachmentServiceProvider.getAttachmentService().
                searchDocumentById(new SimpleDocumentPK(id), lang);
        if (attachment.canBeAccessedBy(user)) {
            response.setContentType(attachment.getContentType());
            response.setHeader("content-disposition", "filename=" + attachment.getFilename());
            AttachmentServiceProvider.getAttachmentService().getBinaryContent(response.getOutputStream(), new SimpleDocumentPK(id), lang);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            checkUserInSession(request);
            processRequest(request, response);
        } catch (Exception e) {
          SilverLogger.getLogger(SpMobileLogModule.getName()).error("AttachmentServlet", e);
          response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    protected void checkUserInSession(HttpServletRequest request) throws AuthenticationException {
        if (request.getSession().getAttribute(AbstractAuthenticateService.USER_ATTRIBUT_NAME) == null) {
            throw new AuthenticationException(AuthenticationException.AuthenticationError.NotAuthenticate);
        } else {
            user = (UserDetail) request.getSession().getAttribute(AbstractAuthenticateService.USER_ATTRIBUT_NAME);
        }
    }
}
