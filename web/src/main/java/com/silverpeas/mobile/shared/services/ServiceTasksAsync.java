package com.silverpeas.mobile.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.silverpeas.mobile.shared.dto.StatusDTO;
import com.silverpeas.mobile.shared.dto.TaskDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.RSEexception;
import com.silverpeas.mobile.shared.exceptions.Taskexception;

import java.util.List;

public interface ServiceTasksAsync {
  void loadTasks(final AsyncCallback<List<TaskDTO>> async);

  void updateTask(int id, String newPercentComplete, final AsyncCallback<Void> async);

  void createTask(final TaskDTO task, final AsyncCallback<Void> async);
}
