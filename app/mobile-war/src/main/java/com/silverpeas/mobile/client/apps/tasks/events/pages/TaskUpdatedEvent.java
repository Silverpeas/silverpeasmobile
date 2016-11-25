package com.silverpeas.mobile.client.apps.tasks.events.pages;

import com.silverpeas.mobile.shared.dto.TaskDTO;

public class TaskUpdatedEvent extends AbstractTasksPagesEvent {

  private TaskDTO task;

  public TaskUpdatedEvent(TaskDTO task){
    super();
    this.task = task;
  }

  @Override
  protected void dispatch(TasksPagesEventHandler handler) {
    handler.onTaskUpdated(this);
  }

  public TaskDTO getTask() {
    return task;
  }
}
