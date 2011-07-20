/**
 * Copyright (C) 2000 - 2009 Silverpeas
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
 * "http://repository.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.oosphere.silverpeasmobile.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.oosphere.silverpeasmobile.bean.WebBeanFactory;
import com.oosphere.silverpeasmobile.exception.SilverpeasMobileException;
import com.oosphere.silverpeasmobile.kmelia.KmeliaManager;
import com.oosphere.silverpeasmobile.trace.SilverpeasMobileTrace;
import com.stratelia.webactiv.beans.admin.OrganizationController;
import com.stratelia.webactiv.util.DateUtil;
import com.stratelia.webactiv.util.node.model.NodeDetail;

public class JsonServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    doPost(req, res);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse res)
      throws ServletException, IOException {
    String action = request.getParameter("action");
    String id = request.getParameter("id");
    String componentId = request.getParameter("componentId");
    String language = request.getParameter("language");

    String userId = request.getParameter("userId");
    String userProfile = request.getParameter("userProfile");
    String spaceId = request.getParameter("spaceId");

    String result;
    try {
      List<NodeDetail> nodes = null;
      KmeliaManager kmeliaManager = getKmeliaManager(request);
      if ("topics".equals(action)) {
        nodes = kmeliaManager.getTopics(id, userId, userProfile, spaceId, componentId);
        Collections.sort(nodes, new NodeDetailComparator());
      } else if ("topic".equals(action)) {
        nodes = new ArrayList<NodeDetail>();
        // nodes.add(kmeliaManager.getTopic(id));
      } else if ("path".equals(action)) {
        // nodes = kmeliaManager.getPath(id);
      }
      result = getJsonList(nodes, language, kmeliaManager);
    } catch (SilverpeasMobileException e) {
      result = e.getMessage();
      SilverpeasMobileTrace.error(this, "doPost", result);
    }

    res.setContentType("application/json");
    res.getWriter().write(result);
  }

  private String getJsonList(List<NodeDetail> nodes, String language, KmeliaManager kmeliaManager) {
    JSONArray jsonArray = new JSONArray();
    for (NodeDetail node : nodes) {
      jsonArray.add(getJsonElement(node, language, kmeliaManager));
    }
    return jsonArray.toString();
  }

  private JSONObject getJsonElement(NodeDetail node, String language, KmeliaManager kmeliaManager) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("id", node.getNodePK().getId());
    jsonObject.put("name", node.getName(language));
    jsonObject.put("description", node.getDescription(language));
    try {
      jsonObject.put("date", DateUtil.getOutputDate(node.getCreationDate(), language));
    } catch (ParseException e) {
      jsonObject.put("date", "error");
    }
    jsonObject.put("creatorId", node.getCreatorId());
    // jsonObject.put("creatorName",
    // kmeliaManager.getUserDetail(node.getCreatorId()).getDisplayedName());
    // jsonObject.put("role", kmeliaManager.getUserProfile().getProfileName());
    jsonObject.put("nbObjects", node.getNbObjects());
    jsonObject.put("status", node.getStatus());
    jsonObject.put("level", node.getLevel());
    jsonObject.put("updateChain", false);
    return jsonObject;
  }

  private KmeliaManager getKmeliaManager(HttpServletRequest request)
      throws SilverpeasMobileException {
    WebBeanFactory beanFactory = new WebBeanFactory();
    OrganizationController organizationController = new OrganizationController();
    return new KmeliaManager(beanFactory.getAdminBm(), beanFactory.getKmeliaBm(),
        organizationController);
  }

  private class NodeDetailComparator implements Comparator<NodeDetail> {

    public int compare(NodeDetail node1, NodeDetail node2) {
      return node1.getName().toLowerCase().compareTo(node2.getName().toLowerCase());
    }

  }

}
