/*
 * Copyright (C) 2000 - 2017 Silverpeas
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
 *
 */

package com.silverpeas.mobile.client.apps.workflow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.SpMobil;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import com.silverpeas.mobile.client.apps.workflow.events.app.AbstractWorkflowAppEvent;
import com.silverpeas.mobile.client.apps.workflow.events.app.WorkflowAppEventHandler;
import com.silverpeas.mobile.client.apps.workflow.events.app.WorkflowLoadActionFormEvent;
import com.silverpeas.mobile.client.apps.workflow.events.app.WorkflowLoadInstanceEvent;
import com.silverpeas.mobile.client.apps.workflow.events.app.WorkflowLoadInstancesEvent;
import com.silverpeas.mobile.client.apps.workflow.events.app.WorkflowLoadUserFieldEvent;
import com.silverpeas.mobile.client.apps.workflow.events.app.WorkflowProcessFormEvent;
import com.silverpeas.mobile.client.apps.workflow.events.pages.WorkflowActionProcessedEvent;
import com.silverpeas.mobile.client.apps.workflow.events.pages.WorkflowLoadedInstancesEvent;
import com.silverpeas.mobile.client.apps.workflow.pages.WorkflowActionFormPage;
import com.silverpeas.mobile.client.apps.workflow.pages.WorkflowPage;
import com.silverpeas.mobile.client.apps.workflow.pages.WorkflowPresentationPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.common.navigation.UrlUtils;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOnly;
import com.silverpeas.mobile.client.components.userselection.events.pages.AllowedUsersAndGroupsLoadedEvent;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import com.silverpeas.mobile.shared.dto.workflow.WorkflowFieldDTO;
import com.silverpeas.mobile.shared.dto.workflow.WorkflowFormActionDTO;
import com.silverpeas.mobile.shared.dto.workflow.WorkflowInstancePresentationFormDTO;
import com.silverpeas.mobile.shared.dto.workflow.WorkflowInstancesDTO;

import java.util.List;


public class WorkflowApp extends App implements NavigationEventHandler, WorkflowAppEventHandler{

  private ApplicationMessages globalMsg;
  private ApplicationInstanceDTO instance;
  private String currentRole;
  private String currentAction;
  private String currentState;


  public WorkflowApp() {
    super();
    globalMsg = GWT.create(ApplicationMessages.class);
    EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
    EventBus.getInstance().addHandler(AbstractWorkflowAppEvent.TYPE, this);
  }

  @Override
  public void start() {
    // no "super.start(lauchingPage);" this apps is used in another apps
  }

  @Override
  public void stop() {
    // never stop
  }

  @Override
  public void appInstanceChanged(NavigationAppInstanceChangedEvent event) {
    if (event.getInstance().isWorkflow()) {
      this.instance = event.getInstance();

      WorkflowPage page = new WorkflowPage();
      page.show();
      loadInstances(new WorkflowLoadInstancesEvent());
    }
  }

  @Override
  public void showContent(final NavigationShowContentEvent event) {

  }

  @Override
  public void loadInstances(final WorkflowLoadInstancesEvent event) {
    AsyncCallbackOnlineOnly action = new AsyncCallbackOnlineOnly<WorkflowInstancesDTO>() {
      @Override
      public void attempt() {
        ServicesLocator.getServiceWorkflow()
            .getInstances(instance.getId(), event.getRole(), this);
      }

      public void onSuccess(WorkflowInstancesDTO dto) {
        super.onSuccess(dto);
        WorkflowLoadedInstancesEvent event = new WorkflowLoadedInstancesEvent();
        currentRole = dto.getRoles().entrySet().iterator().next().getKey();
        event.setData(dto);
        event.setInstanceId(instance.getId());
        EventBus.getInstance().fireEvent(event);
      }
    };
    action.attempt();
  }

  @Override
  public void loadInstance(final WorkflowLoadInstanceEvent event) {
    currentRole = event.getRole();
    AsyncCallbackOnlineOnly action = new AsyncCallbackOnlineOnly<WorkflowInstancePresentationFormDTO>() {
      @Override
      public void attempt() {
        ServicesLocator.getServiceWorkflow().getPresentationForm(event.getId(), event.getRole(), this);
      }

      public void onSuccess(WorkflowInstancePresentationFormDTO form) {
        super.onSuccess(form);
        WorkflowPresentationPage page = new WorkflowPresentationPage();
        page.setData(form, instance);
        page.show();
      }
    };
    action.attempt();
  }

  @Override
  public void loadActionForm(final WorkflowLoadActionFormEvent event) {
    currentAction = event.getActionName();
    currentState = event.getState();
    AsyncCallbackOnlineOnly action = new AsyncCallbackOnlineOnly<WorkflowFormActionDTO>() {
      @Override
      public void attempt() {
        ServicesLocator.getServiceWorkflow().getActionForm(event.getInstanceId(), currentRole, event.getActionName(), this);
      }

      public void onSuccess(WorkflowFormActionDTO dto) {
        super.onSuccess(dto);
        dto.setActionName(currentAction);
        WorkflowActionFormPage page = new WorkflowActionFormPage();
        page.setData(dto, instance);
        page.show();
      }
    };
    action.attempt();
  }

  @Override
  public void loadUserField(final WorkflowLoadUserFieldEvent event) {
    AsyncCallbackOnlineOnly action = new AsyncCallbackOnlineOnly<List<BaseDTO>>() {
      @Override
      public void attempt() {
        ServicesLocator.getServiceWorkflow().getUserField(event.getInstanceId(), event.getActionName(), event.getFieldName(), currentRole, this);
      }

      public void onSuccess(List<BaseDTO> users) {
        super.onSuccess(users);
        AllowedUsersAndGroupsLoadedEvent ev = new AllowedUsersAndGroupsLoadedEvent(users);
        EventBus.getInstance().fireEvent(ev);
      }
    };
    action.attempt();
  }

  @Override
  public void processForm(final WorkflowProcessFormEvent event) {
    //TODO
    JavaScriptObject formData = createFormData();
    for (WorkflowFieldDTO f : event.getData()) {
      if (f.getType().equalsIgnoreCase("file")) {
        formData = populateFormData(formData, f.getName(), f.getObjectValue());
      } else if(f.getType().equalsIgnoreCase("user") || f.getType().equalsIgnoreCase("multipleUser") || f.getType().equalsIgnoreCase("group")) {
        formData = populateFormData(formData, f.getName(), f.getValueId());
      } else {
        formData = populateFormData(formData, f.getName(), f.getValue());
      }
    }
    String url = UrlUtils.getUploadLocation();
    url +=  "FormAction";
    processAction(this, url, formData, SpMobil.getUserToken(), instance.getId(), currentAction, currentRole, currentState, event.getProcessId());
  }

  public void actionProcessed() {
    EventBus.getInstance().fireEvent(new WorkflowActionProcessedEvent());
  }

  public void actionNotProcessed(int error) {
    EventBus.getInstance().fireEvent(new ErrorEvent(new RequestException("Error " + error)));
  }

  private static native JavaScriptObject populateFormData(JavaScriptObject formData, String name, Element value) /*-{
    if (value == null) {
      formData.append(name, null);
    } else {
      formData.append(name, value.files[0]);
    }
    return formData;
  }-*/;

  private static native JavaScriptObject populateFormData(JavaScriptObject formData, String name, String value) /*-{
    formData.append(name, value);
    return formData;
  }-*/;

  private static native JavaScriptObject createFormData() /*-{
    var fd = new FormData();
    return fd;
  }-*/;

  private static native void processAction(WorkflowApp app, String url, JavaScriptObject fd, String token, String instanceId, String currentAction, String currentRole, String currentState, String processId) /*-{
    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, false);
    xhr.setRequestHeader("X-Silverpeas-Session", token);
    xhr.onreadystatechange = function() {
      if (xhr.readyState == 4 && xhr.status == 200) {
        // Every thing ok, file uploaded
        app.@com.silverpeas.mobile.client.apps.workflow.WorkflowApp::actionProcessed()();
      } else {
        app.@com.silverpeas.mobile.client.apps.workflow.WorkflowApp::actionNotProcessed(I)(xhr.status);
      }
    };
    fd.append("instanceId", instanceId);
    fd.append("currentAction", currentAction);
    fd.append("currentRole", currentRole);
    fd.append("currentState", currentState);
    fd.append("processId", processId);
    xhr.send(fd);
  }-*/;
}
