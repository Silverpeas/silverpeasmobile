/*
 * Copyright (C) 2000 - 2018 Silverpeas
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
import org.silverpeas.mobile.client.common.network.SpMobileRpcRequestBuilder;
import org.silverpeas.mobile.server.services.ServiceHyperLinkImpl;
import org.silverpeas.mobile.shared.services.*;
import org.silverpeas.mobile.shared.services.navigation.ServiceNavigation;
import org.silverpeas.mobile.shared.services.navigation.ServiceNavigationAsync;

public class ServicesLocator {
  private static SpMobileRpcRequestBuilder builder = new SpMobileRpcRequestBuilder();

  private static ServiceAgendaAsync serviceAgenda =
      (ServiceAgendaAsync) GWT.create(ServiceAgenda.class);
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
  private static ServiceCommentsAsync serviceComments =
      (ServiceCommentsAsync) GWT.create(ServiceComments.class);
  private static ServiceNotificationsAsync serviceNotifications =
      (ServiceNotificationsAsync) GWT.create(ServiceNotifications.class);
  private static ServiceNewsAsync serviceNews = (ServiceNewsAsync) GWT.create(ServiceNews.class);
  private static ServiceFavoritesAsync serviceFavorites =
      (ServiceFavoritesAsync) GWT.create(ServiceFavorites.class);
  private static ServiceBlogAsync serviceBlog = (ServiceBlogAsync) GWT.create(ServiceBlog.class);
  private static ServiceWorkflowAsync serviceWorkflow =
      (ServiceWorkflowAsync) GWT.create(ServiceWorkflow.class);
  private static ServiceHyperLinkAsync serviceHyperLink =
      (ServiceHyperLinkAsync) GWT.create(ServiceHyperLink.class);

  public static ServiceAgendaAsync getServiceAgenda() {
    ((ServiceDefTarget) serviceAgenda).setRpcRequestBuilder(builder);
    changeServiceEntryPoint((ServiceDefTarget) serviceAgenda);
    return serviceAgenda;
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

  public static ServiceBlogAsync getServiceBlog() {
    ((ServiceDefTarget) serviceBlog).setRpcRequestBuilder(builder);
    changeServiceEntryPoint((ServiceDefTarget) serviceBlog);
    return serviceBlog;
  }

  public static ServiceFavoritesAsync getServiceFavorites() {
    ((ServiceDefTarget) serviceFavorites).setRpcRequestBuilder(builder);
    changeServiceEntryPoint((ServiceDefTarget) serviceFavorites);
    return serviceFavorites;
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

  public static ServiceCommentsAsync getServiceComments() {
    ((ServiceDefTarget) serviceComments).setRpcRequestBuilder(builder);
    changeServiceEntryPoint((ServiceDefTarget) serviceComments);
    return serviceComments;
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
