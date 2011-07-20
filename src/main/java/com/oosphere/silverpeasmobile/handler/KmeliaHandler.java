package com.oosphere.silverpeasmobile.handler;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.oosphere.silverpeasmobile.bean.WebBeanFactory;
import com.oosphere.silverpeasmobile.exception.SilverpeasMobileException;
import com.oosphere.silverpeasmobile.kmelia.KmeliaManager;
import com.oosphere.silverpeasmobile.vo.PublicationVO;
import com.stratelia.webactiv.beans.admin.OrganizationController;
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

  private KmeliaManager getKmeliaManager(HttpServletRequest request)
      throws SilverpeasMobileException {
    WebBeanFactory beanFactory = new WebBeanFactory();
    OrganizationController organizationController = new OrganizationController();
    return new KmeliaManager(beanFactory.getAdminBm(), beanFactory.getKmeliaBm(),
        organizationController);
  }

}
