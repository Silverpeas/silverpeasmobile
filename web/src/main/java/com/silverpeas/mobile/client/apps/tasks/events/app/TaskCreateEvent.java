package com.silverpeas.mobile.client.apps.tasks.events.app;

import com.silverpeas.mobile.shared.dto.TaskDTO;

public class TaskCreateEvent extends AbstractTasksAppEvent {

  private TaskDTO task;

  public TaskCreateEvent(TaskDTO task){
    super();
    this.task = task;
  }

  @Override
  protected void dispatch(TasksAppEventHandler handler) {
    handler.createTask(this);
  }

  public TaskDTO getTask() {
    return task;
  }
}
