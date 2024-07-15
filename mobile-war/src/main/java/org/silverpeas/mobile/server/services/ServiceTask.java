/*
 * Copyright (C) 2000 - 2024 Silverpeas
 *
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
 * "https://www.silverpeas.org/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.silverpeas.mobile.server.services;

import org.silverpeas.core.admin.service.OrganizationController;
import org.silverpeas.core.admin.user.model.UserDetail;
import org.silverpeas.core.annotation.WebService;
import org.silverpeas.core.personalorganizer.model.Attendee;
import org.silverpeas.core.personalorganizer.model.ToDoHeader;
import org.silverpeas.core.personalorganizer.service.SilverpeasCalendar;
import org.silverpeas.kernel.bundle.LocalizationBundle;
import org.silverpeas.kernel.bundle.ResourceLocator;
import org.silverpeas.core.util.ServiceProvider;
import org.silverpeas.core.web.rs.UserPrivilegeValidation;
import org.silverpeas.core.web.rs.annotation.Authorized;
import org.silverpeas.mobile.shared.dto.TaskDTO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@WebService
@Authorized
@Path(ServiceTask.PATH)
public class ServiceTask extends AbstractRestWebService {

  @Context
  HttpServletRequest request;

  static final String PATH = "mobile/personalTask";
  private OrganizationController organizationController = OrganizationController.get();
  private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
  private LocalizationBundle todoMultilang = null;

  private LocalizationBundle getTodoMultiLang() {
    if (todoMultilang == null) {
      todoMultilang = ResourceLocator
          .getLocalizationBundle("org.silverpeas.todo.multilang.todo", getUser().getUserPreferences().getLanguage());
    }
    return todoMultilang;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("all")
  public List<TaskDTO> loadTasks() {
    ArrayList<TaskDTO> tasks = new ArrayList<TaskDTO>();
    Collection<ToDoHeader> todos = getCalendar().getNotCompletedToDosForUser(getUser().getId());
    for (ToDoHeader todo : todos) {
      tasks.add(populate(todo));
    }
    return tasks;
  }

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("")
  public void updateTask(TaskDTO task) {
    ToDoHeader todo = getCalendar().getToDoHeader(String.valueOf(task.getId()));
    todo.setName(task.getName());
    todo.setPercentCompleted(task.getPercentCompleted());
    getCalendar().updateToDo(todo);
  }

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("")
  public void deleteTasks(List<TaskDTO> tasks) {
    for (TaskDTO task : tasks) {
      getCalendar().removeToDo(task.getId());
    }
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("")
  public TaskDTO createTask(TaskDTO task) {
    ToDoHeader todo = new ToDoHeader(task.getName(), getUser().getId());
    todo.setPercentCompleted(task.getPercentCompleted());
    String id = getCalendar().addToDo(todo);
    Attendee attendee = new Attendee(getUser().getId());
    getCalendar().addToDoAttendee(id, attendee);

    ToDoHeader todoCreated = getCalendar().getToDoHeader(String.valueOf(id));
    return populate(todoCreated);
  }

  private TaskDTO populate(ToDoHeader todo) {
    TaskDTO task = new TaskDTO();
    task.setId(todo.getId());
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

  @Override
  protected String getResourceBasePath() {
    return PATH;
  }

  @Override
  public String getComponentId() {
    return null;
  }

  @Override
  public void validateUserAuthorization(final UserPrivilegeValidation validation) {
  }

}
