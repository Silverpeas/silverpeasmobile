/*
 * Copyright (C) 2000 - 2025 Silverpeas
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

import org.silverpeas.components.kmelia.model.TopicDetail;
import org.silverpeas.components.kmelia.service.KmeliaService;
import org.silverpeas.core.admin.ProfiledObjectId;
import org.silverpeas.core.admin.component.model.ComponentInst;
import org.silverpeas.core.admin.service.AdminException;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.user.model.*;
import org.silverpeas.core.annotation.WebService;
import org.silverpeas.core.contribution.publication.model.PublicationPK;
import org.silverpeas.core.node.model.NodeDetail;
import org.silverpeas.core.node.model.NodePK;
import org.silverpeas.core.node.service.NodeService;
import org.silverpeas.core.notification.NotificationException;
import org.silverpeas.core.notification.user.client.GroupRecipient;
import org.silverpeas.core.notification.user.client.NotificationMetaData;
import org.silverpeas.core.notification.user.client.NotificationSender;
import org.silverpeas.core.notification.user.client.UserRecipient;
import org.silverpeas.core.notification.user.client.model.SentNotificationDetail;
import org.silverpeas.core.notification.user.client.model.SentNotificationInterface;
import org.silverpeas.core.notification.user.server.channel.silvermail.SILVERMAILMessage;
import org.silverpeas.core.notification.user.server.channel.silvermail.SILVERMAILPersistence;
import org.silverpeas.core.notification.user.server.channel.silvermail.SilvermailCriteria;
import org.silverpeas.core.web.rs.UserPrivilegeValidation;
import org.silverpeas.core.web.rs.annotation.Authorized;
import org.silverpeas.kernel.logging.SilverLogger;
import org.silverpeas.kernel.util.StringUtil;
import org.silverpeas.mobile.server.services.helpers.UserHelper;
import org.silverpeas.mobile.shared.StreamingList;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.GroupDTO;
import org.silverpeas.mobile.shared.dto.UserDTO;
import org.silverpeas.mobile.shared.dto.notifications.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Service de gestion des Notifications.
 *
 * @author svuillet
 */
@WebService
@Authorized
@Path(ServiceNotifications.PATH)
public class ServiceNotifications extends AbstractRestWebService {

  @Inject
  private OrganizationController organizationController;
  static final String PATH = "mobile/notification";

  @Context
  HttpServletRequest request;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("sended/{callNumber}")
  public StreamingList<NotificationSendedDTO> getUserSendedNotifications(
      @PathParam("callNumber") int callNumber) {

    //noinspection unchecked
    return (StreamingList<NotificationSendedDTO>) makeStreamingList(callNumber,
        "Cache_userNotificationsSended", request, new Populator() {
      @Override
      public List<?> execute() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<NotificationSendedDTO> list = new ArrayList<>();
        try {
          List<SentNotificationDetail> notifications =
              SentNotificationInterface.get().getAllNotifByUser(getUser().getId());
          for (SentNotificationDetail notif : notifications) {
            NotificationSendedDTO dto = new NotificationSendedDTO();
            dto.setSource(notif.getBody());
            dto.setDate(sdf.format(notif.getNotifDate()));
            dto.setLink(notif.getLink());
            dto.setTitle(notif.getTitle());
            dto.setIdNotif(notif.getNotifId());
            list.add(dto);
          }
        } catch (NotificationException e) {
          SilverLogger.getLogger(this).error(e);
        }
        return list;
      }
    });

  }


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("received/{callNumber}")
  public StreamingList<NotificationReceivedDTO> getUserNotifications(
      @PathParam("callNumber") int callNumber) {

    //noinspection unchecked
    return (StreamingList<NotificationReceivedDTO>) makeStreamingList(callNumber,
        "Cache_userNotificationsReceived", request, () -> {
      List<NotificationReceivedDTO> list = new ArrayList<>();
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      Collection<SILVERMAILMessage> messages =
          SILVERMAILPersistence.getMessageOfFolder(getUser().getId(), "INBOX", null,
              SilvermailCriteria.QUERY_ORDER_BY.RECEPTION_DATE_ASC);
      for (SILVERMAILMessage message : messages) {
        NotificationReceivedDTO dto = new NotificationReceivedDTO();
        dto.setSource(message.getSource());
        dto.setAuthor(message.getSenderName());
        dto.setDate(sdf.format(message.getDate()));
        dto.setLink(message.getUrl());
        dto.setReaden(message.getReaden());
        dto.setTitle(message.getSubject());
        dto.setIdNotif(message.getId());
        list.add(dto);
      }
      Collections.reverse(list);
      return list;
    });
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("allowedUsersAndGroups/{componentId}/{contentId}")
  public List<BaseDTO> getAllowedUsersAndGroups(@PathParam("componentId") String componentId,
      @PathParam("contentId") String contentId) throws Exception {
    ArrayList<BaseDTO> usersAndGroups = new ArrayList<>();
    ArrayList<UserDTO> users = new ArrayList<>();
    ArrayList<GroupDTO> groups = new ArrayList<>();

    try {
      if (componentId.equalsIgnoreCase("null")) {
        for (UserDetail user : Administration.get().getAllUsers()) {
          users.add(populate(user));
        }
        for (GroupDetail group : Administration.get().getAllGroups()) {
          GroupDTO g = UserHelper.getInstance().populateGroupDTO(group);
          groups.add(g);
        }
      } else {
        ComponentInst componentInst = Administration.get().getComponentInst(componentId);
        List<ProfileInst> profiles = new ArrayList<>();

        if (componentId.toLowerCase().startsWith("kmelia") && isRightsOnTopicsEnabled(componentId)) {
          TopicDetail topic = getKmeliaService().getBestTopicDetailOfPublicationForUser(
              new PublicationPK(contentId, componentId), true, getUser().getId());
          if (topic != null) {
            NodePK pk = topic.getNodePK();
            NodeDetail node = getNodeService().getDetail(pk);
            if (node.haveRights()) {
              if (node.haveLocalRights()) {
                profiles.addAll(getTopicProfiles(pk.getId(), componentId));
              } else if (node.haveInheritedRights()) {
                profiles.addAll(
                    getTopicProfiles(String.valueOf(node.getRightsDependsOn()), componentId));
              }

            } else {
              profiles.addAll(componentInst.getInheritedProfiles());
              profiles.addAll(componentInst.getProfiles());
            }
          } else {
            profiles.addAll(componentInst.getInheritedProfiles());
            profiles.addAll(componentInst.getProfiles());
          }
        } else {
          profiles.addAll(componentInst.getInheritedProfiles());
          profiles.addAll(componentInst.getProfiles());
        }

        for (ProfileInst profile : profiles) {
          for (String groupId : profile.getAllGroups()) {
            if (!isGroupPresent(groups, groupId)) {
              Group group = organizationController.getGroup(groupId);
              GroupDTO g = UserHelper.getInstance().populateGroupDTO(group);
              groups.add(g);
            }
          }
        }

        for (ProfileInst profile : profiles) {
          for (String userId : profile.getAllUsers()) {
            if (!isUserPresent(users, userId)) {
              UserDTO u = populate(userId);
              users.add(u);
            }
          }
          for (String groupId : profile.getAllGroups()) {
            Group group = organizationController.getGroup(groupId);
            for (User user : group.getAllUsers()) {
              if (!isUserPresent(users, user.getId())) {
                UserDTO u = populate(user.getId());
                users.add(u);
              }
            }
          }
        }
      }
    } catch (Exception e) {
      SilverLogger.getLogger(this).error(e);
      throw e;
    }

    users.sort(Comparator.comparing(UserDTO::getLastName, String.CASE_INSENSITIVE_ORDER));
    groups.sort(Comparator.comparing(GroupDTO::getName, String.CASE_INSENSITIVE_ORDER));
    usersAndGroups.addAll(groups);
    usersAndGroups.addAll(users);
    return usersAndGroups;
  }

  private boolean isGroupPresent(List<GroupDTO> groups, String id) {
    for (GroupDTO dto : groups) {
      if (dto.getId().equals(id)) {
        return true;
      }
    }
    return false;
  }

  private boolean isUserPresent(List<UserDTO> users, String id) {
    for (UserDTO dto : users) {
      if (dto.getId().equals(id)) {
        return true;
      }
    }
    return false;
  }

  private UserDTO populate(final String userId) {
    UserDetail userDetail = organizationController.getUserDetail(userId);
    return populate(userDetail);
  }

  private UserDTO populate(UserDetail userDetail) {
    return UserHelper.getInstance().populateUserDTO(userDetail);
  }

  private List<ProfileInst> getTopicProfiles(String topicId, String componentId)
      throws AdminException {
    List<ProfileInst> alShowProfile = new ArrayList<>();
    String[] asAvailProfileNames = Administration.get().getAllProfilesNames("kmelia");
    for (String asAvailProfileName : asAvailProfileNames) {
      ProfileInst profile = getTopicProfile(asAvailProfileName, topicId, componentId);
      profile.setLabel(Administration.get().getProfileLabelFromName("kmelia", asAvailProfileName,
          getUser().getUserPreferences().getLanguage()));
      alShowProfile.add(profile);
    }

    return alShowProfile;
  }

  private ProfileInst getTopicProfile(String role, String topicId, String componentId)
      throws AdminException {
    NodePK f = new NodePK(topicId, componentId);
    NodeDetail node = NodeService.get().getHeader(f, false);
    ProfiledObjectId profiledObjectId = ProfiledObjectId.fromNode(node.getRightsDependsOn());
    List<ProfileInst> profiles =
        Administration.get().getProfilesByObject(profiledObjectId, componentId);
    for (int p = 0; profiles != null && p < profiles.size(); p++) {
      ProfileInst profile = profiles.get(p);
      if (profile.getName().equals(role)) {
        return profile;
      }
    }
    ProfileInst profile = new ProfileInst();
    profile.setName(role);
    return profile;
  }

  private boolean isRightsOnTopicsEnabled(String instanceId) {
    String value =
        getMainSessionController().getComponentParameterValue(instanceId, "rightsOnTopics");
    return StringUtil.getBooleanValue(value);
  }

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("readed/{id}")
  public void markAsReaden(@PathParam("id") long id) {
    ArrayList<String> ids = new ArrayList<>();
    ids.add(String.valueOf(id));
    SILVERMAILPersistence.markMessagesAsRead(getUser().getId(), ids);
  }

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("readed/")
  public void markAsRead(List<NotificationBoxDTO> selection) {
    SILVERMAILPersistence.markMessagesAsRead(getUser().getId(), getSelectionIds(selection));
  }

  private List<String> getSelectionIds(List<NotificationBoxDTO> selection) {
    ArrayList<String> ids = new ArrayList<>();
    for (NotificationBoxDTO dto : selection) {
      ids.add(String.valueOf(dto.getIdNotif()));
    }
    return ids;
  }

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("")
  public void delete(List<NotificationBoxDTO> selection) {
    if (selection.get(0).isSended()) {
      for (NotificationBoxDTO dto : selection) {
        try {
          SentNotificationInterface.get().deleteNotif(getUser().getId(), (int) dto.getIdNotif());
        } catch (NotificationException e) {
          SilverLogger.getLogger(this).error(e);
        }
      }
    } else {
      SILVERMAILPersistence.deleteMessages(getUser().getId(), getSelectionIds(selection));
    }
  }

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("send/")
  public void send(NotificationToSendDTO notificationToSendDTO) throws AdminException,
      NotificationException {
    NotificationSender notificationSender =
        new NotificationSender(notificationToSendDTO.getNotification().getInstanceId());
    NotificationMetaData metaData = new NotificationMetaData();

    for (BaseDTO receiver : notificationToSendDTO.getReceivers()) {
      if (receiver instanceof UserDTO) {
        metaData.addUserRecipient(new UserRecipient((receiver).getId()));
      } else if (receiver instanceof GroupDTO) {
        metaData.addGroupRecipient(new GroupRecipient((receiver).getId()));
      }
    }

    metaData.setSendImmediately(true);
    UserDetail u = Administration.get().getUserDetail(getUser().getId());
    String silverpeasServerUrl = u.getDomain().getSilverpeasServerURL();
    if (!silverpeasServerUrl.contains("/silverpeas")) {
      silverpeasServerUrl = silverpeasServerUrl + "/silverpeas";
    }

    switch (notificationToSendDTO.getNotification().getContentType()) {
      case NotificationDTO.TYPE_PUBLICATION: {
        String url = silverpeasServerUrl + "/Publication/" +
            notificationToSendDTO.getNotification().getContentId();
        metaData.setLink(url);
        break;
      }
      case NotificationDTO.TYPE_DOCUMENT: {
        String url = silverpeasServerUrl + "/services/media/viewer/embed/pdf?documentId=" +
            notificationToSendDTO.getNotification().getContentId() + "&documentType=attachment" +
            "&embedPlayer=true";
        metaData.setLink(url);
        break;
      }
      case NotificationDTO.TYPE_PHOTO:
      case NotificationDTO.TYPE_SOUND:
      case NotificationDTO.TYPE_VIDEO:
      case NotificationDTO.TYPE_STREAMING: {
        String url = silverpeasServerUrl + "/autoRedirect.jsp?goto=%2FRgallery%2F" +
            notificationToSendDTO.getNotification().getInstanceId() + "%2FsearchResult%3FType%3D" +
            notificationToSendDTO.getNotification().getContentType() + "%26Id%3D" +
            notificationToSendDTO.getNotification().getContentId();
        metaData.setLink(url);
        break;
      }
      case NotificationDTO.TYPE_EVENT: {
        String url = silverpeasServerUrl + "/Contribution/" +
            notificationToSendDTO.getNotification().getContentId();
        metaData.setLink(url);
        break;
      }
      default:
        break;
    }
    metaData.setAnswerAllowed(false);
    metaData.setContent(notificationToSendDTO.getNotification().getMessage());
    metaData.setSender(getUser().getEmailAddress());

    metaData.setTitle(notificationToSendDTO.getSubject());
    notificationSender.notifyUser(metaData);
  }

  private KmeliaService getKmeliaService() {
    return KmeliaService.get();
  }

  private NodeService getNodeService() {
    return NodeService.get();
  }

  @Override
  protected String getResourceBasePath() {
    return PATH;
  }

  @Override
  public String getComponentId() {
    return null;
  }

  @Override
  public void validateUserAuthorization(final UserPrivilegeValidation validation) {
    // no authorization
  }
}
