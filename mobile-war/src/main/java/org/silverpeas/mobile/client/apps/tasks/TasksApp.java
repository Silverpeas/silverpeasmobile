/*
 * Copyright (C) 2000 - 2025 Silverpeas
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

package org.silverpeas.mobile.client.apps.tasks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import org.fusesource.restygwt.client.Method;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.AbstractNavigationEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationAppInstanceChangedEvent;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationEventHandler;
import org.silverpeas.mobile.client.apps.navigation.events.app.external.NavigationShowContentEvent;
import org.silverpeas.mobile.client.apps.tasks.events.app.*;
import org.silverpeas.mobile.client.apps.tasks.events.pages.TaskCreatedEvent;
import org.silverpeas.mobile.client.apps.tasks.events.pages.TaskUpdatedEvent;
import org.silverpeas.mobile.client.apps.tasks.events.pages.TasksLoadedEvent;
import org.silverpeas.mobile.client.apps.tasks.pages.TasksPage;
import org.silverpeas.mobile.client.common.EventBus;
import org.silverpeas.mobile.client.common.Notification;
import org.silverpeas.mobile.client.common.ServicesLocator;
import org.silverpeas.mobile.client.common.app.App;
import org.silverpeas.mobile.client.common.network.MethodCallbackOnlineOnly;
import org.silverpeas.mobile.client.resources.ApplicationMessages;
import org.silverpeas.mobile.shared.dto.ContentsTypes;
import org.silverpeas.mobile.shared.dto.TaskDTO;

import java.util.List;

public class TasksApp extends App implements TasksAppEventHandler, NavigationEventHandler {

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

    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<List<TaskDTO>>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceTasks().loadTasks(this);
      }

      @Override
      public void onSuccess(final Method method, final List<TaskDTO> tasks) {
        super.onSuccess(method, tasks);
        EventBus.getInstance().fireEvent(new TasksLoadedEvent(tasks));
      }
    };
    action.attempt();
  }

  @Override
  public void updateTask(final TaskUpdateEvent event) {

    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<Void>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceTasks().updateTask(event.getTask(), this);
      }

      @Override
      public void onSuccess(final Method method, final Void unused) {
        super.onSuccess(method, unused);
        EventBus.getInstance().fireEvent(new TaskUpdatedEvent(event.getTask()));
      }
    };
    action.attempt();
  }

  @Override
  public void createTask(final TaskCreateEvent event) {

    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<TaskDTO>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceTasks().createTask(event.getTask(), this);
      }

      @Override
      public void onSuccess(final Method method, final TaskDTO taskDTO) {
        super.onSuccess(method, taskDTO);
        EventBus.getInstance().fireEvent(new TaskCreatedEvent(taskDTO));
      }
    };
    action.attempt();
  }

  @Override
  public void deleteTask(TasksDeleteEvent event) {
    MethodCallbackOnlineOnly action = new MethodCallbackOnlineOnly<Void>() {
      @Override
      public void attempt() {
        super.attempt();
        ServicesLocator.getServiceTasks().deleteTasks(event.getTasks(), this);
      }
      @Override
      public void onSuccess(Method method, Void unused) {
        super.onSuccess(method, unused);
        loadTasks(new TasksLoadEvent());
      }
    };
    action.attempt();

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
