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
import org.silverpeas.mobile.shared.StreamingList;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowDataDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowFormActionDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowInstanceDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowInstancePresentationFormDTO;
import org.silverpeas.mobile.shared.helpers.UserFieldHelper;

import java.util.List;

/**
 * Service to manage requests related to workflows.
 * @author svu
 */
public class ServiceWorkflow extends AbstractService {

  private static final String PATH = "/silverpeas/services/mobile/workflow";

  /**
   * Retrieves user field data for a workflow.
   * @param appId The ID of the application.
   * @param instanceId The ID of the workflow instance.
   * @param actionName The name of the action.
   * @param fieldName The name of the field.
   * @param role The user role.
   * @param callback The callback to handle the response (list of BaseDTO).
   */
  public void getUserField(String appId, String instanceId, String actionName, String fieldName, String role, RestCallback<List<BaseDTO>> callback) {
    String url = PATH + "/" + encode(appId) + "/userField/" + encode(instanceId) + "/" +
            encode(actionName) + "/" + encode(fieldName) + "/" + encode(role);
    get(url, result -> mapArray(result, UserFieldHelper::userFieldFromJSON), callback);
  }

  /**
   * Retrieves workflow instances for a given role and call number.
   * @param appId The ID of the application.
   * @param userRole The user role.
   * @param callNumber The call number.
   * @param callback The callback to handle the response (StreamingList of WorkflowInstanceDTO).
   */
  public void getWorkflowInstances(String appId, String userRole, int callNumber, RestCallback<StreamingList<WorkflowInstanceDTO>> callback) {
    String url = PATH + "/" + encode(appId) + "/instances/" + encode(userRole) + "/" + callNumber;
    get(url, result -> StreamingList.fromJSON((JsPropertyMap<Object>) result), callback);
  }

  /**
   * Retrieves data instances for a given role.
   * @param appId The ID of the application.
   * @param userRole The user role.
   * @param callback The callback to handle the response (WorkflowDataDTO).
   */
  public void getDataInstances(String appId, String userRole, RestCallback<WorkflowDataDTO> callback) {
    String url = PATH + "/" + encode(appId) + "/datainstances/" + encode(userRole);
    get(url, result -> WorkflowDataDTO.fromJSON((JsPropertyMap<Object>) result), callback);
  }

  /**
   * Retrieves the presentation form for a workflow instance.
   * @param appId The ID of the application.
   * @param instanceId The ID of the workflow instance.
   * @param role The user role.
   * @param callback The callback to handle the response (WorkflowInstancePresentationFormDTO).
   */
  public void getPresentationForm(String appId, String instanceId, String role, RestCallback<WorkflowInstancePresentationFormDTO> callback) {
    String url = PATH + "/" + encode(appId) + "/presentationForm/" + encode(instanceId) + "/" + encode(role);
    get(url, result -> WorkflowInstancePresentationFormDTO.fromJSON((JsPropertyMap<Object>) result), callback);
  }

  /**
   * Retrieves the action form for a workflow instance.
   * @param appId The ID of the application.
   * @param instanceId The ID of the workflow instance.
   * @param role The user role.
   * @param action The action to perform.
   * @param callback The callback to handle the response (WorkflowFormActionDTO).
   */
  public void getActionForm(String appId, String instanceId, String role, String action, RestCallback<WorkflowFormActionDTO> callback) {
    String url = PATH + "/" + encode(appId) + "/actionForm/" + encode(instanceId) + "/" +
            encode(role) + "/" + encode(action);
    get(url, result -> WorkflowFormActionDTO.fromJSON((JsPropertyMap<Object>) result), callback);
  }
}