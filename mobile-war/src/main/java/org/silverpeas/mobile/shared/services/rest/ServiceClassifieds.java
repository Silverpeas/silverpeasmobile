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

import elemental2.core.Global;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.client.common.network.rest.RestCallback;
import org.silverpeas.mobile.shared.dto.classifieds.ClassifiedDTO;
import org.silverpeas.mobile.shared.dto.classifieds.ClassifiedsDTO;

/**
 * Service to manage requests related to classifieds.
 * @author svu
 */
public class ServiceClassifieds extends AbstractService {

  private static final String PATH = "/silverpeas/services/mobile/classifieds";

  /**
   * Retrieves a specific classified by its ID.
   * @param appId The ID of the application.
   * @param id The ID of the classified.
   * @param callback The callback to handle the response (ClassifiedsDTO).
   */
  public void getClassified(String appId, String id, RestCallback<ClassifiedsDTO> callback) {
    String url = PATH + "/" + encode(appId) + "/" + encode(id);
    get(url, result -> ClassifiedsDTO.fromJSON((JsPropertyMap<Object>) result), callback);
  }

  /**
   * Sends a message to the owner of a classified.
   * @param appId The ID of the application.
   * @param message The message to send.
   * @param classifiedDTO The classified data.
   * @param callback The callback to handle the response (no data returned).
   */
  public void sendMessageToOwner(String appId, String message, ClassifiedDTO classifiedDTO, RestCallback<Void> callback) {
    String url = PATH + "/" + encode(appId) + "/" + encode(message);
    post(
            url,
            Global.JSON.stringify(Js.asAny(classifiedDTO.toJSON_IdOnly())),
            result -> null,
            callback
    );
  }

  /**
   * Retrieves all classifieds for a given application.
   * @param appId The ID of the application.
   * @param callback The callback to handle the response (ClassifiedsDTO).
   */
  public void getClassifieds(String appId, RestCallback<ClassifiedsDTO> callback) {
    String url = PATH + "/" + encode(appId) + "/all";
    get(url, result -> ClassifiedsDTO.fromJSON((JsPropertyMap<Object>) result), callback);
  }
}