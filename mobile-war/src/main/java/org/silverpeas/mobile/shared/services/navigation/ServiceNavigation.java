/*
 * Copyright (C) 2000 - 2021 Silverpeas
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

package org.silverpeas.mobile.shared.services.navigation;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.HomePageDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.silverpeas.mobile.shared.exceptions.NavigationException;

import java.util.List;


@RemoteServiceRelativePath("Navigation")
public interface ServiceNavigation extends RemoteService {
  public List<SilverpeasObjectDTO> getSpacesAndApps(String rootSpaceId) throws NavigationException, AuthenticationException;
  public ApplicationInstanceDTO getApp(String instanceId, String contentId, String contentType) throws NavigationException, AuthenticationException;


  DetailUserDTO getUser(String login, String domainId) throws NavigationException, AuthenticationException;


  boolean setTabletMode() throws NavigationException, AuthenticationException;

  void logout() throws AuthenticationException;

  DetailUserDTO initSession(DetailUserDTO user) throws AuthenticationException;

  String getUserToken();

  HomePageDTO getHomePageData(String spaceId) throws NavigationException, AuthenticationException;

  boolean isWorkflowApp(String intanceId) throws NavigationException, AuthenticationException;

  void storeTokenMessaging(String token) throws NavigationException,AuthenticationException;
}
