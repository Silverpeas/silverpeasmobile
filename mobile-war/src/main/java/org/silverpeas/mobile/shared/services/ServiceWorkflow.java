/*
 * Copyright (C) 2000 - 2020 Silverpeas
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

package org.silverpeas.mobile.shared.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowFormActionDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowInstancePresentationFormDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowInstancesDTO;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.silverpeas.mobile.shared.exceptions.WorkflowException;

import java.util.List;

@RemoteServiceRelativePath("Workflow")
public interface ServiceWorkflow extends RemoteService {

  List<BaseDTO> getUserField(String instanceId, String actionName, String fieldName, String role) throws
                                                                                                  WorkflowException, AuthenticationException;

  WorkflowInstancesDTO getInstances(String instanceId, String userRole) throws WorkflowException, AuthenticationException;

  WorkflowInstancePresentationFormDTO getPresentationForm(String instanceId, String role) throws WorkflowException, AuthenticationException;

  WorkflowFormActionDTO getActionForm(String instanceId, String role, String action) throws WorkflowException, AuthenticationException;
}
