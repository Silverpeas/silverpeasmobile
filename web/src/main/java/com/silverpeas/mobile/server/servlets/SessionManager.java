package com.silverpeas.mobile.server.servlets;

import com.silverpeas.mobile.server.helpers.MediaHelper;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author: svu
 */
public class SessionManager implements HttpSessionListener {
  @Override
  public void sessionCreated(final HttpSessionEvent httpSessionEvent) {

  }

  @Override
  public void sessionDestroyed(final HttpSessionEvent httpSessionEvent) {
    MediaHelper.cleanTemporaryFiles(httpSessionEvent.getSession().getId());
  }
}
