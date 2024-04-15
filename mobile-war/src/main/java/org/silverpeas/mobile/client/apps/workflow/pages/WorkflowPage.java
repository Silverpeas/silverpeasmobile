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

package org.silverpeas.mobile.client.apps.workflow.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
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
import org.silverpeas.mobile.client.apps.workflow.events.app.WorkflowRoleChangeEvent;
import org.silverpeas.mobile.client.apps.workflow.events.pages.AbstractWorkflowPagesEvent;
import org.silverpeas.mobile.client.apps.workflow.events.pages.WorkflowActionProcessedEvent;
import org.silverpeas.mobile.client.apps.workflow.events.pages.WorkflowLoadedDataEvent;
import org.silverpeas.mobile.client.apps.workflow.events.pages.WorkflowLoadedInstancesEvent;
import org.silverpeas.mobile.client.apps.workflow.events.pages.WorkflowPagesEventHandler;
import org.silverpeas.mobile.client.apps.workflow.pages.widgets.ActionButton;
import org.silverpeas.mobile.client.apps.workflow.resources.WorkflowMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.StreamingList;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowDataDTO;
import org.silverpeas.mobile.shared.dto.workflow.WorkflowInstanceDTO;

import java.util.Map;

public class WorkflowPage extends PageContent implements WorkflowPagesEventHandler {

  private static final String ACTION_CREATE = "ACTION_CREATE";
  private static WorkflowPageUiBinder uiBinder = GWT.create(WorkflowPageUiBinder.class);

  @UiField
  FlexTable instances;
  @UiField
  ListBox roles;

  private String instanceId;
  private WorkflowMessages msg;

  private AddToFavoritesButton favorite = new AddToFavoritesButton();
  private StreamingList<WorkflowInstanceDTO> data = null;
  private WorkflowDataDTO metaData = null;

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
  public void loadDataInstances(final WorkflowLoadedDataEvent event) {
    this.instanceId = event.getInstanceId();
    this.metaData = event.getData();
    for (Map.Entry<String, String> role : metaData.getRoles().entrySet()) {
      roles.addItem(role.getValue(), role.getKey());
    }
    roles.setVisible(roles.getItemCount()>1);
    EventBus.getInstance().fireEvent(new WorkflowRoleChangeEvent(roles.getSelectedValue()));
  }

  @Override
  public void loadInstances(final WorkflowLoadedInstancesEvent event) {
    if (this.data == null) {
      this.data = event.getData();
    } else {
      this.data.getList().addAll(event.getData().getList());
    }

    instances.removeAllRows();
    int c = 0;
    for (String label : metaData.getHeaderLabels().get(roles.getSelectedValue())) {
      instances.setHTML(0, c, label);
      c++;
    }

    int r = 1;
    for (WorkflowInstanceDTO d : data.getList()) {
      c = 0;
      for (String value : d.getHeaderFieldsValues()) {
        Anchor link = new Anchor();
        link.setHref("javaScript:;");
        link.setText(value);
        link.setStylePrimaryName("workflow-anchor");
        link.getElement().setId("inst" + r + c);
        link.getElement().setAttribute("data", d.getId());
        link.addClickHandler(clickOnInstance);
        instances.setWidget(r, c, link);
        c++;
      }
      r++;
    }

    creationGesture();
  }

  @Override
  public void actionProcessed(final WorkflowActionProcessedEvent ev) {
    resetWorkflowInstancesList();
    EventBus.getInstance().fireEvent(new WorkflowRoleChangeEvent(roles.getSelectedValue()));
  }

  private void creationGesture() {
    if (metaData.getRolesAllowedToCreate().contains(roles.getSelectedValue())) {
      ActionButton act = new ActionButton();
      act.setId(ACTION_CREATE);
      act.init(instanceId, "create", msg.create(), null);
      addActionShortcut(act);
      //addActionMenu(act);
    } else {
      ActionButton act = new ActionButton();
      act.setId(ACTION_CREATE);
      removeActionShortcut(act);
      //removeActionMenu(act);
    }
  }

  interface WorkflowPageUiBinder extends UiBinder<Widget, WorkflowPage> {}

  public WorkflowPage() {
    initWidget(uiBinder.createAndBindUi(this));
    msg = GWT.create(WorkflowMessages.class);
    EventBus.getInstance().addHandler(AbstractWorkflowPagesEvent.TYPE, this);
  }

  @UiHandler("roles")
  void changeTask(ChangeEvent event) {
    resetWorkflowInstancesList();
    EventBus.getInstance().fireEvent(new WorkflowRoleChangeEvent(roles.getSelectedValue()));
  }

  private void resetWorkflowInstancesList() {
    instances.clear();
    this.data = null;
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractWorkflowPagesEvent.TYPE, this);
  }


}