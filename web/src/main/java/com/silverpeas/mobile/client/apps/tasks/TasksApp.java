package com.silverpeas.mobile.client.apps.tasks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.client.apps.tasks.events.app.AbstractTasksAppEvent;
import com.silverpeas.mobile.client.apps.tasks.events.app.TaskCreateEvent;
import com.silverpeas.mobile.client.apps.tasks.events.app.TaskUpdateEvent;
import com.silverpeas.mobile.client.apps.tasks.events.app.TasksAppEventHandler;
import com.silverpeas.mobile.client.apps.tasks.events.app.TasksLoadEvent;
import com.silverpeas.mobile.client.apps.tasks.events.pages.TaskCreatedEvent;
import com.silverpeas.mobile.client.apps.tasks.events.pages.TasksLoadedEvent;
import com.silverpeas.mobile.client.apps.tasks.pages.TasksPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.event.ErrorEvent;
import com.silverpeas.mobile.client.common.network.OfflineHelper;
import com.silverpeas.mobile.client.common.storage.LocalStorageHelper;
import com.silverpeas.mobile.client.resources.ApplicationMessages;
import com.silverpeas.mobile.shared.dto.TaskDTO;

import java.util.ArrayList;
import java.util.List;

public class TasksApp extends App implements TasksAppEventHandler {

  public static final String TASKS_KEY = "tasks";
  private static ApplicationMessages msg;

  public TasksApp(){
    super();
    msg = GWT.create(ApplicationMessages.class);
    EventBus.getInstance().addHandler(AbstractTasksAppEvent.TYPE, this);
  }

  public void start(){
    setMainPage(new TasksPage());
    super.start();
  }

  @Override
  public void stop() {
    EventBus.getInstance().removeHandler(AbstractTasksAppEvent.TYPE, this);
    super.stop();
  }

  @Override
  public void loadTasks(final TasksLoadEvent event) {
    Notification.activityStart();
    ServicesLocator.getServiceTasks().loadTasks(new AsyncCallback<List<TaskDTO>>() {
      @Override
      public void onFailure(final Throwable caught) {
        if (OfflineHelper.needToGoOffine(caught)) {
          List<TaskDTO> tasks = loadInLocalStorage();
          EventBus.getInstance().fireEvent(new TasksLoadedEvent(tasks));
        } else {
          EventBus.getInstance().fireEvent(new ErrorEvent(caught));
        }
      }

      @Override
      public void onSuccess(final List<TaskDTO> tasks) {
        storeInLocalStorage(tasks);
        EventBus.getInstance().fireEvent(new TasksLoadedEvent(tasks));
      }
    });
  }

  @Override
  public void updateTask(final TaskUpdateEvent event) {
    ServicesLocator.getServiceTasks().updateTask(event.getTask().getId(),
        event.getNewPercentCompleted(), new AsyncCallback<Void>() {

          @Override
          public void onFailure(final Throwable caught) {
            if (OfflineHelper.needToGoOffine(caught)) {
              Notification.alert(msg.needToBeOnline());
            } else {
              EventBus.getInstance().fireEvent(new ErrorEvent(caught));
            }
          }

          @Override
          public void onSuccess(final Void result) {
            // do nothing
          }
        });
  }

  @Override
  public void createTask(final TaskCreateEvent event) {
    ServicesLocator.getServiceTasks().createTask(event.getTask(), new AsyncCallback<TaskDTO>() {

      @Override
      public void onFailure(final Throwable caught) {
        if (OfflineHelper.needToGoOffine(caught)) {
          Notification.alert(msg.needToBeOnline());
        } else {
          EventBus.getInstance().fireEvent(new ErrorEvent(caught));
        }
      }

      @Override
      public void onSuccess(final TaskDTO result) {
        EventBus.getInstance().fireEvent(new TaskCreatedEvent(result));
      }
    });
  }

  private List<TaskDTO> loadInLocalStorage() {
    List<TaskDTO> tasks = LocalStorageHelper.load(TASKS_KEY, List.class);
    if (tasks == null) tasks = new ArrayList<TaskDTO>();
    return tasks;
  }

  private void storeInLocalStorage(final List<TaskDTO> result) {
    LocalStorageHelper.store("tasks", List.class , result);
  }
}
