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

import org.silverpeas.components.kmelia.model.TopicDetail;
import org.silverpeas.components.kmelia.service.KmeliaService;
import org.silverpeas.core.admin.ObjectType;
import org.silverpeas.core.admin.component.model.ComponentInst;
import org.silverpeas.core.admin.service.AdminException;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.space.SpaceInst;
import org.silverpeas.core.admin.user.model.Group;
import org.silverpeas.core.admin.user.model.ProfileInst;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.contribution.publication.model.PublicationPK;
import org.silverpeas.core.contribution.publication.service.PublicationService;
import org.silverpeas.core.node.model.NodeDetail;
import org.silverpeas.core.node.model.NodePK;
import org.silverpeas.core.node.service.NodeService;
import org.silverpeas.core.notification.user.client.GroupRecipient;
import org.silverpeas.core.notification.user.client.NotificationMetaData;
import org.silverpeas.core.notification.user.client.NotificationSender;
import org.silverpeas.core.notification.user.client.UserRecipient;
import org.silverpeas.core.util.LocalizationBundle;
import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.SettingBundle;
import org.silverpeas.core.util.StringUtil;
import org.silverpeas.mobile.server.helpers.DataURLHelper;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.GroupDTO;
import org.silverpeas.mobile.shared.dto.UserDTO;
import org.silverpeas.mobile.shared.dto.notifications.NotificationDTO;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.silverpeas.mobile.shared.exceptions.NotificationsException;
import org.silverpeas.mobile.shared.services.ServiceNotifications;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Service de gestion des Notifications.
 *
 * @author svuillet
 */
public class ServiceNotificationsImpl extends AbstractAuthenticateService implements ServiceNotifications {

    private static final long serialVersionUID = 1L;
    private OrganizationController organizationController = OrganizationController.get();

    @Override
    public List<BaseDTO> getAllowedUsersAndGroups(String componentId, String contentId) throws NotificationsException, AuthenticationException {
        ArrayList<BaseDTO> usersAndGroups = new ArrayList<BaseDTO>();

        try {
            ComponentInst componentInst = Administration.get().getComponentInst(componentId);
            List<ProfileInst> profiles = new ArrayList<ProfileInst>();

                if (componentId.toLowerCase().startsWith("kmelia") && isRightsOnTopicsEnabled(componentId)) {
                    TopicDetail topic = getKmeliaService().getPublicationFather(new PublicationPK(contentId, componentId), true, getUserInSession().getId(), true);
                    if (topic != null){
                        NodePK pk = topic.getNodePK();
                        NodeDetail node = getNodeService().getDetail(pk);
                        if (node.haveRights()) {
                            if (node.haveLocalRights()) {
                                profiles.addAll(getTopicProfiles(pk.getId(), componentId));
                            } else if (node.haveInheritedRights()) {
                                profiles.addAll(getTopicProfiles(String.valueOf(node.getRightsDependsOn()), componentId));
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
                for (String userId : profile.getAllUsers()) {
                    UserDetail userDetail = organizationController.getUserDetail(userId);
                    UserDTO u = new UserDTO();
                    u.setId(userDetail.getId());
                    u.setFirstName(userDetail.getFirstName());
                    u.setLastName(userDetail.getLastName());
                    u.seteMail(userDetail.geteMail());
                  String avatar = DataURLHelper
                      .convertAvatarToUrlData(userDetail.getAvatarFileName(), getSettings().getString("avatar.size", "24x"));
                    u.setAvatar(avatar);
                    usersAndGroups.add(u);
                }
                for (String groupId : profile.getAllGroups()) {
                    Group group = organizationController.getGroup(groupId);
                    GroupDTO g = new GroupDTO();
                    g.setId(group.getId());
                    g.setName(group.getName());
                    usersAndGroups.add(g);
                }
            }
        } catch (Exception e) {
            throw new NotificationsException(e);
        }
        return usersAndGroups;
    }

    private List<ProfileInst> getTopicProfiles(String topicId, String componentId)  throws AdminException {
        List<ProfileInst> alShowProfile = new ArrayList<ProfileInst>();
        String[] asAvailProfileNames = Administration.get().getAllProfilesNames("kmelia");
        for (String asAvailProfileName : asAvailProfileNames) {
            ProfileInst profile = getTopicProfile(asAvailProfileName, topicId, componentId);
            profile.setLabel(Administration.get().getProfileLabelfromName("kmelia", asAvailProfileName,
                    getUserInSession().getUserPreferences().getLanguage()));
            alShowProfile.add(profile);
        }

        return alShowProfile;
    }

    private ProfileInst getTopicProfile(String role, String topicId, String componentId) throws AdminException {
        List<ProfileInst> profiles = Administration.get().getProfilesByObject(topicId, ObjectType.NODE.getCode(), componentId);
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

    private boolean isRightsOnTopicsEnabled(String instanceId) throws Exception {
        String value = getMainSessionController().getComponentParameterValue(instanceId, "rightsOnTopics");
        return StringUtil.getBooleanValue(value);
    }

    @Override
    public void send(NotificationDTO notification, List<BaseDTO> receivers, String subject) throws NotificationsException, AuthenticationException {
        try {
            LocalizationBundle resource = ResourceLocator.getLocalizationBundle("org.silverpeas.mobile.multilang.mobileBundle", getUserInSession().getUserPreferences().getLanguage());
            NotificationSender notificationSender = new NotificationSender(notification.getInstanceId());
            NotificationMetaData metaData = new NotificationMetaData();

            for (BaseDTO receiver : receivers) {
                if (receiver instanceof UserDTO) {
                    metaData.addUserRecipient(new UserRecipient((receiver).getId()));
                } else if (receiver instanceof GroupDTO) {
                    metaData.addGroupRecipient(new GroupRecipient((receiver).getId()));
                }
            }

            metaData.setSendImmediately(true);

            String silverpeasServerUrl = getUserInSession().getDomain().getSilverpeasServerURL();
            if (!silverpeasServerUrl.contains("silverpeas")) {
              silverpeasServerUrl = silverpeasServerUrl + "/silverpeas";
            }

            if (notification.getContentType().equals(NotificationDTO.TYPE_PUBLICATION)) {
                String url = silverpeasServerUrl + "/Publication/" + notification.getContentId();
                metaData.setLink(url);
            } else if (notification.getContentType().equals(NotificationDTO.TYPE_PHOTO) || notification.getContentType().equals(NotificationDTO.TYPE_SOUND) || notification.getContentType().equals(NotificationDTO.TYPE_VIDEO) || notification.getContentType().equals(NotificationDTO.TYPE_STREAMING)) {
                String url = silverpeasServerUrl + "/Media/" + notification.getContentId();
                metaData.setLink(url);
            } else if (notification.getContentType().equals(NotificationDTO.TYPE_EVENT)) {
              String url = silverpeasServerUrl + "/Contribution/" + notification.getContentId();
              metaData.setLink(url);
            }
            metaData.setAnswerAllowed(false);
            metaData.setContent(notification.getMessage());
            metaData.setSender(getUserInSession().geteMail());

            ComponentInst app = Administration.get().getComponentInst(notification.getInstanceId());
            SpaceInst space = Administration.get().getSpaceInstById(app.getDomainFatherId());
            metaData.setTitle(subject);
            notificationSender.notifyUser(metaData);
        } catch (Exception e) {
            throw new NotificationsException();
        }
    }

    private PublicationService getPublicationService() {
        return PublicationService.get();
    }

    private KmeliaService getKmeliaService() {
        return KmeliaService.get();
    }

    private NodeService getNodeService() {
        return NodeService.get();
    }
}
