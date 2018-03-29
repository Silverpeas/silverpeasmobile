/*
 * Copyright (C) 2000 - 2017 Silverpeas
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.silverpeas.mobile.server.services;

import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.personalorganizer.model.Attendee;
import org.silverpeas.core.personalorganizer.model.ToDoHeader;
import org.silverpeas.core.personalorganizer.service.SilverpeasCalendar;
import org.silverpeas.core.util.LocalizationBundle;
import org.silverpeas.core.util.ResourceLocator;
import org.silverpeas.core.util.ServiceProvider;
import org.silverpeas.mobile.shared.dto.TaskDTO;
import org.silverpeas.mobile.shared.exceptions.AuthenticationException;
import org.silverpeas.mobile.shared.exceptions.Taskexception;
import org.silverpeas.mobile.shared.services.ServiceTasks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Service de gestion des tâches.
 * @author svu
 */
public class ServiceTasksImpl extends AbstractAuthenticateService implements ServiceTasks {

  private static final long serialVersionUID = 1L;
  private OrganizationController organizationController = OrganizationController.get();
  private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
  private LocalizationBundle todoMultilang = null;



  private LocalizationBundle getTodoMultiLang() {
    if (todoMultilang == null) {
      todoMultilang = ResourceLocator.getLocalizationBundle("org.silverpeas.todo.multilang.todo", getUserInSession().getUserPreferences().getLanguage());
    }
    return todoMultilang;
  }

  @Override
  public List<TaskDTO> loadTasks() throws Taskexception, AuthenticationException {
    checkUserInSession();
    ArrayList<TaskDTO> tasks = new ArrayList<TaskDTO>();
    Collection<ToDoHeader> todos = getCalendar().getNotCompletedToDosForUser(getUserInSession().getId());
    for (ToDoHeader todo : todos) {
      tasks.add(populate(todo));
    }
    return tasks;
  }

  @Override
  public void updateTask(TaskDTO task) throws Taskexception, AuthenticationException {
    checkUserInSession();
    ToDoHeader todo = getCalendar().getToDoHeader(String.valueOf(task.getId()));
    todo.setName(task.getName());
    todo.setPercentCompleted(task.getPercentCompleted());
    getCalendar().updateToDo(todo);
  }

  @Override
  public TaskDTO createTask(final TaskDTO task) throws Taskexception, AuthenticationException {
    checkUserInSession();
    ToDoHeader todo = new ToDoHeader(task.getName(), getUserInSession().getId());
    todo.setPercentCompleted(task.getPercentCompleted());
    String id = getCalendar().addToDo(todo);
    Attendee attendee = new Attendee(getUserInSession().getId());
    getCalendar().addToDoAttendee(id, attendee);

    ToDoHeader todoCreated = getCalendar().getToDoHeader(String.valueOf(id));
    return populate(todoCreated);
  }

  private TaskDTO populate(ToDoHeader todo) {
    TaskDTO task = new TaskDTO();
    task.setId(Integer.parseInt(todo.getId()));
    task.setName(todo.getName());

    int percent = todo.getPercentCompleted();
    if (percent == -1) percent = 0;
    task.setPercentCompleted(percent);
    task.setPriority(getTodoMultiLang().getString("priorite" + todo.getPriority().getValue()));
    if (todo.getEndDate() != null) {
      task.setEndDate(sdf.format(todo.getEndDate()));
    }
    UserDetail delegator = organizationController.getUserDetail(todo.getDelegatorId());
    task.setDelegator(delegator.getDisplayedName());

    if (todo.getExternalId() != null) {
      task.setExternalId(todo.getExternalId());
    } else {
      task.setExternalId("");
    }
    return task;
  }

  private SilverpeasCalendar getCalendar() {
    return ServiceProvider.getService(SilverpeasCalendar.class);
  }
}
