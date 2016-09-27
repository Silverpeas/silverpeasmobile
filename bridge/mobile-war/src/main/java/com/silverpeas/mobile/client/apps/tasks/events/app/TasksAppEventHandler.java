package com.silverpeas.mobile.client.apps.tasks.events.app;

import com.google.gwt.event.shared.EventHandler;

public interface TasksAppEventHandler extends EventHandler{
  void loadTasks(TasksLoadEvent event);
  void updateTask(TaskUpdateEvent event);
  void createTask(TaskCreateEvent event);
}
