package com.silverpeas.mobile.client.apps.tasks.events.pages;

import com.silverpeas.mobile.shared.dto.TaskDTO;

import java.util.List;

public class TasksLoadedEvent extends AbstractTasksPagesEvent {

  private List<TaskDTO> tasks;

  public TasksLoadedEvent(List<TaskDTO> tasks){
    super();
    this.tasks = tasks;
  }

  @Override
  protected void dispatch(TasksPagesEventHandler handler) {
    handler.onTaskLoad(this);
  }

}
