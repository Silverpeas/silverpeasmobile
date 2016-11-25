package com.silverpeas.mobile.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.shared.dto.comments.CommentDTO;

import java.util.List;

public interface ServiceCommentsAsync {
  void getComments(String id, String type, final AsyncCallback<List<CommentDTO>> async);

  void addComment(String id, String instanceId, String type, String message,
      final AsyncCallback<CommentDTO> async);
}
