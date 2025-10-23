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

import org.apache.commons.lang3.ArrayUtils;
import org.silverpeas.components.yellowpages.service.YellowpagesService;
import org.silverpeas.core.admin.service.AdminException;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.user.model.GroupDetail;
import org.silverpeas.core.admin.user.model.User;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.admin.user.model.UserFull;
import org.silverpeas.core.annotation.WebService;
import org.silverpeas.core.contact.model.CompleteContact;
import org.silverpeas.core.contact.model.ContactDetail;
import org.silverpeas.core.contact.model.ContactPK;
import org.silverpeas.core.contribution.content.form.DataRecord;
import org.silverpeas.core.contribution.content.form.Form;
import org.silverpeas.core.contribution.content.form.PagesContext;
import org.silverpeas.core.contribution.template.publication.PublicationTemplateManager;
import org.silverpeas.core.index.search.SearchEngineProvider;
import org.silverpeas.core.index.search.model.MatchingIndexEntry;
import org.silverpeas.core.index.search.model.ParseException;
import org.silverpeas.core.index.search.model.QueryDescription;
import org.silverpeas.core.socialnetwork.relationship.RelationShipService;
import org.silverpeas.core.web.rs.UserPrivilegeValidation;
import org.silverpeas.core.web.rs.annotation.Authorized;
import org.silverpeas.kernel.bundle.LocalizationBundle;
import org.silverpeas.kernel.bundle.ResourceLocator;
import org.silverpeas.kernel.bundle.SettingBundle;
import org.silverpeas.kernel.logging.SilverLogger;
import org.silverpeas.kernel.util.StringUtil;
import org.silverpeas.mobile.server.common.SpMobileLogModule;
import org.silverpeas.mobile.server.helpers.DataURLHelper;
import org.silverpeas.mobile.server.services.helpers.UserHelper;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.contact.ContactFilters;
import org.silverpeas.mobile.shared.dto.contact.ContactScope;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.*;

@WebService
@Authorized
@Path(ServiceContact.PATH)
public class ServiceContact extends AbstractRestWebService {

    @Context
    HttpServletRequest request;

    static final String PATH = "mobile/contact";

    @Inject
    private OrganizationController organizationController;
    @Inject
    private RelationShipService relationShipService;

    private List<String> getUserProperties() {
        List<String> userProperties = new ArrayList<>();
        String properties = getSettings().getString("directory.user.properties", "");
        StringTokenizer stkU = new StringTokenizer(properties, ",");
        while (stkU.hasMoreTokens()) {
            userProperties.add(stkU.nextToken());
        }
        return userProperties;
    }

    private List<String> getContactProperties() {
        List<String> contactProperties = new ArrayList<>();
        String formProperties = getSettings().getString("directory.contact.properties", "");
        StringTokenizer stkC = new StringTokenizer(formProperties, ",");
        while (stkC.hasMoreTokens()) {
            contactProperties.add(stkC.nextToken());
        }
        return contactProperties;
    }

    private List<String> getdomainsIds() {
        List<String> domainsIds = new ArrayList<>();
        String domains = getSettings().getString("directory.domains", "");

        StringTokenizer stk = new StringTokenizer(domains, ",");
        while (stk.hasMoreTokens()) {
            domainsIds.add(stk.nextToken());
        }
        return domainsIds;
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/contact/{userId}/")
    public DetailUserDTO getContact(@PathParam("userId") String userId) throws AdminException {
        UserDetail u = Administration.get().getUserDetail(userId);
      return populate(u, false);
    }

    /**
     * Return list of DetailUserDTO of my contacts
     *
     * @return list of UserDetailDTO
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("paging/{type}/")
    public List<DetailUserDTO> getContacts(@PathParam("type") String type,
                                           @QueryParam("filter") String filter, @QueryParam("pageSize") int pageSize,
                                           @QueryParam("startIndex") int startIndex) {
        ArrayList<DetailUserDTO> listUsers = new ArrayList<>();
        try {
            switch (type) {
                case ContactFilters.ALL: {
                    List tabUserDetail = null;
                    if (filter.isEmpty()) {
                        tabUserDetail = Administration.get().getUsersOfDomains(getdomainsIds());
                    } else {
                        tabUserDetail = getUsersByQuery(filter, "UserFull");
                    }
                    for (int i = 0; i < tabUserDetail.size(); i++) {
                        if (i >= startIndex && i < startIndex + pageSize) {
                            DetailUserDTO dto = populate(tabUserDetail.get(i), true);
                            if (dto != null) listUsers.add(dto);
                        }
                    }
                    break;
                }
                case ContactFilters.ALL_EXT: {
                    List tabUserDetail = getUsersByQuery(filter, "Contact");
                    for (int i = 0; i < tabUserDetail.size(); i++) {
                        if (i >= startIndex && i < startIndex + pageSize) {
                            listUsers.add(populate(tabUserDetail.get(i), true));
                        }
                    }
                    break;
                }
                case ContactFilters.MY:
                    List<String> contactsIds =
                            relationShipService.getMyContactsIds(Integer.parseInt(getUser().getId()));

                    for (int j = 0; j < contactsIds.size(); j++) {
                        if (j >= startIndex && j < startIndex + pageSize) {
                            String id = contactsIds.get(j);
                            UserDetail userDetail = organizationController.getUserDetail(id);
                            DetailUserDTO userDTO = populate(userDetail, true);
                            listUsers.add(userDTO);
                        }
                    }
                    break;
                default:
                    break;
            }
            defaultSortContacts(listUsers);
        } catch (AdminException ade) {
            SilverLogger.getLogger(this).error("ServiceContact.getContacts", "root.EX_NO_MESSAGE", ade);
        } catch (Exception e) {
            SilverLogger.getLogger(this).error("ServiceContact.getContacts", "root.EX_NO_MESSAGE", e);
            throw e;
        }

        return listUsers;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{type}/")
    public List<DetailUserDTO> getContactsFiltered(@PathParam("type") String type, @QueryParam("filter") String filter)
          throws AdminException {
        ArrayList<DetailUserDTO> listUsers = new ArrayList<>();
        try {
            if (type.equals(ContactScope.group.name())) {
                String[] groupsIds = filter.split(",");
                GroupDetail[] groups = Administration.get().getGroups(groupsIds);
                List<User> users = new ArrayList<>();
                for (GroupDetail group : groups) {
                    users.addAll(group.getAllUsers());
                }
                users = new ArrayList<>(new HashSet<>(users));
                for (User user : users) {
                    DetailUserDTO userDTO = populate(user, true);
                    listUsers.add(userDTO);
                }
            } else if (type.equals(ContactScope.domain.name())) {
                String[] domainsIds = filter.split(",");
                UserDetail[] users = null;
                for (String domainId : domainsIds) {
                    if (users == null) {
                        users = Administration.get().getUsersOfDomain(domainId);
                    } else {
                        users = ArrayUtils.addAll(users, Administration.get().getUsersOfDomain(domainId));
                    }
                }

                Objects.requireNonNull(users);
                for (User user : users) {
                    DetailUserDTO userDTO = populate(user, true);
                    listUsers.add(userDTO);
                }
            } else if (type.isEmpty()) {
                List<UserDetail> users = Administration.get().getAllUsers();
                for (User user : users) {
                    DetailUserDTO userDTO = populate(user, true);
                    listUsers.add(userDTO);
                }
            }
        } catch (Exception e) {
            SilverLogger.getLogger(this)
                    .error("ServiceContact.getContactsFiltered", "root.EX_NO_MESSAGE", e);
            throw e;
        }

        return listUsers;
    }

    private void defaultSortContacts(final List<DetailUserDTO> listUsers) {
        listUsers.sort(Comparator.comparing(DetailUserDTO::getLastName));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("hasContacts/")
    public ContactFilters hasContacts() {
        ContactFilters result = new ContactFilters();
        List tabUserDetail = getUsersByQuery("", "Contact");
        result.setHasContacts(!tabUserDetail.isEmpty());
        List<String> contactsIds = new ArrayList<>();
        try {
            contactsIds = relationShipService.getMyContactsIds(Integer.parseInt(getUser().getId()));
        } catch (Exception e) {
            // don't treat this case
        }
        result.setHasPersonnalContacts(!contactsIds.isEmpty());
        return result;
    }

    private List getUsersByQuery(String query, String type) {
        if (!query.isEmpty()) {
            query += '*';
        }
        List results = new ArrayList<>();
        try {
            QueryDescription queryDescription = new QueryDescription(query);
            queryDescription.addComponent("users");
            for (String appId : getContactComponentIds()) {
                queryDescription.addComponent(appId);
            }

            List<MatchingIndexEntry> plainSearchResults =
                    SearchEngineProvider.getSearchEngine().search(queryDescription).getEntries();
            if (plainSearchResults != null && !plainSearchResults.isEmpty()) {
                for (MatchingIndexEntry result : plainSearchResults) {
                    String objectId = result.getObjectId();
                    if (type.equals(result.getObjectType())) {
                        if (result.getObjectType().equals("Contact")) {
                            ContactDetail contact = YellowpagesService.get()
                                    .getContactDetail(new ContactPK(objectId, result.getComponent()));
                            results.add(contact);
                        } else if (result.getObjectType().equals("UserFull")) {
                            UserDetail userDetail = organizationController.getUserDetail(objectId);
                            // if userDetail is null than mean user was deleted but is still in index
                            List<String> domainsIds = getdomainsIds();
                            if (userDetail != null &&
                                    (domainsIds.isEmpty() || domainsIds.contains(userDetail.getDomainId())) &&
                                    userDetail.isActivatedState()) {
                                results.add(userDetail);
                            }
                        }
                    }
                }
            }

        } catch (ParseException e) {
            SilverLogger.getLogger(this).error(e);
        }

        return results;
    }

    private List<String> getContactComponentIds() {
        String[] appIds =
                organizationController.getComponentIdsForUser(getUser().getId(), "yellowpages");
        List<String> result = new ArrayList<>();
        for (String appId : appIds) {
            String param =
                    organizationController.getComponentParameterValue(appId, "displayedInDirectory");
            if (StringUtil.getBooleanValue(param)) {
                result.add(appId);
            }
        }
        return result;
    }

    private DetailUserDTO populate(Object user, boolean filtered) {
        if (user instanceof UserDetail) {
            UserDetail userDetail = (UserDetail) user;
            SilverLogger.getLogger(this).debug(SpMobileLogModule.getName(), "ServiceContact.populate",
                    "User id=" + userDetail.getId());
            UserFull userFull = UserFull.getById(userDetail.getId());
            DetailUserDTO dto = null;
            if (userFull != null) {
                dto = new DetailUserDTO();
                UserHelper.initUserDTO(userFull, dto);
                dto.setConnected(userFull.isConnected());
                String avatar;

                String path = userDetail.getDomain().getSettings().getString("property.ResourceFile");
                if (!dto.getLanguage().trim().isEmpty()) path += "_" + dto.getLanguage();
                SilverLogger.getLogger(this).info("Loading domain ressource file " + path);
                LocalizationBundle domainMultiLang = ResourceLocator.getLocalizationBundle(path);
                for (String prop : domainMultiLang.keySet()) {
                    String label = domainMultiLang.getString(prop);
                    dto.addPropertyLabel(prop, label);
                }
                Map<String, String> labels = PublicationTemplateManager.getInstance().getDirectoryFormLabels(userFull.getId(), userFull.getDomainId(), dto.getLanguage());
                for (Map.Entry<String, String> prop : labels.entrySet()) {
                    dto.addPropertyLabel(prop.getKey(), prop.getValue());
                }

                if (filtered) {

                    if (DataURLHelper.hasPersonnalizedAvatar(userDetail.getAvatarFileName())) {
                        avatar = "/silverpeas/display/avatar/" + getSettings().getString("avatar.size", "24x") + "/" + userDetail.getLogin() + ".jpg";
                    } else {
                        avatar = "/silverpeas/directory/jsp/icons/avatar.png";
                    }

                    for (String prop : getUserProperties()) {
                        dto.addProperty(prop, userFull.getValue(prop));
                        dto.addProperty(prop, userFull.getDefinedExtraFormValues(((UserDetail) user).getUserPreferences().getLanguage()).get(prop));
                    }
                } else {
                    avatar = DataURLHelper.convertAvatarToUrlData(userDetail.getAvatarFileName(), "96x");
                    Map<String, String> fields = userFull.getAllDefinedValues(userFull.getUserPreferences().getLanguage());
                    for (Map.Entry<String, String> prop : fields.entrySet()) {
                        dto.addProperty(prop.getKey(), prop.getValue());
                    }
                }
                dto.setAvatar(avatar);
            }
            return dto;
        } else if (user instanceof ContactDetail) {
            ContactDetail contactDetail = (ContactDetail) user;
            SilverLogger.getLogger(this).debug(SpMobileLogModule.getName(), "ServiceContact.populate",
                    "Contact id=" + contactDetail.getPK().getId() + "app id=" +
                            contactDetail.getPK().getInstanceId());
            DetailUserDTO dto = new DetailUserDTO();
            dto.setId(contactDetail.getPK().getId());
            dto.setFirstName(contactDetail.getFirstName());
            dto.setLastName(contactDetail.getLastName());
            dto.seteMail(contactDetail.getEmail());
            dto.setPhoneNumber(contactDetail.getPhone());
            dto.setFaxPhoneNumber(contactDetail.getFax());
            dto.setAvatar("");
            dto.setStatus("");
            dto.setCellularPhoneNumber("");
            dto.setLanguage("");

            CompleteContact completeContact =
                    YellowpagesService.get().getCompleteContact(((ContactDetail) user).getPK());
            Map<String, String> fields =
                    completeContact.getFormValues(getUser().getUserPreferences().getLanguage(), true);
            if (fields != null) {
                if (filtered) {
                    for (String prop : getContactProperties()) {
                        dto.addProperty(prop, fields.get(prop));
                    }
                } else {
                    for (Map.Entry<String, String> prop : fields.entrySet()) {
                        dto.addProperty(prop.getKey(), prop.getValue());
                    }
                }
            }
            return dto;
        }
        return null;
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
    }
}