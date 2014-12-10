package com.silverpeas.mobile.client.apps.tasks.events.app;

import com.silverpeas.mobile.shared.dto.TaskDTO;

public class TaskUpdateEvent extends AbstractTasksAppEvent {

  private String newPercentCompleted;
  private TaskDTO task;

  public TaskUpdateEvent(TaskDTO task, String newPercentCompleted){
    super();
    this.newPercentCompleted = newPercentCompleted;
    this.task = task;
  }

  @Override
  protected void dispatch(TasksAppEventHandler handler) {
    handler.updateTask(this);
  }

  public String getNewPercentCompleted() {
    return newPercentCompleted;
  }

  public TaskDTO getTask() {
    return task;
  }
}
