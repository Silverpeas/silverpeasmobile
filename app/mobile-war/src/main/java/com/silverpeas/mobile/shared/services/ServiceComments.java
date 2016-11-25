package com.silverpeas.mobile.shared.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.silverpeas.mobile.shared.dto.comments.CommentDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.CommentsException;

import java.util.List;

@RemoteServiceRelativePath("Comments")
public interface ServiceComments extends RemoteService {
  public List<CommentDTO> getComments(String id, String type) throws CommentsException, AuthenticationException;
  public CommentDTO addComment(String id, String instanceId, String type, String message) throws CommentsException, AuthenticationException;
}
