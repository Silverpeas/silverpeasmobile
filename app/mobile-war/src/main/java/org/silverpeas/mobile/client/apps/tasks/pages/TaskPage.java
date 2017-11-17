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

package org.silverpeas.mobile.client.apps.tasks.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import org.silverpeas.mobile.client.apps.tasks.events.app.TaskCreateEvent;
import org.silverpeas.mobile.client.apps.tasks.events.app.TaskUpdateEvent;
import org.silverpeas.mobile.client.apps.tasks.resources.TasksMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.TaskDTO;

/**
 * @author: svu
 */
public class TaskPage extends PageContent {
  interface TaskPageUiBinder extends UiBinder<HTMLPanel, TaskPage> {}

  @UiField TextBox range;
  @UiField TextArea name;
  @UiField HTMLPanel container;
  @UiField Anchor submit;
  @UiField Label percent;
  private TaskDTO data;

  @UiField(provided = true) protected TasksMessages msg = null;


  private static TaskPageUiBinder uiBinder = GWT.create(TaskPageUiBinder.class);

  public TaskPage() {
    msg = GWT.create(TasksMessages.class);
    initWidget(uiBinder.createAndBindUi(this));
    container.getElement().setId("update-statut");
    range.getElement().setAttribute("type", "range");
    range.getElement().setAttribute("min", "0");
    range.getElement().setAttribute("max", "100");
    range.getElement().setAttribute("step", "5");
    range.getElement().setAttribute("value", "0");
    addListenerInput(range.getElement(), this);
  }

  public native void addListenerInput(Element range, TaskPage page) /*-{
    range.addEventListener('input', function () {
      page.@org.silverpeas.mobile.client.apps.tasks.pages.TaskPage::updateRange(I)(range.value);
    }, false);
  }-*/;

  private void updateRange(final int value) {
    double val = value / 100.0;
    String css = "background-image: -webkit-gradient(linear, 0% 0%, 100% 0%, color-stop(" + val + ", rgb(114, 171, 14)), color-stop(" + val + ", rgb(197, 197, 197)));";
    range.getElement().setAttribute("style", css);
    range.getElement().setAttribute("value", String.valueOf(value));
    percent.setText(String.valueOf(value) + " %");
  }

  private void updateModel() {
    if (data == null) data = new TaskDTO();
    data.setName(name.getText());
    data.setPercentCompleted(Integer.parseInt(range.getText()));
  }

  @UiHandler("submit")
  void changeTask(ClickEvent event)  {
    if (!name.getText().isEmpty()) {

      if (data == null) {
        updateModel();
        EventBus.getInstance().fireEvent(new TaskCreateEvent(data));
        back();
      } else {
       updateModel();
        EventBus.getInstance().fireEvent(new TaskUpdateEvent(data));
        back();
      }
    }
  }

  public void setData(TaskDTO data) {
    this.data = data;
    name.setText(data.getName());
    updateRange(data.getPercentCompleted());

    submit.setHTML("<span class=\"ui-btn-text\">" + msg.actionEdit() + "</span>");
  }

}