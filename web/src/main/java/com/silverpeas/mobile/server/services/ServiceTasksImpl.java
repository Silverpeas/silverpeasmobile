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
    //TODO
    return null;
  }
}
