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
import org.silverpeas.mobile.shared.dto.reservations.ReservationDTO;
import org.silverpeas.mobile.shared.dto.reservations.ResourceDTO;

import java.util.List;

/**
 * @author svu
 */
public class ServiceResourcesManager extends AbstractService {

  private static final String PATH = "/silverpeas/services/mobile/resourcesManager";

  public void checkDates(String appId, String startDate, String endDate, RestCallback<String> callback) {
    String url = PATH + "/" + encode(appId) + "/resources/checkdates/" + encode(startDate) + "/" + encode(endDate);
    getText(url, result -> (String) result, callback);
  }

  public void getAvailableResources(String appId, String startDate, String endDate, RestCallback<List<ResourceDTO>> callback) {
    String url = PATH + "/" + encode(appId) + "/resources/available/" + encode(startDate) + "/" + encode(endDate);
    get(url, result -> mapArray(result, ResourceDTO::fromJSON), callback);
  }

  public void getMyReservations(String appId, RestCallback<List<ReservationDTO>> callback) {
    String url = PATH + "/" + encode(appId) + "/reservations/my";
    get(url, result -> mapArray(result, ReservationDTO::fromJSON), callback);
  }

  public void saveReservation(String appId, ReservationDTO reservation, RestCallback<ReservationDTO> callback) {
    String url = PATH + "/" + encode(appId) + "/saveReservation";
    post(
            url,
            Global.JSON.stringify(Js.asAny(reservation.toJSON())),
            result -> ReservationDTO.fromJSON((JsPropertyMap<Object>) result),
            callback
    );
  }

  public void deleteReservation(String appId, ReservationDTO reservation, RestCallback<Void> callback) {
    String url = PATH + "/" + encode(appId) + "/reservation";
    delete(
            url,
            Global.JSON.stringify(Js.asAny(reservation.toJSON())),
            result -> null,
            callback
    );
  }
}
