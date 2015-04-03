package com.silverpeas.mobile.client.apps.tasks.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.silverpeas.mobile.client.apps.contacts.pages.widgets.ContactItem;
import com.silverpeas.mobile.client.apps.status.events.pages.AbstractStatusPagesEvent;
import com.silverpeas.mobile.client.apps.tasks.events.app.TasksLoadEvent;
import com.silverpeas.mobile.client.apps.tasks.events.pages.AbstractTasksPagesEvent;
import com.silverpeas.mobile.client.apps.tasks.events.pages.TaskCreatedEvent;
import com.silverpeas.mobile.client.apps.tasks.events.pages.TasksLoadedEvent;
import com.silverpeas.mobile.client.apps.tasks.events.pages.TasksPagesEventHandler;
import com.silverpeas.mobile.client.apps.tasks.pages.widgets.AddTaskItem;
import com.silverpeas.mobile.client.apps.tasks.pages.widgets.TaskItem;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.components.UnorderedList;
import com.silverpeas.mobile.client.components.base.PageContent;
import com.silverpeas.mobile.shared.dto.DetailUserDTO;
import com.silverpeas.mobile.shared.dto.TaskDTO;

import java.util.Iterator;

/**
 * @author: svu
 */
public class TasksPage extends PageContent implements TasksPagesEventHandler {


  interface TasksPageUiBinder extends UiBinder<HTMLPanel, TasksPage> { }
  private static TasksPageUiBinder uiBinder = GWT.create(TasksPageUiBinder.class);

  @UiField HTMLPanel container;
  @UiField UnorderedList list;

  public TasksPage() {
    initWidget(uiBinder.createAndBindUi(this));
    EventBus.getInstance().fireEvent(new TasksLoadEvent());
    EventBus.getInstance().addHandler(AbstractTasksPagesEvent.TYPE, this);
  }

  @Override
  public void onTaskLoad(final TasksLoadedEvent event) {
    Notification.activityStop();


    list.add(new AddTaskItem());

    Iterator<TaskDTO> i = event.getTasks().iterator();
    while (i.hasNext()) {
      TaskDTO task = i.next();
      if (task != null) {
        TaskItem item = new TaskItem();
        item.setData(task);
        list.add(item);
      }
    }
  }

  @Override
  public void onTaskCreated(final TaskCreatedEvent taskCreatedEvent) {
    TaskItem item = new TaskItem();
    item.setData(taskCreatedEvent.getTask());
    list.add(item);
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractTasksPagesEvent.TYPE, this);
  }
}