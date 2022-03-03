/*
 * Copyright (C) 2000 - 2022 Silverpeas
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

package org.silverpeas.mobile.client.apps.tasks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.apps.tasks.events.app.AbstractTasksAppEvent;
import org.silverpeas.mobile.client.apps.tasks.events.app.TaskCreateEvent;
import org.silverpeas.mobile.client.apps.tasks.events.app.TaskUpdateEvent;
import org.silverpeas.mobile.client.apps.tasks.events.app.TasksAppEventHandler;
import org.silverpeas.mobile.client.apps.tasks.events.app.TasksLoadEvent;
import org.silverpeas.mobile.client.apps.tasks.events.pages.TaskCreatedEvent;
import org.silverpeas.mobile.client.apps.tasks.events.pages.TaskUpdatedEvent;
import org.silverpeas.mobile.client.apps.tasks.events.pages.TasksLoadedEvent;
import org.silverpeas.mobile.client.apps.tasks.pages.TasksPage;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOnly;
import org.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
import org.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.TaskDTO;

import java.util.ArrayList;
import java.util.List;

public class TasksApp extends App implements TasksAppEventHandler, NavigationEventHandler {

  public static final String TASKS_KEY = "tasks";
  private static ApplicationMessages msg;

  public TasksApp() {
    super();
    msg = GWT.create(ApplicationMessages.class);
    EventBus.getInstance().addHandler(AbstractTasksAppEvent.TYPE, this);
    EventBus.getInstance().addHandler(AbstractNavigationEvent.TYPE, this);
  }

  public void start() {
    // no "super.start(lauchingPage);" this apps is used in another apps
  }

  @Override
  public void stop() {
    // never stop
  }

  @Override
  public void loadTasks(final TasksLoadEvent event) {
    Notification.activityStart();

    AsyncCallbackOnlineOrOffline action =
        new AsyncCallbackOnlineOrOffline<List<TaskDTO>>(getOffLineAction()) {

          @Override
          public void attempt() {
            ServicesLocator.getServiceTasks().loadTasks(this);
          }

          @Override
          public void onSuccess(final List<TaskDTO> tasks) {
            super.onSuccess(tasks);
            LocalStorageHelper.store("tasks", List.class, tasks);
            EventBus.getInstance().fireEvent(new TasksLoadedEvent(tasks));
          }
        };
    action.attempt();
  }

  @Override
  public void updateTask(final TaskUpdateEvent event) {
    AsyncCallbackOnlineOnly action = new AsyncCallbackOnlineOnly<Void>() {

      @Override
      public void attempt() {
        ServicesLocator.getServiceTasks().updateTask(event.getTask(), this);
      }

      @Override
      public void onSuccess(final Void result) {
        super.onSuccess(result);
        EventBus.getInstance().fireEvent(new TaskUpdatedEvent(event.getTask()));
      }
    };
    action.attempt();
  }

  @Override
  public void createTask(final TaskCreateEvent event) {
    AsyncCallbackOnlineOnly action = new AsyncCallbackOnlineOnly<TaskDTO>() {

      @Override
      public void attempt() {
        ServicesLocator.getServiceTasks().createTask(event.getTask(), this);
      }

      @Override
      public void onSuccess(final TaskDTO result) {
        super.onSuccess(result);
        EventBus.getInstance().fireEvent(new TaskCreatedEvent(result));
      }
    };
    action.attempt();
  }

  private Command getOffLineAction() {
    Command offlineAction = new Command() {
      @Override
      public void execute() {
        List<TaskDTO> tasks = LocalStorageHelper.load(TASKS_KEY, List.class);
        if (tasks == null) {
          tasks = new ArrayList<TaskDTO>();
        }
        EventBus.getInstance().fireEvent(new TasksLoadedEvent(tasks));
      }
    };
    return offlineAction;
  }

  @Override
  public void appInstanceChanged(final NavigationAppInstanceChangedEvent event) {
  }

  @Override
  public void showContent(final NavigationShowContentEvent event) {
    if (event.getContent().getType().equals(ContentsTypes.Tasks.toString())) {
      TasksPage page = new TasksPage();
      setMainPage(page);
      page.show();
    }
  }
}
