package com.silverpeas.mobile.client.apps.tasks.events.pages;

import com.silverpeas.mobile.shared.dto.TaskDTO;

import java.util.List;

public class TaskCreatedEvent extends AbstractTasksPagesEvent {

  private TaskDTO task;

  public TaskCreatedEvent(TaskDTO task){
    super();
    this.task = task;
  }

  @Override
  protected void dispatch(TasksPagesEventHandler handler) {
    handler.onTaskCreated(this);
  }

  public TaskDTO getTask() {
    return task;
  }
}
