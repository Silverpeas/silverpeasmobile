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
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.FormFieldDTO;
import org.silverpeas.mobile.shared.dto.GroupDTO;
import org.silverpeas.mobile.shared.dto.UserDTO;
import org.silverpeas.mobile.shared.dto.formsonline.FormDTO;
import org.silverpeas.mobile.shared.dto.formsonline.FormLayerDTO;
import org.silverpeas.mobile.shared.dto.formsonline.FormRequestDTO;
import org.silverpeas.mobile.shared.dto.formsonline.ValidationRequestDTO;
import org.silverpeas.mobile.shared.dto.navigation.SpaceDTO;

import java.util.List;

/**
 * Service for handling requests related to online forms.
 * @author svu
 */
public class ServiceFormsOnline extends AbstractService {

  private static final String PATH = "/silverpeas/services/mobile/formsOnline";

  /**
   * Load a form request.
   * @param appId
   * @param requestId
   * @param callback
   */
  public void loadRequest(String appId, String requestId, RestCallback<FormRequestDTO> callback) {
    String url = PATH + "/" + encode(appId) + "/loadRequest/" + encode(requestId);
    post(
            url,
            null, // Pas de corps pour cette requête POST
            result -> FormRequestDTO.fromJSON((JsPropertyMap<Object>) result),
            callback
    );
  }

  /**
   * Retrieve the forms that can be submitted for a given application.
   * @param appId
   * @param callback
   */
  public void getSendablesForms(String appId, RestCallback<List<FormDTO>> callback) {
    String url = PATH + "/" + encode(appId) + "/sendables";
    get(url, result -> mapArray(result, FormDTO::fromJSON), callback);
  }

  /**
   * Retrieve the user's requests for a given application.
   * @param appId .
   * @param callback
   */
  public void getMyRequests(String appId, RestCallback<List<FormRequestDTO>> callback) {
    String url = PATH + "/" + encode(appId) + "/myrequests";
    get(url, result -> mapArray(result, FormRequestDTO::fromJSON), callback);
  }

  /**
   * Retrieve a form layer.
   * @param appId
   * @param formName
   * @param layerType
   * @param callback
   */
  public void getFormLayer(String appId, String formName, String layerType, RestCallback<FormLayerDTO> callback) {
    String url = PATH + "/" + encode(appId) + "/formlayer/" + encode(formName) + "/" + encode(layerType);
    get(url, result -> FormLayerDTO.fromJSON((JsPropertyMap<Object>) result), callback);
  }

  /**
   * Get form.
   * @param appId
   * @param formName
   * @param callback
   */
  public void getForm(String appId, String formName, RestCallback<List<FormFieldDTO>> callback) {
    String url = PATH + "/" + encode(appId) + "/form/" + encode(formName);
    get(url, result -> mapArray(result, FormFieldDTO::fromJSON), callback);
  }

  /**
   * Save form.
   * @param appId .
   * @param formId
   * @param callback
   */
  public void saveForm(String appId, String formId, RestCallback<Boolean> callback) {
    String url = PATH + "/" + encode(appId) + "/saveForm/" + encode(formId);
    post(
            url,
            null,
            result -> Js.asBoolean(result),
            callback
    );
  }

  /**
   * Get a user field.
   * @param appId
   * @param formName
   * @param fieldName
   * @param callback
   */
  public void getUserField(String appId, String formName, String fieldName, RestCallback<List<BaseDTO>> callback) {
    String url = PATH + "/" + encode(appId) + "/form/" + encode(formName) + "/" + encode(fieldName);
    get(url, result -> mapArray(result, ServiceFormsOnline::userFieldFromJSON), callback);
  }

  private static BaseDTO userFieldFromJSON(JsPropertyMap<Object> json) {
    BaseDTO dto = null;
    Object type = json.get("className");
    if (type != null && type.toString().contains(UserDTO.class.getSimpleName())) {
      dto = UserDTO.fromJSON(json);
    } else if (type != null && type.toString().contains(GroupDTO.class.getSimpleName())) {
      dto = GroupDTO.fromJSON(json);
    }
    return dto;
  }

  /**
   * Retrieve the requests for a given form.
   * @param appId
   * @param formId
   * @param callback
   */
  public void getRequests(String appId, String formId, RestCallback<List<FormRequestDTO>> callback) {
    String url = PATH + "/" + encode(appId) + "/requests/" + encode(formId);
    get(url, result -> mapArray(result, FormRequestDTO::fromJSON), callback);
  }

  /**
   * Retrieve the forms eligible for a given application.
   * @param appId
   * @param callback
   */
  public void getReceivablesForms(String appId, RestCallback<List<FormDTO>> callback) {
    String url = PATH + "/" + encode(appId) + "/receivables";
    get(url, result -> mapArray(result, FormDTO::fromJSON), callback);
  }

  /**
   * Process a form request.
   * @param appId
   * @param requestId
   * @param validation
   * @param callback
   */
  public void processRequest(String appId, String requestId, ValidationRequestDTO validation, RestCallback<Void> callback) {
    String url = PATH + "/" + encode(appId) + "/processRequest/" + encode(requestId);
    post(
            url,
            Global.JSON.stringify(Js.asAny(validation.toJSON())),
            result -> null,
            callback
    );
  }
}