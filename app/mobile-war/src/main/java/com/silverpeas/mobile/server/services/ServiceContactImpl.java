package com.silverpeas.mobile.server.services;

import com.silverpeas.mobile.server.common.SpMobileLogModule;
import com.silverpeas.mobile.server.helpers.DataURLHelper;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;
import com.silverpeas.mobile.shared.dto.contact.ContactFilters;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.ContactException;
import com.silverpeas.mobile.shared.services.ServiceContact;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.admin.user.model.UserFull;
import org.silverpeas.core.index.search.SearchEngineProvider;
import org.silverpeas.core.index.search.model.MatchingIndexEntry;
import org.silverpeas.core.index.search.model.ParseException;
import org.silverpeas.core.index.search.model.QueryDescription;
import org.silverpeas.core.socialnetwork.relationship.RelationShipService;
import org.silverpeas.core.util.StringUtil;
import org.silverpeas.core.util.logging.SilverLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceContactImpl extends AbstractAuthenticateService implements ServiceContact {

  private static final long serialVersionUID = 1L;
  private OrganizationController organizationController = OrganizationController.get();
  private RelationShipService relationShipService = RelationShipService.get();

  /**
   * Return list of DetailUserDTO of my contacts
   * @return list of UserDetailDTO
   * @throws ContactException
   */
  public List<DetailUserDTO> getContacts(String filter, int pageSize, int startIndex)
      throws ContactException, AuthenticationException {
    ArrayList<DetailUserDTO> listUsers = new ArrayList<DetailUserDTO>();
    try {
      checkUserInSession();
      UserDetail user = getUserInSession();

      if (filter.equals(ContactFilters.ALL)) {
        UserDetail[] tabUserDetail = organizationController.getAllUsers();
        for (int i = 0; i < tabUserDetail.length; i++) {
          if (i >= startIndex && i < startIndex + pageSize) {
            listUsers.add(populate(tabUserDetail[i]));
          }
        }
      } else if (filter.equals(ContactFilters.MY)) {
        List<String> contactsIds =
            relationShipService.getMyContactsIds(Integer.parseInt(user.getId()));

        for (int j = 0; j < contactsIds.size(); j++) {
          if (j >= startIndex && j < startIndex + pageSize) {
            String id = contactsIds.get(j);
            UserDetail userDetail = getUserDetail(id);
            DetailUserDTO userDTO = populate(userDetail);
            listUsers.add(userDTO);
          }
        }
      } else {
        List<UserDetail> tabUserDetail = getFilteredUserList(filter);
        for (int i = 0; i < tabUserDetail.size(); i++) {
          if (i >= startIndex && i < startIndex + pageSize) {
            listUsers.add(populate(tabUserDetail.get(i)));
          }
        }
      }
    } catch (Exception e) {
      SilverLogger.getLogger(SpMobileLogModule.getName())
          .error("ServiceContactImpl.getContacts", "root.EX_NO_MESSAGE", e);
      throw new ContactException(e);
    }

    return listUsers;
  }

  private List<UserDetail> getFilteredUserList(final String filter) {
    UserDetail[] tabUserDetail = organizationController.getAllUsers();
    List<UserDetail> filteredUserList = null;
    if (filter.isEmpty()) {
      filteredUserList = new ArrayList<UserDetail>(Arrays.asList(tabUserDetail));
      return filteredUserList;
    }
    filteredUserList = getUsersByQuery(filter);

    return filteredUserList;
  }

  private List<UserDetail> getUsersByQuery(String query) {
    query += '*';
    List<UserDetail> results = new ArrayList<UserDetail>();
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
          if ("Contact".equals(result.getObjectType())) {
            //TODO : manager contact
          } else {
            results.add(organizationController.getUserDetail(objectId));
          }
        }
      }

    } catch (ParseException e) {
      e.printStackTrace();
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
   * Populate user DTO.
   * @param userDetail
   * @return
   */
  private DetailUserDTO populate(UserDetail userDetail) {
    if (userDetail != null) {
      SilverLogger.getLogger(SpMobileLogModule.getName())
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
      String avatar = DataURLHelper.convertAvatarToUrlData(userDetail.getAvatarFileName(), "24x");
      dto.setAvatar(avatar);
      if (userFull != null) {
        dto.setPhoneNumber(userFull.getValue("phone"));
        dto.setCellularPhoneNumber(userFull.getValue("cellularPhone"));
        dto.setFaxPhoneNumber(userFull.getValue("fax"));
      }
      return dto;
    }
    return null;
  }

  /**
   * Return UserDetail with the id contact
   * @param id
   * @return UserDetail
   * @throws ContactException
   */
  private UserDetail getUserDetail(String id) throws ContactException {
    String ldapUserId = organizationController.getUserDetailByDBId(Integer.parseInt(id));
    UserDetail User = organizationController.getUserDetail(ldapUserId);
    return User;
  }
}