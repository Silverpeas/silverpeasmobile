package com.oosphere.silverpeasmobile.contact;

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
    return Arrays.asList(organizationController.getAllUsers());
  }
  
  public UserFull getUserDetail(String userId){
    return organizationController.getUserFull(userId);
  }

}
