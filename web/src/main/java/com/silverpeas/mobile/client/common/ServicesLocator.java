package com.silverpeas.mobile.client.common;

import com.google.gwt.core.client.GWT;
import com.silverpeas.mobile.shared.services.ServiceAdmin;
import com.silverpeas.mobile.shared.services.ServiceAdminAsync;
import com.silverpeas.mobile.shared.services.ServiceAgenda;
import com.silverpeas.mobile.shared.services.ServiceAgendaAsync;
import com.silverpeas.mobile.shared.services.ServiceConnection;
import com.silverpeas.mobile.shared.services.ServiceConnectionAsync;
import com.silverpeas.mobile.shared.services.ServiceContact;
import com.silverpeas.mobile.shared.services.ServiceContactAsync;
import com.silverpeas.mobile.shared.services.ServiceDashboard;
import com.silverpeas.mobile.shared.services.ServiceDashboardAsync;
import com.silverpeas.mobile.shared.services.ServiceGallery;
import com.silverpeas.mobile.shared.services.ServiceGalleryAsync;
import com.silverpeas.mobile.shared.services.ServiceRSE;
import com.silverpeas.mobile.shared.services.ServiceRSEAsync;
import com.silverpeas.mobile.shared.services.navigation.ServiceNavigation;
import com.silverpeas.mobile.shared.services.navigation.ServiceNavigationAsync;

public class ServicesLocator {
	public static ServiceRSEAsync serviceRSE = (ServiceRSEAsync) GWT.create(ServiceRSE.class);
	public static ServiceConnectionAsync serviceConnection = (ServiceConnectionAsync) GWT.create(ServiceConnection.class);
	public static ServiceContactAsync serviceContact = (ServiceContactAsync) GWT.create(ServiceContact.class);
	public static ServiceAgendaAsync serviceAgenda = (ServiceAgendaAsync) GWT.create(ServiceAgenda.class);
	public static ServiceDashboardAsync serviceDashboard = (ServiceDashboardAsync) GWT.create(ServiceDashboard.class);	
	public static ServiceGalleryAsync serviceGallery = (ServiceGalleryAsync) GWT.create(ServiceGallery.class);
	public static ServiceAdminAsync serviceAdmin = (ServiceAdminAsync) GWT.create(ServiceAdmin.class);
	public static ServiceNavigationAsync serviceNavigation = (ServiceNavigationAsync) GWT.create(ServiceNavigation.class);
}
