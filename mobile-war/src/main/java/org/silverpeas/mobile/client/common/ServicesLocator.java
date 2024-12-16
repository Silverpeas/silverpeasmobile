/*
 * Copyright (C) 2000 - 2024 Silverpeas
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
import org.silverpeas.mobile.client.common.network.SpMobileRpcRequestBuilder;
import org.silverpeas.mobile.shared.services.rest.*;

public class ServicesLocator {
  private static SpMobileRpcRequestBuilder builder = new SpMobileRpcRequestBuilder();
  private static RestDispatcher dispatcher = new RestDispatcher();

  private static org.silverpeas.mobile.shared.services.rest.ServiceConnection serviceConnectionRest = GWT.create(
      org.silverpeas.mobile.shared.services.rest.ServiceConnection.class);

  private static ServiceContact serviceContact = GWT.create(ServiceContact.class);
  private static ServiceTask serviceTasks = GWT.create(ServiceTask.class);
  private static ServiceNavigation serviceNavigation = GWT.create(ServiceNavigation.class);
  private static ServiceRSE serviceRSE = GWT.create(ServiceRSE.class);
  private static ServiceDocuments serviceDocuments = GWT.create(ServiceDocuments.class);
  private static ServiceMedia serviceMedia = GWT.create(ServiceMedia.class);
  private static ServiceSearch serviceSearch = GWT.create(ServiceSearch.class);
  private static ServiceNotifications serviceNotifications = GWT.create(ServiceNotifications.class);
  private static ServiceNews serviceNews = GWT.create(ServiceNews.class);
  private static ServiceClassifieds serviceClassifieds = GWT.create(ServiceClassifieds.class);
  private static ServiceSurvey serviceSurvey = GWT.create(ServiceSurvey.class);
  private static ServiceWorkflow serviceWorkflow = GWT.create(ServiceWorkflow.class);
  private static ServiceHyperLink serviceHyperLink = GWT.create(ServiceHyperLink.class);

  private static ServiceBlog serviceBlog = GWT.create(ServiceBlog.class);
  private static ServiceAuthentication serviceRestAuthentication = GWT.create(ServiceAuthentication.class);
  private static ServiceAlmanach serviceAlmanach = GWT.create(ServiceAlmanach.class);
  private static ServiceUserCalendar serviceUserCalendar = GWT.create(ServiceUserCalendar.class);
  private static ServiceReminder serviceReminder = GWT.create(ServiceReminder.class);
  private static ServiceRestDocuments serviceRestDocuments = GWT.create(
      ServiceRestDocuments.class);
  private static ServiceComment serviceRestComment = GWT.create(ServiceComment.class);

  private static ServiceTickets serviceRestTickets = GWT.create(ServiceTickets.class);
  private static ServiceMyLinks serviceMyLinks = GWT.create(ServiceMyLinks.class);
  private static ServicePassword servicePassword = GWT.create(ServicePassword.class);

  private static ServiceFaq serviceFaq = GWT.create(ServiceFaq.class);
  private static ServiceFormsOnline serviceFormsOnline = GWT.create(ServiceFormsOnline.class);
  private static ServiceTermsOfService serviceTermsOfService = GWT.create(ServiceTermsOfService.class);

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
    initRestContext(login, password, domainId);
    return serviceRestAuthentication;
  }

  public static ServiceBlog getServiceBlog() {
    initRestContext();
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
    initRestContext();
    return serviceFormsOnline;
  }

  public static ServiceTermsOfService getServiceTermsOfService() {
    initRestContext();
    return serviceTermsOfService;
  }

  public static ServicePassword getServicePassword() {
    initRestContext();
    return servicePassword;
  }

  public static ServiceMyLinks getServiceMyLinks() {
    initRestContext();
    return serviceMyLinks;
  }

  public static ServiceComment getRestServiceComment() {
    initRestContext();
    return serviceRestComment;
  }

  public static ServiceTickets getRestServiceTickets() {
    initRestContext();
    return serviceRestTickets;
  }

  public static ServiceRestDocuments getRestServiceDocuments() {
    initRestContext();
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
    initRestContext();
    return serviceHyperLink;
  }

  public static ServiceWorkflow getServiceWorkflow() {
    initRestContext();
    return serviceWorkflow;
  }

  public static ServiceClassifieds getServiceClassifieds() {
    initRestContext();
    return serviceClassifieds;
  }

  public static ServiceSurvey getServiceSurvey() {
    initRestContext();
    return serviceSurvey;
  }

  public static ServiceNews getServiceNews() {
    initRestContext();
    return serviceNews;
  }

  public static ServiceNotifications getServiceNotifications() {
    initRestContext();
    return serviceNotifications;
  }

  public static ServiceSearch getServiceSearch() {
    initRestContext();
    return serviceSearch;
  }

  public static ServiceMedia getServiceMedia() {
    initRestContext();
    return serviceMedia;
  }

  public static ServiceDocuments getServiceDocuments() {
    initRestContext();
    return serviceDocuments;
  }

  public static ServiceRSE getServiceRSE() {
    initRestContext();
    return serviceRSE;
  }

  public static ServiceNavigation getServiceNavigation() {
    initRestContext();
    return serviceNavigation;
  }

  public static ServiceTask getServiceTasks() {
    initRestContext();
    return serviceTasks;
  }

  public static ServiceConnection getServiceConnection() {
    initRestContext();
    return serviceConnectionRest;
  }

  public static ServiceContact getServiceContact() {
    initRestContext();
    return serviceContact;
  }

  public static ServiceOrgChartGroup getServiceOrgChartGroup() {
    initRestContext();
    return serviceOrgChartGroup;
  }

}
