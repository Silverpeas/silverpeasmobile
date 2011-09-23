package com.oosphere.silverpeasmobile.handler;

import javax.servlet.http.HttpServletRequest;

import com.oosphere.silverpeasmobile.bean.WebBeanFactory;
import com.oosphere.silverpeasmobile.exception.SilverpeasMobileException;
import com.oosphere.silverpeasmobile.kmelia.KmeliaManager;
import com.stratelia.webactiv.beans.admin.OrganizationController;

public abstract class Handler {
  
  public abstract String getPage(HttpServletRequest request)
  throws SilverpeasMobileException;
  
}