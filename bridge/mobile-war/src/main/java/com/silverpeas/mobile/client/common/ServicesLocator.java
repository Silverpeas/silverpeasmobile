package com.silverpeas.mobile.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.silverpeas.mobile.client.common.network.SpMobileRpcRequestBuilder;
import com.silverpeas.mobile.shared.services.*;
import com.silverpeas.mobile.shared.services.navigation.ServiceNavigation;
import com.silverpeas.mobile.shared.services.navigation.ServiceNavigationAsync;

public class ServicesLocator {
    private static SpMobileRpcRequestBuilder builder = new SpMobileRpcRequestBuilder();

    private static ServiceConnectionAsync serviceConnection = (ServiceConnectionAsync) GWT.create(ServiceConnection.class);
    private static ServiceContactAsync serviceContact = (ServiceContactAsync) GWT.create(ServiceContact.class);
    private static ServiceTasksAsync serviceTasks = (ServiceTasksAsync) GWT.create(ServiceTasks.class);
    private static ServiceNavigationAsync serviceNavigation = (ServiceNavigationAsync) GWT.create(ServiceNavigation.class);
    private static ServiceRSEAsync serviceRSE = (ServiceRSEAsync) GWT.create(ServiceRSE.class);
    private static ServiceDocumentsAsync serviceDocuments = (ServiceDocumentsAsync) GWT.create(ServiceDocuments.class);
    private static ServiceMediaAsync serviceMedia = (ServiceMediaAsync) GWT.create(ServiceMedia.class);
    private static ServiceSearchAsync serviceSearch = (ServiceSearchAsync) GWT.create(ServiceSearch.class);
    private static ServiceCommentsAsync serviceComments = (ServiceCommentsAsync) GWT.create(ServiceComments.class);
    private static ServiceNotificationsAsync serviceNotifications = (ServiceNotificationsAsync) GWT.create(ServiceNotifications.class);
    public static ServiceNewsAsync serviceNews = (ServiceNewsAsync) GWT.create(ServiceNews.class);

    public static ServiceNotificationsAsync getServiceNotifications() {
        ((ServiceDefTarget) serviceNotifications).setRpcRequestBuilder(builder);
        return serviceNotifications;
    }

    public static ServiceCommentsAsync getServiceComments() {
        ((ServiceDefTarget) serviceComments).setRpcRequestBuilder(builder);
        return serviceComments;
    }

    public static ServiceSearchAsync getServiceSearch() {
        ((ServiceDefTarget) serviceSearch).setRpcRequestBuilder(builder);
        return serviceSearch;
    }

    public static ServiceMediaAsync getServiceMedia() {
        ((ServiceDefTarget) serviceMedia).setRpcRequestBuilder(builder);
        return serviceMedia;
    }

    public static ServiceDocumentsAsync getServiceDocuments() {
        ((ServiceDefTarget) serviceDocuments).setRpcRequestBuilder(builder);
        return serviceDocuments;
    }

    public static ServiceRSEAsync getServiceRSE() {
        ((ServiceDefTarget) serviceRSE).setRpcRequestBuilder(builder);
        return serviceRSE;
    }

    public static ServiceNavigationAsync getServiceNavigation() {
        ((ServiceDefTarget) serviceNavigation).setRpcRequestBuilder(builder);
        return serviceNavigation;
    }

    public static ServiceTasksAsync getServiceTasks() {
        ((ServiceDefTarget) serviceTasks).setRpcRequestBuilder(builder);
        return serviceTasks;
    }

    public static ServiceConnectionAsync getServiceConnection() {
        ((ServiceDefTarget) serviceConnection).setRpcRequestBuilder(builder);
        return serviceConnection;
    }

    public static ServiceContactAsync getServiceContact() {
        ((ServiceDefTarget) serviceContact).setRpcRequestBuilder(builder);
        return serviceContact;
    }
}
