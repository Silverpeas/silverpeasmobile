package com.silverpeas.mobile.shared.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.comments.CommentDTO;
import com.silverpeas.mobile.shared.dto.notifications.NotificationDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.CommentsException;
import com.silverpeas.mobile.shared.exceptions.NotificationsException;

import java.util.List;

@RemoteServiceRelativePath("Notifications")
public interface ServiceNotifications extends RemoteService {
  List<BaseDTO> getAllowedUsersAndGroups(String componentId, String contentId) throws NotificationsException, AuthenticationException;
  void send(NotificationDTO notification, List<BaseDTO> receivers) throws NotificationsException, AuthenticationException;
}
