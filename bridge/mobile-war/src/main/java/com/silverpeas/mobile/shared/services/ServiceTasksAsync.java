package com.silverpeas.mobile.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.silverpeas.mobile.shared.dto.TaskDTO;

import java.util.List;

public interface ServiceTasksAsync {
  void loadTasks(final AsyncCallback<List<TaskDTO>> async);

  void updateTask(TaskDTO task, final AsyncCallback<Void> async);

  void createTask(final TaskDTO task, final AsyncCallback<TaskDTO> async);
}
