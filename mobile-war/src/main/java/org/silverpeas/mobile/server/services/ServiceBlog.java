/*
 * Copyright (C) 2000 - 2021 Silverpeas
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

package org.silverpeas.mobile.server.services;

import org.silverpeas.components.blog.model.PostDetail;
import org.silverpeas.components.blog.service.BlogServiceFactory;
import org.silverpeas.core.annotation.WebService;
import org.silverpeas.core.web.rs.RESTWebService;
import org.silverpeas.core.web.rs.annotation.Authorized;
import org.silverpeas.mobile.shared.dto.blog.PostDTO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@WebService
@Authorized
@Path(ServiceBlog.PATH + "/{appId}")
public class ServiceBlog extends RESTWebService {

  @Context
  HttpServletRequest request;

  @PathParam("appId")
  private String componentId;

  private SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

  static final String PATH = "blog";

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("category/{categoryId}")
  public List<PostDTO> getPosts(@PathParam("categoryId") String categoryId) {
    List<PostDTO> postsDTO = null;
    Collection<PostDetail> posts = null;

    if (categoryId.equalsIgnoreCase("all")) {
      posts = BlogServiceFactory.getBlogService().getAllPosts(componentId);
    } else {
      posts = BlogServiceFactory.getBlogService().getPostsByCategory(categoryId, componentId);
    }

    postsDTO = populate(posts);

    return postsDTO;
  }

  private PostDTO populate(PostDetail post) {
    PostDTO dto = new PostDTO();
    dto.setId(post.getId());
    dto.setTitle(post.getTitle());
    dto.setDescription(post.getDescription());
    if (post.getCategory() != null) {
      dto.setCategoryName(post.getCategory().getName());
      dto.setCategoryId(post.getCategory().getId());
    } else {
      dto.setCategoryId("");
      dto.setCategoryName("");
    }
    dto.setCreatorName(post.getCreatorName());
    dto.setCreationDate(sdf.format(post.getCreationDate()));
    dto.setDateEvent(sdf.format(post.getDateEvent()));
    dto.setInstanceId(post.getComponentInstanceId());
    return dto;
  }

  private List<PostDTO> populate(Collection<PostDetail> posts) {
    List<PostDTO> dtos = new ArrayList<PostDTO>();
    if (posts != null) {
      for (PostDetail post : posts) {
        PostDTO dto = populate(post);
        dtos.add(dto);
      }
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

}
