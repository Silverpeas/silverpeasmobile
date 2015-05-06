package com.silverpeas.mobile.server.servlets;

import org.realityforge.gwt.appcache.server.AbstractManifestServlet;
import org.realityforge.gwt.appcache.server.propertyprovider.UserAgentPropertyProvider;

/**
 * @author: svu
 */
public class ManifestServlet extends AbstractManifestServlet {
    public ManifestServlet()
    {
      addPropertyProvider( new UserAgentPropertyProvider() );
    }
}
