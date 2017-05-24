package com.silverpeas.mobile.server.services;

import com.silverpeas.mobile.server.common.SpMobileLogModule;
import com.silverpeas.mobile.server.helpers.DataURLHelper;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;
import com.silverpeas.mobile.shared.dto.contact.ContactFilters;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.ContactException;
import com.silverpeas.mobile.shared.services.ServiceContact;
import org.silverpeas.components.yellowpages.service.YellowpagesService;
import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.admin.user.model.UserFull;
import org.silverpeas.core.contact.model.ContactDetail;
import org.silverpeas.core.contact.model.ContactPK;
import org.silverpeas.core.index.search.SearchEngineProvider;
import org.silverpeas.core.index.search.model.MatchingIndexEntry;
import org.silverpeas.core.index.search.model.ParseException;
import org.silverpeas.core.index.search.model.QueryDescription;
import org.silverpeas.core.socialnetwork.relationship.RelationShipService;
import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.SettingBundle;
import org.silverpeas.core.util.StringUtil;
import org.silverpeas.core.util.logging.SilverLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class ServiceContactImpl extends AbstractAuthenticateService implements ServiceContact {

  private static final long serialVersionUID = 1L;
  private OrganizationController organizationController = OrganizationController.get();
  private RelationShipService relationShipService = RelationShipService.get();
  private static List<String> domainsIds = new ArrayList<String>();

  static {
    SettingBundle mobileSettings = ResourceLocator.getSettingBundle("org.silverpeas.mobile.mobileSettings");
    String domains = mobileSettings.getString("directory.domains", "");
    StringTokenizer stk = new StringTokenizer(domains,",");
    while(stk.hasMoreTokens()) {
      domainsIds.add(stk.nextToken());
    }
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
            UserDetail userDetail = getUserDetail(id);
            DetailUserDTO userDTO = populate(userDetail);
            listUsers.add(userDTO);
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
              if (domainsIds.isEmpty() || domainsIds.contains(userDetail.getDomainId())) {
                results.add(userDetail);
              }
            }
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
   * Populate user or contact DTO.
   * @param
   * @return
   */
  private DetailUserDTO populate(Object user) {
    if (user != null && user instanceof UserDetail) {
      UserDetail userDetail = (UserDetail) user;
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
    } else if (user != null && user instanceof ContactDetail) {
      ContactDetail contactDetail = (ContactDetail) user;
      SilverLogger.getLogger(SpMobileLogModule.getName())
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
      dto.setCellularPhoneNumber("");
      dto.setLanguage("");
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