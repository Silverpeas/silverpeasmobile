/*
 * Copyright (C) 2000 - 2026 Silverpeas
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

import elemental2.core.Global;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.client.common.network.rest.RestCallback;

import org.silverpeas.mobile.shared.dto.comments.CommentDTO;

import java.util.List;

/**
 * @author svu
 */

public class ServiceComment extends AbstractService {

  public final static String PATH = "/silverpeas/services/comments/";
  public void saveNewComment(String componentId, String contentType, String contentId,
                             CommentDTO commentToSave, RestCallback<CommentDTO> callback) {
    post(PATH + encode(componentId) + "/" + encode(contentType) + "/" + encode(contentId),
            Global.JSON.stringify(Js.asAny(commentToSave.toJSON())),
            result -> CommentDTO.fromJSON(
                    (JsPropertyMap<Object>) result
            ),
            callback
    );
  }

  public void getAllComments(String componentId, String contentType, String contentId,
                             RestCallback<List<CommentDTO>> callback) {
    get(PATH + encode(componentId) + "/" + encode(contentType) + "/" + encode(contentId),
            this::mapComments,
            callback);
  }

  private List<CommentDTO> mapComments(Object result) {
    return mapArray(result, CommentDTO::fromJSON);
  }
  public void deleteComment(String componentId, String contentType, String contentId, String commentId,
                            RestCallback<Void> callback) {
    delete(
            PATH + encode(componentId) + "/" + encode(contentType) + "/" + encode(contentId) + "/" + encode(commentId),
            null,
            text -> null,
            callback
    );

  }
  public void updateComment(String componentId, String contentType, String contentId, String commentId,
                            CommentDTO comment, RestCallback<CommentDTO> callback) {

    put(PATH + encode(componentId) + "/" + encode(contentType) + "/" + encode(contentId) + "/" + encode(commentId),
            Global.JSON.stringify(Js.asAny(comment.toJSON())),
            result -> CommentDTO.fromJSON(
                    (JsPropertyMap<Object>) result),
            callback);
  }
}
