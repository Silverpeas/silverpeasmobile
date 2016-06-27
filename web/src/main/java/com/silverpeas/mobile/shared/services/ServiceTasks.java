package com.silverpeas.mobile.shared.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.silverpeas.mobile.shared.dto.StatusDTO;
import com.silverpeas.mobile.shared.dto.TaskDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.RSEexception;
import com.silverpeas.mobile.shared.exceptions.Taskexception;

import java.util.List;

@RemoteServiceRelativePath("Tasks")
public interface ServiceTasks extends RemoteService {
  public List<TaskDTO> loadTasks() throws Taskexception, AuthenticationException;
  public void updateTask(TaskDTO task) throws Taskexception, AuthenticationException;
  public TaskDTO createTask(final TaskDTO task) throws Taskexception, AuthenticationException;
}
