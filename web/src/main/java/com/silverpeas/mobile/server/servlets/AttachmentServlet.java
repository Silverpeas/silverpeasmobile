package com.silverpeas.mobile.server.servlets;

import com.silverpeas.mobile.server.services.AbstractAuthenticateService;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.stratelia.webactiv.beans.admin.UserDetail;
import org.silverpeas.attachment.AttachmentServiceFactory;
import org.silverpeas.attachment.model.SimpleDocument;
import org.silverpeas.attachment.model.SimpleDocumentPK;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("serial")
public class AttachmentServlet extends HttpServlet {

    private UserDetail user;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String lang = request.getParameter("lang");

        SimpleDocument attachment = AttachmentServiceFactory.getAttachmentService().
                searchDocumentById(new SimpleDocumentPK(id), lang);
        if (attachment.canBeAccessedBy(user)) {
            response.setContentType(attachment.getContentType());
            response.setHeader("content-disposition", "filename=" + attachment.getFilename());
            AttachmentServiceFactory.getAttachmentService().getBinaryContent(response.getOutputStream(), new SimpleDocumentPK(id), lang);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
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
        } else {
            user = (UserDetail) request.getSession().getAttribute(AbstractAuthenticateService.USER_ATTRIBUT_NAME);
        }
    }
}
