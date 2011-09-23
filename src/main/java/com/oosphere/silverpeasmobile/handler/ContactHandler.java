package com.oosphere.silverpeasmobile.handler;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.oosphere.silverpeasmobile.contact.ContactManager;
import com.oosphere.silverpeasmobile.exception.SilverpeasMobileException;
import com.oosphere.silverpeasmobile.vo.ContactPropertyVO;
import com.stratelia.webactiv.beans.admin.OrganizationController;
import com.stratelia.webactiv.beans.admin.UserDetail;
import com.stratelia.webactiv.beans.admin.UserFull;

public class ContactHandler extends Handler {

  @Override
  public String getPage(HttpServletRequest request) throws SilverpeasMobileException {
    String page = "contacts.jsp";
    String subAction = request.getParameter("subAction");
    if ("contacts".equals(subAction)) {
      ContactManager contactManager = getContactManager(request);
      page = contacts(request, contactManager);
    } else if ("contactDetail".equals(subAction)) {
      ContactManager contactManager = getContactManager(request);
      page = contactDetail(request, contactManager);
    }
    return "contact/" + page;
  }

  private String contacts(HttpServletRequest request, ContactManager contactManager)
      throws SilverpeasMobileException {
    String userId = request.getParameter("userId");
    
    List<UserDetail> contacts = contactManager.getAll(userId);
    
    request.setAttribute("contacts", contacts);
    request.setAttribute("userId", userId);
    return "contacts.jsp";
  }
  
  private String contactDetail(HttpServletRequest request, ContactManager contactManager)
      throws SilverpeasMobileException {
    String userId = request.getParameter("userId");
    String contactId = request.getParameter("contactId");
    String lang = (String)request.getAttribute("lang");
    
    UserFull contact = contactManager.getUserDetail(contactId);
    List<ContactPropertyVO> contactProperties = getContactProperties(contact, lang);
    
    request.setAttribute("contact", contact);
    request.setAttribute("contactProperties", contactProperties);
    request.setAttribute("userId", userId);
    return "contact.jsp";
  }
  
  private List<ContactPropertyVO> getContactProperties(UserFull contact, String lang){
    List<ContactPropertyVO> contactProperties = new ArrayList<ContactPropertyVO>(contact.getPropertiesNames().length);
    
    for (String propertyName : contact.getPropertiesNames()) {
      ContactPropertyVO contactProperty = new ContactPropertyVO();
      contactProperty.setName(contact.getSpecificLabel(lang, propertyName));
      contactProperty.setValue(contact.getValue(propertyName));
      contactProperties.add(contactProperty);
    }
    
    return contactProperties;
  }



  private ContactManager getContactManager(HttpServletRequest request)
      throws SilverpeasMobileException {
    OrganizationController organizationController = new OrganizationController();
    return new ContactManager(organizationController);
  }

}
