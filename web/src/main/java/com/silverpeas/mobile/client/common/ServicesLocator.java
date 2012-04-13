/**
 * Copyright (C) 2000 - 2011 Silverpeas
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
 * "http://www.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.silverpeas.mobile.client.common;

import com.google.gwt.core.client.GWT;
import com.silverpeas.mobile.shared.services.ServiceAgenda;
import com.silverpeas.mobile.shared.services.ServiceAgendaAsync;
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
  public static ServiceConnectionAsync serviceConnection =
      (ServiceConnectionAsync) GWT.create(ServiceConnection.class);
  public static ServiceContactAsync serviceContact =
      (ServiceContactAsync) GWT.create(ServiceContact.class);
  public static ServiceAgendaAsync serviceAgenda =
      (ServiceAgendaAsync) GWT.create(ServiceAgenda.class);
  public static ServiceDashboardAsync serviceDashboard =
      (ServiceDashboardAsync) GWT.create(ServiceDashboard.class);
  public static ServiceGalleryAsync serviceGallery =
      (ServiceGalleryAsync) GWT.create(ServiceGallery.class);
  public static ServiceNavigationAsync serviceNavigation =
      (ServiceNavigationAsync) GWT.create(ServiceNavigation.class);
  public static ServiceDocumentsAsync serviceDocuments =
      (ServiceDocumentsAsync) GWT.create(ServiceDocuments.class);
}
