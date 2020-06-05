/*
 * Copyright (C) 2000 - 2020 Silverpeas
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

package org.silverpeas.mobile.server.services;

import org.silverpeas.components.questionreply.model.Category;
import org.silverpeas.components.questionreply.model.Question;
import org.silverpeas.components.questionreply.model.Reply;
import org.silverpeas.components.questionreply.service.QuestionManagerProvider;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.annotation.RequestScoped;
import org.silverpeas.core.annotation.Service;
import org.silverpeas.core.node.model.NodeDetail;
import org.silverpeas.core.node.model.NodePK;
import org.silverpeas.core.node.service.NodeService;
import org.silverpeas.core.util.logging.SilverLogger;
import org.silverpeas.core.webapi.base.RESTWebService;
import org.silverpeas.core.webapi.base.annotation.Authorized;
import org.silverpeas.mobile.shared.dto.faq.CategoryDTO;
import org.silverpeas.mobile.shared.dto.faq.QuestionDTO;
import org.silverpeas.mobile.shared.dto.faq.ReplyDTO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*
 * Copyright (C) 2000 - 2020 Silverpeas
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
@Service
@RequestScoped
@Authorized
@Path(ServiceFaq.PATH + "/{appId}")
public class ServiceFaq extends RESTWebService {

  @Context
  HttpServletRequest request;

  static final String PATH = "faq";

  @PathParam("appId")
  private String componentId;

  @GET
  @Path("question/all")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public List<QuestionDTO> getAllQuestions() {
    List<QuestionDTO> dtos = new ArrayList<>();
    try {
      List<Question> questions = QuestionManagerProvider.getQuestionManager().getAllQuestions(getComponentId());
      for (Question q : questions) {
        List<ReplyDTO> dtoReplies = new ArrayList<>();
        QuestionDTO dto = new QuestionDTO();
        dto.setTitle(q.getTitle());
        dto.setCategory(getCategory(q.getCategoryId()));

        for (Reply r : q.readReplies()) {
          ReplyDTO dt = new ReplyDTO();
          dt.setId(r.getPK().getId());
          dt.setTitle(r.getTitle());
          dt.setContent(r.loadWysiwygContent());
          dtoReplies.add(dt);
        }
        dto.setReplies(dtoReplies);
        dtos.add(dto);
      }
    } catch(Exception e) {
      SilverLogger.getLogger(this).error(e);
    }

    return dtos;
  }

  private CategoryDTO getCategory(String categoryId) throws Exception {
    NodePK nodePK = new NodePK(categoryId, getComponentId());
    Category c = new Category(NodeService.get().getDetail(nodePK));
    CategoryDTO dto = new CategoryDTO();
    dto.setId(String.valueOf(c.getId()));
    dto.setTitle(c.getName(getUser().getUserPreferences().getLanguage()));
    dto.setDescription(c.getDescription(getUser().getUserPreferences().getLanguage()));
    return dto;
  }

  @GET
  @Path("category/all")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public List<CategoryDTO> getAllCategories() {
    List<CategoryDTO> dtos = new ArrayList<>();
    try {
      NodePK nodePK = new NodePK(NodePK.ROOT_NODE_ID, getComponentId());
      Collection<NodeDetail> cats = NodeService.get().getChildrenDetails(nodePK);
      for (NodeDetail cat : cats) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(String.valueOf(cat.getId()));
        dto.setTitle(cat.getName(getUser().getUserPreferences().getLanguage()));
        dto.setDescription(cat.getDescription(getUser().getUserPreferences().getLanguage()));
        dtos.add(dto);
      }
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
    }

    return dtos;
  }

  @Override
  protected String getResourceBasePath() {
    return PATH;
  }

  @Override
  public String getComponentId() {
    return this.componentId;
  }

  public String getComponentParameterValue(String parameterName) {
    return OrganizationController.get().getComponentParameterValue(getComponentId(), parameterName);
  }
}
