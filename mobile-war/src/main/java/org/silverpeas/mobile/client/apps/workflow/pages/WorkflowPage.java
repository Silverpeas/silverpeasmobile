/*
 * Copyright (C) 2000 - 2018 Silverpeas
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

package org.silverpeas.mobile.client.apps.workflow.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.favorites.pages.widgets.AddToFavoritesButton;
import org.silverpeas.mobile.client.apps.workflow.events.app.WorkflowLoadInstanceEvent;
import org.silverpeas.mobile.client.apps.workflow.events.app.WorkflowLoadInstancesEvent;
import org.silverpeas.mobile.client.apps.workflow.events.app.WorkflowRoleChangeEvent;
import org.silverpeas.mobile.client.apps.workflow.events.pages.AbstractWorkflowPagesEvent;
import org.silverpeas.mobile.client.apps.workflow.events.pages.WorkflowActionProcessedEvent;
import org.silverpeas.mobile.client.apps.workflow.events.pages.WorkflowLoadedInstancesEvent;
import org.silverpeas.mobile.client.apps.workflow.events.pages.WorkflowPagesEventHandler;
import org.silverpeas.mobile.client.apps.workflow.pages.widgets.ActionButton;
import org.silverpeas.mobile.client.apps.workflow.resources.WorkflowMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.base.ActionsMenu;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowInstanceDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowInstancesDTO;

import java.util.Map;

public class WorkflowPage extends PageContent implements WorkflowPagesEventHandler, ChangeHandler {

  private static final String ACTION_CREATE = "ACTION_CREATE";
  private static WorkflowPageUiBinder uiBinder = GWT.create(WorkflowPageUiBinder.class);

  @UiField FlexTable instances;
  @UiField
  ActionsMenu actionsMenu;
  @UiField ListBox roles;

  private String instanceId;
  private WorkflowMessages msg;

  private AddToFavoritesButton favorite = new AddToFavoritesButton();
  private WorkflowInstancesDTO data = null;

  private ClickHandler clickOnInstance = new ClickHandler() {
    @Override
    public void onClick(final ClickEvent event) {
      Anchor link = (Anchor) event.getSource();
      String id = link.getElement().getAttribute("data");
      String role = roles.getSelectedValue();

      WorkflowLoadInstanceEvent loadevent = new WorkflowLoadInstanceEvent();
      loadevent.setId(id);
      loadevent.setRole(role);
      EventBus.getInstance().fireEvent(loadevent);
    }
  };

  @Override
  public void onChange(final ChangeEvent changeEvent) {
    creationGesture();
    WorkflowLoadInstancesEvent event = new WorkflowLoadInstancesEvent();
    event.setRole(roles.getSelectedValue());
    EventBus.getInstance().fireEvent(event);
  }

  @Override
  public void loadInstances(final WorkflowLoadedInstancesEvent event) {
    if (data == null) {
      this.instanceId = event.getInstanceId();
      this.data = event.getData();
      for (Map.Entry<String,String> role : data.getRoles().entrySet()) {
        roles.addItem(role.getValue(), role.getKey());
      }
      roles.setVisible(true);
      EventBus.getInstance().fireEvent(new WorkflowRoleChangeEvent(roles.getSelectedValue()));
    } else {
      instances.clear();
      this.data = event.getData();
    }

    int c = 0;
    for (String label : data.getHeaderLabels()) {
      instances.setHTML(0, c, label);
      c++;
    }

    int r = 1;
    for (WorkflowInstanceDTO d : data.getInstances()) {
      c = 0;
      for (String value : d.getHeaderFieldsValues()) {
        Anchor link = new Anchor();
        link.setHref("javaScript:;");
        link.setText(value);
        link.setStylePrimaryName("workflow-anchor");
        link.getElement().setId("inst" + r + c);
        link.getElement().setAttribute("data", d.getId());
        link.addClickHandler(clickOnInstance);
        instances.setWidget(r,c,link);
        c++;
      }
      r++;
    }

    creationGesture();
  }

  @Override
  public void actionProcessed(final WorkflowActionProcessedEvent ev) {
    WorkflowLoadInstancesEvent event = new WorkflowLoadInstancesEvent();
    event.setRole(roles.getSelectedValue());
    EventBus.getInstance().fireEvent(event);
  }

  private void creationGesture() {
    if (data.getRolesAllowedToCreate().contains(roles.getSelectedValue())) {
      ActionButton act = new ActionButton();
      act.setId(ACTION_CREATE);
      act.init(instanceId, "create", msg.create(), null);
      actionsMenu.addAction(act);
    } else {
      actionsMenu.removeAction(ACTION_CREATE);
    }
  }

  interface WorkflowPageUiBinder extends UiBinder<Widget, WorkflowPage> {
  }

  public WorkflowPage() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(WorkflowMessages.class);
    EventBus.getInstance().addHandler(AbstractWorkflowPagesEvent.TYPE, this);
    roles.addChangeHandler(this);
  }

  @UiHandler("roles")
  void changeTask(ChangeEvent event)  {
    EventBus.getInstance().fireEvent(new WorkflowRoleChangeEvent(roles.getSelectedValue()));
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractWorkflowPagesEvent.TYPE, this);
  }


}