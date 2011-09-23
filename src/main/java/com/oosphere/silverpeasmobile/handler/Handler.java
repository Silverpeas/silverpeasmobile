package com.oosphere.silverpeasmobile.handler;

import javax.servlet.http.HttpServletRequest;

import com.oosphere.silverpeasmobile.exception.SilverpeasMobileException;

public abstract class Handler {
  
  public abstract String getPage(HttpServletRequest request)
  throws SilverpeasMobileException;
  
}