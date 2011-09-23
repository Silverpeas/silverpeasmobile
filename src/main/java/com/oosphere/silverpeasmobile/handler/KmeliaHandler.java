package com.oosphere.silverpeasmobile.handler;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.oosphere.silverpeasmobile.bean.WebBeanFactory;
import com.oosphere.silverpeasmobile.exception.SilverpeasMobileException;
import com.oosphere.silverpeasmobile.kmelia.KmeliaManager;
import com.oosphere.silverpeasmobile.vo.PublicationVO;
import com.silverpeas.comment.model.Comment;
import com.silverpeas.external.filesharing.model.TicketDetail;
import com.stratelia.webactiv.beans.admin.OrganizationController;
import com.stratelia.webactiv.beans.admin.UserDetail;
import com.stratelia.webactiv.kmelia.model.TopicDetail;
import com.stratelia.webactiv.util.node.model.NodeDetail;
import com.stratelia.webactiv.util.node.model.NodePK;

public class KmeliaHandler extends Handler {

  @Override
  public String getPage(HttpServletRequest request) throws SilverpeasMobileException {
    String page = "home.jsp";
    String subAction = request.getParameter("subAction");
    if ("home".equals(subAction)) {
      KmeliaManager kmeliaManager = getKmeliaManager(request);
      page = home(request, kmeliaManager);
    } else if ("component".equals(subAction)) {
      KmeliaManager kmeliaManager = getKmeliaManager(request);
      page = component(request, kmeliaManager);
    } else if ("publications".equals(subAction)) {
      KmeliaManager kmeliaManager = getKmeliaManager(request);
      page = publications(request, kmeliaManager);
    } else if ("publication".equals(subAction)) {
      KmeliaManager kmeliaManager = getKmeliaManager(request);
      page = publication(request, kmeliaManager);
    } else if ("share".equals(subAction)) {
      KmeliaManager kmeliaManager = getKmeliaManager(request);
      page = shareAttachment(request, kmeliaManager);
    } else if ("documentAction".equals(subAction)) {
      KmeliaManager kmeliaManager = getKmeliaManager(request);
      page = documentAction(request, kmeliaManager);
    } else if ("comments".equals(subAction)) {
      KmeliaManager kmeliaManager = getKmeliaManager(request);
      page = comments(request, kmeliaManager);
    } else if ("notify".equals(subAction)) {
      KmeliaManager kmeliaManager = getKmeliaManager(request);
      page = notify(request, kmeliaManager);
    } else if ("addComment".equals(subAction)) {
      KmeliaManager kmeliaManager = getKmeliaManager(request);
      page = addComment(request, kmeliaManager);
    } else if ("doNotify".equals(subAction)) {
      KmeliaManager kmeliaManager = getKmeliaManager(request);
      page = doNotify(request, kmeliaManager);
    }
    return "kmelia/" + page;
  }

  private String home(HttpServletRequest request, KmeliaManager kmeliaManager)
      throws SilverpeasMobileException {
    String userId = request.getParameter("userId");
    request.setAttribute("userId", userId);
    request.setAttribute("spaces", kmeliaManager.getKmeliaSpaces(userId));
    return "home.jsp";
  }

  private String component(HttpServletRequest request, KmeliaManager kmeliaManager) {
    List<NodeDetail> nodes = null;
    List<PublicationVO> publis = null;
    String spaceId = request.getParameter("spaceId");
    String componentId = request.getParameter("componentId");
    String userId = request.getParameter("userId");
    String nodeId = request.getParameter("nodeId");

    nodeId = (nodeId == null) ? "0" : nodeId;

    try {
      if (!nodeId.equals("0")) {
        TopicDetail topic =
            kmeliaManager.getTopicDetail(nodeId, userId, "user", spaceId, componentId);
        NodePK pk = topic.getNodeDetail().getFatherPK();
        topic = kmeliaManager.getTopicDetail(pk.getId(), userId, "user", spaceId, componentId);
        request.setAttribute("backLabel",
            (pk.getId().equals("0")) ? kmeliaManager.getComponent(componentId).getName() : topic
                .getNodeDetail().getName());
        request.setAttribute("backNodeId", pk.getId());
      }

      nodes = kmeliaManager.getTopics(nodeId, userId, "user", spaceId, componentId);
      publis = kmeliaManager.getTopicPublications(nodeId, userId, "user", spaceId, componentId);

    } catch (SilverpeasMobileException e) {
      nodes = new ArrayList<NodeDetail>();
      publis = new ArrayList<PublicationVO>();
    }

    request.setAttribute("userId", userId);
    request.setAttribute("component", kmeliaManager.getComponent(componentId));
    request.setAttribute("id", "0");
    request.setAttribute("spaceId", spaceId);
    request.setAttribute("nodes", nodes);
    request.setAttribute("publis", publis);

    return "component.jsp";
  }

  private String publications(HttpServletRequest request, KmeliaManager kmeliaManager)
      throws SilverpeasMobileException {
    String id = request.getParameter("id");
    String spaceId = request.getParameter("spaceId");
    String componentId = request.getParameter("componentId");
    String userId = request.getParameter("userId");
    String userProfile = request.getParameter("userProfile");
    request.setAttribute("publications",
        kmeliaManager.getTopicPublications(id, userId, userProfile, spaceId, componentId));
    request.setAttribute("userId", userId);
    request.setAttribute("spaceId", spaceId);
    request.setAttribute("componentId", componentId);
    return "publications.jsp";
  }

  private String publication(HttpServletRequest request, KmeliaManager kmeliaManager)
      throws SilverpeasMobileException {
    String publicationId = request.getParameter("pubId");
    String spaceId = request.getParameter("spaceId");
    String componentId = request.getParameter("componentId");
    String userId = request.getParameter("userId");
    String backLabel = request.getParameter("backLabel");

    PublicationVO publi = kmeliaManager.getPublication(publicationId);
    request.setAttribute("publication", publi);
    
    boolean isComponentWithComments = kmeliaManager.isComponentWithComments(componentId);
    request.setAttribute("commentsActive", isComponentWithComments);
    if(isComponentWithComments){
      List<Comment> comments = kmeliaManager.getPublicationComments(componentId, publicationId);
      request.setAttribute("comments", comments);
    }
    
    request.setAttribute("publicationId", publicationId);
    request.setAttribute("userId", userId);
    request.setAttribute("spaceId", spaceId);
    request.setAttribute("componentId", componentId);
    request.setAttribute("backLabel", backLabel);
    request.setAttribute("commentDataCollapsed", "true");

    return "publication.jsp";
  }
  
  private String documentAction(HttpServletRequest request, KmeliaManager kmeliaManager)
      throws SilverpeasMobileException {
    String attachmentId = request.getParameter("attachmentId");
    String publicationId = request.getParameter("publicationId");
    String componentId = request.getParameter("componentId");
    String spaceId = request.getParameter("spaceId");
    String userId = request.getParameter("userId");
    
    PublicationVO publi = kmeliaManager.getPublication(publicationId);
    request.setAttribute("publication", publi);
    
    boolean isComponentWithFileSharing = kmeliaManager.isAttachmentSharable(attachmentId);
//    boolean isComponentWithComments = kmeliaManager.isAttachmentCommentable(attachmentId);
    boolean isComponentWithNotifications = kmeliaManager.isAttachmentNotifiable(attachmentId);

    request.setAttribute("fileSharingActive", isComponentWithFileSharing);
//    request.setAttribute("commentsActive", isComponentWithComments);
    request.setAttribute("notificationsActive", isComponentWithNotifications);
    request.setAttribute("userId", userId);
    request.setAttribute("componentId", componentId);
    request.setAttribute("attachmentId", attachmentId);
    request.setAttribute("publicationId", publicationId);
    request.setAttribute("spaceId", spaceId);

    return "documentAction.jsp";
  }
  
  private String shareAttachment(HttpServletRequest request, KmeliaManager kmeliaManager)
      throws SilverpeasMobileException {
    
    String componentId = request.getParameter("componentId");
    String attachmentId = request.getParameter("attachmentId");
    String userId = request.getParameter("userId");
    
    TicketDetail newTicket = kmeliaManager.generateFileSharingTicket(componentId, attachmentId, userId);
    
    request.setAttribute("urlAttachment", newTicket.getUrl(request));
    request.setAttribute("userId", userId);
    
    return "share.jsp";
  }
  
  private String comments(HttpServletRequest request, KmeliaManager kmeliaManager)
      throws SilverpeasMobileException {
    
    String componentId = request.getParameter("componentId");
    String attachmentId = request.getParameter("attachmentId");
    String publicationId = request.getParameter("publicationId");
    String userId = request.getParameter("userId");
    
    List<Comment> comments = kmeliaManager.getPublicationComments(componentId, publicationId);
    
    request.setAttribute("comments", comments);
    request.setAttribute("userId", userId);
    request.setAttribute("componentId", componentId);
    request.setAttribute("attachmentId", attachmentId);
    request.setAttribute("publicationId", publicationId);
    
    return "comments.jsp";
  }
  
  private String addComment(HttpServletRequest request, KmeliaManager kmeliaManager)
      throws SilverpeasMobileException {
    
    String spaceId = request.getParameter("spaceId");
    String componentId = request.getParameter("componentId");
    String attachmentId = request.getParameter("attachmentId");
    String publicationId = request.getParameter("publicationId");
    String userId = request.getParameter("userId");
    String newComment = request.getParameter("comment");
    String backLabel = request.getParameter("backLabel");
    
    kmeliaManager.addPublicationComment(componentId, publicationId, newComment, userId);
    List<Comment> comments = kmeliaManager.getPublicationComments(componentId, publicationId);
    request.setAttribute("comments", comments);
    
    PublicationVO publi = kmeliaManager.getPublication(publicationId);
    request.setAttribute("publication", publi);
    
    request.setAttribute("userId", userId);
    request.setAttribute("componentId", componentId);
    request.setAttribute("attachmentId", attachmentId);
    request.setAttribute("publicationId", publicationId);
    request.setAttribute("spaceId", spaceId);
    request.setAttribute("backLabel", backLabel);
    request.setAttribute("commentDataCollapsed", "false");
    request.setAttribute("commentsActive", "true");
    
//    return "comments.jsp";
    return "publication.jsp";
  }
  
  private String notify(HttpServletRequest request, KmeliaManager kmeliaManager)
      throws SilverpeasMobileException {
    
    String spaceId = request.getParameter("spaceId");
    String componentId = request.getParameter("componentId");
    String attachmentId = request.getParameter("attachmentId");
    String publicationId = request.getParameter("publicationId");
    String userId = request.getParameter("userId");
    
    PublicationVO publi = kmeliaManager.getPublication(publicationId);
    request.setAttribute("publication", publi);
    
    List<UserDetail> listComponentUsers = kmeliaManager.getComponentUsers(componentId);
    request.setAttribute("listComponentUsers", listComponentUsers);
    
    request.setAttribute("spaceId", spaceId);
    request.setAttribute("publicationId", publicationId);
    request.setAttribute("userId", userId);
    request.setAttribute("componentId", componentId);
    request.setAttribute("attachmentId", attachmentId);
    
    return "notify.jsp";
  }
  
  
  private String doNotify(HttpServletRequest request, KmeliaManager kmeliaManager)
      throws SilverpeasMobileException {
    
    String spaceId = request.getParameter("spaceId");
    String componentId = request.getParameter("componentId");
    String attachmentId = request.getParameter("attachmentId");
    String publicationId = request.getParameter("publicationId");
    String userId = request.getParameter("userId");
    String message = request.getParameter("message");
    
    PublicationVO publi = kmeliaManager.getPublication(publicationId);
    request.setAttribute("publication", publi);
    
    List<UserDetail> listComponentUsers = kmeliaManager.getComponentUsers(componentId);
    request.setAttribute("listComponentUsers", listComponentUsers);
    
    List<String> selectedRecipients = new ArrayList<String>();
    for (UserDetail userDetail : listComponentUsers) {
      if(request.getParameter("recipient-"+userDetail.getId())!=null){
        selectedRecipients.add(userDetail.getId());
      }
    }
    
    if(!selectedRecipients.isEmpty()){
      String [] stringArray = new String[0];
      kmeliaManager.notifyUsers(userId, componentId, selectedRecipients.toArray(stringArray), attachmentId, publicationId, message, request);
    }
    
    request.setAttribute("publicationId", publicationId);
    request.setAttribute("userId", userId);
    request.setAttribute("componentId", componentId);
    request.setAttribute("attachmentId", attachmentId);
    request.setAttribute("spaceId", spaceId);
    
    return "notify.jsp";
  }

  private KmeliaManager getKmeliaManager(HttpServletRequest request)
      throws SilverpeasMobileException {
    WebBeanFactory beanFactory = new WebBeanFactory();
    OrganizationController organizationController = new OrganizationController();
    return new KmeliaManager(beanFactory.getAdminBm(), beanFactory.getKmeliaBm(),
        organizationController);
  }
  
}
