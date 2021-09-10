/*
 * Copyright (C) 2000 - 2021 Silverpeas
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

package org.silverpeas.mobile.client.apps.workflow.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.silverpeas.mobile.client.apps.workflow.events.app.WorkflowLoadActionFormEvent;
import org.silverpeas.mobile.client.apps.workflow.resources.WorkflowMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.base.ActionItem;

/**
 * @author: svu
 */
public class ActionButton extends ActionItem {
    interface ActionButtonUiBinder extends UiBinder<HTMLPanel, ActionButton> {
    }

    private static ActionButtonUiBinder uiBinder = GWT.create(ActionButtonUiBinder.class);

    @UiField  HTMLPanel container;
    @UiField  Anchor action;

    @UiField(provided = true) protected WorkflowMessages msg = null;
    private String instanceId, actionCode, actionLabel, state;

    public ActionButton() {
        msg = GWT.create(WorkflowMessages.class);
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void init(String instanceId, String actionCode, String actionLabel, String state) {
        this.instanceId = instanceId;
        this.actionCode = actionCode;
        this.actionLabel = actionLabel;
        this.state = state;
        action.setText(actionLabel);
    }

    @UiHandler("action")
    void executeAction(ClickEvent event){
      WorkflowLoadActionFormEvent ev = new WorkflowLoadActionFormEvent();
      ev.setActionName(actionCode);
      ev.setInstanceId(instanceId);
      ev.setState(state);
      EventBus.getInstance().fireEvent(ev);
        // hide menu
        getElement().getParentElement().removeAttribute("style");
    }

}