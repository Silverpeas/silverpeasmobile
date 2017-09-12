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

package com.silverpeas.mobile.client.apps.workflow.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.silverpeas.mobile.client.apps.blog.events.app.BlogLoadEvent;
import com.silverpeas.mobile.client.apps.blog.events.pages.AbstractBlogPagesEvent;
import com.silverpeas.mobile.client.apps.blog.events.pages.BlogLoadedEvent;
import com.silverpeas.mobile.client.apps.blog.events.pages.BlogPagesEventHandler;
import com.silverpeas.mobile.client.apps.blog.resources.BlogMessages;
import com.silverpeas.mobile.client.apps.favorites.pages.widgets.AddToFavoritesButton;
import com.silverpeas.mobile.client.apps.workflow.events.pages.AbstractWorkflowPagesEvent;
import com.silverpeas.mobile.client.apps.workflow.events.pages.WorkflowPagesEventHandler;
import com.silverpeas.mobile.client.apps.workflow.pages.widgets.WorkflowItem;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.components.UnorderedList;
import com.silverpeas.mobile.client.components.base.ActionsMenu;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.shared.dto.ContentsTypes;
import com.silverpeas.mobile.shared.dto.blog.PostDTO;
import com.silverpeas.mobile.shared.dto.workflow.WorkflowInstanceDTO;

import java.util.List;

public class WorkflowPage extends PageContent implements WorkflowPagesEventHandler {

  private static WorkflowPageUiBinder uiBinder = GWT.create(WorkflowPageUiBinder.class);

  @UiField UnorderedList instances;
  @UiField ActionsMenu actionsMenu;

  private AddToFavoritesButton favorite = new AddToFavoritesButton();
  private List<WorkflowInstanceDTO> data;

  public void setData(final List<WorkflowInstanceDTO> data) {
    this.data = data;
    for (WorkflowInstanceDTO d : data) {
      WorkflowItem item = new WorkflowItem();
      item.setData(d);
      instances.add(item);
    }
  }

  interface WorkflowPageUiBinder extends UiBinder<Widget, WorkflowPage> {
  }

  public WorkflowPage() {
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().addHandler(AbstractWorkflowPagesEvent.TYPE, this);
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractWorkflowPagesEvent.TYPE, this);
  }
}