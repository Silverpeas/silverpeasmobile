package com.silverpeas.mobile.server.services;

import com.silverpeas.admin.ejb.AdminBusiness;
import com.silverpeas.mobile.server.config.Configurator;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.GroupDTO;
import com.silverpeas.mobile.shared.dto.UserDTO;
import com.silverpeas.mobile.shared.dto.notifications.NotificationDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.NotificationsException;
import com.silverpeas.mobile.shared.services.ServiceNotifications;
import com.silverpeas.util.StringUtil;
import com.stratelia.silverpeas.notificationManager.GroupRecipient;
import com.stratelia.silverpeas.notificationManager.NotificationMetaData;
import com.stratelia.silverpeas.notificationManager.NotificationSender;
import com.stratelia.silverpeas.notificationManager.UserRecipient;
import com.stratelia.webactiv.beans.admin.*;
import com.stratelia.webactiv.kmelia.control.ejb.KmeliaBm;
import com.stratelia.webactiv.kmelia.model.TopicDetail;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.node.control.NodeBm;
import com.stratelia.webactiv.util.node.model.NodeDetail;
import com.stratelia.webactiv.util.node.model.NodePK;
import com.stratelia.webactiv.util.publication.control.PublicationBm;
import com.stratelia.webactiv.util.publication.model.PublicationPK;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Service de gestion des Notifications.
 *
 * @author svuillet
 */
public class ServiceNotificationsImpl extends AbstractAuthenticateService implements ServiceNotifications {

    private static final long serialVersionUID = 1L;
    private AdminBusiness adminBm;
    private PublicationBm pubBm;
    private NodeBm nodeBm;
    private KmeliaBm kmeliaBm;
    private OrganizationController organizationController = new OrganizationController();

    @Override
    public List<BaseDTO> getAllowedUsersAndGroups(String componentId, String contentId) throws NotificationsException, AuthenticationException {
        ArrayList<BaseDTO> usersAndGroups = new ArrayList<BaseDTO>();

        try {
            ComponentInst componentInst = getAdminBm().getComponentInst(componentId);
            List<ProfileInst> profiles = new ArrayList<ProfileInst>();
            if (componentId.toLowerCase().startsWith("kmelia")) {
                if (isRightsOnTopicsEnabled(componentId)) {
                    TopicDetail topic = getKmeliaBm().getPublicationFather(new PublicationPK(contentId, componentId), true, getUserInSession().getId(), true);
                    if (topic != null){
                        NodePK pk = topic.getNodePK();
                        NodeDetail node = getNodeBm().getDetail(pk);
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
                }
            }

            for (ProfileInst profile : profiles) {
                for (String userId : profile.getAllUsers()) {
                    UserDetail userDetail = organizationController.getUserDetail(userId);
                    UserDTO u = new UserDTO();
                    u.setId(userDetail.getId());
                    u.setFirstName(userDetail.getFirstName());
                    u.setLastName(userDetail.getLastName());
                    u.seteMail(userDetail.geteMail());
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

    private List<ProfileInst> getTopicProfiles(String topicId, String componentId) {
        List<ProfileInst> alShowProfile = new ArrayList<ProfileInst>();
        String[] asAvailProfileNames = getAdmin().getAllProfilesNames("kmelia");
        for (String asAvailProfileName : asAvailProfileNames) {
            ProfileInst profile = getTopicProfile(asAvailProfileName, topicId, componentId);
            profile.setLabel(getAdmin().getProfileLabelfromName("kmelia", asAvailProfileName,
                    getUserInSession().getUserPreferences().getLanguage()));
            alShowProfile.add(profile);
        }

        return alShowProfile;
    }

    private ProfileInst getTopicProfile(String role, String topicId, String componentId) {
        List<ProfileInst> profiles = getAdmin().getProfilesByObject(topicId, ObjectType.NODE.getCode(), componentId);
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
    public void send(NotificationDTO notification, List<BaseDTO> receivers) throws NotificationsException, AuthenticationException {
        try {
            NotificationSender notificationSender = new NotificationSender(notification.getInstanceId());
            NotificationMetaData metaData = new NotificationMetaData();

            for (BaseDTO receiver : receivers) {
                if (receiver instanceof UserDTO) {
                    metaData.addUserRecipient(new UserRecipient(((UserDTO) receiver).getId()));
                } else if (receiver instanceof GroupDTO) {
                    metaData.addGroupRecipient(new GroupRecipient(((GroupDTO) receiver).getId()));
                }
            }

            metaData.setSendImmediately(true);
            if (notification.getContentType().equals(NotificationDTO.TYPE_PUBLICATION)) {
                String url = Configurator.getConfigValue("protocol") + "://" + Configurator.getConfigValue("localhost") + ":" + Configurator.getConfigValue("jboss.http.port") + "/silverpeas/Publication/" + notification.getContentId();
                metaData.setLink(url);
            }
            metaData.setAnswerAllowed(false);
            metaData.setContent(notification.getMessage());
            metaData.setSender(getUserInSession().geteMail());

            ComponentInst app = getAdminBm().getComponentInst(notification.getInstanceId());
            SpaceInst space = getAdminBm().getSpaceInstById(app.getDomainFatherId());
            String title = space.getName(getUserInSession().getUserPreferences().getLanguage()) + " - ";
            title += app.getLabel(getUserInSession().getUserPreferences().getLanguage()) + " : ";
            title += "Notification de " + getUserInSession().getDisplayedName(); //TODO : internationalization
            metaData.setTitle(title);
            notificationSender.notifyUser(metaData);
        } catch (Exception e) {
            throw new NotificationsException();
        }
    }

    private AdminBusiness getAdminBm() throws Exception {
        if (adminBm == null) {
            adminBm = EJBUtilitaire.getEJBObjectRef(JNDINames.ADMINBM_EJBHOME, AdminBusiness.class);
        }
        return adminBm;
    }

    private PublicationBm getPubBm() throws Exception {
        if (pubBm == null) {
            pubBm = EJBUtilitaire.getEJBObjectRef(JNDINames.PUBLICATIONBM_EJBHOME, PublicationBm.class);
        }
        return pubBm;
    }

    private KmeliaBm getKmeliaBm() throws Exception {
        if (kmeliaBm == null) {
            kmeliaBm = EJBUtilitaire.getEJBObjectRef(JNDINames.KMELIABM_EJBHOME, KmeliaBm.class);
        }
        return kmeliaBm;
    }

    private NodeBm getNodeBm() throws Exception {
        if (nodeBm == null) {
            nodeBm = EJBUtilitaire.getEJBObjectRef(JNDINames.NODEBM_EJBHOME, NodeBm.class);
        }
        return nodeBm;
    }

    private AdminController getAdmin() {
        return new AdminController(getUserInSession().getId());
    }
}
