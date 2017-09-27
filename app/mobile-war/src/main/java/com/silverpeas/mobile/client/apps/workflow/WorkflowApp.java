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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import com.silverpeas.mobile.client.apps.navigation.events.app.external
    .NavigationAppInstanceChangedEvent;
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
import com.silverpeas.mobile.client.components.userselection.events.pages.AllowedUsersAndGroupsLoadedEvent;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.BaseDTO;
import com.silverpeas.mobile.shared.dto.navigation.ApplicationInstanceDTO;
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
    ServicesLocator.getServiceWorkflow()
        .getInstances(instance.getId(), event.getRole(), new AsyncCallback<WorkflowInstancesDTO>() {
          @Override
          public void onFailure(final Throwable throwable) {
            EventBus.getInstance().fireEvent(new ErrorEvent(throwable));
          }

          @Override
          public void onSuccess(final WorkflowInstancesDTO dto) {
            WorkflowLoadedInstancesEvent event = new WorkflowLoadedInstancesEvent();
            if (currentRole == null) {
              currentRole = dto.getRoles().entrySet().iterator().next().getKey();
            }
            event.setData(dto);
            event.setInstanceId(instance.getId());
            EventBus.getInstance().fireEvent(event);
          }
        });
  }

  @Override
  public void loadInstance(final WorkflowLoadInstanceEvent event) {
    currentRole = event.getRole();
    ServicesLocator.getServiceWorkflow().getPresentationForm(event.getId(), event.getRole(),
        new AsyncCallback<WorkflowInstancePresentationFormDTO>() {
          @Override
          public void onFailure(final Throwable throwable) {
            EventBus.getInstance().fireEvent(new ErrorEvent(throwable));
          }

          @Override
          public void onSuccess(final WorkflowInstancePresentationFormDTO form) {
            WorkflowPresentationPage page = new WorkflowPresentationPage();
            page.setData(form, instance);
            page.show();
          }
        });

  }

  @Override
  public void loadActionForm(final WorkflowLoadActionFormEvent event) {
    currentAction = event.getActionName();
    currentState = event.getState();
    ServicesLocator.getServiceWorkflow().getActionForm(event.getInstanceId(), currentRole, event.getActionName(), new AsyncCallback<WorkflowFormActionDTO>() {
      @Override
      public void onFailure(final Throwable throwable) {
        EventBus.getInstance().fireEvent(new ErrorEvent(throwable));
      }

      @Override
      public void onSuccess(final WorkflowFormActionDTO dto) {
        dto.setActionName(currentAction);
        WorkflowActionFormPage page = new WorkflowActionFormPage();
        page.setData(dto, instance);
        page.show();
      }
    });

  }

  @Override
  public void loadUserField(final WorkflowLoadUserFieldEvent event) {
    ServicesLocator.getServiceWorkflow().getUserField(event.getInstanceId(), event.getFieldName(), currentRole,
        new AsyncCallback<List<BaseDTO>>() {
          @Override
          public void onFailure(final Throwable throwable) {
            EventBus.getInstance().fireEvent(new ErrorEvent(throwable));
          }

          @Override
          public void onSuccess(final List<BaseDTO> users) {
            AllowedUsersAndGroupsLoadedEvent ev = new AllowedUsersAndGroupsLoadedEvent(users);
            EventBus.getInstance().fireEvent(ev);
          }
        });
  }

  @Override
  public void processForm(final WorkflowProcessFormEvent event) {
    ServicesLocator.getServiceWorkflow().processAction(event.getData(), instance.getId(), currentAction, currentRole, currentState, event.getProcessId(), new AsyncCallback<Void>() {
      @Override
      public void onFailure(final Throwable throwable) {
        EventBus.getInstance().fireEvent(new ErrorEvent(throwable));
      }

      @Override
      public void onSuccess(final Void aVoid) {
        EventBus.getInstance().fireEvent(new WorkflowActionProcessedEvent());
      }
    });
  }
}
