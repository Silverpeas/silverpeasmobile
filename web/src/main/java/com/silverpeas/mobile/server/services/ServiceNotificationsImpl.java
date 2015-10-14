package com.silverpeas.mobile.server.services;

import com.silverpeas.comment.model.Comment;
import com.silverpeas.comment.model.CommentPK;
import com.silverpeas.comment.service.CommentServiceFactory;
import com.silverpeas.mobile.server.helpers.DataURLHelper;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.GroupDTO;
import com.silverpeas.mobile.shared.dto.UserDTO;
import com.silverpeas.mobile.shared.dto.comments.CommentDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.CommentsException;
import com.silverpeas.mobile.shared.exceptions.NotificationsException;
import com.silverpeas.mobile.shared.services.ServiceComments;
import com.silverpeas.mobile.shared.services.ServiceNotifications;
import com.stratelia.webactiv.util.publication.model.PublicationPK;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Service de gestion des Notifications.
 * @author svuillet
 */
public class ServiceNotificationsImpl extends AbstractAuthenticateService implements ServiceNotifications {

  private static final long serialVersionUID = 1L;


  @Override
  public List<BaseDTO> getAllowedUsersAndGroups(String contentId) throws NotificationsException, AuthenticationException {
    ArrayList<BaseDTO> usersAndGroups = new ArrayList<BaseDTO>();


    //TODO : replace with real code
    GroupDTO g = new GroupDTO();
    g.setId("1");
    g.setName("group test");

    UserDTO u = new UserDTO();
    u.setId("1");
    u.setLastName("Durant");
    u.setFirstName("Charles");

    usersAndGroups.add(u);
    usersAndGroups.add(g);

    return usersAndGroups;
  }
}
