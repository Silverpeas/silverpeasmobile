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

package org.silverpeas.mobile.client.apps.tasks.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import org.silverpeas.mobile.client.apps.tasks.pages.TaskPage;
import org.silverpeas.mobile.client.apps.tasks.resources.TasksMessages;
import org.silverpeas.mobile.shared.dto.TaskDTO;

public class TaskItem extends Composite {

  private static ContactItemUiBinder uiBinder = GWT.create(ContactItemUiBinder.class);

  @UiField HTMLPanel container;
  @UiField SpanElement name, endDate, delegator, priority, percentCompleted;
  @UiField InputElement range;
  @UiField Anchor link;

  @UiField(provided = true) protected TasksMessages msg = null;

  private TaskDTO task;

  interface ContactItemUiBinder extends UiBinder<Widget, TaskItem> {
  }

  public TaskItem() {
    msg = GWT.create(TasksMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
  }

  public void setData(TaskDTO data) {
    this.task = data;
    name.setInnerHTML(data.getName());
    endDate.setInnerHTML(data.getEndDate());
    delegator.setInnerHTML(data.getDelegator());
    priority.setInnerHTML(data.getPriority() + "");
    percentCompleted.setInnerHTML(data.getPercentCompleted() + " %");
    if (!data.getExternalId().isEmpty()) {
      //TODO : display edit percent completed
      range.getStyle().setDisplay(Style.Display.NONE);
    }


    range.setAttribute("min", "0");
    range.setAttribute("max", "100");
    range.setAttribute("step", "5");
    range.setAttribute("value", String.valueOf(data.getPercentCompleted()));
    range.setDisabled(true);
    updateRange(data.getPercentCompleted());
  }

  public TaskDTO getData() {
    return task;
  }

  @UiHandler("link")
  protected void edit(ClickEvent event) {
    if (task.getExternalId().isEmpty()) {
      TaskPage page = new TaskPage();
      page.setPageTitle(msg.edit());
      page.setData(task);
      page.show();
    } else {
      //TODO : redirect on content
    }
  }

  private void updateRange(final int value) {
    double val = value / 100.0;
    String css = "background-image: -webkit-gradient(linear, 0% 0%, 100% 0%, color-stop(" + val + ", rgb(114, 171, 14)), color-stop(" + val + ", rgb(197, 197, 197)));";
    range.setAttribute("style", css);
  }


}
