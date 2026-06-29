/*
 * Copyright (C) 2000 - 2026 Silverpeas
 *
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
 * "https://www.silverpeas.org/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.client.common;

import com.google.gwt.core.client.GWT;
import org.fusesource.restygwt.client.Defaults;
import org.silverpeas.mobile.client.common.network.RestAuthenticationDispatcher;
import org.silverpeas.mobile.client.common.network.RestDispatcher;
import org.silverpeas.mobile.shared.services.rest.*;

public class ServicesLocator {
  private static RestDispatcher dispatcher = new RestDispatcher();

  private static ServiceConnection serviceConnectionRest = new ServiceConnection();
  private static ServiceContact serviceContact = new ServiceContact();
  private static ServiceTask serviceTasks = new ServiceTask();
  private static ServiceNavigation serviceNavigation = new ServiceNavigation();
  private static ServiceRSE serviceRSE = new ServiceRSE();
  private static ServiceDocuments serviceDocuments = new ServiceDocuments();
  private static ServiceMedia serviceMedia = new ServiceMedia();
  private static ServiceSearch serviceSearch = new ServiceSearch();
  private static ServiceNotifications serviceNotifications = GWT.create(ServiceNotifications.class);
  private static ServiceNews serviceNews = new ServiceNews();
  private static ServiceClassifieds serviceClassifieds = new ServiceClassifieds();
  private static ServiceSurvey serviceSurvey = GWT.create(ServiceSurvey.class);
  private static ServiceWorkflow serviceWorkflow = new ServiceWorkflow();
  private static ServiceHyperLink serviceHyperLink = new ServiceHyperLink();

  private static ServiceBlog serviceBlog = new ServiceBlog();
  private static ServiceAuthentication serviceRestAuthentication = new ServiceAuthentication();
  private static ServiceAlmanach serviceAlmanach = GWT.create(ServiceAlmanach.class);
  private static ServiceUserCalendar serviceUserCalendar = GWT.create(ServiceUserCalendar.class);
  private static ServiceReminder serviceReminder = GWT.create(ServiceReminder.class);
  private static ServiceRestDocuments serviceRestDocuments = new ServiceRestDocuments();
  private static ServiceComment serviceRestComment = new ServiceComment();

  private static ServiceTickets serviceRestTickets = new ServiceTickets();
  private static ServiceMyLinks serviceMyLinks = new ServiceMyLinks();
  private static ServicePassword servicePassword = GWT.create(ServicePassword.class);

  private static ServiceFaq serviceFaq = GWT.create(ServiceFaq.class);
  private static ServiceFormsOnline serviceFormsOnline = new ServiceFormsOnline();
  private static ServiceTermsOfService serviceTermsOfService = new ServiceTermsOfService();

  private static ServiceResourcesManager serviceResourcesManager = GWT.create(ServiceResourcesManager.class);
  private static ServiceOrgChartGroup serviceOrgChartGroup = GWT.create(ServiceOrgChartGroup.class);

  private static void initRestContext() {
      Defaults.getServiceRoot().equals("/silverpeas/services");
      Defaults.setServiceRoot("/silverpeas/services");
      Defaults.setDispatcher(dispatcher);
  }

  private static void initRestContext(String login, String password, String domainId) {
    RestAuthenticationDispatcher disp = new RestAuthenticationDispatcher(login, password, domainId);
    Defaults.getServiceRoot().equals("/silverpeas/services");
    Defaults.setServiceRoot("/silverpeas/services");
    Defaults.setDispatcher(disp);
  }

  public static ServiceAuthentication getRestServiceAuthentication(String login, String password, String domainId) {
    serviceRestAuthentication.initContext(login, password, domainId);
    return serviceRestAuthentication;
  }

  public static ServiceBlog getServiceBlog() {
    return serviceBlog;
  }

  public static ServiceResourcesManager getServiceResourcesManager() {
    initRestContext();
    return serviceResourcesManager;
  }

  public static ServiceFaq getServiceFaq() {
    initRestContext();
    return serviceFaq;
  }

  public static ServiceFormsOnline getServiceFormsOnline() {
    return serviceFormsOnline;
  }

  public static ServiceTermsOfService getServiceTermsOfService() {
    return serviceTermsOfService;
  }

  public static ServicePassword getServicePassword() {
    initRestContext();
    return servicePassword;
  }

  public static ServiceMyLinks getServiceMyLinks() {
    return serviceMyLinks;
  }

  public static ServiceComment getRestServiceComment() {
    return serviceRestComment;
  }

  public static ServiceTickets getRestServiceTickets() {
    return serviceRestTickets;
  }

  public static ServiceRestDocuments getRestServiceDocuments() {
    return serviceRestDocuments;
  }

  public static ServiceReminder getServiceReminder() {
    initRestContext();
    return serviceReminder;
  }

  public static ServiceUserCalendar getServiceUserCalendar() {
    initRestContext();
    return serviceUserCalendar;
  }

  public static ServiceAlmanach getServiceAlmanach() {
    initRestContext();
    return serviceAlmanach;
  }

  public static ServiceHyperLink getServiceHyperLink() {
    return serviceHyperLink;
  }

  public static ServiceWorkflow getServiceWorkflow() {
    return serviceWorkflow;
  }

  public static ServiceClassifieds getServiceClassifieds() {
    return serviceClassifieds;
  }

  public static ServiceSurvey getServiceSurvey() {
    initRestContext();
    return serviceSurvey;
  }

  public static ServiceNews getServiceNews() {
    return serviceNews;
  }

  public static ServiceNotifications getServiceNotifications() {
    initRestContext();
    return serviceNotifications;
  }

  public static ServiceSearch getServiceSearch() {
    return serviceSearch;
  }

  public static ServiceMedia getServiceMedia() {
    initRestContext();
    return serviceMedia;
  }

  public static ServiceDocuments getServiceDocuments() {
    return serviceDocuments;
  }

  public static ServiceRSE getServiceRSE() {
    return serviceRSE;
  }

  public static ServiceNavigation getServiceNavigation() {
    return serviceNavigation;
  }

  public static ServiceTask getServiceTasks() {
    return serviceTasks;
  }

  public static ServiceConnection getServiceConnection() {
    return serviceConnectionRest;
  }

  public static ServiceContact getServiceContact() {
    return serviceContact;
  }

  public static ServiceOrgChartGroup getServiceOrgChartGroup() {
    initRestContext();
    return serviceOrgChartGroup;
  }

}
