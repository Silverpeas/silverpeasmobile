package com.silverpeas.mobile.server.services;

import com.silverpeas.mobile.server.common.SpMobileLogModule;
import com.silverpeas.mobile.server.dao.StatusDao;
import com.silverpeas.mobile.shared.dto.StatusDTO;
import com.silverpeas.mobile.shared.dto.TaskDTO;
import com.silverpeas.mobile.shared.exceptions.AuthenticationException;
import com.silverpeas.mobile.shared.exceptions.RSEexception;
import com.silverpeas.mobile.shared.exceptions.Taskexception;
import com.silverpeas.mobile.shared.services.ServiceRSE;
import com.silverpeas.mobile.shared.services.ServiceTasks;
import com.silverpeas.socialnetwork.status.Status;
import com.silverpeas.socialnetwork.status.StatusService;
import com.stratelia.silverpeas.silvertrace.SilverTrace;
import com.stratelia.webactiv.beans.admin.UserDetail;
import com.stratelia.webactiv.calendar.control.SilverpeasCalendar;
import com.stratelia.webactiv.calendar.model.ToDoHeader;
import com.stratelia.webactiv.util.EJBUtilitaire;
import com.stratelia.webactiv.util.JNDINames;
import com.sun.xml.bind.v2.TODO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Service de gestion des tâches.
 * @author svu
 */
public class ServiceTasksImpl extends AbstractAuthenticateService implements ServiceTasks {

  private static final long serialVersionUID = 1L;


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

  private TaskDTO populate(ToDoHeader todo) {
    TaskDTO task = new TaskDTO();
    task.setName(todo.getDescription());
    return task;
  }

  private SilverpeasCalendar getCalendar() {
    try {
      return EJBUtilitaire.getEJBObjectRef(JNDINames.CALENDARBM_EJBHOME, SilverpeasCalendar.class);
    } catch(Exception e) {
      return null;
    }
  }
}
