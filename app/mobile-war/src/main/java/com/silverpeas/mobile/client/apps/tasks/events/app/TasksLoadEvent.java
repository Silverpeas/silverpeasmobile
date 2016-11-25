package com.silverpeas.mobile.client.apps.tasks.events.app;

public class TasksLoadEvent extends AbstractTasksAppEvent {

  public TasksLoadEvent(){
    super();
  }

  @Override
  protected void dispatch(TasksAppEventHandler handler) {
    handler.loadTasks(this);
  }
}
