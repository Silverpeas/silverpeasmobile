/*
 * Copyright (C) 2000 - 2024 Silverpeas
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
 * "https://www.silverpeas.org/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.shared.services.rest;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import org.silverpeas.mobile.shared.dto.faq.CategoryDTO;
import org.silverpeas.mobile.shared.dto.faq.QuestionDTO;
import org.silverpeas.mobile.shared.dto.faq.QuestionDetailDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author svu
 */
@Path("/mobile/faq")
public interface ServiceFaq extends RestService {

  @GET
  @Path("{appId}/question/all")
  public void getAllQuestions(@PathParam("appId") String appId,
      MethodCallback<List<QuestionDTO>> callback);

  @GET
  @Path("{appId}/category/all")
  public void getAllCategories(@PathParam("appId") String appId,
      MethodCallback<List<CategoryDTO>> callback);

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("{appId}/question")
  public void createQuestion(@PathParam("appId") String appId, QuestionDetailDTO question, MethodCallback<QuestionDetailDTO> callback);
}
