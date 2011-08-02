package com.oosphere.silverpeasmobile.kmelia;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.oosphere.silverpeasmobile.comment.comparator.CommentReverseIdComparator;
import com.oosphere.silverpeasmobile.exception.SilverpeasMobileException;
import com.oosphere.silverpeasmobile.trace.SilverpeasMobileTrace;
import com.oosphere.silverpeasmobile.vo.AttachmentVO;
import com.oosphere.silverpeasmobile.vo.ComponentVO;
import com.oosphere.silverpeasmobile.vo.PublicationVO;
import com.oosphere.silverpeasmobile.vo.SpaceVO;
import com.oosphere.silverpeasmobile.vo.comparator.PublicationVOComparator;
import com.silverpeas.admin.components.Parameter;
import com.silverpeas.admin.ejb.AdminBm;
import com.silverpeas.comment.model.Comment;
import com.silverpeas.comment.model.CommentPK;
import com.silverpeas.comment.service.CommentService;
import com.silverpeas.comment.service.CommentServiceFactory;
import com.silverpeas.external.filesharing.model.FileSharingService;
import com.silverpeas.external.filesharing.model.FileSharingServiceFactory;
import com.silverpeas.external.filesharing.model.TicketDetail;
import com.silverpeas.util.StringUtil;
import com.stratelia.webactiv.beans.admin.ComponentInst;
import com.stratelia.webactiv.beans.admin.OrganizationController;
import com.stratelia.webactiv.beans.admin.SpaceInst;
import com.stratelia.webactiv.beans.admin.UserDetail;
import com.stratelia.webactiv.kmelia.control.ejb.KmeliaBm;
import com.stratelia.webactiv.kmelia.model.TopicDetail;
import com.stratelia.webactiv.kmelia.model.UserPublication;
import com.stratelia.webactiv.util.WAPrimaryKey;
import com.stratelia.webactiv.util.attachment.control.AttachmentBm;
import com.stratelia.webactiv.util.attachment.control.AttachmentBmImpl;
import com.stratelia.webactiv.util.attachment.ejb.AttachmentException;
import com.stratelia.webactiv.util.attachment.ejb.AttachmentPK;
import com.stratelia.webactiv.util.attachment.model.AttachmentDAO;
import com.stratelia.webactiv.util.attachment.model.AttachmentDetail;
import com.stratelia.webactiv.util.node.model.NodeDetail;
import com.stratelia.webactiv.util.node.model.NodePK;
import com.stratelia.webactiv.util.publication.model.CompletePublication;
import com.stratelia.webactiv.util.publication.model.PublicationDetail;
import com.stratelia.webactiv.util.publication.model.PublicationPK;

public class KmeliaManager {

  private static final String KMELIA = "kmelia";

  private DateFormat fullDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
  private DateFormat shortDateFormat = new SimpleDateFormat("yyyy/MM/dd");
  private DateFormat hourDateFormat = new SimpleDateFormat("HH:mm");

  private AdminBm adminBm;
  private KmeliaBm kmeliaBm;
  private OrganizationController organizationController;

  public KmeliaManager(AdminBm adminBm, KmeliaBm kmeliaBm,
      OrganizationController organizationController) {
    this.adminBm = adminBm;
    this.kmeliaBm = kmeliaBm;
    this.organizationController = organizationController;
  }

  public List<SpaceVO> getKmeliaSpaces(String userId) {
    ArrayList<SpaceVO> spaces = new ArrayList<SpaceVO>();
    List<String> spaceIds = getAvailableSpaceIds(userId);
    for (String spaceId : spaceIds) {
      List<String> kmeliaIds = getAvailableKmeliaIds(userId, spaceId);
      if (!kmeliaIds.isEmpty()) {
        SpaceVO space = getSpace(spaceId);
        if (space != null) {
          for (String kmeliaId : kmeliaIds) {
            ComponentVO component = getComponent(kmeliaId);
            if (component != null) {
              space.addComponent(component);
            }
          }
        }
        spaces.add(space);
      }
    }
    return spaces;
  }

  @SuppressWarnings("unchecked")
  private List<String> getAvailableSpaceIds(String userId) {
    List<String> spaceIds = new ArrayList<String>();
    try {
      spaceIds = adminBm.getAvailableSpaceIds(userId);
    } catch (RemoteException e) {
      SilverpeasMobileTrace.error(this, "getAvailableSpaceIds", "EX_GET_COMPONENT_IDS_FAILED",
          "userId = " + userId, e);
    }
    return spaceIds;
  }

  @SuppressWarnings("unchecked")
  private List<String> getAvailableComponentIds(String userId, String spaceId) {
    List<String> componentIds = new ArrayList<String>();
    try {
      componentIds = adminBm.getAvailCompoIds(spaceId, userId);
    } catch (RemoteException e) {
      SilverpeasMobileTrace.error(this, "getAvailableComponentIds", "EX_GET_COMPONENT_IDS_FAILED",
          "spaceId = " + spaceId + " ; userId = " + userId, e);
    }
    return componentIds;
  }

  private List<String> getAvailableKmeliaIds(String userId, String spaceId) {
    List<String> availableComponentIds = getAvailableComponentIds(userId, spaceId);
    ArrayList<String> componentIds = new ArrayList<String>();
    for (String availableComponentId : availableComponentIds) {
      if (availableComponentId.startsWith(KMELIA)) {
        componentIds.add(availableComponentId);
      }
    }
    return componentIds;
  }

  private SpaceVO getSpace(String spaceId) {
    try {
      SpaceInst space = adminBm.getSpaceInstById(spaceId);
      return new SpaceVO(space.getId(), space.getName());
    } catch (RemoteException e) {
      return null;
    }
  }

  public ComponentVO getComponent(String componentId) {
    try {
      ComponentInst component = adminBm.getComponentInst(componentId);
      return new ComponentVO(component.getId(), component.getLabel());
    } catch (RemoteException e) {
      return null;
    }
  }

  public List<Comment> getPublicationComments(String componentId, String publicationId) throws SilverpeasMobileException {
    CommentService commentService = CommentServiceFactory.getFactory().getCommentService();
    List<Comment> comments = commentService.getAllCommentsOnPublication(new CommentPK(publicationId, componentId));
    Collections.sort(comments, new CommentReverseIdComparator());
    return comments;
  }
  
  public void addPublicationComment(String componentId, String publicationId, String newComment, String userId) throws SilverpeasMobileException {
    Calendar calendar = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    String today = dateFormat.format(calendar.getTime());
    
    CommentPK commentPK = new CommentPK(publicationId, componentId); 
    Comment comment = new Comment(commentPK, new PublicationPK(publicationId), Integer.valueOf(userId).intValue(), userId, newComment, today, today);
    
    CommentService commentService = CommentServiceFactory.getFactory().getCommentService();
    commentService.createComment(comment);
  }

  public TicketDetail generateFileSharingTicket(String componentId, String attachmentId,
      String userId) throws SilverpeasMobileException {
    Calendar endCalendar = Calendar.getInstance();
    endCalendar.add(Calendar.DAY_OF_MONTH, 1);
    Date endDate = endCalendar.getTime();

    UserDetail user = getCurrentUser(userId);

    TicketDetail newTicket = TicketDetail.aTicket(Integer.parseInt(attachmentId), componentId,
        false, user, new Date(), endDate, 1);
    FileSharingService fileSharingService =
        FileSharingServiceFactory.getFactory().getFileSharingService();
    String shareKey = fileSharingService.createTicket(newTicket);
    newTicket.setKeyFile(shareKey);
    return newTicket;
  }

  private UserDetail getCurrentUser(String userId) throws SilverpeasMobileException {
    UserDetail user = null;
    if (StringUtil.isDefined(userId)) {
      OrganizationController organizationController = new OrganizationController();
      user = organizationController.getUserDetail(userId);
    } else {
      throw new SilverpeasMobileException(this, "getCurrentUser",
          "Parameter userId not present in request");
    }
    return user;
  }

  public boolean isAttachmentSharable(String attachmentId) throws SilverpeasMobileException {
    AttachmentDetail attachment = getAttachmentDetail(attachmentId);
    return isComponentWithFileSharing(attachment.getInstanceId());
  }

  public boolean isAttachmentCommentable(String attachmentId) throws SilverpeasMobileException {
    AttachmentDetail attachment = getAttachmentDetail(attachmentId);
    return isComponentWithComments(attachment.getInstanceId());
  }

  public boolean isAttachmentNotifiable(String attachmentId) throws SilverpeasMobileException {
    AttachmentDetail attachment = getAttachmentDetail(attachmentId);
    return isComponentWithNotifications(attachment.getInstanceId());
  }

  public boolean isComponentWithFileSharing(String componentId) throws SilverpeasMobileException {
    try {
      ComponentInst component = adminBm.getComponentInst(componentId);
      Parameter fileSharingParameter = component.getParameter("useFileSharing");
      return isParameterActive(fileSharingParameter);
    } catch (RemoteException e) {
      throw new SilverpeasMobileException(this, "isComponentWithFileSharing",
          "Error while retreiving component", e);
    }
  }

  public boolean isComponentWithComments(String componentId) throws SilverpeasMobileException {
    try {
      ComponentInst component = adminBm.getComponentInst(componentId);
      Parameter fileSharingParameter = component.getParameter("tabComments");
      return isParameterActive(fileSharingParameter);
    } catch (RemoteException e) {
      throw new SilverpeasMobileException(this, "isComponentWithComments",
          "Error while retreiving component", e);
    }
  }

  public boolean isComponentWithNotifications(String componentId) throws SilverpeasMobileException {
    try {
      ComponentInst component = adminBm.getComponentInst(componentId);
      Parameter fileSharingParameter = component.getParameter("notifications");
      return isParameterActive(fileSharingParameter);
    } catch (RemoteException e) {
      throw new SilverpeasMobileException(this, "isComponentWithNotifications",
          "Error while retreiving component", e);
    }
  }

  private boolean isParameterActive(Parameter fileSharingParameter) {
    return fileSharingParameter != null && "yes".equalsIgnoreCase(fileSharingParameter.getValue());
  }

  private AttachmentDetail getAttachmentDetail(String attachmentId)
      throws SilverpeasMobileException {
    try {
      AttachmentBm attachmentBm = new AttachmentBmImpl();
      AttachmentDetail attachment =
          attachmentBm.getAttachmentByPrimaryKey(new AttachmentPK(attachmentId));
      return attachment;
    } catch (AttachmentException e) {
      throw new SilverpeasMobileException(this, "getAttachmentDetail",
          "Error while retreiving attachment", e);
    }
  }

  public List<NodeDetail> getTopics(String id, String userId, String userProfile, String spaceId,
      String componentId)
      throws SilverpeasMobileException {
    TopicDetail topic = getTopicDetail(id, userId, userProfile, spaceId, componentId);
    return (List<NodeDetail>) topic.getNodeDetail().getChildrenDetails();
  }

  public TopicDetail getTopicDetail(String id, String userId, String userProfile, String spaceId,
      String componentId)
      throws SilverpeasMobileException {
    TopicDetail currentTopic;
    try {
      NodePK nodePK = new NodePK(id, spaceId, componentId);
      currentTopic = kmeliaBm.goTo(nodePK, userId, true, userProfile, false);
    } catch (RemoteException e) {
      throw new SilverpeasMobileException(this, "getTopicDetail", "EX_GET_TOPIC_FAILED", e);
    }

    List<NodeDetail> treeview;
    try {
      NodePK nodePK = new NodePK("0", spaceId, componentId);
      treeview = kmeliaBm.getTreeview(
          nodePK, "admin", false, false, userId, displayNbPublis(), false);
    } catch (RemoteException e) {
      throw new SilverpeasMobileException(this, "getTopicDetail", "EX_GET_TREEVIEW_FAILED", e);
    }

    if (displayNbPublis()) {
      List<NodeDetail> children = (List<NodeDetail>) currentTopic.getNodeDetail()
          .getChildrenDetails();
      for (int n = 0; n < children.size(); n++) {
        NodeDetail node = children.get(n);
        if (node != null) {
          int index = treeview.indexOf(node);
          if (index != -1) {
            NodeDetail nodeTreeview = treeview.get(index);
            if (nodeTreeview != null) {
              node.setNbObjects(nodeTreeview.getNbObjects());
            }
          }
        }
      }
    }
    return currentTopic;
  }

  private boolean displayNbPublis() {
    return true;
  }

  public List<PublicationVO> getTopicPublications(String id, String userId, String userProfile,
      String spaceId, String componentId) throws SilverpeasMobileException {
    TopicDetail topic = getTopicDetail(id, userId, userProfile, spaceId, componentId);
    List<UserPublication> userPublications = (List<UserPublication>) topic.getPublicationDetails();
    return getPublications(userPublications);
  }

  public List<PublicationVO> getPublications(List<UserPublication> userPublications)
      throws SilverpeasMobileException {
    List<PublicationVO> publications = new ArrayList<PublicationVO>();
    for (UserPublication userPublication : userPublications) {
      PublicationVO publication = getPublication(userPublication);
      if (publication != null) {
        publications.add(publication);
      }
    }
    Collections.sort(publications, new PublicationVOComparator());
    return publications;
  }

  private PublicationVO getPublication(UserPublication userPublication) {
    return getPublication(userPublication.getPublication());
  }

  public PublicationVO getPublication(String id) {
    try {
      PublicationDetail publi = kmeliaBm.getPublicationDetail(new PublicationPK(id));
      return getPublication(publi);
    } catch (RemoteException e) {
      return null;
    }
  }

  private PublicationVO getPublication(PublicationDetail publicationDetail) {
    String creatorId = publicationDetail.getCreatorId();
    String creatorName = getUserName(creatorId);
    Date creationDate = getPublicationDate(
        publicationDetail.getCreationDate(), publicationDetail.getBeginHour());
    PublicationVO publication = new PublicationVO(publicationDetail.getId(),
        publicationDetail.getName(), publicationDetail.getDescription(), creatorId, creatorName,
        creationDate);

    String updaterId = publicationDetail.getUpdaterId();
    if (StringUtil.isDefined(updaterId)) {
      Date updateDate = getPublicationDate(
          publicationDetail.getUpdateDate(), publicationDetail.getEndHour());
      publication.setUpdaterData(updaterId, getUserName(updaterId), updateDate);
    }

    List<AttachmentDetail> attachments =
        (List<AttachmentDetail>) publicationDetail.getAttachments();
    for (AttachmentDetail attachment : attachments) {
      publication.addAttachment(new AttachmentVO(attachment.getLogicalName(),
          attachment.getPK().getId(), attachment.getAttachmentFileSize(),
          attachment.getAttachmentIcon(), attachment.getAttachmentURL()));
    }

    return publication;
  }

  private Date getPublicationDate(Date incompleteDate, String hour) {
    Date date = null;
    try {
      date = fullDateFormat.parse(shortDateFormat.format(incompleteDate) + " " + hour);
    } catch (ParseException e) {
      // Date format error
    }
    return date;
  }

  protected UserDetail getUserDetail(String userId) {
    return organizationController.getUserDetail(userId);
  }

  protected String getUserName(String userId) {
    String name = null;
    UserDetail user = getUserDetail(userId);
    if (user != null) {
      name = user.getDisplayedName();
    }
    return (name != null ? name.trim() : "");
  }

}
