package com.oosphere.silverpeasmobile.contact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.stratelia.webactiv.beans.admin.OrganizationController;
import com.stratelia.webactiv.beans.admin.UserDetail;
import com.stratelia.webactiv.beans.admin.UserFull;

public class ContactManager {

  private OrganizationController organizationController;

  public ContactManager(OrganizationController organizationController) {
    this.organizationController = organizationController;
  }

  public List<UserDetail> getAll(String userId){
    UserDetail currentUser = organizationController.getUserDetail(userId);
    String userDomainId = currentUser.getDomainId();
    
    List<UserDetail> listUsers = Arrays.asList(organizationController.getAllUsers());
    List<UserDetail> listUsersOfSameDomain = getListUsersOfDomain(userDomainId, listUsers);
    
    return listUsersOfSameDomain;
  }

  private List<UserDetail> getListUsersOfDomain(String domainId, List<UserDetail> listUsers) {
    List<UserDetail> listUsersOfSameDomain = new ArrayList<UserDetail>();
    for (UserDetail userDetail : listUsers) {
      if(domainId.equals(userDetail.getDomainId())){
        listUsersOfSameDomain.add(userDetail);
      }
    }
    return listUsersOfSameDomain;
  }
  
  public UserFull getUserDetail(String userId){
    return organizationController.getUserFull(userId);
  }

}
