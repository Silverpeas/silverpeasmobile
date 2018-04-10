/*
 * Copyright (C) 2000 - 2018 Silverpeas
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

import org.silverpeas.core.comment.model.Comment;
import org.silverpeas.core.comment.model.CommentPK;
import org.silverpeas.core.comment.service.CommentServiceProvider;
import org.silverpeas.core.contribution.publication.model.PublicationPK;
import org.silverpeas.mobile.server.helpers.DataURLHelper;
import org.silverpeas.mobile.shared.dto.comments.CommentDTO;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.silverpeas.mobile.shared.exceptions.CommentsException;
import org.silverpeas.mobile.shared.services.ServiceComments;

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
    ArrayList<CommentDTO> list = new ArrayList<>();
    List<Comment> comments = CommentServiceProvider.getCommentService().getAllCommentsOnPublication(
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
    String avatar = DataURLHelper.convertAvatarToUrlData(c.getOwnerDetail().getSmallAvatar(), getSettings().getString("avatar.size", "24x"));
    dto.setAvatar(avatar);
    dto.setDate(sdf.format(c.getCreationDate()));
    return dto;
  }

  @Override
  public CommentDTO addComment(String id, String instanceId, String type, String message) throws CommentsException, AuthenticationException {
    checkUserInSession();
    CommentDTO comment;
    Date now = new Date();
    Comment c = new Comment(new CommentPK("", instanceId), type, new PublicationPK(
        id, instanceId), Integer.valueOf(getUserInSession().getId()), getUserInSession().getDisplayedName(),
        message,
        now, now);
    c.setOwnerDetail(getUserInSession());

    CommentServiceProvider.getCommentService().createComment(c);
    comment = populate(c);

    return comment;
  }
}
