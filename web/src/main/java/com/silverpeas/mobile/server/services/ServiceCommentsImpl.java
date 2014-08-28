package com.silverpeas.mobile.server.services;

import com.silverpeas.comment.model.Comment;
import com.silverpeas.comment.service.CommentServiceFactory;
import com.silverpeas.mobile.server.common.SpMobileLogModule;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.comments.CommentDTO;
import com.silverpeas.mobile.shared.dto.documents.AttachmentDTO;
import com.silverpeas.mobile.shared.dto.documents.PublicationDTO;
import com.silverpeas.mobile.shared.dto.documents.TopicDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.CommentsException;
import com.silverpeas.mobile.shared.exceptions.DocumentsException;
import com.silverpeas.mobile.shared.services.ServiceComments;
import com.silverpeas.mobile.shared.services.ServiceDocuments;
import com.stratelia.silverpeas.silvertrace.SilverTrace;
import com.stratelia.webactiv.beans.admin.OrganizationController;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.GeneralPropertiesManager;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.node.control.NodeBm;
import com.stratelia.webactiv.util.node.model.NodeDetail;
import com.stratelia.webactiv.util.node.model.NodePK;
import com.stratelia.webactiv.util.publication.control.PublicationBm;
import com.stratelia.webactiv.util.publication.model.PublicationDetail;
import com.stratelia.webactiv.util.publication.model.PublicationPK;
import org.silverpeas.attachment.AttachmentServiceFactory;
import org.silverpeas.attachment.model.DocumentType;
import org.silverpeas.attachment.model.SimpleDocument;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Service de gestion des GED.
 * @author svuillet
 */
public class ServiceCommentsImpl extends AbstractAuthenticateService implements ServiceComments {
	
	private static final long serialVersionUID = 1L;


  @Override
  public List<CommentDTO> getComments(String id, String type) throws CommentsException, AuthenticationException {
    checkUserInSession();
    ArrayList<CommentDTO> list = new ArrayList<CommentDTO>();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    List<Comment> comments = CommentServiceFactory.getFactory().getCommentService().getAllCommentsOnPublication(type, new PublicationPK(id));
    for (Comment c : comments) {
      CommentDTO dto = new CommentDTO();
      dto.setContent(c.getMessage());
      dto.setUserName(c.getOwner());
      dto.setAvatar("");
      dto.setAvatar(GeneralPropertiesManager.getString("ApplicationURL")+c.getOwnerDetail().getAvatar());
      dto.setDate(sdf.format(c.getCreationDate()));
      list.add(dto);
    }

    return list;
  }
}
