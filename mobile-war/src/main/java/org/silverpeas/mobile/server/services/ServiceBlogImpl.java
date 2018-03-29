/*
 * Copyright (C) 2000 - 2017 Silverpeas
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
 *
 */

package org.silverpeas.mobile.server.services;

import org.silverpeas.components.blog.model.PostDetail;
import org.silverpeas.components.blog.service.BlogServiceFactory;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.mobile.shared.dto.blog.PostDTO;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.silverpeas.mobile.shared.exceptions.BlogException;
import org.silverpeas.mobile.shared.services.ServiceBlog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Service de gestion des news.
 * @author svu
 */
public class ServiceBlogImpl extends AbstractAuthenticateService implements ServiceBlog {

  private static final long serialVersionUID = 1L;
  private SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
  private OrganizationController organizationController = OrganizationController.get();

  @Override
  public List<PostDTO> getPosts(String instanceId, String categoryId) throws BlogException, AuthenticationException {
    checkUserInSession();

    List<PostDTO> postsDTO = null;
    Collection<PostDetail> posts = null;

    if (categoryId.equalsIgnoreCase("all")) {
      posts = BlogServiceFactory.getBlogService().getAllPosts(instanceId);
    } else {
      posts = BlogServiceFactory.getBlogService().getPostsByCategory(categoryId, instanceId);
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
      dto.setCategoryId(String.valueOf(post.getCategory().getId()));
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
}