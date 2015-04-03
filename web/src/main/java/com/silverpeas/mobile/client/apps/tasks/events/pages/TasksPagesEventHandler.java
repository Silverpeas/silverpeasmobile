package com.silverpeas.mobile.client.apps.tasks.events.pages;

import com.google.gwt.event.shared.EventHandler;

public interface TasksPagesEventHandler extends EventHandler{
  void onTaskLoad(TasksLoadedEvent event);

  void onTaskCreated(TaskCreatedEvent taskCreatedEvent);
}
