package com.silverpeas.mobile.client.apps.tasks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.silverpeas.mobile.client.apps.tasks.events.app.AbstractTasksAppEvent;
import com.silverpeas.mobile.client.apps.tasks.events.app.TaskCreateEvent;
import com.silverpeas.mobile.client.apps.tasks.events.app.TaskUpdateEvent;
import com.silverpeas.mobile.client.apps.tasks.events.app.TasksAppEventHandler;
import com.silverpeas.mobile.client.apps.tasks.events.app.TasksLoadEvent;
import com.silverpeas.mobile.client.apps.tasks.events.pages.TaskCreatedEvent;
import com.silverpeas.mobile.client.apps.tasks.events.pages.TaskUpdatedEvent;
import com.silverpeas.mobile.client.apps.tasks.events.pages.TasksLoadedEvent;
import com.silverpeas.mobile.client.apps.tasks.pages.TasksPage;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.client.common.Notification;
import com.silverpeas.mobile.client.common.ServicesLocator;
import com.silverpeas.mobile.client.common.app.App;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOnly;
import com.silverpeas.mobile.client.common.network.AsyncCallbackOnlineOrOffline;
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

        AsyncCallbackOnlineOrOffline action = new AsyncCallbackOnlineOrOffline<List<TaskDTO>>(getOffLineAction()) {

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

    private Command getOffLineAction () {
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

}
