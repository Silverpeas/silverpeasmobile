package com.silverpeas.mobile.server.services;

import com.silverpeas.mobile.server.helpers.DataURLHelper;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.GroupDTO;
import com.silverpeas.mobile.shared.dto.UserDTO;
import com.silverpeas.mobile.shared.dto.notifications.NotificationDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.NotificationsException;
import com.silverpeas.mobile.shared.services.ServiceNotifications;
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
import org.silverpeas.core.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

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
                      .convertAvatarToUrlData(userDetail.getAvatarFileName(), "24x");
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
