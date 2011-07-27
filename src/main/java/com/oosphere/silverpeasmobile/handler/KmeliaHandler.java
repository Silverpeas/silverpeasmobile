package com.oosphere.silverpeasmobile.handler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.oosphere.silverpeasmobile.bean.WebBeanFactory;
import com.oosphere.silverpeasmobile.exception.SilverpeasMobileException;
import com.oosphere.silverpeasmobile.kmelia.KmeliaManager;
import com.oosphere.silverpeasmobile.vo.PublicationVO;
import com.silverpeas.external.filesharing.model.FileSharingService;
import com.silverpeas.external.filesharing.model.FileSharingServiceFactory;
import com.silverpeas.external.filesharing.model.TicketDetail;
import com.silverpeas.util.StringUtil;
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
    String id = request.getParameter("pubId");
    String spaceId = request.getParameter("spaceId");
    String componentId = request.getParameter("componentId");
    String userId = request.getParameter("userId");
    String backLabel = request.getParameter("backLabel");

    PublicationVO publi = kmeliaManager.getPublication(id);

    request.setAttribute("publication", publi);
    request.setAttribute("userId", userId);
    request.setAttribute("spaceId", spaceId);
    request.setAttribute("componentId", componentId);
    request.setAttribute("backLabel", backLabel);

    return "publication.jsp";
  }
  
  private String documentAction(HttpServletRequest request, KmeliaManager kmeliaManager)
      throws SilverpeasMobileException {
    String attachmentId = request.getParameter("attachmentId");
    String componentId = request.getParameter("componentId");
    String userId = request.getParameter("userId");
    
    boolean isComponentWithFileSharing = kmeliaManager.isAttachmentSharable(attachmentId);

    request.setAttribute("fileSharingActive", isComponentWithFileSharing);
    request.setAttribute("userId", userId);
    request.setAttribute("componentId", componentId);
    request.setAttribute("attachmentId", attachmentId);

    return "documentAction.jsp";
  }
  
  private String shareAttachment(HttpServletRequest request, KmeliaManager kmeliaManager)
      throws SilverpeasMobileException {
    
    String componentId = request.getParameter("componentId");
    String attachmentId = request.getParameter("attachmentId");
    String userId = request.getParameter("userId");
    
    Calendar endCalendar = Calendar.getInstance();
    endCalendar.add(Calendar.DAY_OF_MONTH, 1);
    Date endDate = endCalendar.getTime();
    
    UserDetail user = getCurrentUser(request);
    
    TicketDetail newTicket = TicketDetail.aTicket(Integer.parseInt(attachmentId), componentId,
        false, user, new Date(), endDate, 1);
    FileSharingService fileSharingService = FileSharingServiceFactory.getFactory().getFileSharingService();
    String shareKey = fileSharingService.createTicket(newTicket);
    newTicket.setKeyFile(shareKey);
    
    request.setAttribute("urlAttachment", newTicket.getUrl(request));
    
    return "share.jsp";
    
  }

  private KmeliaManager getKmeliaManager(HttpServletRequest request)
      throws SilverpeasMobileException {
    WebBeanFactory beanFactory = new WebBeanFactory();
    OrganizationController organizationController = new OrganizationController();
    return new KmeliaManager(beanFactory.getAdminBm(), beanFactory.getKmeliaBm(),
        organizationController);
  }
  
  private UserDetail getCurrentUser(HttpServletRequest request) throws SilverpeasMobileException {
    UserDetail user = null ;
    String userId = request.getParameter("userId");
    if(StringUtil.isDefined(userId)){
      OrganizationController organizationController = new OrganizationController();
      user = organizationController.getUserDetail(userId);
    } else{
      throw new SilverpeasMobileException(this, "getCurrentUser", "Parameter userId not present in request");
    }
    return user;
  }

}
