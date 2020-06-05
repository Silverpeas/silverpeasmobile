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

package org.silverpeas.mobile.client.apps.workflow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.silverpeas.mobile.client.SpMobil;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.apps.workflow.events.app.AbstractWorkflowAppEvent;
import org.silverpeas.mobile.client.apps.workflow.events.app.WorkflowAppEventHandler;
import org.silverpeas.mobile.client.apps.workflow.events.app.WorkflowLoadActionFormEvent;
import org.silverpeas.mobile.client.apps.workflow.events.app.WorkflowLoadInstanceEvent;
import org.silverpeas.mobile.client.apps.workflow.events.app.WorkflowLoadInstancesEvent;
import org.silverpeas.mobile.client.apps.workflow.events.app.WorkflowLoadUserFieldEvent;
import org.silverpeas.mobile.client.apps.workflow.events.app.WorkflowProcessFormEvent;
import org.silverpeas.mobile.client.apps.workflow.events.app.WorkflowRoleChangeEvent;
import org.silverpeas.mobile.client.apps.workflow.events.pages.WorkflowActionProcessedEvent;
import org.silverpeas.mobile.client.apps.workflow.events.pages.WorkflowLoadedInstancesEvent;
import org.silverpeas.mobile.client.apps.workflow.pages.WorkflowActionFormPage;
import org.silverpeas.mobile.client.apps.workflow.pages.WorkflowPage;
import org.silverpeas.mobile.client.apps.workflow.pages.WorkflowPresentationPage;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.FormsHelper;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.event.ErrorEvent;
import org.silverpeas.mobile.client.common.navigation.UrlUtils;
import org.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOnly;
import org.silverpeas.mobile.client.components.userselection.events.pages.AllowedUsersAndGroupsLoadedEvent;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowFieldDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowFormActionDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowInstancePresentationFormDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowInstancesDTO;

import java.util.List;


public class WorkflowApp extends App implements NavigationEventHandler, WorkflowAppEventHandler {

  private ApplicationMessages globalMsg;
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
      setApplicationInstance(event.getInstance());
      WorkflowPage page = new WorkflowPage();
      page.show();
      loadInstances(new WorkflowLoadInstancesEvent());
    }
  }

  @Override
  public void showContent(final NavigationShowContentEvent event) {
    if (event.getContent().getType().equals("Component")) {
      ServicesLocator.getServiceNavigation().isWorkflowApp(event.getContent().getInstanceId(), new AsyncCallback<Boolean>() {
        @Override
        public void onFailure(final Throwable t) {
          EventBus.getInstance().fireEvent(new ErrorEvent(t));
        }

        @Override
        public void onSuccess(final Boolean workflow) {
          if (workflow) {
            ApplicationInstanceDTO app = new ApplicationInstanceDTO();
            app.setId(event.getContent().getInstanceId());
            app.setWorkflow(true);
            NavigationAppInstanceChangedEvent ev = new NavigationAppInstanceChangedEvent(app);
            appInstanceChanged(ev);
          }
          Notification.activityStop();
        }
      });
    } else if (event.getContent().getType().equals("ProcessInstance")) {
      ApplicationInstanceDTO app = new ApplicationInstanceDTO();
      app.setId(event.getContent().getInstanceId());
      app.setWorkflow(true);
      setApplicationInstance(app);
      currentRole = event.getContent().getRole();

      WorkflowLoadInstanceEvent levent = new WorkflowLoadInstanceEvent();
      levent.setRole(event.getContent().getRole());
      levent.setId(event.getContent().getId());
      loadInstance(levent);
    }
  }

  @Override
  public void loadInstances(final WorkflowLoadInstancesEvent event) {
    AsyncCallbackOnlineOnly action = new AsyncCallbackOnlineOnly<WorkflowInstancesDTO>() {
      @Override
      public void attempt() {
        ServicesLocator.getServiceWorkflow()
            .getInstances(getApplicationInstance().getId(), event.getRole(), this);
      }

      public void onSuccess(WorkflowInstancesDTO dto) {
        super.onSuccess(dto);
        WorkflowLoadedInstancesEvent event = new WorkflowLoadedInstancesEvent();
        //currentRole = dto.getRoles().entrySet().iterator().next().getKey();
        event.setData(dto);
        event.setInstanceId(getApplicationInstance().getId());
        EventBus.getInstance().fireEvent(event);
      }
    };
    action.attempt();
  }

  @Override
  public void loadInstance(final WorkflowLoadInstanceEvent event) {
    AsyncCallbackOnlineOnly action = new AsyncCallbackOnlineOnly<WorkflowInstancePresentationFormDTO>() {
      @Override
      public void attempt() {
        ServicesLocator.getServiceWorkflow().getPresentationForm(event.getId(), event.getRole(), this);
      }

      public void onSuccess(WorkflowInstancePresentationFormDTO form) {
        super.onSuccess(form);
        WorkflowPresentationPage page = new WorkflowPresentationPage();
        page.setData(form, getApplicationInstance());
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
        page.setData(dto, getApplicationInstance());
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
    JavaScriptObject formData = FormsHelper.createFormData();
    for (WorkflowFieldDTO f : event.getData()) {
      if (f.getType().equalsIgnoreCase("file")) {
        formData = FormsHelper.populateFormData(formData, f.getName(), f.getObjectValue());
      } else if(f.getType().equalsIgnoreCase("user") || f.getType().equalsIgnoreCase("multipleUser") || f.getType().equalsIgnoreCase("group")) {
        formData = FormsHelper.populateFormData(formData, f.getName(), f.getValueId());
      } else {
        formData = FormsHelper.populateFormData(formData, f.getName(), f.getValue());
      }
    }
    String url = UrlUtils.getUploadLocation();
    url +=  "FormAction";
    processAction(this, url, formData, SpMobil.getUserToken(), getApplicationInstance().getId(), currentAction, currentRole, currentState, event.getProcessId());
  }

  @Override
  public void roleChanged(final WorkflowRoleChangeEvent workflowRoleChangeEvent) {
    this.currentRole = workflowRoleChangeEvent.getRole();
  }

  public void actionProcessed() {
    EventBus.getInstance().fireEvent(new WorkflowActionProcessedEvent());
  }

  public void actionNotProcessed(int error) {
    EventBus.getInstance().fireEvent(new ErrorEvent(new RequestException("Error " + error)));
  }

  private static native void processAction(WorkflowApp app, String url, JavaScriptObject fd, String token, String instanceId, String currentAction, final String currentRole, String currentState, String processId) /*-{
    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, false);
    xhr.setRequestHeader("X-Silverpeas-Session", token);
    xhr.onreadystatechange = function() {
      if (xhr.readyState == 4 && xhr.status == 200) {
        // Every thing ok, file uploaded
        app.@org.silverpeas.mobile.client.apps.workflow.WorkflowApp::actionProcessed()();
      } else {
        app.@org.silverpeas.mobile.client.apps.workflow.WorkflowApp::actionNotProcessed(I)(xhr.status);
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
