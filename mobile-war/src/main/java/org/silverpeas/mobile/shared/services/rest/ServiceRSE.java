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

import jsinterop.base.JsPropertyMap;
import org.silverpeas.mobile.client.common.network.rest.RestCallback;
import org.silverpeas.mobile.shared.dto.StatusDTO;

/**
 * Service pour gérer les requêtes liées au statut RSE (Responsabilité Sociétale des Entreprises).
 * @author svu
 */
public class ServiceRSE extends AbstractService {

  private static final String PATH = "/silverpeas/services/mobile/rse";

  public void updateStatus(String textStatus, RestCallback<String> callback) {
    String url = PATH + "/status/" + encode(textStatus);
    post(
            url,
            null,
            result -> (String) result,
            callback
    );
  }

  public void getStatus(RestCallback<StatusDTO> callback) {
    String url = PATH + "/status";
    get(url, result -> StatusDTO.fromJSON((JsPropertyMap<Object>) result), callback);
  }
}