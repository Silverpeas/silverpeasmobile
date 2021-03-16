/*
 * Copyright (C) 2000 - 2020 Silverpeas
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
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
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
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import org.fusesource.restygwt.client.Defaults;
import org.silverpeas.mobile.client.common.network.RestAuthenticationDispatcher;
import org.silverpeas.mobile.client.common.network.RestDispatcher;
import org.silverpeas.mobile.client.common.network.SpMobileRpcRequestBuilder;
import org.silverpeas.mobile.shared.services.*;
import org.silverpeas.mobile.shared.services.navigation.ServiceNavigation;
import org.silverpeas.mobile.shared.services.navigation.ServiceNavigationAsync;
import org.silverpeas.mobile.shared.services.rest.*;

public class ServicesLocator {
  private static SpMobileRpcRequestBuilder builder = new SpMobileRpcRequestBuilder();
  private static RestDispatcher dispatcher = new RestDispatcher();

  private static ServiceConnectionAsync serviceConnection =
      (ServiceConnectionAsync) GWT.create(ServiceConnection.class);
  private static ServiceContactAsync serviceContact =
      (ServiceContactAsync) GWT.create(ServiceContact.class);
  private static ServiceTasksAsync serviceTasks =
      (ServiceTasksAsync) GWT.create(ServiceTasks.class);
  private static ServiceNavigationAsync serviceNavigation =
      (ServiceNavigationAsync) GWT.create(ServiceNavigation.class);
  private static ServiceRSEAsync serviceRSE = (ServiceRSEAsync) GWT.create(ServiceRSE.class);
  private static ServiceDocumentsAsync serviceDocuments =
      (ServiceDocumentsAsync) GWT.create(ServiceDocuments.class);
  private static ServiceMediaAsync serviceMedia =
      (ServiceMediaAsync) GWT.create(ServiceMedia.class);
  private static ServiceSearchAsync serviceSearch =
      (ServiceSearchAsync) GWT.create(ServiceSearch.class);
  private static ServiceNotificationsAsync serviceNotifications =
      (ServiceNotificationsAsync) GWT.create(ServiceNotifications.class);
  private static ServiceNewsAsync serviceNews = (ServiceNewsAsync) GWT.create(ServiceNews.class);
  private static ServiceClassifiedsAsync serviceClassifieds = (ServiceClassifiedsAsync) GWT.create(ServiceClassifieds.class);
  private static ServiceSurveyAsync serviceSurvey = (ServiceSurveyAsync) GWT.create(ServiceSurvey.class);
  private static ServiceWorkflowAsync serviceWorkflow =
      (ServiceWorkflowAsync) GWT.create(ServiceWorkflow.class);
  private static ServiceHyperLinkAsync serviceHyperLink =
      (ServiceHyperLinkAsync) GWT.create(ServiceHyperLink.class);

  private static ServiceBlog serviceBlog = GWT.create(ServiceBlog.class);
  private static ServiceAuthentication serviceRestAuthentication = GWT.create(ServiceAuthentication.class);
  private static ServiceAlmanach serviceAlmanach = GWT.create(ServiceAlmanach.class);
  private static ServiceUserCalendar serviceUserCalendar = GWT.create(ServiceUserCalendar.class);
  private static ServiceReminder serviceReminder = GWT.create(ServiceReminder.class);
  private static ServiceRestDocuments serviceRestDocuments = GWT.create(
      ServiceRestDocuments.class);
  private static ServiceComment serviceRestComment = GWT.create(ServiceComment.class);
  private static ServiceMyLinks serviceMyLinks = GWT.create(ServiceMyLinks.class);
  private static ServicePassword servicePassword = GWT.create(ServicePassword.class);

  private static ServiceFaq serviceFaq = GWT.create(ServiceFaq.class);
  private static ServiceFormsOnline serviceFormsOnline = GWT.create(ServiceFormsOnline.class);
  private static ServiceTermsOfService serviceTermsOfService = GWT.create(ServiceTermsOfService.class);

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

  public static ServiceHyperLinkAsync getServiceHyperLink() {
    ((ServiceDefTarget) serviceHyperLink).setRpcRequestBuilder(builder);
    changeServiceEntryPoint((ServiceDefTarget) serviceHyperLink);
    return serviceHyperLink;
  }

  public static ServiceWorkflowAsync getServiceWorkflow() {
    ((ServiceDefTarget) serviceWorkflow).setRpcRequestBuilder(builder);
    changeServiceEntryPoint((ServiceDefTarget) serviceWorkflow);
    return serviceWorkflow;
  }

  public static ServiceClassifiedsAsync getServiceClassifieds() {
    ((ServiceDefTarget) serviceClassifieds).setRpcRequestBuilder(builder);
    changeServiceEntryPoint((ServiceDefTarget) serviceClassifieds);
    return serviceClassifieds;
  }

  public static ServiceSurveyAsync getServiceSurvey() {
    ((ServiceDefTarget) serviceSurvey).setRpcRequestBuilder(builder);
    changeServiceEntryPoint((ServiceDefTarget) serviceSurvey);
    return serviceSurvey;
  }

  public static ServiceNewsAsync getServiceNews() {
    ((ServiceDefTarget) serviceNews).setRpcRequestBuilder(builder);
    changeServiceEntryPoint((ServiceDefTarget) serviceNews);
    return serviceNews;
  }

  public static ServiceNotificationsAsync getServiceNotifications() {
    ((ServiceDefTarget) serviceNotifications).setRpcRequestBuilder(builder);
    changeServiceEntryPoint((ServiceDefTarget) serviceNotifications);
    return serviceNotifications;
  }

  public static ServiceSearchAsync getServiceSearch() {
    ((ServiceDefTarget) serviceSearch).setRpcRequestBuilder(builder);
    changeServiceEntryPoint((ServiceDefTarget) serviceSearch);
    return serviceSearch;
  }

  public static ServiceMediaAsync getServiceMedia() {
    return getServiceMedia(true);
  }

  public static ServiceMediaAsync getServiceMedia(boolean guiWaiting) {
    ((ServiceDefTarget) serviceMedia).setRpcRequestBuilder(builder);
    changeServiceEntryPoint((ServiceDefTarget) serviceMedia, guiWaiting);
    return serviceMedia;
  }

  public static ServiceDocumentsAsync getServiceDocuments() {
    ((ServiceDefTarget) serviceDocuments).setRpcRequestBuilder(builder);
    changeServiceEntryPoint((ServiceDefTarget) serviceDocuments);
    return serviceDocuments;
  }

  public static ServiceRSEAsync getServiceRSE() {
    ((ServiceDefTarget) serviceRSE).setRpcRequestBuilder(builder);
    changeServiceEntryPoint((ServiceDefTarget) serviceRSE);
    return serviceRSE;
  }

  public static ServiceNavigationAsync getServiceNavigation() {
    ((ServiceDefTarget) serviceNavigation).setRpcRequestBuilder(builder);
    changeServiceEntryPoint((ServiceDefTarget) serviceNavigation);
    return serviceNavigation;
  }

  public static ServiceTasksAsync getServiceTasks() {
    ((ServiceDefTarget) serviceTasks).setRpcRequestBuilder(builder);
    changeServiceEntryPoint((ServiceDefTarget) serviceTasks);
    return serviceTasks;
  }

  public static ServiceConnectionAsync getServiceConnection() {
    ((ServiceDefTarget) serviceConnection).setRpcRequestBuilder(builder);
    changeAuthentificationServiceEntryPoint((ServiceDefTarget) serviceConnection);
    return serviceConnection;
  }

  public static ServiceContactAsync getServiceContact() {
    ((ServiceDefTarget) serviceContact).setRpcRequestBuilder(builder);
    changeServiceEntryPoint((ServiceDefTarget) serviceContact);
    return serviceContact;
  }

  private static void changeServiceEntryPoint(ServiceDefTarget service) {
    changeServiceEntryPoint(service, true);
  }

  private static void changeServiceEntryPoint(ServiceDefTarget service, boolean guiWaiting) {
    if (guiWaiting) Notification.activityStart();
    String serviceEntryPoint = service.getServiceEntryPoint();
    if (!serviceEntryPoint.contains("services")) {
      serviceEntryPoint = serviceEntryPoint.replace("spmobile", "services/spmobile");
    }
    service.setServiceEntryPoint(serviceEntryPoint);
  }

  private static void changeAuthentificationServiceEntryPoint(ServiceDefTarget service) {
    String serviceEntryPoint = service.getServiceEntryPoint();
    if (!serviceEntryPoint.contains("AuthenticationServlet")) {
      serviceEntryPoint = serviceEntryPoint.replace("spmobile", "AuthenticationServlet/spmobile");
    }
    service.setServiceEntryPoint(serviceEntryPoint);
  }
}
