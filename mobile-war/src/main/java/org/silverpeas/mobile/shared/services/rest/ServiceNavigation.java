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

package org.silverpeas.mobile.shared.services.rest;

import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.client.common.network.rest.RestCallback;
import org.silverpeas.mobile.shared.dto.DetailUserDTO;
import org.silverpeas.mobile.shared.dto.HomePageDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.navigation.SilverpeasObjectDTO;
import org.silverpeas.mobile.shared.dto.navigation.SpaceDTO;

import java.util.List;

/**
 * Service to manage requests related to navigation within Silverpeas.
 * @author svu
 */
public class ServiceNavigation extends AbstractService {

  private static final String PATH = "/silverpeas/services/mobile/navigation";

  /**
   * Retrieves a space by its ID.
   * @param spaceId
   * @param callback
   */
  public void getSpace(String spaceId, RestCallback<SpaceDTO> callback) {
    String url = PATH + "/space/" + encode(spaceId) + "/";
    get(url, result -> SpaceDTO.fromJSON((JsPropertyMap<Object>) result), callback);
  }

  /**
   * Retrieves the contents of a user's personal space.
   * @param userId
   * @param callback
   */
  public void getPersonnalSpaceContent(String userId, RestCallback<List<ApplicationInstanceDTO>> callback) {
    String url = PATH + "/personalSpace/" + encode(userId) + "/";
    get(url, result -> mapArray(result, ApplicationInstanceDTO::fromJSON), callback);
  }

  /**
   * Retrieves spaces and applications from a root space.
   * @param rootSpaceId
   * @param callback
   */
  public void getSpacesAndApps(String rootSpaceId, RestCallback<List<SilverpeasObjectDTO>> callback) {
    String url = PATH + "/spacesAndApps/" + encode(rootSpaceId) + "/";
    get(url, result -> mapArray(result, ServiceNavigation::spacesAndAppsFromJSON), callback);
  }

  private static SilverpeasObjectDTO spacesAndAppsFromJSON(JsPropertyMap<Object> json) {
    SilverpeasObjectDTO dto = null;
    Object type = json.get("className");
    if (type != null && type.toString().contains(ApplicationInstanceDTO.class.getSimpleName())) {
      dto = ApplicationInstanceDTO.fromJSON(json);
    } else if (type != null && type.toString().contains(SpaceDTO.class.getSimpleName())) {
      dto = SpaceDTO.fromJSON(json);
    }
    return dto;
  }

  /**
   * Retrieves an application by its instance ID, content ID, and content type.
   * @param instanceId
   * @param contentId
   * @param contentType
   * @param callback
   */
  public void getApp(String instanceId, String contentId, String contentType, RestCallback<ApplicationInstanceDTO> callback) {
    String url = PATH + "/app/" + encode(instanceId) + "/" + encode(contentId) + "/" + encode(contentType) + "/";
    get(url, result -> ApplicationInstanceDTO.fromJSON((JsPropertyMap<Object>) result), callback);
  }

  /**
   * Retrieves a user's details by their login and domain ID.
   * @param login
   * @param domainId
   * @param callback
   */
  public void getUser(String login, String domainId, RestCallback<DetailUserDTO> callback) {
    String url = PATH + "/user/" + encode(login) + "/" + encode(domainId) + "/";
    get(url, result -> DetailUserDTO.fromJSON((JsPropertyMap<Object>) result), callback);
  }

  /**
   * Enables tablet mode.
   * @param callback
   */
  public void setTabletMode(RestCallback<Boolean> callback) {
    String url = PATH + "/setTabletMode";
    post(url, null, result -> Js.asBoolean(result), callback);
  }

  /**
   * Clears the application cache.
   * @param callback
   */
  public void clearAppCache(RestCallback<Void> callback) {
    String url = PATH + "/clearAppCache";
    get(url, result -> null, callback);
  }

  /**
   * Retrieves homepage data for a given space.
   * @param spaceId
   * @param zoom
   * @param callback
   */
  public void getHomePageData(String spaceId, String zoom, RestCallback<HomePageDTO> callback) {
    String url = PATH + "/homepage/" + encode(spaceId) + "/" + encode(zoom) + "/";
    get(url, result -> HomePageDTO.fromJSON((JsPropertyMap<Object>) result), callback);
  }

  /**
   * Checks whether an application is of workflow type.
   * @param instanceId
   * @param callback
   */
  public void isWorkflowApp(String instanceId, RestCallback<Boolean> callback) {
    String url = PATH + "/isWorkflowApp/" + encode(instanceId) + "/";
    get(url, result -> Js.asBoolean(result), callback);
  }

  /**
   * Stores a messaging token.
   * @param token
   * @param callback
   */
  public void storeTokenMessaging(String token, RestCallback<Void> callback) {
    String url = PATH + "/storeTokenMessaging/" + encode(token) + "/";
    put(url, null, result -> null, callback);
  }
}
