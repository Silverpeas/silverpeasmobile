package com.silverpeas.mobile.client.apps.tasks.events.app;

import com.silverpeas.mobile.shared.dto.TaskDTO;

public class TaskUpdateEvent extends AbstractTasksAppEvent {

  private TaskDTO task;

  public TaskUpdateEvent(TaskDTO task){
    super();
    this.task = task;
  }

  @Override
  protected void dispatch(TasksAppEventHandler handler) {
    handler.updateTask(this);
  }

  public TaskDTO getTask() {
    return task;
  }
}
