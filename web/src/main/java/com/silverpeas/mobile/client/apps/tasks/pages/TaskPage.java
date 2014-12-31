package com.silverpeas.mobile.client.apps.tasks.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.silverpeas.mobile.client.apps.tasks.events.app.TaskCreateEvent;
import com.silverpeas.mobile.client.apps.tasks.events.app.TaskUpdateEvent;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.shared.dto.TaskDTO;

/**
 * @author: svu
 */
public class TaskPage extends PageContent {
  interface TaskPageUiBinder extends UiBinder<HTMLPanel, TaskPage> {}

  @UiField TextBox range, name;


  private static TaskPageUiBinder uiBinder = GWT.create(TaskPageUiBinder.class);

  public TaskPage() {
    initWidget(uiBinder.createAndBindUi(this));
    range.getElement().setAttribute("type", "range");
    range.getElement().setAttribute("min", "0");
    range.getElement().setAttribute("max", "100");
    range.getElement().setAttribute("step", "5");
    range.getElement().setAttribute("value", "0");
  }

  @UiHandler("create")
  void changePercent(ClickEvent event)  {
    TaskDTO task = new TaskDTO();
    task.setName(name.getText());
    task.setPercentCompleted(Integer.parseInt(range.getText()));
    EventBus.getInstance().fireEvent(new TaskCreateEvent(task));
    back();
  }

}