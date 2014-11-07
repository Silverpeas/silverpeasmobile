package com.silverpeas.mobile.client.apps.tasks.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.silverpeas.mobile.client.apps.status.events.pages.AbstractStatusPagesEvent;
import com.silverpeas.mobile.client.apps.tasks.events.app.TasksLoadEvent;
import com.silverpeas.mobile.client.apps.tasks.events.pages.AbstractTasksPagesEvent;
import com.silverpeas.mobile.client.apps.tasks.events.pages.TasksLoadedEvent;
import com.silverpeas.mobile.client.apps.tasks.events.pages.TasksPagesEventHandler;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.components.base.PageContent;

/**
 * @author: svu
 */
public class TasksPage extends PageContent implements TasksPagesEventHandler {


 interface TasksPageUiBinder extends UiBinder<HTMLPanel, TasksPage> { }

  private static TasksPageUiBinder uiBinder = GWT.create(TasksPageUiBinder.class);

  public TasksPage() {
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().fireEvent(new TasksLoadEvent());
  }

  @Override
  public void onTaskLoad(final TasksLoadedEvent event) {
    //TODO
    Window.alert("Coming soon");
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractTasksPagesEvent.TYPE, this);
  }
}