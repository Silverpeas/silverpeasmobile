package com.silverpeas.mobile.server.services;

import com.silverpeas.comment.model.Comment;
import com.silverpeas.comment.model.CommentPK;
import com.silverpeas.comment.service.CommentServiceFactory;
import com.silverpeas.mobile.server.common.SpMobileLogModule;
import com.silverpeas.mobile.server.helpers.DataURLHelper;
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
import java.util.Date;
import java.util.List;

/**
 * Service de gestion des GED.
 * @author svuillet
 */
public class ServiceCommentsImpl extends AbstractAuthenticateService implements ServiceComments {
	
	private static final long serialVersionUID = 1L;
  private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

  @Override
  public List<CommentDTO> getComments(String id, String type) throws CommentsException, AuthenticationException {
    checkUserInSession();
    ArrayList<CommentDTO> list = new ArrayList<CommentDTO>();
    List<Comment> comments = CommentServiceFactory.getFactory().getCommentService().getAllCommentsOnPublication(
        type, new PublicationPK(id));
    for (Comment c : comments) {
      CommentDTO dto = populate(c);
      list.add(dto);
    }

    return list;
  }

  private CommentDTO populate(final Comment c) {
    CommentDTO dto = new CommentDTO();
    dto.setContent(c.getMessage());
    dto.setUserName(c.getOwner());
    String avatar = DataURLHelper.convertAvatarToUrlData(c.getOwnerDetail().getAvatarFileName(), "24x");
    dto.setAvatar(avatar);
    dto.setDate(sdf.format(c.getCreationDate()));
    return dto;
  }

  @Override
  public CommentDTO addComment(String id, String instanceId, String type, String message) throws CommentsException, AuthenticationException {
    checkUserInSession();
    CommentDTO comment = new CommentDTO();
    Date now = new Date();
    Comment c = new Comment(new CommentPK("", instanceId), type, new PublicationPK(
        id, instanceId), Integer.valueOf(getUserInSession().getId()), getUserInSession().getDisplayedName(),
        message,
        now, now);
    c.setOwnerDetail(getUserInSession());

    CommentServiceFactory.getFactory().getCommentService().createComment(c);
    comment = populate(c);

    return comment;
  }
}
