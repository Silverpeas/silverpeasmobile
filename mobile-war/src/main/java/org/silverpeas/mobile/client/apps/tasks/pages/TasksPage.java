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

package org.silverpeas.mobile.client.apps.tasks.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.silverpeas.mobile.client.apps.tasks.events.app.TasksDeleteEvent;
import org.silverpeas.mobile.client.apps.tasks.events.app.TasksLoadEvent;
import org.silverpeas.mobile.client.apps.tasks.events.pages.AbstractTasksPagesEvent;
import org.silverpeas.mobile.client.apps.tasks.events.pages.TaskCreatedEvent;
import org.silverpeas.mobile.client.apps.tasks.events.pages.TaskUpdatedEvent;
import org.silverpeas.mobile.client.apps.tasks.events.pages.TasksLoadedEvent;
import org.silverpeas.mobile.client.apps.tasks.events.pages.TasksPagesEventHandler;
import org.silverpeas.mobile.client.apps.tasks.pages.widgets.AddTaskButton;
import org.silverpeas.mobile.client.apps.tasks.pages.widgets.DeleteTaskButton;
import org.silverpeas.mobile.client.apps.tasks.pages.widgets.TaskItem;
import org.silverpeas.mobile.client.apps.tasks.resources.TasksMessages;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.components.PopinConfirmation;
import org.silverpeas.mobile.client.components.UnorderedList;
import org.silverpeas.mobile.client.components.base.PageContent;
import org.silverpeas.mobile.shared.dto.TaskDTO;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author: svu
 */
public class TasksPage extends PageContent implements TasksPagesEventHandler {

  interface TasksPageUiBinder extends UiBinder<HTMLPanel, TasksPage> { }
  private static TasksPageUiBinder uiBinder = GWT.create(TasksPageUiBinder.class);
  private AddTaskButton buttonCreate = new AddTaskButton();

  private DeleteTaskButton buttonDelete = new DeleteTaskButton();
  @UiField HTMLPanel container;
  @UiField
  UnorderedList list;

  private TasksMessages msg = GWT.create(TasksMessages .class);

  public TasksPage() {
    initWidget(uiBinder.createAndBindUi(this));
    list.getElement().setId("tasks");
    EventBus.getInstance().fireEvent(new TasksLoadEvent());
    EventBus.getInstance().addHandler(AbstractTasksPagesEvent.TYPE, this);
  }

  @Override
  public void onTaskLoad(final TasksLoadedEvent event) {
    Notification.activityStop();
    list.clear();
    clearActions();
    addActionShortcut(buttonCreate);
    Iterator<TaskDTO> i = event.getTasks().iterator();
    while (i.hasNext()) {
      TaskDTO task = i.next();
      if (task != null) {
        TaskItem item = new TaskItem();
        item.setParent(this);
        item.setData(task);
        list.add(item);
      }
    }
  }

  @Override
  public void onTaskCreated(final TaskCreatedEvent taskCreatedEvent) {
    TaskItem item = new TaskItem();
    item.setParent(this);
    item.setData(taskCreatedEvent.getTask());
    list.add(item);
  }

  @Override
  public void onTaskUpdated(TaskUpdatedEvent taskUpdatedEvent) {
    int i = 0;
    while (i < list.getWidgetCount()) {
      if (list.getWidget(i) instanceof TaskItem) {
        TaskItem t = (TaskItem) list.getWidget(i);
        if (t.getData().getId() == taskUpdatedEvent.getTask().getId()) {
          list.remove(t);
          TaskItem item = new TaskItem();
          item.setData(taskUpdatedEvent.getTask());
          list.add(item);
          break;
        }
      }
      i++;
    }
  }

  @Override
  public void stop() {
    super.stop();
    EventBus.getInstance().removeHandler(AbstractTasksPagesEvent.TYPE, this);
  }

  private void deleteSelectedTasks() {
    PopinConfirmation popin = new PopinConfirmation(msg.deleteConfirmation());
    popin.setYesCallback(new Command() {
      @Override
      public void execute() {
        ArrayList<TaskDTO> selection = new ArrayList<>();
        for (int i = 0; i < list.getCount(); i++) {
          TaskItem item = (TaskItem) list.getWidget(i);
          if (item.isSelected()) {
            selection.add(item.getData());
          }
        }
        EventBus.getInstance().fireEvent(new TasksDeleteEvent(selection));
      }
    });
    popin.show();
  }

  @Override
  public void setSelectionMode(boolean selectionMode) {
    super.setSelectionMode(selectionMode);

    if (selectionMode) {
      clearActions();
      buttonDelete.setCallback(new Command() {@Override public void execute() {deleteSelectedTasks();}});
      addActionShortcut(buttonDelete);
    } else {
      clearActions();
      addActionShortcut(buttonCreate);
    }
    for (int i = 0; i < list.getCount(); i++) {
      TaskItem item = (TaskItem) list.getWidget(i);
      item.setSelectionMode(selectionMode);
    }
  }
}