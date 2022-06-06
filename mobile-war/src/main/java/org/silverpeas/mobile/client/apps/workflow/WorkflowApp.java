/*
 * Copyright (C) 2000 - 2022 Silverpeas
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

package org.silverpeas.mobile.client.apps.workflow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.RequestException;
import org.fusesource.restygwt.client.Method;
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
import org.silverpeas.mobile.client.apps.workflow.events.pages.WorkflowLoadedDataEvent;
import org.silverpeas.mobile.client.apps.workflow.events.pages.WorkflowLoadedInstancesEvent;
import org.silverpeas.mobile.client.apps.workflow.pages.WorkflowActionFormPage;
import org.silverpeas.mobile.client.apps.workflow.pages.WorkflowPage;
import org.silverpeas.mobile.client.apps.workflow.pages.WorkflowPresentationPage;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.FormsHelper;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.event.ErrorEvent;
import org.silverpeas.mobile.client.common.navigation.UrlUtils;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.components.userselection.events.pages.AllowedUsersAndGroupsLoadedEvent;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.StreamingList;
import org.silverpeas.mobile.shared.dto.BaseDTO;
import org.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowDataDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowFieldDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowFormActionDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowInstanceDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowInstancePresentationFormDTO;

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
    if (event.getInstance().getWorkflow()) {
      setApplicationInstance(event.getInstance());
      WorkflowPage page = new WorkflowPage();
      page.show();
      loadInstances(new WorkflowLoadInstancesEvent());
    }
  }

  @Override
  public void showContent(final NavigationShowContentEvent event) {
    if (event.getContent().getType().equals("Component")) {

      MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<Boolean>() {
        @Override
        public void attempt() {
          super.attempt();
          ServicesLocator.getServiceNavigation()
              .isWorkflowApp(event.getContent().getInstanceId(), this);
        }

        @Override
        public void onSuccess(final Method method, final Boolean workflow) {
          super.onSuccess(method, workflow);
          if (workflow) {
            ApplicationInstanceDTO app = new ApplicationInstanceDTO();
            app.setId(event.getContent().getInstanceId());
            app.setWorkflow(true);
            NavigationAppInstanceChangedEvent ev = new NavigationAppInstanceChangedEvent(app);
            appInstanceChanged(ev);
          }
        }
      };
      action.attempt();
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
    MethodCallbackOnlineOnly action =
        new MethodCallbackOnlineOnly<WorkflowDataDTO>() {
          @Override
          public void attempt() {
            super.attempt();
            ServicesLocator.getServiceWorkflow()
                .getDataInstances(getApplicationInstance().getId(), event.getRole(), this);
          }

          @Override
          public void onSuccess(final Method method, final WorkflowDataDTO dto) {
            super.onSuccess(method, dto);
            WorkflowLoadedDataEvent e = new WorkflowLoadedDataEvent();
            e.setData(dto);
            EventBus.getInstance().fireEvent(e);

            WorkflowRoleChangeEvent ev = new WorkflowRoleChangeEvent(dto.getRoles().values().iterator().next());

            loadWorkflowInstances(ev, 0);
          }
        };
    action.attempt();
  }

  private void loadWorkflowInstances(final WorkflowRoleChangeEvent event, final int nbCall) {
    MethodCallbackOnlineOnly action2 = new MethodCallbackOnlineOnly<StreamingList<WorkflowInstanceDTO>>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceWorkflow()
            .getWorkflowInstances(getApplicationInstance().getId(), event.getRole(), nbCall, this);
      }

      @Override
      public void onSuccess(final Method method,
          final StreamingList<WorkflowInstanceDTO> instances) {
        super.onSuccess(method, instances);
        WorkflowLoadedInstancesEvent eventPage = new WorkflowLoadedInstancesEvent();

        eventPage.setData(instances);
        eventPage.setInstanceId(getApplicationInstance().getId());
        EventBus.getInstance().fireEvent(eventPage);
        if (instances.getMoreElement()) {
          loadWorkflowInstances(event, nbCall+1);
        }
      }
    };
    action2.attempt();
  }

  @Override
  public void loadInstance(final WorkflowLoadInstanceEvent event) {
    MethodCallbackOnlineOnly action =
        new MethodCallbackOnlineOnly<WorkflowInstancePresentationFormDTO>() {
          @Override
          public void attempt() {
            super.attempt();
            ServicesLocator.getServiceWorkflow()
                .getPresentationForm(getApplicationInstance().getId() ,event.getId(), event.getRole(), this);
          }

          @Override
          public void onSuccess(final Method method,
              final WorkflowInstancePresentationFormDTO form) {
            super.onSuccess(method, form);
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
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<WorkflowFormActionDTO>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceWorkflow()
            .getActionForm(getApplicationInstance().getId(), event.getInstanceId(), currentRole, event.getActionName(), this);
      }

      @Override
      public void onSuccess(final Method method, final WorkflowFormActionDTO dto) {
        super.onSuccess(method, dto);
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
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<BaseDTO>>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceWorkflow()
            .getUserField(getApplicationInstance().getId(), event.getInstanceId(), event.getActionName(), event.getFieldName(),
                currentRole, this);
      }

      @Override
      public void onSuccess(final Method method, final List<BaseDTO> users) {
        super.onSuccess(method, users);
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
      } else if (f.getType().equalsIgnoreCase("user") ||
          f.getType().equalsIgnoreCase("multipleUser") || f.getType().equalsIgnoreCase("group")) {
        formData = FormsHelper.populateFormData(formData, f.getName(), f.getValueId());
      } else {
        formData = FormsHelper.populateFormData(formData, f.getName(), f.getValue());
      }
    }
    String url = UrlUtils.getUploadLocation();
    url += "FormAction";
    processAction(this, url, formData, SpMobil.getUserToken(), getApplicationInstance().getId(),
        currentAction, currentRole, currentState, event.getProcessId());
  }

  @Override
  public void roleChanged(final WorkflowRoleChangeEvent workflowRoleChangeEvent) {
    this.currentRole = workflowRoleChangeEvent.getRole();
    loadWorkflowInstances(workflowRoleChangeEvent, 0);
  }

  public void actionProcessed() {
    EventBus.getInstance().fireEvent(new WorkflowActionProcessedEvent());
  }

  public void actionNotProcessed(int error) {
    EventBus.getInstance().fireEvent(new ErrorEvent(new RequestException("Error " + error)));
  }

  private static native void processAction(WorkflowApp app, String url, JavaScriptObject fd,
      String token, String instanceId, String currentAction, final String currentRole,
      String currentState, String processId) /*-{
    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, false);
    xhr.setRequestHeader("X-Silverpeas-Session", token);
    xhr.onreadystatechange = function() {
      if (xhr.readyState == 4 && xhr.status == 200) {
        // Every thing ok, file uploaded
        app.@org.silverpeas.mobile.client.apps.workflow.WorkflowApp::actionProcessed()();
      } else {
        app.@org.silverpeas.mobile.client.apps.workflow.WorkflowApp::actionNotProcessed(I)(
            xhr.status);
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
