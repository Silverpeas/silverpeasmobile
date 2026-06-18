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
import org.silverpeas.mobile.shared.dto.survey.SurveyDTO;
import org.silverpeas.mobile.shared.dto.survey.SurveyDetailDTO;

import java.util.List;

/**
 * Service to manage requests related to surveys.
 * @author svu
 */
public class ServiceSurvey extends AbstractService {

  private static final String PATH = "/silverpeas/services/mobile/survey";

  /**
   * Retrieves all surveys for a given application.
   * @param appId The ID of the application.
   * @param callback The callback to handle the response (list of SurveyDTO).
   */
  public void getSurveys(String appId, RestCallback<List<SurveyDTO>> callback) {
    String url = PATH + "/" + encode(appId) + "/all";
    get(url, result -> mapArray(result, SurveyDTO::fromJSON), callback);
  }

  /**
   * Retrieves a specific survey by its ID.
   * @param appId The ID of the application.
   * @param id The ID of the survey.
   * @param callback The callback to handle the response (SurveyDetailDTO).
   */
  public void getSurvey(String appId, String id, RestCallback<SurveyDetailDTO> callback) {
    String url = PATH + "/" + encode(appId) + "/" + encode(id);
    get(url, result -> SurveyDetailDTO.fromJSON((JsPropertyMap<Object>) result), callback);
  }

  /**
   * Saves a survey for a given application.
   * @param appId The ID of the application.
   * @param survey The survey data to save.
   * @param callback The callback to handle the response (no data returned).
   */
  public void saveSurvey(String appId, SurveyDetailDTO survey, RestCallback<Void> callback) {
    String url = PATH + "/" + encode(appId) + "/";
    post(
            url,
            Global.JSON.stringify(Js.asAny(survey.toJSON())),
            result -> null,
            callback
    );
  }
}