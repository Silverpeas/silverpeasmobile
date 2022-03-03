/*
 * Copyright (C) 2000 - 2022 Silverpeas
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

import org.apache.commons.lang3.ArrayUtils;
import org.silverpeas.components.yellowpages.service.YellowpagesService;
import org.silverpeas.core.admin.service.Administration;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.user.model.GroupDetail;
import org.silverpeas.core.admin.user.model.User;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.admin.user.model.UserFull;
import org.silverpeas.core.contact.model.CompleteContact;
import org.silverpeas.core.contact.model.ContactDetail;
import org.silverpeas.core.contact.model.ContactPK;
import org.silverpeas.core.index.search.SearchEngineProvider;
import org.silverpeas.core.index.search.model.MatchingIndexEntry;
import org.silverpeas.core.index.search.model.ParseException;
import org.silverpeas.core.index.search.model.QueryDescription;
import org.silverpeas.core.socialnetwork.relationship.RelationShipService;
import org.silverpeas.core.util.StringUtil;
import org.silverpeas.core.util.logging.SilverLogger;
import org.silverpeas.mobile.server.common.SpMobileLogModule;
import org.silverpeas.mobile.server.helpers.DataURLHelper;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.contact.ContactFilters;
import org.silverpeas.mobile.shared.dto.contact.ContactScope;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.silverpeas.mobile.shared.exceptions.ContactException;
import org.silverpeas.mobile.shared.services.ServiceContact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class ServiceContactImpl extends AbstractAuthenticateService implements ServiceContact {

  private static final long serialVersionUID = 1L;
  private OrganizationController organizationController = OrganizationController.get();
  private RelationShipService relationShipService = RelationShipService.get();

  private List<String> getUserProperties() {
    List<String> userProperties = new ArrayList<String>();
    String properties = getSettings().getString("directory.user.properties", "");
    StringTokenizer stkU = new StringTokenizer(properties,",");
    while(stkU.hasMoreTokens()) {
      userProperties.add(stkU.nextToken());
    }
    return userProperties;
  }

  private List<String> getContactProperties() {
    List<String> contactProperties = new ArrayList<String>();
    String formProperties = getSettings().getString("directory.contact.properties", "");
    StringTokenizer stkC = new StringTokenizer(formProperties,",");
    while(stkC.hasMoreTokens()) {
      contactProperties.add(stkC.nextToken());
    }
    return contactProperties;
  }

  private List<String> getdomainsIds() {
    List<String> domainsIds = new ArrayList<String>();
    String domains = getSettings().getString("directory.domains", "");

    StringTokenizer stk = new StringTokenizer(domains,",");
    while(stk.hasMoreTokens()) {
      domainsIds.add(stk.nextToken());
    }
    return domainsIds;
  }

  /**
   * Return list of DetailUserDTO of my contacts
   * @return list of UserDetailDTO
   * @throws ContactException
   */
  public List<DetailUserDTO> getContacts(String type, String filter, int pageSize, int startIndex)
      throws ContactException, AuthenticationException {
    ArrayList<DetailUserDTO> listUsers = new ArrayList<DetailUserDTO>();
    try {
      checkUserInSession();
      UserDetail user = getUserInSession();

      if (type.equals(ContactFilters.ALL)) {
        List tabUserDetail = getUsersByQuery(filter, "UserFull");
        for (int i = 0; i < tabUserDetail.size(); i++) {
          if (i >= startIndex && i < startIndex + pageSize) {
            listUsers.add(populate(tabUserDetail.get(i)));
          }
        }
      } else if(type.equals(ContactFilters.ALL_EXT)) {
        List tabUserDetail = getUsersByQuery(filter,"Contact");
        for (int i = 0; i < tabUserDetail.size(); i++) {
          if (i >= startIndex && i < startIndex + pageSize) {
            listUsers.add(populate(tabUserDetail.get(i)));
          }
        }
      } else if (type.equals(ContactFilters.MY)) {
        List<String> contactsIds =
            relationShipService.getMyContactsIds(Integer.parseInt(user.getId()));

        for (int j = 0; j < contactsIds.size(); j++) {
          if (j >= startIndex && j < startIndex + pageSize) {
            String id = contactsIds.get(j);
            UserDetail userDetail = organizationController.getUserDetail(id);
            DetailUserDTO userDTO = populate(userDetail);
            listUsers.add(userDTO);
          }
        }
      }
      defaultSortContacts(listUsers);
    } catch (Throwable e) {
      SilverLogger.getLogger(this)
          .error("ServiceContactImpl.getContacts", "root.EX_NO_MESSAGE", e);
      throw new ContactException(e);
    }

    return listUsers;
  }

  public List<DetailUserDTO> getContactsFiltered(String type, String filter) throws ContactException, AuthenticationException {
    ArrayList<DetailUserDTO> listUsers = new ArrayList<DetailUserDTO>();
    checkUserInSession();
    try {
      if (type.equals(ContactScope.group.name())) {
        String [] groupsIds = filter.split(",");
        GroupDetail [] groups = Administration.get().getGroups(groupsIds);
        List<User>  users = new ArrayList<>();
        for (GroupDetail group : groups) {
          users.addAll(group.getAllUsers());
        }
        users = new ArrayList<User>(new HashSet<>(users));
        for (User user : users) {
          DetailUserDTO userDTO = populate(user);
          listUsers.add(userDTO);
        }
      } else if (type.equals(ContactScope.domain.name())) {
        String [] domainsIds = filter.split(",");
        UserDetail [] users = null;
        for (String domainId : domainsIds) {
          if (users == null) {
            users = Administration.get().getUsersOfDomain(domainId);
          } else {
            users = ArrayUtils.addAll(users, Administration.get().getUsersOfDomain(domainId));
          }
        }
        for (User user : users) {
          DetailUserDTO userDTO = populate(user);
          listUsers.add(userDTO);
        }
      } else if (type.isEmpty()) {
        List<UserDetail> users = Administration.get().getAllUsers();
        for (User user : users) {
          DetailUserDTO userDTO = populate(user);
          listUsers.add(userDTO);
        }
      }
    } catch (Throwable e) {
      SilverLogger.getLogger(this)
          .error("ServiceContactImpl.getContactsFiltered", "root.EX_NO_MESSAGE", e);
      throw new ContactException(e);
    }

    return listUsers;
  }

  private void defaultSortContacts(final List listUsers) {
    Collections.sort(listUsers, new Comparator<DetailUserDTO>() {
      @Override
      public int compare(final DetailUserDTO u1, final DetailUserDTO u2) {
        return u1.getLastName().compareTo(u2.getLastName());
      }
    });
  }

  public ContactFilters hasContacts() {
    ContactFilters result = new ContactFilters();
    List tabUserDetail = getUsersByQuery("","Contact");
    result.setHasContacts(!tabUserDetail.isEmpty());
    List<String> contactsIds = new ArrayList<>();
    try {
      contactsIds = relationShipService.getMyContactsIds(Integer.parseInt(getUserInSession().getId()));
    } catch (Exception e) {}
    result.setHasPersonnalContacts(!contactsIds.isEmpty());
    return result;
  }

  private List getFilteredUserList(final String filter, String type) {
    UserDetail[] tabUserDetail = organizationController.getAllUsers();
    List filteredUserList = null;
    if (filter.isEmpty()) {
      filteredUserList = new ArrayList<UserDetail>(Arrays.asList(tabUserDetail));
      return filteredUserList;
    }
    filteredUserList = getUsersByQuery(filter, type);

    return filteredUserList;
  }

  private List getUsersByQuery(String query, String type) {
    if (!query.isEmpty()) query += '*';
    List results = new ArrayList<>();
    try {
      QueryDescription queryDescription = new QueryDescription(query);
      queryDescription.addComponent("users");
      for (String appId : getContactComponentIds()) {
        queryDescription.addComponent(appId);
      }

      List<MatchingIndexEntry> plainSearchResults = SearchEngineProvider.getSearchEngine().search(queryDescription).getEntries();
      if (plainSearchResults != null && !plainSearchResults.isEmpty()) {
        for (MatchingIndexEntry result : plainSearchResults) {
          String objectId = result.getObjectId();
          if (type.equals(result.getObjectType())) {
            if (result.getObjectType().equals("Contact")) {
              ContactDetail contact = YellowpagesService.get().getContactDetail(new ContactPK(objectId, result.getComponent()));
              results.add(contact);
            } else if (result.getObjectType().equals("UserFull")) {
              UserDetail userDetail = organizationController.getUserDetail(objectId);
              // if userDetail is null than mean user was deleted but is still in index
              List<String> domainsIds = getdomainsIds();
              if (userDetail!= null && (domainsIds.isEmpty() || domainsIds.contains(userDetail.getDomainId())) && userDetail.isActivatedState()) {
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
        organizationController.getComponentIdsForUser(getUserInSession().getId(), "yellowpages");
    List<String> result = new ArrayList<String>();
    for (String appId : appIds) {
      String param =
          organizationController.getComponentParameterValue(appId, "displayedInDirectory");
      if (StringUtil.getBooleanValue(param)) {
        result.add(appId);
      }
    }
    return result;
  }

  /**
   * Populate user or contact DTO.
   * @param
   * @return
   */
  private DetailUserDTO populate(Object user) {
    if (user != null && user instanceof UserDetail) {
      UserDetail userDetail = (UserDetail) user;
      SilverLogger.getLogger(this)
          .debug(SpMobileLogModule.getName(), "ServiceContactImpl.populate",
              "User id=" + userDetail.getId());
      UserFull userFull = UserFull.getById(userDetail.getId());
      DetailUserDTO dto = new DetailUserDTO();
      dto.setId(userFull.getId());
      dto.setFirstName(userFull.getFirstName());
      dto.setLastName(userFull.getLastName());
      dto.seteMail(userFull.geteMail());
      dto.setStatus(userFull.getStatus());
      dto.setAvatar(userFull.getAvatar());
      dto.setLanguage(userFull.getUserPreferences().getLanguage());
      dto.setConnected(userFull.isConnected());
      String avatar = DataURLHelper.convertAvatarToUrlData(userDetail.getAvatarFileName(), getSettings().getString("avatar.size", "24x"));
      dto.setAvatar(avatar);
      if (userFull != null) {
        for (String prop : getUserProperties()) {
          dto.addProperty(prop, userFull.getValue(prop));
        }
      }
      return dto;
    } else if (user != null && user instanceof ContactDetail) {
      ContactDetail contactDetail = (ContactDetail) user;
      SilverLogger.getLogger(this)
          .debug(SpMobileLogModule.getName(), "ServiceContactImpl.populate",
              "Contact id=" + contactDetail.getPK().getId() + "app id=" + contactDetail.getPK().getInstanceId());
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

      CompleteContact completeContact = YellowpagesService.get().getCompleteContact(((ContactDetail) user).getPK());
      Map<String, String> fields = completeContact.getFormValues(getUserInSession().getUserPreferences().getLanguage(), true);
      if (fields != null) {
        for (String prop : getContactProperties()) {
          dto.addProperty(prop, fields.get(prop));
        }
      }
      return dto;
    }
    return null;
  }
}