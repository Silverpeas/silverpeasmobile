package com.silverpeas.mobile.client.common;

import com.google.gwt.core.client.GWT;
import com.silverpeas.mobile.shared.services.ServiceAlmanach;
import com.silverpeas.mobile.shared.services.ServiceAlmanachAsync;
import com.silverpeas.mobile.shared.services.ServiceConnection;
import com.silverpeas.mobile.shared.services.ServiceConnectionAsync;
import com.silverpeas.mobile.shared.services.ServiceContact;
import com.silverpeas.mobile.shared.services.ServiceContactAsync;
import com.silverpeas.mobile.shared.services.ServiceDashboard;
import com.silverpeas.mobile.shared.services.ServiceDashboardAsync;
import com.silverpeas.mobile.shared.services.ServiceDocuments;
import com.silverpeas.mobile.shared.services.ServiceDocumentsAsync;
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
	public static ServiceGalleryAsync serviceMedia = (ServiceGalleryAsync) GWT.create(ServiceGallery.class);
	public static ServiceNavigationAsync serviceNavigation = (ServiceNavigationAsync) GWT.create(ServiceNavigation.class);
	public static ServiceDocumentsAsync serviceDocuments = (ServiceDocumentsAsync) GWT.create(ServiceDocuments.class);
}
